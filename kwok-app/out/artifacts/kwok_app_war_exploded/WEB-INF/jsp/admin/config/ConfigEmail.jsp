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

<h3><bean:message key="admin.config.email.outgoingServer.header"/>
    <logic:equal name="canEditSmtpSettings" value="true">
        <span class="command"> [${editSmtpSettingsLink}]</span>
    </logic:equal>
</h3>
<table class="standard details">
    <tr>
        <th width="30%"><bean:message key="admin.config.email.notification"/>:</th>
        <td><bean:write name="emailNotification"/></td>
    </tr>
    <tr>
        <th><bean:message key="admin.config.email.smtp.host"/>:</th>
        <td><bean:write name="smtpHost"/></td>
    </tr>
    <tr>
        <th><bean:message key="admin.config.email.port"/>:</th>
        <td><bean:write name="smtpPort"/></td>
    </tr>
    <tr>
        <th><bean:message key="admin.config.email.username"/>:</th>
        <td><bean:write name="smtpUsername"/></td>
    </tr>
    <tr>
        <th><bean:message key="admin.config.email.password"/>:</th>
        <td><logic:notEmpty name="smtpPassword">
                <bean:message key="admin.configData.smtp.password"/>
                <span class="formFieldDesc">(<bean:message key="admin.configDesc.smtp.password"/>)</span>
            </logic:notEmpty>
        </td>
    </tr>
    <tr>
        <th><bean:message key="admin.config.email.smtp.from"/>:</th>
        <td><bean:write name="emailFrom"/></td>
    </tr>
    <tr>
        <th><bean:message key="admin.config.email.smtp.to"/>:</th>
        <td><bean:write name="emailTo"/></td>
    </tr>
    <tr>
        <th><bean:message key="admin.config.email.smtp.starttls"/>:</th>
        <td><bean:write name="starttls"/></td>
    </tr>
    <tr>
        <th><bean:message key="admin.config.email.domainFiltering"/>:</th>
        <td><bean:write name="emailDomainFilter"/></td>
    </tr>
    <tr>
        <th><bean:message key="admin.config.email.allowedDomains"/>:</th>
        <td><bean:write name="emailAllowedDomains"/></td>
    </tr>    
    <tr>
        <th><bean:message key="admin.config.email.issueReportTemplate"/>:</th>
        <td><bean:write name="issueReportEmailTemplate" filter="false"/></td>
    </tr>
    <tr>
        <th><bean:message key="admin.config.email.issueAddTemplate"/>:</th>
        <td><bean:write name="issueAddEmailTemplate" filter="false"/></td>
    </tr>
    <tr>
        <th><bean:message key="admin.config.email.issueUpdateTemplate"/>:</th>
        <td><bean:write name="issueUpdateEmailTemplate" filter="false"/></td>
    </tr>
</table>

<%-- Incoming Email Settings --%>
<h3><bean:message key="admin.config.email.incomingServer.header"/>
    <logic:equal name="canEditPopSettings" value="true">
       <span class="command"> [${editPopSettingsLink}]</span>
    </logic:equal>
</h3>
<table class="standard details">
    <tr>
        <th width="30%"><bean:message key="admin.config.email.pop.host"/>:</th>
        <td><bean:write name="popHost"/></td>
    </tr>
    <tr>
        <th><bean:message key="admin.config.email.port"/>:</th>
        <td><bean:write name="popPort"/></td>
    </tr>
    <tr>
        <th><bean:message key="admin.config.email.username"/>:</th>
        <td><bean:write name="popUsername"/></td>
    </tr>
    <tr>
        <th><bean:message key="admin.config.email.password"/>:</th>
        <td><logic:notEmpty name="popPassword">
                <bean:message key="admin.configData.smtp.password"/>
                <span class="formFieldDesc">(<bean:message key="admin.configDesc.smtp.password"/>)</span>
            </logic:notEmpty>
        </td>
    </tr>
    <tr>
        <th><bean:message key="admin.config.email.pop.useSSL"/>:</th>
        <td><bean:message key="common.boolean.true_false.${popUseSSL}"/></td>
    </tr>
    <tr>
        <th><bean:message key="admin.config.email.pop.ignoreSender"/>:</th>
        <td><bean:write name="popIgnoreSender" filter="false"/></td>
    </tr>
</table>
