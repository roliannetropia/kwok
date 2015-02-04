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

import com.kwoksys.action.common.template.*;
import com.kwoksys.action.hardware.HardwareListTemplate;
import com.kwoksys.biz.ServiceProvider;
import com.kwoksys.biz.admin.dto.AccessUser;
import com.kwoksys.biz.auth.core.Access;
import com.kwoksys.biz.contacts.core.CompanyUtils;
import com.kwoksys.biz.contacts.dao.ContactQueries;
import com.kwoksys.biz.contacts.dto.Company;
import com.kwoksys.biz.hardware.core.HardwareUtils;
import com.kwoksys.biz.hardware.dao.HardwareQueries;
import com.kwoksys.biz.hardware.dto.Hardware;
import com.kwoksys.biz.issues.IssueService;
import com.kwoksys.biz.issues.core.IssueUtils;
import com.kwoksys.biz.issues.dto.Issue;
import com.kwoksys.biz.software.SoftwareUtils;
import com.kwoksys.biz.software.dao.SoftwareQueries;
import com.kwoksys.biz.software.dto.Software;
import com.kwoksys.biz.system.SystemService;
import com.kwoksys.biz.system.core.AppPaths;
import com.kwoksys.biz.system.core.ObjectTypes;
import com.kwoksys.framework.connections.database.QueryBits;
import com.kwoksys.framework.properties.Localizer;
import com.kwoksys.framework.session.SessionManager;
import com.kwoksys.framework.struts2.Action2;
import com.kwoksys.framework.ui.Link;
import com.kwoksys.framework.util.Counter;

import java.util.Arrays;
import java.util.List;
import java.util.Map;

/**
 * Action class for issue relationship.
 */
public class IssueRelationshipAction extends Action2 {

