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

import com.kwoksys.biz.base.BaseTemplate;
import com.kwoksys.biz.ServiceProvider;
import com.kwoksys.biz.system.core.AppPaths;
import com.kwoksys.biz.auth.core.Access;
import com.kwoksys.biz.admin.core.AdminUtils;
import com.kwoksys.biz.admin.dto.AccessUser;
import com.kwoksys.biz.issues.IssueService;
import com.kwoksys.biz.issues.core.IssueUtils;
import com.kwoksys.biz.issues.dto.Issue;
import com.kwoksys.framework.exceptions.DatabaseException;
import com.kwoksys.biz.system.core.Attributes;
import com.kwoksys.biz.system.core.AttributeManager;
import com.kwoksys.framework.util.HtmlUtils;
import com.kwoksys.framework.util.StringUtils;
import com.kwoksys.framework.ui.WidgetUtils;

import java.util.ArrayList;
import java.util.List;

/**
 * Template class for Issue spec.
 * detailList
 * columnText
 * columnValue
 * rowClass
 */
public class IssueSpecTemplate extends BaseTemplate {

    private String headerText;
    private Issue issue;
    private String subscribers;
    private boolean hasHtmlContent;

    public IssueSpecTemplate(Issue issue) {
        super(IssueSpecTemplate.class);
        this.issue = issue;
    }

    @Override
    public String getJspPath() {
        return "/WEB-INF/jsp/issues/IssueSpecTemplate.jsp";
    }

    public void setHeaderText(String headerText) {
        this.headerText = headerText;
    }

    public void populateSubscribers() throws DatabaseException {
        IssueService issueService = ServiceProvider.getIssueService(requestContext);

        List<AccessUser> users = issueService.getSelectedSubscribers(issue.getId());
        List list = new ArrayList();
        for (AccessUser user : users) {
            list.add(AdminUtils.getSystemUsername(requestContext, user));
        }
        subscribers = StringUtils.join(list, ", ");
    }

    public void applyTemplate() throws DatabaseException {
        AccessUser user = requestContext.getUser();

        AttributeManager attributeManager = new AttributeManager(requestContext);

        hasHtmlContent = IssueUtils.isHtmlEmail(issue.getDescription());
        issue.setDescription(HtmlUtils.formatMultiLineDisplay(issue.getDescription()));

        boolean hasPermission = Access.hasPermission(user, AppPaths.ADMIN_USER_DETAIL);
        String assigneeName = IssueUtils.getAssigneeIconLink(requestContext, hasPermission, issue.getAssignee());

        request.setAttribute("TemplateIssueSpec_issue", issue);
        request.setAttribute("TemplateIssueSpec_issueAssignee", assigneeName);
        request.setAttribute("TemplateIssueSpec_issueCreatorInfo", WidgetUtils.formatCreatorInfo(requestContext, issue.getCreationDate(), issue.getCreator()));
        request.setAttribute("TemplateIssueSpec_issueModifierInfo", WidgetUtils.formatCreatorInfo(requestContext, issue.getModificationDate(), issue.getModifier()));
        request.setAttribute("TemplateIssueSpec_issueTypeName", attributeManager.getAttrFieldNameCache(Attributes.ISSUE_TYPE, issue.getType()));
        request.setAttribute("TemplateIssueSpec_issueStatusName", attributeManager.getAttrFieldNameCache(Attributes.ISSUE_STATUS, issue.getStatus()));
        request.setAttribute("TemplateIssueSpec_issuePriorityName", attributeManager.getAttrFieldNameCache(Attributes.ISSUE_PRIORITY, issue.getPriority()));
        request.setAttribute("TemplateIssueSpec_issueResolutionName", attributeManager.getAttrFieldNameCache(Attributes.ISSUE_RESOLUTION, issue.getResolution()));
        request.setAttribute("TemplateIssueSpec_issueSubscribers", subscribers);
    }

    public Issue getIssue() {
        return issue;
    }

    public String getHeaderText() {
        return headerText;
    }

    public boolean isHasHtmlContent() {
        return hasHtmlContent;
    }
}