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

<form action="${formAction}" method="post" enctype="multipart/form-data">
<input type="hidden" name="_resubmit" value="true">
<table class="listTable">
<tr class="header1">
    <th class="rownum">#</th>
    <th><bean:message key="common.column.hardware_name"/></th>
    <th><bean:message key="import.heading.action"/></th>
    <th><bean:message key="import.heading.messages"/></th>
</tr>
<logic:iterate id="row" name="dataList">
    <bean:define id="importItem" name="row" type="com.kwoksys.biz.admin.dto.ImportItem"/>

    <tr>
        <td>${importItem.rowNum}</td>
        <td>${importItem.title}</td>
        <td><bean:message key="import.validate.action.${importItem.action}"/></td>
        <td>
            <logic:notEmpty name="importItem" property="errorMessages">
                <span class="error"><bean:message key="import.validate.errors"/></span>:
                <ul>
                <logic:iterate id="message" name="importItem" property="errorMessages">
                    <li><bean:write name="message"/></li>
                </logic:iterate>
                </ul>
            </logic:notEmpty>
            <logic:notEmpty name="importItem" property="warningMessages">
                <span class="warning"><bean:message key="import.validate.warnings"/></span>:
                <ul>
                <logic:iterate id="message" name="importItem" property="warningMessages">
                    <li><bean:write name="message"/></li>
                </logic:iterate>
                </ul>
            </logic:notEmpty>
        </td>
    </tr>
</logic:iterate>
</table>
<p>
<html:submit onclick="disableButton(this)">Import</html:submit>&nbsp;
<input type="button" onclick="redirect('${formBackAction}')" value="<bean:message key="form.button.back"/>">
${formCancelLink}
</form>