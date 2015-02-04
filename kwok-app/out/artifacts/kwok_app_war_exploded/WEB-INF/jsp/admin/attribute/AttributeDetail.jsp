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

<bean:define id="attribute" name="attribute" type="com.kwoksys.biz.admin.dto.Attribute"/>

<h2><bean:message key="admin.attributeDetail.header"/>:
    <bean:message key="common.objectType.${attribute.objectTypeId}"/> &raquo; <bean:message key="common.column.${attribute.name}"/></h2>

<table class="standard">
    <logic:equal name="attribute" property="requiredFieldEditable" value="true">
    <tr>
        <th><bean:message key="common.column.attribute_is_required"/>?</th>
        <td><bean:message key="${attributeIsRequired}"/></td>
    </tr>
    </logic:equal>
    <logic:equal name="attribute" property="defaultAttrFieldEditable" value="true">
    <tr>
        <th><bean:message key="common.column.attribute_default_field"/>:</th>
        <td>${defaultAttrField.name}</td>
    </tr>
    </logic:equal>
    <logic:equal name="canAddAttrField" value="true">
    <tr>
        <th><bean:message key="common.column.attribute_drop_down_list"/>:</th>
        <td>
            <bean:write name="attributeFieldAddPath" filter="false" ignore="true"/>

        <table class="standard">
        <logic:iterate id="row" name="attrFieldList">
        <tr>
            <th width="300"><bean:write name="row" property="attrFieldName" filter="false"/> </th>
            <th class="infoContent">
                <bean:write name="row" property="attrFieldDescription" filter="false"/>&nbsp;</th>
            <td style="text-align:left; white-space:nowrap;">
                <bean:write name="row" property="attributeEditPath" filter="false"/>
                
                <logic:equal name="row" property="attrFieldDefault" value="true">
                    &nbsp;<bean:message key="admin.attributeDetail.defaultField"/>
                </logic:equal>
            </td>
        </tr>
        </logic:iterate>
        <logic:iterate id="row" name="attrFieldDisabledList">
            <tr>
                <th class="inactive" width="30%"><bean:write name="row" property="attrFieldName" filter="false"/> </th>
                <th class="infoContent">
                    <bean:write name="row" property="attrFieldDescription"/>&nbsp;</th>
                <td>
                    <bean:write name="row" property="attributeEditPath" filter="false"/>
                    
                    <logic:equal name="row" property="attrFieldDefault" value="true">
                        &nbsp;<bean:message key="admin.attributeDetail.defaultField"/>
                    </logic:equal>
                </td>
            </tr>
        </logic:iterate>
        </table>
    </td></tr>
    </logic:equal>
</table>
