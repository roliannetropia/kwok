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
<input type="hidden" name="_resubmit" value="true">
<input type="hidden" name="cmd" value="${cmd}"/>
<input type="hidden" name="test" value="0"/>
<table class="standardForm">
    <tr>
        <th><bean:message key="admin.config.email.pop.host"/>:</th>
        <td><html:text name="form" property="popHost" size="40"/></td>
    </tr>
    <tr>
        <th><bean:message key="admin.config.email.port"/>:</th>
        <td><html:text name="form" property="popPort" size="40"/>
            <br><span class="formFieldDesc"><bean:message key="admin.config.email.pop.port.description"/></span></td>
    </tr>
    <tr>
        <th><bean:message key="admin.config.email.username"/>:</th>
        <td><html:text name="form" property="popUsername" size="40"/></td>
    </tr>
    <tr>
        <th><bean:message key="admin.config.email.password"/>:</th>
        <td><html:password name="form" property="popPassword" size="40"/>
            <br><span class="formFieldDesc"><bean:message key="admin.config.email.password.description"/></span></td>
    </tr>
    <tr>
        <th><bean:message key="admin.config.email.pop.useSSL"/>:</th>
        <td><html:checkbox name="form" property="popUseSSL" value="true"/></td>
    </tr>
    <tr>
        <th><bean:message key="admin.config.email.pop.ignoreSender"/>:</th>
        <td><html:textarea name="form" property="popIgnoreSender" rows="10" cols="50"/>
        <br><span class="formFieldDesc"><bean:message key="admin.config.email.pop.ignoreSender.desc"/></span></td>
    </tr>
    <tr>
        <td>&nbsp;</td><td><html:submit onclick="disableButton(this)"><bean:message key="form.button.save"/></html:submit>&nbsp;
        <html:submit onclick="setFormFieldValue(this.form.test,1);disableButton(this)"><bean:message key="form.button.test"/></html:submit>
        ${formCancelLink}
        </td>
    </tr>
</table>
</form>
