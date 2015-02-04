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
package com.kwoksys.action.admin.config;

import com.kwoksys.action.common.template.HeaderTemplate;
import com.kwoksys.action.common.template.StandardTemplate;
import com.kwoksys.biz.ServiceProvider;
import com.kwoksys.biz.admin.core.AdminUtils;
import com.kwoksys.biz.admin.dto.AccessUser;
import com.kwoksys.biz.auth.core.Access;
import com.kwoksys.biz.files.FileService;
import com.kwoksys.biz.files.core.FileUtils;
import com.kwoksys.biz.system.core.AppPaths;
import com.kwoksys.biz.system.core.Links;
import com.kwoksys.biz.system.core.configs.ConfigManager;
import com.kwoksys.framework.http.RequestContext;
import com.kwoksys.framework.struts2.Action2;
import com.kwoksys.framework.ui.Link;

/**
 * ConfigFileAction
 */
public class ConfigFileAction extends Action2 {

    public String execute() throws Exception {
        AccessUser user = requestContext.getUser();

        FileService fileService = ServiceProvider.getFileService(requestContext);

        //
        // Template: StandardTemplate
        //
        StandardTemplate standardTemplate = new StandardTemplate(requestContext, AdminUtils.ADMIN_FILE_CMD);

        standardTemplate.setAttribute("maxFileUploadSize", FileUtils.formatFileSize(requestContext, ConfigManager.file.getMaxFileUploadSize()));
        standardTemplate.setAttribute("kilobyteUnits", ConfigManager.file.getKilobyteUnits());

        standardTemplate.setAttribute("companyRepositoryPath", ConfigManager.file.getCompanyFileRepositoryLocation());
        standardTemplate.setAttribute("validCompanyRepositoryPath", fileService.isDirectoryExist(ConfigManager.file.getCompanyFileRepositoryLocation()));

        standardTemplate.setAttribute("issueRepositoryPath", ConfigManager.file.getIssueFileRepositoryLocation());
        standardTemplate.setAttribute("validIssueRepositoryPath", fileService.isDirectoryExist(ConfigManager.file.getIssueFileRepositoryLocation()));

        standardTemplate.setAttribute("hardwareRepositoryPath", ConfigManager.file.getHardwareFileRepositoryLocation());
        standardTemplate.setAttribute("validHardwareRepositoryPath", fileService.isDirectoryExist(ConfigManager.file.getHardwareFileRepositoryLocation()));

        standardTemplate.setAttribute("softwareRepositoryPath", ConfigManager.file.getSoftwareFileRepositoryLocation());
        standardTemplate.setAttribute("validSoftwareRepositoryPath", fileService.isDirectoryExist(ConfigManager.file.getSoftwareFileRepositoryLocation()));

        standardTemplate.setAttribute("contractRepositoryPath", ConfigManager.file.getContractFileRepositoryLocation());
        standardTemplate.setAttribute("validContractRepositoryPath", fileService.isDirectoryExist(ConfigManager.file.getContractFileRepositoryLocation()));

        standardTemplate.setAttribute("kbRepositoryPath", ConfigManager.file.getKbFileRepositoryLocation());
        standardTemplate.setAttribute("validKbRepositoryPath", fileService.isDirectoryExist(ConfigManager.file.getKbFileRepositoryLocation()));

        //
        // Template: HeaderTemplate
        //
        HeaderTemplate header = standardTemplate.getHeaderTemplate();
        header.setTitleKey("admin.config.file");
        
        if (Access.hasPermission(user, AppPaths.ADMIN_CONFIG_WRITE)) {
            Link link = new Link(requestContext);
            link.setAjaxPath(AppPaths.ADMIN_CONFIG_WRITE + "?cmd=" + AdminUtils.ADMIN_FILE_EDIT_CMD);
            link.setTitleKey("common.command.Edit");
            header.addHeaderCmds(link);
        }

        // Back to admin home
        header.addNavLink(Links.getAdminHomeLink(requestContext));

        Link link = new Link(requestContext);
        link.setTitleKey("admin.config.file");
        header.addNavLink(link);

        return standardTemplate.findTemplate(STANDARD_TEMPLATE);
    }
}