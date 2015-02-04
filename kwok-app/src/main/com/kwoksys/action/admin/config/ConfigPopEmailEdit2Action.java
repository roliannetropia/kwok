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

import com.kwoksys.biz.ServiceProvider;
import com.kwoksys.biz.admin.AdminService;
import com.kwoksys.biz.admin.core.AdminUtils;
import com.kwoksys.biz.admin.dto.SystemConfig;
import com.kwoksys.biz.issues.IssueService;
import com.kwoksys.framework.http.RequestContext;
import com.kwoksys.biz.system.core.configs.ConfigManager;
import com.kwoksys.biz.system.core.configs.SystemConfigNames;
import com.kwoksys.framework.connections.mail.EmailMessage;
import com.kwoksys.framework.connections.mail.PopConnection;
import com.kwoksys.biz.system.core.AppPaths;
import com.kwoksys.framework.struts2.Action2;
import com.kwoksys.framework.util.StringUtils;
import org.apache.struts.action.*;

import java.util.ArrayList;
import java.util.List;

/**
 * Action class for editing POP configuratons.
 */
public class ConfigPopEmailEdit2Action extends Action2 {

    public String execute() throws Exception {
        ConfigForm actionForm = saveActionForm(new ConfigForm());

        ActionMessages errors = new ActionMessages();

        PopConnection conn = new PopConnection();
        // Retrieves 1 message at a time for testing.
        conn.setMessagesLimit(1);

        if (actionForm.isTest()) {
            conn.setHost(actionForm.getPopHost());
            conn.setPort(actionForm.getPopPort());
            conn.setUsername(actionForm.getPopUsername());
            conn.setSslEnabled(actionForm.getPopUseSSL());
            conn.setSenderIgnoreList(actionForm.getPopIgnoreSender());

            // Only use the password given in submit form if it's not empty.
            if (actionForm.getPopPassword().isEmpty()) {
                conn.setPassword(ConfigManager.email.getPopPassword());
            } else {
                conn.setPassword(actionForm.getPopPassword());
            }

            try {
                IssueService issueService = ServiceProvider.getIssueService(requestContext);
                List<EmailMessage> list = issueService.retrieveIssueEmails(conn);
                errors.add("fetchSuccess", new ActionMessage("admin.config.email.pop.success", new Object[]{list.size()}));

            } catch (Exception e) {
                errors.add("fetchMail", new ActionMessage("admin.config.email.pop.error", e.getClass(), e.getMessage()));
            }

            saveActionErrors(errors);
            return redirect(AppPaths.ADMIN_CONFIG_WRITE + "?cmd=" + AdminUtils.ADMIN_POP_EMAIL_EDIT_CMD + "&" + RequestContext.URL_PARAM_ERROR_TRUE);
        }

        List list = new ArrayList();
        list.add(new SystemConfig(SystemConfigNames.POP_HOST, actionForm.getPopHost()));
        list.add(new SystemConfig(SystemConfigNames.POP_PORT, actionForm.getPopPort()));
        list.add(new SystemConfig(SystemConfigNames.POP_USERNAME, actionForm.getPopUsername()));
        list.add(new SystemConfig(SystemConfigNames.POP_SSL_ENABLED, actionForm.getPopUseSSL()));
        list.add(new SystemConfig(SystemConfigNames.POP_SENDER_IGNORE_LIST, actionForm.getPopIgnoreSender()));

        if (!actionForm.getPopPassword().isEmpty()) {
            list.add(new SystemConfig(SystemConfigNames.POP_PASSWORD, StringUtils.encodeBase64Codec(actionForm.getPopPassword())));
        }

        AdminService adminService = ServiceProvider.getAdminService(requestContext);

        errors = adminService.updateConfig(list);
        if (!errors.isEmpty()) {
            saveActionErrors(errors);
            return redirect(AppPaths.ADMIN_CONFIG_WRITE + "?cmd=" + AdminUtils.ADMIN_POP_EMAIL_EDIT_CMD + "&" + RequestContext.URL_PARAM_ERROR_TRUE);

        } else {
            return redirect(AppPaths.ADMIN_CONFIG + "?cmd=" + AdminUtils.ADMIN_EMAIL_CMD);
        }
    }
}
