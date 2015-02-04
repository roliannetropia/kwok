/*
 * ====================================================================
 * Copyright 2005-2011 Wai-Lun Kwok
 *
 * http://www.kwoksys.com/LICENSE
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 * ====================================================================
 */
package com.kwoksys.framework.connections.mail;

import com.kwoksys.biz.system.core.configs.ConfigManager;
import com.kwoksys.framework.util.StringUtils;
import org.apache.struts.action.ActionMessage;
import org.apache.struts.action.ActionMessages;

import javax.mail.*;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;
import java.util.Date;
import java.util.Properties;
import java.util.logging.Logger;
import java.util.logging.Level;

/**
 * SMTP class
 */
public class Smtp {

    private static final Logger logger = Logger.getLogger(Smtp.class.getName());

    private Smtp() {}

    public static ActionMessages send(EmailMessage message) throws Exception {
        return send(message, new SmtpConnection());
    }

    public static ActionMessages send(EmailMessage message, SmtpConnection conn) throws Exception {
        // Lots of email provider require pop-authenticated.
        Properties properties = new Properties();
        properties.put("mail.smtp.host", conn.getHost());
        properties.put("mail.smtp.port", conn.getPort());
        properties.put("mail.smtp.auth", String.valueOf(conn.isAuthRequired()));
        properties.put("mail.smtp.timeout", "4000");
        properties.put("mail.smtp.starttls.enable", conn.getStarttls());

        // Convert from our EmailMessage class to MimeMessage class
        MimeMessage msg = new MimeMessage(Session.getInstance(properties,
                new MailAuthenticator(conn.getUsername(), conn.getPassword())));
        msg.addHeader("Content-Type", "charset=UTF-8");
        msg.setSentDate(new Date());

        msg.setFrom(message.getFromField().isEmpty() ? null : new InternetAddress(message.getFromField()));

        for (String to : message.getToField()) {
            if (emailAddressAllowed(to)) {
                msg.addRecipients(Message.RecipientType.TO, to);
            }
        }

        for (String cc : message.getCcField()) {
            if (emailAddressAllowed(cc)) {
                msg.addRecipients(Message.RecipientType.CC, cc);
            }
        }

        msg.setSubject(StringUtils.encodeBase64Codec(message.getSubjectField()));

        msg.setText(message.getBodyField(), ConfigManager.system.getCharacterEncoding());

        ActionMessages errors = new ActionMessages();

        // Make sure we have some kind of recepients before trying to send.
        if (msg.getAllRecipients() != null) {
            try {
                Transport.send(msg);
                logger.info("Successfully sent an email message to: " + joinList(msg.getAllRecipients(), ","));

            } catch (javax.mail.AuthenticationFailedException e) {
                errors.add("sendMail", new ActionMessage("admin.config.email.error.authentication"));
                logger.severe("Failed to send mail: Authentication error. Please make sure username and password are correct.");

            } catch (Exception e) {
                errors.add("sendMail", new ActionMessage("admin.config.email.error", e.getClass(), e.getMessage()));
                logger.log(Level.SEVERE, "Failed to send mail to: " + joinList(msg.getAllRecipients(), ","), e);
            }
        }
        return errors;
    }

    /**
     * Checks whether an email is allowed to be sent out.
     * Only receipient matching out pre-defined domains to go out.
     *
     * @return ..
     */
    public static boolean emailAddressAllowed(String receipient) {
        if (ConfigManager.email.getEmailDomainFiltering() == 1) {
            // We don't block emails going out, we're good now.
            return true;
        }
        for (String allowedDomain : ConfigManager.email.getAllowedDomainOptions()) {
            if (receipient.indexOf(allowedDomain.trim()) > 0) {
                return true;
            }
        }
        logger.info("Email to " + receipient + " was blocked because it's not in allowed domain list.");
        return false;
    }

    /**
     * Joins a list of email addresses into a string.
     * @param addresses
     * @param token
     * @return
     */
    public static String joinList(Address[] addresses, String token) {
        StringBuilder result = new StringBuilder();
        for (Address address : addresses) {
            if (result.length() != 0) {
                result.append(token).append(" ");
            }
            result.append(address.toString());
        }
        return result.toString();
    }
}
