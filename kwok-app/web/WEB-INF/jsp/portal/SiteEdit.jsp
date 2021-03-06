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
<input type="hidden" name="siteId" value="${form.siteId}">
<table class="standardForm">
    <tr>
        <th><bean:message key="common.column.site_id"/>:</th>
        <td><bean:write name="siteId"/></td>
    </tr>
    <tr>
        <th><bean:message key="common.requiredFieldIndicator.true"/><bean:message key="common.column.site_name"/>:</th>
        <td><input type="text" name="siteName" value="<bean:write name="form" property="siteName"/>" size="40" autofocus></td>
    </tr>
    <tr>
        <th><bean:message key="common.requiredFieldIndicator.true"/><bean:message key="common.column.site_path"/>:</th>
        <td><html:text name="form" property="sitePath" size="60"/>
            <br><span class="formFieldDesc"><bean:message key="contactMgmt.colExample.website"/></span></td>
    </tr>
    <tr>
        <th><bean:message key="common.column.site_description"/>:</th>
        <td><html:textarea name="form" property="siteDescription" rows="7" cols="50"/></td>
    </tr>
    <tr>
        <th><bean:message key="common.column.site_category"/>:</th>
        <td><html:select name="form" property="categoryId">
            <html:options collection="optionCategories" property="value" labelProperty="label"/>
        </html:select></td>
    </tr>
    <tr>
        <th><bean:message key="common.column.site_placement"/>:</th>
        <td><html:select name="form" property="sitePlacement">
            <html:options collection="optionSitePlacement" property="value" labelProperty="label"/>
        </html:select></td>
    </tr>
    <tr>
        <th><bean:message key="common.column.site_support_iframe"/>:</th>
        <td><html:select name="form" property="siteSupportIframe">
            <html:options collection="optionSiteSupportIframe" property="value" labelProperty="label"/>
        </html:select></td>
    </tr>
    <tr>
        <td>&nbsp;</td><td><html:submit onclick="disableButton(this)"><bean:message key="form.button.save"/></html:submit>
            ${formCancelLink}
        </td>
    </tr>
</table>
</form>
