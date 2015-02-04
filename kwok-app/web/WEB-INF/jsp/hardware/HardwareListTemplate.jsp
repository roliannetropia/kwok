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

<bean:define id="thisTemplate" name="HardwareListTemplate${RootTemplate.prefix}" type="com.kwoksys.action.hardware.HardwareListTemplate"/>

<table class="listTable">
    <logic:notEmpty name="thisTemplate" property="listHeader">
        <tr class="header3">
            <td colspan="<bean:write name="thisTemplate" property="colspan"/>">
                <b><bean:message name="thisTemplate" property="listHeader"/></b></td>
        </tr>
    </logic:notEmpty>
<logic:equal name="thisTemplate" property="canRemoveHardware" value="true">
    <form action="${formRemoveHardwareAction}" method="post">
        <logic:iterate id="var" name="thisTemplate" property="formHiddenVariableMap">
            <html:hidden property="${var.key}" value="${var.value}"/>
        </logic:iterate>
        <jsp:include page="/WEB-INF/jsp/common/template/TableHeader.jsp"/>
        <logic:notEmpty name="thisTemplate" property="formattedList">
            <logic:iterate id="row" name="thisTemplate" property="formattedList">
                <tr class="${row.rowClass}">
                    <td valign="top"><html:radio name="form" property="formHardwareId" value="${row.hardwareId}"/></td>

                <logic:iterate id="column" property="columns" name="row">
                    <td><bean:write name="column" filter="false"/></td>
                </logic:iterate>
                </tr>
            </logic:iterate>
            <tr class="header1">
                <td colspan="<bean:write name="thisTemplate" property="colspan"/>">
                    <input type="submit" value="<bean:message key="form.button.remove"/>" onclick="return confirmSubmit('<bean:message key="common.form.confirmRemove"/>')">
                </td>
            </tr>
        </logic:notEmpty>
        <%-- Show some message when there is no data --%>
        <logic:empty name="thisTemplate" property="formattedList">
            <jsp:include page="/WEB-INF/jsp/common/template/TableEmpty.jsp"/>
        </logic:empty>
    </form>
</logic:equal>
<logic:notEqual name="thisTemplate" property="canRemoveHardware" value="true">
    <jsp:include page="/WEB-INF/jsp/common/template/TableHeader.jsp"/>
    <logic:notEmpty name="thisTemplate" property="formattedList">
        <logic:iterate id="row" name="thisTemplate" property="formattedList">
            <tr class="${row.rowClass}">
                <logic:iterate id="column" property="columns" name="row">
                    <td><bean:write name="column" filter="false"/></td>
                </logic:iterate>
            </tr>
        </logic:iterate>
    </logic:notEmpty>
    <%-- Show some message when there is no data --%>
    <logic:empty name="thisTemplate" property="formattedList">
        <jsp:include page="/WEB-INF/jsp/common/template/TableEmpty.jsp"/>
    </logic:empty>
</logic:notEqual>
</table>