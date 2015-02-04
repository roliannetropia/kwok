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
package com.kwoksys.biz.reports.types;

import com.kwoksys.biz.ServiceProvider;
import com.kwoksys.biz.hardware.core.HardwareSearch;
import com.kwoksys.biz.hardware.core.HardwareUtils;
import com.kwoksys.biz.hardware.dto.Hardware;
import com.kwoksys.biz.reports.Report;
import com.kwoksys.biz.reports.ReportService;
import com.kwoksys.biz.reports.dao.ReportQueries;
import com.kwoksys.biz.reports.writers.ReportWriter;
import com.kwoksys.biz.system.core.AppPaths;
import com.kwoksys.framework.properties.Localizer;
import com.kwoksys.framework.http.RequestContext;
import com.kwoksys.biz.system.core.configs.ConfigManager;
import com.kwoksys.biz.system.core.configs.ReportConfigManager;
import com.kwoksys.framework.connections.database.QueryBits;
import com.kwoksys.framework.session.SessionManager;

import javax.servlet.http.HttpSession;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * HardwareMembersReport
 */
public class HardwareMembersReport extends Report {

    private String reportCriteriaMapKey;

    public HardwareMembersReport(RequestContext requestContext, String reportCriteriaMapKey) {
        this.requestContext = requestContext;
        this.reportCriteriaMapKey = reportCriteriaMapKey;
    }

    public String getCsvFilename() {
        return ReportConfigManager.HARDWARE_MEMBER_REPORT_CSV;
    }

    public String getPdfFilename() {
        return ReportConfigManager.HARDWARE_MEMBER_REPORT_PDF;
    }

    public String getReportFormName() {
        return "HardwareSearchForm";
    }

    public String getReportPath() {
        return AppPaths.REPORTS_HARDWARE_SEARCH;
    }

    @Override
    public void populateData(ReportWriter reportWriter) throws Exception {
        HttpSession session = requestContext.getRequest().getSession();

        // This is for column header.
        List<String> columnKeys = computeColumns(ConfigManager.app.getHardwareMembersExportColumns());

        if (columnKeys.contains(Hardware.ID)) {
            getColumnHeaders().add(Localizer.getText(requestContext, "common.column.hardware_id"));
        }
        if (columnKeys.contains(Hardware.HARDWARE_NAME)) {
            getColumnHeaders().add(Localizer.getText(requestContext, "common.column.hardware_name"));
        }
        if (columnKeys.contains(Hardware.ID)) {
            getColumnHeaders().add(Localizer.getText(requestContext, "common.column.hardware_member_id"));
        }
        if (columnKeys.contains(Hardware.HARDWARE_NAME)) {
            getColumnHeaders().add(Localizer.getText(requestContext, "common.column.hardware_member_name"));
        }
        reportWriter.addHeaderRow(getColumnHeaders());

        // Getting search criteria map from session variable.
        HardwareSearch hardwareSearch = new HardwareSearch(requestContext, reportCriteriaMapKey);

        // Get order and orderBy
        String orderBy = getReportColumnOrderBy();
        if (orderBy == null) {
            orderBy = SessionManager.getAttribute(requestContext.getRequest(), SessionManager.HARDWARE_ORDER_BY, Hardware.HARDWARE_NAME);
        }

        String order = getReportColumnOrder();
        if (order == null) {
            order = SessionManager.getAttribute(requestContext.getRequest(), SessionManager.HARDWARE_ORDER, QueryBits.ASCENDING);
        }

        // Ready to pass variables to query.
        QueryBits searchQuery = new QueryBits(hardwareSearch);

        QueryBits query = new QueryBits();
        if (HardwareUtils.isSortableColumn(orderBy)) {
            query.addSortColumn(ReportQueries.getHardwareMemberOrderByColumn(orderBy), order);
        }

        ReportService reportService = ServiceProvider.getReportService(requestContext);

        // Loop through the Hardware list.
        for (Map<String, String> record : reportService.getHardwareMembers(searchQuery, query)) {
            List columns = new ArrayList();

            if (columnKeys.contains(Hardware.ID)) {
                columns.add(record.get("hardware_id"));
            }
            if (columnKeys.contains(Hardware.HARDWARE_NAME)) {
                columns.add(record.get("hardware_name"));
            }
            if (columnKeys.contains(Hardware.ID)) {
                columns.add(record.get("hardware_member_id"));
            }
            if (columnKeys.contains(Hardware.HARDWARE_NAME)) {
                columns.add(record.get("hardware_member_name"));
            }

            reportWriter.addRow(columns);
        }
    }
}