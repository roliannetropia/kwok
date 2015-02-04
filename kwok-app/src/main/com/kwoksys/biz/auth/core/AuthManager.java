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
package com.kwoksys.biz.auth.core;

import com.kwoksys.biz.admin.dto.AccessUser;
import com.kwoksys.framework.exceptions.UsernameNotFoundException;
import com.kwoksys.biz.auth.dao.AuthDao;
import com.kwoksys.biz.system.core.configs.LogConfigManager;
import com.kwoksys.biz.system.core.configs.ConfigManager;
import com.kwoksys.framework.exceptions.DatabaseException;
import com.kwoksys.framework.http.RequestContext;
import com.kwoksys.framework.connections.ldap.Ldap;
import org.apache.struts.action.ActionMessages;
import org.apache.struts.action.ActionMessage;

import java.util.logging.Logger;
import java.util.logging.Level;
import java.util.Date;

/**
 * AuthManager
 */
public class AuthManager {

    private static final Logger logger = Logger.getLogger(AuthManager.class.getName());
    private AccessUser accessUser;
    private RequestContext requestContext;
    private ActionMessages errors = new ActionMessages();

    public AuthManager(RequestContext requestContext, AccessUser accessUser) {
        this.requestContext = requestContext;
        this.accessUser = accessUser;
    }

    /**
     * Checks username/password format.
     * @return
     */
    public ActionMessages validateCredentialFormat() {
        if (accessUser.getUsername().isEmpty()) {
            errors.add("emptyUsername", new ActionMessage("auth.login.error.emptyUsername"));

        } else if (accessUser.getRequestedPassword().isEmpty()) {
            errors.add("emptyPassword", new ActionMessage("auth.login.error.emptyPassword"));
        }
         return errors;
    }

    /**
     * Check whether user is in database. This doesn't verify password. Will do it at a later step.
     * @return
     * @throws DatabaseException
     */
    public ActionMessages validateUserName() throws DatabaseException {
        try {
            AuthDao authDao = new AuthDao(requestContext);
            errors = authDao.isValidUsername(accessUser);
        } catch (UsernameNotFoundException e) {
            errors.add("noUsernameFound", new ActionMessage("auth.login.error.wrongCredential"));
            logger.warning(LogConfigManager.AUTHENTICATION_PREFIX + " Login failed for user: " + accessUser.getUsername() + ". No such username.");
        }
        return errors;
    }

    public ActionMessages validateUserStatus() {
        int accountLockout = ConfigManager.admin.getAccountLockoutValue();
        long accountLockoutMs = ConfigManager.admin.getAccountLockoutDurationMs();
        boolean isValidateAcctLockout = ConfigManager.admin.isValidateAcctLockout();

        if (accessUser.isAccountDisabled()) {
            errors.add("accountDisabled", new ActionMessage("auth.login.error.accountDisabled"));
            logger.warning(LogConfigManager.AUTHENTICATION_PREFIX + " Login failed for user: " + accessUser.getUsername() + ". Account disabled.");

        } else if (accessUser.isGuessUser()) {
            errors.add("noGuestUser", new ActionMessage("auth.login.error.noGuestUser"));

        } else {
            Date sysdate = requestContext.getSysdate();

            if (isValidateAcctLockout
                    && accessUser.getInvalidLogonCount() == accountLockout
                    && accessUser.getInvalidLogonDate().getTime() > (sysdate.getTime() - accountLockoutMs)) {
                errors.add("login", new ActionMessage("auth.login.error.accountLocked"));
            }
        }
        return errors;
    }

    /**
     * Authenticates against database user directory.
     * @return
     */
    public boolean authenticateDbUser() throws Exception {
        logger.log(Level.INFO, LogConfigManager.AUTHENTICATION_PREFIX + " Validating database user: " + accessUser.getUsername());
        boolean isValidLogin = AuthUtils.hashPassword(accessUser.getRequestedPassword()).equals(accessUser.getHashedPassword());

        if (!isValidLogin && !ConfigManager.auth.isLdapAuthEnabled()) {
            logger.warning(LogConfigManager.AUTHENTICATION_PREFIX + " Login failed for user: " + accessUser.getUsername() + ". Wrong password.");
        }
        return isValidLogin;
    }

    /**
     * Authenticates against LDAP user directory.
     * @return
     */
    public boolean authenticateLdapUser() {
        logger.log(Level.INFO, LogConfigManager.AUTHENTICATION_PREFIX + " Validating LDAP user: " + accessUser.getUsername());
        Ldap ldap = new Ldap();
        ldap.setUrl(ConfigManager.auth.getLdapUrlScheme() + ConfigManager.auth.getAuthLdapUrl());
        ldap.setUsername(accessUser.getUsername());
        ldap.setSecurityPrincipal(ConfigManager.auth.getAuthLdapSecurityPrincipal());
        ldap.setPassword(accessUser.getRequestedPassword());

        return ldap.authenticate().isEmpty();
    }

    public AccessUser getAccessUser() {
        return accessUser;
    }

    public ActionMessages getErrors() {
        return errors;
    }
}
