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

<form action="${ContractSearchTemplate_formAction}" name="ContractSearchForm" method="post">
<input type="hidden" name="cmd" value="search">
<input type="hidden" name="_resubmit" value="true">
<input type="hidden" name="reportType" value="${reportType}">
<table class="standard section">
    <tr>
        <th><bean:message key="common.column.contract_name"/>:</th>
        <td><html:text name="ContractSearchForm" property="contractName" size="40"/></td>
    </tr>
    <tr>
        <th><bean:message key="common.column.contract_description"/>:</th>
        <td><html:text name="ContractSearchForm" property="description" size="40"/></td>
    </tr>
    <tr>
        <th><bean:message key="common.column.contract_type"/>:</th>
        <td><html:select name="ContractSearchForm" property="contractTypeId">
            <html:options collection="contractTypeOptions" property="value" labelProperty="label"/>
            </html:select></td>
    </tr>
    <tr>
        <th><bean:message key="common.column.contract_stage"/>:</th>
        <td><html:select name="ContractSearchForm" property="stage">
            <html:options collection="contractStageOptions" property="value" labelProperty="label"/>
            </html:select></td>
    </tr>
    <tr>
        <th><bean:message key="common.column.contract_provider_name"/>:</th>
        <td><html:select name="ContractSearchForm" property="contractProviderId">
            <html:options collection="contractProviderOptions" property="value" labelProperty="label"/>
            </html:select></td>
    </tr>
    <logic:notEmpty name="customFieldsOptions">
        <tr><th><bean:message key="common.template.customFields"/>:</th>
            <td><html:select name="ContractSearchForm" property="attrId">
                <html:options collection="customFieldsOptions" property="value" labelProperty="label"/>
            </html:select> <html:text name="ContractSearchForm" property="attrValue" size="40"/>
        </td></tr>
    </logic:notEmpty>
    <tr>
        <td colspan="2">
            <logic:notEqual name="ContractSearchTemplate_hideSearchButton" value="true">
                <html:submit onclick="disableButton(this)"><bean:message key="form.button.search"/></html:submit>
            </logic:notEqual>
        </td>
    </tr>
</table>
</form>
