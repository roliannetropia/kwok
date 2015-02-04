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

<form action="${runReportAction}" target="_blank" method="post">
<input type="hidden" name="_resubmit" value="true">
<table class="standard">
    <tr>
        <td colspan="2"><bean:message key="reports.workflow.outputSelection.description"/>
            &nbsp;<input type="button" name="back" onclick="redirect('${reportBackAction}')" value="<bean:message key="reports.button.back"/>">
            &nbsp;<html:submit><bean:message key="reports.button.run"/></html:submit><p>
        </td>
    </tr>
    <tr>
        <th width="20%"><bean:message key="reports.field.report_title"/>:</th>
        <td width="80%"><html:text name="ReportForm" property="reportTitle" size="60"/>
            <span class="formFieldDesc"><bean:message key="reports.workflow.outputSelection.report_title.desc"/></span></td>
    </tr>
    <tr>
        <th><bean:message key="reports.field.output_format"/>:</th>
        <td><logic:iterate id="output" name="outputOptions">
                <html:radio name="ReportForm" property="outputType" value="${output}"/>
                <bean:message key="reports.workflow.output.${output}"/>&nbsp;
            </logic:iterate>
        </td>
    </tr>
</table>
</form>
