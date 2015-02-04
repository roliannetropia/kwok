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
import com.kwoksys.framework.ui.WidgetUtils;
import org.apache.struts.util.LabelValueBean;

import java.util.ArrayList;
import java.util.List;

/**
 * Action class for editing configurations.
 */
public class ConfigAuthEditAction extends Action2 {

    public String execute() throws Exception {
        ConfigForm actionForm = (ConfigForm) getBaseForm(ConfigForm.class);

        // If not a resubmit, set some defaults
        if (!actionForm.isResubmit()) {
            actionForm.setAuthType(ConfigManager.auth.getAuthType());
            actionForm.setAuthMethod(ConfigManager.auth.getAuthMethod());
            actionForm.setAuthTimeout(ConfigManager.auth.getSessionTimeoutSeconds());
            actionForm.setLdapSecurityPrincipal(ConfigManager.auth.getAuthLdapSecurityPrincipal());
            actionForm.setLdapUrlScheme(ConfigManager.auth.getLdapUrlScheme());
            actionForm.setLdapUrl(ConfigManager.auth.getAuthLdapUrl());
            actionForm.setDomain(ConfigManager.auth.getAuthDomain());
            actionForm.setMinimumPasswordLength(ConfigManager.auth.getSecurityMinPasswordLength());
            actionForm.setPasswordComplexityEnabled(ConfigManager.admin.isSecurityPasswordComplexityEnabled());
            actionForm.setAllowBlankUserPassword(ConfigManager.admin.isAllowBlankUserPassword()? 1 : 0);
            actionForm.setAccountLockoutThreshold(ConfigManager.admin.getAccountLockoutThreshold());
            actionForm.setAccountLockoutDurationMinutes(ConfigManager.admin.getAccountLockoutDurationMinutes());
        }

        List authTypeOptions = new ArrayList();
        for (String option : ConfigManager.auth.getAuthTypeOptions()) {
            authTypeOptions.add(new LabelValueBean(Localizer.getText(requestContext, "admin.config.auth.type." + option), option));
        }

        List authMethodOptions = new ArrayList();
        for (String option : ConfigManager.auth.getAuthMethodOptions()) {
            authMethodOptions.add(new LabelValueBean(Localizer.getText(requestContext, "admin.config.auth.authenticationMethod." + option), option));
        }

        List sessionTimeoutOptions = new ArrayList();
        for (String option : ConfigManager.auth.getSessionTimeoutSecondsOptions()) {
            sessionTimeoutOptions.add(new LabelValueBean(String.valueOf(Integer.parseInt(option) / 3600), option));
        }

        List ldapUrlSchemeOptions = new ArrayList();
        for (String option : ConfigManager.auth.getLdapUrlSchemeOptions()) {
            ldapUrlSchemeOptions.add(new LabelValueBean(option, option));
        }

        List passwordLenOptions = new ArrayList();
        for (int i = 1; i <= 14; i++) {
            passwordLenOptions.add(new LabelValueBean(String.valueOf(i), String.valueOf(i)));
        }

        //
        // Template: StandardTemplate
        //
        StandardTemplate standardTemplate = new StandardTemplate(requestContext, AdminUtils.ADMIN_AUTH_EDIT_CMD);
        standardTemplate.setPathAttribute("formAction", AppPaths.ADMIN_CONFIG_WRITE);
        standardTemplate.setAttribute("formCancelLink", Links.getCancelLink(requestContext, AppPaths.ADMIN_CONFIG + "?cmd="
                + AdminUtils.ADMIN_AUTH_CMD).getString());
        request.setAttribute("cmd", AdminUtils.ADMIN_AUTH_EDIT_2_CMD);
        request.setAttribute("authTypeOptions", authTypeOptions);
        request.setAttribute("authMethodOptions", authMethodOptions);
        request.setAttribute("sessionTimeoutOptions", sessionTimeoutOptions);
        request.setAttribute("allowBlankUserPasswordOptions", WidgetUtils.getYesNoOptions(requestContext));
        request.setAttribute("passwordLenOptions", passwordLenOptions);
        request.setAttribute("passwordComplexityOptions", WidgetUtils.getBooleanOptions(requestContext));
        request.setAttribute("ldapUrlSchemeOptions", ldapUrlSchemeOptions);

        //
        // Template: HeaderTemplate
        //
        HeaderTemplate header = standardTemplate.getHeaderTemplate();
        header.setTitleKey("admin.config.auth.edit");

        //
        // Template: ActionErrorsTemplate
        //
        standardTemplate.addTemplate(new ActionErrorsTemplate());

        return standardTemplate.findTemplate(STANDARD_TEMPLATE);
    }
}
