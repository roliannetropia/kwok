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

<jsp:include page="/WEB-INF/jsp/software/SoftwareSpecTemplate.jsp"/>

<jsp:include page="/WEB-INF/jsp/common/template/ActionError.jsp"/>

<form action="${formAction}" method="post">
<table class="listTable">
    <tr class="header1">
        <td colspan="2"><b><bean:message key="bookmarkMgmt.bookmarkEdit"/></b></td>
    </tr>
    <tr>
        <td class="row2"><bean:message key="common.column.bookmark_name"/>:</td>
        <td class="row2"><html:text name="form" property="bookmarkName" size="40"/>&nbsp;<bean:message key="common.requiredFieldIndicator.true"/></td>
    </tr>
    <tr>
        <td class="row2"><bean:message key="common.column.bookmark_path"/>:</td>
        <td class="row2"><html:text name="form" property="bookmarkPath" size="60"/>&nbsp;<bean:message key="common.requiredFieldIndicator.true"/>
    </tr>
    <tr>
        <td class="row2">&nbsp;</td>
        <td class="row2"><html:submit onclick="disableButton(this)"><bean:message key="form.button.save"/></html:submit>
            ${formCancelLink}
        </td>
    </tr>
</table>
</form>
