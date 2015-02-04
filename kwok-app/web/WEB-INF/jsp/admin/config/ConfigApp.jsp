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

<table class="standard details">
    <tr>
        <th width="30%"><bean:message key="admin.config.buildNumber"/>:</th>
        <td width="70%"><bean:write name="buildNumber"/></td>
    </tr>
    <tr>
        <th><bean:message key="admin.config.system.licenseKey"/>:</th>
        <td><bean:write name="licenseKey"/> (<bean:write name="appEdition" filter="false"/>,
            <bean:write name="viewLicenseKeysLink" filter="false"/>)</td>
    </tr>
    <tr>
        <th><bean:message key="admin.config.applicationPath"/>:</th>
        <td><bean:write name="applicationUrl"/></td>
    </tr>
    <tr>
        <th><bean:message key="admin.config.locale"/>:</th>
        <td><bean:message key="admin.config.locale.${locale}"/></td>
    </tr>
    <tr>
        <th><bean:message key="admin.config.timezone"/>:</th>
        <td><bean:write name="timezoneLocal"/></td>
    </tr>
    <tr>
        <th><bean:message key="admin.config.db.serverTime"/>:</th>
        <td><bean:write name="serverTime"/></td>
    </tr>
    <tr>
        <th><bean:message key="admin.config.datetime.shortDateFormat"/>:</th>
        <td><bean:write name="shortDateFormat"/></td>
    </tr>
    <tr>
        <th><bean:message key="admin.config.datetime.timeFormat"/>:</th>
        <td><bean:write name="timeFormat"/></td>
    </tr>
    <tr>
        <th><bean:message key="admin.config.currency"/>:</th>
        <td><bean:write name="currencyOptions"/></td>
    </tr>
    <tr>
        <th><bean:message key="admin.config.numberOfPastYears"/>:</th>
        <td><bean:write name="numberOfPastYearsToShow"/></td>
    </tr>
    <tr>
        <th><bean:message key="admin.config.numberOfFutureYears"/>:</th>
        <td><bean:write name="numberOfFutureYearsToShow"/></td>
    </tr>
    <tr>
        <th><bean:message key="admin.config.users.displayOption"/>:</th>
        <td><bean:message key="common.column.${usernameDisplay}"/></td>
    </tr>
    <tr>
        <th><bean:message key="admin.config.users.numberOfRowsToShow"/>:</th>
        <td><bean:write name="usersRowsToShow"/></td>
    </tr>
    <tr>
        <th><bean:message key="admin.config.blogs.numberOfPostsToShow"/>:</th>
        <td><bean:write name="portal_numberOfBlogPostsToShowOnList"/></td>
    </tr>
    <tr>
        <th><bean:message key="admin.config.blogs.numberOfPostCharsToShow"/>:</th>
        <td><bean:write name="portal_numberOfBlogPostCharactersOnList"/></td>
    </tr>
    <tr>
        <th><bean:message key="admin.config.customFields.expand"/>:</th>
        <td><bean:write name="loadCustomFields"/></td>
    </tr>
    <%-- Hardware module --%>
    <tr><td colspan="2">
        <h3><bean:message key="core.moduleName.1"/></h3>
    </td></tr>
    <tr>
        <th><bean:message key="admin.config.hardwareColumns"/>:</th>
        <td><bean:write name="hardwareColumns"/></td>
    </tr>
    <tr>
        <th><bean:message key="admin.config.hardware.numberOfRowsToShow"/>:</th>
        <td><bean:write name="hardwareRowsToShow"/></td>
    </tr>
    <tr>
        <th><bean:message key="admin.config.hardware.expireCountdown"/>:</th>
        <td><bean:write name="hardwareExpirationCountdown"/></td>
    </tr>
    <tr>
        <th><bean:message key="admin.config.hardware.validateUniqueName"/>:</th>
        <td><bean:message key="common.boolean.true_false.${checkUniqueHardwareName}"/></td>
    </tr>
    <tr>
        <th><bean:message key="admin.config.hardware.validateSerialNumber"/>:</th>
        <td><bean:message key="common.boolean.true_false.${checkUniqueSerialNumber}"/></td>
    </tr>
    <%-- Software module --%>
    <tr><td colspan="2">
        <h3><bean:message key="core.moduleName.2"/></h3>
    </td></tr>
    <tr>
        <th><bean:message key="admin.config.softwareColumns"/>:</th>
        <td><bean:write name="softwareColumns"/></td>
    </tr>
    <tr>
        <th><bean:message key="admin.config.software.numberOfRowsToShow"/>:</th>
        <td><bean:write name="softwareRowsToShow"/></td>
    </tr>
    <tr>
        <th><bean:message key="admin.config.software.licenseNotesCharacters"/>:</th>
        <td><bean:write name="softwareLicneseNotesNumChars"/></td>
    </tr>
    <%-- Issues module --%>
    <tr><td colspan="2">
        <h3><bean:message key="core.moduleName.4"/></h3>
    </td></tr>
    <tr>
        <th><bean:message key="admin.config.issues.columns"/>:</th>
        <td><bean:write name="issuesColumns"/></td>
    </tr>
    <tr>
        <th><bean:message key="admin.config.issues.numberOfRowsToShow"/>:</th>
        <td><bean:write name="issuesRowsToShow"/></td>
    </tr>
    <tr>
        <th><bean:message key="admin.config.issues.guestSubmitModuleEnabled"/>:</th>
        <td><bean:message key="common.boolean.true_false.${issuesGuestSubmitModuleEnabled}"/></td>
    </tr>
    <tr>
        <th><bean:message key="admin.config.issues.guestSubmitEnabled"/>:</th>
        <td><bean:message key="common.boolean.true_false.${issuesGuestSubmitEnabled}"/></td>
    </tr>
    <tr>
        <th><bean:message key="admin.config.issues.multipleIssueDeleteEnabled"/>:</th>
        <td><bean:message key="common.boolean.true_false.${issuesMultipleDeleteEnabled}"/></td>
    </tr>
    <%-- Contacts module --%>
    <tr><td colspan="2">
        <h3><bean:message key="core.moduleName.5"/></h3>
    </td></tr>
    <tr>
        <th><bean:message key="admin.config.companies.numberOfRowsToShow"/>:</th>
        <td><bean:write name="companiesRowsToShow"/></td>
    </tr>
    <tr>
        <th><bean:message key="admin.config.contacts.numberOfRowsToShow"/>:</th>
        <td><bean:write name="contactsRowsToShow"/></td>
    </tr>
    <%-- Contracts module --%>
    <tr><td colspan="2">
        <h3><bean:message key="core.moduleName.3"/></h3>
    </td></tr>
    <tr>
        <th><bean:message key="admin.config.columnList"/>:</th>
        <td><bean:write name="contractsColumns"/></td>
    </tr>
    <tr>
        <th><bean:message key="admin.config.contracts.numberOfRowsToShow"/>:</th>
        <td><bean:write name="contractsRowsToShow"/></td>
    </tr>
    <tr>
        <th><bean:message key="admin.config.contracts.expireCountdown"/>:</th>
        <td><bean:write name="contractsExpirationCountdown"/></td>
    </tr>
    <%-- Knowledge Base module --%>
    <tr><td colspan="2">
        <h3><bean:message key="core.moduleName.14"/></h3>
    </td></tr>
    <tr>
        <th><bean:message key="admin.config.columnList"/>:</th>
        <td><bean:write name="kbColumns"/></td>
    </tr>
</table>
