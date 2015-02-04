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

<div class="section">
<p><bean:message key="itMgmt.hardwareIndex.numRecords" arg0="${numHardwareRecords}"/>

<div>
    <ul><logic:iterate id="row" name="linkList">
        <li><html:link action="${row.urlPath}"><bean:write name="row" property="urlText"/></html:link></li>
        </logic:iterate></ul>
</div>
</div>

<%-- Hardware summary. --%>
<h3><bean:message key="itMgmt.index.hardwareTypeCountHeader"/></h3>
<table class="section standard">
    <tr>
    <%-- Hardware type count --%>
    <th>
        <p><b><bean:message key="itMgmt.index.hardwareTypeCountDesc"/></b> &nbsp;
        <p>
        <table class="stats">
        <logic:iterate id="row" name="hardwareTypeCountList">
            <tr class="${row.style}">
                <th><a href="${row.path}"><div><bean:write name="row" property="countKey" filter="false"/></div></a></th>
                <td><a href="${row.path}"><div><bean:write name="row" property="countValue"/></div></a></td></tr>
        </logic:iterate>
        </table>
    </th>
    <%-- Hardware status count --%>
    <th class="borderLeft">
        <p><b><bean:message key="itMgmt.index.hardwareStatusCountDesc"/></b> &nbsp;
        <p>
        <table class="stats">
        <logic:iterate id="row" name="hardwareStatusCounts">
            <tr class="${row.style}">
                <th><a href="${row.path}"><div><bean:write name="row" property="countKey"/></div></a></th>
                <td><a href="${row.path}"><div><bean:write name="row" property="countValue"/></div></a></td></tr>
        </logic:iterate>
        </table>
    </th>
    <%-- Hardware location count --%>
    <th class="borderLeft">
        <p><b><bean:message key="itMgmt.index.hardwareLocationCountDesc"/></b>
        <p><table class="stats">
        <logic:iterate id="row" name="hardwareLocationCountList">
            <tr class="${row.style}">
                <th><a href="${row.path}"><div><bean:write name="row" property="countKey"/></div></a></th>
                <td><a href="${row.path}"><div><bean:write name="row" property="countValue"/></div></a></td></tr>
        </logic:iterate>
        </table>
    </th>
    </tr>
</table>

<%-- Hardware search --%>
<h3><bean:message key="itMgmt.index.searchHeader"/></h3>

<jsp:include page="/WEB-INF/jsp/hardware/HardwareSearchTemplate.jsp"/>
