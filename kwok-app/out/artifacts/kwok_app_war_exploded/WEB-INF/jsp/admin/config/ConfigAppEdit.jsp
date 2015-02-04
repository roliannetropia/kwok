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

<jsp:include page="/WEB-INF/jsp/common/template/ActionError.jsp"/>

<form action="${formAction}" method="post">
<input type="hidden" name="_resubmit" value="true">
<input type="hidden" name="cmd" value="${cmd}"/>
<table class="standardForm">
    <tr>
        <th width="40%"><bean:message key="admin.config.system.licenseKey"/>:</th>
        <td><html:text name="form" property="licenseKey" size="60"/></td>
    </tr>
    <tr>
        <th width="40%"><bean:message key="admin.config.applicationPath"/>:</th>
        <td><html:text name="form" property="applicationUrl" size="60"/>
            <br><span class="formFieldDesc"><bean:message key="admin.configDesc.url.application"/></span></td>
    </tr>
    <tr>
        <th><bean:message key="admin.config.locale"/>:</th>
        <td><html:select name="form" property="locale">
            <html:options collection="localeOptions" property="value" labelProperty="label"/>
            </html:select></td>
    </tr>
    <tr>
        <th><bean:message key="admin.config.timezone"/>:</th>
        <td><html:select name="form" property="timezone">
            <html:options collection="timezoneOptions" property="value" labelProperty="label"/>
            </html:select></td>
    </tr>
    <tr>
        <th><bean:message key="admin.config.datetime.shortDateFormat"/>:</th>
        <td><html:select name="form" property="shortDateFormat">
            <html:options collection="shortDateFormatOptions" property="value" labelProperty="label"/>
            </html:select></td>
    </tr>
    <tr>
        <th><bean:message key="admin.config.datetime.timeFormat"/>:</th>
        <td><html:select name="form" property="timeFormat">
            <html:options collection="timeFormatOptions" property="value" labelProperty="label"/>
            </html:select></td>
    </tr>
    <tr>
        <th><bean:message key="admin.config.currency"/>:</th>
        <td><html:text name="form" property="currency" size="8"/></td>
    </tr>
    <tr>
        <th><bean:message key="admin.config.numberOfPastYears"/>:</th>
        <td><html:text name="form" property="numberOfPastYears" size="8"/></td>
    </tr>
    <tr>
        <th><bean:message key="admin.config.numberOfFutureYears"/>:</th>
        <td><html:text name="form" property="numberOfFutureYears" size="8"/></td>
    </tr>
    <tr>
        <th><bean:message key="admin.config.users.displayOption"/>:</th>
        <td><html:select name="form" property="userNameDisplay">
            <html:options collection="userNameDisplayOptions" property="value" labelProperty="label"/>
            </html:select></td>
    </tr>
    <tr>
        <th><bean:message key="admin.config.users.numberOfRowsToShow"/>:</th>
        <td><html:select name="form" property="usersNumRows">
            <html:options collection="numrowOptions" property="value" labelProperty="label"/>
            </html:select></td>
    </tr>
    <tr>
        <th><bean:message key="admin.config.blogs.numberOfPostsToShow"/>:</th>
        <td><html:select name="form" property="blogPostsListNumRows">
            <html:options collection="numBlogPostsOptions" property="value" labelProperty="label"/>
            </html:select></td>
    </tr>
    <tr>
        <th><bean:message key="admin.config.blogs.numberOfPostCharsToShow"/>:</th>
        <td><html:select name="form" property="blogPostCharactersList">
            <html:options collection="numBlogPostCharactersOptions" property="value" labelProperty="label"/>
            </html:select>
            <br><span class="formFieldDesc"><bean:message key="admin.configDesc.portal.numberOfBlogPostCharacters"/></span>
            </td>
    </tr>
    <tr>
        <th><bean:message key="admin.config.customFields.expand"/>:</th>
        <td><html:select name="form" property="loadCustomFields">
            <html:options collection="booleanOptions" property="value" labelProperty="label"/>
            </html:select>
            <br><span class="formFieldDesc"><bean:message key="admin.config.customFields.expand.description"/></span>
        </td>
    </tr>
    <%-- Hardware module --%>
    <tr><td colspan="2">
        <br><h3><bean:message key="core.moduleName.1"/></h3>
    </td></tr>
    <tr>
        <th><bean:message key="admin.config.hardwareColumns"/>:</th>
        <td><logic:iterate id="column" name="hardwareColumnOptions">
            <logic:present name="column" property="disabled">
                <input type="hidden" name="hardwareColumns" value="${column.name}">
            </logic:present>

            <input type="checkbox" name="hardwareColumns" value="${column.name}" ${column.checked} ${column.disabled}/>
            <bean:message key="common.column.${column.name}"/>&nbsp;
            </logic:iterate></td>
    </tr>
    <tr>
        <th><bean:message key="admin.config.hardware.numberOfRowsToShow"/>:</th>
        <td><html:select name="form" property="hardwareNumRows">
            <html:options collection="numrowOptions" property="value" labelProperty="label"/>
            </html:select></td>
    </tr>
    <tr>
        <th><bean:message key="admin.config.hardware.expireCountdown"/>:</th>
        <td><html:select name="form" property="hardwareExpireCountdown">
            <html:options collection="expireCountdownOptions" property="value" labelProperty="label"/>
            </html:select>
            <br><span class="formFieldDesc"><bean:message key="admin.config.contracts.expireCountdown.desc"/></span></td>
    </tr>
    <tr>
        <th><bean:message key="admin.config.hardware.validateUniqueName"/>:</th>
        <td><html:checkbox name="form" property="checkUniqueHardwareName" value="true"/></td>
    </tr>
    <tr>
        <th><bean:message key="admin.config.hardware.validateSerialNumber"/>:</th>
        <td><html:checkbox name="form" property="checkUniqueSerialNumber" value="true"/>
            <br><span class="formFieldDesc"><bean:message key="admin.config.hardware.validateSerialNumber.description"/></span></td>
    </tr>
    <%-- Software module --%>
    <tr><td colspan="2">
        <br><h3><bean:message key="core.moduleName.2"/></h3>
    </td></tr>
    <tr>
        <th><bean:message key="admin.config.softwareColumns"/>:</th>
        <td><logic:iterate id="column" name="softwareColumnOptions">
            <logic:present name="column" property="disabled">
                <input type="hidden" name="softwareColumns" value="${column.name}">
            </logic:present>

            <input type="checkbox" name="softwareColumns" value="${column.name}" ${column.checked} ${column.disabled}/>
            <bean:message key="common.column.${column.name}"/>&nbsp;
            </logic:iterate></td>
    </tr>
    <tr>
        <th><bean:message key="admin.config.software.numberOfRowsToShow"/>:</th>
        <td><html:select name="form" property="softwareNumRows">
            <html:options collection="numrowOptions" property="value" labelProperty="label"/>
            </html:select></td>
    </tr>
    <tr>
        <th><bean:message key="admin.config.software.licenseNotesCharacters"/>:</th>
        <td><html:text name="form" property="softwareLicneseNotesNumChars" size="4"/>
            <br><span class="formFieldDesc"><bean:message key="admin.config.software.licenseNotesCharacters.desc"/></span>
        </td>
    </tr>    
    <%-- Issues module --%>
    <tr><td colspan="2">
        <br><h3><bean:message key="core.moduleName.4"/></h3>
    </td></tr>
    <tr>
        <th><bean:message key="admin.config.issues.columns"/>:</th>
        <td><logic:iterate id="column" name="issuesColumnOptions">
            <logic:present name="column" property="disabled">
                <input type="hidden" name="issuesColumns" value="${column.name}">
            </logic:present>

            <input type="checkbox" name="issuesColumns" value="${column.name}" ${column.checked} ${column.disabled}/>
            <bean:message key="issueMgmt.colName.${column.name}"/>&nbsp;
            </logic:iterate></td>
    </tr>
    <tr>
        <th><bean:message key="admin.config.issues.numberOfRowsToShow"/>:</th>
        <td><html:select name="form" property="issuesNumRows">
            <html:options collection="numrowOptions" property="value" labelProperty="label"/>
            </html:select></td>
    </tr>
    <tr>
        <th><bean:message key="admin.config.issues.guestSubmitModuleEnabled"/>:</th>
        <td><html:select name="form" property="issuesGuestSubmitModuleEnabled">
            <html:options collection="booleanOptions" property="value" labelProperty="label"/>
            </html:select></td>
    </tr>
    <tr>
        <th><bean:message key="admin.config.issues.guestSubmitEnabled"/>:</th>
        <td><html:select name="form" property="issuesGuestSubmitEnabled">
            <html:options collection="booleanOptions" property="value" labelProperty="label"/>
            </html:select></td>
    </tr>
    <tr>
        <th><bean:message key="admin.config.issues.multipleIssueDeleteEnabled"/>:</th>
        <td><html:select name="form" property="issuesMultipleDeleteEnabled">
            <html:options collection="booleanOptions" property="value" labelProperty="label"/>
            </html:select></td>
    </tr>
    <%-- Contacts module --%>
    <tr><td colspan="2">
        <br><h3><bean:message key="core.moduleName.5"/></h3>
    </td></tr>
    <tr>
        <th><bean:message key="admin.config.companies.numberOfRowsToShow"/>:</th>
        <td><html:select name="form" property="companiesNumRows">
            <html:options collection="numrowOptions" property="value" labelProperty="label"/>
            </html:select></td>
    </tr>
    <tr>
        <th><bean:message key="admin.config.contacts.numberOfRowsToShow"/>:</th>
        <td><html:select name="form" property="contactsNumRows">
            <html:options collection="numrowOptions" property="value" labelProperty="label"/>
            </html:select></td>
    </tr>    
    <%-- Contracts module --%>
    <tr><td colspan="2">
        <br><h3><bean:message key="core.moduleName.3"/></h3>
    </td></tr>
    <tr>
        <th><bean:message key="admin.config.columnList"/>:</th>
        <td><logic:iterate id="column" name="contractsColumnOptions">
            <logic:present name="column" property="disabled">
                <input type="hidden" name="contractColumns" value="${column.name}">
            </logic:present>

            <input type="checkbox" name="contractColumns" value="${column.name}" ${column.checked} ${column.disabled}/>
            <bean:message key="common.column.${column.name}"/>&nbsp;
            </logic:iterate></td>
    </tr>
    <tr>
        <th><bean:message key="admin.config.contracts.numberOfRowsToShow"/>:</th>
        <td><html:select name="form" property="contractsNumRows">
            <html:options collection="numrowOptions" property="value" labelProperty="label"/>
            </html:select></td>
    </tr>
    <tr>
        <th><bean:message key="admin.config.contracts.expireCountdown"/>:</th>
        <td><html:select name="form" property="contractsExpireCountdown">
            <html:options collection="expireCountdownOptions" property="value" labelProperty="label"/>
            </html:select>
            <br><span class="formFieldDesc"><bean:message key="admin.config.contracts.expireCountdown.desc"/></span></td>
    </tr>
    <%-- KB module --%>
    <tr><td colspan="2">
        <br><h3><bean:message key="core.moduleName.14"/></h3>
    </td></tr>
    <tr>
        <th><bean:message key="admin.config.columnList"/>:</th>
        <td><logic:iterate id="column" name="kbColumnOptions">
            <logic:present name="column" property="disabled">
                <input type="hidden" name="kbColumns" value="${column.name}">
            </logic:present>

            <input type="checkbox" name="kbColumns" value="${column.name}" ${column.checked} ${column.disabled}/>
            <bean:message key="common.column.${column.name}"/>&nbsp;
            </logic:iterate></td>
    </tr>
    <tr>
        <td>&nbsp;</td>
        <td><html:submit onclick="disableButton(this)">
            <bean:message key="form.button.save"/>
        </html:submit> 
            ${formCancelLink}
        </td>
    </tr>
</table>
</form>
