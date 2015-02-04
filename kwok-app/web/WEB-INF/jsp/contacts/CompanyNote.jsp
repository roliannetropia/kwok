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

<%-- Company Notes --%>
<jsp:include page="/WEB-INF/jsp/common/template/Tabs.jsp"/>
<table class="listTable">
    <tr class="header1">
        <th width="20%"><bean:message key="common.column.company_note_creation_date"/></th>
        <th width="70%"><bean:message key="common.column.company_note_description"/></th>
        <th><bean:message key="common.column.company_note_type"/></th>
    </tr>
    <logic:notEmpty name="notes">
        <logic:iterate id="note" name="notes">
            <tr>
                <td class="row2" valign="top"><bean:write name="note" property="creationDate"/>&nbsp;</td>
                <td class="row2" valign="top"><bean:write name="note" property="subject"/>
                    <div>
                        <bean:write name="note" property="description" filter="false"/>
                    </div>
                </td>
                <td class="row2" valign="top"><bean:write name="note" property="type"/>&nbsp;</td>
            </tr>
        </logic:iterate>
    </logic:notEmpty>
    <%-- Show some message when there is no data --%>
    <logic:empty name="notes">
        <jsp:include page="/WEB-INF/jsp/common/template/TableEmpty.jsp"/>
    </logic:empty>
</table>
