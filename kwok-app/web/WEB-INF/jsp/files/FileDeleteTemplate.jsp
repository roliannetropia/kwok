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

<bean:define id="fileDeleteTemplate" name="FileDeleteTemplate" type="com.kwoksys.action.files.FileDeleteTemplate"/>

<!-- FileDeleteTemplate -->
<jsp:include page="/WEB-INF/jsp/common/template/ActionError.jsp"/>

<form method="post" action="${fileDeleteTemplate.formAction}">
<table class="tabBody">
    <tr class="header1">
        <td colspan="2"><b><bean:message key="files.fileDelete"/></b></td>
    </tr>
    <tr>
        <td colspan="2"><img src="${image.deleteIcon}" class="standard" alt="">
            <b><bean:message key="files.fileDelete.warning"/></b>
            <p>
            <table class="standard">
                <tr>
                    <th><bean:message key="files.colName.file_name"/>:</th>
                    <td><bean:write name="fileDeleteTemplate" property="fileName"/></td>
                </tr>
                <tr>
                    <th><bean:message key="files.colName.file_friendly_name"/>:</th>
                    <td><bean:write name="fileDeleteTemplate" property="fileTitle"/></td>
                </tr>
                <tr>
                    <th><bean:message key="files.colName.file_byte_size"/>:</th>
                    <td><bean:write name="fileDeleteTemplate" property="fileSize"/></td>
                </tr>
            </table>
            <blockquote><html:submit onclick="disableButton(this)"><bean:message key="form.button.delete"/></html:submit>&nbsp;
                <html:link action="${fileDeleteTemplate.formCancelAction}" styleClass="cancel"><bean:message key="form.button.cancel"/></html:link>
            </blockquote>
        </td>
    </tr>
</table>
</form>
