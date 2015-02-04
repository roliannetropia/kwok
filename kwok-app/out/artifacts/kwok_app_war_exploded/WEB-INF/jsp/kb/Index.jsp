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

<%-- KB article search --%>
<div class="section">
<form action="${articleSearchPath}" method="post">
    <html:hidden name="ArticleSearchForm" property="cmd" value="search"/>
        <bean:message key="common.column.article_text"/>:
        <html:text name="ArticleSearchForm" property="articleText" size="40"/>
        <html:submit onclick="disableButton(this)"><bean:message key="form.button.search"/></html:submit>
</form>
</div>

<%-- KB categories --%>
<h3><bean:message key="kb.index.categoryList"/></h3>
<ul class="section">
<logic:iterate id="category" name="categories">
    <li>${category}</li>
</logic:iterate>
</ul>
