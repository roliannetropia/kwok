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

<bean:define id="customFieldsTemplate" name="CustomFieldsTemplate" type="com.kwoksys.action.common.template.CustomFieldsTemplate"/>
<bean:define id="customFields" name="customFieldsTemplate" property="customFields"/>

<jsp:include page="/WEB-INF/jsp/issues/IssueSpecTemplate.jsp"/>

<jsp:include page="/WEB-INF/jsp/common/template/CustomFieldsTableToggle.jsp"/>

<jsp:include page="/WEB-INF/jsp/common/template/Tabs.jsp"/>

<table class="listTable">
    <tr class="header1"><th width="10">#</th><th align="left"><bean:message key="issueMgmt.issueDetail.historyModifierHeader"/></th><th align="left"><bean:write name="issueChangeCreationDateHeader" filter="false"/></th><th align="left"><bean:message key="issueMgmt.issueDetail.historyCommentHeader"/></th></tr>

    <logic:notEmpty name="issueHistoryList">
        <logic:iterate id="row" name="issueHistoryList" indexId="index">
            <tr class="${row.rowClass}">
                <td valign="top"><bean:write name="row" property="rownum"/></td>
                <td width="20%" valign="top" colspan="2">
                    <bean:write name="row" property="changeCreator" filter="false"/>
                    <logic:present name="row" property="changeCreatorEmail">
                        <br>(<bean:write name="row" property="changeCreatorEmail"/>)
                    </logic:present>
                    <br><bean:write name="row" property="changeCreationDate"/>
                </td>
                <td width="80%" valign="top">
                <logic:present name="row" property="changeComment">
                    <bean:write name="row" property="changeComment" filter="false"/><p>
                </logic:present>
                <logic:present name="row" property="changeFile">
                    <bean:write name="row" property="changeFile" filter="false"/><br>
                </logic:present>
                <logic:present name="row" property="changeSubject">
                    <bean:write name="row" property="changeSubject" filter="false"/><br>
                </logic:present>
                <logic:present name="row" property="changeType">
                    <bean:write name="row" property="changeType" filter="false"/><br>
                </logic:present>
                <logic:present name="row" property="changeStatus">
                    <bean:write name="row" property="changeStatus" filter="false"/><br>
                </logic:present>
                <logic:present name="row" property="changePriority">
                    <bean:write name="row" property="changePriority" filter="false"/><br>
                </logic:present>
                <logic:present name="row" property="changeResolution">
                    <bean:write name="row" property="changeResolution" filter="false"/><br>
                </logic:present>
                <logic:present name="row" property="changeAssignee">
                    <bean:write name="row" property="changeAssignee" filter="false"/><br>
                </logic:present>
            </td></tr>
        </logic:iterate>
    </logic:notEmpty>
    <%-- Show some message when there is no data --%>
    <logic:empty name="issueHistoryList">
        <jsp:include page="/WEB-INF/jsp/common/template/TableEmpty.jsp"/>
    </logic:empty>
</table>
