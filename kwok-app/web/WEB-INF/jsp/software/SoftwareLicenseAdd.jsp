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
<bean:define id="softwareLicense" name="softwareLicense" type="com.kwoksys.biz.software.dto.SoftwareLicense"/>

<jsp:include page="/WEB-INF/jsp/software/SoftwareSpecTemplate.jsp"/>

<jsp:include page="/WEB-INF/jsp/common/template/ActionError.jsp"/>

<form action="${formAction}" method="post">
<input type="hidden" name="hasCustomFields" value="true">
<table class="tabBody">
    <tr class="header1">
        <td colspan="2"><b><bean:message key="software.license.addLicenseHeader"/></b></td>
    </tr>
    <tr>
        <td>
            <table class="standardForm">
                    <tr>
                        <th nowrap="nowrap"><bean:message key="common.requiredFieldIndicator.true"/><bean:message key="common.column.license_key"/>:</th>
                        <td><input type="text" name="licenseKey" value="<bean:write name="form" property="licenseKey"/>" size="40" autofocus></td>
                    </tr>
                    <tr>
                        <th><bean:message key="common.column.license_note"/>:</th>
                        <td><html:textarea name="form" property="licenseNote" rows="2" cols="40"/>&nbsp;</td>
                    </tr>
                    <tr>
                        <th><bean:message key="common.requiredFieldIndicator.true"/><bean:message key="common.column.license_entitlement"/>:</th>
                        <td><html:text name="form" property="licenseEntitlement" size="10"/>&nbsp;</td>
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
        </td>
    </tr>
</table>
</form>
