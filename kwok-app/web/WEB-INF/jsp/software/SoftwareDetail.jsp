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

<%-- Some ajax script here. --%>
<div id="hardwareDetail">&nbsp;</div>
<script type="text/javascript">
function hardwarePopup(obj, hardwareId) {
    cmenu.divPos = 'top';
    cmenu.popupDiv = 'hardwareDetail';
    cmenu.url = '${ajaxHardwareDetailPath}';
    cmenu.dropit(obj, hardwareId);
}
</script>

<jsp:include page="/WEB-INF/jsp/common/template/CustomFieldsTableToggle.jsp"/>

<jsp:include page="/WEB-INF/jsp/common/template/Tabs.jsp"/>

<form action="${formAction}" method="post">
<table class="listTable">
    <tr class="header1">
        <logic:equal name="canDeleteLicense" value="true"><th class="rownum">&nbsp;</th></logic:equal>
        <th width="35%"><bean:message key="common.column.license_key"/></th>
        <th><bean:message key="common.column.license_note"/></th>
        <th><bean:message key="common.column.license_entitlement"/></th>
        <th><bean:message key="common.column.command"/></th>
        <th><bean:message key="common.column.installed_on_hardware"/></th>
    </tr>
    <%-- Show input fields for adding a new license. --%>
    <logic:equal name="addLicense" value="true">
    <html:hidden name="form" property="softwareId"/>
    <html:hidden name="form" property="cmd"/>
    <tr class="row2">
        <logic:equal name="canDeleteLicense" value="true"><td>&nbsp;</td></logic:equal>
        <td><input type="text" name="licenseKey" value="<bean:write name="form" property="licenseKey"/>" size="60" autofocus>&nbsp;<bean:message key="common.requiredFieldIndicator.true"/></td>
        <td><html:textarea name="form" property="licenseNote" rows="2" cols="40"/>&nbsp;</td>
        <td><html:text name="form" property="licenseEntitlement" size="10"/>&nbsp;<bean:message key="common.requiredFieldIndicator.true"/></td>
        <td nowrap><html:submit onclick="disableButton(this)"><bean:message key="form.button.save"/></html:submit>
            ${formCancelLink}
        </td>
        <td>&nbsp;</td>
    </tr>
    </logic:equal>
    <%-- The UI is confusing because we have add/edit/remove all on the same page.
    Show licenses associated with this Software. --%>
    <logic:notEmpty name="softwareLicenseList">
        <html:hidden name="form" property="softwareId" value="${softwareId}"/>
        <html:hidden name="form" property="cmd" value="${cmd}"/>
        <logic:iterate id="row" name="softwareLicenseList">
            <tr class="${row.rowClass}">
            <logic:equal name="row" property="licenseId" value="${formLicenseId}">
                <html:hidden name="form" property="licenseId" value="${formLicenseId}"/>
                <logic:equal name="canDeleteLicense" value="true">
                    <td rowspan="${row.rowSpan}">&nbsp;</td>
                </logic:equal>
                <td rowspan="${row.rowSpan}"><html:text name="form" property="licenseKey" size="60"/>&nbsp;<bean:message key="common.requiredFieldIndicator.true"/></td>
                <td rowspan="${row.rowSpan}"><html:textarea name="form" property="licenseNote" rows="2" cols="40"/></td>
                <td rowspan="${row.rowSpan}"><html:text name="form" property="licenseEntitlement" size="10"/>&nbsp;<bean:message key="common.requiredFieldIndicator.true"/></td>
                <td rowspan="${row.rowSpan}"><html:submit onclick="disableButton(this)"><bean:message key="form.button.save"/></html:submit>
                    ${formCancelLink}</td>
            </logic:equal>
            <logic:notEqual name="row" property="licenseId" value="${formLicenseId}">
                <logic:present name="row" property="showCol">
                    <logic:equal name="canDeleteLicense" value="true">
                        <td rowspan="${row.rowSpan}">
                            <html:radio name="form" property="licenseId" value="${row.licenseId}"/>
                        </td>
                    </logic:equal>
                    <td rowspan="${row.rowSpan}"><bean:write name="row" property="licenseKey" filter="false"/><div id="cf${row.licenseId}" class="customFieldEmbedded" style="display:none;"></div></td>
                    <td rowspan="${row.rowSpan}"><bean:write name="row" property="licenseNote" filter="false"/></td>
                    <td rowspan="${row.rowSpan}"><bean:write name="row" property="licenseEntitlement"/></td>
                    <%-- Show the edit link only if the user has permission. --%>
                    <td rowspan="${row.rowSpan}">
                    <logic:present name="row" property="editPath">
                        <html:link action="${row.editPath}"><bean:message key="form.button.edit"/></html:link>
                    </logic:present>&nbsp;
                    </td>
                </logic:present>
            </logic:notEqual>
            <td><bean:write name="row" property="hardwareName" filter="false"/></td>
            </tr>
        </logic:iterate>
        <logic:equal name="canDeleteLicense" value="true">
            <tr class="header1">
                <td colspan="${colSpan}">
                    <input type="submit" value="<bean:message key="itMgmt.softwareDetail.deleteLicenseButton"/>" onclick="return confirmSubmit('<bean:message key="common.form.confirmDelete"/>')">
                </td>
            </tr>
        </logic:equal>
    </logic:notEmpty>
    <%-- Show some message when there is no data --%>
    <logic:empty name="softwareLicenseList">
        <jsp:include page="/WEB-INF/jsp/common/template/TableEmpty.jsp"/>
    </logic:empty>
</table>
</form>
<%-- Show computers that are using invalid software licenses --%>
<logic:notEmpty name="softwareNeedLicenseList">
<br><br>
<table class="listTable">
    <tr class="header1"><td colspan="2"><b><bean:message key="itMgmt.softwareDetail.unknownLicenseTableHeader"/></b></td></tr>
    <logic:iterate id="row" name="softwareNeedLicenseList">
        <tr class="row2">
            <td><bean:write name="row" property="rowNum"/></td>
            <td width="100%"><bean:write name="row" property="hardwareName" filter="false"/></td>
        </tr>
    </logic:iterate>
</table>
</logic:notEmpty>
