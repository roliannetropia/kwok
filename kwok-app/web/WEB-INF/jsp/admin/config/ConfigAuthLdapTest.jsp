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
<%@ taglib uri="http://struts.apache.org/tags-html" prefix="html" %>
<%@ taglib uri="http://struts.apache.org/tags-logic" prefix="logic" %>

<jsp:include page="/WEB-INF/jsp/common/template/ActionMessages.jsp"/>

<form action="${formAction}" method="post">
<input type="hidden" name="cmd" value="${cmd}"/>
<table class="standardForm">
    <tr>
        <th width="30%"><bean:message key="common.requiredFieldIndicator.true"/><bean:message key="admin.config.auth.ldapUrl"/>:</th>
        <td><html:select name="form" property="ldapUrlScheme">
                <html:options collection="ldapUrlSchemeOptions" property="value" labelProperty="label"/>
            </html:select>
            <html:text name="form" property="ldapUrl" size="60"/>
            <br><span class="formFieldDesc"><bean:message key="admin.configDesc.auth.ldapUrl"/></span></td>
    </tr>
    <tr>
        <th><bean:message key="common.requiredFieldIndicator.true"/><bean:message key="admin.config.auth.ldap.username"/>:</th>
        <td><html:text name="form" property="ldapUsername" size="60"/>
            <br><span class="formFieldDesc"><bean:message key="admin.config.auth.ldap.username.description"/></span></td>
    </tr>
    <tr>
        <th><bean:message key="common.requiredFieldIndicator.true"/><bean:message key="admin.config.auth.ldap.password"/>:</th>
        <td><html:password name="form" property="ldapPassword" size="60"/></td>
    </tr>
    <tr>
        <th><bean:message key="admin.config.auth.ldapSecurityPrincipal"/>:</th>
        <td><html:text name="form" property="ldapSecurityPrincipal" size="60"/>
            <br><span class="formFieldDesc"><bean:message key="admin.config.auth.ldapSecurityPrincipal.desc"/></span>
        </td>
    </tr>
    <tr>
        <td>&nbsp;</td><td>
        <html:submit><bean:message key="form.button.test"/></html:submit>
        ${formCancelLink}
        </td>
    </tr>
</table>
</form>
