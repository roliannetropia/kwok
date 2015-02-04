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

import com.kwoksys.action.common.template.*;
import com.kwoksys.biz.ServiceProvider;
import com.kwoksys.biz.admin.dto.AccessUser;
import com.kwoksys.biz.auth.core.Access;
import com.kwoksys.biz.contacts.ContactService;
import com.kwoksys.biz.contacts.core.CompanyTabs;
import com.kwoksys.biz.contacts.core.CompanyUtils;
import com.kwoksys.biz.contacts.dao.ContactQueries;
import com.kwoksys.biz.contacts.dto.Company;
import com.kwoksys.biz.contacts.dto.Contact;
import com.kwoksys.biz.system.core.AppPaths;
import com.kwoksys.biz.system.core.ObjectTypes;
import com.kwoksys.framework.connections.database.QueryBits;
import com.kwoksys.framework.properties.Localizer;
import com.kwoksys.framework.struts2.Action2;
import com.kwoksys.framework.ui.Link;
import com.kwoksys.framework.util.HtmlUtils;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Action class for showing company detail.
 */
public class CompanyDetailAction extends Action2 {

    public String execute() throws Exception {
        Integer companyId = requestContext.getParameter("companyId");

        AccessUser user = requestContext.getUser();

        ContactService contactService = ServiceProvider.getContactService(requestContext);
        Company company = contactService.getCompany(companyId);

        boolean canEditContact = Access.hasPermission(user, AppPaths.CONTACTS_COMPANY_CONTACT_EDIT);
        boolean canDeleteContact = Access.hasPermission(user, AppPaths.CONTACTS_COMPANY_CONTACT_DELETE);

        // Ready to pass variables to query.
        QueryBits query = new QueryBits();
        query.addSortColumn(ContactQueries.getOrderByColumn(Contact.TITLE));

        List<Contact> contacts = contactService.getCompanyContacts(query, companyId, ObjectTypes.COMPANY_MAIN_CONTACT);

        //
        // Template: StandardTemplate
        //
        StandardTemplate standardTemplate = new StandardTemplate(requestContext);
        standardTemplate.setAttribute("companyId", companyId);
        standardTemplate.setAttribute("companyListPath", AppPaths.CONTACTS_COMPANY_LIST + "?cmd=search&companyTag=");

        if (!contacts.isEmpty()) {
            List mainContactList = new ArrayList();

            for (Contact contact : contacts) {
                Map map = new HashMap();
                map.put("contact", contact);
                map.put("contactNote", HtmlUtils.formatMultiLineDisplay(contact.getDescription()));
                map.put("contactPhoneWork", HtmlUtils.encode(contact.getPhoneWork()));
                map.put("contactFax", HtmlUtils.encode(contact.getFax()));
                map.put("contactEmailPrimary", HtmlUtils.formatMailtoLink(contact.getEmailPrimary()));
                map.put("contactHomepageUrl", HtmlUtils.formatExternalLink(requestContext, contact.getHomepageUrl()));

                if (canEditContact) {
                    map.put("contactEditPath", AppPaths.ROOT + AppPaths.CONTACTS_COMPANY_CONTACT_EDIT + "?contactId=" + contact.getId());
                }
                if (canDeleteContact) {
                    map.put("contactDeletePath", AppPaths.ROOT + AppPaths.CONTACTS_COMPANY_CONTACT_DELETE + "?contactId=" + contact.getId());
                }
                mainContactList.add(map);
            }
            standardTemplate.setAttribute("mainContactList", mainContactList);

        } else {
            //
            // Template: TableEmptyTemplate
            //
            TableEmptyTemplate empty = new TableEmptyTemplate();
            standardTemplate.addTemplate(empty);
            empty.setRowText(Localizer.getText(requestContext, "contactMgmt.companyDetail.emptyTableMessage"));
        }

        //
        // Template: CompanySpecTemplate
        //
        CompanySpecTemplate tmpl = new CompanySpecTemplate(company);
        standardTemplate.addTemplate(tmpl);
        tmpl.setPopulateCompanyTagList(true);

        //
        // Template: HeaderTemplate
        //
        HeaderTemplate header = standardTemplate.getHeaderTemplate();
        header.setPageTitleKey("contactMgmt.companyDetail.header", new Object[] {company.getName()});

        // Link to company edit page
        if (Access.hasPermission(user, AppPaths.CONTACTS_COMPANY_EDIT)) {
            Link link = new Link(requestContext);
            link.setAjaxPath(AppPaths.CONTACTS_COMPANY_EDIT + "?companyId=" + companyId);
            link.setTitleKey("contactMgmt.companyEdit.title");
            header.addHeaderCmds(link);
        }

        // Link to delete Company page
        if (Access.hasPermission(user, AppPaths.CONTACTS_COMPANY_DELETE)) {
            Link link = new Link(requestContext);
            link.setAjaxPath(AppPaths.CONTACTS_COMPANY_DELETE + "?companyId=" + companyId);
            link.setTitleKey("contactMgmt.companyDelete.title");
            header.addHeaderCmds(link);
        }

        // Link to add company contacts
        if (Access.hasPermission(user, AppPaths.CONTACTS_COMPANY_CONTACT_ADD)) {
            Link link = new Link(requestContext);
            link.setAjaxPath(AppPaths.CONTACTS_COMPANY_CONTACT_ADD + "?companyId=" + companyId);
            link.setTitleKey("contactMgmt.cmd.companyContactAdd");
            header.addHeaderCmds(link);
        }

        // Link to company list
        if (Access.hasPermission(user, AppPaths.CONTACTS_COMPANY_LIST)) {
            Link link = new Link(requestContext);
            link.setAjaxPath(AppPaths.CONTACTS_COMPANY_LIST);
            link.setTitleKey("contactMgmt.cmd.companyList");
            header.addHeaderCmds(link);
        }

        //
        //  Template: TabsTemplate
        //
        TabsTemplate tabs = new TabsTemplate();
        standardTemplate.addTemplate(tabs);
        tabs.setTabList(CompanyUtils.companyTabList(requestContext, company));
        tabs.setTabActive(CompanyTabs.MAIN_CONTACT_TAB);

        //
        // Template: CustomFieldsTemplate
        //
        CustomFieldsTemplate customFieldsTemplate = new CustomFieldsTemplate();
        standardTemplate.addTemplate(customFieldsTemplate);
        customFieldsTemplate.setObjectTypeId(ObjectTypes.COMPANY);
        customFieldsTemplate.setObjectId(companyId);
        customFieldsTemplate.setShowDefaultHeader(false);

        return standardTemplate.findTemplate(STANDARD_TEMPLATE);
    }
}