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
package com.kwoksys.action.issues;

import com.kwoksys.biz.ServiceProvider;
import com.kwoksys.biz.admin.core.AdminUtils;
import com.kwoksys.biz.admin.dto.Attribute;
import com.kwoksys.biz.issues.IssueService;
import com.kwoksys.biz.issues.core.IssueUtils;
import com.kwoksys.biz.issues.dto.Issue;
import com.kwoksys.biz.system.core.*;
import com.kwoksys.biz.system.core.configs.ConfigManager;
import com.kwoksys.framework.http.RequestContext;
import com.kwoksys.framework.properties.Localizer;
import com.kwoksys.framework.struts2.Action2;
import org.apache.struts.action.ActionMessages;

import java.util.Map;

/**
 * Action class for editing issue.
 */
public class IssueEdit2Action extends Action2 {

    public String execute() throws Exception {
        IssueService issueService = ServiceProvider.getIssueService(requestContext);

        IssueForm actionForm = saveActionForm(new IssueForm());
        Issue issue = issueService.getIssue(actionForm.getIssueId());
        Integer prevAssigneeId = issue.getAssignee().getId();
        issue.setSubject(actionForm.getSubject());
        issue.setFollowup(actionForm.getFollowup());
        issue.setStatus(actionForm.getStatus());
        issue.setType(actionForm.getType());
        issue.setPriority(actionForm.getPriority());
        issue.setResolution(actionForm.getResolution());
        issue.getAssignee().setId(actionForm.getAssignedTo());
        issue.setSelectedSubscribers(actionForm.getSelectedSubscribers());
        issue.setFromEmail("");

        issue.setHasDueDate(actionForm.getHasDueDate() == 1);
        if (actionForm.getHasDueDate() == 1) {
            issue.setDueDate(actionForm.getDueDateYear(), actionForm.getDueDateMonth(), actionForm.getDueDateDate());
        }

        // Get custom field values from request
        AttributeManager attributeManager = new AttributeManager(requestContext);
        Map<Integer, Attribute> customAttributes = attributeManager.getCustomFieldMap(ObjectTypes.ISSUE);
        AdminUtils.populateCustomFieldValues(requestContext, actionForm, issue, customAttributes);

        // Perform the update and see if there is any error.
        ActionMessages errors = issueService.updateIssue(issue, customAttributes, true);
        if (!errors.isEmpty()) {
            saveActionErrors(errors);
            return redirect(AppPaths.ISSUES_EDIT + "?issueId=" + issue.getId() + "&" + RequestContext.URL_PARAM_ERROR_TRUE);

        } else {
            redirect(AppPaths.ISSUES_DETAIL + "?issueId=" + issue.getId());

            // Send an email to assignee/subscribers.
            if (ConfigManager.email.isEmailNotificationOn() && actionForm.getSuppressNotification() != 1) {
                // Here, we need to get the issue again because otherwise, we don't have correct modifier name and
                // modification date.
                Issue modifiedIssue = issueService.getPublicIssue(issue.getId());

                String emailBody = ConfigManager.email.getIssueUpdateEmailTemplate().isEmpty() ?
                                        Localizer.getText(requestContext, "issues.issueEdit2.emailBody") : ConfigManager.email.getIssueUpdateEmailTemplate();

                emailBody = emailBody.replace(Keywords.ISSUE_ASSIGNEE_VAR, AdminUtils.getSystemUsername(requestContext, issue.getAssignee()))
                        .replace(Keywords.ISSUE_ID_VAR, String.valueOf(issue.getId()))
                        .replace(Keywords.ISSUE_REPORTED_BY_VAR, AdminUtils.getSystemUsername(requestContext, issue.getCreator()))
                        .replace(Keywords.ISSUE_REPORTED_ON_VAR, issue.getCreationDate())
                        .replace(Keywords.ISSUE_STATUS_VAR, attributeManager.getAttrFieldNameCache(Attributes.ISSUE_STATUS, issue.getStatus()))
                        .replace(Keywords.ISSUE_PRIORITY_VAR, attributeManager.getAttrFieldNameCache(Attributes.ISSUE_PRIORITY, issue.getPriority()))
                        .replace(Keywords.ISSUE_TYPE_VAR, attributeManager.getAttrFieldNameCache(Attributes.ISSUE_TYPE, issue.getType()))
                        .replace(Keywords.ISSUE_DESCRIPTION_VAR, issue.getDescription())
                        .replace(Keywords.ISSUE_COMMENT_VAR, issue.getFollowup())
                        .replace(Keywords.ISSUE_COMMENTED_BY_VAR, AdminUtils.getSystemUsername(requestContext, modifiedIssue.getModifier())
                        .replace(Keywords.ISSUE_COMMENTED_DATE_VAR, modifiedIssue.getModificationDate())
                        .replace(Keywords.ISSUE_URL_VAR, ConfigManager.system.getAppUrl() + AppPaths.ISSUES_DETAIL + "?issueId=" + issue.getId()));

                IssueUtils.sendMail(requestContext, modifiedIssue, prevAssigneeId, emailBody);
            }
            return null;
        }
    }
}
