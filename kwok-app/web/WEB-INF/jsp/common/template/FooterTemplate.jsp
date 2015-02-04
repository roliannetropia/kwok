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

<bean:define id="footerTemplate" name="FooterTemplate" type="com.kwoksys.action.common.template.FooterTemplate"/>
<bean:define id="standardTemplate" name="StandardTemplate" type="com.kwoksys.action.common.template.StandardTemplate"/>

<logic:equal name="standardTemplate" property="ajax" value="false">
</td>
</logic:equal>

<logic:equal name="standardTemplate" property="adEnabled" value="true">
<td id="sponsorAd">
    <jsp:include page="/WEB-INF/jsp/common/template/SponsorAd.jsp"/>
</td>
</logic:equal>

<logic:equal name="standardTemplate" property="ajax" value="false">
</tr></table>
</div>

<div id="footer" class="themeBgImg">
    <logic:notEmpty name="_pageExecutionTime">
        <bean:message key="admin.config.timezone"/>: <bean:write name="footerTemplate" property="timezone"/> |
        <span id="footer_pageExecutionTime">&nbsp;</span><br>
    </logic:notEmpty>

    <a href="${path.site}" target="_blank"><bean:message key="common.app.name"/>&nbsp;<bean:write name="standardTemplate" property="appVersion"/></a>,

    <bean:message key="${footerTemplate.edition}"/>
    <logic:notEmpty name="footerTemplate" property="reportIssuePath">
        | ${footerTemplate.reportIssuePath}
    </logic:notEmpty>
    <br><bean:write name="footerTemplate" property="copyrightNotice" filter="false"/>
</div>
</div>
</logic:equal>

<script type="text/javascript">
    <logic:notEmpty name="footerTemplate" property="onloadJavascript">
        <bean:write name="footerTemplate" property="onloadJavascript" filter="false"/>
    </logic:notEmpty>
    var footer_pageExecutionTime = document.getElementById('footer_pageExecutionTime');
    if (footer_pageExecutionTime != undefined) {
        footer_pageExecutionTime.innerHTML = '${_pageExecutionTime}';
    }
</script>

<logic:equal name="standardTemplate" property="ajax" value="false">
<%--<table>--%>
<%--<%--%>
    <%--Enumeration<String> attrEnum = request.getAttributeNames();--%>
    <%--while (attrEnum.hasMoreElements()) {--%>
        <%--String attrName = attrEnum.nextElement();--%>
        <%--out.println("<tr><td>" + attrName + "</td><td>" + request.getAttribute(attrName) + "</td></tr>");--%>
    <%--}--%>
<%--%>--%>
<%--</table>--%>

<%--<table>--%>
<%--<%--%>
    <%--Enumeration<String> attrEnum = request.getSession().getAttributeNames();--%>
    <%--while (attrEnum.hasMoreElements()) {--%>
        <%--String attrName = attrEnum.nextElement();--%>
        <%--out.println("<tr><td>" + attrName + "</td><td>" + request.getSession().getAttribute(attrName) + "</td></tr>");--%>
    <%--}--%>
<%--%>--%>
<%--</table>--%>

</body>
</html>
</logic:equal>