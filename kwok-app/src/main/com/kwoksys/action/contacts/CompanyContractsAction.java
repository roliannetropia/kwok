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
import com.kwoksys.action.common.template.StandardV2Template;
import com.kwoksys.action.common.template.TableTemplate;
import com.kwoksys.action.common.template.TabsTemplate;
import com.kwoksys.biz.ServiceProvider;
import com.kwoksys.biz.admin.dto.AccessUser;
import com.kwoksys.biz.auth.core.Access;
import com.kwoksys.biz.contacts.ContactService;
import com.kwoksys.biz.contacts.core.CompanyTabs;
import com.kwoksys.biz.contacts.core.CompanyUtils;
import com.kwoksys.biz.contacts.dao.ContactQueries;
import com.kwoksys.biz.contacts.dto.Company;
import com.kwoksys.biz.contracts.ContractService;
import com.kwoksys.biz.contracts.core.ContractSearch;
import com.kwoksys.biz.contracts.core.ContractUtils;
import com.kwoksys.biz.contracts.dto.Contract;
import com.kwoksys.biz.system.core.AppPaths;
import com.kwoksys.framework.connections.database.QueryBits;
import com.kwoksys.framework.struts2.Action2;
import com.kwoksys.framework.ui.Link;
import com.kwoksys.framework.util.Counter;

import java.util.List;

/**
 * Action class for showing Company Contracts.
 */
public class CompanyContractsAction extends Action2 {

    public String execute() throws Exception {
        Integer companyId = requestContext.getParameter("companyId");

        AccessUser user = requestContext.getUser();

        ContactService contactService = ServiceProvider.getContactService(requestContext);
        Company company = contactService.getCompany(companyId);

        ContractService contractService = ServiceProvider.getContractService(requestContext);

        // Get column headers
        List<String> columnHeaders = ContractUtils.getColumnHeaderList();

        ContractSearch contractSearch = new ContractSearch();
        contractSearch.put(ContractSearch.CONTRACT_PROVIDER_ID_KEY, company.getId());

        QueryBits query = new QueryBits(contractSearch);
        query.addSortColumn(ContactQueries.getOrderByColumn(Contract.NAME));

        List<Contract> contracts = contractService.getContracts(query);
        List dataList = null;

        if (!contracts.isEmpty()) {
           dataList = ContractUtils.formatContractList(requestContext, contracts, new Counter());
        }

        //
        // Template: StandardV2Template
        //
        StandardV2Template standardTemplate = new StandardV2Template(requestContext);

        //
        // Template: HeaderTemplate
        //
        HeaderTemplate header = standardTemplate.getHeaderTemplate();
        header.setPageTitleKey("contactMgmt.companyDetail.header", new Object[] {company.getName()});

        // Back to company list.
        if (Access.hasPermission(user, AppPaths.CONTACTS_COMPANY_LIST)) {
            Link link = new Link(requestContext);
            link.setAjaxPath(AppPaths.CONTACTS_COMPANY_LIST);
            link.setTitleKey("contactMgmt.cmd.companyList");
            header.addHeaderCmds(link);
        }

        //
        // Template: CompanySpecTemplate
        //
        standardTemplate.addTemplate(new CompanySpecTemplate(company));

        //
        // Template: TabsTemplate
        //
        TabsTemplate tabs = new TabsTemplate();
        standardTemplate.addTemplate(tabs);
        tabs.setTabList(CompanyUtils.companyTabList(requestContext, company));
        tabs.setTabActive(CompanyTabs.CONTRACTS_TAB);

        //
        // Template: TableTemplate
        //
        TableTemplate tableTemplate = new TableTemplate();
        standardTemplate.addTemplate(tableTemplate);
        tableTemplate.setColumnHeaders(columnHeaders);
        tableTemplate.setColumnTextKey("common.column.");
        tableTemplate.setDataList(dataList);
        tableTemplate.setEmptyRowMsgKey("contracts.contractList.emptyTableMessage");

        return standardTemplate.findTemplate(STANDARD_TEMPLATE);
    }
}