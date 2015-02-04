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
package com.kwoksys.biz.contacts.core;

import com.kwoksys.biz.ServiceProvider;
import com.kwoksys.biz.admin.dto.AccessUser;
import com.kwoksys.biz.auth.core.Access;
import com.kwoksys.biz.contacts.ContactService;
import com.kwoksys.biz.contacts.dto.Company;
import com.kwoksys.biz.system.core.AppPaths;
import com.kwoksys.biz.system.core.AttributeFieldIds;
import com.kwoksys.framework.properties.Localizer;
import com.kwoksys.framework.util.Counter;
import com.kwoksys.framework.http.RequestContext;
import com.kwoksys.framework.connections.database.QueryBits;
import com.kwoksys.framework.exceptions.DatabaseException;
import com.kwoksys.framework.ui.Link;
import com.kwoksys.framework.ui.RowStyle;
import org.apache.struts.util.LabelValueBean;

import java.util.*;

/**
 * Utility class for Company module.
 */
public class CompanyUtils {

    public static boolean isSortableColumn(String columnName) {
        return false;
    }

    public static List formatCompanyList(RequestContext requestContext, List<Company> companyDataset, Counter counter)
            throws DatabaseException {

        List list = new ArrayList();
        if (companyDataset == null) {
            return list;
        }

        boolean hasCompanyDetailsAccess = Access.hasPermission(requestContext.getUser(), AppPaths.CONTACTS_COMPANY_DETAIL);

        RowStyle ui = new RowStyle();
        List<String> columnHeaders = Company.getCompanyColumnHeaderList();

        for (Company company : companyDataset) {
            List columns = new ArrayList();

            for (String column : columnHeaders) {
                if (column.equals(Company.COMPANY_ID)) {
                    columns.add(company.getId());

                } else if (column.equals(Company.COMPANY_NAME)) {
                    Link link = new Link(requestContext);
                    link.setTitle(company.getName());

                    if (hasCompanyDetailsAccess) {
                        link.setAjaxPath(AppPaths.CONTACTS_COMPANY_DETAIL + "?companyId=" + company.getId());
                    }

                    columns.add(link.getString());

                } else if (column.equals(Company.ROWNUM)) {
                    columns.add(counter.incrCounter() + ".");
                }
            }
            Map map = new HashMap();
            map.put("rowClass", ui.getRowClass());
            map.put("columns", columns);
            list.add(map);
        }
        return list;
    }

    /**
     * This is for showing the tabs.
     *
     * @return ..
     */
    public static List companyTabList(RequestContext requestContext, Company company) throws DatabaseException {
        AccessUser user = requestContext.getUser();

        List tabList = new ArrayList();

        // Link to Company main contacts tab.
        if (Access.hasPermission(user, AppPaths.CONTACTS_COMPANY_DETAIL)) {
            Map tabMap = new HashMap();
            tabMap.put("tabName", CompanyTabs.MAIN_CONTACT_TAB);
            tabMap.put("tabPath", AppPaths.CONTACTS_COMPANY_DETAIL + "?companyId=" + company.getId());
            tabMap.put("tabText", Localizer.getText(requestContext, "contactMgmt.tab.companyMainContacts",
                    new Object[] {company.getCountMainContact()}));
            tabList.add(tabMap);
        }

        // Link to Company employee contacts tab.
        if (Access.hasPermission(user, AppPaths.CONTACTS_COMPANY_CONTACT)) {
            Map tabMap = new HashMap();
            tabMap.put("tabName", CompanyTabs.OTHER_CONTACT_TAB);
            tabMap.put("tabPath", AppPaths.CONTACTS_COMPANY_CONTACT + "?companyId=" + company.getId());
            tabMap.put("tabText", Localizer.getText(requestContext, "contactMgmt.tab.companyOtherContacts",
                    new Object[] {company.getCountEmployeeContact()}));
            tabList.add(tabMap);
        }

        // Link to Company notes tab.
        if (Access.hasPermission(user, AppPaths.CONTACTS_COMPANY_NOTE)) {
            Map tabMap = new HashMap();
            tabMap.put("tabName", CompanyTabs.NOTES_TAB);
            tabMap.put("tabPath", AppPaths.CONTACTS_COMPANY_NOTE + "?companyId=" + company.getId());
            tabMap.put("tabText", Localizer.getText(requestContext, "contactMgmt.tab.companyNotes",
                    new Object[] {company.getCountNote()}));
            tabList.add(tabMap);
        }

        // Link to Company files tab.
        if (Access.hasPermission(user, AppPaths.CONTACTS_COMPANY_FILES)) {
            Map tabMap = new HashMap();
            tabMap.put("tabName", CompanyTabs.FILES_TAB);
            tabMap.put("tabPath", AppPaths.CONTACTS_COMPANY_FILES + "?companyId=" + company.getId());
            tabMap.put("tabText", Localizer.getText(requestContext, "contactMgmt.tab.companyFiles",
                    new Object[] {company.getCountFile()}));
            tabList.add(tabMap);
        }

        // Link to Company Bookmarks tab.
        if (Access.hasPermission(user, AppPaths.CONTACTS_COMPANY_BOOKMARK)) {
            Map tabMap = new HashMap();
            tabMap.put("tabName", CompanyTabs.BOOKMAKRS_TAB);
            tabMap.put("tabPath", AppPaths.CONTACTS_COMPANY_BOOKMARK + "?companyId=" + company.getId());
            tabMap.put("tabText", Localizer.getText(requestContext, "contactMgmt.tab.companyBookmarks",
                    new Object[] {company.getCountBookmark()}));
            tabList.add(tabMap);
        }

        // Link to Company Issues tab.
        if (Access.hasPermission(user, AppPaths.CONTACTS_COMPANY_ISSUES)) {
            Map tabMap = new HashMap();
            tabMap.put("tabName", CompanyTabs.ISSUES_TAB);
            tabMap.put("tabPath", AppPaths.CONTACTS_COMPANY_ISSUES + "?companyId=" + company.getId());
            tabMap.put("tabText", Localizer.getText(requestContext, "contactMgmt.tab.companyIssues"));
            tabList.add(tabMap);
        }

         // Link to Company Contracts tab.
        if (Access.hasPermission(user, AppPaths.CONTACTS_COMPANY_CONTRACTS)) {
            Map tabMap = new HashMap();
            tabMap.put("tabName", CompanyTabs.CONTRACTS_TAB);
            tabMap.put("tabPath", AppPaths.CONTACTS_COMPANY_CONTRACTS + "?companyId=" + company.getId());
            tabMap.put("tabText", Localizer.getText(requestContext, "contracts"));
            tabList.add(tabMap);
        }
        
        return tabList;
    }

