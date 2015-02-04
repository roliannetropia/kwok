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

import com.kwoksys.action.common.template.*;
import com.kwoksys.action.hardware.HardwareListTemplate;
import com.kwoksys.biz.ServiceProvider;
import com.kwoksys.biz.admin.dto.AccessUser;
import com.kwoksys.biz.auth.core.Access;
import com.kwoksys.biz.contracts.ContractService;
import com.kwoksys.biz.contracts.core.ContractUtils;
import com.kwoksys.biz.contracts.dto.Contract;
import com.kwoksys.biz.hardware.core.HardwareUtils;
import com.kwoksys.biz.hardware.dao.HardwareQueries;
import com.kwoksys.biz.hardware.dto.Hardware;
import com.kwoksys.biz.software.SoftwareUtils;
import com.kwoksys.biz.software.dao.SoftwareQueries;
import com.kwoksys.biz.software.dto.Software;
import com.kwoksys.biz.system.core.AppPaths;
import com.kwoksys.framework.connections.database.QueryBits;
import com.kwoksys.framework.properties.Localizer;
import com.kwoksys.framework.session.SessionManager;
import com.kwoksys.framework.struts2.Action2;
import com.kwoksys.framework.ui.Link;
import com.kwoksys.framework.util.Counter;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * Action class for contract detail.
 */
public class ContractRelationshipAction extends Action2 {

    public String execute() throws Exception {
        AccessUser accessUser = requestContext.getUser();

        Integer contractId = requestContext.getParameter("contractId");

        ContractService contractService = ServiceProvider.getContractService(requestContext);
        Contract contract = contractService.getContract(contractId);

        getBaseForm(ContractRelationshipForm.class);

        //
        // Linked hardware
        //
        String hwOrderBy = SessionManager.getOrSetAttribute(requestContext, "hwOrderBy", SessionManager.HARDWARE_ORDER_BY, Hardware.HARDWARE_NAME);
        String hwOrder = SessionManager.getOrSetAttribute(requestContext, "hwOrder", SessionManager.HARDWARE_ORDER, QueryBits.ASCENDING);

        boolean canRemoveHardware = Access.hasPermission(accessUser, AppPaths.CONTRACTS_HARDWARE_REMOVE_2);

        // Get column headers
        List hwColumnHeaders = new ArrayList();
        if (canRemoveHardware) {
            // Add an extra blank column to the headers, that's for the radio button to remove hardware
            hwColumnHeaders.add("");
        }
        hwColumnHeaders.addAll(HardwareUtils.getColumnHeaderList());

        // Do some sorting.
        QueryBits query = new QueryBits();

        if (HardwareUtils.isSortableColumn(hwOrderBy)) {
            query.addSortColumn(HardwareQueries.getOrderByColumn(hwOrderBy), hwOrder);
        }

        //
        // Template: StandardTemplate
        //
        StandardTemplate standardTemplate = new StandardTemplate(requestContext);
        standardTemplate.setPathAttribute("ajaxHardwareDetailPath", AppPaths.IT_MGMT_AJAX_GET_HARDWARE_DETAIL + "?hardwareId=");
        standardTemplate.setPathAttribute("formRemoveHardwareAction", AppPaths.CONTRACTS_HARDWARE_REMOVE_2);

        //
        // Template: HardwareListTemplate
        //
        HardwareListTemplate listTemplate = new HardwareListTemplate("_hardware");
        standardTemplate.addTemplate(listTemplate);
        listTemplate.setHardwareList(contractService.getContractHardwareList(query, contractId));
        listTemplate.setCanRemoveHardware(canRemoveHardware);
        listTemplate.setColspan(hwColumnHeaders.size());
        listTemplate.setCounter(new Counter());
        listTemplate.getFormHiddenVariableMap().put("contractId", String.valueOf(contract.getId()));

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
        tableHeader.setColumnPath(AppPaths.CONTRACTS_ITEMS + "?contractId=" + contractId);
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

        List<Software> softwareList = contractService.getContractSoftwareList(swQuery, contractId);
        List<Map> formattedList = SoftwareUtils.formatSoftwareList(requestContext, softwareList, new Counter());

        //
        // Template: TableTemplate
        //
        TableTemplate tableTemplate = new TableTemplate("_software");
        standardTemplate.addTemplate(tableTemplate);
        tableTemplate.setDataList(formattedList);
        tableTemplate.setColumnHeaders(SoftwareUtils.getColumnHeaderList());
        tableTemplate.setColumnPath(AppPaths.CONTRACTS_ITEMS + "?contractId=" + contractId);
        tableTemplate.setSortableColumnHeaders(SoftwareUtils.getSortableColumns());
        tableTemplate.setColumnTextKey("common.column.");
        tableTemplate.setEmptyRowMsgKey("itMgmt.softwareList.emptyTableMessage");
        tableTemplate.setOrderBy(swOrderBy);
        tableTemplate.setOrder(swOrder);
        tableTemplate.setOrderByParamName("swOrderBy");
        tableTemplate.setOrderParamName("swOrder");
        tableTemplate.setFormRemoveItemAction(AppPaths.CONTRACTS_SOFTWARE_REMOVE_2);
        tableTemplate.getFormHiddenVariableMap().put("contractId", String.valueOf(contract.getId()));
        tableTemplate.setFormRowIdName("formSoftwareId");

        //
        // Template: ContractSpecTemplate
        //
        standardTemplate.addTemplate(new ContractSpecTemplate(contract));

        //
        // Template: HeaderTemplate
        //
        HeaderTemplate header = standardTemplate.getHeaderTemplate();
        header.setPageTitleKey("itMgmt.contractDetail.header", new Object[] {contract.getName()});

        if (Access.hasPermission(accessUser, AppPaths.CONTRACTS_HARDWARE_ADD)) {
            Link link = new Link(requestContext);
            link.setAjaxPath(AppPaths.CONTRACTS_HARDWARE_ADD + "?contractId=" + contractId);
            link.setTitleKey("common.linking.linkHardware");
            header.addHeaderCmds(link);
        }

        if (Access.hasPermission(accessUser, AppPaths.CONTRACTS_SOFTWARE_ADD)) {
            Link link = new Link(requestContext);
            link.setAjaxPath(AppPaths.CONTRACTS_SOFTWARE_ADD + "?contractId=" + contractId);
            link.setTitleKey("common.linking.linkSoftware");
            header.addHeaderCmds(link);
        }

        Link link = new Link(requestContext);
        link.setAjaxPath(AppPaths.CONTRACTS_LIST);
        link.setTitleKey("itMgmt.cmd.contractList");
        header.addHeaderCmds(link);

        //
        // Template: TabsTemplate
        //
        TabsTemplate tabs = new TabsTemplate();
        standardTemplate.addTemplate(tabs);
        tabs.setTabList(ContractUtils.contractTabList(requestContext, contract));
        tabs.setTabActive(ContractUtils.HARDWARE_TAB);

        return standardTemplate.findTemplate(STANDARD_TEMPLATE);
    }
}