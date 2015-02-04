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

<bean:define id="issue" name="issue" type="com.kwoksys.biz.issues.dto.Issue"/>

<jsp:include page="/WEB-INF/jsp/issues/IssueSpecTemplate.jsp"/>

<jsp:include page="/WEB-INF/jsp/common/template/ActionError.jsp"/>

<form action="${formAction}" method="post">
<input type="hidden" name="_resubmit" value="true">
<html:hidden name="form" property="issueId"/>
<table class="standardForm">
    <tr>
        <th width="20%"><bean:message key="common.requiredFieldIndicator.true"/><bean:message key="issueMgmt.colName.issue_name"/>:</th>
        <td><html:text name="form" property="subject" size="40"/></td>
    </tr>
    <tr>
        <th><bean:message key="common.requiredFieldIndicator.true"/><bean:message key="issueMgmt.colName.issue_history_description"/>:</th>
        <td><html:textarea name="form" property="followup" rows="10" cols="50"/></td>
    </tr>
    <tr>
        <th><bean:message key="common.requiredFieldIndicator.true"/><bean:message key="issueMgmt.colName.issue_type"/>:</th>
        <td><html:select name="form" property="type" onchange="changeAction(this, '${formThisAction}');">
            <html:options collection="typeOptions" property="value" labelProperty="label"/>
            </html:select></td>
    </tr>
    <tr>
        <th><bean:message key="common.requiredFieldIndicator.true"/><bean:message key="issueMgmt.colName.issue_status"/>:</th>
        <td><html:select name="form" property="status">
            <html:options collection="statusOptions" property="value" labelProperty="label"/>
            </html:select></td>
    </tr>
    <tr>
        <th><bean:message key="common.requiredFieldIndicator.true"/><bean:message key="issueMgmt.colName.issue_priority"/>:</th>
        <td><html:select name="form" property="priority">
            <html:options collection="priorityOptions" property="value" labelProperty="label"/>
            </html:select></td>
    </tr>
    <tr>
        <th><bean:message key="issueMgmt.colName.issue_resolution"/>:</th>
        <td><html:select name="form" property="resolution">
            <html:options collection="resolutionOptions" property="value" labelProperty="label"/>
            </html:select></td>
    </tr>
    <tr>
        <th><bean:message name="issue" property="attrRequiredMsgKey(issue_assignee)"/><bean:message key="issueMgmt.colName.assignee_name"/>:</th>
        <td><html:select name="form" property="assignedTo">
            <html:options collection="assignedToOptions" property="value" labelProperty="label"/>
            </html:select>
        </td>
    </tr>
    <tr>
        <th><bean:message key="issueMgmt.colName.issue_subscriber"/>:</th>
        <td>
            <table><tr>
                <td>
                <span class="formSubscriberHeader"><bean:message key="issueMgmt.issueAdd.availableSubscribers"/></span><Br>
                <html:select name="form" property="availableSubscribers" multiple="true" size="5" styleClass="formSubscriberSelectbox">
                <html:options collection="availableSubscribersOptions" property="value" labelProperty="label"/>
                </html:select>
                </td>
                <td valign="middle">
                <html:button property="add" onclick="moveOptions(this.form.availableSubscribers, this.form.selectedSubscribers)" styleClass="formSubscriberButton"><bean:message key="issueMgmt.issueAdd.addSubsribers"/></html:button><br>
                <html:button property="remove" onclick="moveOptions(this.form.selectedSubscribers, this.form.availableSubscribers)" styleClass="formSubscriberButton"><bean:message key="issueMgmt.issueAdd.removeSubsribers"/></html:button>
                </td>
                <td>
                <span class="formSubscriberHeader"><bean:message key="issueMgmt.issueAdd.selectedSubscribers"/></span><Br>
                <html:select name="form" property="selectedSubscribers" multiple="true" size="5" styleClass="formSubscriberSelectbox">
                <html:options collection="selectedSubscribersOptions" property="value" labelProperty="label"/>
                </html:select>
                </td>
            </tr></table>
        </td>
    </tr>
    <tr>
        <th><bean:message key="issueMgmt.issueEdit.emailNotification"/>:</th>
        <td>
            <logic:equal name="emailNotification" value="true">
                <bean:message key="issueMgmt.issueEdit.emailNotificationOn"/>
                <br><html:checkbox name="form" property="suppressNotification" value="1"/><bean:message key="issueMgmt.issueEdit.suppressNotification"/>
            </logic:equal>
            <logic:equal name="emailNotification" value="false">
                <bean:message key="issueMgmt.issueEdit.emailNotificationOff"/>
            </logic:equal>
        </td>
    </tr>
    <tr>
        <th><bean:message key="issueMgmt.colName.issue_due_date"/>:</th>
        <td>
            <html:radio name="form" property="hasDueDate" value="0" onclick="toggleIssueDueDate(this)"/>
            <bean:message key="issueMgmt.issueEdit.issueHasNoDueDate"/>
            <br>
            <html:radio name="form" property="hasDueDate" value="1" onclick="toggleIssueDueDate(this)"/>
            <bean:message key="issueMgmt.issueEdit.issueHasDueDate"/>
            <html:select name="form" property="dueDateMonth" disabled="${formDisableIssueDueDate}">
            <html:options collection="dueMonthOptions" property="value" labelProperty="label"/>
            </html:select>
            <html:select name="form" property="dueDateDate" disabled="${formDisableIssueDueDate}">
            <html:options collection="dueDateOptions" property="value" labelProperty="label"/>
            </html:select>
            <html:select name="form" property="dueDateYear" disabled="${formDisableIssueDueDate}">
            <html:options collection="dueYearOptions" property="value" labelProperty="label"/>
            </html:select>
        </td>
    </tr>
    <tr>
        <td colspan="2"><jsp:include page="/WEB-INF/jsp/common/template/CustomFieldsEdit.jsp"/></td>
    </tr>
    <tr>
        <td>&nbsp;</td>
        <td><html:submit onclick="selectAllOptions(this.form.selectedSubscribers);disableButton(this)"><bean:message key="form.button.save"/></html:submit>
            ${formCancelLink}
        </td>
    </tr>
</table>
</form>