    /**
     * Gets a list of company objects in LabelValueBean format.
     *
     * @return ..
     */
    public static List<LabelValueBean> getCompanyOptions(RequestContext requestContext) throws DatabaseException {
        QueryBits query = new QueryBits();
        query.addSortColumn(Company.COMPANY_NAME);
        return getCompanyOptions(requestContext, query);
    }
    
    /**
     * Gets a list of company objects in LabelValueBean format.
     * @param query
     * @return
     * @throws DatabaseException
     */
    public static List<LabelValueBean> getCompanyOptions(RequestContext requestContext, QueryBits query) throws DatabaseException {
        List companyIdOptions = new ArrayList();

        ContactService contactService = ServiceProvider.getContactService(requestContext);
        for (Company company : contactService.getCompanies(query)) {
            companyIdOptions.add(new LabelValueBean(company.getName(), String.valueOf(company.getId())));
        }
        return companyIdOptions;
    }

    public static List getCompanyTypeOptions(RequestContext requestContext) {
        List<LabelValueBean> companyTypes = new ArrayList();
        companyTypes.add(new LabelValueBean(Localizer.getText(requestContext, "system.attribute_field.company_type_hardware_manufacturer"),
                String.valueOf(AttributeFieldIds.COMPANY_TYPE_HARDWARE_MANUFACTURER)));
        companyTypes.add(new LabelValueBean(Localizer.getText(requestContext, "system.attribute_field.company_type_hardware_vendor"),
                String.valueOf(AttributeFieldIds.COMPANY_TYPE_HARDWARE_VENDOR)));
        companyTypes.add(new LabelValueBean(Localizer.getText(requestContext, "system.attribute_field.company_type_software_maker"),
                String.valueOf(AttributeFieldIds.COMPANY_TYPE_SOFTWARE_MAKER)));
        companyTypes.add(new LabelValueBean(Localizer.getText(requestContext, "system.attribute_field.company_type_software_vendor"),
                String.valueOf(AttributeFieldIds.COMPANY_TYPE_SOFTWARE_VENDOR)));
        return companyTypes;
    }

    public static final List<Integer> DEFAULT_COMPANY_TYPES = Arrays.asList(
            AttributeFieldIds.COMPANY_TYPE_HARDWARE_MANUFACTURER,
            AttributeFieldIds.COMPANY_TYPE_HARDWARE_VENDOR,
            AttributeFieldIds.COMPANY_TYPE_SOFTWARE_MAKER,
            AttributeFieldIds.COMPANY_TYPE_SOFTWARE_VENDOR);
}