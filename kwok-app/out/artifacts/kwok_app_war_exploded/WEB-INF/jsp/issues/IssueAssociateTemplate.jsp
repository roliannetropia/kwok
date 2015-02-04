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

<%-- Show Issue search form. --%>
<form action="${formSearchAction}" method="post">
<logic:notEmpty name="hardwareId"><html:hidden name="form" property="hardwareId"/></logic:notEmpty>
<logic:notEmpty name="softwareId"><html:hidden name="form" property="softwareId"/></logic:notEmpty>
<logic:notEmpty name="companyId"><html:hidden name="form" property="companyId"/></logic:notEmpty>

<table class="listTable">
    <tr class="header1">
        <td colspan="2"><b><bean:message key="common.linking.linkIssue"/></b></td>
    </tr>
    <tr>
        <td><bean:message key="issueMgmt.index.searchHeader"/>:</td>
        <td><bean:message key="issueMgmt.colName.issue_id"/>&nbsp;<input type="text" name="issueId" value="<bean:write name="form" property="issueId"/>" size="40" autofocus>
            <html:submit property="search" onclick="disableButton(this)"><bean:message key="form.button.search"/></html:submit>
            <logic:notEmpty name="issueAddPath">
                &nbsp;<bean:message key="common.condition.or"/>&nbsp;<a href="${issueAddPath}"><bean:message key="system.createLinkIssues"/></a>
            </logic:notEmpty>
        </td>
    </tr>
    <tr>
        <td><bean:message key="system.issueSelect"/>:</td>
        <td>
            <logic:notPresent name="issueList">
                <bean:message key="${selectIssueMessage}"/>
            </logic:notPresent>
            <logic:present name="issueList">
                <table class="noBorder">
                    <logic:iterate id="issue" name="issueList">
                        <tr><td><html:checkbox name="form" property="issueId" value="${issue.issueId}"/>
                            <bean:write name="issue" property="issueTitle" filter="false"/></td></tr>
                    </logic:iterate>
                </table>
            </logic:present>
        </td>
    </tr>
    <tr>
        <td>&nbsp;</td>
        <td>
            <html:submit onclick="this.form.action='${formSaveAction}';disableButton(this)" disabled="${disableSaveButton}"><bean:message key="form.button.save"/></html:submit>
            ${formCancelLink}
        </td>
    </tr>
</table>
</form>
