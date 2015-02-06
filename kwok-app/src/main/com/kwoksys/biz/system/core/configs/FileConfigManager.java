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
 * FileConfigManager
 */
public class FileConfigManager extends BaseConfigManager {

    private static FileConfigManager instance = new FileConfigManager();

    private static final String DELETE_FILE_PREFIX = "DEL-";

    private static final String POSTGRES_BACKUP_EXTENSION = ".backup";

    private long maxFileUploadSize;
    private int kilobyteUnits;
    private Integer[] kilobyteUnitsList = {1000, 1024};

    // Company files.
    private String companyFileRepositoryLocation;
    private String companyUploadedFilePrefix;

    // Hardware files.
    private String hardwareFileRepositoryLocation;
    private String hardwareUploadedFilePrefix;

    // Tape files.
    private String tapeFileRepositoryLocation;
    private String tapeUploadedFilePrefix;

    // Software files.
    private String softwareFileRepositoryLocation;
    private String softwareUploadedFilePrefix;

    // Issue files.
    private String issueFileRepositoryLocation;
    private String issueUploadedFilePrefix;

    // Contract files.
    private String contractFileRepositoryLocation;
    private String contractUploadedFilePrefix;

    // KB article files
    private String kbFileRepositoryLocation;
    private String kbUploadedFilePrefix;

    // Backup files
    private String dbPostgresProgramPath;
    private String dbBackupRepositoryPath;

    private FileConfigManager() {}

    static FileConfigManager getInstance() {
        return instance;
    }

    void init(Map configMap) {
        setConfigMap(configMap);

        kilobyteUnits = getInt(SystemConfigNames.FILES_KILOBYTE_UNITS);
        maxFileUploadSize = getLong(SystemConfigNames.FILES_UPLOAD_BYTE_SIZE);
        companyFileRepositoryLocation = getString(SystemConfigNames.COMPANY_FILE_PATH);
        companyUploadedFilePrefix = getString("file.company.uploadFilePrefix");
        hardwareFileRepositoryLocation = getString(SystemConfigNames.HARDWARE_FILE_PATH);
        hardwareUploadedFilePrefix = getString("file.hardware.uploadFilePrefix");

        tapeFileRepositoryLocation = getString(SystemConfigNames.TAPE_FILE_PATH);
        tapeUploadedFilePrefix = getString("file.tape.uploadFilePrefix");
        softwareFileRepositoryLocation = getString(SystemConfigNames.SOFTWARE_FILE_PATH);
        softwareUploadedFilePrefix = getString("file.software.uploadFilePrefix");
        issueFileRepositoryLocation = getString(SystemConfigNames.ISSUE_FILE_PATH);
        issueUploadedFilePrefix = getString("file.issue.uploadFilePrefix");
        contractFileRepositoryLocation = getString(SystemConfigNames.CONTRACT_FILE_PATH);
        contractUploadedFilePrefix = getString("file.contract.uploadFilePrefix");
        kbFileRepositoryLocation = getString(SystemConfigNames.KB_FILE_PATH);
        kbUploadedFilePrefix = getString("file.kb.uploadFilePrefix");

        dbPostgresProgramPath = getString(SystemConfigNames.DB_POSTGRES_PROGRAM_PATH);
        dbBackupRepositoryPath = getString(SystemConfigNames.DB_BACKUP_REPOSITORY_PATH);
    }

    public String getDeleteFilePrefix() {
        return DELETE_FILE_PREFIX;
    }

    public String getCompanyFileRepositoryLocation() {
        return companyFileRepositoryLocation;
    }
    public String getCompanyUploadedFilePrefix() {
        return companyUploadedFilePrefix;
    }
    public String getSoftwareFileRepositoryLocation() {
        return softwareFileRepositoryLocation;
    }
    public String getSoftwareUploadedFilePrefix() {
        return softwareUploadedFilePrefix;
    }
    public String getHardwareUploadedFilePrefix() {
        return hardwareUploadedFilePrefix;
    }
    public String getHardwareFileRepositoryLocation() {
        return hardwareFileRepositoryLocation;
    }
    public String getTapeFileRepositoryLocation() {
        return tapeFileRepositoryLocation;
    }
    public String getIssueUploadedFilePrefix() {
        return issueUploadedFilePrefix;
    }
    public String getIssueFileRepositoryLocation() {
        return issueFileRepositoryLocation;
    }
    public String getContractFileRepositoryLocation() {
        return contractFileRepositoryLocation;
    }
    public String getContractUploadedFilePrefix() {
        return contractUploadedFilePrefix;
    }
    public String getKbUploadedFilePrefix() {
        return kbUploadedFilePrefix;
    }
    public long getMaxFileUploadSize() {
        return maxFileUploadSize;
    }

    public int getKilobyteUnits() {
        return kilobyteUnits;
    }

    public Integer[] getKilobyteUnitsList() {
        return kilobyteUnitsList;
    }

    public String getKbFileRepositoryLocation() {
        return kbFileRepositoryLocation;
    }

    public String getDbPostgresProgramPath() {
        return dbPostgresProgramPath;
    }

    public String getDbBackupRepositoryPath() {
        return dbBackupRepositoryPath;
    }

    public String getPostgresBackupExtension() {
        return POSTGRES_BACKUP_EXTENSION;
    }
}
