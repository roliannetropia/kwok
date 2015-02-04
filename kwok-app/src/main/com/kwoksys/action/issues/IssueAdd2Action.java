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
import com.kwoksys.biz.system.core.*;
import com.kwoksys.biz.auth.core.Permissions;
import com.kwoksys.biz.admin.core.AdminUtils;
import com.kwoksys.biz.admin.dto.AccessUser;
import com.kwoksys.biz.admin.dto.Attribute;
import com.kwoksys.biz.issues.IssueService;
import com.kwoksys.biz.issues.core.IssueUtils;
import com.kwoksys.biz.issues.dto.Issue;
import com.kwoksys.biz.system.core.configs.ConfigManager;
import com.kwoksys.framework.properties.Localizer;
import com.kwoksys.framework.struts2.Action2;
import com.kwoksys.framework.util.NumberUtils;
import com.kwoksys.framework.http.RequestContext;
import org.apache.struts.action.ActionMessages;

import java.util.Map;

/**
 * Action class for adding issue.
 */
public class IssueAdd2Action extends Action2 {

    public String execute() throws Exception {
        AccessUser accessUser = requestContext.getUser();

        IssueForm actionForm = saveActionForm(new IssueForm());
        Issue issue = new Issue();
        issue.setSubject(actionForm.getSubject());
        issue.setDescription(actionForm.getDescription());
        issue.setStatus(actionForm.getStatus());
        issue.setType(actionForm.getType());
        issue.setPriority(actionForm.getPriority());
        issue.setResolution(actionForm.getResolution());
        issue.getAssignee().setId(actionForm.getAssignedTo());
        issue.setSelectedSubscribers(actionForm.getSelectedSubscribers());

        if (accessUser.hasPermission(Permissions.ISSUE_PROXY_SUBMIT)) {
            issue.setProxyUserId(actionForm.getCreator());
        }
        issue.setCreatorIP(request.getRemoteAddr());

        issue.setHasDueDate(actionForm.getHasDueDate() == 1);
        if (actionForm.getHasDueDate() == 1) {
            issue.setDueDate(actionForm.getDueDateYear(), actionForm.getDueDateMonth(), actionForm.getDueDateDate());
        }

        // Get custom field values from request
        AttributeManager attributeManager = new AttributeManager(requestContext);
        Map<Integer, Attribute> customAttributes = attributeManager.getCustomFieldMap(ObjectTypes.ISSUE);
        AdminUtils.populateCustomFieldValues(requestContext, actionForm, issue, customAttributes);
        
        IssueService issueService = ServiceProvider.getIssueService(requestContext);

        // Add the issue
        ActionMessages errors = issueService.addIssue(issue, customAttributes);
        if (!errors.isEmpty()) {
            saveActionErrors(errors);
            return redirect(AppPaths.ISSUES_ADD + "?" + RequestContext.URL_PARAM_ERROR_TRUE);

        } else {
            Integer objectTypeId = NumberUtils.replaceNull(actionForm.getLinkedObjectTypeId());
            Integer objectId = NumberUtils.replaceNull(actionForm.getLinkedObjectId());

            if (objectTypeId != 0 && objectId != 0) {
                if (objectTypeId.equals(ObjectTypes.HARDWARE)) {
                    redirect(AppPaths.HARDWARE_ISSUE_ADD_2 + "?issueId=" + issue.getId() + "&hardwareId=" + actionForm.getLinkedObjectId());

                } else if (objectTypeId.equals(ObjectTypes.SOFTWARE)) {
                    redirect(AppPaths.SOFTWARE_ISSUE_ADD_2 + "?issueId=" + issue.getId() + "&softwareId=" + actionForm.getLinkedObjectId());

                } else if (objectTypeId.equals(ObjectTypes.COMPANY)) {
                    redirect(AppPaths.CONTACTS_COMPANY_ISSUE_ADD_2 + "?issueId=" + issue.getId() + "&companyId=" + actionForm.getLinkedObjectId());
                }
            } else {
                redirect(AppPaths.ISSUES_DETAIL + "?issueId=" + issue.getId());
            }

            // Send an email to assignee.
            if (ConfigManager.email.isEmailNotificationOn() && actionForm.getSuppressNotification() != 1) {
                // Here, we need to get the issue again because otherwise, we don't have creator name and creation date.
                issue = issueService.getPublicIssue(issue.getId());

                String emailBody = ConfigManager.email.getIssueAddEmailTemplate().isEmpty() ?
                                        Localizer.getText(requestContext, "issues.issueAdd2.emailBody") : ConfigManager.email.getIssueAddEmailTemplate();

                emailBody = emailBody.replace(Keywords.ISSUE_ASSIGNEE_VAR, AdminUtils.getSystemUsername(requestContext, issue.getAssignee()))
                        .replace(Keywords.ISSUE_ID_VAR, String.valueOf(issue.getId()))
                        .replace(Keywords.ISSUE_REPORTED_BY_VAR, AdminUtils.getSystemUsername(requestContext, issue.getCreator()))
                        .replace(Keywords.ISSUE_REPORTED_ON_VAR, issue.getCreationDate())
                        .replace(Keywords.ISSUE_STATUS_VAR, attributeManager.getAttrFieldNameCache(Attributes.ISSUE_STATUS, issue.getStatus()))
                        .replace(Keywords.ISSUE_PRIORITY_VAR, attributeManager.getAttrFieldNameCache(Attributes.ISSUE_PRIORITY, issue.getPriority()))
                        .replace(Keywords.ISSUE_TYPE_VAR, attributeManager.getAttrFieldNameCache(Attributes.ISSUE_TYPE, issue.getType()))
                        .replace(Keywords.ISSUE_DESCRIPTION_VAR, issue.getDescription())
                        .replace(Keywords.ISSUE_URL_VAR, ConfigManager.system.getAppUrl() + AppPaths.ISSUES_DETAIL + "?issueId=" + issue.getId());

                IssueUtils.sendMail(requestContext, issue, null, emailBody);
            }
            return null;
        }
    }
}
