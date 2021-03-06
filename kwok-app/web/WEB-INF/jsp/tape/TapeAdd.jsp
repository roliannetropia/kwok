<%@ taglib uri="http://struts.apache.org/tags-bean" prefix="bean" %>
<%@ taglib uri="http://struts.apache.org/tags-html" prefix="html" %>
<%@ taglib uri="http://struts.apache.org/tags-logic" prefix="logic" %>

<bean:define id="tape" name="tape" type="com.kwoksys.biz.tape.dto.Tape"/>

<jsp:include page="/WEB-INF/jsp/common/template/ActionError.jsp"/>

<form action="${formAction}" method="post">
<input type="hidden" name="_resubmit" value="true">
<table class="standardForm">
    <%--<tr>--%>
        <%--<th><bean:message key="common.column.tape_id"/>:</th>--%>
        <%--<td><bean:message key="form.autoId"/></td>--%>
    <%--</tr>--%>
    <%--<tr>--%>
        <%--<th><bean:message key="common.requiredFieldIndicator.true"/><bean:message key="common.column.tape_name"/>:</th>--%>
        <%--<td><input type="text" name="tapeName" value="<bean:write name="form" property="tapeName"/>" size="40" autofocus></td>--%>
    <%--</tr>--%>
    <%--<tr>--%>
        <%--<th><bean:message name="tape" property="attrRequiredMsgKey(tape_type)"/><bean:message key="common.column.tape_type"/>:</th>--%>
        <%--<td><html:select name="form" property="mediaType" onchange="changeAction(this, '${formThisAction}');">--%>
            <%--<html:options collection="mediaTypeOptions" property="value" labelProperty="label"/>--%>
            <%--</html:select></td>--%>
    <%--<tr>--%>
    <%--</tr>--%>
        <%--<th><bean:message name="tape" property="attrRequiredMsgKey(tape_status)"/><bean:message key="common.column.tape_status"/>:</th>--%>
        <%--<td><html:select name="form" property="tapeStatus">--%>
            <%--<html:options collection="tapeStatusOptions" property="value" labelProperty="label"/>--%>
            <%--</html:select></td>--%>
    <%--</tr>--%>
    <%--<tr>--%>
        <%--<th><bean:message key="common.column.tape_model_name"/>:</th>--%>
        <%--<td><input type="text" name="tapeModelName" value="<bean:write name="form" property="tapeModelName"/>" size="40"></td>--%>
        <%--<th><bean:message key="common.column.tape_model_number"/>:</th>--%>
        <%--<td><html:text name="form" property="tapeModelNumber" size="40"/></td>--%>
    <%--</tr>--%>
    <tr>
        <th><bean:message key="common.requiredFieldIndicator.true"/><bean:message key="common.column.tape_serial_number"/>:</th>
        <td><html:text name="form" property="serialNumber" size="40"/></td>

        <th><bean:message key="common.column.tape_vendor_name"/>:</th>
        <td><html:select name="form" property="vendorId">
            <html:options collection="vendorsOptions" property="value" labelProperty="label"/>
        </html:select> <html:img src="${image.helpIcon}" titleKey="help.addTapeVendor" styleClass="standard" alt=""/></td>
    </tr>
    <tr>
        <th><bean:message key="common.requiredFieldIndicator.true"/><bean:message key="common.column.tape_barcode_number"/>:</th>
        <td><html:text name="form" property="barcodeNumber" size="40"/></td>

        <th><%--<bean:message name="tape" property="attrRequiredMsgKey(media_type)"/>--%><bean:message key="common.column.media_type"/>:</th>
        <td><html:select name="form" property="mediaType" onchange="changeAction(this, '${formThisAction}');">
            <html:options collection="mediaTypeOptions" property="value" labelProperty="label"/>
        </html:select></td>
    </tr>
    <tr>
        <th><bean:message key="common.column.tape_manufacturer_name"/>:</th>
        <td><html:select name="form" property="manufacturerId">
            <html:options collection="manufacturersOptions" property="value" labelProperty="label"/>
        </html:select> <html:img src="${image.helpIcon}" titleKey="help.addTapeManufacturer" styleClass="standard" alt=""/></td>

        <th><bean:message key="common.column.tape_manufactured_date"/>:</th>
        <td><html:select name="form" property="manufacturedMonth">
            <html:options collection="manufacturedMonthOptions" property="value" labelProperty="label"/>
        </html:select>
            <html:select name="form" property="manufacturedDate">
                <html:options collection="manufacturedDateOptions" property="value" labelProperty="label"/>
            </html:select>
            <html:select name="form" property="manufacturedYear">
                <html:options collection="manufacturedYearOptions" property="value" labelProperty="label"/>
            </html:select>
        </td>
    </tr>

    <%--</tr>--%>
        <%--<th>&lt;%&ndash;<bean:message name="tape" property="attrRequiredMsgKey(tape_location)"/>&ndash;%&gt;<bean:message key="common.column.tape_location"/>:</th>--%>
        <%--<td><html:select name="form" property="tapeLocation">--%>
            <%--<html:options collection="tapeLocationOptions" property="value" labelProperty="label"/>--%>
        <%--</html:select></td>--%>
    <%--</tr>--%>
    <%--</tr>--%>
        <%--<th>&lt;%&ndash;<bean:message name="tape" property="attrRequiredMsgKey(tape_retention)"/>&ndash;%&gt;<bean:message key="common.column.tape_retention"/>:</th>--%>
        <%--<td><html:select name="form" property="tapeRetention">--%>
            <%--<html:options collection="tapeRetentionOptions" property="value" labelProperty="label"/>--%>
        <%--</html:select></td>--%>
    <%--</tr>--%>
    <%--</tr>--%>
        <%--<th>&lt;%&ndash;<bean:message name="tape" property="attrRequiredMsgKey(tape_system)"/>&ndash;%&gt;<bean:message key="common.column.tape_system"/>:</th>--%>
        <%--<td><html:select name="form" property="tapeSystem">--%>
            <%--<html:options collection="tapeSystemOptions" property="value" labelProperty="label"/>--%>
        <%--</html:select></td>--%>
    <%--</tr>--%>
    <%--</tr>--%>
        <%--<th>&lt;%&ndash;<bean:message name="tape" property="attrRequiredMsgKey(tape_status)"/>&ndash;%&gt;<bean:message key="common.column.tape_status"/>:</th>--%>
        <%--<td><html:select name="form" property="tapeStatus">--%>
            <%--<html:options collection="tapeStatusOptions" property="value" labelProperty="label"/>--%>
        <%--</html:select></td>--%>
    <%--</tr>--%>
    <%--<tr>--%>
        <%--<th><bean:message key="common.column.tape_transaction_date"/>:</th>--%>
        <%--<td><html:select name="form" property="transactionMonth">--%>
            <%--<html:options collection="transactionMonthOptions" property="value" labelProperty="label"/>--%>
        <%--</html:select>--%>
            <%--<html:select name="form" property="transactionDate">--%>
                <%--<html:options collection="transactionDateOptions" property="value" labelProperty="label"/>--%>
            <%--</html:select>--%>
            <%--<html:select name="form" property="transactionYear">--%>
                <%--<html:options collection="transactionYearOptions" property="value" labelProperty="label"/>--%>
            <%--</html:select>--%>
        <%--</td>--%>
    <%--</tr>--%>

    <%--<tr>--%>
        <%--<th><bean:message key="common.column.tape_move_date"/>:</th>--%>
        <%--<td><html:select name="form" property="moveMonth">--%>
            <%--<html:options collection="moveMonthOptions" property="value" labelProperty="label"/>--%>
        <%--</html:select>--%>
            <%--<html:select name="form" property="moveDate">--%>
                <%--<html:options collection="moveDateOptions" property="value" labelProperty="label"/>--%>
            <%--</html:select>--%>
            <%--<html:select name="form" property="moveYear">--%>
                <%--<html:options collection="moveYearOptions" property="value" labelProperty="label"/>--%>
            <%--</html:select>--%>
        <%--</td>--%>
    <%--</tr>--%>

    <%--<tr>--%>
        <%--<th><bean:message key="common.column.tape_expire_date"/>:</th>--%>
        <%--<td><html:select name="form" property="expireMonth">--%>
            <%--<html:options collection="expireMonthOptions" property="value" labelProperty="label"/>--%>
        <%--</html:select>--%>
            <%--<html:select name="form" property="expireDate">--%>
                <%--<html:options collection="expireDateOptions" property="value" labelProperty="label"/>--%>
            <%--</html:select>--%>
            <%--<html:select name="form" property="expireYear">--%>
                <%--<html:options collection="expireYearOptions" property="value" labelProperty="label"/>--%>
            <%--</html:select>--%>
        <%--</td>--%>
    <%--</tr>--%>

    <%--<tr>--%>
        <%--<th><bean:message key="common.column.tape_transaction_time"/>:</th>--%>
        <%--<td>--%>
            <%--<html:select name="form" property="transactionHour">--%>
                <%--<html:options collection="transactionHourOptions" property="value" labelProperty="label"/>--%>
            <%--</html:select>--%>
            <%--<html:select name="form" property="transactionMin">--%>
                <%--<html:options collection="transactionMinOptions" property="value" labelProperty="label"/>--%>
            <%--</html:select>--%>
        <%--</td>--%>
    <%--</tr>--%>

        <%--<th><bean:message key="common.column.tape_purchase_price"/>:</th>--%>
        <%--<td><bean:write name="currencySymbol"/><html:text name="form" property="tapeCost" size="20"/>--%>
            <%--<span class="formFieldDesc"><bean:message key="itMgmt.colDesc.tape_cost" arg0="${currencySymbol}"/></span>--%>
        <%--</td>--%>
    <%--</tr>--%>
    <%--<tr>--%>
        <%--<th><bean:message key="common.column.tape_purchase_date"/>:</th>--%>
        <%--<td><html:select name="form" property="purchaseMonth" onchange="setWarrantyExpireYear(this.form.name, this.form.warrantyDuration.value)">--%>
                <%--<html:options collection="purchaseMonthOptions" property="value" labelProperty="label"/>--%>
            <%--</html:select>--%>
            <%--<html:select name="form" property="purchaseDate" onchange="setWarrantyExpireYear(this.form.name, this.form.warrantyDuration.value)">--%>
                <%--<html:options collection="purchaseDateOptions" property="value" labelProperty="label"/>--%>
            <%--</html:select>--%>
            <%--<html:select name="form" property="purchaseYear" onchange="setWarrantyExpireYear(this.form.name, this.form.warrantyDuration.value)">--%>
                <%--<html:options collection="purchaseYearOptions" property="value" labelProperty="label"/>--%>
            <%--</html:select>--%>
        <%--</td>--%>
        <%--<th><bean:message key="common.column.tape_warranty_expire_date"/>:</th>--%>
        <%--<td><html:select name="form" property="warrantyMonth">--%>
                <%--<html:options collection="warrantyMonthOptions" property="value" labelProperty="label"/>--%>
            <%--</html:select>--%>
            <%--<html:select name="form" property="warrantyDate">--%>
                <%--<html:options collection="warrantyDateOptions" property="value" labelProperty="label"/>--%>
            <%--</html:select>--%>
            <%--<html:select name="form" property="warrantyYear">--%>
                <%--<html:options collection="warrantyYearOptions" property="value" labelProperty="label"/>--%>
            <%--</html:select>--%>
            <%--&nbsp;<bean:message key="common.condition.or"/>&nbsp;--%>
            <%--<html:select name="form" property="warrantyDuration" onchange="setWarrantyExpireYear(this.form.name, this.form.warrantyDuration.value)">--%>
                <%--<html:options collection="warrantyPeriodOptions" property="value" labelProperty="label"/>--%>
            <%--</html:select>--%>
        <%--</td>--%>
    <%--</tr>--%>
    <%--<tr>--%>
        <%--<th><bean:message key="common.column.tape_last_service_date"/>:</th>--%>
        <%--<td><html:checkbox name="form" property="lastServicedOn" value="1"/> <i><bean:message key="itMgmt.colDesc.tape_last_service_date"/></i></td>--%>
        <%--<th><bean:message name="tape" property="attrRequiredMsgKey(tape_location)"/><bean:message key="common.column.tape_location"/>:</th>--%>
        <%--<td><html:select name="form" property="tapeLocation">--%>
            <%--<html:options collection="locationOptions" property="value" labelProperty="label"/>--%>
            <%--</html:select></td>--%>
    <%--</tr>--%>
    <%--<tr>--%>
        <%--<th><bean:message key="common.column.tape_description"/>:</th>--%>
        <%--<td><html:textarea name="form" property="tapeDescription" rows="10" cols="50"/></td>--%>
        <%--<th><bean:message key="common.column.tape_owner_name"/>:</th>--%>
        <%--<td><html:select name="form" property="tapeOwner">--%>
                <%--<html:options collection="tapeOwnerOptions" property="value" labelProperty="label"/>--%>
            <%--</html:select>--%>
        <%--</td>--%>
    <%--</tr>--%>
    <%--<tr>--%>
        <%--<td colspan="4"><jsp:include page="/WEB-INF/jsp/common/template/CustomFieldsEdit.jsp"/></td>--%>
    <%--</tr>--%>
    <tr>
        <td colspan="4" style="text-align:center"><html:submit onclick="disableButton(this)"><bean:message key="form.button.add"/></html:submit>
            ${formCancelLink}
        </td>
    </tr>
</table>
</form>
