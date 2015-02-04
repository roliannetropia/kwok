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

<p class="section"><bean:write name="rowCountText" filter="false"/>

<%-- Showing posts --%>
<logic:notEmpty name="postList">
    <div align="right" class="blogPermalink solidLine">&nbsp;</div>
    <logic:iterate id="post" name="postList">
        <h2 class="noLine"><a href="${post.postPermalinkPath}" class="h2"><bean:write name="post" property="postTitle"/></a></h2>
        <p><div class="infoContent section"><bean:write name="post" property="postCreator"/></div>
        <p class="section"><bean:write name="post" property="postBody" filter="false"/>
        <div align="right" class="blogPermalink solidLine">&raquo;
            <bean:write name="post" property="postCategoryPath" filter="false"/> |
            <bean:write name="post" property="postCommentPath" filter="false"/> |
            <a href="${post.postPermalinkPath}"><bean:message key="blogs.postList.permalink"/></a></div>
    </logic:iterate>
</logic:notEmpty>

<%-- Showing Blogs navigation. --%>
<div align="center">
<jsp:include page="/WEB-INF/jsp/common/template/RecordsNavigationWidget.jsp"/>
</div>
