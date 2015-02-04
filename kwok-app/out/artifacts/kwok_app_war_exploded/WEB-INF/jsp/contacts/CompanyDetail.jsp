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

<jsp:include page="/WEB-INF/jsp/contacts/CompanySpecTemplate.jsp"/>

<jsp:include page="/WEB-INF/jsp/common/template/CustomFieldsTableToggle.jsp"/>

<jsp:include page="/WEB-INF/jsp/common/template/Tabs.jsp"/>

<table class="tabBody">
<tr><td class="header1 emptyHeader">&nbsp;</td></tr>
<logic:notEmpty name="mainContactList">
    <logic:iterate id="row" name="mainContactList">
        <tr class="solidLine"><td class="row2">
        <bean:define id="contact" name="row" property="contact" type="com.kwoksys.biz.contacts.dto.Contact"/>

    <table width="100%" cellspacing=5>
        <tr><td width="40%"><b><bean:write name="contact" property="title"/></b></td>
        <td width="50%">&nbsp;</td><td width="10%">&nbsp;</td>
        </tr>
        <logic:notEmpty name="row" property="contactNote"><tr><td><bean:write name="row" property="contactNote" filter="false"/></td></tr></logic:notEmpty>
        <tr>
        <td>
        <logic:notEmpty name="contact" property="addressStreetPrimary"><bean:write name="contact" property="addressStreetPrimary"/><br></logic:notEmpty>
        <logic:notEmpty name="contact" property="addressCityPrimary"><bean:write name="contact" property="addressCityPrimary"/>, </logic:notEmpty>
        <bean:write name="contact" property="addressStatePrimary"/> <bean:write name="contact" property="addressZipcodePrimary"/><br>
        <logic:notEmpty name="contact" property="addressCountryPrimary"><bean:write name="contact" property="addressCountryPrimary"/><br></logic:notEmpty>
        <logic:notEmpty name="row" property="contactPhoneWork">
            <bean:message key="contactMgmt.companyContact.contactPhoneWork" arg0="${row.contactPhoneWork}"/><br>
        </logic:notEmpty>
        <logic:notEmpty name="row" property="contactFax">
            <bean:message key="contactMgmt.companyContact.contactFax" arg0="${row.contactFax}"/>
        </logic:notEmpty>
        </td>
        <td valign="top">
        <logic:notEmpty name="row" property="contactEmailPrimary">
            <bean:message key="contactMgmt.companyContact.contactEmailPrimary" arg0="${row.contactEmailPrimary}"/><br>            
        </logic:notEmpty>
        <logic:notEmpty name="row" property="contactHomepageUrl">
            <bean:message key="contactMgmt.companyContact.contactHomepageURL" arg0="${row.contactHomepageUrl}"/>
        </logic:notEmpty>
        </td>
        <td valign="top">
        <logic:notEmpty name="row" property="contactEditPath">
             <a href="${row.contactEditPath}"><bean:message key="common.command.Edit"/></a>&nbsp;
        </logic:notEmpty>
        <logic:notEmpty name="row" property="contactDeletePath">
            <a href="${row.contactDeletePath}"/><bean:message key="common.command.Delete"/></a>
        </logic:notEmpty>
        &nbsp;</td>
        </tr>
    </table>

    </td></tr></logic:iterate>
</logic:notEmpty>
<%-- Show some message when there is no data --%>
<logic:empty name="mainContactList">
    <jsp:include page="/WEB-INF/jsp/common/template/TableEmpty.jsp"/>
</logic:empty>
</table>
