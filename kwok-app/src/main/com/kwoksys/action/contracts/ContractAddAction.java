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

import com.kwoksys.action.common.template.ActionErrorsTemplate;
import com.kwoksys.action.common.template.CustomFieldsTemplate;
import com.kwoksys.action.common.template.HeaderTemplate;
import com.kwoksys.action.common.template.StandardTemplate;
import com.kwoksys.biz.admin.core.AdminUtils;
import com.kwoksys.biz.admin.core.CalendarUtils;
import com.kwoksys.biz.contacts.core.CompanyUtils;
import com.kwoksys.biz.contracts.dto.Contract;
import com.kwoksys.biz.system.core.*;
import com.kwoksys.framework.struts2.Action2;
import com.kwoksys.framework.ui.SelectOneLabelValueBean;
import org.apache.struts.util.LabelValueBean;

import java.util.ArrayList;
import java.util.List;

/**
 * Action class for editing contract.
 */
public class ContractAddAction extends Action2 {

    public String execute() throws Exception {
        ContractForm actionForm = (ContractForm) getBaseForm(ContractForm.class);

        Contract contract = new Contract();

        // Load attributes
        contract.loadAttrs(requestContext);

        // Not a resubmit, set some defaults
        if (!actionForm.isResubmit()) {
            actionForm.setContract(contract);
        }

        AttributeManager attributeManager = new AttributeManager(requestContext);

        // Get contract type options
        List typeOptions = new ArrayList();
        typeOptions.add(new SelectOneLabelValueBean(requestContext, "0"));
        attributeManager.getActiveAttrFieldOptionsCache(Attributes.CONTRACT_TYPE, typeOptions);

        // Get contract stage options
        List contractStageOptions = new ArrayList();
        contractStageOptions.add(new SelectOneLabelValueBean(requestContext, "0"));
        attributeManager.getAttrValueOptionsCache(Attributes.CONTRACT_STAGE, contractStageOptions);

        // Get contract renewal options
        List renewalOptions = new ArrayList();
        renewalOptions.add(new SelectOneLabelValueBean(requestContext, "0"));
        attributeManager.getActiveAttrFieldOptionsCache(Attributes.CONTRACT_RENEWAL_TYPE, renewalOptions);

        // Contract owner options.
        List<LabelValueBean> contractOwnerOptions = new ArrayList();
        contractOwnerOptions.add(new SelectOneLabelValueBean(requestContext, "0"));
        contractOwnerOptions.addAll(AdminUtils.getUserOptions(requestContext));

        // Get company list
        List companyOptions = new ArrayList();
        companyOptions.add(new SelectOneLabelValueBean(requestContext, "0"));
        companyOptions.addAll(CompanyUtils.getCompanyOptions(requestContext));

        //
        // Template: StandardTemplate
        //
        StandardTemplate standardTemplate = new StandardTemplate(requestContext);
        standardTemplate.setAttribute("contract", contract);
        standardTemplate.setPathAttribute("formAction", AppPaths.CONTRACTS_ADD_2);
        standardTemplate.setPathAttribute("formThisAction", AppPaths.CONTRACTS_ADD);
        standardTemplate.setAttribute("formCancelLink", Links.getCancelLink(requestContext, AppPaths.CONTRACTS_LIST).getString());
        standardTemplate.setAttribute("contractTypeOptions", typeOptions);
        standardTemplate.setAttribute("contractStageOptions", contractStageOptions);
        standardTemplate.setAttribute("contractOwnerOptions", contractOwnerOptions);
        standardTemplate.setAttribute("contractRenewalTypeOptions", renewalOptions);
        standardTemplate.setAttribute("dateOptions", CalendarUtils.getDateOptions(requestContext));
        standardTemplate.setAttribute("monthOptions", CalendarUtils.getMonthOptions(requestContext));
        standardTemplate.setAttribute("yearOptions", CalendarUtils.getYearOptions(requestContext));
        standardTemplate.setAttribute("contractProviderOptions", companyOptions);

        //
        // Template: HeaderTemplate
        //
        HeaderTemplate header = standardTemplate.getHeaderTemplate();
        header.setTitleKey("itMgmt.contractAdd.title");

        //
        // Template: ActionErrorsTemplate
        //
        ActionErrorsTemplate errorsTemplate = new ActionErrorsTemplate();
        standardTemplate.addTemplate(errorsTemplate);
        errorsTemplate.setShowRequiredFieldMsg(true);

        //
        // Template: CustomFieldsTemplate
        //
        CustomFieldsTemplate customFieldsTemplate = new CustomFieldsTemplate();
        standardTemplate.addTemplate(customFieldsTemplate);
        customFieldsTemplate.setObjectTypeId(ObjectTypes.CONTRACT);
        customFieldsTemplate.setObjectAttrTypeId(actionForm.getContractType());
        customFieldsTemplate.setForm(actionForm);

        return standardTemplate.findTemplate(STANDARD_TEMPLATE);
    }
}
