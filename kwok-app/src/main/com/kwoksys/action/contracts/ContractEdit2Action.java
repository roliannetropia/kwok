/*
 * ====================================================================
 * Copyright 2005-2011 Wai-Lun Kwok
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
 */
package com.kwoksys.action.contracts;

import com.kwoksys.biz.ServiceProvider;
import com.kwoksys.biz.admin.core.AdminUtils;
import com.kwoksys.biz.admin.dto.Attribute;
import com.kwoksys.biz.contracts.ContractService;
import com.kwoksys.biz.contracts.dto.Contract;
import com.kwoksys.biz.system.core.AppPaths;
import com.kwoksys.biz.system.core.AttributeManager;
import com.kwoksys.biz.system.core.ObjectTypes;
import com.kwoksys.framework.http.RequestContext;
import com.kwoksys.framework.struts2.Action2;
import org.apache.struts.action.ActionMessages;

import java.util.Map;

/**
 * Action class for editing contract.
 */
public class ContractEdit2Action extends Action2 {

    public String execute() throws Exception {
        ContractForm actionForm = saveActionForm(new ContractForm());
        Contract contract = new Contract();
        contract.setId(actionForm.getContractId());
        contract.setName(actionForm.getContractName());
        contract.setDescription(actionForm.getContractDescription());
        contract.setOwnerId(actionForm.getContractOwner());
        contract.setType(actionForm.getContractType());
        contract.setStage(actionForm.getContractStage());
        contract.setContractProviderId(actionForm.getContractProviderId());
        contract.setEffectiveDateYear(actionForm.getContractEffectiveDateYear());
        contract.setEffectiveDateMonth(actionForm.getContractEffectiveDateMonth());
        contract.setEffectiveDateDate(actionForm.getContractEffectiveDateDate());
        contract.setExpireDateYear(actionForm.getContractExpirationDateYear());
        contract.setExpireDateMonth(actionForm.getContractExpirationDateMonth());
        contract.setExpireDateDate(actionForm.getContractExpirationDateDate());
        contract.setRenewalDateYear(actionForm.getContractRenewalDateYear());
        contract.setRenewalDateMonth(actionForm.getContractRenewalDateMonth());
        contract.setRenewalDateDate(actionForm.getContractRenewalDateDate());
        contract.setRenewalType(actionForm.getContractRenewalType());

        // Get custom field values from request
        Map<Integer, Attribute> customAttributes = new AttributeManager(requestContext).getCustomFieldMap(ObjectTypes.CONTRACT);
        AdminUtils.populateCustomFieldValues(requestContext, actionForm, contract, customAttributes);

        ContractService contractService = ServiceProvider.getContractService(requestContext);

        ActionMessages errors = contractService.updateContract(contract, customAttributes);
        if (!errors.isEmpty()) {
            saveActionErrors(errors);
            return redirect(AppPaths.CONTRACTS_EDIT + "?contractId=" + contract.getId() + "&" + RequestContext.URL_PARAM_ERROR_TRUE);
        } else {
            return redirect(AppPaths.CONTRACTS_DETAIL + "?contractId=" + contract.getId());
        }
    }
}
