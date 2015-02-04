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

import com.kwoksys.action.common.template.*;
import com.kwoksys.biz.ServiceProvider;
import com.kwoksys.biz.admin.dto.AccessUser;
import com.kwoksys.biz.auth.core.Access;
import com.kwoksys.biz.base.BaseObject;
import com.kwoksys.biz.contacts.core.ContactUtils;
import com.kwoksys.biz.contacts.dao.ContactQueries;
import com.kwoksys.biz.contacts.dto.Contact;
import com.kwoksys.biz.software.SoftwareService;
import com.kwoksys.biz.software.SoftwareUtils;
import com.kwoksys.biz.software.dto.Software;
import com.kwoksys.biz.system.core.AppPaths;
import com.kwoksys.framework.connections.database.QueryBits;
import com.kwoksys.framework.struts2.Action2;
import com.kwoksys.framework.ui.Link;
import com.kwoksys.framework.util.Counter;

import java.util.Arrays;
import java.util.List;
import java.util.Map;

/**
 * Action class for displaying software contacts.
 */
public class SoftwareContactsAction extends Action2 {

    public String execute() throws Exception {
        Integer softwareId = requestContext.getParameterInteger("softwareId");

        AccessUser accessUser = requestContext.getUser();

        SoftwareService softwareService = ServiceProvider.getSoftwareService(requestContext);

        getBaseForm(SoftwareContactForm.class);

        Software software = softwareService.getSoftware(softwareId);

        List<String> columnHeaders = Arrays.asList(Contact.FIRST_NAME, Contact.LAST_NAME, Contact.PRIMARY_EMAIL, BaseObject.REL_DESCRIPTION);

        String orderBy = Contact.FIRST_NAME;
        String order = QueryBits.ASCENDING;

        QueryBits queryBits = new QueryBits();
        queryBits.addSortColumn(ContactQueries.getOrderByColumn(orderBy));
        
        List<Contact> contacts = softwareService.getSoftwareContacts(queryBits, software.getId());
        List<Map> dataList = ContactUtils.formatContacts(requestContext, contacts, columnHeaders, new Counter());

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

        // Add Software contact
        if (Access.hasPermission(accessUser, AppPaths.SOFTWARE_CONTACT_ADD)) {
            Link link = new Link(requestContext);
            link.setAjaxPath(AppPaths.SOFTWARE_CONTACT_ADD + "?softwareId=" + software.getId());
            link.setTitleKey("common.linking.linkContacts");
            header.addHeaderCmds(link);
        }

        //
        // Template: SoftwareSpecTemplate
        //
        SoftwareSpecTemplate softwareSpecTemplate = new SoftwareSpecTemplate(software);
        standardTemplate.addTemplate(softwareSpecTemplate);

        //
        // Template: ActionErrorsTemplate
        //
        standardTemplate.addTemplate(new ActionErrorsTemplate());

        //
        // Template: TabsTemplate
        //
        TabsTemplate tabsTemplate = new TabsTemplate();
        standardTemplate.addTemplate(tabsTemplate);
        tabsTemplate.setTabList(SoftwareUtils.softwareTabList(requestContext, software));
        tabsTemplate.setTabActive(SoftwareUtils.CONTACTS_TAB);

        //
        // Template: TableTemplate
        //
        TableTemplate tableTemplate = new TableTemplate();
        standardTemplate.addTemplate(tableTemplate);
        tableTemplate.setDataList(dataList);
        tableTemplate.setColumnHeaders(columnHeaders);
        tableTemplate.setColumnPath(AppPaths.SOFTWARE_LIST);
        tableTemplate.setColumnTextKey("common.column.");
        tableTemplate.setOrderBy(orderBy);
        tableTemplate.setOrder(order);
        tableTemplate.setFormRemoveItemAction(AppPaths.SOFTWARE_CONTACT_REMOVE_2);
        tableTemplate.getFormHiddenVariableMap().put("softwareId", String.valueOf(software.getId()));
        tableTemplate.setFormRowIdName("contactId");
        tableTemplate.setEmptyRowMsgKey("contactMgmt.contactList.emptyTableMessage");

        return standardTemplate.findTemplate(STANDARD_TEMPLATE);
    }
}
