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

<jsp:include page="/WEB-INF/jsp/common/template/HeaderTemplate.jsp"/>

<p><bean:message key="webCalendar.year.header"/> </p>
<div align="center">
<a href="${prevPath}"><bean:message key="webCalendar.yearPrev"/></a> &nbsp;
<a href="${nextPath}"><bean:message key="webCalendar.yearNext"/></a>
</div>
<p>
<table class="standard"><tr>
<logic:iterate id="month" property="calendarList">
    <logic:equal name="month" property="monthCounter" value="1"></tr><tr></logic:equal>
    <td align="center" valign="top">
        <bean:write name="month" property="name"/>
        <table>
            <tr>
                <logic:iterate id="weekday" property="weekdayList">
                    <td><bean:write name="weekday"/></td>
                </logic:iterate>
            </tr>
            <tr>
                <logic:iterate id="day" name="month" property="dayList"><logic:equal name="day" property="dayCounter" value="1"></tr><tr></logic:equal><td><bean:write name="day" property="date" filter="false"/></td></logic:iterate>
            </tr>
        </table><br>
    </td>
</logic:iterate>
</tr></table>
<p>

<jsp:include page="/WEB-INF/jsp/common/template/FooterTemplate.jsp"/>