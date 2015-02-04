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
package com.kwoksys.biz.software;

import com.kwoksys.action.common.template.HeaderTemplate;
import com.kwoksys.biz.ServiceProvider;
import com.kwoksys.biz.admin.dto.AccessUser;
import com.kwoksys.biz.admin.dto.AttributeField;
import com.kwoksys.biz.auth.core.Access;
import com.kwoksys.biz.software.dto.Software;
import com.kwoksys.biz.software.dto.SoftwareLicense;
import com.kwoksys.biz.system.SystemService;
import com.kwoksys.biz.system.core.*;
import com.kwoksys.framework.util.Counter;
import com.kwoksys.framework.http.RequestContext;
import com.kwoksys.biz.system.core.configs.ConfigManager;
import com.kwoksys.framework.exceptions.DatabaseException;
import com.kwoksys.framework.properties.Localizer;
import com.kwoksys.framework.ui.Link;
import com.kwoksys.framework.ui.RowStyle;
import com.kwoksys.framework.util.DatetimeUtils;
import com.kwoksys.framework.util.HtmlUtils;

import java.util.*;

/**
 * SoftwareControl
 */
public class SoftwareUtils {

    public static final int SOFTWARE_NAME_MAX_LEN = 100;

    public static final String DETAILS_TAB = "detailsTab";

    public static final String BOOKMARKS_TAB = "bookmarksTab";

    public static final String ISSUES_TAB = "issuesTab";

    public static final String CONTACTS_TAB = "contactsTab";

    public static final String FILES_TAB = "filesTab";

    public static String[] getSoftwareDefaultColumns() {
        return new String[] {Software.ROWNUM, Software.NAME, Software.VERSION, Software.EXPIRE_DATE,
                Software.MANUFACTURER, Software.VENDOR, Software.TYPE, Software.OS, Software.LICENSE_PUCHASED,
                Software.LICENSE_INSTALLED, Software.LICENSE_AVAILABLE};
    }

    /**
     * Speficify the sortable columns allowed.
     */
    public static List<String> getSortableColumns() {
        return Arrays.asList(Software.NAME, Software.VERSION, Software.EXPIRE_DATE,
                Software.MANUFACTURER, Software.VENDOR, Software.TYPE, Software.OS, Software.LICENSE_PUCHASED,
                Software.LICENSE_INSTALLED, Software.LICENSE_AVAILABLE);
    }

    /**
     * Check whether a column is sortable.
     *
     * @param columnName
     * @return ..
     */
    public static boolean isSortableColumn(String columnName) {
        return getSortableColumns().contains(columnName);
    }

    /**
     * Specify the column header for the list page.
     */
    public static List<String> getColumnHeaderList() {
        return Arrays.asList(ConfigManager.app.getSoftwareColumns());
    }

    /**
     * Returns only the first line of license key
     */
    public static String formatLicenseKey(String input) {
        if (input == null) {
            return "";
        }
        int carriageReturn = input.indexOf("\n");
        if (carriageReturn != -1) {
            return HtmlUtils.encode(input.substring(0, carriageReturn)) + "...";
        }

        int notesNumChar = ConfigManager.app.getSoftwareLicenseNotesNumChars();
        if (notesNumChar != 0 && input.length() > notesNumChar) {
            return HtmlUtils.encode(input.substring(0, notesNumChar) + "...");
        }
        return HtmlUtils.encode(input);
    }

    /**
     * Returns the first x number of characters
     * @param input
     * @param charCount
     * @return
     */
    public static String formatLicenseKey(RequestContext requestContext, String input, int charCount, Integer id) {
        if (input == null) {
            return "";
        }
        if ((charCount != 0) && (input.length() > charCount)) {
            Link link = new Link(requestContext);
            link.setEscapeTitle("&raquo;");
            link.setJavascript("toggleView('licDiv_"+ id + "');");
            return HtmlUtils.formatMultiLineDisplay(input.substring(0, charCount)) + "<span style='display:none;' id=\"licDiv_"+ id + "\">"
                    + HtmlUtils.formatMultiLineDisplay(input.substring(charCount)) + "</span> " + link.getString();
        }
        return HtmlUtils.formatMultiLineDisplay(input);
    }

