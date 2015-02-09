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
        <th><bean:message key="common.column.tape_description"/>:</th>
        <td><html:text name="TapeSearchForm" property="description" size="40"/></td>
    </tr>
    <tr>
        <th><bean:message key="common.column.tape_model_name"/>:</th>
        <td><html:text name="TapeSearchForm" property="tapeModelName" size="40"/></td>
    </tr>
    <tr>
        <th><bean:message key="common.column.tape_model_number"/>:</th>
        <td><html:text name="TapeSearchForm" property="tapeModelNumber" size="40"/></td>
    </tr>
    <tr>
        <th><bean:message key="common.column.tape_serial_number"/>:</th>
        <td><html:text name="TapeSearchForm" property="serialNumber" size="40"/></td>
    </tr>
    <tr>
        <th><bean:message key="common.column.tape_owner_name"/>:</th>
        <td><html:text name="TapeSearchForm" property="tapeOwner" size="40"/></td>
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
    <tr><th><bean:message key="tape.index.search.purchase_date"/>:</th>
        <td>
        <html:select name="TapeSearchForm" property="purchasedAfterMonth">
        <html:options collection="monthOptions" property="value" labelProperty="label"/>
        </html:select>
        <html:select name="TapeSearchForm" property="purchasedAfterDate">
        <html:options collection="dateOptions" property="value" labelProperty="label"/>
        </html:select>
        <html:select name="TapeSearchForm" property="purchasedAfterYear">
        <html:options collection="yearOptions" property="value" labelProperty="label"/>
        </html:select>
        -
        <html:select name="TapeSearchForm" property="purchasedBeforeMonth">
        <html:options collection="monthOptions" property="value" labelProperty="label"/>
        </html:select>
        <html:select name="TapeSearchForm" property="purchasedBeforeDate">
        <html:options collection="dateOptions" property="value" labelProperty="label"/>
        </html:select>
        <html:select name="TapeSearchForm" property="purchasedBeforeYear">
        <html:options collection="yearOptions" property="value" labelProperty="label"/>
        </html:select>
        </td>
    </tr>

    <tr><th><bean:message key="common.column.tape_warranty_expire_date"/>:</th>
        <td>
            <logic:iterate id="warranty" collection="${warrantyOptions}">
                <input type="checkbox" name="${warranty.key}" value="true" ${warranty.checked}>
                <bean:message key="tape.$${warranty.key}"/>
            </logic:iterate>
        </td>
    </tr>

    <tr><td colspan="2">
        <table><tr>
        <th><bean:message key="common.column.tape_type"/>:</th>
        <th>
            <html:select name="TapeSearchForm" property="tapeTypes" multiple="true" size="5">
                <html:options collection="tapeTypeOptions" property="value" labelProperty="label"/>
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
    <%-- Search by custom fields --%>
    <logic:notEmpty name="customFieldsOptions">
        <tr><th><bean:message key="common.template.customFields"/>:</th>
            <td><html:select name="TapeSearchForm" property="attrId">
                <html:options collection="customFieldsOptions" property="value" labelProperty="label"/>
            </html:select> <html:text name="TapeSearchForm" property="attrValue" size="40"/>
        </td></tr>
    </logic:notEmpty>
    <%-- Search by components --%>
    <logic:notEmpty name="componentTypeOptions">
        <tr><th><bean:message key="common.column.tape_component_type"/>:</th>
            <td><html:select name="TapeSearchForm" property="compTypeId">
                <html:options collection="componentTypeOptions" property="value" labelProperty="label"/>
            </html:select> <html:text name="TapeSearchForm" property="compValue" size="40"/>
        </td></tr>
    </logic:notEmpty>
    <tr><td colspan="2">
        <logic:notEqual name="TapeSearchTemplate_hideSearchButton" value="true">
            <html:submit onclick="disableButton(this)"><bean:message key="form.button.search"/></html:submit>
        </logic:notEqual>
    </td></tr>
</table>
</form>
