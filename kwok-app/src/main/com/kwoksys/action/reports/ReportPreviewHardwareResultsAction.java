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
package com.kwoksys.action.reports;

import com.kwoksys.action.common.template.RecordsNavigationTemplate;
import com.kwoksys.action.common.template.StandardTemplate;
import com.kwoksys.action.common.template.TableTemplate;
import com.kwoksys.action.hardware.HardwareSearchForm;
import com.kwoksys.biz.ServiceProvider;
import com.kwoksys.biz.admin.dto.AccessUser;
import com.kwoksys.biz.admin.dto.Attribute;
import com.kwoksys.biz.auth.core.Access;
import com.kwoksys.biz.auth.core.ReportAccess;
import com.kwoksys.biz.hardware.HardwareService;
import com.kwoksys.biz.hardware.core.HardwareSearch;
import com.kwoksys.biz.hardware.core.HardwareUtils;
import com.kwoksys.biz.hardware.dao.HardwareQueries;
import com.kwoksys.biz.hardware.dto.Hardware;
import com.kwoksys.biz.reports.ReportService;
import com.kwoksys.biz.reports.ReportUtils;
import com.kwoksys.biz.reports.dao.ReportQueries;
import com.kwoksys.biz.software.dto.Software;
import com.kwoksys.biz.software.dto.SoftwareLicense;
import com.kwoksys.biz.system.core.AppPaths;
import com.kwoksys.biz.system.core.AttributeManager;
import com.kwoksys.biz.system.core.ObjectTypes;
import com.kwoksys.biz.system.core.configs.ConfigManager;
import com.kwoksys.framework.connections.database.QueryBits;
import com.kwoksys.framework.exceptions.AccessDeniedException;
import com.kwoksys.framework.http.RequestContext;
import com.kwoksys.framework.properties.Localizer;
import com.kwoksys.framework.session.SessionManager;
import com.kwoksys.framework.struts2.Action2;
import com.kwoksys.framework.ui.Link;
import com.kwoksys.framework.ui.RowStyle;
import com.kwoksys.framework.util.Counter;
import org.apache.struts.util.LabelValueBean;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Action class for running a report.
 */
public class ReportPreviewHardwareResultsAction extends Action2 {

    private ReportForm reportForm;

    private HardwareSearchForm hardwareSearchForm ;

