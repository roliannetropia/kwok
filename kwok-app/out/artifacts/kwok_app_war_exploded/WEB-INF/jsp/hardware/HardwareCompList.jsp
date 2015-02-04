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

<jsp:include page="/WEB-INF/jsp/hardware/HardwareSpecTemplate.jsp"/>

<jsp:include page="/WEB-INF/jsp/common/template/ActionError.jsp"/>

<jsp:include page="/WEB-INF/jsp/common/template/Tabs.jsp"/>

<%-- Hardware Components --%>
<form action="${deletePath}" method="post">
<html:hidden name="form" property="hardwareId" value="${hardwareId}"/>
<table class="listTable">
    <tr class="header1">
        <logic:equal name="canDeleteComponent" value="true">
            <td>&nbsp;</td>
        </logic:equal>
        <th align="left" width="30%"><bean:message key="common.column.hardware_component_type"/></th>
        <th align="left" width="55%"><bean:message key="common.column.hardware_component_description"/></th>
        <logic:equal name="canEditComponent" value="true">
            <th align="left" width="15%"><bean:message key="common.column.command"/></th>
        </logic:equal>
    </tr>
    <%-- Show the data --%>
    <logic:notEmpty name="components">
        <logic:iterate id="row" name="components">
            <bean:define id="component" name="row" property="component" type="com.kwoksys.biz.hardware.dto.HardwareComponent"/>
            <tr class="${row.rowClass}">
                <logic:equal name="canDeleteComponent" value="true">
                    <td valign="top"><html:radio name="form" property="compId" value="${component.id}"/></td>
                </logic:equal>
                <td valign="top">
                    <bean:write name="component" property="typeName"/>
                    <logic:notEmpty name="row" property="compPath">
                        <bean:write name="row" property="compPath" filter="false"/><div id="cf${component.id}" class="customFieldEmbedded" style="display:none;"></div>
                    </logic:notEmpty>
                </td>
                <td valign="top"><bean:write name="component" property="description"/></td>
                <logic:equal name="canEditComponent" value="true">
                    <td><html:link action="${row.editPath}"><bean:message key="form.button.edit"/></html:link>&nbsp;</td>
                </logic:equal>
            </tr>
        </logic:iterate>
        <logic:equal name="canDeleteComponent" value="true">
            <tr class="header1"><td colspan="${colspan}">
                <input type="submit" value="<bean:message key="itMgmt.hardwareComp.deleteButton"/>" onclick="return confirmSubmit('<bean:message key="common.form.confirmDelete"/>')">
            </td></tr>
        </logic:equal>
    </logic:notEmpty>
    <%-- Show some message when there is no data --%>
    <logic:empty name="components">
        <jsp:include page="/WEB-INF/jsp/common/template/TableEmpty.jsp"/>
    </logic:empty>
</table>
</form>
