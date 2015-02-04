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

<h2><bean:message key="reports.workflow.reportCriteria.header"/></h2>

<div class="section">
<bean:message key="reports.workflow.reportCriteria.description"/>
&nbsp;<html:button property="back" onclick="redirect('${reportBackAction}')"><bean:message key="reports.button.back"/></html:button>
&nbsp;<html:submit onclick="disableButton2(this, document.${formName})"><bean:message key="reports.button.next"/></html:submit>
<p>
 
<logic:equal name="reportType" value="issue_report">
    <h4><bean:message key="reports.workflow.type.issue_report"/></h4>
    <jsp:include page="/WEB-INF/jsp/issues/IssueSearchTemplate.jsp"/>
</logic:equal>
<logic:equal name="reportType" value="hardware_report">
    <h4><bean:message key="reports.workflow.type.hardware_report"/></h4>
    <jsp:include page="/WEB-INF/jsp/hardware/HardwareSearchTemplate.jsp"/>
</logic:equal>
<logic:equal name="reportType" value="hardware_member_report">
    <h4><bean:message key="reports.workflow.type.hardware_member_report"/></h4>
    <jsp:include page="/WEB-INF/jsp/hardware/HardwareSearchTemplate.jsp"/>
</logic:equal>
<logic:equal name="reportType" value="hardware_license_report">
    <h4><bean:message key="reports.workflow.type.hardware_license_report"/></h4>
    <jsp:include page="/WEB-INF/jsp/hardware/HardwareSearchTemplate.jsp"/>
</logic:equal>
<logic:equal name="reportType" value="software_report">
    <h4><bean:message key="reports.workflow.type.software_report"/></h4>
    <jsp:include page="/WEB-INF/jsp/software/SoftwareSearchTemplate.jsp"/>
</logic:equal>
<logic:equal name="reportType" value="software_usage_report">
    <h4><bean:message key="reports.workflow.type.software_usage_report"/></h4>
    <jsp:include page="/WEB-INF/jsp/software/SoftwareSearchTemplate.jsp"/>
</logic:equal>
<logic:equal name="reportType" value="contract_report">
    <h4><bean:message key="reports.workflow.type.contract_report"/></h4>
    <jsp:include page="/WEB-INF/jsp/contracts/ContractSearchTemplate.jsp"/>
</logic:equal>
</div>