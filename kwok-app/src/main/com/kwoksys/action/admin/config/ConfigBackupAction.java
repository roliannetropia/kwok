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
import com.kwoksys.framework.util.DatetimeUtils;

import java.io.File;
import java.util.*;

/**
 * Action class for DB backup.
 */
public class ConfigBackupAction extends Action2 {

    public String execute() throws Exception {
        AccessUser user = requestContext.getUser();

        FileService fileService = ServiceProvider.getFileService(requestContext);
        boolean validBackupCmdPath = fileService.isFileExist(ConfigManager.file.getDbPostgresProgramPath());
        boolean validBackupRepoPath = fileService.isDirectoryExist(ConfigManager.file.getDbBackupRepositoryPath());

        //
        // Template: StandardTemplate
        //
        StandardTemplate standardTemplate = new StandardTemplate(requestContext, AdminUtils.ADMIN_DB_BACKUP_CMD);

        request.setAttribute("backupCmdPath", ConfigManager.file.getDbPostgresProgramPath());
        request.setAttribute("validBackupCmdPath", validBackupCmdPath);

        request.setAttribute("backupCmd", AdminUtils.getBackupCommandDisplay());

        request.setAttribute("backupRepoPath", ConfigManager.file.getDbBackupRepositoryPath());
        request.setAttribute("validBackupRepoPath", validBackupRepoPath);

        request.setAttribute("backupExecutePath", AppPaths.ADMIN_CONFIG_WRITE + "?cmd="
                + AdminUtils.ADMIN_DB_BACKUP_EXECUTE);

        if (Access.hasPermission(user, AppPaths.ADMIN_CONFIG_WRITE)) {
            request.setAttribute("backupCmdEnabled", (validBackupCmdPath && validBackupRepoPath));
        }

        File directory = new File(ConfigManager.file.getDbBackupRepositoryPath());
        if (directory.isDirectory()) {
            File[] files = directory.listFiles();
            List fileMaps = new ArrayList();
            for (File file : files) {
                Map map = new HashMap();
                map.put("filename", file.getName());
                
                Date date = DatetimeUtils.longToDate(file.lastModified());
                map.put("fileModifiedDate", DatetimeUtils.toLocalDatetime(date));

                map.put("filesize", FileUtils.formatFileSize(requestContext, file.length()));
                fileMaps.add(map);
            }
            request.setAttribute("backupFiles", fileMaps);
        }

        //
        // Template: HeaderTemplate
        //
        HeaderTemplate header = standardTemplate.getHeaderTemplate();
        header.setTitleKey("admin.config.db.backup.header");

        if (Access.hasPermission(user, AppPaths.ADMIN_CONFIG_WRITE)) {
            Link link = new Link(requestContext);
            link.setAjaxPath(AppPaths.ADMIN_CONFIG_WRITE + "?cmd=" + AdminUtils.ADMIN_DB_BACKUP_EDIT_CMD);
            link.setTitleKey("common.command.Edit");
            header.addHeaderCmds(link);
        }

        // Back to admin home
        header.addNavLink(Links.getAdminHomeLink(requestContext));

        if (Access.hasPermission(user, AppPaths.ADMIN_CONFIG)) {
            Link link = new Link(requestContext);
            link.setAjaxPath(AppPaths.ADMIN_CONFIG + "?cmd=" + AdminUtils.ADMIN_SYSTEM_INFO_CMD);
            link.setTitleKey("admin.configHeader.system_info");
            header.addNavLink(link);
        }

        return standardTemplate.findTemplate(STANDARD_TEMPLATE);
    }
}