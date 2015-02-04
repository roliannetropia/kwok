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
        <th width="30%"><bean:message key="admin.config.email.notification"/>:</th>
        <td><html:select name="form" property="notificationMethod">
            <html:options collection="notificationMethodOptions" property="value" labelProperty="label"/>
            </html:select></td>
    </tr>
    <tr>
        <th><bean:message key="admin.config.email.smtp.host"/>:</th>
        <td><html:text name="form" property="smtpHost" size="40"/></td>
    </tr>
    <tr>
        <th><bean:message key="admin.config.email.port"/>:</th>
        <td><html:text name="form" property="smtpPort" size="40"/>
            <br><span class="formFieldDesc"><bean:message key="admin.configDesc.smtp.port"/></span></td>
    </tr>
    <tr>
        <th><bean:message key="admin.config.email.username"/>:</th>
        <td><html:text name="form" property="smtpUsername" size="40"/></td>
    </tr>
    <tr>
        <th><bean:message key="admin.config.email.password"/>:</th>
        <td><html:password name="form" property="smtpPassword" size="40"/>
            <br><span class="formFieldDesc"><bean:message key="admin.config.email.password.description"/></span></td>
    </tr>
    <tr>
        <th><bean:message key="admin.config.email.smtp.from"/>:</th>
        <td><html:text name="form" property="smtpFrom" size="40"/></td>
    </tr>
    <tr>
        <th><bean:message key="admin.config.email.smtp.to"/>:</th>
        <td><html:text name="form" property="smtpTo" size="40"/></td>
    </tr>
    <tr>
        <th><bean:message key="admin.config.email.smtp.starttls"/>:</th>
        <td><html:select name="form" property="smtpStarttls">
            <html:options collection="smtpStarttlsOptions" property="value" labelProperty="label"/>
            </html:select></td>
    </tr>
    <tr>
        <th><bean:message key="admin.config.email.domainFiltering"/>:</th>
        <td><html:select name="form" property="domainFiltering">
            <html:options collection="domainFilteringOptions" property="value" labelProperty="label"/>
            </html:select></td>
    </tr>
    <tr>
        <th><bean:message key="admin.config.email.allowedDomains"/>:</th>
        <td><html:text name="form" property="allowedDomains" size="40"/>
            <br><span class="formFieldDesc"><bean:message key="admin.configDesc.email.allowedDomains"/></span></td>
    </tr>
    <tr>
        <th><bean:message key="admin.config.email.issueReportTemplate"/>:</th>
        <td><table><tr>
            <td><html:textarea name="form" property="reportIssueEmailTemplate" cols="60" rows="10"/>
                <br><span class="formFieldDesc"><bean:message key="admin.configDesc.email.issueTemplate"/></span></td>
            <td><pre class="formFieldDesc"><bean:write name="defaultReportIssueTemplate" filter="false"/></pre></td></tr></table>
        </td>
    </tr>
    <tr>
        <th><bean:message key="admin.config.email.issueAddTemplate"/>:</th>
        <td><table><tr>
            <td><html:textarea name="form" property="issueAddEmailTemplate" cols="60" rows="10"/>
                <br><span class="formFieldDesc"><bean:message key="admin.configDesc.email.issueTemplate"/></span></td>
            <td><pre class="formFieldDesc"><bean:write name="defaultAddIssueTemplate" filter="false"/></pre></td></tr></table>
        </td>
    </tr>
    <tr>
        <th><bean:message key="admin.config.email.issueUpdateTemplate"/>:</th>
        <td><table><tr>
            <td><html:textarea name="form" property="issueUpdateEmailTemplate" cols="60" rows="10"/>
                <br><span class="formFieldDesc"><bean:message key="admin.configDesc.email.issueTemplate"/></span></td>
            <td><pre class="formFieldDesc"><bean:write name="defaultUpdateIssueTemplate" filter="false"/></pre></td></tr></table>
        </td>
    </tr>
    <tr>
        <td>&nbsp;</td><td><html:submit onclick="disableButton(this)"><bean:message key="form.button.save"/></html:submit>&nbsp;
        <html:submit onclick="setFormFieldValue(this.form.test,1);disableButton(this)"><bean:message key="form.button.test"/></html:submit>
        ${formCancelLink}</td>
    </tr>
</table>
</form>
