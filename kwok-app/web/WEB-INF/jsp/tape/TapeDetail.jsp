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

<jsp:include page="/WEB-INF/jsp/tape/TapeSpecTemplate.jsp"/>

<%-- The user is trying to add a Software to this Tape --%>
<logic:equal name="form" property="cmd" value="add">
<jsp:include page="/WEB-INF/jsp/common/template/ActionError.jsp"/>

<form action="${formAddLicAction}" method="post">
<html:hidden name="form" property="tapeId"/>
<table class="listTable">
<tr class="header1"><td colspan="2"><b><bean:message key="itMgmt.tapeDetail.addLicenseHeader"/> </b></td></tr>
<tr>
    <td class="row2" width="20%"><bean:message key="itMgmt.tapeDetail.addLicenseStep1"/></td>
    <td class="row2">
        <logic:notEmpty name="softwareOptions">
            <html:select name="form" property="softwareId" onchange="updateView('softwareLicensesDiv', '${formGetSoftwareLicenseAction}'+this.value);" >
                <html:options collection="softwareOptions" property="value" labelProperty="label"/>
            </html:select>
        </logic:notEmpty>
        <logic:empty name="softwareOptions" >
            <bean:message key="itMgmt.tapeDetail.noSoftware"/>
        </logic:empty>
    </td>
</tr>
<tr>
    <td class="row2"><bean:message key="itMgmt.tapeDetail.addLicenseStep2"/></td>
    <%-- This area is left empty for AJAX script to get data. --%>
    <td class="row2" id="softwareLicensesDiv">&nbsp;</td>
</tr>
<tr>
    <td class="row2">
        <bean:message key="common.column.license_entitlement"/>
    </td>
    <td class="row2">
        <bean:write name="form" property="licenseEntitlement"/>
        <html:hidden name="form" property="licenseEntitlement"/>
    </td>
</tr>
<tr>
<td class="row2"><bean:message key="itMgmt.tapeDetail.addLicenseStep3"/></td>
<td class="row2">
    <logic:notEmpty name="softwareOptions">
        <html:submit onclick="disableButton(this)">
            <bean:message key="form.button.save"/>
        </html:submit>
        ${formCancelLink}
    </logic:notEmpty>
</td>
</tr>
</table>
</form>
</logic:equal>

<p>
<jsp:include page="/WEB-INF/jsp/common/template/CustomFieldsTableToggle.jsp"/>

<jsp:include page="/WEB-INF/jsp/common/template/Tabs.jsp"/>

<table class="listTable">
<form action="${formRemoveLicenseAction}" method="post">
    <html:hidden name="form" property="tapeId"/>
    <tr class="header1">
        <logic:equal name="canRemoveLicense" value="true">
            <td>&nbsp;</td>
        </logic:equal>
        <th align="left" width="34%"><bean:message key="common.column.software_name"/></th>
        <th align="left" width="33%"><bean:message key="common.column.license_key"/></th>
        <th align="left" width="33%"><bean:message key="software.license.details"/></th>
    </tr>
    <%-- Show the data --%>
    <logic:notEmpty name="installedLicenses">
        <logic:iterate id="row" name="installedLicenses">
            <tr class="${row.rowClass}">
                <logic:equal name="canRemoveLicense" value="true">
                    <td valign="top"><html:radio name="form" property="mapId" value="${row.mapId}"/></td>
                </logic:equal>
                <td valign="top"><bean:write name="row" property="softwareName" filter="false"/></td>
                <td valign="top"><bean:write name="row" property="licenseKey" filter="false"/>
                    <logic:notEmpty name="formLicenseAjaxAction">
                        &nbsp;<a href="javascript:void(0);" onClick="toggleView('note${row.licenseId}');toggleViewUpdate('cf${row.licenseId}','${formLicenseAjaxAction}?softwareId=<bean:write name="row" property="softwareId"/>&licenseId=<bean:write name="row" property="licenseId"/>')"><img src="${image.magGlassIcon}"></a>
                    </logic:notEmpty>
                </td>
                <td valign="top">
                    <span id="note${row.licenseId}"><bean:write name="row" property="licenseNote" filter="false"/></span>
                    <span id="cf${row.licenseId}" class="customFieldEmbedded" style="display:none;"></span>
                </td>
            </tr>
        </logic:iterate>
        <logic:equal name="canRemoveLicense" value="true">
            <tr class="header1"><td colspan="${colSpan}"><input type="submit" value="<bean:message key="itMgmt.tapeDetail.removeLicenseButton"/>" onclick="return confirmSubmit('<bean:message key="common.form.confirmRemove"/>')"></td></tr>
        </logic:equal>
    </logic:notEmpty>
    <%-- Show some message when there is no data --%>
    <logic:empty name="installedLicenses">
        <jsp:include page="/WEB-INF/jsp/common/template/TableEmpty.jsp"/>
    </logic:empty>
</form>
</table>
