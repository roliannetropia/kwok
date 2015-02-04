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
package com.kwoksys.action.contracts;

import com.kwoksys.action.common.template.HeaderTemplate;
import com.kwoksys.action.common.template.RecordsNavigationTemplate;
import com.kwoksys.action.common.template.StandardTemplate;
import com.kwoksys.action.common.template.TableTemplate;
import com.kwoksys.biz.ServiceProvider;
import com.kwoksys.biz.admin.dto.AccessUser;
import com.kwoksys.biz.auth.core.Access;
import com.kwoksys.biz.contracts.ContractService;
import com.kwoksys.biz.contracts.core.ContractSearch;
import com.kwoksys.biz.contracts.core.ContractUtils;
import com.kwoksys.biz.contracts.dao.ContractQueries;
import com.kwoksys.biz.contracts.dto.Contract;
import com.kwoksys.biz.system.core.AppPaths;
import com.kwoksys.biz.system.core.AttributeManager;
import com.kwoksys.biz.system.core.Attributes;
import com.kwoksys.biz.system.core.configs.ConfigManager;
import com.kwoksys.framework.connections.database.QueryBits;
import com.kwoksys.framework.properties.Localizer;
import com.kwoksys.framework.session.SessionManager;
import com.kwoksys.framework.struts2.Action2;
import com.kwoksys.framework.ui.Link;
import com.kwoksys.framework.ui.SelectOneLabelValueBean;
import com.kwoksys.framework.util.Counter;

import java.util.ArrayList;
import java.util.List;

/**
 * Action class for displaying contract list.
 */
public class ContractListAction extends Action2 {

    public String execute() throws Exception {
        AccessUser user = requestContext.getUser();

        ContractSearchForm actionForm = (ContractSearchForm) getSessionBaseForm(ContractSearchForm.class);
        Integer stageFilter = requestContext.getParameterInteger("stage");
        if (stageFilter != null) {
            actionForm.setStage(stageFilter);
        }

        String cmd = requestContext.getParameterString("cmd");
        String rowCmd = requestContext.getParameterString("rowCmd");
        String orderBy = SessionManager.getOrSetAttribute(requestContext, "orderBy", SessionManager.CONTRACTS_ORDER_BY, Contract.NAME);
        String order = SessionManager.getOrSetAttribute(requestContext, "order", SessionManager.CONTRACTS_ORDER, QueryBits.ASCENDING);

        int rowStart = 0;
        if (!cmd.isEmpty() || rowCmd.equals("showAll") || stageFilter != null) {
            request.getSession().setAttribute(SessionManager.CONTRACTS_ROW_START, rowStart);
        } else {
            rowStart = SessionManager.getOrSetAttribute(requestContext, "rowStart", SessionManager.CONTRACTS_ROW_START, rowStart);
        }

        int rowLimit = requestContext.getParameter("rowLimit", ConfigManager.app.getContractsRowsToShow());

        if (rowCmd.equals("showAll")) {
            rowLimit = 0;
        }

        // Get column headers
        List<String> columnHeaders = ContractUtils.getColumnHeaderList();

        ContractService contractService = ServiceProvider.getContractService(requestContext);

        ContractSearch contractSearch = new ContractSearch(requestContext, SessionManager.CONTRACT_SEARCH_CRITERIA_MAP);
        contractSearch.prepareMap(actionForm);

        // Pass variables to query.
        QueryBits query = new QueryBits(contractSearch);
        query.setLimit(rowLimit, rowStart);

        if (ContractUtils.isSortableColumn(orderBy)) {
            query.addSortColumn(ContractQueries.getOrderByColumn(orderBy), order);
        }

        int rowCount = contractService.getContractCount(query);
        List dataList = null;

        if (rowCount != 0) {
            List<Contract> contracts = contractService.getContracts(query);
            dataList = ContractUtils.formatContractList(requestContext, contracts, new Counter(rowStart));
        }

        AttributeManager attributeManager = new AttributeManager(requestContext);

        // Get contract stage options
        List contractStageOptions = new ArrayList();
        contractStageOptions.add(new SelectOneLabelValueBean(requestContext));
        attributeManager.getAttrValueOptionsCache(Attributes.CONTRACT_STAGE, contractStageOptions);

        //
        // Template: StandardTemplate
        //
        StandardTemplate standardTemplate = new StandardTemplate(requestContext);

        if (!contractSearch.getSearchCriteriaMap().isEmpty()) {
            standardTemplate.setAttribute("searchResultText", Localizer.getText(requestContext, "contracts.contractList.searchResult"));
        }
        standardTemplate.setAttribute("stageFilterOptions", contractStageOptions);
        standardTemplate.setPathAttribute("formAction", AppPaths.CONTRACTS_LIST);

        //
        // Template: TableTemplate
        //
        TableTemplate tableTemplate = new TableTemplate();
        standardTemplate.addTemplate(tableTemplate);
        tableTemplate.setColumnHeaders(columnHeaders);
        tableTemplate.setSortableColumnHeaders(ContractUtils.getSortableColumns());
        tableTemplate.setColumnPath(AppPaths.CONTRACTS_LIST);
        tableTemplate.setColumnTextKey("common.column.");
        tableTemplate.setOrderBy(orderBy);
        tableTemplate.setOrder(order);
        tableTemplate.setDataList(dataList);
        tableTemplate.setEmptyRowMsgKey("itMgmt.contractList.emptyTableMessage");

        //
        // Template: HeaderTemplate
        //
        HeaderTemplate header = standardTemplate.getHeaderTemplate();
        header.setTitleKey("itMgmt.contractList.title");
        header.setTitleClassNoLine();
        
        // Link to add contract.
        if (Access.hasPermission(user, AppPaths.CONTRACTS_ADD)) {
            Link link = new Link(requestContext);
            link.setAjaxPath(AppPaths.CONTRACTS_ADD);
            link.setTitleKey("itMgmt.contractAdd.title");
            header.addHeaderCmds(link);
        }

        //
        // Template: RecordsNavigationTemplate
        //
        RecordsNavigationTemplate nav = new RecordsNavigationTemplate();
        standardTemplate.addTemplate(nav);
        nav.setRowOffset(rowStart);
        nav.setRowLimit(rowLimit);
        nav.setRowCount(rowCount);
        nav.setRowCountMsgkey("core.template.recordsNav.rownum");
        nav.setShowAllRecordsText(Localizer.getText(requestContext, "contracts.contractList.rowCount", new Object[]{rowCount}));
        nav.setShowAllRecordsPath(AppPaths.CONTRACTS_LIST + "?rowCmd=showAll");
        nav.setPath(AppPaths.CONTRACTS_LIST + "?rowStart=");

        return standardTemplate.findTemplate(STANDARD_TEMPLATE);
    }
}