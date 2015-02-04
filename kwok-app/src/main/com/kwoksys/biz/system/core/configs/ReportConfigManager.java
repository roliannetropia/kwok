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
package com.kwoksys.biz.system.core.configs;

import java.util.Map;

/**
 * ReportConfigManager
 */
public class ReportConfigManager extends BaseConfigManager {

    private static final String contactsReportCsvFilename = "contact-report.csv";

    private static final String contactsReportPdfFilename = "contact-report.pdf";

    private static final String contractsReportCsvFilename = "contract-report.csv";

    private static final String contractsReportPdfFilename = "contract-report.pdf";

    private static final String issuesReportCsvFilename = "issue-report.csv";

    private static final String issuesReportPdfFilename = "issue-report.pdf";

    private static final String hardwareReportCsvFilename = "hardware-report.csv";

    private static final String hardwareReportPdfFilename = "hardware-report.pdf";

    public static final String HARDWARE_MEMBER_REPORT_CSV = "hardware-member-report.csv";

    public static final String HARDWARE_MEMBER_REPORT_PDF = "hardware-member-report.pdf";

    public static final String HARDWARE_LICENSE_REPORT_CSV = "hardware-license-report.csv";

    public static final String HARDWARE_LICENSE_REPORT_PDF = "hardware-license-report.pdf";

    private static final String softwareReportCsvFilename = "software-report.csv";

    private static final String softwareReportPdfFilename = "software-report.pdf";

    private static final String softwareUsageReportCsvFilename = "software-usage-report.csv";

    private static final String softwareUsageReportPdfFilename = "software-usage-report.pdf";

    private static ReportConfigManager instance = new ReportConfigManager();

    private ReportConfigManager() {}

    static ReportConfigManager getInstance() {
        return instance;
    }

    /**
     * Initializes configurations.
     * @param configMap
     * @return
     */
    void init(Map configMap) {
        setConfigMap(configMap);
    }

    public String getContactsReportCsvFilename() {
        return contactsReportCsvFilename;
    }

    public String getContactsReportPdfFilename() {
        return contactsReportPdfFilename;
    }

    public String getContractsReportCsvFilename() {
        return contractsReportCsvFilename;
    }

    public String getContractsReportPdfFilename() {
        return contractsReportPdfFilename;
    }

    public String getIssuesReportCsvFilename() {
        return issuesReportCsvFilename;
    }

    public String getIssuesReportPdfFilename() {
        return issuesReportPdfFilename;
    }

    public String getHardwareReportCsvFilename() {
        return hardwareReportCsvFilename;
    }

    public String getHardwareReportPdfFilename() {
        return hardwareReportPdfFilename;
    }

    public String getSoftwareReportCsvFilename() {
        return softwareReportCsvFilename;
    }

    public String getSoftwareReportPdfFilename() {
        return softwareReportPdfFilename;
    }

    public String getSoftwareUsageReportCsvFilename() {
        return softwareUsageReportCsvFilename;
    }

    public String getSoftwareUsageReportPdfFilename() {
        return softwareUsageReportPdfFilename;
    }
}
