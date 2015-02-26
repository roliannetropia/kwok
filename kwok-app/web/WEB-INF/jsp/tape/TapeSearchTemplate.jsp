<%@ taglib uri="http://struts.apache.org/tags-bean" prefix="bean" %>
<%@ taglib uri="http://struts.apache.org/tags-html" prefix="html" %>
<%@ taglib uri="http://struts.apache.org/tags-logic" prefix="logic" %>

<%-- This is for showing tape search --%>
<form action="${TapeSearchTemplate_formAction}" name="TapeSearchForm" method="post">
<input type="hidden" name="_resubmit" value="true">
<html:hidden name="TapeSearchForm" property="cmd" value="search"/>
<table class="standard section">
    <tr>
        <th><bean:message key="common.column.tape_id"/>:</th>
        <td><html:text name="TapeSearchForm" property="tapeId" size="40"/></td>
    </tr>
    <tr>
        <th><bean:message key="common.column.tape_name"/>:</th>
        <td><html:select name="TapeSearchForm" property="tapeNameCriteria">
            <html:options collection="tapeNameCriteriaOptions" property="value" labelProperty="label"/>
        </html:select> <html:text name="TapeSearchForm" property="tapeName" size="40"/></td>
    </tr>
    <tr>
        <th><bean:message key="common.column.tape_serial_number"/>:</th>
        <td><html:text name="TapeSearchForm" property="serialNumber" size="40"/></td>
    </tr>
    <tr>
        <th><bean:message key="common.column.tape_barcode_number"/>:</th>
        <td><html:text name="TapeSearchForm" property="barcodeNumber" size="40"/></td>
    </tr>

    <tr>
        <th><bean:message key="common.column.tape_manufacturer_name"/>:</th>
        <td><html:select name="TapeSearchForm" property="manufacturerId">
            <html:options collection="manufacturersOptions" property="value" labelProperty="label"/>
            </html:select></td>
    </tr>
    <tr>
        <th><bean:message key="common.column.tape_vendor_name"/>:</th>
        <td><html:select name="TapeSearchForm" property="vendorId">
            <html:options collection="vendorsOptions" property="value" labelProperty="label"/>
            </html:select></td>
    </tr>
    <tr><td colspan="2">
        <table><tr>
            <th><bean:message key="common.column.media_type"/>:</th>
            <th>
                <html:select name="TapeSearchForm" property="mediaTypes" multiple="true" size="5">
                    <html:options collection="mediaTypeOptions" property="value" labelProperty="label"/>
                </html:select>
            </th>
            <th><bean:message key="common.column.tape_system"/>:</th>
            <th>
                <html:select name="TapeSearchForm" property="tapeSystem" multiple="true" size="5">
                    <html:options collection="tapeSystemOptions" property="value" labelProperty="label"/>
                </html:select>
            </th>
            <th><bean:message key="common.column.tape_status"/>:</th>
            <th>
                <html:select name="TapeSearchForm" property="tapeStatus" multiple="true" size="5">
                    <html:options collection="tapeStatusOptions" property="value" labelProperty="label"/>
                </html:select>
            </th>
            <th><bean:message key="common.column.tape_location"/>:</th>
            <th>
                <html:select name="TapeSearchForm" property="tapeLocation" multiple="true" size="5">
                    <html:options collection="tapeLocationOptions" property="value" labelProperty="label"/>
                </html:select>
            </th>
        </tr></table>
    </td>
    </tr>
    <tr><td colspan="2">
        <logic:notEqual name="TapeSearchTemplate_hideSearchButton" value="true">
            <html:submit onclick="disableButton(this)"><bean:message key="form.button.search"/></html:submit>
        </logic:notEqual>
    </td></tr>
</table>
</form>
