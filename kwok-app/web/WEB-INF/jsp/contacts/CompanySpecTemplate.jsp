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

<bean:define id="companyTemplate" name="CompanySpecTemplate" type="com.kwoksys.action.contacts.CompanySpecTemplate"/>

<logic:notEmpty name="companyTemplate" property="companyHeaderText">
    <h2><bean:write name="companyTemplate" property="companyHeaderText"/></h2>
</logic:notEmpty>

<jsp:include page="/WEB-INF/jsp/common/template/DetailTable.jsp"/>

<table class="standard details" style="margin-top:-2px">
<logic:notEmpty name="TemplateCompanySpec_companyTagList">
    <tr><th width="15%"><bean:message key="common.column.company_tags"/>:</th>
    <td><logic:iterate id="tag" name="TemplateCompanySpec_companyTagList">
        <html:link action="${companyListPath}${tag.tagUrl}"><bean:write name="tag" property="tagName"/></html:link>&nbsp;
    </logic:iterate></td></tr>
</logic:notEmpty>
</table>
<p>