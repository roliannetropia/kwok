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
import com.kwoksys.biz.system.core.AppPaths;
import com.kwoksys.biz.admin.AdminService;
import com.kwoksys.biz.admin.core.AdminUtils;
import com.kwoksys.biz.admin.dto.SystemConfig;
import com.kwoksys.framework.http.RequestContext;
import com.kwoksys.biz.system.core.configs.SystemConfigNames;
import com.kwoksys.framework.session.SessionManager;
import com.kwoksys.framework.struts2.Action2;
import org.apache.struts.action.*;

import java.util.ArrayList;
import java.util.List;

/**
 * Action class for editing look and feel configurations.
 */
public class ConfigLookEdit2Action extends Action2 {

    public String execute() throws Exception {
        ConfigForm actionForm = saveActionForm(new ConfigForm());

        // Set session theme.
        String theme = actionForm.getTheme();

        List list = new ArrayList();
        list.add(new SystemConfig(SystemConfigNames.THEME_DEFAULT, theme));
        list.add(new SystemConfig(SystemConfigNames.UI_STYLESHEET, actionForm.getStylesheet()));
        list.add(new SystemConfig(SystemConfigNames.CUSTOM_HOME_DESCRIPTION, actionForm.getHomeCustomDescription()));

        // Get the service
        AdminService adminService = ServiceProvider.getAdminService(requestContext);

        ActionMessages errors = adminService.updateConfig(list);
        if (!errors.isEmpty()) {
            saveActionErrors(errors);
            return redirect(AppPaths.ADMIN_CONFIG_WRITE + "?cmd=" + AdminUtils.ADMIN_LOOK_FEEL_EDIT_CMD + "&" + RequestContext.URL_PARAM_ERROR_TRUE);

        } else {
            // Update session variable
            SessionManager.setAppSessionTheme(request.getSession(), theme);

            return redirect(AppPaths.ADMIN_CONFIG + "?cmd=" + AdminUtils.ADMIN_LOOK_FEEL_CMD);
        }
    }
}
