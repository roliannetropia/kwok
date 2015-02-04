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

<bean:define id="software" name="software" type="com.kwoksys.biz.software.dto.Software"/>

<jsp:include page="/WEB-INF/jsp/common/template/ActionError.jsp"/>

<form action="${formAction}" method="post">
<input type="hidden" name="_resubmit" value="true">
<html:hidden name="form" property="softwareId"/>
<table class="standardForm">
    <tr>
        <th><bean:message key="common.column.software_id"/>:</th>
        <td><bean:write name="software" property="id"/></td>
    </tr>
    <tr>
        <th><bean:message key="common.requiredFieldIndicator.true"/><bean:message key="common.column.software_name"/>:</th>
        <td><input type="text" name="softwareName" value="<bean:write name="form" property="softwareName"/>" size="40" autofocus></td>
    </tr>
    <tr>
        <th><bean:message key="common.column.software_version"/>:</th>
        <td><html:text name="form" property="version" size="40"/></td>
    </tr>
    <tr>
        <th><bean:message key="common.column.software_description"/>:</th>
        <td><html:textarea name="form" property="softwareDescription" rows="10" cols="50"/></td>
    </tr>
    <tr>
        <th><bean:message key="common.column.software_owner"/>:</th>
        <td><html:select name="form" property="softwareOwner">
                <html:options collection="softwareOwnerOptions" property="value" labelProperty="label"/>
            </html:select>
        </td>
    </tr>
    <tr>
    <th><bean:message key="common.column.software_manufacturer"/>:</th>
    <td><html:select name="form" property="manufacturerId">
        <html:options collection="manufacturersOptions" property="value" labelProperty="label"/>
        </html:select> <html:img src="${image.helpIcon}" titleKey="help.addSoftwareMaker" align="absmiddle"/></td>
    </tr>
    <tr>
    <th><bean:message key="common.column.software_vendor"/>:</th>
    <td><html:select name="form" property="vendorId">
        <html:options collection="vendorsOptions" property="value" labelProperty="label"/>
        </html:select> <html:img src="${image.helpIcon}" titleKey="help.addSoftwareVendor" align="absmiddle"/></td>
    </tr>    
    <tr>
        <th><bean:message name="software" property="attrRequiredMsgKey(software_type)"/><bean:message key="common.column.software_type"/>:</th>
        <td><html:select name="form" property="softwareType" onchange="changeAction(this, '${formThisAction}');">
            <html:options collection="softwareTypeOptions" property="value" labelProperty="label"/></html:select></td>
    </tr>
    <tr>
        <th><bean:message name="software" property="attrRequiredMsgKey(software_platform)"/><bean:message key="common.column.software_platform"/>:</th>
        <td><html:select name="form" property="softwareOS">
            <html:options collection="softwareOsOptions" property="value" labelProperty="label"/>
            </html:select></td>
    </tr>
    <tr>
        <th><bean:message key="common.column.software_quoted_retail_price"/>:</th>
        <td><html:text name="form" property="retailPrice" size="40"/></td>
    </tr>
    <tr>
        <th><bean:message key="common.column.software_quoted_oem_price"/>:</th>
        <td><html:text name="form" property="oemPrice" size="40"/></td>
    </tr>
    <tr>
        <th><bean:message key="common.column.software_expire_date"/>:</th>
        <td><html:select name="form" property="expireDateM">
                <html:options collection="monthOptions" property="value" labelProperty="label"/>
            </html:select>
            <html:select name="form" property="expireDateD">
                <html:options collection="dateOptions" property="value" labelProperty="label"/>
            </html:select>
            <html:select name="form" property="expireDateY">
                <html:options collection="yearOptions" property="value" labelProperty="label"/>
            </html:select>
        </td>
    </tr>
    <tr>
        <td colspan="2"><jsp:include page="/WEB-INF/jsp/common/template/CustomFieldsEdit.jsp"/></td>
    </tr>
    <tr>
        <td>&nbsp;</td><td><html:submit onclick="disableButton(this)"><bean:message key="form.button.save"/></html:submit>
            ${formCancelLink}
    </td>
    </tr>
</table>
</form>
