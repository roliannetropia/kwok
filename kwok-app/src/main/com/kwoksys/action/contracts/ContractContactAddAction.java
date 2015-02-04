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

import com.kwoksys.action.common.template.HeaderTemplate;
import com.kwoksys.action.common.template.StandardV2Template;
import com.kwoksys.action.contacts.ContactAssociateTemplate;
import com.kwoksys.biz.ServiceProvider;
import com.kwoksys.biz.contracts.ContractService;
import com.kwoksys.biz.contracts.dto.Contract;
import com.kwoksys.biz.system.core.AppPaths;
import com.kwoksys.framework.struts2.Action2;

/**
 * Action class for associating contact.
 */
public class ContractContactAddAction extends Action2 {

    public String execute() throws Exception {
        ContractForm actionForm = (ContractForm) getBaseForm(ContractForm.class);

        ContractService contractService = ServiceProvider.getContractService(requestContext);
        Contract contract = contractService.getContract(actionForm.getContractId());

        //
        // Template: StandardV2Template
        //
        StandardV2Template standardTemplate = new StandardV2Template(requestContext);
        standardTemplate.setAttribute("contractId", contract.getId());

        //
        // Template: HeaderTemplate
        //
        HeaderTemplate header = standardTemplate.getHeaderTemplate();
        header.setPageTitleKey("itMgmt.contractDetail.header", new Object[] {contract.getName()});

        //
        // Template: ContractSpecTemplate
        //
        ContractSpecTemplate template = new ContractSpecTemplate(contract);
        standardTemplate.addTemplate(template);

        //
        // Template: ContactAssociateTemplate
        //
        ContactAssociateTemplate associateTemplate = new ContactAssociateTemplate();
        standardTemplate.addTemplate(associateTemplate);
        associateTemplate.setFormContactId(actionForm.getFormContactId());
        associateTemplate.setFormSearchAction(AppPaths.CONTRACTS_CONTACT_ADD + "?contractId=" + contract.getId());
        associateTemplate.setFormSaveAction(AppPaths.CONTRACTS_CONTACT_ADD_2 + "?contractId=" + contract.getId());
        associateTemplate.setFormCancelAction(AppPaths.CONTRACTS_CONTACTS + "?contractId=" + contract.getId());

        return standardTemplate.findTemplate(STANDARD_TEMPLATE);
    }
}