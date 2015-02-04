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

<form action="${formAction}" method="post">
<table class="standardForm">
    <tr><td colspan="2"><br><bean:message key="issuePlugin.issueAdd.confirm"/>
        <p><jsp:include page="/WEB-INF/jsp/common/template/ActionError.jsp"/></td></tr>
    <logic:notEmpty name="createdBy">
        <tr>
            <th><bean:message key="issueMgmt.colName.creator_name"/>:</th>
            <td><bean:write name="createdBy"/></td>
        </tr>
    </logic:notEmpty>
    <tr>
        <th><bean:message key="issueMgmt.colName.issue_type"/>:</th>
        <td><html:select name="form" property="type">
            <html:options collection="typeOptions" property="value" labelProperty="label"/>
            </html:select>
            <a href="#" onClick="window.open('${legendPath}', 'legend', 'width=480, height=420, left=50, top=50, scrollbars=yes, resizable=yes')">
                <img src="${image.helpIcon}" class="standard" alt=""></a>
        </td>
    </tr>
    <tr>
        <th><bean:message key="issueMgmt.colName.issue_priority"/>:</th>
        <td><html:select name="form" property="priority">
            <html:options collection="priorityOptions" property="value" labelProperty="label"/>
            </html:select></td>
    </tr>    
    <tr>
        <th><bean:message key="common.requiredFieldIndicator.true"/><bean:message key="issueMgmt.colName.issue_name"/>:</th>
        <td><html:text name="form" property="subject" size="40"/>
            <div class="formFieldDesc"><bean:message key="common.form.fieldMaxLen" arg0="${issueNameCharLimit}"/></div>
        </td>
    </tr>
    <tr>
        <th><bean:message key="common.requiredFieldIndicator.true"/><bean:message key="issueMgmt.colName.issue_description"/>:</th>
        <td><html:textarea name="form" property="description" rows="16" cols="60"/>
            <div class="formFieldDesc"><bean:message key="common.form.fieldMaxLen" arg0="${issueDescriptionCharLimit}"/></div>
        </td>
    </tr>
    <tr>
        <td>&nbsp;</td><td><html:submit onclick="disableButton(this)"/></td>
    </tr>
</table>
</form>
