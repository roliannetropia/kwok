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

<ul class="section">
<logic:iterate id="row" name="linkList">
    <li><html:link action="${row.urlPath}"><bean:write name="row" property="urlText"/></html:link></li>
</logic:iterate>
</ul>
<p>

<%-- Blog post search --%>
<h3><bean:message key="portal.index.blogPostSearch"/></h3>

<form action="${postListPath}" method="get">
<html:hidden name="PostSearchForm" property="cmd" value="search"/>
<table class="standard section">
    <tr>
        <th><bean:message key="common.column.post_name"/>:</th>
        <td width="100%"><html:text name="PostSearchForm" property="postTitle" size="40"/></td>
    </tr>
    <tr>
        <th><bean:message key="common.column.post_description"/>:</th>
        <td width="100%"><html:text name="PostSearchForm" property="postDescription" size="40"/></td>
    </tr>
    <tr>
        <th><bean:message key="common.column.category_id"/>: </th>
        <td><html:select name="PostSearchForm" property="categoryId">
            <html:options collection="postCategoryIdLabel" property="value" labelProperty="label"/>
            </html:select></td>
    </tr>
    <tr>
        <th><bean:message key="common.column.post_allow_comment"/>:</th>
        <td><html:select name="PostSearchForm" property="postAllowComment">
            <html:options collection="postAllowCommentOptions" property="value" labelProperty="label"/>
        </html:select></td>
    </tr>
    <tr>
        <td colspan="2"><html:submit onclick="disableButton(this)">
            <bean:message key="form.button.search"/></html:submit></td>
    </tr>
</table>
</form>

<%-- Post categories --%>
<p>
<h3><bean:message key="portal.index.blogPostCategory"/></h3>
<div class="section">
<logic:iterate id="category" name="postCategoryIdList">
    <html:link action="${postListPath}${category.postListParam}">
        <bean:write name="category" property="categoryName"/></html:link>
    (<bean:write name="category" property="postCount"/>)&nbsp;
</logic:iterate>
</div>
