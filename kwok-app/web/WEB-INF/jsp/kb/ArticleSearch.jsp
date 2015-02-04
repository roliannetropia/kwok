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

<%-- Show articles --%>
<logic:notEmpty name="articles">
    <div align="right" class="blogPermalink solidLine">&nbsp;</div>
    <logic:iterate id="article" name="articles">
        <h2 class="noLine"><a href="${article.articleDetailPath}" class="h2"><bean:write name="article" property="articleName"/></a></h2>
        <div class="infoContent section">
            <p><bean:message key="common.column.article_creator"/>: <bean:write name="article" property="articleCreator"/>
            <br><bean:message key="common.column.article_creation_date"/>: <bean:write name="article" property="articleCreationDate"/>
        </div>
        <p class="section"><bean:message key="common.column.article_text"/>: <bean:write name="article" property="articleText"/>

        <div align="right" class="blogPermalink solidLine">&nbsp;</div>
    </logic:iterate>
</logic:notEmpty>
<logic:empty name="articles">
    <bean:message key="kb.articleSearchResults.noArticles"/>
</logic:empty>

<%-- Showing navigation. --%>
<div align="center">
<jsp:include page="/WEB-INF/jsp/common/template/RecordsNavigationWidget.jsp"/>
</div>
