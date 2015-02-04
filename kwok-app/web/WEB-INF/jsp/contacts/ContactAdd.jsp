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

<jsp:include page="/WEB-INF/jsp/common/template/ActionError.jsp"/>

<form action="${formAction}" method="post">
<table class="standardForm">
    <tr>
        <th width="15%"><bean:message key="common.requiredFieldIndicator.true"/><bean:message key="common.column.contact_first_name"/>:</th>
        <td width="35%"><input type="text" name="contactFirstName" value="<bean:write name="form" property="contactFirstName"/>" size="40" autofocus></td>
        <th width="15%"><bean:message key="common.requiredFieldIndicator.true"/><bean:message key="common.column.contact_last_name"/>:</th>
        <td width="35%"><html:text name="form" property="contactLastName" size="40"/></td>
    </tr>
    <tr>
        <th><bean:message key="common.column.contact_title"/>:</th>
        <td><html:text name="form" property="contactTitle" size="40"/></td>
        <th><bean:message key="common.column.company_name"/>:</th>
        <td><html:select name="form" property="companyId">
            <html:options collection="companyIdOptions" property="value" labelProperty="label"/>
            </html:select></td>
    </tr>
    <tr>
        <th><bean:message key="common.column.contact_phone_work"/>:</th>
        <td><html:text name="form" property="contactPhoneWork" size="40"/></td>
        <th><bean:message key="common.column.contact_phone_mobile"/>:</th>
        <td><html:text name="form" property="contactPhoneMobile" size="40"/></td>
    </tr>
    <tr>
        <th><bean:message key="common.column.contact_phone_home"/>:</th>
        <td><html:text name="form" property="contactPhoneHome" size="40"/></td>
        <th><bean:message key="common.column.contact_fax"/>:</th>
        <td><html:text name="form" property="contactFax" size="40"/></td>
    </tr>
    <tr>
        <th><bean:message key="common.column.contact_email_primary"/>:</th>
        <td><html:text name="form" property="contactEmailPrimary" size="40"/></td>
        <th><bean:message key="common.column.contact_email_secondary"/>:</th>
        <td><html:text name="form" property="contactEmailSecondary" size="40"/></td>
    </tr>
    <tr>
        <th><bean:message key="common.column.contact_im"/>:</th>
        <td><html:select name="form" property="messenger1Type">
            <html:options collection="messengerTypeOptions" property="value" labelProperty="label"/>
            </html:select>
            <html:text name="form" property="messenger1Id" size="24"/>
        </td>
        <th><bean:message key="common.column.contact_im"/>:</th>
        <td>
            <html:select name="form" property="messenger2Type">
            <html:options collection="messengerTypeOptions" property="value" labelProperty="label"/>
            </html:select>
            <html:text name="form" property="messenger2Id" size="24"/>
        </td>
    </tr>
    <tr>
        <th><bean:message key="common.column.contact_homepage_url"/>:</th>
        <td><html:text name="form" property="contactHomepageUrl" size="40"/>
            <br><span class="formFieldDesc"><bean:message key="contactMgmt.colExample.website"/></span></td>
        <th><bean:message key="common.column.contact_description"/>:</th>
        <td><html:textarea name="form" property="contactDescription" rows="10" cols="30"/></td>
    </tr>
    <tr>
        <td colspan="4" align="center">
            <html:submit onclick="disableButton(this)"><bean:message key="form.button.add"/></html:submit>
            ${formCancelLink}
        </td>
    </tr>
</table>
</form>
