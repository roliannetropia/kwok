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
import com.kwoksys.framework.properties.Localizer;
import com.kwoksys.framework.struts2.Action2;
import com.kwoksys.framework.ui.Link;

/**
 * Action class for security settings.
 */
public class ConfigAuthAction extends Action2 {

    public String execute() throws Exception {
        AccessUser user = requestContext.getUser();

        //
        // Template: StandardTemplate
        //
        StandardTemplate standardTemplate = new StandardTemplate(requestContext, AdminUtils.ADMIN_AUTH_CMD);

        request.setAttribute("authType", Localizer.getText(requestContext,
                "admin.config.auth.type." + ConfigManager.auth.getAuthType()));

        request.setAttribute("authenticationMethod", Localizer.getText(requestContext,
                "admin.config.auth.authenticationMethod." + ConfigManager.auth.getAuthMethod()));

        request.setAttribute("authLdapUrl", ConfigManager.auth.getLdapUrlScheme() + ConfigManager.auth.getAuthLdapUrl());

        request.setAttribute("authLdapSecurityPrincipal", ConfigManager.auth.getAuthLdapSecurityPrincipal());

        request.setAttribute("authDomain", ConfigManager.auth.getAuthDomain());

        request.setAttribute("authSessionTimeout", Localizer.getText(requestContext, "admin.configData.auth.sessionTimeoutHours",
                new Object[]{ConfigManager.auth.getSessionTimeoutHours()}));

        request.setAttribute("allowBlankUserPassword", Localizer.getText(requestContext,
                ConfigManager.admin.isAllowBlankUserPassword() ? "common.boolean.yes_no.yes" : "common.boolean.yes_no.no"));

        request.setAttribute("minimumPasswordLength", ConfigManager.auth.getSecurityMinPasswordLength());

        request.setAttribute("passwordComplexity", Localizer.getText(requestContext,
                "common.boolean.true_false." + ConfigManager.admin.isSecurityPasswordComplexityEnabled()));

        request.setAttribute("accountLockoutThreshold", ConfigManager.admin.getAccountLockoutThreshold());

        request.setAttribute("accountLockoutDuration", ConfigManager.admin.getAccountLockoutDurationMinutes());

        request.setAttribute("accountLockoutDescription", ConfigManager.admin.isValidateAcctLockout() ?
                Localizer.getText(requestContext, "admin.config.security.accountLockoutOn.description",
                        new Object[]{ConfigManager.admin.getAccountLockoutDurationMinutes(),
                        ConfigManager.admin.getAccountLockoutThreshold()}) :
                Localizer.getText(requestContext, "admin.config.security.accountLockoutOff.description"));

        //
        // Template: HeaderTemplate
        //
        HeaderTemplate header = standardTemplate.getHeaderTemplate();
        header.setTitleKey("admin.config.auth");
        
        if (Access.hasPermission(user, AppPaths.ADMIN_CONFIG_WRITE)) {
            Link link = new Link(requestContext);
            link.setAjaxPath(AppPaths.ADMIN_CONFIG_WRITE + "?cmd=" + AdminUtils.ADMIN_AUTH_EDIT_CMD);
            link.setTitleKey("common.command.Edit");
            header.addHeaderCmds(link);
        }
        if (Access.hasPermission(user, AppPaths.ADMIN_CONFIG)) {
            Link link = new Link(requestContext);
            link.setAjaxPath(AppPaths.ADMIN_CONFIG + "?cmd=" + AdminUtils.ADMIN_LDAP_TEST_CMD);
            link.setTitleKey("admin.cmd.ldapTest");
            header.addHeaderCmds(link);
        }

        // Back to admin home
        header.addNavLink(Links.getAdminHomeLink(requestContext));
        header.addNavLink(new Link(requestContext).setTitleKey("admin.config.auth"));

        return standardTemplate.findTemplate(STANDARD_TEMPLATE);
    }
}