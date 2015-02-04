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

import com.kwoksys.biz.issues.IssueService;
import com.kwoksys.framework.parsers.email.IssueEmailParser;
import com.kwoksys.framework.util.StringUtils;
import org.apache.struts.action.ActionMessages;

import javax.mail.*;
import javax.mail.internet.MimeMultipart;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Properties;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * POP
 */
public class Pop {

    private static final Logger logger = Logger.getLogger(Pop.class.getName());

    public static List<EmailMessage> receive(PopConnection conn, IssueService issueService) throws Exception {
        Properties props = new Properties();
        props.put("mail.pop3.host", conn.getHost());
        props.put("mail.pop3.port", conn.getPort());
        props.put("mail.pop3.timeout", "10000");

        if (conn.isSslEnabled()) {
            props.put("mail.pop3.ssl.enable", Boolean.toString(conn.isSslEnabled()));
            props.put("mail.pop3.socketFactory.class", "javax.net.ssl.SSLSocketFactory");
            props.put("mail.pop3.socketFactory.fallback", "false");
            props.put("mail.pop3.socketFactory.port", conn.getPort());
        }

        // Setup authentication, get session
        Session session = Session.getInstance(props, new MailAuthenticator(conn.getUsername(), conn.getPassword()));

        List messages = new ArrayList();
        Folder folder = null;
        Store store = null;

        try {
            // Get store
            store = session.getStore("pop3");
            store.connect();

            // Get folder
            folder = store.getFolder("INBOX");
            folder.open(Folder.READ_WRITE);

            // Get messages
            Message[] message = folder.getMessages();
            logger.info("Number of messages in issue mailbox: " + message.length);
            for (int i=0; (i<message.length && i<conn.getMessagesLimit()); i++) {
                try {
                    EmailMessage emailMessage = new EmailMessage();
                    emailMessage.setFromField(IssueEmailParser.parseEmailAddress(message[i].getFrom()[0].toString()));
                    emailMessage.setSubjectField(StringUtils.replaceNull(message[i].getSubject()));

                    Object content = message[i].getContent();
                    StringBuilder bodyText = new StringBuilder();

                    if (content instanceof MimeMultipart) {
                        MimeMultipart mp = (MimeMultipart) content;

                        for (int j = 0; j < mp.getCount(); j++) {
                            BodyPart bodyPart = mp.getBodyPart(j);
                            String disposition = bodyPart.getDisposition();

                            if (disposition != null && (disposition.equals(BodyPart.ATTACHMENT))) {
                            } else {
                                String text = getText(bodyPart);
                                if (text != null && bodyText.toString().isEmpty()) {
                                    bodyText.append(text);
                                }
                            }
                        }
                    } else {
                        bodyText.append(StringUtils.replaceNull(content));
                    }
                    emailMessage.setBodyField(IssueEmailParser.parseEmailBody(bodyText.toString().trim()));

                    message[i].setFlag(Flags.Flag.DELETED, true);

                    if (ignoreSender(conn.getSenderIgnoreList().split("\n"), emailMessage.getFromField())) {
                        logger.info("Ignoring email from: " + emailMessage.getFromField());
                    } else {
                        ActionMessages errors = issueService.retrieveIssueEmail(emailMessage);
                        if (!errors.isEmpty()) {
                            throw new Exception("Problem adding issues.");
                        }
                        messages.add(emailMessage);
                    }
                } catch (Exception e) {
                    logger.log(Level.WARNING, "Failed to receive email " + i, e);
                }
            }
        } catch (Exception e) {
            logger.log(Level.SEVERE, "Failed to receive emails", e);
            throw new Exception(e);

        } finally {
            // Close connection, and delete messages marked DELETED
            if (folder != null) {
                folder.close(true);
            }
            if (store != null) {
                store.close();
            }
        }
        return messages;
    }

    public static boolean ignoreSender(String[] senderList, String from) {
        for (String ignoreSender : senderList) {
            if (ignoreSender.trim().equals(from)) {
                return true;
            }
        }
        return false;
    }

    /**
     * Return the primary text content of the message. From "JAVAMAIL API FAQ" sample.
     * http://www.oracle.com/technetwork/java/faq-135477.html
     */
    private static String getText(Part p) throws MessagingException, IOException {
        if (p.isMimeType("text/*")) {
            return (String)p.getContent();
        }

        if (p.isMimeType("multipart/alternative")) {
            // Prefer plain text over html text
            Multipart mp = (Multipart)p.getContent();
            String text = null;
            for (int i = 0; i < mp.getCount(); i++) {
                Part bp = mp.getBodyPart(i);
                if (bp.isMimeType("text/html")) {
                    if (text == null) {
                        text = getText(bp);
                    }
                } else if (bp.isMimeType("text/plain")) {
                    String s = getText(bp);
                    if (s != null)
                        return s;
                } else {
                    return getText(bp);
                }
            }
            return text;
        } else if (p.isMimeType("multipart/*")) {
            Multipart mp = (Multipart)p.getContent();
            for (int i = 0; i < mp.getCount(); i++) {
                String s = getText(mp.getBodyPart(i));
                if (s != null)
                    return s;
            }
        }
        return null;
    }
}