    /**
     * This is for generating software tabs.
     *
     * @param request
     * @param software
     * @return ..
     */
    public static List softwareTabList(RequestContext requestContext, Software software)
            throws DatabaseException {

        AccessUser user = requestContext.getUser();

        List tabList = new ArrayList();

        // Link to Software License view.
        if (Access.hasPermission(user, AppPaths.SOFTWARE_DETAIL)) {
            Map tabMap = new HashMap();
            tabMap.put("tabName", DETAILS_TAB);
            tabMap.put("tabPath", AppPaths.SOFTWARE_DETAIL + "?softwareId=" + software.getId());
            tabMap.put("tabText", Localizer.getText(requestContext, "itMgmt.tab.softwareLicense", new Object[]{software.getCountLicense()}));
            tabList.add(tabMap);
        }

        // Link to Software Attachments view.
        if (Access.hasPermission(user, AppPaths.SOFTWARE_FILE)) {
            Map tabMap = new HashMap();
            tabMap.put("tabName", FILES_TAB);
            tabMap.put("tabPath", AppPaths.SOFTWARE_FILE + "?softwareId=" + software.getId());
            tabMap.put("tabText", Localizer.getText(requestContext, "itMgmt.tab.softwareFile", new Object[]{software.getCountFile()}));
            tabList.add(tabMap);
        }

        // Link to Software Bookmark view.
        if (Access.hasPermission(user, AppPaths.SOFTWARE_BOOKMARK)) {
            Map tabMap = new HashMap();
            tabMap.put("tabName", BOOKMARKS_TAB);
            tabMap.put("tabPath", AppPaths.SOFTWARE_BOOKMARK + "?softwareId=" + software.getId());
            tabMap.put("tabText", Localizer.getText(requestContext, "itMgmt.tab.softwareBookmark", new Object[]{software.getCountBookmark()}));
            tabList.add(tabMap);
        }

        // Link to Software Issues
        if (Access.hasPermission(user, AppPaths.SOFTWARE_ISSUE)) {
            Map tabMap = new HashMap();
            tabMap.put("tabName", ISSUES_TAB);
            tabMap.put("tabPath", AppPaths.SOFTWARE_ISSUE + "?softwareId=" + software.getId());
            tabMap.put("tabText", Localizer.getText(requestContext, "itMgmt.tab.softwareIssue"));
            tabList.add(tabMap);
        }

        if (Access.hasPermission(user, AppPaths.SOFTWARE_CONTACTS)) {
            SystemService systemService = ServiceProvider.getSystemService(requestContext);
            List linkedTypes = Arrays.asList(String.valueOf(ObjectTypes.CONTACT));
            int relationshipCount = systemService.getLinkedObjectMapCount(linkedTypes, software.getId(), ObjectTypes.SOFTWARE);

            Map tabMap = new HashMap();
            tabMap.put("tabName", CONTACTS_TAB);
            tabMap.put("tabPath", AppPaths.SOFTWARE_CONTACTS + "?softwareId=" + software.getId());
            tabMap.put("tabText", Localizer.getText(requestContext, "common.linking.tab.linkedContacts", new Object[]{relationshipCount}));
            tabList.add(tabMap);
        }
        return tabList;
    }

