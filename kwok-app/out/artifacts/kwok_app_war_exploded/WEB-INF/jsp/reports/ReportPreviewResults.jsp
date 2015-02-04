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

<h2><bean:message key="reports.workflow.previewResults.header"/></h2>

<logic:equal name="reportType" value="hardware_report">
    <%-- Some ajax script here. --%>
    <div id="hardwareDetail">&nbsp;</div>
    <script type="text/javascript">
    function hardwarePopup(obj, hardwareId) {
        cmenu.popupDiv = 'hardwareDetail';
        cmenu.url = '${ajaxHardwareDetailPath}';
        cmenu.dropit(obj, hardwareId);
    }
    </script>
</logic:equal>
<logic:equal name="reportType" value="hardware_member_report">
    <%-- Some ajax script here. --%>
    <div id="hardwareDetail">&nbsp;</div>
    <script type="text/javascript">
    function hardwarePopup(obj, hardwareId) {
        cmenu.popupDiv = 'hardwareDetail';
        cmenu.url = '${ajaxHardwareDetailPath}';
        cmenu.dropit(obj, hardwareId);
    }
    </script>
</logic:equal>

<form action="${formAction}" method="post">
<input type="hidden" name="_resubmit" value="true">
<table class="standard">
    <tr>
        <td colspan="2"><bean:message key="reports.workflow.previewResults.description"/>
            &nbsp;<input type="button" name="back" onclick="redirect('${backAction}')" value="<bean:message key="reports.button.back"/>">
            &nbsp;<html:submit onclick="disableButton(this)"><bean:message key="reports.button.next"/></html:submit><p>
        </td>
    </tr>
    <tr>
        <th><bean:message key="reports.workflow.previewResults.columns"/>:</th>
        <td>
            <input type="checkbox" onclick="checkAll(this, this.form.reportColumns);"><bean:message key="common.form.checkAll"/><br>
            <logic:iterate id="column" name="reportColumnOptions">
                <html:multibox name="ReportForm" property="reportColumns" value="${column.value}"/>${column.label}&nbsp;
            </logic:iterate>
        </td>
    </tr>
    <logic:notEmpty name="reportSortColumnOptions">
    <tr>
        <th><bean:message key="reports.workflow.previewResults.sortColumns"/>:</th>
        <td>
            <html:select name="ReportForm" property="reportSortColumns">
                <html:options collection="reportSortColumnOptions" property="value" labelProperty="label"/>
            </html:select>
            <html:select name="ReportForm" property="reportSortOrder">
                <html:options collection="reportSortOrderOptions" property="value" labelProperty="label"/>
            </html:select>
        </td>
    </tr>
    </logic:notEmpty>
</table>
<form>

<table class="standard">
<tr>
    <td align="right">
        <jsp:include page="/WEB-INF/jsp/common/template/RecordsNavigationWidget.jsp"/>
    </td>
</tr>
</table>

<jsp:include page="/WEB-INF/jsp/common/template/Table.jsp"/>
