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
import com.kwoksys.biz.system.core.FeatureManager;
import com.kwoksys.biz.system.core.Links;
import com.kwoksys.biz.system.core.configs.ConfigManager;
import com.kwoksys.framework.struts2.Action2;
import org.apache.struts.util.LabelValueBean;

import java.util.ArrayList;
import java.util.List;

/**
 * Action class for editing file settings.
 */
public class ConfigFileEditAction extends Action2 {

    public String execute() throws Exception {
        ConfigForm actionForm = (ConfigForm) getBaseForm(ConfigForm.class);

        // If not a resubmit, set some defaults
        if (!actionForm.isResubmit()) {
            actionForm.setKilobyteUnits(ConfigManager.file.getKilobyteUnits());
            actionForm.setFileRepositoryCompany(ConfigManager.file.getCompanyFileRepositoryLocation());
            actionForm.setFileRepositoryIssue(ConfigManager.file.getIssueFileRepositoryLocation());
            actionForm.setFileRepositoryHardware(ConfigManager.file.getHardwareFileRepositoryLocation());
            actionForm.setFileRepositorySoftware(ConfigManager.file.getSoftwareFileRepositoryLocation());
            actionForm.setFileRepositoryContract(ConfigManager.file.getContractFileRepositoryLocation());
            actionForm.setFileRepositoryKb(ConfigManager.file.getKbFileRepositoryLocation());
        }

        List kbUnitOptions = new ArrayList();
        for (Integer option : ConfigManager.file.getKilobyteUnitsList()) {
            kbUnitOptions.add(new LabelValueBean(String.valueOf(option), String.valueOf(option)));
        }

        //
        // Template: StandardTemplate
        //
        StandardTemplate standardTemplate = new StandardTemplate(requestContext, AdminUtils.ADMIN_FILE_EDIT_CMD);
        standardTemplate.setPathAttribute("formAction", AppPaths.ADMIN_CONFIG_WRITE);
        standardTemplate.setAttribute("formCancelLink", Links.getCancelLink(requestContext, AppPaths.ADMIN_CONFIG + "?cmd=" + AdminUtils.ADMIN_FILE_CMD).getString());
        standardTemplate.setAttribute("cmd", AdminUtils.ADMIN_FILE_EDIT_2_CMD);
        standardTemplate.setAttribute("disableFilePathUpdate", FeatureManager.isMultiAppsInstance());
        standardTemplate.setAttribute("kbUnitOptions", kbUnitOptions);

        //
        // Template: HeaderTemplate
        //
        HeaderTemplate headerTemplate = standardTemplate.getHeaderTemplate();
        headerTemplate.setTitleKey("admin.config.file.edit");

        //
        // Template: ActionErrorsTemplate
        //
        standardTemplate.addTemplate(new ActionErrorsTemplate());

        return standardTemplate.findTemplate(STANDARD_TEMPLATE);
    }
}