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
package com.kwoksys.biz.hardware.core;

import com.kwoksys.action.common.template.HeaderTemplate;
import com.kwoksys.biz.ServiceProvider;
import com.kwoksys.biz.admin.dto.AccessUser;
import com.kwoksys.biz.admin.dto.AttributeField;
import com.kwoksys.biz.auth.core.Access;
import com.kwoksys.biz.hardware.dto.Hardware;
import com.kwoksys.biz.system.SystemService;
import com.kwoksys.biz.system.SystemUtils;
import com.kwoksys.biz.system.core.*;
import com.kwoksys.framework.util.Counter;
import com.kwoksys.framework.http.RequestContext;
import com.kwoksys.biz.system.core.configs.ConfigManager;
import com.kwoksys.framework.exceptions.DatabaseException;
import com.kwoksys.framework.properties.Localizer;
import com.kwoksys.framework.ui.Link;
import com.kwoksys.framework.ui.RowStyle;
import com.kwoksys.framework.util.HtmlUtils;

import java.util.*;
import java.util.regex.Pattern;

/**
 * HardwareUtil
 */
public class HardwareUtils {

    public static final String HARDWARE_LICENSE_TAB = "licenseTab";
    public static final String HARDWARE_COMP_TAB = "componentTab";
    public static final String HARDWARE_FILE_TAB = "fileTab";
    public static final String HARDWARE_ISSUE_TAB = "issueTab";
    public static final String HARDWARE_MEMBER_TAB = "memberTab";
    public static final String HARDWARE_CONTACT_TAB = "contactTab";

    public static String[] getHardwareDefaultColumns() {
        return new String[] {Hardware.ROWNUM, Hardware.ID, Hardware.HARDWARE_NAME, Hardware.TYPE, Hardware.STATUS, Hardware.MODEL_NAME, 
                Hardware.MODEL_NUMBER, Hardware.SERIAL_NUMBER, Hardware.LOCATION, Hardware.WARRANTY_EXPIRATION,
                Hardware.SERVICE_DATE, Hardware.OWNER_NAME};
    }

    /**
     * Speficify the sortable columns allowed.
     */
    public static List<String> getSortableColumns() {
        return Arrays.asList(Hardware.ID, Hardware.HARDWARE_NAME, Hardware.MODEL_NAME, Hardware.MODEL_NUMBER,
                Hardware.SERIAL_NUMBER, Hardware.WARRANTY_EXPIRATION, Hardware.SERVICE_DATE, Hardware.OWNER_NAME,
                Hardware.LOCATION);
    }

