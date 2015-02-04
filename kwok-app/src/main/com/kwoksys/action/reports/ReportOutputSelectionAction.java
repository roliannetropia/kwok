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

import com.kwoksys.action.common.template.HeaderTemplate;
import com.kwoksys.action.common.template.StandardTemplate;
import com.kwoksys.biz.admin.dto.AccessUser;
import com.kwoksys.biz.auth.core.ReportAccess;
import com.kwoksys.biz.reports.Report;
import com.kwoksys.biz.reports.ReportFactory;
import com.kwoksys.biz.reports.ReportUtils;
import com.kwoksys.biz.system.core.AppPaths;
import com.kwoksys.framework.exceptions.AccessDeniedException;
import com.kwoksys.framework.struts2.Action2;

/**
 * Action class for running a report.
 */
public class ReportOutputSelectionAction extends Action2 {

    public String execute() throws Exception {
        AccessUser user = requestContext.getUser();
        
        ReportForm reportForm = (ReportForm) getSessionBaseForm(ReportForm.class);

        // Checks report permission
        if (!ReportAccess.hasPermission(user, reportForm.getReportType())) {
            throw new AccessDeniedException();
        }

        Report report = ReportFactory.getReport(requestContext, reportForm.getReportType());

        //
        // Template: StandardTemplate
        //
        StandardTemplate standardTemplate = new StandardTemplate(requestContext);
        standardTemplate.setPathAttribute("runReportAction", AppPaths.REPORTS_RESULTS_EXPORT);
        standardTemplate.setPathAttribute("reportBackAction", report.getReportPath() + "?reportType=" + reportForm.getReportType());
        standardTemplate.setAttribute("outputOptions", ReportUtils.getOutputOptions());

        //
        // Template: HeaderTemplate
        //
        HeaderTemplate headerTemplate = standardTemplate.getHeaderTemplate();
        headerTemplate.setTitleKey("reports.workflow.outputSelection.header");

        return standardTemplate.findTemplate(STANDARD_TEMPLATE);
    }
}
