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
        <th><bean:message key="common.requiredFieldIndicator.true"/><bean:message key="common.column.company_name"/>:</th>
        <td><input type="text" name="companyName" value="<bean:write name="form" property="companyName"/>" size="40" autofocus></td>
    </tr>
    <tr>
        <th><bean:message key="common.column.company_description"/>:</th>
        <td><html:textarea name="form" property="companyDescription" rows="10" cols="50"/></td>
    </tr>
    <tr>
        <th><bean:message key="common.column.company_types"/>:</th>
        <td>
            <logic:iterate id="type" name="companyTypes">
                <html:multibox name="form" property="companyTypes" value="${type.value}"/>${type.label}<br/>
            </logic:iterate>
        </td>
    </tr>
    <tr>
        <th><bean:message key="common.column.company_tags"/>:</th>
        <td><html:text name="form" property="companyTags" size="60"/>
            <br><span class="formFieldDesc"><bean:message key="contactMgmt.colDesc.company_tags"/></span>
        </td>
    </tr>
    <tr>
        <td colspan="2"><jsp:include page="/WEB-INF/jsp/common/template/CustomFieldsEdit.jsp"/></td>
    </tr>
    <tr>
        <td>&nbsp;</td><td><html:submit onclick="disableButton(this)"><bean:message key="form.button.add"/></html:submit>
        ${formCancelLink}
    </td>
    </tr>
</table>
</form>
