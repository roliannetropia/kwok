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
import com.kwoksys.action.contacts.ContactAssociateForm;
import com.kwoksys.biz.ServiceProvider;
import com.kwoksys.biz.admin.dto.AccessUser;
import com.kwoksys.biz.auth.core.Access;
import com.kwoksys.biz.base.BaseObject;
import com.kwoksys.biz.contacts.core.ContactUtils;
import com.kwoksys.biz.contacts.dao.ContactQueries;
import com.kwoksys.biz.contacts.dto.Contact;
import com.kwoksys.biz.contracts.ContractService;
import com.kwoksys.biz.contracts.core.ContractUtils;
import com.kwoksys.biz.contracts.dto.Contract;
import com.kwoksys.biz.system.core.AppPaths;
import com.kwoksys.framework.connections.database.QueryBits;
import com.kwoksys.framework.struts2.Action2;
import com.kwoksys.framework.ui.Link;
import com.kwoksys.framework.util.Counter;

import java.util.Arrays;
import java.util.List;
import java.util.Map;

/**
 * Action class for contract contacts.
 */
public class ContractContactsAction extends Action2 {

    public String execute() throws Exception {
        getBaseForm(ContactAssociateForm.class);

        AccessUser accessUser = requestContext.getUser();

        Integer contractId = requestContext.getParameter("contractId");

        ContractService contractService = ServiceProvider.getContractService(requestContext);
        Contract contract = contractService.getContract(contractId);

        List<String> columnHeaders = Arrays.asList(Contact.FIRST_NAME, Contact.LAST_NAME, Contact.PRIMARY_EMAIL, BaseObject.REL_DESCRIPTION);

        String orderBy = Contact.FIRST_NAME;
        String order = QueryBits.ASCENDING;

        QueryBits queryBits = new QueryBits();
        queryBits.addSortColumn(ContactQueries.getOrderByColumn(orderBy));

        List<Contact> contacts = contractService.getContractContacts(queryBits, contract.getId());
        List<Map> dataList = ContactUtils.formatContacts(requestContext, contacts, columnHeaders, new Counter());

        //
        // Template: StandardV2Template
        //
        StandardV2Template standardTemplate = new StandardV2Template(requestContext);

        //
        // Template: HeaderTemplate
        //
        HeaderTemplate header = standardTemplate.getHeaderTemplate();
        header.setPageTitleKey("itMgmt.contractDetail.header", new Object[] {contract.getName()});

        // Add contract contact.
        if (Access.hasPermission(accessUser, AppPaths.CONTRACTS_CONTACT_ADD)) {
            Link link = new Link(requestContext);
            link.setAjaxPath(AppPaths.CONTRACTS_CONTACT_ADD + "?contractId=" + contract.getId());
            link.setTitleKey("common.linking.linkContacts");
            header.addHeaderCmds(link);
        }

        Link link = new Link(requestContext);
        link.setAjaxPath(AppPaths.CONTRACTS_LIST);
        link.setTitleKey("itMgmt.cmd.contractList");
        header.addHeaderCmds(link);

        //
        // Template: ContractSpecTemplate
        //
        ContractSpecTemplate template = new ContractSpecTemplate(contract);
        standardTemplate.addTemplate(template);

        //
        // Template: ActionErrorsTemplate
        //
        standardTemplate.addTemplate(new ActionErrorsTemplate());

        //
        // Template: TabsTemplate
        //
        TabsTemplate tabs = new TabsTemplate();
        standardTemplate.addTemplate(tabs);
        tabs.setTabList(ContractUtils.contractTabList(requestContext, contract));
        tabs.setTabActive(ContractUtils.CONTACTS_TAB);

        //
        // Template: TableTemplate
        //
        TableTemplate tableTemplate = new TableTemplate();
        standardTemplate.addTemplate(tableTemplate);
        tableTemplate.setDataList(dataList);
        tableTemplate.setColumnHeaders(columnHeaders);
        tableTemplate.setColumnTextKey("common.column.");
        tableTemplate.setOrderBy(orderBy);
        tableTemplate.setOrder(order);
        tableTemplate.setFormRemoveItemAction(AppPaths.CONTRACTS_CONTACT_REMOVE_2);
        tableTemplate.getFormHiddenVariableMap().put("contractId", String.valueOf(contract.getId()));
        tableTemplate.setFormRowIdName("contactId");
        tableTemplate.setEmptyRowMsgKey("contactMgmt.contactList.emptyTableMessage");

        return standardTemplate.findTemplate(STANDARD_TEMPLATE);
    }
}