    public static List formatSoftwareList(RequestContext requestContext, List<Software> softwareDataset, Counter counter) throws Exception {
        List softwareList = new ArrayList();
        if (softwareDataset == null) {
            return softwareList;
        }

        AccessUser user = requestContext.getUser();
        boolean hasSoftwareAccess = Access.hasPermission(user, AppPaths.SOFTWARE_DETAIL);
        boolean hasCompanyAccess = Access.hasPermission(user, AppPaths.CONTACTS_COMPANY_DETAIL);
        RowStyle ui = new RowStyle();

        AttributeManager attributeManager = new AttributeManager(requestContext);

        for (Software software : softwareDataset) {
            List columns = new ArrayList();

            // For Software detail
            for (String column : SoftwareUtils.getColumnHeaderList()) {
                if (column.equals(Software.ROWNUM)) {
                    columns.add(counter.incrCounter() + ".");

                } else if (column.equals(Software.NAME)) {
                    Link softwareLink = new Link(requestContext);
                    softwareLink.setTitle(software.getName());

                    if (hasSoftwareAccess) {
                        softwareLink.setAppPath(AppPaths.SOFTWARE_DETAIL + "?softwareId=" + software.getId());
                    }
                    columns.add(softwareLink);

                } else if (column.equals(Software.VERSION)) {
                    columns.add(HtmlUtils.encode(software.getVersion()));

                } else if (column.equals(Software.EXPIRE_DATE)) {
                    columns.add(DatetimeUtils.toShortDate(software.getExpireDate()));

                } else if (column.equals(Software.TYPE)) {
                    columns.add(HtmlUtils.encode(software.getTypeName()));

                } else if (column.equals(Software.OS)) {
                    AttributeField attrField = attributeManager.getAttrFieldMapCache(
                            Attributes.SOFTWARE_OS).get(software.getOs());
                    columns.add(Links.getAttrFieldIcon(requestContext, attrField));

                } else if (column.equals(Software.MANUFACTURER)) {
                    columns.add(Links.getCompanyDetailsLink(requestContext, hasCompanyAccess,
                            software.getManufacturerName(), software.getManufacturerId()));

                } else if (column.equals(Software.VENDOR)) {
                    columns.add(Links.getCompanyDetailsLink(requestContext, hasCompanyAccess,
                            software.getVendorName(), software.getVendorId()));

                } else if (column.equals(SoftwareLicense.LICENSE_AVAILABLE)) {
                    if (!Software.isEnoughLicenses(software.getLicenseAvailable())) {
                         // We want to flag licenses that go below 0.
                        Object[] args = {software.getLicenseAvailable()};
                        columns.add(Localizer.getText(requestContext, "itMgmt.softwareList.licenseOut", args));
                    } else {
                        columns.add(software.getLicenseAvailable());
                    }
                } else if (column.equals(SoftwareLicense.LICENSE_PUCHASED)) {
                    columns.add(software.getLicensePurchased());

                } else if (column.equals(SoftwareLicense.LICENSE_INSTALLED)) {
                    columns.add(software.getLicenseInstalled());
                }
            }
            Map map = new HashMap();
            map.put("softwareId", software.getId());
            map.put("rowId", software.getId());
            map.put("rowClass", ui.getRowClass());
            map.put("columns", columns);
            softwareList.add(map);
        }
        return softwareList;
    }

    public static void addSoftwareHeaderCommands(RequestContext requestContext, HeaderTemplate headerTemplate, Integer softwareId) throws DatabaseException {
        AccessUser accessUser = requestContext.getUser();

        // Link to Software edit.
        if (Access.hasPermission(accessUser, AppPaths.SOFTWARE_EDIT)) {
            Link link = new Link(requestContext);
            link.setAjaxPath(AppPaths.SOFTWARE_EDIT + "?softwareId=" + softwareId);
            link.setTitleKey("itMgmt.cmd.softwareEdit");
            headerTemplate.addHeaderCmds(link);
        }

        // Link to delete software.
        if (Access.hasPermission(accessUser, AppPaths.SOFTWARE_DELETE)) {
            Link link = new Link(requestContext);
            link.setAjaxPath(AppPaths.SOFTWARE_DELETE + "?softwareId=" + softwareId);
            link.setTitleKey("itMgmt.cmd.softwareLicenseDelete");
            headerTemplate.addHeaderCmds(link);
        }

        // Back to software list.
        if (Access.hasPermission(accessUser, AppPaths.SOFTWARE_LIST)) {
            Link link = new Link(requestContext);
            link.setAjaxPath(AppPaths.SOFTWARE_LIST);
            link.setTitleKey("itMgmt.cmd.softwareList");
            headerTemplate.addHeaderCmds(link);
        }
    }
}
