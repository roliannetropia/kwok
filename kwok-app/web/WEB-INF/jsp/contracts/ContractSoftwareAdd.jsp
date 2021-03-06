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

<jsp:include page="/WEB-INF/jsp/contracts/ContractSpecTemplate.jsp"/>

<jsp:include page="/WEB-INF/jsp/common/template/ActionError.jsp"/>

<form action="${formSearchAction}" method="post">
<input type="hidden" name="_resubmit" value="true">
<table class="listTable">
    <tr class="header1">
        <td colspan="2"><b><bean:message key="common.linking.linkSoftware"/></b></td>
    </tr>
    <tr>
        <td class="row2"><bean:message key="itMgmt.index.filterSoftware"/>:</td>
        <td class="row2"><bean:message key="common.column.software_id"/>&nbsp;<input type="text" name="formSoftwareId" value="<bean:write name="form" property="formSoftwareId"/>" size="40" autofocus>
            <html:submit property="search" onclick="disableButton(this)"><bean:message key="form.button.search"/></html:submit>
        </td>
    </tr>
    <tr>
        <td class="row2"><bean:message key="contracts.softwareAdd.selectSoftware"/>:</td>
        <td class="row2">
            <logic:notPresent name="softwareList">
                <bean:message key="${selectSoftwareMessage}"/>
            </logic:notPresent>
            <logic:present name="softwareList">
                <table class="noBorder">
                    <logic:iterate id="software" name="softwareList">
                        <tr><td><html:checkbox name="form" property="softwareId" value="${software.softwareId}"/>
                            <bean:write name="software" property="softwareName" filter="false"/></td></tr>
                    </logic:iterate>
                </table>
            </logic:present>
        </td>
    </tr>
    <tr>
        <td class="row2">&nbsp;</td>
        <td class="row2">
            <html:submit onclick="this.form.action='${formSaveAction}';disableButton(this);" disabled="${disableSaveButton}"><bean:message key="form.button.save"/></html:submit>
            ${formCancelLink}
        </td>
    </tr>
</table>
</form>
