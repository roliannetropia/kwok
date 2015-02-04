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
package com.kwoksys.action.issueplugin;

import com.kwoksys.action.issues.IssueForm;
import com.kwoksys.biz.ServiceProvider;
import com.kwoksys.biz.admin.core.AdminUtils;
import com.kwoksys.biz.issues.IssueService;
import com.kwoksys.biz.issues.core.IssueUtils;
import com.kwoksys.biz.issues.dto.Issue;
import com.kwoksys.biz.system.core.AppPaths;
import com.kwoksys.biz.system.core.AttributeManager;
import com.kwoksys.biz.system.core.Attributes;
import com.kwoksys.biz.system.core.Keywords;
import com.kwoksys.biz.system.core.configs.ConfigManager;
import com.kwoksys.framework.connections.mail.EmailMessage;
import com.kwoksys.framework.connections.mail.Smtp;
import com.kwoksys.framework.http.RequestContext;
import com.kwoksys.framework.properties.Localizer;
import com.kwoksys.framework.struts2.Action2;
import org.apache.struts.action.ActionMessages;

/**
 * Action class for reporting a new issue.
 */
public class IssueAdd2Action extends Action2 {

    public String execute() throws Exception {
        // Get request parameters
        IssueForm actionForm = saveActionForm(new IssueForm());
        Issue issue = new Issue();
        issue.setSubject(actionForm.getSubject());
        issue.setDescription(actionForm.getDescription());
        issue.setType(actionForm.getType());
        issue.setPriority(actionForm.getPriority());
        issue.setCreatorIP(request.getRemoteAddr());

        IssueService issueService = ServiceProvider.getIssueService(requestContext);

        // Add issue
        ActionMessages errors = issueService.addIssueSimple(issue);
        if (!errors.isEmpty()) {
            saveActionErrors(errors);
            return redirect(AppPaths.ISSUE_PLUGIN_ADD + "?" + RequestContext.URL_PARAM_ERROR_TRUE);
        } else {
            redirect(AppPaths.ISSUE_PLUGIN_ADD_3 + "?issueId=" + issue.getId());

            // Send out an email.
            if (ConfigManager.email.isEmailNotificationOn()) {
                EmailMessage message = new EmailMessage();

                // Set FROM field
                message.setFromField(ConfigManager.email.getSmtpFrom());

                // Set TO field
                message.getToField().add(ConfigManager.email.getSmtpTo());

                // Here, we need to get the issue again because otherwise, we don't have creator name and creation date.
                issue = issueService.getPublicIssue(issue.getId());

                // Set SUBJECT field
                message.setSubjectField(IssueUtils.formatEmailSubject(requestContext, issue));

                AttributeManager attributeManager = new AttributeManager(requestContext);

                String emailBody = ConfigManager.email.getIssueReportEmailTemplate().isEmpty() ?
                        Localizer.getText(requestContext, "issuePlugin.issueAdd2.emailBody") : ConfigManager.email.getIssueReportEmailTemplate();

                emailBody = emailBody.replace(Keywords.ISSUE_ID_VAR, String.valueOf(issue.getId()))
                        .replace(Keywords.ISSUE_REPORTED_BY_VAR, AdminUtils.getSystemUsername(requestContext, issue.getCreator()))
                        .replace(Keywords.ISSUE_REPORTED_ON_VAR, issue.getCreationDate())
                        .replace(Keywords.ISSUE_STATUS_VAR, attributeManager.getAttrFieldNameCache(Attributes.ISSUE_STATUS, issue.getStatus()))
                        .replace(Keywords.ISSUE_PRIORITY_VAR, attributeManager.getAttrFieldNameCache(Attributes.ISSUE_PRIORITY, issue.getPriority()))
                        .replace(Keywords.ISSUE_TYPE_VAR, attributeManager.getAttrFieldNameCache(Attributes.ISSUE_TYPE, issue.getType()))
                        .replace(Keywords.ISSUE_DESCRIPTION_VAR, issue.getDescription())
                        .replace(Keywords.ISSUE_URL_VAR, ConfigManager.system.getAppUrl() + AppPaths.ISSUES_DETAIL + "?issueId=" + issue.getId());

                // Set BODY field
                message.setBodyField(IssueUtils.formatEmailBody(requestContext, emailBody));

                Smtp.send(message);
            }
            return null;
        }
    }
}