    public String execute() throws Exception {
        reportForm = (ReportForm) getSessionBaseForm(ReportForm.class);
        hardwareSearchForm = (HardwareSearchForm) getSessionBaseForm(HardwareSearchForm.class);
        AccessUser user = requestContext.getUser();

        // Checks report permission
        if (!ReportAccess.hasPermission(user, reportForm.getReportType())) {
            throw new AccessDeniedException();
        }

        if (reportForm.getReportType().equals(ReportUtils.HARDWARE_MEMBER_REPORT_TYPE)) {
            return hardwareMembers(request, response);
        } else if (reportForm.getReportType().equals(ReportUtils.HARDWARE_LICENSE_REPORT_TYPE)) {
            return hardwareLicenses(request, response);
        }

        String rowCmd = requestContext.getParameterString("rowCmd");
        String orderBy = requestContext.getParameterString("orderBy", Hardware.HARDWARE_NAME);
        String order = requestContext.getParameterString("order");
        int rowStart = requestContext.getParameter("rowStart", 0);

        int rowLimit = requestContext.getParameter("rowLimit", ConfigManager.app.getHardwareRowsToShow());
        if (rowCmd.equals("showAll")) {
            rowLimit = 0;
        }

        HardwareSearch hardwareSearch = new HardwareSearch(requestContext, SessionManager.HARDWARE_REPORT_CRITERIA_MAP);
        hardwareSearch.prepareMap(hardwareSearchForm);

        HardwareService hardwareService = ServiceProvider.getHardwareService(requestContext);

        QueryBits query = new QueryBits(hardwareSearch);
        query.setLimit(rowLimit, rowStart);

        if (HardwareUtils.isSortableColumn(orderBy)) {
            query.addSortColumn(HardwareQueries.getOrderByColumn(orderBy), order);
        }

        int rowCount = hardwareService.getHardwareCount(query);
        List dataList = null;

        if (rowCount != 0) {
            List<Hardware> hardwareList = hardwareService.getHardwareList(query);
            dataList = HardwareUtils.formatHardwareList(requestContext, hardwareList, new Counter(rowStart), AppPaths.HARDWARE_DETAIL);
        }

        //
        // Template: StandardTemplate
        //
        StandardTemplate standardTemplate = new StandardTemplate(requestContext);
        standardTemplate.setAttribute("ajaxHardwareDetailPath", AppPaths.IT_MGMT_AJAX_GET_HARDWARE_DETAIL + "?hardwareId=");

        //
        // Template: ReportPreviewResultsTemplate
        //
        ReportPreviewResultsTemplate preview = new ReportPreviewResultsTemplate();
        standardTemplate.addTemplate(preview);
        preview.setReportType(reportForm.getReportType());

        for (String column : ConfigManager.app.getHardwareExportColumns()) {
            preview.addReportColumnOptions(new LabelValueBean(Localizer.getText(requestContext, "common.column." + column), column));
        }
        for (Attribute attr : new AttributeManager(requestContext).getCustomFieldList(ObjectTypes.HARDWARE)) {
            preview.addReportColumnOptions(new LabelValueBean(attr.getName(), String.valueOf(attr.getId())));
        }

        for (String column : HardwareUtils.getSortableColumns()) {
            preview.addSortColumnOptions(new LabelValueBean(Localizer.getText(requestContext, "common.column." + column), column));
        }

        //
        // Template: RecordsNavigationTemplate
        //
        RecordsNavigationTemplate nav = new RecordsNavigationTemplate();
        standardTemplate.addTemplate(nav);
        nav.setRowOffset(rowStart);
        nav.setRowLimit(rowLimit);
        nav.setRowCount(rowCount);
        nav.setRowCountMsgkey("core.template.recordsNav.rownum");
        nav.setShowAllRecordsText(Localizer.getText(requestContext, "itMgmt.hardwareList.rowCount", new Object[]{rowCount}));
        nav.setShowAllRecordsPath(AppPaths.REPORTS_HARDWARE_SEARCH + "?reportType=" + reportForm.getReportType() + "&rowCmd=showAll");
        nav.setPath(AppPaths.REPORTS_HARDWARE_SEARCH + "?reportType=" + reportForm.getReportType() + "&rowStart=");

        //
        // Template: TableTemplate
        //
        TableTemplate tableTemplate = new TableTemplate();
        standardTemplate.addTemplate(tableTemplate);
        tableTemplate.setDataList(dataList);
        tableTemplate.setColumnHeaders(HardwareUtils.getColumnHeaderList());
        tableTemplate.setColumnPath(AppPaths.HARDWARE_LIST);
        tableTemplate.setColumnTextKey("common.column.");
        tableTemplate.setRowCmd(rowCmd);
        tableTemplate.setEmptyRowMsgKey("itMgmt.hardwareList.emptyTableMessage");

        return standardTemplate.findTemplate(STANDARD_TEMPLATE);
    }

