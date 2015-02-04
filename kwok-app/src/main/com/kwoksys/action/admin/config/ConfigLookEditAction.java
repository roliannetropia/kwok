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

import com.kwoksys.action.common.template.ActionErrorsTemplate;
import com.kwoksys.action.common.template.HeaderTemplate;
import com.kwoksys.action.common.template.StandardTemplate;
import com.kwoksys.biz.admin.core.AdminUtils;
import com.kwoksys.biz.system.core.AppPaths;
import com.kwoksys.biz.system.core.Links;
import com.kwoksys.biz.system.core.configs.ConfigManager;
import com.kwoksys.framework.properties.Localizer;
import com.kwoksys.framework.struts2.Action2;
import com.kwoksys.framework.util.StringUtils;
import org.apache.struts.util.LabelValueBean;

import java.util.ArrayList;
import java.util.List;

/**
 * Action class for editing look and feel configurations.
 */
public class ConfigLookEditAction extends Action2 {

    public String execute() throws Exception {
        ConfigForm actionForm = (ConfigForm) getBaseForm(ConfigForm.class);

        // If not a resubmit, set some defaults
        if (!actionForm.isResubmit()) {
            actionForm.setTheme(ConfigManager.system.getTheme());
            actionForm.setStylesheet(ConfigManager.system.getSytlesheet());
            actionForm.setHomeCustomDescription(ConfigManager.system.getCustomHomeDescription());
        }

        List themeOptions = new ArrayList();
        for (String option : ConfigManager.system.getThemeOptions()) {
            themeOptions.add(new LabelValueBean(Localizer.getText(requestContext, "admin.config.theme." + option), option));
        }

        //
        // Template: StandardTemplate
        //
        StandardTemplate standardTemplate = new StandardTemplate(requestContext, AdminUtils.ADMIN_LOOK_FEEL_EDIT_CMD);
        standardTemplate.setPathAttribute("formAction", AppPaths.ADMIN_CONFIG_WRITE);
        standardTemplate.setAttribute("formCancelLink", Links.getCancelLink(requestContext, AppPaths.ADMIN_CONFIG + "?cmd=" + AdminUtils.ADMIN_LOOK_FEEL_CMD).getString());
        standardTemplate.setAttribute("cmd", AdminUtils.ADMIN_LOOK_FEEL_EDIT_2_CMD);
        standardTemplate.setAttribute("themeOptions", themeOptions);
        standardTemplate.setAttribute("homepageContent", StringUtils.encodeCkeditorValue(actionForm.getHomeCustomDescription()));

        //
        // Template: HeaderTemplate
        //
        HeaderTemplate header = standardTemplate.getHeaderTemplate();
        header.setTitleKey("admin.configHeader.look_config_edit");

        //
        // Template: ActionErrorsTemplate
        //
        standardTemplate.addTemplate(new ActionErrorsTemplate());

        return standardTemplate.findTemplate(STANDARD_TEMPLATE);
    }
}
