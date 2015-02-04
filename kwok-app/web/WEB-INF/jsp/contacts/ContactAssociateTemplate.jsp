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

<bean:define id="contactAssociateTemplate" name="ContactAssociateTemplate" type="com.kwoksys.action.contacts.ContactAssociateTemplate"/>

<jsp:include page="/WEB-INF/jsp/common/template/ActionError.jsp"/>

<form action="${contactAssociateTemplate.formSearchAction}" method="post">
<input type="hidden" name="_resubmit" value="true">
<table class="listTable">
    <tr class="header1">
        <td colspan="2"><b><bean:message key="common.linking.linkContacts"/></b></td>
    </tr>
    <tr>
        <td class="row2"><bean:message key="contactMgmt.index.contactSearch"/>:</td>
        <td class="row2"><bean:message key="common.column.contact_id"/>&nbsp;<input type="text" name="formContactId" value="<bean:write name="form" property="formContactId"/>" size="40" autofocus>
            <html:submit property="search" onclick="disableButton(this)"><bean:message key="form.button.search"/></html:submit>
        </td>
    </tr>
    <tr>
        <td class="row2"><bean:message key="common.linking.selectContact"/>:</td>
        <td class="row2">
            <logic:notPresent name="contactList">
                <bean:message key="${selectContactMessage}"/>
            </logic:notPresent>
            <logic:present name="contactList">
                <table class="noBorder">
                    <logic:iterate id="contact" name="contactList">
                        <tr>
                            <td><html:checkbox name="contactAssociateTemplate" property="contactId" value="${contact.contactId}"/>
                                <bean:write name="contact" property="contactName" filter="false"/></td>
                            <td>
                        </tr>
                        <tr>
                            <td>
                                <bean:message key="common.column.relationship_description"/>:
                                <html:text name="form" property="relationshipDescription" size="50" maxlength="50"/>
                            </td>
                        </tr>
                    </logic:iterate>
                </table>
            </logic:present>
        </td>
    </tr>
    <tr>
        <td class="row2">&nbsp;</td>
        <td class="row2">
            <html:submit onclick="this.form.action='${contactAssociateTemplate.formSaveAction}';disableButton(this)" disabled="${disableSaveButton}"><bean:message key="form.button.save"/></html:submit>
            <html:link action="${contactAssociateTemplate.formCancelAction}" styleClass="cancel"><bean:message key="form.button.cancel"/></html:link>
        </td>
    </tr>
</table>
</form>