    public String hardwareMembers(HttpServletRequest request,
                                 HttpServletResponse response) throws Exception {

        String reportType = requestContext.getParameterString("reportType");

        String rowCmd = requestContext.getParameterString("rowCmd");
        String orderBy = requestContext.getParameterString("orderBy", Hardware.HARDWARE_NAME);
        String order = requestContext.getParameterString("order");
        int rowStart = requestContext.getParameter("rowStart", 0);

        int rowLimit = requestContext.getParameter("rowLimit", ConfigManager.app.getHardwareRowsToShow());
        if (rowCmd.equals("showAll")) {
            rowLimit = 0;
        }

        HardwareSearch hardwareSearch = new HardwareSearch(requestContext, SessionManager.HARDWARE_REPORT_CRITERIA_MAP);
        hardwareSearch.prepareMap(hardwareSearchForm);

        QueryBits searchQuery = new QueryBits(hardwareSearch);

        QueryBits query = new QueryBits();
        query.setLimit(rowLimit, rowStart);

        if (HardwareUtils.isSortableColumn(orderBy)) {
            query.addSortColumn(ReportQueries.getHardwareMemberOrderByColumn(orderBy), order);
        }

        ReportService reportService = ServiceProvider.getReportService(requestContext);

        int rowCount = reportService.getHardwareMembersCount(searchQuery);
        List dataList = null;
        List columnList = ReportUtils.getHardwareMembersExportColumns();

        if (rowCount != 0) {
            Counter counter = new Counter(rowStart);
            List<Map<String, String>> hardwareList = reportService.getHardwareMembers(searchQuery, query);
            dataList = formatDataList(requestContext, hardwareList, columnList, counter, AppPaths.HARDWARE_MEMBER);
        }

        //
        // Template: StandardTemplate
        //
        StandardTemplate standardTemplate = new StandardTemplate(requestContext);

        //
        // Template: ReportPreviewResultsTemplate
        //
        ReportPreviewResultsTemplate preview = new ReportPreviewResultsTemplate();
        standardTemplate.addTemplate(preview);
        preview.setReportType(reportType);

        for (String column : new String[] {Hardware.ID, Hardware.HARDWARE_NAME}) {
            preview.addReportColumnOptions(new LabelValueBean(Localizer.getText(requestContext, "common.column." + column), column));
        }

        //
        // Template: RecordsNavigationTemplate
        //
        RecordsNavigationTemplate nav = new RecordsNavigationTemplate();
        standardTemplate.addTemplate(nav);
        nav.setRowOffset(rowStart);
        nav.setRowLimit(rowLimit);
        nav.setRowCount(rowCount);
        nav.setRowCountMsgkey("core.template.recordsNav.rownum");
        nav.setShowAllRecordsText(Localizer.getText(requestContext, "reports.list.rowCount", new Object[]{rowCount}));
        nav.setShowAllRecordsPath(AppPaths.REPORTS_HARDWARE_SEARCH + "?reportType=" + reportType + "&rowCmd=showAll");
        nav.setPath(AppPaths.REPORTS_HARDWARE_SEARCH + "?reportType=" + reportType + "&rowStart=");

        //
        // Template: TableTemplate
        //
        TableTemplate tableTemplate = new TableTemplate();
        standardTemplate.addTemplate(tableTemplate);
        tableTemplate.setDataList(dataList);
        tableTemplate.setColumnHeaders(columnList);
        tableTemplate.setColumnPath(AppPaths.HARDWARE_LIST);
        tableTemplate.setColumnTextKey("common.column.");
        tableTemplate.setRowCmd(rowCmd);
        tableTemplate.setEmptyRowMsgKey("itMgmt.hardwareList.emptyTableMessage");

        return standardTemplate.findTemplate(STANDARD_TEMPLATE);
    }

