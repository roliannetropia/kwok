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

import com.kwoksys.action.common.template.*;
import com.kwoksys.action.contacts.ContactAssociateForm;
import com.kwoksys.biz.ServiceProvider;
import com.kwoksys.biz.admin.dto.AccessUser;
import com.kwoksys.biz.auth.core.Access;
import com.kwoksys.biz.base.BaseObject;
import com.kwoksys.biz.contacts.ContactService;
import com.kwoksys.biz.contacts.core.ContactUtils;
import com.kwoksys.biz.contacts.dao.ContactQueries;
import com.kwoksys.biz.contacts.dto.Contact;
import com.kwoksys.biz.hardware.HardwareService;
import com.kwoksys.biz.hardware.core.HardwareUtils;
import com.kwoksys.biz.hardware.dto.Hardware;
import com.kwoksys.biz.system.core.AppPaths;
import com.kwoksys.biz.system.dto.linking.HardwareContactLink;
import com.kwoksys.framework.connections.database.QueryBits;
import com.kwoksys.framework.session.SessionManager;
import com.kwoksys.framework.struts2.Action2;
import com.kwoksys.framework.ui.Link;
import com.kwoksys.framework.util.Counter;

import java.util.Arrays;
import java.util.List;
import java.util.Map;

/**
 * Action class for showing hardware contacts.
 */
public class HardwareContactsAction extends Action2 {

    public String execute() throws Exception {
        getBaseForm(ContactAssociateForm.class);
        AccessUser user = requestContext.getUser();

        // Get request parameters
        Integer hardwareId = requestContext.getParameter("hardwareId");

        HardwareService hardwareService = ServiceProvider.getHardwareService(requestContext);
        Hardware hardware = hardwareService.getHardware(hardwareId);

        // Hardware members
        String orderBy = SessionManager.getOrSetAttribute(requestContext, "orderBy", SessionManager.HARDWARE_CONTACTS_ORDER_BY, Contact.FIRST_NAME);
        String order = SessionManager.getOrSetAttribute(requestContext, "order", SessionManager.HARDWARE_CONTACTS_ORDER, QueryBits.ASCENDING);

        List<String> columnHeaders = Arrays.asList(Contact.FIRST_NAME, Contact.LAST_NAME, Contact.PRIMARY_EMAIL, BaseObject.REL_DESCRIPTION);

        // Do some sorting.
        QueryBits query = new QueryBits();

        if (ContactUtils.isSortableColumn(orderBy)) {
            query.addSortColumn(ContactQueries.getOrderByColumn(orderBy), order);
        }

        HardwareContactLink hardwareContact = new HardwareContactLink();
        hardwareContact.setHardwareId(hardwareId);

        ContactService contactService = ServiceProvider.getContactService(requestContext);
        List<Contact> contacts = contactService.getLinkedContacts(query, hardwareContact.createObjectMap());
        List<Map> dataList = ContactUtils.formatContacts(requestContext, contacts, columnHeaders, new Counter());

        //
        // Template: StandardV2Template
        //
        StandardV2Template standardTemplate = new StandardV2Template(requestContext);

        //
        // Template: HardwareSpecTemplate
        //
        HardwareSpecTemplate tmpl = new HardwareSpecTemplate(hardware);
        standardTemplate.addTemplate(tmpl);

        //
        // Template: HeaderTemplate
        //
        HeaderTemplate header = standardTemplate.getHeaderTemplate();
        header.setPageTitleKey("itMgmt.hardwareDetail.header", new Object[] {hardware.getName()});
        HardwareUtils.addHardwareHeaderCommands(requestContext, header, hardwareId);

        // Add hardware contact
        if (Access.hasPermission(user, AppPaths.HARDWARE_CONTACT_ADD)) {
            Link link = new Link(requestContext);
            link.setAjaxPath(AppPaths.HARDWARE_CONTACT_ADD + "?hardwareId=" + hardwareId);
            link.setTitleKey("common.linking.linkContacts");
            header.addHeaderCmds(link);
        }

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
        tabs.setTabActive(HardwareUtils.HARDWARE_CONTACT_TAB);

        //
        // Template: TableTemplate
        //
        TableTemplate tableTemplate = new TableTemplate();
        standardTemplate.addTemplate(tableTemplate);
        tableTemplate.setDataList(dataList);
        tableTemplate.setColumnHeaders(columnHeaders);
        tableTemplate.setSortableColumnHeaders(ContactUtils.getSortableContactColumnList());
        tableTemplate.setColumnPath(AppPaths.HARDWARE_CONTACTS + "?hardwareId=" + hardwareId);
        tableTemplate.setColumnTextKey("common.column.");
        tableTemplate.setRowCmd(null);
        tableTemplate.setOrderBy(orderBy);
        tableTemplate.setOrder(order);
        tableTemplate.setEmptyRowMsgKey("contactMgmt.contactList.emptyTableMessage");
        tableTemplate.getFormHiddenVariableMap().put("hardwareId", String.valueOf(hardware.getId()));
        tableTemplate.setFormRowIdName("contactId");
        tableTemplate.setFormRemoveItemAction(AppPaths.HARDWARE_CONTACT_REMOVE);

        return standardTemplate.findTemplate(STANDARD_TEMPLATE);
    }
}