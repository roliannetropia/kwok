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

import com.kwoksys.action.common.template.TableTemplate;
import com.kwoksys.biz.ServiceProvider;
import com.kwoksys.biz.admin.dto.AccessUser;
import com.kwoksys.biz.auth.core.Access;
import com.kwoksys.biz.auth.core.IssueAccess;
import com.kwoksys.biz.base.BaseTemplate;
import com.kwoksys.biz.issues.IssueService;
import com.kwoksys.biz.issues.core.IssueSearch;
import com.kwoksys.biz.issues.core.IssueUtils;
import com.kwoksys.biz.issues.dto.Issue;
import com.kwoksys.biz.system.core.AppPaths;
import com.kwoksys.biz.system.dto.linking.ObjectLink;
import com.kwoksys.framework.connections.database.QueryBits;
import com.kwoksys.framework.util.Counter;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * IssueListTemplate
 */
public class IssueListTemplate extends BaseTemplate {

    private AccessUser accessUser;
    private QueryBits query;
    private ObjectLink objectMap;
    private Map formHiddenVariableMap = new HashMap();
    private String formRemoveItemAction;
    private String formRowIdName;

    // Template: TableHeaderTemplate
    private List columnHeaders;

    // Template: TableEmptyTemplate
    private String emptyTableRowKey;

    private TableTemplate tableTemplate = new TableTemplate();

    public IssueListTemplate() {
        super(IssueListTemplate.class);

        addTemplate(tableTemplate);
    }

    public void applyTemplate() throws Exception {
        IssueService issueService = ServiceProvider.getIssueService(requestContext);
        List<Issue> issueList = issueService.getLinkedIssues(query, objectMap);

        boolean hasIssueDetailAccess = Access.hasPermission(accessUser, AppPaths.ISSUES_DETAIL);

        IssueAccess access = new IssueAccess(accessUser);

        if (hasIssueDetailAccess && !access.hasReadAllPermission()) {
            // For access control
            IssueSearch issueSearch = new IssueSearch();
            issueSearch.put(IssueSearch.ISSUE_PERMITTED_USER_ID, accessUser.getId());

            List<Issue> allowedIssues = issueService.getLinkedIssues(new QueryBits(issueSearch), objectMap);
            access.setAllowedIssues(allowedIssues);
        }
        List<Map> formattedList = IssueUtils.formatIssues(requestContext, issueList, access, new Counter());

        tableTemplate.setDataList(formattedList);
        tableTemplate.setColumnHeaders(columnHeaders);
        tableTemplate.setColumnTextKey("issueMgmt.colName.");
        tableTemplate.setEmptyRowMsgKey(emptyTableRowKey);
        tableTemplate.setFormRemoveItemAction(formRemoveItemAction);
        tableTemplate.setFormHiddenVariableMap(formHiddenVariableMap);
        tableTemplate.setFormRowIdName(formRowIdName);
    }

    @Override
    public String getJspPath() {
        return "/WEB-INF/jsp/issues/IssueListTemplate.jsp";
    }

    public void setQuery(QueryBits query) {
        this.query = query;
    }

    public void setObjectMap(ObjectLink objectMap) {
        this.objectMap = objectMap;
    }

    public void setAccessUser(AccessUser accessUser) {
        this.accessUser = accessUser;
    }

    public void setFormRemoveItemAction(String formRemoveItemAction) {
        this.formRemoveItemAction = formRemoveItemAction;
    }

    public void setColumnHeaders(List columnHeaders) {
        this.columnHeaders = columnHeaders;
    }

    public void setEmptyTableRowKey(String emptyTableRowKey) {
        this.emptyTableRowKey = emptyTableRowKey;
    }

    public Map getFormHiddenVariableMap() {
        return formHiddenVariableMap;
    }

    public void setFormRowIdName(String formRowIdName) {
        this.formRowIdName = formRowIdName;
    }
}