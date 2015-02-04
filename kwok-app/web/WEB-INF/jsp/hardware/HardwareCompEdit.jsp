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

<jsp:include page="/WEB-INF/jsp/hardware/HardwareSpecTemplate.jsp"/>

<jsp:include page="/WEB-INF/jsp/common/template/ActionError.jsp"/>

<%-- Show input fields for adding a new hardware component. --%>
<form action="${formAction}" method="post">
<html:hidden name="form" property="hardwareId"/>
<html:hidden name="form" property="compId"/>
<table class="tabBody">
    <tr class="header1">
        <td colspan="2"><b><bean:message key="itMgmt.cmd.hardwareComponentEdit"/></b></td>
    </tr>
    <tr>
    <td>
        <table class="standardForm">    
            <tr>
                <th nowrap><bean:message key="common.requiredFieldIndicator.true"/><bean:message key="common.column.hardware_component_type"/>:</th>
                <td><html:select name="form" property="hardwareComponentType">
                    <html:options collection="compOptions" property="value" labelProperty="label"/>
                    </html:select>&nbsp;</td>
            </tr>
            <tr>
                <th><bean:message key="common.column.hardware_component_description"/>:</th>
                <td class="row2"><html:text name="form" property="compDescription" size="60"/>
            </tr>
            <tr>
                <td colspan="2"><jsp:include page="/WEB-INF/jsp/common/template/CustomFieldsEdit.jsp"/></td>
            </tr>
            <tr>
                <th>&nbsp;</th>
                <td class="row2"><html:submit onclick="disableButton(this)"><bean:message key="form.button.save"/></html:submit>
                    ${formCancelLink}
                </td>
            </tr>
        </table>
    </td>
    </tr>
</table>
</form>

