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

<table align="center" cellpadding="2" width="80%">
<tr>
    <td>
        <h2><bean:write name="postTitle"/></h2>
        <p><div class="infoContent"><bean:write name="postCreator"/></div>
        <p><bean:write name="postBody" filter="false"/>
        <p>&nbsp;<a name="comment"></a><div class="blogPostCommentHeader"><b><bean:write name="postCommentCount"/></b></div><p>

<%-- Comments for this post --%>
<logic:notEmpty name="postCommentList">
    <logic:iterate id="row" name="postCommentList">
        <div class="blogPostCommentBody"><img src="${image.blogComment}" alt=""> <bean:write name="row" property="commentCreator"/></div>
        <br><bean:write name="row" property="commentDescription" filter="false"/><br><br>
    </logic:iterate>
</logic:notEmpty>

<%-- Allow posting comment if the user has permission --%>
<logic:notEmpty name="postCommentPath">
    <form action="${postCommentPath}" method="post">
    <html:hidden name="form" property="postId"/>
        <jsp:include page="/WEB-INF/jsp/common/template/ActionError.jsp"/>

        <div class="blogPostCommentHeader"><b><bean:message key="blogs.postDetail.postComment"/></b></div>
        <html:textarea name="form" property="postComment" rows="14" cols="68"/>
        <br><html:submit onclick="disableButton(this)"><bean:message key="blogs.postDetail.postCommentButton"/></html:submit>
    </form>
</logic:notEmpty>

<%-- Show this if the post does not allow comment. --%>
<logic:equal name="postAllowComment" value="false">
    <div class="blogPostCommentHeader"><b><bean:message key="blogs.postDetail.postNotAllowComment"/></b></div>
</logic:equal>
</td></tr></table>