    public String execute() throws Exception {
        AccessUser accessUser = requestContext.getUser();

        Integer issueId = requestContext.getParameter("issueId");

        IssueService issueService = ServiceProvider.getIssueService(requestContext);
        Issue issue = issueService.getIssue(issueId);

        SystemService systemService = ServiceProvider.getSystemService(requestContext);
        List types = Arrays.asList(String.valueOf(ObjectTypes.COMPANY), String.valueOf(ObjectTypes.HARDWARE),
                String.valueOf(ObjectTypes.SOFTWARE));
        List linkedTypes = Arrays.asList(String.valueOf(ObjectTypes.ISSUE));
        int relationshipCount = systemService.getObjectMapCount(types, issueId, linkedTypes);

        String hwOrderBy = SessionManager.getOrSetAttribute(requestContext, "hwOrderBy", SessionManager.HARDWARE_ORDER_BY, Hardware.HARDWARE_NAME);
        String hwOrder = SessionManager.getOrSetAttribute(requestContext, "hwOrder", SessionManager.HARDWARE_ORDER, QueryBits.ASCENDING);

        // Get column headers
        List hwColumnHeaders = HardwareUtils.getColumnHeaderList();

        // Do some sorting.
        QueryBits query = new QueryBits();

        if (HardwareUtils.isSortableColumn(hwOrderBy)) {
            query.addSortColumn(HardwareQueries.getOrderByColumn(hwOrderBy), hwOrder);
        }

        //
        // Template: StandardTemplate
        //
        StandardTemplate standardTemplate = new StandardTemplate(requestContext);
        standardTemplate.setAttribute("ajaxHardwareDetailPath", AppPaths.IT_MGMT_AJAX_GET_HARDWARE_DETAIL + "?hardwareId=");

        //
        // Template: HardwareListTemplate
        //
        HardwareListTemplate listTemplate = new HardwareListTemplate("_hardware");
        standardTemplate.addTemplate(listTemplate);
        listTemplate.setHardwareList(issueService.getIssueHardwareList(new QueryBits(), issue.getId()));
        listTemplate.setColspan(hwColumnHeaders.size());
        listTemplate.setCounter(new Counter());

        //
        // Template: TableEmptyTemplate
        //
        TableEmptyTemplate empty = new TableEmptyTemplate("_hardware");
        standardTemplate.addTemplate(empty);
        empty.setColSpan(hwColumnHeaders.size());
        empty.setRowText(Localizer.getText(requestContext, "itMgmt.hardwareList.emptyTableMessage"));

        //
        // Template: TableHeaderTemplate
        //
        TableHeaderTemplate tableHeader = new TableHeaderTemplate("_hardware");
        standardTemplate.addTemplate(tableHeader);
        tableHeader.setColumnList(hwColumnHeaders);
        tableHeader.setSortableColumnList(HardwareUtils.getSortableColumns());
        tableHeader.setColumnPath(AppPaths.ISSUES_RELATIONSHIP + "?issueId=" + issueId);
        tableHeader.setColumnTextKey("common.column.");
        tableHeader.setOrderBy(hwOrderBy);
        tableHeader.setOrderByParamName("hwOrderBy");
        tableHeader.setOrderParamName("hwOrder");
        tableHeader.setOrder(hwOrder);

        //
        // Linked software
        //
        String swOrderBy = SessionManager.getOrSetAttribute(requestContext, "swOrderBy", SessionManager.SOFTWARE_ORDER_BY, Software.NAME);
        String swOrder = SessionManager.getOrSetAttribute(requestContext, "swOrder", SessionManager.SOFTWARE_ORDER, QueryBits.ASCENDING);

        // Do some sorting.
        QueryBits swQuery = new QueryBits();

        if (SoftwareUtils.isSortableColumn(swOrderBy)) {
            swQuery.addSortColumn(SoftwareQueries.getOrderByColumn(swOrderBy), swOrder);
        }

        List<Software> softwareList = issueService.getIssueSoftwareList(swQuery, issue.getId());
        List<Map> formattedList = SoftwareUtils.formatSoftwareList(requestContext, softwareList, new Counter());

        //
        // Template: TableTemplate
        //
        TableTemplate swTableTemplate = new TableTemplate("_software");
        standardTemplate.addTemplate(swTableTemplate);
        swTableTemplate.setDataList(formattedList);
        swTableTemplate.setColumnHeaders(SoftwareUtils.getColumnHeaderList());
        swTableTemplate.setColumnPath(AppPaths.ISSUES_RELATIONSHIP + "?issueId=" + issueId);
        swTableTemplate.setSortableColumnHeaders(SoftwareUtils.getSortableColumns());
        swTableTemplate.setColumnTextKey("common.column.");
        swTableTemplate.setEmptyRowMsgKey("itMgmt.softwareList.emptyTableMessage");
        swTableTemplate.setOrderBy(swOrderBy);
        swTableTemplate.setOrder(swOrder);
        swTableTemplate.setOrderByParamName("swOrderBy");
        swTableTemplate.setOrderParamName("swOrder");

        //
        // Template: TableTemplate
        //
        String orderBy = SessionManager.getOrSetAttribute(requestContext, "cOrderBy", SessionManager.COMPANIES_ORDER_BY, Company.COMPANY_NAME);
        String order = SessionManager.getOrSetAttribute(requestContext, "cOrder", SessionManager.COMPANIES_ORDER, QueryBits.ASCENDING);

        query = new QueryBits();
        if (Company.isSortableCompanyColumn(orderBy)) {
            query.addSortColumn(ContactQueries.getOrderByColumn(orderBy), order);
        }

        List<Company> companyDataset = issueService.getIssueCompanyList(query, issue.getId());
        List dataList = CompanyUtils.formatCompanyList(requestContext, companyDataset, new Counter());

        TableTemplate tableTemplate = new TableTemplate("_companies");
        standardTemplate.addTemplate(tableTemplate);
        tableTemplate.setDataList(dataList);
        tableTemplate.setColumnHeaders(Company.getCompanyColumnHeaderList());
        tableTemplate.setSortableColumnHeaders(Company.getSortableCompanyColumnList());
        tableTemplate.setColumnPath(AppPaths.ISSUES_RELATIONSHIP + "?issueId=" + issueId);
        tableTemplate.setColumnTextKey("common.column.");
        tableTemplate.setOrderBy(orderBy);
        tableTemplate.setOrder(order);
        tableTemplate.setOrderByParamName("cOrderBy");
        tableTemplate.setOrderParamName("cOrder");
        tableTemplate.setEmptyRowMsgKey("contactMgmt.companyList.emptyTableMessage");

        //
        // Template: HeaderTemplate
        //
        HeaderTemplate header = standardTemplate.getHeaderTemplate();
        header.setPageTitleKey("issueMgmt.issueDetail.title", new Object[] {issue.getSubject()});

        // Back to Issue list.
        if (Access.hasPermission(accessUser, AppPaths.ISSUES_LIST)) {
            Link link = new Link(requestContext);
            link.setAjaxPath(AppPaths.ISSUES_LIST);
            link.setTitleKey("issueMgmt.cmd.issueList");
            header.addHeaderCmds(link);
        }

        //
        // Template: TabsTemplate
        //
        TabsTemplate tabs = new TabsTemplate();
        standardTemplate.addTemplate(tabs);
        tabs.setTabList(IssueUtils.getIssueTabs(requestContext, issue, relationshipCount));
        tabs.setTabActive(IssueUtils.ISSUE_TAB_RELATIONSHIP);

        //
        // Template: IssueSpecTemplate
        //
        IssueSpecTemplate spec = new IssueSpecTemplate(issue);
        standardTemplate.addTemplate(spec);
        spec.setHeaderText(issue.getSubject());
        spec.populateSubscribers();

        return standardTemplate.findTemplate(STANDARD_TEMPLATE);
    }
}