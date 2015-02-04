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
import com.kwoksys.framework.struts2.Action2;

import java.io.FileNotFoundException;

/**
 * Action class for admin commands.
 */
public class ConfigWriteAction extends Action2 {

    public String execute() throws Exception {
        String cmd = requestContext.getParameterString("cmd");

        Action2 forwardAction = null;

        if (cmd.equals(AdminUtils.ADMIN_APP_EDIT_CMD)) {
            forwardAction = new ConfigAppEditAction();

        } else if (cmd.equals(AdminUtils.ADMIN_APP_EDIT_2_CMD)) {
            forwardAction = new ConfigAppEdit2Action();

        } else if (cmd.equals(AdminUtils.ADMIN_AUTH_EDIT_CMD)) {
            forwardAction = new ConfigAuthEditAction();

        } else if (cmd.equals(AdminUtils.ADMIN_AUTH_EDIT_2_CMD)) {
            forwardAction = new ConfigAuthEdit2Action();

        } else if (cmd.equals(AdminUtils.ADMIN_DB_BACKUP_EDIT_CMD)) {
            forwardAction = new ConfigBackupEditAction();

        } else if (cmd.equals(AdminUtils.ADMIN_DB_BACKUP_EDIT_2_CMD)) {
            forwardAction = new ConfigBackupEdit2Action();

        } else if (cmd.equals(AdminUtils.ADMIN_DB_BACKUP_EDIT_CMD)) {
            forwardAction = new ConfigBackupExecuteAction();

        } else if (cmd.equals(AdminUtils.ADMIN_COMPANY_EDIT_CMD)) {
            forwardAction = new ConfigCompanyEditAction();

        } else if (cmd.equals(AdminUtils.ADMIN_COMPANY_EDIT_2_CMD)) {
            forwardAction = new ConfigCompanyEdit2Action();

        } else if (cmd.equals(AdminUtils.ADMIN_EMAIL_EDIT_CMD)) {
            forwardAction = new ConfigEmailEditAction();

        } else if (cmd.equals(AdminUtils.ADMIN_EMAIL_EDIT_2_CMD)) {
            forwardAction = new ConfigEmailEdit2Action();

        } else if (cmd.equals(AdminUtils.ADMIN_FILE_EDIT_CMD)) {
            forwardAction = new ConfigFileEditAction();

        } else if (cmd.equals(AdminUtils.ADMIN_FILE_EDIT_2_CMD)) {
            forwardAction = new ConfigFileEdit2Action();

        } else if (cmd.equals(AdminUtils.ADMIN_LOGGING_EDIT_CMD)) {
            forwardAction = new ConfigLoggingEditAction();

        } else if (cmd.equals(AdminUtils.ADMIN_LOGGING_EDIT_2_CMD)) {
            forwardAction = new ConfigLoggingEdit2Action();

        } else if (cmd.equals(AdminUtils.ADMIN_LOOK_FEEL_EDIT_CMD)) {
            forwardAction = new ConfigLookEditAction();

        } else if (cmd.equals(AdminUtils.ADMIN_LOOK_FEEL_EDIT_2_CMD)) {
            forwardAction = new ConfigLookEdit2Action();

        } else if (cmd.equals(AdminUtils.ADMIN_POP_EMAIL_EDIT_CMD)) {
            forwardAction = new ConfigPopEmailEditAction();

        } else if (cmd.equals(AdminUtils.ADMIN_POP_EMAIL_EDIT_2_CMD)) {
            forwardAction = new ConfigPopEmailEdit2Action();
        }

        if (forwardAction != null) {
            return forwardAction.setRequestContext(requestContext).execute();
        }

        throw new FileNotFoundException();
    }
}