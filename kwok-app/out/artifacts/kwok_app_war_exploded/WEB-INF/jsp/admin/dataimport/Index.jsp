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

<jsp:include page="/WEB-INF/jsp/common/template/ActionError.jsp"/>

<form action="${formAction}" method="post" enctype="multipart/form-data">
<input type="hidden" name="_resubmit" value="true">
<table class="standardForm">
<tr>
    <th>
        <bean:message key="import.selectTypes"/>:
    </th>
    <td>
        <html:select name="DataImportForm" property="importType">
            <html:options collection="importTypeOptions" property="value" labelProperty="label"/>
        </html:select>
    </td>
</tr>
<tr>
    <th>
        <bean:message key="common.requiredFieldIndicator.true"/><bean:message key="import.selectImportFile"/>:
    </th>
    <td>
        <input type="file" name="file0" size="60">
        &nbsp;<a href="${path.hardwareImportSample}" target="_blank">Sample file</a>
    </td>
</tr>
<tr>
    <th>
        &nbsp;
    </th>
    <td>
        <html:submit onclick="disableButton(this)"><bean:message key="form.button.next"/></html:submit>
        ${formCancelLink}
    </td>
</tr>
</table>
</form>
