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

import com.kwoksys.action.common.template.ActionErrorsTemplate;
import com.kwoksys.action.common.template.CustomFieldsTemplate;
import com.kwoksys.action.common.template.HeaderTemplate;
import com.kwoksys.action.common.template.StandardTemplate;
import com.kwoksys.biz.ServiceProvider;
import com.kwoksys.biz.admin.AdminService;
import com.kwoksys.biz.admin.core.AdminUtils;
import com.kwoksys.biz.admin.core.CalendarUtils;
import com.kwoksys.biz.admin.core.UserSearch;
import com.kwoksys.biz.admin.dao.AdminQueries;
import com.kwoksys.biz.admin.dto.AccessUser;
import com.kwoksys.biz.auth.core.Permissions;
import com.kwoksys.biz.issues.dto.Issue;
import com.kwoksys.biz.system.core.*;
import com.kwoksys.biz.system.core.configs.ConfigManager;
import com.kwoksys.framework.connections.database.QueryBits;
import com.kwoksys.framework.properties.Localizer;
import com.kwoksys.framework.struts2.Action2;
import com.kwoksys.framework.ui.SelectOneLabelValueBean;
import org.apache.struts.util.LabelValueBean;

import java.util.ArrayList;
import java.util.List;

/**
 * Action class for adding issue.
 */
public class IssueAddAction extends Action2 {

    public String execute() throws Exception {
        AttributeManager attributeManager = new AttributeManager(requestContext);

        AccessUser accessUser = requestContext.getUser();

        String linkedObjectTypeId = requestContext.getParameterString("linkedObjectTypeId");
        request.setAttribute("linkedObjectTypeId", linkedObjectTypeId);

        String linkedObjectId = requestContext.getParameterString("linkedObjectId");
        request.setAttribute("linkedObjectId", linkedObjectId);

        // Instantiate Issue class.
        IssueForm actionForm = (IssueForm) getBaseForm(IssueForm.class);
        Issue issue = new Issue();

        // Load attributes
        issue.loadAttrs(requestContext);
        
        // Not a resubmit, set some defaults
        if (!actionForm.isResubmit()) {
            actionForm.setIssue(issue);
            actionForm.setCreator(0);
        }

        if (accessUser.hasPermission(Permissions.ISSUE_PROXY_SUBMIT)) {
            if (actionForm.getCreator() != 0) {
                request.setAttribute("hideIssueCreatedBy", "display:none");
            } else {
                request.setAttribute("hideIssueCreatedBySelect", "display:none");
            }
            request.setAttribute("proxySubmitOn", "true");
        }
        request.setAttribute("createdBy", AdminUtils.getSystemUsername(requestContext, accessUser));

        // Get Issue resolution options
        List resolutionOptions = new ArrayList();
        resolutionOptions.add(new SelectOneLabelValueBean(requestContext, "0"));
        attributeManager.getActiveAttrFieldOptionsCache(Attributes.ISSUE_RESOLUTION, resolutionOptions);

        // Get a list of Users whose status is "Enable" for assignee and subscribers
        UserSearch userSearch = new UserSearch();
        userSearch.put(UserSearch.USER_STATUS, AttributeFieldIds.USER_STATUS_ENABLED);

        QueryBits query = new QueryBits(userSearch);
        query.addSortColumn(AdminQueries.getOrderByColumn(AdminUtils.getUsernameSort()));

        // Call the service
        AdminService adminService = ServiceProvider.getAdminService(requestContext);
        List<AccessUser> users = adminService.getUsers(query);

        List assigneeOptions = new ArrayList();
        assigneeOptions.add(new LabelValueBean(Localizer.getText(requestContext, "issueMgmt.colName.unassigned"), "0"));
        for (AccessUser user : users) {
            assigneeOptions.add(new LabelValueBean(AdminUtils.getSystemUsername(requestContext, user), String.valueOf(user.getId())));
        }

        List subscribersOptions = new ArrayList();
        for (AccessUser user : users) {
            subscribersOptions.add(new LabelValueBean(AdminUtils.getSystemUsername(requestContext, user), String.valueOf(user.getId())));
        }

        List creatorOptions = new ArrayList();
        creatorOptions.add(new SelectOneLabelValueBean(requestContext, "0"));
        for (AccessUser user : users) {
            creatorOptions.add(new LabelValueBean(AdminUtils.getSystemUsername(requestContext, user), String.valueOf(user.getId())));
        }

        request.setAttribute("creatorOptions", creatorOptions);

        //
        // Template: StandardTemplate
        //
        StandardTemplate standardTemplate = new StandardTemplate(requestContext);
        request.setAttribute("issue", issue);
        standardTemplate.setPathAttribute("formAction", AppPaths.ISSUES_ADD_2);
        standardTemplate.setPathAttribute("formThisAction", AppPaths.ISSUES_ADD);
        standardTemplate.setAttribute("formCancelLink", Links.getCancelLink(requestContext, AppPaths.ISSUES_LIST).getString());
        request.setAttribute("emailNotification", ConfigManager.email.isEmailNotificationOn());
        request.setAttribute("statusOptions", attributeManager.getActiveAttrFieldOptionsCache(Attributes.ISSUE_STATUS));
        request.setAttribute("priorityOptions", attributeManager.getActiveAttrFieldOptionsCache(Attributes.ISSUE_PRIORITY));
        request.setAttribute("typeOptions", attributeManager.getActiveAttrFieldOptionsCache(Attributes.ISSUE_TYPE));
        request.setAttribute("resolutionOptions", resolutionOptions);
        request.setAttribute("assignedToOptions", assigneeOptions);
        request.setAttribute("availableSubscribersOptions", subscribersOptions);
        // New issue doesn't have any selected subscribers
        request.setAttribute("selectedSubscribersOptions", new ArrayList());
        // If issue has due date, disabling of issue due date is set to false
        request.setAttribute("formDisableIssueDueDate", actionForm.getHasDueDate() != 1);
        request.setAttribute("dueDateOptions", CalendarUtils.getDateOptions(requestContext));
        request.setAttribute("dueMonthOptions", CalendarUtils.getMonthOptions(requestContext));
        request.setAttribute("dueYearOptions", CalendarUtils.getYearOptions(requestContext));

        //
        // Template: HeaderTemplate
        //
        HeaderTemplate header = standardTemplate.getHeaderTemplate();
        header.setTitleKey("issueMgmt.cmd.issueAdd");

        //
        // Template: CustomFieldsTemplate
        //
        CustomFieldsTemplate customFieldsTemplate = new CustomFieldsTemplate();
        standardTemplate.addTemplate(customFieldsTemplate);
        customFieldsTemplate.setObjectTypeId(ObjectTypes.ISSUE);
        customFieldsTemplate.setObjectAttrTypeId(actionForm.getType());
        customFieldsTemplate.setForm(actionForm);

        //
        // Template: ActionErrorsTemplate
        //
        ActionErrorsTemplate errorsTemplate = new ActionErrorsTemplate();
        standardTemplate.addTemplate(errorsTemplate);
        errorsTemplate.setShowRequiredFieldMsg(true);
        errorsTemplate.setMessage(Localizer.getText(requestContext, "issueMgmt.issueAdd.sectionHeader"));

        return standardTemplate.findTemplate(STANDARD_TEMPLATE);
    }
}
