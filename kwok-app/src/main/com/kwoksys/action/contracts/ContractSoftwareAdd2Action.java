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
import com.kwoksys.biz.contracts.ContractService;
import com.kwoksys.biz.contracts.dto.Contract;
import com.kwoksys.biz.system.core.AppPaths;
import com.kwoksys.biz.system.dto.linking.ContractSoftwareLink;
import com.kwoksys.framework.http.RequestContext;
import com.kwoksys.framework.struts2.Action2;
import org.apache.struts.action.ActionMessages;

/**
 * Action class for adding software to contract.
 */
public class ContractSoftwareAdd2Action extends Action2 {

    public String execute() throws Exception {
        ContractRelationshipForm actionForm = (ContractRelationshipForm) getBaseForm(ContractRelationshipForm.class);

        ContractService contractService = ServiceProvider.getContractService(requestContext);
        Contract contract = contractService.getContract(actionForm.getContractId());

        ContractSoftwareLink contractSoftware = new ContractSoftwareLink();
        contractSoftware.setContractId(actionForm.getContractId());
        contractSoftware.setSoftwareId(actionForm.getSoftwareId());

        ActionMessages errors = contractService.addContractSoftware(contractSoftware);
        if (!errors.isEmpty()) {
            saveActionErrors(errors);
            return redirect(AppPaths.CONTRACTS_SOFTWARE_ADD + "?contractId=" + contract.getId() + "&" + RequestContext.URL_PARAM_ERROR_TRUE);

        } else {
            return redirect(AppPaths.CONTRACTS_ITEMS + "?contractId=" + contract.getId());
        }
    }
}