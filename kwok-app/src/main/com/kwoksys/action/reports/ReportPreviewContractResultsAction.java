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
package com.kwoksys.action.reports;

import com.kwoksys.action.common.template.RecordsNavigationTemplate;
import com.kwoksys.action.common.template.StandardTemplate;
import com.kwoksys.action.common.template.TableTemplate;
import com.kwoksys.action.contracts.ContractSearchForm;
import com.kwoksys.biz.ServiceProvider;
import com.kwoksys.biz.admin.dto.AccessUser;
import com.kwoksys.biz.admin.dto.Attribute;
import com.kwoksys.biz.auth.core.ReportAccess;
import com.kwoksys.biz.contracts.ContractService;
import com.kwoksys.biz.contracts.core.ContractSearch;
import com.kwoksys.biz.contracts.core.ContractUtils;
import com.kwoksys.biz.contracts.dao.ContractQueries;
import com.kwoksys.biz.contracts.dto.Contract;
import com.kwoksys.biz.system.core.AppPaths;
import com.kwoksys.biz.system.core.AttributeManager;
import com.kwoksys.biz.system.core.ObjectTypes;
import com.kwoksys.biz.system.core.configs.ConfigManager;
import com.kwoksys.framework.connections.database.QueryBits;
import com.kwoksys.framework.exceptions.AccessDeniedException;
import com.kwoksys.framework.properties.Localizer;
import com.kwoksys.framework.session.SessionManager;
import com.kwoksys.framework.struts2.Action2;
import com.kwoksys.framework.util.Counter;
import org.apache.struts.util.LabelValueBean;

import java.util.List;

/**
 * Action class for running a report.
 */
public class ReportPreviewContractResultsAction extends Action2 {

    public String execute() throws Exception {
        ContractSearchForm actionForm = (ContractSearchForm) getSessionBaseForm(ContractSearchForm.class);

        AccessUser user = requestContext.getUser();
        String reportType = requestContext.getParameterString("reportType");

        // Checks report permission
        if (!ReportAccess.hasPermission(user, reportType)) {
            throw new AccessDeniedException();
        }

        String rowCmd = requestContext.getParameterString("rowCmd");
        String orderBy = requestContext.getParameterString("orderBy", Contract.NAME);
        String order = requestContext.getParameterString("order");
        int rowStart = requestContext.getParameter("rowStart", 0);

        int rowLimit = requestContext.getParameter("rowLimit", ConfigManager.app.getContractsRowsToShow());
        if (rowCmd.equals("showAll")) {
            rowLimit = 0;
        }

        ContractSearch contractSearch = new ContractSearch(requestContext, SessionManager.CONTRACT_REPORT_CRITERIA_MAP);
        contractSearch.prepareMap(actionForm);

        QueryBits query = new QueryBits(contractSearch);
        query.setLimit(rowLimit, rowStart);

        if (ContractUtils.isSortableColumn(orderBy)) {
            query.addSortColumn(ContractQueries.getOrderByColumn(orderBy), order);
        }

        ContractService contractService = ServiceProvider.getContractService(requestContext);

        int rowCount = contractService.getContractCount(query);
        List dataList = null;

        if (rowCount != 0) {
            List<Contract> contractList = contractService.getContracts(query);
            dataList = ContractUtils.formatContractList(requestContext, contractList, new Counter(rowStart));
        }

        //
        // Template: StandardTemplate
        //
        StandardTemplate standardTemplate = new StandardTemplate(requestContext);

        //
        // Template: ReportPreviewResultsTemplate
        //
        ReportPreviewResultsTemplate preview = new ReportPreviewResultsTemplate();
        standardTemplate.addTemplate(preview);
        preview.setReportType(reportType);

        for (String column : ConfigManager.app.getContractsExportColumns()) {
            preview.addReportColumnOptions(new LabelValueBean(Localizer.getText(requestContext, "common.column." + column), column));
        }
        for (Attribute attr : new AttributeManager(requestContext).getCustomFieldList(ObjectTypes.CONTRACT)) {
            preview.addReportColumnOptions(new LabelValueBean(attr.getName(), String.valueOf(attr.getId())));
        }

        for (String column : ContractUtils.getSortableColumns()) {
            preview.addSortColumnOptions(new LabelValueBean(Localizer.getText(requestContext, "common.column." + column), column));
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
        nav.setShowAllRecordsPath(AppPaths.REPORTS_CONTRACT_SEARCH + "?reportType=" + reportType + "&rowCmd=showAll");
        nav.setPath(AppPaths.REPORTS_CONTRACT_SEARCH + "?reportType=" + reportType + "&rowStart=");

        //
        // Template: TableTemplate
        //
        TableTemplate tableHeader = new TableTemplate();
        standardTemplate.addTemplate(tableHeader);
        tableHeader.setDataList(dataList);
        tableHeader.setColumnHeaders(ContractUtils.getColumnHeaderList());
        tableHeader.setColumnPath(AppPaths.REPORTS_CONTRACT_SEARCH);
        tableHeader.setColumnTextKey("common.column.");
        tableHeader.setRowCmd(rowCmd);
        tableHeader.setEmptyRowMsgKey("itMgmt.contractList.emptyTableMessage");

        return standardTemplate.findTemplate(STANDARD_TEMPLATE);
    }
}