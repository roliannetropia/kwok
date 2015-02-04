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
import com.kwoksys.biz.auth.AuthService;
import com.kwoksys.biz.admin.core.AdminUtils;
import com.kwoksys.biz.system.core.AppPaths;
import com.kwoksys.framework.http.RequestContext;
import com.kwoksys.framework.struts2.Action2;
import org.apache.struts.action.*;

/**
 * Action class for editing notification configuratons.
 */
public class ConfigLdapTest2Action extends Action2 {

    public String execute() throws Exception {
        ConfigForm actionForm = saveActionForm(new ConfigForm());

        AuthService authService = ServiceProvider.getAuthService(requestContext);

        ActionMessages errors = authService.testLdapConnection(actionForm.getLdapUrlScheme(),
                actionForm.getLdapUrl(), actionForm.getLdapUsername(), actionForm.getLdapPassword(),
                actionForm.getLdapSecurityPrincipal());

        if (errors.isEmpty()) {
            errors.add("connectPassed", new ActionMessage("admin.config.auth.ldapTest.passed"));
        }

        saveActionErrors(errors);
        return redirect(AppPaths.ADMIN_CONFIG + "?cmd=" + AdminUtils.ADMIN_LDAP_TEST_CMD + "&" + RequestContext.URL_PARAM_ERROR_TRUE);
    }
}