    public String hardwareLicenses(HttpServletRequest request,
                                 HttpServletResponse response) throws Exception {

        String reportType = requestContext.getParameterString("reportType");

        String rowCmd = requestContext.getParameterString("rowCmd");
        String orderBy = requestContext.getParameterString("orderBy", Hardware.HARDWARE_NAME);
        String order = requestContext.getParameterString("order");
        int rowStart = requestContext.getParameter("rowStart", 0);

        int rowLimit = requestContext.getParameter("rowLimit", ConfigManager.app.getHardwareRowsToShow());
        if (rowCmd.equals("showAll")) {
            rowLimit = 0;
        }

        HardwareSearch hardwareSearch = new HardwareSearch(requestContext, SessionManager.HARDWARE_REPORT_CRITERIA_MAP);
        hardwareSearch.prepareMap(hardwareSearchForm);

        QueryBits query = new QueryBits(hardwareSearch);
        query.setLimit(rowLimit, rowStart);
        query.addSortColumn(ReportQueries.getHardwareLicenseOrderByColumn(Hardware.HARDWARE_NAME), QueryBits.ASCENDING);
        query.addSortColumn(ReportQueries.getHardwareLicenseOrderByColumn(Software.NAME), QueryBits.ASCENDING);
        query.addSortColumn(ReportQueries.getHardwareLicenseOrderByColumn(SoftwareLicense.LICENSE_KEY), QueryBits.ASCENDING);

        ReportService reportService = ServiceProvider.getReportService(requestContext);

        int rowCount = reportService.getHardwareLicenseCount(query);
        List dataList = null;
        List columnList = ReportUtils.getHardwareLicenseColumns();

        if (rowCount != 0) {
            List<Map<String, String>> hardwareList = reportService.getHardwareLicenses(query);
            dataList = formatDataList(requestContext, hardwareList, columnList, new Counter(rowStart), AppPaths.HARDWARE_DETAIL);
        }

        //
        // Template: StandardTemplate
        //
        StandardTemplate standardTemplate = new StandardTemplate(requestContext);

        //
        // Template: ReportPreviewResultsTemplate
        //
        ReportPreviewResultsTemplate preview = new ReportPreviewResultsTemplate();
        standardTemplate.addTemplate(preview);
        preview.setReportType(reportType);

        for (String column : new String[] {Hardware.ID, Hardware.HARDWARE_NAME, Software.NAME, SoftwareLicense.LICENSE_KEY}) {
            preview.addReportColumnOptions(new LabelValueBean(Localizer.getText(requestContext, "common.column." + column), column));
        }

        //
        // Template: RecordsNavigationTemplate
        //
        RecordsNavigationTemplate nav = new RecordsNavigationTemplate();
        standardTemplate.addTemplate(nav);
        nav.setRowOffset(rowStart);
        nav.setRowLimit(rowLimit);
        nav.setRowCount(rowCount);
        nav.setRowCountMsgkey("core.template.recordsNav.rownum");
        nav.setShowAllRecordsText(Localizer.getText(requestContext, "reports.list.rowCount", new Object[]{rowCount}));
        nav.setShowAllRecordsPath(AppPaths.REPORTS_HARDWARE_SEARCH + "?reportType=" + reportType + "&rowCmd=showAll");
        nav.setPath(AppPaths.REPORTS_HARDWARE_SEARCH + "?reportType=" + reportType + "&rowStart=");

        //
        // Template: TableTemplate
        //
        TableTemplate tableTemplate = new TableTemplate();
        standardTemplate.addTemplate(tableTemplate);
        tableTemplate.setDataList(dataList);
        tableTemplate.setColumnHeaders(columnList);
        tableTemplate.setColumnPath(AppPaths.HARDWARE_LIST);
        tableTemplate.setColumnTextKey("common.column.");
        tableTemplate.setRowCmd(rowCmd);
        tableTemplate.setEmptyRowMsgKey("itMgmt.hardwareList.emptyTableMessage");

        return standardTemplate.findTemplate(STANDARD_TEMPLATE);
    }

    private List formatDataList(RequestContext requestContext, List<Map<String, String>> dataset, List<String> columnList, Counter counter, String hardwarePath) throws Exception {
        List formattedDatalist = new ArrayList();
        if (dataset == null) {
            return formattedDatalist;
        }

        AccessUser user = requestContext.getUser();
        boolean hasHardwareAccess = Access.hasPermission(user, hardwarePath);
        RowStyle ui = new RowStyle();

        for (Map<String, String> record : dataset) {
            List columns = new ArrayList();

            for (String column : columnList) {
                if (column.equals(Software.ROWNUM)) {
                    columns.add(counter.incrCounter() + ".");

                } else if (column.equals(Hardware.HARDWARE_NAME)) {
                    Link link = new Link(requestContext);
                    link.setTitle(record.get("hardware_name"));
                    if (hasHardwareAccess) {
                        link.setAjaxPath(hardwarePath + "?hardwareId=" + record.get("hardware_id"));
                    }
                    columns.add(link.getString());

                } else if (column.equals("hardware_member_name")) {
                    Link link = new Link(requestContext);
                    link.setTitle(record.get("hardware_member_name"));
                    if (hasHardwareAccess) {
                        link.setAjaxPath(hardwarePath + "?hardwareId=" + record.get("hardware_member_id"));
                    }
                    columns.add(link.getString());

                } else if (column.equals(Software.NAME)) {
                    String value = record.get("software_name");
                    columns.add(value == null ? "" : value);

                } else if (column.equals(SoftwareLicense.LICENSE_KEY)) {
                    String value = record.get("license_key");
                    columns.add(value == null ? "" : value);
                }
            }
            Map map = new HashMap();
            map.put("rowClass", ui.getRowClass());
            map.put("columns", columns);
            formattedDatalist.add(map);
        }
        return formattedDatalist;
    }
}