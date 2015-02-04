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
import com.kwoksys.biz.contacts.core.ContactUtils;
import com.kwoksys.biz.contacts.dao.ContactQueries;
import com.kwoksys.biz.contacts.dto.Company;
import com.kwoksys.biz.contacts.dto.Contact;
import com.kwoksys.biz.system.core.AppPaths;
import com.kwoksys.biz.system.core.Image;
import com.kwoksys.biz.system.core.ObjectTypes;
import com.kwoksys.framework.connections.database.QueryBits;
import com.kwoksys.framework.session.SessionManager;
import com.kwoksys.framework.struts2.Action2;
import com.kwoksys.framework.ui.Link;
import com.kwoksys.framework.util.Counter;

import java.util.List;

/**
 * Action class for showing employee contacts.
 */
public class CompanyContactAction extends Action2 {

    public String execute() throws Exception {
        AccessUser user = requestContext.getUser();

        Integer companyId = requestContext.getParameter("companyId");
        String orderBy = SessionManager.getOrSetAttribute(requestContext, "orderBy", SessionManager.COMPANY_CONTACTS_ORDER_BY, Contact.FIRST_NAME);
        String order = SessionManager.getOrSetAttribute(requestContext, "order", SessionManager.COMPANY_CONTACTS_ORDER, QueryBits.ASCENDING);

        // Call the service
        ContactService contactService = ServiceProvider.getContactService(requestContext);
        Company company = contactService.getCompany(companyId);

        // Ready to pass variables to query.
        QueryBits query = new QueryBits();

        if (ContactUtils.isSortableColumn(orderBy)) {
            query.addSortColumn(ContactQueries.getOrderByColumn(orderBy), order);
        }

        // Get column heading
        List<String> columnHeaders = ContactUtils.getEmployeeContactColumnHeader();

        List<Contact> contacts = contactService.getCompanyContacts(query, company.getId(), ObjectTypes.COMPANY_EMPLOYEE_CONTACT);
        List dataList = ContactUtils.formatContacts(requestContext, contacts, columnHeaders, new Counter());

        //
        // Template: StandardV2Template
        //
        StandardV2Template standardTemplate = new StandardV2Template(requestContext);
        standardTemplate.setAttribute("companyId", companyId);

        //
        // Template: CompanySpecTemplate
        //
        CompanySpecTemplate tmpl = new CompanySpecTemplate(company);
        standardTemplate.addTemplate(tmpl);

        //
        // Template: HeaderTemplate
        //
        HeaderTemplate header = standardTemplate.getHeaderTemplate();
        header.setPageTitleKey("contactMgmt.companyDetail.header", new Object[] {company.getName()});

        // Add Contact
        if (Access.hasPermission(user, AppPaths.CONTACTS_CONTACT_ADD)) {
            Link link = new Link(requestContext);
            link.setAjaxPath(AppPaths.CONTACTS_CONTACT_ADD + "?companyId=" + companyId);
            link.setTitleKey("contactMgmt.cmd.employeeContactAdd");
            header.addHeaderCmds(link);
        }

        // Export Contact
        if (Access.hasPermission(user, AppPaths.CONTACTS_COMPANY_CONTACT_EXPORT)) {
            Link link = new Link(requestContext);
            link.setExportPath(AppPaths.CONTACTS_COMPANY_CONTACT_EXPORT + "?companyId=" + companyId);
            link.setTitleKey("contactMgmt.cmd.companyOtherContactsExport");
            link.setImgSrc(Image.getInstance().getCsvFileIcon());
            header.addHeaderCmds(link);
        }

        // Company list
        if (Access.hasPermission(user, AppPaths.CONTACTS_COMPANY_LIST)) {
            Link link = new Link(requestContext);
            link.setAjaxPath(AppPaths.CONTACTS_COMPANY_LIST);
            link.setTitleKey("contactMgmt.cmd.companyList");
            header.addHeaderCmds(link);
        }

        //
        // Template: TabsTemplate
        //
        TabsTemplate tabs = new TabsTemplate();
        standardTemplate.addTemplate(tabs);
        tabs.setTabList(CompanyUtils.companyTabList(requestContext, company));
        tabs.setTabActive(CompanyTabs.OTHER_CONTACT_TAB);

        //
        // Template: TableTemplate
        //
        TableTemplate tableHeader = new TableTemplate();
        standardTemplate.addTemplate(tableHeader);
        tableHeader.setDataList(dataList);
        tableHeader.setColumnHeaders(columnHeaders);
        tableHeader.setSortableColumnHeaders(ContactUtils.getSortableContactColumnList());
        tableHeader.setColumnPath(AppPaths.CONTACTS_COMPANY_CONTACT + "?companyId=" + companyId);
        tableHeader.setColumnTextKey("common.column.");
        tableHeader.setOrderBy(orderBy);
        tableHeader.setOrder(order);
        tableHeader.setEmptyRowMsgKey("contactMgmt.companyContact.emptyTableMessage");

        return standardTemplate.findTemplate(STANDARD_TEMPLATE);
    }
}
