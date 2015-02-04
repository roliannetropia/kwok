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
package com.kwoksys.biz.system.core.configs;

import com.kwoksys.biz.auth.core.Access;

import java.util.Arrays;
import java.util.List;
import java.util.Map;

/**
 * AuthConfigManager
 */
public class AuthConfigManager extends BaseConfigManager {

    private static final AuthConfigManager instance = new AuthConfigManager();

    private String authMethod;

    private String[] authMethodOptions;

    private String authLdapSecurityPrincipal;

    private String ldapUrlScheme;

    private String[] ldapUrlSchemeOptions;

    private String authLdapUrl;

    private String authDomain;

    private List<String> authDomainOptions;

    private int sessionTimeoutHours;

    private int sessionTimeoutSeconds;

    private String[] sessionTimeoutSecondsOptions;

    private String authType;

    private String[] authTypeOptions;

    private boolean isBasicAuth;

    private int securityMinPasswordLength;

    private AuthConfigManager() {}

    static AuthConfigManager getInstance() {
        return instance;
    }

    /**
     * Initializes configurations.
     * @param configMap
     * @return
     */
    void init(Map<String, String> configMap) {
        setConfigMap(configMap);

        authMethod = configMap.get(SystemConfigNames.AUTH_METHOD);
        authMethodOptions = configMap.get(SystemConfigNames.AUTH_METHOD_OPTIONS).split(",");
        authLdapSecurityPrincipal = configMap.get(SystemConfigNames.AUTH_LDAP_SECURITY_PRINCIPAL);
        ldapUrlScheme = configMap.get(SystemConfigNames.AUTH_LDAP_URL_SCHEME);
        ldapUrlSchemeOptions = new String[] {"ldap://", "ldaps://"};
        authLdapUrl = configMap.get(SystemConfigNames.AUTH_LDAP_URL);
        authDomain = configMap.get(SystemConfigNames.AUTH_DOMAIN);
        authDomainOptions = Arrays.asList(authDomain.split(","));
        // This would take timeout seconds and turn it into hours.
        sessionTimeoutSeconds = getInt(SystemConfigNames.AUTH_TIMEOUT);
        sessionTimeoutHours = getInt(SystemConfigNames.AUTH_TIMEOUT) / 3600;
        sessionTimeoutSecondsOptions = configMap.get(SystemConfigNames.AUTH_TIMEOUT_OPTIONS).split(",");

        securityMinPasswordLength = getInt(SystemConfigNames.SECURITY_MIN_PASSWORD_LENGTH);

        authType = configMap.get(SystemConfigNames.AUTH_TYPE);
        isBasicAuth = configMap.get(SystemConfigNames.AUTH_TYPE).equals("basic");
        authTypeOptions = getStringArray(SystemConfigNames.AUTH_TYPE_OPTIONS);
    }

    public boolean isDbAuthEnabled() {
        return authMethod.equals(Access.AUTH_APP) || authMethod.equals(Access.AUTH_MIXED);
    }

    public boolean isLdapAuthEnabled() {
        return authMethod.equals(Access.AUTH_LDAP) || authMethod.equals(Access.AUTH_MIXED);
    }

    public int getSessionTimeoutHours() {
        return sessionTimeoutHours;
    }
    public int getSessionTimeoutSeconds() {
        return sessionTimeoutSeconds;
    }
    public String[] getSessionTimeoutSecondsOptions() {
        return sessionTimeoutSecondsOptions;
    }

    public String getAuthMethod() {
        return authMethod;
    }
    public String[] getAuthMethodOptions() {
        return authMethodOptions;
    }
    public String getAuthLdapUrl() {
        return authLdapUrl;
    }
    public String getAuthDomain() {
        return authDomain;
    }
    public List<String> getAuthDomainOptions() {
        return authDomainOptions;
    }
    public String getAuthType() {
        return authType;
    }

    public boolean isBasicAuth() {
        return isBasicAuth;
    }

    public String getAuthLdapSecurityPrincipal() {
        return authLdapSecurityPrincipal;
    }

    public int getSecurityMinPasswordLength() {
        return securityMinPasswordLength;
    }

    public String[] getAuthTypeOptions() {
        return authTypeOptions;
    }

    public String getLdapUrlScheme() {
        return ldapUrlScheme;
    }

    public String[] getLdapUrlSchemeOptions() {
        return ldapUrlSchemeOptions;
    }
}
