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
public class ConfigAction extends Action2 {

    public String execute() throws Exception {
        String cmd = requestContext.getParameterString("cmd");

        Action2 forwardAction = null;

        if (cmd.equals(AdminUtils.ADMIN_APP_CMD)) {
            forwardAction = new ConfigAppAction();

        } else if (cmd.equals(AdminUtils.ADMIN_AUTH_CMD)) {
            forwardAction = new ConfigAuthAction();

        } else if (cmd.equals(AdminUtils.ADMIN_DB_BACKUP_CMD)) {
            forwardAction = new ConfigBackupAction();

        } else if (cmd.equals(AdminUtils.ADMIN_COMPANY_CMD)) {
            forwardAction = new ConfigCompanyAction();

        } else if (cmd.equals(AdminUtils.ADMIN_EMAIL_CMD)) {
            forwardAction = new ConfigEmailAction();

        } else if (cmd.equals(AdminUtils.ADMIN_FILE_CMD)) {
            forwardAction = new ConfigFileAction();

        } else if (cmd.equals(AdminUtils.ADMIN_LDAP_TEST_CMD)) {
            forwardAction = new ConfigLdapTestAction();

        } else if (cmd.equals(AdminUtils.ADMIN_LDAP_TEST_2_CMD)) {
            forwardAction = new ConfigLdapTest2Action();

        } else if (cmd.equals(AdminUtils.ADMIN_LOOK_FEEL_CMD)) {
            forwardAction = new ConfigLookAction();

        } else if (cmd.equals(AdminUtils.ADMIN_SYSTEM_INFO_CMD)) {
            forwardAction = new ConfigSystemAction();
        }

        if (forwardAction != null) {
            return forwardAction.setRequestContext(requestContext).execute();
        }

        throw new FileNotFoundException();
    }
}