    /**
     * Check whether a column is sortable.
     * Return true if the given column is sortable.
     * Return false if the given column is not sortable.
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
        return ConfigManager.app.getHardwareColumns();
    }

    public static boolean isValidCostFormat(String input) {
        // \\d means decimal point.
        if (input != null && !input.isEmpty()) {
            String pattern = "(\\d{1,8})|(\\d{1,8}\\.\\d{0,2})";
            return Pattern.matches(pattern, input);
        } else {
            return true;
        }
    }

    public static void addHardwareHeaderCommands(RequestContext requestContext, HeaderTemplate headerTemplate, Integer hardwareId) throws DatabaseException {
        AccessUser accessUser = requestContext.getUser();

        // Edit Hardware link.
        if (Access.hasPermission(accessUser, AppPaths.HARDWARE_EDIT)) {
            Link link = new Link(requestContext);
            link.setAjaxPath(AppPaths.HARDWARE_EDIT + "?hardwareId=" + hardwareId);
            link.setTitleKey("itMgmt.cmd.hardwareEdit");
            headerTemplate.addHeaderCmds(link);
        }

        // Copy hardware
        if (Access.hasPermission(accessUser, AppPaths.HARDWARE_ADD)) {
            Link link = new Link(requestContext);
            link.setAjaxPath(AppPaths.HARDWARE_ADD + "?copyHardwareId=" + hardwareId);
            link.setTitleKey("itMgmt.cmd.hardwareCopy");
            headerTemplate.addHeaderCmds(link);
        }

        // Delete Hardware link.
        if (Access.hasPermission(accessUser, AppPaths.HARDWARE_DELETE)) {
            Link link = new Link(requestContext);
            link.setAjaxPath(AppPaths.HARDWARE_DELETE + "?hardwareId=" + hardwareId);
            link.setTitleKey("itMgmt.cmd.hardwareDelete");
            headerTemplate.addHeaderCmds(link);
        }

        if (Access.hasPermission(accessUser, AppPaths.HARDWARE_LIST)) {
            Link link = new Link(requestContext);
            link.setAjaxPath(AppPaths.HARDWARE_LIST);
            link.setTitleKey("itMgmt.cmd.hardwareList");
            headerTemplate.addHeaderCmds(link);
        }
    }

    /**
     * This is for generating hardware tabs.
     *
     * @param request
     * @param hardware
     * @return ..
     */
    public static List hardwareTabList(Hardware hardware, RequestContext requestContext) throws DatabaseException {
        AccessUser user = requestContext.getUser();

        List tabList = new ArrayList();

        // Link to Hardware assigned software tab.
        if (Access.hasPermission(user, AppPaths.HARDWARE_DETAIL)) {
            Map tabMap = new HashMap();
            tabMap.put("tabName", HARDWARE_LICENSE_TAB);
            tabMap.put("tabPath", AppPaths.HARDWARE_DETAIL + "?hardwareId=" + hardware.getId());
            tabMap.put("tabText", Localizer.getText(requestContext, "itMgmt.tab.hardwareAssignedSoftware",
                    new Object[]{hardware.getCountSoftware()}));
            tabList.add(tabMap);
        }

        // Link to Hardware components tab.
        if (Access.hasPermission(user, AppPaths.HARDWARE_COMP)) {
            Map tabMap = new HashMap();
            tabMap.put("tabName", HARDWARE_COMP_TAB);
            tabMap.put("tabPath", AppPaths.HARDWARE_COMP + "?hardwareId=" + hardware.getId());
            tabMap.put("tabText", Localizer.getText(requestContext, "itMgmt.tab.hardwareComponents",
                    new Object[] {hardware.getCountComponent()}));
            tabList.add(tabMap);
        }

        // Link to Hardware attachments tab.
        if (Access.hasPermission(user, AppPaths.HARDWARE_FILE)) {
            Map tabMap = new HashMap();
            tabMap.put("tabName", HARDWARE_FILE_TAB);
            tabMap.put("tabPath", AppPaths.HARDWARE_FILE + "?hardwareId=" + hardware.getId());
            tabMap.put("tabText", Localizer.getText(requestContext, "itMgmt.tab.hardwareFile",
                    new Object[] {hardware.getCountFile()}));
            tabList.add(tabMap);
        }

        // Link to Hardware association tab.
        if (Access.hasPermission(user, AppPaths.HARDWARE_MEMBER)) {
            Map tabMap = new HashMap();
            tabMap.put("tabName", HARDWARE_MEMBER_TAB);
            tabMap.put("tabPath", AppPaths.HARDWARE_MEMBER + "?hardwareId=" + hardware.getId());
            tabMap.put("tabText", Localizer.getText(requestContext, "hardware.tab.hardwareMembers"));
            tabList.add(tabMap);
        }

        // Link to Hardware Issues tab.
        if (Access.hasPermission(user, AppPaths.HARDWARE_ISSUE)) {
            Map tabMap = new HashMap();
            tabMap.put("tabName", HARDWARE_ISSUE_TAB);
            tabMap.put("tabPath", AppPaths.HARDWARE_ISSUE + "?hardwareId=" + hardware.getId());
            tabMap.put("tabText", Localizer.getText(requestContext, "itMgmt.tab.hardwareIssues"));
            tabList.add(tabMap);
        }

        // Link to Hardware Contacts tab.
        if (Access.hasPermission(user, AppPaths.HARDWARE_CONTACTS)) {
            SystemService systemService = ServiceProvider.getSystemService(requestContext);
            List linkedTypes = Arrays.asList(String.valueOf(ObjectTypes.CONTACT));
            int relationshipCount = systemService.getLinkedObjectMapCount(linkedTypes, hardware.getId(), ObjectTypes.HARDWARE);

            Map tabMap = new HashMap();
            tabMap.put("tabName", HARDWARE_CONTACT_TAB);
            tabMap.put("tabPath", AppPaths.HARDWARE_CONTACTS + "?hardwareId=" + hardware.getId());
            tabMap.put("tabText", Localizer.getText(requestContext, "common.linking.tab.linkedContacts",
                    new Object[] {relationshipCount}));
            tabList.add(tabMap);
        }
        return tabList;
    }

    public static List formatHardwareList(RequestContext requestContext, List<Hardware> hardwareDataset, Counter counter,
                                          String hardwarePath) throws Exception {
        return formatHardwareList(requestContext, hardwareDataset, HardwareUtils.getColumnHeaderList(), counter, hardwarePath);
    }

