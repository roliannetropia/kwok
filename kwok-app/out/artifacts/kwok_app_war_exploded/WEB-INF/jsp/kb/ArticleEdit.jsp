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
<html:hidden name="form" property="articleId"/>
<input type="hidden" name="_resubmit" value="true">
<table class="standard">
    <tr>
        <th><b><bean:message key="common.requiredFieldIndicator.true"/><bean:message key="common.column.article_name"/>:</b> </th>
        <td><input type="text" name="articleName" value="<bean:write name="form" property="articleName"/>" size="60" autofocus></td>
    </tr>
    <tr>
        <th><b><bean:message key="common.column.category_id"/>:</b> </th>
        <td><html:select name="form" property="categoryId">
            <html:options collection="categoryIdOptions" property="value" labelProperty="label"/>
            </html:select></td>
    </tr>
    <logic:notEmpty name="articleSyntaxOptions">
    <tr>
        <th><b><bean:message key="common.column.article_syntax_type"/>:</b></th>
        <td><html:select name="form" property="articleSyntax" onchange="changeAction(this, '${formThisAction}');">
            <html:options collection="articleSyntaxOptions" property="value" labelProperty="label"/>
            </html:select></td>
    </tr>
    </logic:notEmpty>
    <tr>
        <td colspan="2">
            <html:textarea name="form" property="articleText" cols="120" rows="25"/>
            <logic:notEqual name="isWikiSyntax" value="true">
                <script type="text/javascript">
                    CKEDITOR.replace( 'articleText' );
                </script>
            </logic:notEqual>
        </td>
    </tr>
    <tr>
        <td colspan="2">
            <html:submit onclick="disableButton(this)"><bean:message key="form.button.save"/></html:submit>
            ${formCancelLink}
        </td>
    </tr>
</table>
</form>
