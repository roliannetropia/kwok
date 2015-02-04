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

<bean:define id="tableTemplate" name="TableTemplate${RootTemplate.prefix}" type="com.kwoksys.action.common.template.TableTemplate"/>

<table class="${tableTemplate.style}">
<%-- Column headers --%>
<jsp:include page="/WEB-INF/jsp/common/template/TableHeader.jsp"/>

<logic:notEmpty name="tableTemplate" property="dataList">
    <logic:notEqual name="tableTemplate" property="canRemoveItem" value="true">
        <logic:iterate id="row" name="tableTemplate" property="dataList">
            <logic:empty name="row" property="rowClass">
                <tr>
            </logic:empty>
            <logic:notEmpty name="row" property="rowClass">
                <tr class="${row.rowClass}">
            </logic:notEmpty>

            <logic:iterate name="row" property="columns" id="column">
                <td><bean:write name="column" filter="false"/></td>
            </logic:iterate>
            </tr>
        </logic:iterate>
    </logic:notEqual>

    <logic:equal name="tableTemplate" property="canRemoveItem" value="true">
        <bean:define id="formRowIdName" name="tableTemplate" property="formRowIdName"/>

        <form action="${path.root}${tableTemplate.formRemoveItemAction}" method="post">
            <logic:iterate id="var" name="tableTemplate" property="formHiddenVariableMap">
                <html:hidden property="${var.key}" value="${var.value}"/>
            </logic:iterate>
            <logic:iterate id="row" name="tableTemplate" property="dataList">
                <logic:empty name="row" property="rowClass">
                    <tr>
                </logic:empty>
                <logic:notEmpty name="row" property="rowClass">
                    <tr class="${row.rowClass}">
                </logic:notEmpty>

                <td valign="top">
                    <logic:equal name="tableTemplate" property="formSelectMultipleRows" value="true">
                        <html:checkbox name="${tableTemplate.formName}" property="${formRowIdName}" value="${row.rowId}"/>
                    </logic:equal>
                    <logic:equal name="tableTemplate" property="formSelectMultipleRows" value="false">
                        <html:radio name="${tableTemplate.formName}" property="${formRowIdName}" value="${row.rowId}"/>
                    </logic:equal>
                </td>

                <logic:iterate id="column" property="columns" name="row">
                    <td><bean:write name="column" filter="false"/></td>
                </logic:iterate>
                </tr>
            </logic:iterate>

            <tr class="header1">
                <td colspan="<bean:write name="tableTemplate" property="colSpan"/>">
                    <logic:iterate id="button" name="tableTemplate" property="formButtons">
                        <input type="submit" value="<bean:message key="${button.key}"/>" onclick="return confirmSubmit('<bean:message key="${button.value}"/>')">
                    </logic:iterate>
                </td>
            </tr>
        </form>
    </logic:equal>
</logic:notEmpty>

<%-- Show some message when there is no data --%>
<logic:empty name="tableTemplate" property="dataList">
    <jsp:include page="/WEB-INF/jsp/common/template/TableEmpty.jsp"/>
</logic:empty>
</table>
