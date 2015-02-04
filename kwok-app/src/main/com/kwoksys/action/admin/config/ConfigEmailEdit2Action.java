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
package com.kwoksys.action.admin.config;

import com.kwoksys.biz.ServiceProvider;
import com.kwoksys.biz.admin.AdminService;
import com.kwoksys.biz.admin.core.AdminUtils;
import com.kwoksys.biz.admin.dto.SystemConfig;
import com.kwoksys.biz.system.core.configs.SystemConfigNames;
import com.kwoksys.framework.connections.mail.Smtp;
import com.kwoksys.framework.connections.mail.EmailMessage;
import com.kwoksys.framework.connections.mail.SmtpConnection;
import com.kwoksys.biz.system.core.AppPaths;
import com.kwoksys.framework.properties.Localizer;
import com.kwoksys.framework.http.RequestContext;
import com.kwoksys.framework.struts2.Action2;
import com.kwoksys.framework.util.StringUtils;
import org.apache.struts.action.*;

import java.util.ArrayList;
import java.util.List;

/**
 * Action class for editing notification configuratons.
 */
public class ConfigEmailEdit2Action extends Action2 {

    public String execute() throws Exception {
        ConfigForm actionForm = saveActionForm(new ConfigForm());
        String password = actionForm.getSmtpPassword();

        ActionMessages errors;

        if (actionForm.isTest()) {
            EmailMessage message = new EmailMessage();

            // Set FROM field
            message.setFromField(actionForm.getSmtpFrom());

            // Set TO field
            message.getToField().add(actionForm.getSmtpTo());

            // Set SUBJECT field
            message.setSubjectField(Localizer.getText(requestContext, "admin.config.email.test.subject"));

            // Set BODY field
            message.setBodyField(Localizer.getText(requestContext, "admin.config.email.test.body"));

            SmtpConnection conn = new SmtpConnection();
            conn.setHost(actionForm.getSmtpHost());
            conn.setPort(actionForm.getSmtpPort());
            conn.setUsername(actionForm.getSmtpUsername());
            conn.setStarttls(actionForm.getSmtpStarttls());

            // Only use the password given in submit form if it's not empty.
            if (!password.isEmpty()) {
                conn.setPassword(actionForm.getSmtpPassword());
            }

            errors = Smtp.send(message, conn);
            if (errors.isEmpty()) {
                errors.add("sendSuccess", new ActionMessage("admin.config.email.success", actionForm.getSmtpTo()));
            }

            saveActionErrors(errors);
            return redirect(AppPaths.ADMIN_CONFIG_WRITE + "?cmd=" + AdminUtils.ADMIN_EMAIL_EDIT_CMD + "&" + RequestContext.URL_PARAM_ERROR_TRUE);
        }

        List list = new ArrayList();
        list.add(new SystemConfig(SystemConfigNames.EMAIL_NOTIFICATION, actionForm.getNotificationMethod()));
        list.add(new SystemConfig(SystemConfigNames.EMAIL_FILTER, actionForm.getDomainFiltering()));
        list.add(new SystemConfig(SystemConfigNames.EMAIL_ALLOWED_DOMAINS, actionForm.getAllowedDomains()));
        list.add(new SystemConfig(SystemConfigNames.SMTP_HOST, actionForm.getSmtpHost()));
        list.add(new SystemConfig(SystemConfigNames.SMTP_PORT, actionForm.getSmtpPort()));
        list.add(new SystemConfig(SystemConfigNames.SMTP_USERNAME, actionForm.getSmtpUsername()));
        list.add(new SystemConfig(SystemConfigNames.SMTP_FROM, actionForm.getSmtpFrom()));
        list.add(new SystemConfig(SystemConfigNames.SMTP_TO, actionForm.getSmtpTo()));
        list.add(new SystemConfig(SystemConfigNames.SMTP_STARTTLS, actionForm.getSmtpStarttls()));
        list.add(new SystemConfig(SystemConfigNames.ISSUE_REPORT_EMAIL_TEMPLATE, actionForm.getReportIssueEmailTemplate()));
        list.add(new SystemConfig(SystemConfigNames.ISSUE_ADD_EMAIL_TEMPLATE, actionForm.getIssueAddEmailTemplate()));
        list.add(new SystemConfig(SystemConfigNames.ISSUE_UPDATE_EMAIL_TEMPLATE, actionForm.getIssueUpdateEmailTemplate()));

        if (!password.isEmpty()) {
            list.add(new SystemConfig(SystemConfigNames.SMTP_PASSWORD, StringUtils.encodeBase64Codec(password)));
        }

        AdminService adminService = ServiceProvider.getAdminService(requestContext);

        errors = adminService.updateConfig(list);
        if (!errors.isEmpty()) {
            saveActionErrors(errors);
            return redirect(AppPaths.ADMIN_CONFIG_WRITE + "?cmd=" + AdminUtils.ADMIN_EMAIL_EDIT_CMD + "&" + RequestContext.URL_PARAM_ERROR_TRUE);

        } else {
            return redirect(AppPaths.ADMIN_CONFIG + "?cmd=" + AdminUtils.ADMIN_EMAIL_CMD);
        }
    }
}
