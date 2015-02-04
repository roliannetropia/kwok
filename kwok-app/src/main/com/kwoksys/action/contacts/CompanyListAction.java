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
package com.kwoksys.action.contacts;

import com.kwoksys.action.common.template.HeaderTemplate;
import com.kwoksys.action.common.template.RecordsNavigationTemplate;
import com.kwoksys.action.common.template.StandardV2Template;
import com.kwoksys.action.common.template.TableTemplate;
import com.kwoksys.biz.ServiceProvider;
import com.kwoksys.biz.admin.dto.AccessUser;
import com.kwoksys.biz.auth.core.Access;
import com.kwoksys.biz.contacts.ContactService;
import com.kwoksys.biz.contacts.core.CompanySearch;
import com.kwoksys.biz.contacts.core.CompanyUtils;
import com.kwoksys.biz.contacts.dao.ContactQueries;
import com.kwoksys.biz.contacts.dto.Company;
import com.kwoksys.biz.system.core.AppPaths;
import com.kwoksys.biz.system.core.Image;
import com.kwoksys.biz.system.core.configs.ConfigManager;
import com.kwoksys.framework.connections.database.QueryBits;
import com.kwoksys.framework.properties.Localizer;
import com.kwoksys.framework.session.SessionManager;
import com.kwoksys.framework.struts2.Action2;
import com.kwoksys.framework.ui.Link;
import com.kwoksys.framework.util.Counter;

import java.util.ArrayList;
import java.util.List;

/**
 * Action class for displaying company list.
 */
public class CompanyListAction extends Action2 {

    public String execute() throws Exception {
        ContactSearchForm actionForm = (ContactSearchForm) getSessionBaseForm(ContactSearchForm.class);

        AccessUser user = requestContext.getUser();

        String cmd = requestContext.getParameterString("cmd");
        String rowCmd = requestContext.getParameterString("rowCmd");
        String orderBy = SessionManager.getOrSetAttribute(requestContext, "orderBy", SessionManager.COMPANIES_ORDER_BY, Company.COMPANY_NAME);
        String order = SessionManager.getOrSetAttribute(requestContext, "order", SessionManager.COMPANIES_ORDER, QueryBits.ASCENDING);

        int rowStart = 0;
        if (!cmd.isEmpty() || rowCmd.equals("showAll")) {
            request.getSession().setAttribute(SessionManager.COMPANIES_ROW_START, rowStart);
        } else {
            rowStart = SessionManager.getOrSetAttribute(requestContext, "rowStart", SessionManager.COMPANIES_ROW_START, rowStart);
        }

        int rowLimit = requestContext.getParameter("rowLimit", ConfigManager.app.getCompanyRows());
        if (rowCmd.equals("showAll")) {
            rowLimit = 0;
        }

        CompanySearch companySearch = new CompanySearch(requestContext, SessionManager.COMPANY_SEARCH_CRITERIA_MAP);

        // Getting search criteria map from session variable.
        if (!cmd.isEmpty()) {
            companySearch.reset();

            if (cmd.equals("search")) {
                // We are expecting user to enter some search criteria.
                companySearch.prepareMap(actionForm);

            } else if (cmd.equals("showAll")) {
                // We're expecting to reset the search criteria.
            }

            companySearch.put("cmd", cmd);
        }

        // Ready to pass variables to query.
        QueryBits query = new QueryBits(companySearch);
        query.setLimit(rowLimit, rowStart);

        if (Company.isSortableCompanyColumn(orderBy)) {
            query.addSortColumn(ContactQueries.getOrderByColumn(orderBy), order);
        }

        // Get column headers
        List<String> columnHeaders = Company.getCompanyColumnHeaderList();

        ContactService contactService = ServiceProvider.getContactService(requestContext);

        int rowCount = contactService.getCompanyCount(query);
        List dataList = new ArrayList();

        if (rowCount != 0) {
            List<Company> companyDataset = contactService.getCompanies(query);
            Counter counter = new Counter(rowStart);
            dataList = CompanyUtils.formatCompanyList(requestContext, companyDataset, counter);
        }

        //
        // Template: StandardV2Template
        //
        StandardV2Template standardTemplate = new StandardV2Template(requestContext);

        //
        // Template: HeaderTemplate
        //
        HeaderTemplate header = standardTemplate.getHeaderTemplate();
        header.setTitleKey("contactMgmt.companyList.title");
        header.setTitleClassNoLine();
        
        // Link to add company page
        if (Access.hasPermission(user, AppPaths.CONTACTS_COMPANY_ADD)) {
            Link link = new Link(requestContext);
            link.setAjaxPath(AppPaths.CONTACTS_COMPANY_ADD);
            link.setTitleKey("contactMgmt.cmd.companyAdd");
            header.addHeaderCmds(link);
        }

        if (Access.hasPermission(user, AppPaths.CONTACTS_COMPANY_LIST_EXPORT)) {
            Link link = new Link(requestContext);
            link.setExportPath(AppPaths.CONTACTS_COMPANY_LIST_EXPORT);
            link.setTitleKey("contacts.cmd.companyExport");
            link.setImgSrc(Image.getInstance().getCsvFileIcon());
            header.addHeaderCmds(link);
        }

        //
        // Template: RecordsNavigationTemplate
        //
        RecordsNavigationTemplate nav = new RecordsNavigationTemplate();
        standardTemplate.addTemplate(nav);
        if (companySearch.getSearchCriteriaMap().containsKey("cmd")
                && companySearch.getSearchCriteriaMap().get("cmd").equals("search")) {        
            nav.setInfoText(Localizer.getText(requestContext, "contactMgmt.companyList.searchResult"));
        }
        nav.setRowOffset(rowStart);
        nav.setRowLimit(rowLimit);
        nav.setRowCount(rowCount);
        nav.setRowCountMsgkey("core.template.recordsNav.rownum");
        nav.setShowAllRecordsText(Localizer.getText(requestContext, "contactMgmt.companyList.rowCount", new Object[]{rowCount}));
        nav.setShowAllRecordsPath(AppPaths.CONTACTS_COMPANY_LIST + "?rowCmd=showAll");
        nav.setPath(AppPaths.CONTACTS_COMPANY_LIST + "?rowStart=");

        //
        // Template: TableTemplate
        //
        TableTemplate tableTemplate = new TableTemplate();
        standardTemplate.addTemplate(tableTemplate);
        tableTemplate.setDataList(dataList);
        tableTemplate.setColumnHeaders(columnHeaders);
        tableTemplate.setSortableColumnHeaders(Company.getSortableCompanyColumnList());
        tableTemplate.setColumnPath(AppPaths.CONTACTS_COMPANY_LIST);
        tableTemplate.setColumnTextKey("common.column.");
        tableTemplate.setRowCmd(rowCmd);
        tableTemplate.setOrderBy(orderBy);
        tableTemplate.setOrder(order);
        tableTemplate.setEmptyRowMsgKey("contactMgmt.companyList.emptyTableMessage");

        return standardTemplate.findTemplate(STANDARD_TEMPLATE);
    }
}
