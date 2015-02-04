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
<%@ taglib uri="http://struts.apache.org/tags-logic" prefix="logic" %>

<jsp:include page="/WEB-INF/jsp/admin/manageusers/GroupDetailTemplate.jsp"/>

<jsp:include page="/WEB-INF/jsp/common/template/Tabs.jsp"/>

<%-- Show a list of access --%>
<table class="tabBody">
<tr><td class="header1 emptyHeader" colspan="2">&nbsp;</td></tr>
<logic:iterate id="row" name="accessList" indexId="index">
<tr>
    <td nowrap="nowrap"><bean:write name="row" property="accessText" filter="false"/></td>
    <td width="100%"><bean:write name="row" property="accessValue" filter="false"/>&nbsp;</td>
</tr>
</logic:iterate>
</table>
