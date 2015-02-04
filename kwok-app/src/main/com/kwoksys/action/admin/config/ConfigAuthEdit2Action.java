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
import com.kwoksys.biz.auth.core.Access;
import com.kwoksys.framework.http.RequestContext;
import com.kwoksys.biz.system.core.configs.SystemConfigNames;
import com.kwoksys.framework.struts2.Action2;
import org.apache.struts.action.*;

import java.util.ArrayList;
import java.util.List;

/**
 * Action class for editing configurations.
 */
public class ConfigAuthEdit2Action extends Action2 {

    public String execute() throws Exception {
        ConfigForm actionForm = saveActionForm(new ConfigForm());
        String authType = actionForm.getAuthType();
        String authMethod = actionForm.getAuthMethod();
        String ldapUrl = actionForm.getLdapUrl();

        ActionMessages errors = new ActionMessages();

        if (authMethod.equals(Access.AUTH_LDAP) && ldapUrl.isEmpty()) {
            errors.add("emptyLdapUrl", new ActionMessage("admin.config.error.emptyLdapUrl"));
        }

        if (errors.isEmpty()) {
            List list = new ArrayList();
            list.add(new SystemConfig(SystemConfigNames.AUTH_TYPE, authType));
            list.add(new SystemConfig(SystemConfigNames.AUTH_METHOD, authMethod));
            list.add(new SystemConfig(SystemConfigNames.AUTH_LDAP_URL_SCHEME, actionForm.getLdapUrlScheme()));
            list.add(new SystemConfig(SystemConfigNames.AUTH_LDAP_URL, ldapUrl));
            list.add(new SystemConfig(SystemConfigNames.AUTH_LDAP_SECURITY_PRINCIPAL, actionForm.getLdapSecurityPrincipal()));
            list.add(new SystemConfig(SystemConfigNames.AUTH_DOMAIN, actionForm.getDomain()));
            list.add(new SystemConfig(SystemConfigNames.AUTH_TIMEOUT, String.valueOf(actionForm.getAuthTimeout())));
            list.add(new SystemConfig(SystemConfigNames.ALLOW_BLANK_USER_PASSWORD, String.valueOf(actionForm.getAllowBlankUserPassword())));
            list.add(new SystemConfig(SystemConfigNames.SECURITY_MIN_PASSWORD_LENGTH, String.valueOf(actionForm.getMinimumPasswordLength())));
            list.add(new SystemConfig(SystemConfigNames.SECURITY_USER_PASSWORD_COMPLEX_ENABLED, actionForm.isPasswordComplexityEnabled()));
            list.add(new SystemConfig(SystemConfigNames.SECURITY_ACCOUNT_LOCKOUT_THRESHOLD, actionForm.getAccountLockoutThreshold()));
            list.add(new SystemConfig(SystemConfigNames.SECURITY_ACCOUNT_LOCKOUT_DURATION_MINUTES, actionForm.getAccountLockoutDurationMinutes()));

            // Get the service
            AdminService adminService = ServiceProvider.getAdminService(requestContext);
            errors = adminService.updateConfig(list);
        }

        if (!errors.isEmpty()) {
            saveActionErrors(errors);
            return redirect(AppPaths.ADMIN_CONFIG_WRITE + "?cmd=" + AdminUtils.ADMIN_AUTH_EDIT_CMD + "&" + RequestContext.URL_PARAM_ERROR_TRUE);

        } else {
            return redirect(AppPaths.ADMIN_CONFIG + "?cmd=" + AdminUtils.ADMIN_AUTH_CMD);
        }
    }
}

