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

import com.kwoksys.biz.admin.core.AdminUtils;
import com.kwoksys.biz.system.core.AppPaths;
import com.kwoksys.framework.properties.AppProperties;
import com.kwoksys.framework.struts2.Action2;

import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * Action class for executing database backup.
 */
public class ConfigBackupExecuteAction extends Action2 {

    private static final Logger logger = Logger.getLogger(ConfigBackupExecuteAction.class.getName());

    public String execute() throws Exception {
        ProcessBuilder pb = new ProcessBuilder(AdminUtils.getBackupCommand());
        pb.environment().put("PGPASSWORD", AppProperties.get(AppProperties.DB_PASSWORD_KEY));
        pb.redirectErrorStream(true);

        try{
            pb.start();

        } catch (Exception e) {
            logger.log(Level.SEVERE, "Problem with PostgreSQL backup.", e);
        }

        pb.environment().remove("PGPASSWORD");

        return redirect(AppPaths.ADMIN_CONFIG + "?cmd=" + AdminUtils.ADMIN_DB_BACKUP_CMD);
    }
}