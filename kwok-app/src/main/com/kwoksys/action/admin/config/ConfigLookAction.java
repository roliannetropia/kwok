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
import com.kwoksys.biz.admin.core.AdminUtils;
import com.kwoksys.biz.admin.dto.AccessUser;
import com.kwoksys.biz.auth.core.Access;
import com.kwoksys.biz.system.core.AppPaths;
import com.kwoksys.biz.system.core.Links;
import com.kwoksys.biz.system.core.configs.ConfigManager;
import com.kwoksys.framework.http.RequestContext;
import com.kwoksys.framework.properties.Localizer;
import com.kwoksys.framework.struts2.Action2;
import com.kwoksys.framework.ui.Link;

/**
 * Action class for look and feel configuration.
 */
public class ConfigLookAction extends Action2 {

    public String execute() throws Exception {
        AccessUser user = requestContext.getUser();

        //
        // Template: StandardTemplate
        //
        StandardTemplate standardTemplate = new StandardTemplate(requestContext, AdminUtils.ADMIN_LOOK_FEEL_CMD);
        standardTemplate.setAttribute("theme", Localizer.getText(requestContext, "admin.config.theme." + ConfigManager.system.getTheme()));
        standardTemplate.setAttribute("stylesheet", ConfigManager.system.getSytlesheet());
        standardTemplate.setAttribute("homeCustomDescription", ConfigManager.system.getCustomHomeDescription());

        //
        // Template: HeaderTemplate
        //
        HeaderTemplate header = standardTemplate.getHeaderTemplate();
        header.setTitleKey("admin.index.lookAndFeel");
        
        if (Access.hasPermission(user, AppPaths.ADMIN_CONFIG_WRITE)) {
            Link link = new Link(requestContext);
            link.setAjaxPath(AppPaths.ADMIN_CONFIG_WRITE + "?cmd=" + AdminUtils.ADMIN_LOOK_FEEL_EDIT_CMD);
            link.setTitleKey("common.command.Edit");
            header.addHeaderCmds(link);
        }

        // Back to admin home
        header.addNavLink(Links.getAdminHomeLink(requestContext));

        Link link = new Link(requestContext);
        link.setTitleKey("admin.index.lookAndFeel");
        header.addNavLink(link);
        
        return standardTemplate.findTemplate(STANDARD_TEMPLATE);
    }
}
