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

<bean:define id="fileAddTemplate" name="FileAddTemplate" type="com.kwoksys.action.files.FileAddTemplate"/>

<!-- FileAddTemplate -->
<jsp:include page="/WEB-INF/jsp/common/template/ActionError.jsp"/>

<%-- Show input fields for adding a new File. --%>
<form action="${fileAddTemplate.formAction}" method="post" enctype="multipart/form-data">
<table class="listTable">
    <tr class="header1">
        <td colspan="2"><b><bean:message key="files.fileAdd"/></b></td>
    </tr>
    <tr>
        <td><bean:message key="files.filePath"/>:</td>
        <td><html:file name="form" property="file0" size="60"/>&nbsp;<bean:message key="common.requiredFieldIndicator.true"/></td>
    </tr>
    <tr>
        <td><bean:message key="files.colName.file_friendly_name"/>:</td>
        <td><html:text name="form" property="fileName0" value="${fileAddTemplate.fileName}" size="40"/>
            <br><span class="formFieldDesc"><bean:message key="files.colDesc.file_name"/></span>
        </td>
    </tr>
    <tr>
        <td>&nbsp;</td>
        <td><html:submit onclick="disableButton(this)"><bean:message key="form.button.upload"/></html:submit>
            <html:link action="${fileAddTemplate.formCancelAction}" styleClass="cancel"><bean:message key="form.button.cancel"/></html:link>
    </tr>
</table>
</form>
