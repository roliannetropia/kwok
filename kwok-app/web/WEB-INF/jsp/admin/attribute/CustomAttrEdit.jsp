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

<form action="${formAction}" method="post">
<html:hidden name="form" property="attrId"/>
<table class="standardForm">
    <tr>
        <th><bean:message key="common.requiredFieldIndicator.true"/><bean:message key="admin.attribute.attribute_name"/>:</th>
        <td><input type="text" name="attrName" value="<bean:write name="form" property="attrName"/>" size="48" maxlength="50" autofocus></td>    </tr>
    <tr>
        <th><bean:message key="admin.attribute.attribute_description"/>:</th>
        <td><html:text name="form" property="description" size="48" maxlength="255"/>
            <br><span class="formFieldDesc"><bean:message key="admin.attribute.attribute_description.desc"/></span></td>
    </tr>
    <logic:notEmpty name="attrGroupOptions">
         <tr>
            <th><bean:message key="admin.attribute.attribute_group"/>:</th>
            <td><html:select name="form" property="attrGroupId">
                    <html:options collection="attrGroupOptions" property="value" labelProperty="label"/>
                </html:select>
            </td>
        </tr>
    </logic:notEmpty>
    <tr>
        <th><bean:message key="admin.attribute.attribute_type"/>:</th>
        <td><html:select name="form" property="attrType" onchange="attrOptions(this.value)">
                <html:options collection="attrTypeOptions" property="value" labelProperty="label"/>
            </html:select>
        </td>
    </tr>
    <tr id="attrOptions">
        <th><bean:message key="admin.attribute.attribute_option"/>:</th>
        <td><html:textarea name="form" property="attrOption" cols="50" rows="6"/>
            <br><span class="formFieldDesc"><bean:message key="admin.attribute.attribute_option.desc"/></span></td>
    </tr>
    <tr id="attrCurrencySymbol">
        <th><bean:message key="admin.attribute.currency_symbol"/>:</th>
        <td><html:text name="form" property="currencySymbol" size="5"/></td>
    </tr>
    <logic:notEmpty name="systemFields">
    <tr>
        <th><bean:message key="admin.attributeList"/>:</th>
        <td>
            <table>
            <logic:iterate id="systemField" name="systemFields">
                <tr>
                    <td><bean:write name="systemField" property="fieldName"/></td>
                    <td>
                        <input type="checkbox" name="systemFields" value="${systemField.fieldId}" ${systemField.checked}>
                    </td>
                </tr>
            </logic:iterate>
            </table>
        </td>
    </tr>
    </logic:notEmpty>
    <tr>
        <td colspan="2"><h3><bean:message key="common.form.advancedOptions"/></h3></td>
    </tr>
    <tr>
        <th><bean:message key="admin.attribute.attribute_input_mask"/>:</th>
        <td><html:text name="form" property="inputMask" size="48" maxlength="50"/>
            <br><span class="formFieldDesc"><bean:message key="admin.attribute.attribute_input_mask.desc" arg0="${path.siteDocInputMask}"/></span></td>
    </tr>
    <tr id="convertUrl">
        <th><bean:message key="admin.attribute.attribute_convert_url"/>:</th>
        <td><logic:iterate id="option" name="attrConvertUrlOptions">
                <html:radio name="form" property="attrConvertUrl" value="${option.value}"/>
                <bean:write name="option" property="label"/>
            </logic:iterate>
        </td>
    </tr>
    <tr id="createUrl">
        <th><bean:message key="admin.attribute.attribute_url"/>:</th>
        <td><html:text name="form" property="attrUrl" size="80"/>
            <br><span class="formFieldDesc"><bean:message key="common.example" arg0="${attrUrlExample}"/></span></td>
    </tr>
    <tr><td>&nbsp;</td>
        <td><html:submit onclick="disableButton(this)">
            <bean:message key="form.button.save"/>
        </html:submit>
            ${formCancelLink}
        </td></tr>
</table>
</form>
