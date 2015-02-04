<%--
 * ====================================================================
 * Copyright 2005-2012 Wai-Lun Kwok
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
--%>
<%@ taglib uri="http://struts.apache.org/tags-bean" prefix="bean" %>
<%@ taglib uri="http://struts.apache.org/tags-logic" prefix="logic" %>

<table class="standard details">
    <tr>
        <th><bean:message key="admin.config.auth.type"/>:</th>
        <td><bean:write name="authType"/></td>
    </tr>
    <tr>
        <th><bean:message key="admin.config.auth.authenticationMethod"/>:</th>
        <td><bean:write name="authenticationMethod"/></td>
    </tr>
    <tr>
        <th><bean:message key="admin.config.auth.ldapUrl"/>:</th>
        <td><bean:write name="authLdapUrl"/></td>
    </tr>
    <tr>
        <th><bean:message key="admin.config.auth.ldapSecurityPrincipal"/>:</th>
        <td><bean:write name="authLdapSecurityPrincipal"/></td>
    </tr>
    <tr>
        <th><bean:message key="admin.config.auth.domain"/>:</th>
        <td><bean:write name="authDomain"/></td>
    </tr>
    <tr>
        <th><bean:message key="admin.config.auth.sessionTimeoutSeconds"/>:</th>
        <td><bean:write name="authSessionTimeout"/></td>
    </tr>
    <tr>
        <td colspan="2"><h3><bean:message key="admin.config.security.passwordPolicy"/></h3></td>
    </tr>
    <tr>
        <th><bean:message key="admin.config.security.allowBlankUserPassword"/>:</th>
        <td><bean:write name="allowBlankUserPassword"/></td>
    </tr>
    <tr>
        <th><bean:message key="admin.config.security.minimumPasswordLength"/>:</th>
        <td><bean:message key="admin.config.security.minimumPasswordLength.value" arg0="${minimumPasswordLength}"/></td>
    </tr>
    <tr>
        <th><bean:message key="admin.config.security.passwordComplexity"/>:</th>
        <td><bean:write name="passwordComplexity"/></td>
    </tr>
    <tr>
        <td colspan="2"><h3><bean:message key="admin.config.security.accountLockoutPolicy"/></h3></td></tr>
    <tr>
        <th><bean:message key="admin.config.security.accountLockoutThreshold"/>:</th>
        <td><bean:write name="accountLockoutThreshold"/></td>
    </tr>
    <tr>
        <th><bean:message key="admin.config.security.accountLockoutDuration"/>:</th>
        <td><bean:message key="admin.config.security.accountLockoutDuration.value" arg0="${accountLockoutDuration}"/>
            <br><span class="formFieldDesc"><bean:write name="accountLockoutDescription"/></span></td>
    </tr>
</table>
