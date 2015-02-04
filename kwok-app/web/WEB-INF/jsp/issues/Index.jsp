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


<div class="section"><bean:message key="issueMgmt.numIssues" arg0="${numIssueRecords}"/>
<logic:equal name="hasIssueReadPermission" value="false"><bean:message key="issues.numAllowedIssues" arg0="${numIssuesHasPerm}"/></logic:equal>
</div>

<%-- Issue filter --%>
<table class="standard">
    <tr>
    <td>
        <h3><bean:message key="issueMgmt.index.filterHeader"/></h3>
        <ul>
        <logic:iterate id="row" name="links">
            <li><html:link action="${row.linkPath}"><bean:write name="row" property="linkText"/></html:link></li>
        </logic:iterate>
        </ul>
        <br><span class="formFieldDesc"><bean:message key="issueMgmt.index.searchDesc"/></span>
    </td>
    </tr>
</table>

<%-- Issue summary --%>
<logic:equal name="hasIssueReadPermission" value="true">
<h3><bean:message key="issueMgmt.index.issueCountHeader"/></h3>
<table class="standard"><tr>
    <th>
        <p><b><bean:message key="issueMgmt.index.countIssueByStatus"/></b>
        <p><table class="stats">
        <logic:iterate id="row" name="statusCountList">
            <tr class="${row.style}">
                <th><div><bean:write name="row" property="countKey" filter="false"/></div></th>
                <td><div><bean:write name="row" property="countValue" filter="false"/></div></td>
            </tr>
        </logic:iterate>
        </table>
    </th>
    <th>
        <p><b><bean:message key="issueMgmt.index.countOpenIssueByPriority"/></b>
        <p><table class="stats">
        <logic:iterate id="row" name="priorityCountList">
            <tr class="${row.style}">
                <th><div><bean:write name="row" property="countKey" filter="false"/></div></th>
                <td><div><bean:write name="row" property="countValue" filter="false"/></div></td>
            </tr>
        </logic:iterate>
        </table>
    </th>
    <th>
        <p><b><bean:message key="issueMgmt.index.countOpenIssueByType"/></b>
        <p><table class="stats">
        <logic:iterate id="row" name="typeCountList">
            <tr class="${row.style}">
                <th><div><bean:write name="row" property="countKey" filter="false"/></div></th>
                <td><div><bean:write name="row" property="countValue" filter="false"/></div></td>
            </tr>
        </logic:iterate>
        </table>
    </th>
    <th>
        <p><b><bean:message key="issueMgmt.index.countOpenIssueByAssignee"/></b>
        <p><table class="stats">
        <logic:iterate id="row" name="assigneeCountList">
            <tr class="${row.style}">
                <th><div><bean:write name="row" property="countKey" filter="false"/></div></th>
                <td><div><bean:write name="row" property="countValue" filter="false"/></div></td>
            </tr>
        </logic:iterate>
        </table>
    </th>
    </tr>
</table>
</logic:equal>

<h3><bean:message key="issueMgmt.index.searchHeader"/></h3>
<jsp:include page="/WEB-INF/jsp/issues/IssueSearchTemplate.jsp"/>
