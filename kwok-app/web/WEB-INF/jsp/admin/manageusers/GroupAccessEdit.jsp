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

<jsp:include page="/WEB-INF/jsp/admin/manageusers/GroupDetailTemplate.jsp"/>

<jsp:include page="/WEB-INF/jsp/common/template/ActionError.jsp"/>

<jsp:include page="/WEB-INF/jsp/common/template/Tabs.jsp"/>

<script type="text/javascript">
var perms = new Array(${permissionList});
</script>

<table class="tabBody">
<tr><td class="header1 emptyHeader">&nbsp;</td></tr>

<tr><td>
<form action="${formAction}" method="post">
<table>
<tr>
    <td><b><bean:message key="form.button.selectAll"/></b></td>
    <td>
        <logic:iterate id="option" name="accessOptions">
            <input type="radio" name="selectall" value="${option.value}" onclick="selectAllAccessItems(this, perms)"/>
            <bean:write name="option" property="label" filter="false"/>
        </logic:iterate>
    </td>
</tr>

<logic:iterate id="row" name="accessList" indexId="index">
<tr>
    <td><bean:write name="row" property="accessText" filter="false"/></td>
    <td><%-- Radio buttons here--%>
        <logic:iterate id="subrow" name="accessOptions">
            <html:radio name="row" property="${row.accessName}" idName="subrow" value="value"/>
            <bean:write name="subrow" property="label" filter="false"/>&nbsp;
        </logic:iterate></td>
</tr>
</logic:iterate>
<tr>
    <td>&nbsp;</td>
    <td>
        <html:submit onclick="disableButton(this)"><bean:message key="form.button.save"/></html:submit>
        ${formCancelLink}
    </td>
</tr>
</table>
</form>

</td></tr>
</table>
