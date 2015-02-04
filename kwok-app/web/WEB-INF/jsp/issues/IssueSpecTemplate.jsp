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
<%@ taglib uri="http://struts.apache.org/tags-logic" prefix="logic" %>

<bean:define id="issueSpecTemplate" name="IssueSpecTemplate" type="com.kwoksys.action.issues.IssueSpecTemplate"/>
<bean:define id="issue" name="issueSpecTemplate" property="issue" type="com.kwoksys.biz.issues.dto.Issue"/>

<h2><bean:write name="issueSpecTemplate" property="headerText"/></h2>

<table class="standard">
    <tr>
        <th width="20%"><bean:message key="issueMgmt.colName.issue_id"/>:</th>
        <td><bean:write name="issue" property="id"/></td>
    </tr>
    <tr>
        <th><bean:message key="issueMgmt.colName.issue_description"/>:</th>
        <td><div id="issueText">
                <logic:equal name="issueSpecTemplate" property="hasHtmlContent" value="true">
                    <a href="javascript:issueDisplayHtml()" onmouseover="showContent('issueTextDesc');" onmouseout="hideContent('issueTextDesc');">[Show HTML]</a>
                    <span id="issueTextDesc" style="display:none; border:1px solid #ccc; background-color:#F6F6F6;">Note: Rendering contents in HTML format will also download pictures in the message</span>
                </logic:equal>
                <div id="issueTextContent">
                    <bean:write name="TemplateIssueSpec_issue" property="description" filter="false"/>
                </div>
            </div>
            <div id="issueHtml" style="display:none;">
                <a href="javascript:issueDisplayText()">[Show plain-text]</a>
                <div id="issueHtmlContent">
                </div>
            </div>
        </td>
    </tr>
    <tr>
        <th><bean:message key="issueMgmt.colName.issue_url"/>:</th>
        <td><bean:write name="TemplateIssueSpec_issue" property="url"/></td>
    </tr>

    <tr>
        <th><bean:message key="issueMgmt.colName.issue_type"/>:</th>
        <td><bean:write name="TemplateIssueSpec_issueTypeName"/></td>
    </tr>
    <tr>
        <th><bean:message key="issueMgmt.colName.issue_status"/>:</th>
        <td><bean:write name="TemplateIssueSpec_issueStatusName"/></td>
    </tr>
    <tr>
        <th><bean:message key="issueMgmt.colName.issue_priority"/>:</th>
        <td><bean:write name="TemplateIssueSpec_issuePriorityName"/></td>
    </tr>
    <tr>
        <th><bean:message key="issueMgmt.colName.issue_resolution"/>:</th>
        <td><bean:write name="TemplateIssueSpec_issueResolutionName"/></td>
    </tr>
    <tr>
        <th><bean:message key="issueMgmt.colName.assignee_name"/>:</th>
        <td><bean:write name="TemplateIssueSpec_issueAssignee" filter="false"/></td>
    </tr>
    <tr>
        <th><bean:message key="issueMgmt.colName.issue_due_date"/>:</th>
        <td><bean:write name="TemplateIssueSpec_issue" property="dueDateShort"/></td>
    </tr>
    <logic:notEmpty name="TemplateIssueSpec_issue" property="fromEmail">
        <tr>
            <th><bean:message key="issueMgmt.colName.issue_created_from_email"/>:</th>
            <td><bean:write name="TemplateIssueSpec_issue" property="fromEmail"/></td>
        </tr>
    </logic:notEmpty>
    <tr>
        <th><bean:message key="issueMgmt.colName.creator"/>:</th>
        <td><bean:write name="TemplateIssueSpec_issueCreatorInfo" filter="false"/></td>
    </tr>
    <tr>
        <th><bean:message key="issueMgmt.colName.creator_ip"/>:</th>
        <td><bean:write name="TemplateIssueSpec_issue" property="creatorIP"/></td>
    </tr>
    <tr>
        <th><bean:message key="issueMgmt.colName.modifier"/>:</th>
        <td><bean:write name="TemplateIssueSpec_issueModifierInfo" filter="false"/></td>
    </tr>

    <logic:notEmpty name="TemplateIssueSpec_issueSubscribers">
    <tr>
        <th><bean:message key="issueMgmt.colName.issue_subscriber"/>:</th>
        <td><bean:write name="TemplateIssueSpec_issueSubscribers"/></td>
    </tr>
    </logic:notEmpty>
</table>
<p>