    public static List formatHardwareList(RequestContext requestContext, List<Hardware> hardwareDataset, List<String> columnHeaders,
                                      Counter counter, String hardwarePath) throws Exception {
        List list = new ArrayList();

        if (hardwareDataset.isEmpty()) {
            return list;
        }

        AccessUser user = requestContext.getUser();

        // Sysdate timestamp
        long unixTimestamp = requestContext.getSysdate().getTime();
        RowStyle ui = new RowStyle();

        boolean hasHardwareAccess = Access.hasPermission(user, hardwarePath);
        boolean hasUserDetailAccess = Access.hasPermission(user, AppPaths.ADMIN_USER_DETAIL);
        boolean hasHwAjaxAccess = Access.hasPermission(user, AppPaths.IT_MGMT_AJAX_GET_HARDWARE_DETAIL);

        AttributeManager attributeManager = new AttributeManager(requestContext);

        for (Hardware hardware : hardwareDataset) {
            List columns = new ArrayList();

            for (String column : columnHeaders) {
                if (column.equals(Hardware.ROWNUM)) {
                    columns.add(counter.incrCounter() + ".");

                } else if (column.equals(Hardware.ID)) {
                    columns.add(hardware.getId());

                } else if (column.equals(Hardware.HARDWARE_NAME)) {
                    if (hasHardwareAccess) {
                        Link link = new Link(requestContext);
                        link.setTitle(hardware.getName());
                        link.setAjaxPath(hardwarePath + "?hardwareId=" + hardware.getId());
                        String tempHardwareName = link.getString();

                        if (hasHwAjaxAccess) {
                            link = new Link(requestContext);
                            link.setJavascript("hardwarePopup(this," + hardware.getId() + ")");
                            link.setImgSrc(Image.getInstance().getMagGlassIcon());
                            tempHardwareName += "&nbsp;" + link.getString();
                        }
                        columns.add(tempHardwareName);
                    } else {
                        columns.add(HtmlUtils.encode(hardware.getName()));
                    }

                } else if (column.equals(Hardware.OWNER_NAME)) {
                    columns.add(Links.getUserIconLink(requestContext, hardware.getOwner(), hasUserDetailAccess, true));

                } else if (column.equals(Hardware.MODEL_NAME)) {
                    columns.add(HtmlUtils.encode(hardware.getModelName()));

                } else if (column.equals(Hardware.MODEL_NUMBER)) {
                    columns.add(HtmlUtils.encode(hardware.getModelNumber()));

                } else if (column.equals(Hardware.SERIAL_NUMBER)) {
                    columns.add(HtmlUtils.encode(hardware.getSerialNumber()));

                } else if (column.equals(Hardware.WARRANTY_EXPIRATION)) {
                    columns.add(HardwareUtils.formatWarrantyExpirationDate(requestContext, unixTimestamp,
                            hardware.getWarrantyExpireDate()));

                } else if (column.equals(Hardware.SERVICE_DATE)) {
                    columns.add(hardware.getLastServicedOn());

                } else if (column.equals(Hardware.STATUS)) {
                    AttributeField attrField = attributeManager.getAttrFieldMapCache(
                            Attributes.HARDWARE_STATUS).get(hardware.getStatus());
                    columns.add(Links.getAttrFieldIcon(requestContext, attrField));

                } else if (column.equals(Hardware.TYPE)) {
                    AttributeField attrField = attributeManager.getAttrFieldMapCache(
                            Attributes.HARDWARE_TYPE).get(hardware.getType());
                    columns.add(Links.getAttrFieldIcon(requestContext, attrField));

                } else if (column.equals(Hardware.LOCATION)) {
                    columns.add(HtmlUtils.encode(attributeManager.getAttrFieldNameCache(Attributes.HARDWARE_LOCATION,
                            hardware.getLocation())));
                }
            }

            Map map = new HashMap();
            map.put("hardwareId", hardware.getId());
            map.put("rowClass", ui.getRowClass());
            map.put("columns", columns);
            list.add(map);
        }
        return list;
    }

    public static String formatWarrantyExpirationDate(RequestContext requestContext, Long unixTimestamp, Date expirationDate) {
        return SystemUtils.formatExpirationDate(requestContext, unixTimestamp, expirationDate, ConfigManager.app.getHardwareWarrantyExpireCountdown());
    }
}
