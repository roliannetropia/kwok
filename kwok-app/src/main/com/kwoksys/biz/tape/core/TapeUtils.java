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
package com.kwoksys.biz.tape.core;

import com.kwoksys.action.common.template.HeaderTemplate;
import com.kwoksys.biz.ServiceProvider;
import com.kwoksys.biz.admin.dto.AccessUser;
import com.kwoksys.biz.admin.dto.AttributeField;
import com.kwoksys.biz.auth.core.Access;
import com.kwoksys.biz.tape.dto.Tape;
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
 * TapeUtil
 */
public class TapeUtils {

    public static final String TAPE_LICENSE_TAB = "licenseTab";
    public static final String TAPE_COMP_TAB = "componentTab";
    public static final String TAPE_FILE_TAB = "fileTab";
    public static final String TAPE_ISSUE_TAB = "issueTab";
    public static final String TAPE_MEMBER_TAB = "memberTab";
    public static final String TAPE_CONTACT_TAB = "contactTab";

//    todo default columns in tape details
    public static String[] getTapeDefaultColumns() {
        return new String[] {Tape.ROWNUM, Tape.ID, Tape.TAPE_SERIAL_NUMBER, Tape.TAPE_BARCODE_NUMBER};
    }

    /**
     * Speficify the sortable columns allowed.
     */
    public static List<String> getSortableColumns() {
        return Arrays.asList(Tape.ID);
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
        return ConfigManager.app.getTapeColumns();
    }

//    public static boolean isValidCostFormat(String input) {
//        // \\d means decimal point.
//        if (input != null && !input.isEmpty()) {
//            String pattern = "(\\d{1,8})|(\\d{1,8}\\.\\d{0,2})";
//            return Pattern.matches(pattern, input);
//        } else {
//            return true;
//        }
//    }

    public static void addTapeHeaderCommands(RequestContext requestContext, HeaderTemplate headerTemplate, Integer tapeId) throws DatabaseException {
        AccessUser accessUser = requestContext.getUser();

        // Edit Tape link.
        if (Access.hasPermission(accessUser, AppPaths.TAPE_EDIT)) {
            Link link = new Link(requestContext);
            link.setAjaxPath(AppPaths.TAPE_EDIT + "?tapeId=" + tapeId);
            link.setTitleKey("itMgmt.cmd.tapeEdit");
            headerTemplate.addHeaderCmds(link);
        }

        // Copy tape
        if (Access.hasPermission(accessUser, AppPaths.TAPE_ADD)) {
            Link link = new Link(requestContext);
            link.setAjaxPath(AppPaths.TAPE_ADD + "?copyTapeId=" + tapeId);
            link.setTitleKey("itMgmt.cmd.tapeCopy");
            headerTemplate.addHeaderCmds(link);
        }

        // Delete Tape link.
        if (Access.hasPermission(accessUser, AppPaths.TAPE_DELETE)) {
            Link link = new Link(requestContext);
            link.setAjaxPath(AppPaths.TAPE_DELETE + "?tapeId=" + tapeId);
            link.setTitleKey("itMgmt.cmd.tapeDelete");
            headerTemplate.addHeaderCmds(link);
        }

        if (Access.hasPermission(accessUser, AppPaths.TAPE_LIST)) {
            Link link = new Link(requestContext);
            link.setAjaxPath(AppPaths.TAPE_LIST);
            link.setTitleKey("itMgmt.cmd.tapeList");
            headerTemplate.addHeaderCmds(link);
        }
    }

