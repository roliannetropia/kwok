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

<h2><bean:message key="admin.attributeEdit.header"/>: 
    <bean:message key="common.objectType.${attribute.objectTypeId}"/> &raquo; <bean:message key="common.column.${attribute.name}"/></h2>

<jsp:include page="/WEB-INF/jsp/common/template/ActionError.jsp"/>

<form action="${formAction}" method="post">
<html:hidden name="form" property="attributeId"/>
<html:hidden name="form" property="attrFieldId"/>
<table class="standardForm">
    <tr><td>
        <table>
        <tr>
            <th><bean:message key="common.requiredFieldIndicator.true"/><bean:message key="admin.attributeAdd.name"/>:</th>
            <td><input type="text" name="attributeFieldName" value="<bean:write name="form" property="attributeFieldName"/>" size="40" autofocus></td>
        </tr>
        <tr>
            <th><bean:message key="admin.attributeAdd.description"/>:</th>
            <td><html:textarea name="form" property="attributeFieldDescription" cols="40" rows="6"/></td>
        </tr>
        <tr>
            <th><bean:message key="admin.attributeAdd.status"/>:</th>
            <td>             
                <html:select name="form" property="disabled">
                    <html:options collection="statusList" property="value" labelProperty="label"/>
                </html:select>
            </td>
        </tr>
        <logic:notEmpty name="customAttrs">
        <tr>
            <th><bean:message key="admin.customAttrList"/>:</th>
            <td>
                <table border="0">
                <logic:iterate id="customAttrMap" name="customAttrs">
                    <bean:define id="customAttr" name="customAttrMap" property="attr" type="com.kwoksys.biz.admin.dto.Attribute"/>
                    <tr>
                        <td><bean:write name="customAttr" property="name"/></td>
                        <td>
                            <input type="checkbox" name="customAttrs" value="${customAttr.id}" ${customAttrMap.checked}>
                        </td>
                    </tr>
                </logic:iterate>
                </table>
            </td>
        </tr>
        </logic:notEmpty>
        <logic:notEmpty name="iconList">
            <tr>
                <th><bean:message key="admin.attributeAdd.icon"/>:</th>
                <td>
                    <html:radio name="form" property="iconId" value="0"/><bean:message key="admin.attributeAdd.noIcon"/>
                    <logic:iterate id="icon" name="iconList" type="com.kwoksys.biz.admin.dto.Icon">
                        <html:radio name="form" property="iconId" value="${icon.id}"/><img src="${icon.path}" width="16" height="16" alt="">&nbsp;&nbsp;
                    </logic:iterate>
                </td>
            </tr>
        </logic:notEmpty>
        <tr><td>&nbsp;</td>
            <td>
                <html:submit onclick="disableButton(this)">
                    <bean:message key="form.button.save"/>
                </html:submit>
                ${formCancelLink}
            </td>
        </tr>
        </table>
    </td></tr>
</table>
</form>
