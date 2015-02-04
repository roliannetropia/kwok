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
import com.kwoksys.biz.contacts.ContactService;
import com.kwoksys.biz.contacts.core.ContactSearch;
import com.kwoksys.biz.contacts.core.ContactUtils;
import com.kwoksys.biz.contacts.dao.ContactQueries;
import com.kwoksys.biz.contacts.dto.Contact;
import com.kwoksys.biz.system.core.AppPaths;
import com.kwoksys.biz.system.core.ObjectTypes;
import com.kwoksys.biz.system.core.configs.ConfigManager;
import com.kwoksys.framework.connections.database.QueryBits;
import com.kwoksys.framework.properties.Localizer;
import com.kwoksys.framework.session.SessionManager;
import com.kwoksys.framework.struts2.Action2;
import com.kwoksys.framework.util.Counter;

import javax.servlet.http.HttpSession;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * Action class for showing contact list.
 */
public class ContactListAction extends Action2 {

    public String execute() throws Exception {
        ContactSearchForm actionForm = (ContactSearchForm) getSessionBaseForm(ContactSearchForm.class);
        String cmd = requestContext.getParameterString("cmd");
        String rowCmd = requestContext.getParameterString("rowCmd");
        String orderBy = SessionManager.getOrSetAttribute(requestContext, "orderBy", SessionManager.CONTACTS_ORDER_BY, Contact.FIRST_NAME);
        String order = SessionManager.getOrSetAttribute(requestContext, "order", SessionManager.CONTACTS_ORDER, QueryBits.ASCENDING);

        int rowStart = 0;
        if (!cmd.isEmpty() || rowCmd.equals("showAll")) {
            request.getSession().setAttribute(SessionManager.CONTACTS_ROW_START, rowStart);
        } else {
            rowStart = SessionManager.getOrSetAttribute(requestContext, "rowStart", SessionManager.CONTACTS_ROW_START, rowStart);
        }

        int rowLimit = requestContext.getParameter("rowLimit", ConfigManager.app.getContactRows());
        if (rowCmd.equals("showAll")) {
            rowLimit = 0;
        }
       
        HttpSession session = request.getSession();

        // Getting search criteria map from session variable.
        ContactSearch contactSearch = new ContactSearch();

        if (!cmd.isEmpty()) {
            if (cmd.equals("search")) {
                // We are expecting user to enter some search criteria.
                contactSearch.prepareMap(actionForm);
                contactSearch.put(ContactSearch.CONTACT_TYPE, ObjectTypes.COMPANY_EMPLOYEE_CONTACT);

            } else if (cmd.equals("showAll")) {
                // We're expecting to reset the search criteria.
            }

            // This is for so that we know what the user is searching for.
            contactSearch.put("cmd", cmd);
            session.setAttribute(SessionManager.CONTACT_SEARCH_CRITERIA_MAP, contactSearch.getSearchCriteriaMap());

        } else if (session.getAttribute(SessionManager.CONTACT_SEARCH_CRITERIA_MAP) != null) {
            contactSearch.setSearchCriteriaMap((Map) session.getAttribute(SessionManager.CONTACT_SEARCH_CRITERIA_MAP));
        }

        // Pass variables to query.
        QueryBits query = new QueryBits(contactSearch);
        query.setLimit(rowLimit, rowStart);

        if (ContactUtils.isSortableColumn(orderBy)) {
            query.addSortColumn(ContactQueries.getOrderByColumn(orderBy), order);
        }

        // Get column headers
        List<String> columnHeaders = ContactUtils.getContactColumnHeader();

        ContactService contactService = ServiceProvider.getContactService(requestContext);

        int rowCount = contactService.getContactCount(query);

        List dataList = new ArrayList();

        if (rowCount != 0) {
            Counter counter = new Counter(rowStart);
            List<Contact> contacts = contactService.getContacts(query);
            dataList = ContactUtils.formatContacts(requestContext, contacts, columnHeaders, counter);
        }

        //
        // Template: StandardV2Template
        //
        StandardV2Template standardTemplate = new StandardV2Template(requestContext);

        //
        // Template: HeaderTemplate
        //
        HeaderTemplate headerTemplate = standardTemplate.getHeaderTemplate();
        headerTemplate.setTitleKey("contactMgmt.contactList.title");
        headerTemplate.setTitleClassNoLine();

        //
        // Template: RecordsNavigationTemplate
        //
        RecordsNavigationTemplate recordNavTemplate = new RecordsNavigationTemplate();
        standardTemplate.addTemplate(recordNavTemplate);
        recordNavTemplate.setInfoText(Localizer.getText(requestContext, "contactMgmt.contactList.searchResult"));
        recordNavTemplate.setRowOffset(rowStart);
        recordNavTemplate.setRowLimit(rowLimit);
        recordNavTemplate.setRowCount(rowCount);
        recordNavTemplate.setRowCountMsgkey("core.template.recordsNav.rownum");
        recordNavTemplate.setShowAllRecordsText(Localizer.getText(requestContext, "contactMgmt.contactList.rowCount", new Object[]{rowCount}));
        recordNavTemplate.setShowAllRecordsPath(AppPaths.CONTACTS_CONTACT_LIST + "?rowCmd=showAll");
        recordNavTemplate.setPath(AppPaths.CONTACTS_CONTACT_LIST + "?rowStart=");

        //
        // Template: TableTemplate
        //
        TableTemplate tableTemplate = new TableTemplate();
        standardTemplate.addTemplate(tableTemplate);
        tableTemplate.setDataList(dataList);
        tableTemplate.setColumnHeaders(columnHeaders);
        tableTemplate.setSortableColumnHeaders(ContactUtils.getSortableContactColumnList());
        tableTemplate.setColumnPath(AppPaths.CONTACTS_CONTACT_LIST);
        tableTemplate.setColumnTextKey("common.column.");
        tableTemplate.setRowCmd(null);
        tableTemplate.setOrderBy(orderBy);
        tableTemplate.setOrder(order);
        tableTemplate.setEmptyRowMsgKey("contactMgmt.contactList.emptyTableMessage");

        return standardTemplate.findTemplate(STANDARD_TEMPLATE);
    }
}

