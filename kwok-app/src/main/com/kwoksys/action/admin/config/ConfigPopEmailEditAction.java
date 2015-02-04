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

/**
 * Action class editing POP configuratons.
 */
public class ConfigPopEmailEditAction extends Action2 {

    public String execute() throws Exception {
        ConfigForm actionForm = (ConfigForm) getBaseForm(ConfigForm.class);

        // If not a resubmit, set some defaults
        if (!actionForm.isResubmit()) {
            actionForm.setPopHost(ConfigManager.email.getPopHost());
            actionForm.setPopPort(ConfigManager.email.getPopPort());
            actionForm.setPopUsername(ConfigManager.email.getPopUsername());
            actionForm.setPopIgnoreSender(ConfigManager.email.getPopSenderIgnoreList());
            actionForm.setPopUseSSL(ConfigManager.email.isPopSslEnabled());
            actionForm.setPopPassword("");
        }

        //
        // Template: StandardTemplate
        //
        StandardTemplate standardTemplate = new StandardTemplate(requestContext, AdminUtils.ADMIN_POP_EMAIL_EDIT_CMD);
        standardTemplate.setPathAttribute("formAction", AppPaths.ADMIN_CONFIG_WRITE);
        standardTemplate.setAttribute("formCancelLink", Links.getCancelLink(requestContext, AppPaths.ADMIN_CONFIG + "?cmd=" + AdminUtils.ADMIN_EMAIL_CMD).getString());
        standardTemplate.setAttribute("cmd", AdminUtils.ADMIN_POP_EMAIL_EDIT_2_CMD);

        //
        // Template: HeaderTemplate
        //
        HeaderTemplate headerTemplate = standardTemplate.getHeaderTemplate();
        headerTemplate.setTitleKey("admin.config.email.incomingServer.header");

        //
        // Template: ActionErrorsTemplate
        //
        ActionErrorsTemplate errorsTemplate = new ActionErrorsTemplate();
        standardTemplate.addTemplate(errorsTemplate);
        errorsTemplate.setMessage(Localizer.getText(requestContext, "admin.config.email.incomingServer.sectionHeader"));

        return standardTemplate.findTemplate(STANDARD_TEMPLATE);
    }
}
