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

<p class="section"><bean:message key="contracts.numContracts" arg0="${numContractRecords}"/>

<h3><bean:message key="contracts.filter"/></h3>

<%-- Contract filter --%>
<ul class="section">
<logic:iterate id="row" name="contractFilters">
    <li>${row.string}</li>
</logic:iterate>
</ul>

<%-- Contracts summary --%>
<h3><bean:message key="contracts.summary"/></h3>
<table class="standard section">
    <tr><td>
        <table><tr>
        <td>
            <p><b><bean:message key="contracts.summary.byExpiration"/></b>
            <p><table class="standard">
            <logic:iterate id="row" name="contractsSummary">
                <tr><th><bean:write name="row" property="text"/></th>
                    <td><bean:write name="row" property="path" filter="false"/></td></tr>
            </logic:iterate>
            </table>
        </td>
        </tr></table>
    </td></tr>
</table>

<%-- Contract search --%>
<h3><bean:message key="contracts.search"/></h3>
<jsp:include page="/WEB-INF/jsp/contracts/ContractSearchTemplate.jsp"/>
