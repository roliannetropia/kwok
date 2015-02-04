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

<form action="${formAction}" method="get">
<input type="hidden" name="cmd" value="filter">
<table class="standard">
<tr>
<logic:notEmpty name="searchResultText">
    <th><bean:write name="searchResultText" filter="false"/></th>
</logic:notEmpty>    
<td align="right">
<bean:message key="itMgmt.hardwareList.hardwareTypeFilter"/>:
    <html:select name="HardwareSearchForm" property="hardwareType" onchange="changeSelectedOption(this);">
        <html:options collection="hardwareTypeOptions" property="value" labelProperty="label"/>
    </html:select>
</td>
<td width="20%" align="right" nowrap>
    <jsp:include page="/WEB-INF/jsp/common/template/RecordsNavigationWidget.jsp"/>
</td>
</tr>
</table>
</form>

<%-- Some ajax script here. --%>
<div id="hardwareDetail">&nbsp;</div>
<script type="text/javascript">
function hardwarePopup(obj, hardwareId) {
    cmenu.popupDiv = 'hardwareDetail';
    cmenu.url = '${ajaxHardwareDetailPath}';
    cmenu.dropit(obj, hardwareId);
}
</script>

<jsp:include page="/WEB-INF/jsp/common/template/Table.jsp"/>
