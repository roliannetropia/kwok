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

<jsp:include page="/WEB-INF/jsp/contacts/CompanySpecTemplate.jsp"/>

<%-- Company Bookmarks --%>
<jsp:include page="/WEB-INF/jsp/common/template/Tabs.jsp"/>
<table class="listTable">
    <tr class="header1">
        <logic:equal name="canDeleteBookmark" value="true">
            <td>&nbsp;</td>
        </logic:equal>
        <th align="left" width="70%"><bean:message key="common.column.bookmark_name"/></th>
        <logic:equal name="canEditBookmark" value="true">
            <th align="left" width="30%"><bean:message key="common.column.command"/></th>
        </logic:equal>
    </tr>

    <logic:notEmpty name="bookmarkList">
    <form action="${deleteBookmarkPath}" method="post">
        <html:hidden name="form" property="companyId"/>
        <logic:iterate id="row" name="bookmarkList">
           <tr class="${row.rowClass}">
            <logic:equal name="canDeleteBookmark" value="true">
                 <td>
                    <html:radio name="form" property="bookmarkId" value="${row.bookmarkId}"/>
                 </td>
            </logic:equal>
            <td width="100%"><bean:write name="row" property="bookmarkPath" filter="false"/></td>
            <logic:equal name="canEditBookmark" value="true">
                <td width="100%"><bean:write name="row" property="bookmarkEditPath" filter="false"/></td>
            </logic:equal>
            </tr>
        </logic:iterate>
        <logic:equal name="canDeleteBookmark" value="true">
            <tr class="header1">
                <td colspan="${bookmarkRowSpan}">
                    <input type="submit" value="<bean:message key="bookmarks.deleteBookmarkButton"/>" onclick="return confirmSubmit('<bean:message key="common.form.confirmDelete"/>')">                    
                </td>
            </tr>
        </logic:equal>
    </form>
    </logic:notEmpty>
    <%-- Show some message when there is no data --%>
    <logic:empty name="bookmarkList">
        <jsp:include page="/WEB-INF/jsp/common/template/TableEmpty.jsp"/>
    </logic:empty>
</table>
