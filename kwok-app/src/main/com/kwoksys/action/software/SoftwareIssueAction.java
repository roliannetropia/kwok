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
package com.kwoksys.action.software;

import com.kwoksys.action.common.template.ActionErrorsTemplate;
import com.kwoksys.action.common.template.HeaderTemplate;
import com.kwoksys.action.common.template.StandardV2Template;
import com.kwoksys.action.common.template.TabsTemplate;
import com.kwoksys.action.issues.IssueListTemplate;
import com.kwoksys.biz.ServiceProvider;
import com.kwoksys.biz.admin.dto.AccessUser;
import com.kwoksys.biz.auth.core.Access;
import com.kwoksys.biz.issues.core.IssueUtils;
import com.kwoksys.biz.issues.dto.Issue;
import com.kwoksys.biz.software.SoftwareService;
import com.kwoksys.biz.software.SoftwareUtils;
import com.kwoksys.biz.software.dao.SoftwareQueries;
import com.kwoksys.biz.software.dto.Software;
import com.kwoksys.biz.system.core.AppPaths;
import com.kwoksys.biz.system.dto.linking.SoftwareIssueLink;
import com.kwoksys.framework.connections.database.QueryBits;
import com.kwoksys.framework.struts2.Action2;
import com.kwoksys.framework.ui.Link;

/**
 * Action class for showing Software Issues.
 */
public class SoftwareIssueAction extends Action2 {

    public String execute() throws Exception {
        AccessUser accessUser = requestContext.getUser();

        Integer softwareId = requestContext.getParameter("softwareId");

        SoftwareService softwareService = ServiceProvider.getSoftwareService(requestContext);
        Software software = softwareService.getSoftware(softwareId);

        SoftwareIssueForm actionForm = (SoftwareIssueForm) getBaseForm(SoftwareIssueForm.class);

        QueryBits query = new QueryBits();
        query.addSortColumn(SoftwareQueries.getOrderByColumn(Issue.CREATION_DATE));

        //
        // Template: StandardV2Template
        //
        StandardV2Template standardTemplate = new StandardV2Template(requestContext);

        //
        // Template: HeaderTemplate
        //
        HeaderTemplate header = standardTemplate.getHeaderTemplate();
        SoftwareUtils.addSoftwareHeaderCommands(requestContext, header, software.getId());
        header.setPageTitleKey("itMgmt.softwareDetail.header", new Object[] {software.getName()});

        // Add Software Issue.
        if (Access.hasPermission(accessUser, AppPaths.SOFTWARE_ISSUE_ADD)) {
            Link link = new Link(requestContext);
            link.setAjaxPath(AppPaths.SOFTWARE_ISSUE_ADD + "?softwareId=" + softwareId);
            link.setTitleKey("common.linking.linkIssue");
            header.addHeaderCmds(link);
        }

        //
        // Template: SoftwareSpecTemplate
        //
        SoftwareSpecTemplate tmpl = new SoftwareSpecTemplate(software);
        standardTemplate.addTemplate(tmpl);

        //
        // Template: ActionErrorsTemplate
        //
        standardTemplate.addTemplate(new ActionErrorsTemplate());

        //
        // Template: TabsTemplate
        //
        TabsTemplate tabs = new TabsTemplate();
        standardTemplate.addTemplate(tabs);
        tabs.setTabList(SoftwareUtils.softwareTabList(requestContext, software));
        tabs.setTabActive(SoftwareUtils.ISSUES_TAB);

        //
        // Template: IssueListTemplate
        //
        IssueListTemplate listTemplate = new IssueListTemplate();
        standardTemplate.addTemplate(listTemplate);
        listTemplate.setAccessUser(accessUser);
        listTemplate.setQuery(query);
        listTemplate.setObjectMap(new SoftwareIssueLink(softwareId).createObjectMap());
        listTemplate.setFormRemoveItemAction(AppPaths.SOFTWARE_ISSUE_REMOVE_2);
        listTemplate.setColumnHeaders(IssueUtils.getIssueColumnHeaders());
        listTemplate.setEmptyTableRowKey("issueMgmt.issueList.emptyTableMessage");
        listTemplate.getFormHiddenVariableMap().put("softwareId", software.getId());
        listTemplate.setFormRowIdName("issueId");

        return standardTemplate.findTemplate(STANDARD_TEMPLATE);
    }
}