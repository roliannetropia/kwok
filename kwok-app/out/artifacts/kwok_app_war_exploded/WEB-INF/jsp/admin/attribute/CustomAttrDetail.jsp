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
<%@ taglib uri="http://struts.apache.org/tags-logic" prefix="logic" %>

<bean:define id="attr" name="attr" type="com.kwoksys.biz.admin.dto.Attribute"/>

<table class="standard">
    <tr>
        <th width="20%"><bean:message key="admin.attribute.attribute_name"/>:</th>
        <td><bean:write name="attr" property="name"/></td>
    </tr>
    <tr>
        <th width="20%"><bean:message key="admin.attribute.attribute_description"/>:</th>
        <td><bean:write name="attr" property="description"/></td>
    </tr>
    <logic:notEmpty name="attrGroup">
    <tr>
        <th><bean:message key="admin.attribute.attribute_group"/>:</th>
        <td><bean:write name="attrGroup" property="name"/></td>
    </tr>
    </logic:notEmpty>
    <tr>
        <th><bean:message key="admin.attribute.attribute_type"/>:</th>
        <td><bean:message key="admin.attribute.attribute_type.${attr.type}"/></td>
    </tr>
    <tr>
        <th><bean:message key="admin.attribute.attribute_option"/>:</th>
        <td><bean:write name="attrOption"/></td>
    </tr>
    <tr>
        <th><bean:message key="admin.attribute.currency_symbol"/>:</th>
        <td><bean:write name="attr" property="typeCurrencySymbol"/></td>
    </tr>
    <logic:present name="systemFields">
        <tr>
            <th><bean:message key="admin.attributeList"/>:</th>
            <td>
                <ul>
                <logic:iterate id="attrLink" name="systemFields">
                    <li><bean:write name="attrLink" filter="false"/>
                </logic:iterate>
                </ul>
            </td>
        </tr>
    </logic:present>
    <tr>
        <td colspan="2"><h3><bean:message key="common.form.advancedOptions"/></h3></td>
    </tr>
    <tr>
        <th><bean:message key="admin.attribute.attribute_input_mask"/>:</th>
        <td><bean:write name="attr" property="inputMask"/></td>
    </tr>
    <tr>
        <th><bean:message key="admin.attribute.attribute_convert_url"/>:</th>
        <td><bean:message key="${attrConvertUrl}"/></td>
    </tr>
    <tr>
        <th><bean:message key="admin.attribute.attribute_url"/>:</th>
        <td><bean:write name="attr" property="url"/></td>
    </tr>
</table>
