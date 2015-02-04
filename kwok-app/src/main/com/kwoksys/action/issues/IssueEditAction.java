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
import com.kwoksys.action.common.template.StandardTemplate;
import com.kwoksys.biz.ServiceProvider;
import com.kwoksys.biz.admin.core.AdminUtils;
import com.kwoksys.biz.admin.core.CalendarUtils;
import com.kwoksys.biz.admin.dto.AccessUser;
import com.kwoksys.biz.issues.IssueService;
import com.kwoksys.biz.issues.dto.Issue;
import com.kwoksys.biz.system.core.*;
import com.kwoksys.biz.system.core.configs.ConfigManager;
import com.kwoksys.framework.properties.Localizer;
import com.kwoksys.framework.struts2.Action2;
import com.kwoksys.framework.ui.SelectOneLabelValueBean;
import com.kwoksys.framework.util.NumberUtils;
import org.apache.struts.util.LabelValueBean;

import java.util.ArrayList;
import java.util.List;

/**
 * Action class for editing issue.
 */
public class IssueEditAction extends Action2 {

    public String execute() throws Exception {
        IssueForm actionForm = (IssueForm) getBaseForm(IssueForm.class);

        IssueService issueService = ServiceProvider.getIssueService(requestContext);
        Issue issue = issueService.getIssue(actionForm.getIssueId());

        AttributeManager attributeManager = new AttributeManager(requestContext);

        // Load attributes
        issue.loadAttrs(requestContext);
        
        // Not a resubmit, set some defaults
        if (!actionForm.isResubmit()) {
            actionForm.setIssue(issue);
        }

        // Get issue resolution options
        List resolutionOptions = new ArrayList();
        resolutionOptions.add(new SelectOneLabelValueBean(requestContext, "0"));
        attributeManager.getActiveAttrFieldOptionsCache(Attributes.ISSUE_RESOLUTION,
                issue.getResolution(), resolutionOptions);

        // Only shows users whose status is "Enable", plus issue assignee.
        List<LabelValueBean> assigneeOptions = new ArrayList();
        assigneeOptions.add(new LabelValueBean(Localizer.getText(requestContext, "issueMgmt.colName.unassigned"), "0"));
        assigneeOptions.addAll(AdminUtils.getUserOptions(requestContext, issue.getAssignee()));

        // Get a list of available subscribers.
        List availableSubscribers = new ArrayList();
        for (AccessUser availableSubscriber : issueService.getAvailableSubscribers(issue.getId())) {
            availableSubscribers.add(new LabelValueBean(AdminUtils.getSystemUsername(requestContext, availableSubscriber), String.valueOf(availableSubscriber.getId())));
        }

        // Get a list of selected subscribers.
        List selectedSubscribers = new ArrayList();
        for (AccessUser selectedSubscriber : issueService.getSelectedSubscribers(issue.getId())) {
            selectedSubscribers.add(new LabelValueBean(AdminUtils.getSystemUsername(requestContext, selectedSubscriber), String.valueOf(selectedSubscriber.getId())));
        }

        // Issue due date: year
        int dueDateYear = NumberUtils.replaceNull(actionForm.getDueDateYear(), 0);

        //
        // Template: StandardTemplate
        //
        StandardTemplate standardTemplate = new StandardTemplate(requestContext);
        request.setAttribute("issue", issue);
        standardTemplate.setPathAttribute("formAction", AppPaths.ISSUES_EDIT_2);
        standardTemplate.setPathAttribute("formThisAction", AppPaths.ISSUES_EDIT);
        standardTemplate.setAttribute("formCancelLink", Links.getCancelLink(requestContext, AppPaths.ISSUES_DETAIL + "?issueId=" + issue.getId()).getString());
        request.setAttribute("emailNotification", ConfigManager.email.isEmailNotificationOn());
        request.setAttribute("statusOptions", attributeManager.getActiveAttrFieldOptionsCache(Attributes.ISSUE_STATUS,
                issue.getStatus()));
        request.setAttribute("priorityOptions", attributeManager.getActiveAttrFieldOptionsCache(
                Attributes.ISSUE_PRIORITY, issue.getPriority()));
        request.setAttribute("typeOptions", attributeManager.getActiveAttrFieldOptionsCache(Attributes.ISSUE_TYPE,
                issue.getType()));
        request.setAttribute("resolutionOptions", resolutionOptions);
        request.setAttribute("assignedToOptions", assigneeOptions);
        request.setAttribute("availableSubscribersOptions", availableSubscribers);
        request.setAttribute("selectedSubscribersOptions", selectedSubscribers);
        // If issue has due date, disabling of issue due date is set to false
        request.setAttribute("formDisableIssueDueDate", actionForm.getHasDueDate() != 1);
        request.setAttribute("dueDateOptions", CalendarUtils.getDateOptions(requestContext));
        request.setAttribute("dueMonthOptions", CalendarUtils.getMonthOptions(requestContext));
        request.setAttribute("dueYearOptions", CalendarUtils.getExtraYearOptions(requestContext, dueDateYear));

        //
        // Template: IssueSpecTemplate
        //
        IssueSpecTemplate spec = new IssueSpecTemplate(issue);
        standardTemplate.addTemplate(spec);
        spec.setHeaderText(Localizer.getText(requestContext, "issueMgmt.issueEdit.header"));

        //
        // Template: ActionErrorsTemplate
        //
        ActionErrorsTemplate errorsTemplate = new ActionErrorsTemplate();
        standardTemplate.addTemplate(errorsTemplate);
        errorsTemplate.setShowRequiredFieldMsg(true);
        errorsTemplate.setMessage(Localizer.getText(requestContext, "issueMgmt.issueEdit.issueUpdateSectionTitle"));

        //
        // Template: CustomFieldsTemplate
        //
        CustomFieldsTemplate customFieldsTemplate = new CustomFieldsTemplate();
        standardTemplate.addTemplate(customFieldsTemplate);
        customFieldsTemplate.setObjectTypeId(ObjectTypes.ISSUE);
        customFieldsTemplate.setObjectId(issue.getId());
        customFieldsTemplate.setObjectAttrTypeId(actionForm.getType());
        customFieldsTemplate.setForm(actionForm);

        return standardTemplate.findTemplate(STANDARD_TEMPLATE);
    }
}