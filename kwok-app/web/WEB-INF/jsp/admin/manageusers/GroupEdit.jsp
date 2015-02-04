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
<html:hidden name="form" property="groupId"/>
<table class="standardForm">
    <tr>
        <th><bean:message key="common.column.group_id"/>:</th>
        <td><bean:write name="form" property="groupId"/></td>
    </tr>
    <tr>
        <th><bean:message key="common.requiredFieldIndicator.true"/><bean:message key="common.column.group_name"/>:</th>
        <td><html:text name="form" property="groupName" size="40"/></td>
    </tr>
    <tr>
        <th><bean:message key="common.column.group_description"/>:</th>
        <td><html:textarea name="form" property="groupDescription" rows="10" cols="50"/></td>
    </tr>
    <tr>
        <th><bean:message key="common.column.group_members"/>:</th>
        <td><table><tr>
            <td>
            <span class="formSubscriberHeader"><bean:message key="admin.groupMembersEdit.availableMembers"/></span><Br>
            <html:select name="form" property="availableMembers" multiple="true" size="5" styleClass="formSubscriberSelectbox">
            <html:options collection="availableMembersOptions" property="value" labelProperty="label"/>
            </html:select>
            </td>
            <td valign="middle">
                <input type="button" name="add" onclick="moveOptions(this.form.availableMembers, this.form.selectedMembers)" class="formSubscriberButton" value="<bean:message key="issueMgmt.issueAdd.addSubsribers"/>"><br>
                <input type="button" name="remove" onclick="moveOptions(this.form.selectedMembers, this.form.availableMembers)" class="formSubscriberButton" value="<bean:message key="issueMgmt.issueAdd.removeSubsribers"/>">
            </td>
            <td>
            <span class="formSubscriberHeader"><bean:message key="admin.groupMembersEdit.selectedMembers"/></span><Br>
            <html:select name="form" property="selectedMembers" multiple="true" size="5" styleClass="formSubscriberSelectbox">
            <html:options collection="selectedMembersOptions" property="value" labelProperty="label"/>
            </html:select>
            </td>
            </tr></table>
        </td>
    </tr>
    <tr>
        <td>&nbsp;</td>
        <td>
            <html:submit onclick="selectAllOptions(this.form.selectedMembers);disableButton(this)"><bean:message key="form.button.save"/></html:submit>
            ${formCancelLink}
        </td>
    </tr>
</table>
</form>