    /**
     * This is for generating tape tabs.
     *
//     * @param request
//     * @param tape
     * @return ..
     */
    public static List tapeTabList(Tape tape, RequestContext requestContext) throws DatabaseException {
        AccessUser user = requestContext.getUser();

        List tabList = new ArrayList();

        // Link to Tape assigned software tab.
        if (Access.hasPermission(user, AppPaths.TAPE_DETAIL)) {
            Map tabMap = new HashMap();
            tabMap.put("tabName", TAPE_LICENSE_TAB);
            tabMap.put("tabPath", AppPaths.TAPE_DETAIL + "?tapeId=" + tape.getId());
//            tabMap.put("tabText", Localizer.getText(requestContext, "itMgmt.tab.tapeAssignedSoftware",
//                    new Object[]{tape.getCountSoftware()}));
            tabList.add(tabMap);
        }

//        // Link to Tape components tab.
////        if (Access.hasPermission(user, AppPaths.TAPE_COMP)) {
////            Map tabMap = new HashMap();
////            tabMap.put("tabName", TAPE_COMP_TAB);
////            tabMap.put("tabPath", AppPaths.TAPE_COMP + "?tapeId=" + tape.getId());
////            tabMap.put("tabText", Localizer.getText(requestContext, "itMgmt.tab.tapeComponents",
////                    new Object[] {tape.getCountComponent()}));
////            tabList.add(tabMap);
////        }
//
//        // Link to Tape attachments tab.
////        if (Access.hasPermission(user, AppPaths.TAPE_FILE)) {
////            Map tabMap = new HashMap();
////            tabMap.put("tabName", TAPE_FILE_TAB);
////            tabMap.put("tabPath", AppPaths.TAPE_FILE + "?tapeId=" + tape.getId());
////            tabMap.put("tabText", Localizer.getText(requestContext, "itMgmt.tab.tapeFile",
////                    new Object[] {tape.getCountFile()}));
////            tabList.add(tabMap);
////        }
//
//        // Link to Tape association tab.
////        if (Access.hasPermission(user, AppPaths.TAPE_MEMBER)) {
////            Map tabMap = new HashMap();
////            tabMap.put("tabName", TAPE_MEMBER_TAB);
////            tabMap.put("tabPath", AppPaths.TAPE_MEMBER + "?tapeId=" + tape.getId());
////            tabMap.put("tabText", Localizer.getText(requestContext, "tape.tab.tapeMembers"));
////            tabList.add(tabMap);
////        }
//
//        // Link to Tape Issues tab.
////        if (Access.hasPermission(user, AppPaths.TAPE_ISSUE)) {
////            Map tabMap = new HashMap();
////            tabMap.put("tabName", TAPE_ISSUE_TAB);
////            tabMap.put("tabPath", AppPaths.TAPE_ISSUE + "?tapeId=" + tape.getId());
////            tabMap.put("tabText", Localizer.getText(requestContext, "itMgmt.tab.tapeIssues"));
////            tabList.add(tabMap);
////        }
//
//        // Link to Tape Contacts tab.
////        if (Access.hasPermission(user, AppPaths.TAPE_CONTACTS)) {
////            SystemService systemService = ServiceProvider.getSystemService(requestContext);
////            List linkedTypes = Arrays.asList(String.valueOf(ObjectTypes.CONTACT));
////            int relationshipCount = systemService.getLinkedObjectMapCount(linkedTypes, tape.getId(), ObjectTypes.TAPE);
////
////            Map tabMap = new HashMap();
////            tabMap.put("tabName", TAPE_CONTACT_TAB);
////            tabMap.put("tabPath", AppPaths.TAPE_CONTACTS + "?tapeId=" + tape.getId());
////            tabMap.put("tabText", Localizer.getText(requestContext, "common.linking.tab.linkedContacts",
////                    new Object[] {relationshipCount}));
////            tabList.add(tabMap);
////        }
        return tabList;
    }

    public static List formatTapeList(RequestContext requestContext, List<Tape> tapeDataset, Counter counter,
                                          String tapePath) throws Exception {
//        todo tape header
        return formatTapeList(requestContext, tapeDataset, TapeUtils.getColumnHeaderList(), counter, tapePath);
    }

