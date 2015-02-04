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

<div class="section">
<bean:message key="contactMgmt.numCompanies" arg0="${numCompanyRecords}"/>

<p><ul>
<logic:iterate id="link" name="linkList">
    <li>${link}</li>
</logic:iterate>
</ul>
</div>

<%-- Company search section --%>
<logic:notEmpty name="formCompanySearchAction">
<p>
<h3><bean:message key="contactMgmt.index.companySearch"/></h3>
<form action="${formCompanySearchAction}" method="post">
<html:hidden name="ContactSearchForm" property="cmd" value="search"/>
<table class="standard section">
    <tr>
        <th><bean:message key="common.column.company_name"/>:</th>
        <td width="100%"><html:text name="ContactSearchForm" property="companyName" size="40"/></td>
    </tr>
    <tr>
        <th><bean:message key="common.column.company_description"/>:</th>
        <td><html:text name="ContactSearchForm" property="description" size="40"/></td>
    </tr>
    <tr>
        <th><bean:message key="common.column.company_tag"/>:</th>
        <td><html:text name="ContactSearchForm" property="companyTag" size="40"/></td>
    </tr>
    <logic:notEmpty name="customFieldsOptions">
        <tr><th><bean:message key="common.template.customFields"/>:</th>
            <td><html:select name="ContactSearchForm" property="attrId">
                <html:options collection="customFieldsOptions" property="value" labelProperty="label"/>
            </html:select> <html:text name="ContactSearchForm" property="attrValue" size="40"/>
        </td></tr>
    </logic:notEmpty>
    <tr>
        <td colspan="2"><html:submit onclick="disableButton(this)">
        <bean:message key="form.button.search"/></html:submit></td>
    </tr>
</table>
</form>

<%-- Company tags section --%>
<p>
<h3><bean:message key="contactMgmt.index.companyRecentTags"/></h3>

<ul>
<logic:iterate id="tag" name="companyTagList">
    <html:link action="${companyListPath}${tag.tag_name}"><bean:write name="tag" property="tag_name"/></html:link>&nbsp;&nbsp;
</logic:iterate>&nbsp;
</ul>
</logic:notEmpty>
 
<%-- Contact search section --%>
<logic:notEmpty name="formContactSearchAction">
<p>
<h3><bean:message key="contactMgmt.index.contactSearch"/></h3>

<form action="${formContactSearchAction}" method="post">
<html:hidden name="ContactSearchForm" property="cmd" value="search"/>
<table class="standard section">
    <tr>
        <th><bean:message key="common.column.contact_first_name"/>:</th>
        <td><html:text name="ContactSearchForm" property="contactFirstName" size="40"/></td>
    </tr>
    <tr><th><bean:message key="common.column.contact_last_name"/>:</th>
        <td><html:text name="ContactSearchForm" property="contactLastName" size="40"/></td>
    </tr>
    <tr>
        <th><bean:message key="common.column.contact_title"/>:</th>
        <td><html:text name="ContactSearchForm" property="contactTitle" size="40"/></td>
    </tr>
    <tr>
        <th><bean:message key="common.column.contact_email"/>:</th>
        <td><html:text name="ContactSearchForm" property="contactEmail" size="40"/></td>
    </tr>
    <tr><td colspan="2"><html:submit onclick="disableButton(this)">
            <bean:message key="form.button.search"/></html:submit></td></tr>
</table>
</form>
</logic:notEmpty>
