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

<bean:define id="contract" name="contract" type="com.kwoksys.biz.contracts.dto.Contract"/>

<jsp:include page="/WEB-INF/jsp/common/template/ActionError.jsp"/>

<form action="${formAction}" method="post">
<table class="standardForm">
    <input type="hidden" name="_resubmit" value="true">
    <html:hidden name="form" property="contractId"/>
    <tr>
        <th><bean:message key="common.requiredFieldIndicator.true"/><bean:message key="common.column.contract_name"/>:</th>
        <td><input type="text" name="contractName" value="<bean:write name="form" property="contractName"/>" size="40" autofocus></td>
    </tr>
    <tr>
        <th><bean:message key="common.column.contract_description"/>:</th>
        <td><html:textarea name="form" property="contractDescription" rows="10" cols="50"/></td>
    </tr>
    <tr>
        <th><bean:message name="contract" property="attrRequiredMsgKey(contract_type)"/><bean:message key="common.column.contract_type"/>:</th>
        <td><html:select name="form" property="contractType" onchange="changeAction(this, '${formThisAction}');">
            <html:options collection="contractTypeOptions" property="value" labelProperty="label"/>
            </html:select></td>
    </tr>
    <tr>
        <th><bean:message name="contract" property="attrRequiredMsgKey(contract_stage)"/><bean:message key="common.column.contract_stage"/>:</th>
        <td><html:select name="form" property="contractStage">
            <html:options collection="contractStageOptions" property="value" labelProperty="label"/>
            </html:select></td>
    </tr>
    <tr>
        <th><bean:message key="common.column.contract_owner"/>:</th>
        <td><html:select name="form" property="contractOwner">
            <html:options collection="contractOwnerOptions" property="value" labelProperty="label"/>
            </html:select>
        </td>
    </tr>
    <tr>
        <th><bean:message key="common.column.contract_provider_name"/>:</th>
        <td><html:select name="form" property="contractProviderId">
            <html:options collection="contractProviderOptions" property="value" labelProperty="label"/>
            </html:select></td>
    </tr>    
    <tr>
        <th><bean:message key="common.column.contract_effective_date"/>:</th>
        <td><html:select name="form" property="contractEffectiveDateMonth">
            <html:options collection="monthOptions" property="value" labelProperty="label"/>
            </html:select>
            <html:select name="form" property="contractEffectiveDateDate">
            <html:options collection="dateOptions" property="value" labelProperty="label"/>
            </html:select>
            <html:select name="form" property="contractEffectiveDateYear">
            <html:options collection="effectiveYearOptions" property="value" labelProperty="label"/>
            </html:select></td>
    </tr>
    <tr>
        <th><bean:message key="common.column.contract_expiration_date"/>:</th>
        <td><html:select name="form" property="contractExpirationDateMonth">
            <html:options collection="monthOptions" property="value" labelProperty="label"/>
            </html:select>
            <html:select name="form" property="contractExpirationDateDate">
            <html:options collection="dateOptions" property="value" labelProperty="label"/>
            </html:select>
            <html:select name="form" property="contractExpirationDateYear">
            <html:options collection="expirationYearOptions" property="value" labelProperty="label"/>
            </html:select></td>
    </tr>
    <tr>
        <th><bean:message key="common.column.contract_renewal_date"/>:</th>
        <td><html:select name="form" property="contractRenewalDateMonth">
            <html:options collection="monthOptions" property="value" labelProperty="label"/>
            </html:select>
            <html:select name="form" property="contractRenewalDateDate">
            <html:options collection="dateOptions" property="value" labelProperty="label"/>
            </html:select>
            <html:select name="form" property="contractRenewalDateYear">
            <html:options collection="renewalYearOptions" property="value" labelProperty="label"/>
            </html:select></td>
    </tr>
    <tr>
        <th><bean:message name="contract" property="attrRequiredMsgKey(contract_renewal_type)"/><bean:message key="common.column.contract_renewal_type"/>:</th>
        <td> <html:select name="form" property="contractRenewalType">
            <html:options collection="contractRenewalTypeOptions" property="value" labelProperty="label"/>
            </html:select></td>
    </tr>
    <tr>
        <td colspan="2"><jsp:include page="/WEB-INF/jsp/common/template/CustomFieldsEdit.jsp"/></td>
    </tr>
    <tr>
        <td>&nbsp;</td>
        <td><html:submit onclick="disableButton(this)"><bean:message key="form.button.save"/></html:submit>
            ${formCancelLink}
        </td>
    </tr>
</table>
</form>
