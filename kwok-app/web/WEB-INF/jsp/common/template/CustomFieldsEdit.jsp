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

<bean:define id="customFieldsTemplate" name="CustomFieldsTemplate" type="com.kwoksys.action.common.template.CustomFieldsTemplate"/>

<logic:iterate id="attrMap" name="CustomFieldsTemplate" property="customFields">

<logic:notEqual name="customFieldsTemplate" property="partialTable" value="true">
    <table class="standardForm2">
</logic:notEqual>

    <tr><td colspan="2"><h3><bean:write name="attrMap" property="key" filter="false"/></h3></td></tr>    
<logic:iterate id="field" name="attrMap" property="value">
    <bean:define id="attr" name="field" property="attr" type="com.kwoksys.biz.admin.dto.Attribute"/>
    <tr>
    <th><bean:write name="field" property="name"/>:</th>
    <td>
    <%-- Here is the logic to display different input depending on attribute type --%>
    <%-- Attribute Type: String --%>
    <logic:equal name="attr" property="type" value="1">
        <html:text name="field" property="attrId${field.attrId}" value="${field.value}" size="40"/>
    </logic:equal>
    <%-- Attribute Type: Multi-line --%>
    <logic:equal name="attr" property="type" value="2">
        <html:textarea name="field" property="attrId${field.attrId}" value="${field.value}" cols="40" rows="8"/>
    </logic:equal>
    <%-- Attribute Type: Selectbox --%>
    <logic:equal name="attr" property="type" value="3">
        <html:select name="field" property="attrId${field.attrId}">
            <logic:iterate id="option" name="field" property="attrOptions">
                <html:option value="${option.value}">${option.label}</html:option>
            </logic:iterate>
        </html:select>
    </logic:equal>
    <%-- Attribute Type: Radio Button --%>
    <logic:equal name="attr" property="type" value="4">
        <logic:iterate id="option" name="field" property="attrOptions">
            <html:radio name="field" property="attrId${field.attrId}" idName="option" value="value"/>
            <bean:write name="option" property="label" filter="false"/>&nbsp;
        </logic:iterate>
    </logic:equal>
    <%-- Attribute Type: Date --%>
    <logic:equal name="attr" property="type" value="5">
        <script>
            $(function() {
                $( "#attrId${field.attrId}" ).datepicker(options);
            });
        </script>
        <input type="text" name="attrId${field.attrId}" id="attrId${field.attrId}" value="${field.value}" size="20"/>
            <span class="formFieldDesc"><bean:message key="common.example" arg0="${customFieldsTemplate.datePattern}"/></span>
    </logic:equal>
    <%-- Attribute Type: Currency --%>
    <logic:equal name="attr" property="type" value="7">
        <bean:write name="attr" property="typeCurrencySymbol"/><html:text name="field" property="attrId${field.attrId}" value="${field.value}" size="10"/>
    </logic:equal>
    <br><span class="formFieldDesc"><bean:write name="attr" property="description"/></span>
    </td>
    </tr>
</logic:iterate>
<logic:notEqual name="customFieldsTemplate" property="partialTable" value="true">
    </table>
</logic:notEqual>

</logic:iterate>