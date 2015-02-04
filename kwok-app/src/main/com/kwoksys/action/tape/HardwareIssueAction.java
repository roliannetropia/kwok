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
package com.kwoksys.action.tape;

import com.kwoksys.action.common.template.ActionErrorsTemplate;
import com.kwoksys.action.common.template.HeaderTemplate;
import com.kwoksys.action.common.template.StandardV2Template;
import com.kwoksys.action.common.template.TabsTemplate;
import com.kwoksys.action.issues.IssueListTemplate;
import com.kwoksys.biz.ServiceProvider;
import com.kwoksys.biz.admin.dto.AccessUser;
import com.kwoksys.biz.auth.core.Access;
import com.kwoksys.biz.hardware.HardwareService;
import com.kwoksys.biz.hardware.core.HardwareUtils;
import com.kwoksys.biz.hardware.dao.HardwareQueries;
import com.kwoksys.biz.hardware.dto.Hardware;
import com.kwoksys.biz.issues.core.IssueUtils;
import com.kwoksys.biz.issues.dto.Issue;
import com.kwoksys.biz.system.core.AppPaths;
import com.kwoksys.biz.system.dto.linking.HardwareIssueLink;
import com.kwoksys.framework.connections.database.QueryBits;
import com.kwoksys.framework.struts2.Action2;
import com.kwoksys.framework.ui.Link;

/**
 * Action class for showing hardware components.
 */
public class HardwareIssueAction extends Action2 {

    public String execute() throws Exception {
        HardwareForm actionForm = (HardwareForm) getBaseForm(HardwareForm.class);

        AccessUser user = requestContext.getUser();

        HardwareService hardwareService = ServiceProvider.getHardwareService(requestContext);
        Hardware hardware = hardwareService.getHardware(actionForm.getHardwareId());

        QueryBits query = new QueryBits();
        query.addSortColumn(HardwareQueries.getOrderByColumn(Issue.CREATION_DATE));

        //
        // Template: StandardV2Template
        //
        StandardV2Template standardTemplate = new StandardV2Template(requestContext);

        //
        // Template: HeaderTemplate
        //
        HeaderTemplate headerTemplate = standardTemplate.getHeaderTemplate();
        headerTemplate.setPageTitleKey("itMgmt.hardwareDetail.header", new Object[] {hardware.getName()});
        HardwareUtils.addHardwareHeaderCommands(requestContext, headerTemplate, hardware.getId());

        // Add hardware components.
        if (Access.hasPermission(user, AppPaths.HARDWARE_ISSUE_ADD)) {
            Link link = new Link(requestContext);
            link.setAjaxPath(AppPaths.HARDWARE_ISSUE_ADD + "?hardwareId=" + hardware.getId());
            link.setTitleKey("common.linking.linkIssue");
            headerTemplate.addHeaderCmds(link);
        }

        //
        // Template: HardwareSpecTemplate
        //
        HardwareSpecTemplate tmpl = new HardwareSpecTemplate(hardware);
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
        tabs.setTabList(HardwareUtils.hardwareTabList(hardware, requestContext));
        tabs.setTabActive(HardwareUtils.HARDWARE_ISSUE_TAB);

        //
        // Template: IssueListTemplate
        //
        IssueListTemplate listTemplate = new IssueListTemplate();
        standardTemplate.addTemplate(listTemplate);
        listTemplate.setAccessUser(user);
        listTemplate.setQuery(query);
        listTemplate.setObjectMap(new HardwareIssueLink(hardware.getId()).createObjectMap());
        listTemplate.setFormRemoveItemAction(AppPaths.HARDWARE_ISSUE_REMOVE_2);
        listTemplate.setColumnHeaders(IssueUtils.getIssueColumnHeaders());
        listTemplate.setEmptyTableRowKey("issueMgmt.issueList.emptyTableMessage");
        listTemplate.getFormHiddenVariableMap().put("hardwareId", hardware.getId());
        listTemplate.setFormRowIdName("issueId");

        return standardTemplate.findTemplate(STANDARD_TEMPLATE);
    }
}