    public static List formatTapeList(RequestContext requestContext, List<Tape> tapeDataset, List<String> columnHeaders,
                                      Counter counter, String tapePath) throws Exception {
        List list = new ArrayList();
        System.out.println("column headers: "+columnHeaders);

        if (tapeDataset.isEmpty()) {
            return list;
        }

        AccessUser user = requestContext.getUser();

        // Sysdate timestamp
        long unixTimestamp = requestContext.getSysdate().getTime();
        RowStyle ui = new RowStyle();

        boolean hasTapeAccess = Access.hasPermission(user, tapePath);
        boolean hasUserDetailAccess = Access.hasPermission(user, AppPaths.ADMIN_USER_DETAIL);
        boolean hasTpAjaxAccess = Access.hasPermission(user, AppPaths.IT_MGMT_AJAX_GET_TAPE_DETAIL);

        AttributeManager attributeManager = new AttributeManager(requestContext);

        for (Tape tape : tapeDataset) {
            List columns = new ArrayList();

            for (String column : columnHeaders) {
                if (column.equals(Tape.ROWNUM)) {
                    System.out.println("1");
                    columns.add(counter.incrCounter() + ".");

                } else if (column.equals(Tape.ID)) {
                    System.out.println("2");
                    columns.add(tape.getId());

//                } else if (column.equals(Tape.TAPE_NAME)) {
//                    System.out.println("3");
//
//                    if (hasTapeAccess) {
//                        Link link = new Link(requestContext);
//                        link.setTitle(tape.getTapeName());
//                        link.setAjaxPath(tapePath + "?tapeId=" + tape.getId());
//                        String tempTapeName = link.getString();
//
//                        if (hasTpAjaxAccess) {
//                            link = new Link(requestContext);
//                            link.setJavascript("tapePopup(this," + tape.getId() + ")");
//                            link.setImgSrc(Image.getInstance().getMagGlassIcon());
//                            tempTapeName += "&nbsp;" + link.getString();
//                        }
//                        columns.add(tempTapeName);
//                    } else {
//                        columns.add(HtmlUtils.encode(tape.getTapeName()));
//                    }

//                } else if (column.equals(Tape.OWNER_NAME)) {
//                    columns.add(Links.getUserIconLink(requestContext, tape.getOwner(), hasUserDetailAccess, true));
//
//                } else if (column.equals(Tape.MODEL_NAME)) {
//                    columns.add(HtmlUtils.encode(tape.getModelName()));
//
//                } else if (column.equals(Tape.MODEL_NUMBER)) {
//                    columns.add(HtmlUtils.encode(tape.getModelNumber()));
//
                } else if (column.equals(Tape.TAPE_SERIAL_NUMBER)) {
                    System.out.println("4");
                    columns.add(HtmlUtils.encode(tape.getTapeSerialNumber()));

                } else if (column.equals(Tape.TAPE_BARCODE_NUMBER)) {
                    System.out.println("5");
//                    columns.add(HtmlUtils.encode(tape.getTapeBarcodeNumber()));

                    if (hasTapeAccess) {
                        Link link = new Link(requestContext);
                        link.setTitle(tape.getTapeBarcodeNumber());
                        link.setAjaxPath(tapePath + "?tapeId=" + tape.getId());
                        String tempTapeBarcodeNumber = link.getString();

                        if (hasTpAjaxAccess) {
                            link = new Link(requestContext);
                            link.setJavascript("tapePopup(this," + tape.getId() + ")");
                            link.setImgSrc(Image.getInstance().getMagGlassIcon());
                            tempTapeBarcodeNumber += "&nbsp;" + link.getString();
                        }
                        columns.add(tempTapeBarcodeNumber);
                    } else {
                        columns.add(HtmlUtils.encode(tape.getTapeBarcodeNumber()));
                    }

//                } else if (column.equals(Tape.WARRANTY_EXPIRATION)) {
//                    columns.add(com.kwoksys.biz.tape.core.TapeUtils.formatWarrantyExpirationDate(requestContext, unixTimestamp,
//                            tape.getWarrantyExpireDate()));
//
//                } else if (column.equals(Tape.SERVICE_DATE)) {
//                    columns.add(tape.getLastServicedOn());

//                } else if (column.equals(Tape.STATUS)) {
//                    AttributeField attrField = attributeManager.getAttrFieldMapCache(
//                            Attributes.TAPE_STATUS).get(tape.getStatus());
//                    columns.add(Links.getAttrFieldIcon(requestContext, attrField));
//
//                } else if (column.equals(Tape.TYPE)) {
//                    AttributeField attrField = attributeManager.getAttrFieldMapCache(
//                            Attributes.TAPE_TYPE).get(tape.getType());
//                    columns.add(Links.getAttrFieldIcon(requestContext, attrField));

                } else if (column.equals(Tape.TAPE_RETENTION)) {
                    columns.add(HtmlUtils.encode(attributeManager.getAttrFieldNameCache(Attributes.TAPE_RETENTION,
                            tape.getTapeRetention())));

                } else if (column.equals(Tape.TAPE_LOCATION)) {
                    columns.add(HtmlUtils.encode(attributeManager.getAttrFieldNameCache(Attributes.TAPE_LOCATION,
                            tape.getTapeLocation())));
                }
            }

            Map map = new HashMap();
            map.put("tapeId", tape.getId());
            map.put("rowClass", ui.getRowClass());
            map.put("columns", columns);
            list.add(map);
        }
        return list;
    }
//
//    public static String formatWarrantyExpirationDate(RequestContext requestContext, Long unixTimestamp, Date expirationDate) {
//        return SystemUtils.formatExpirationDate(requestContext, unixTimestamp, expirationDate, ConfigManager.app.getTapeWarrantyExpireCountdown());
//    }
}
