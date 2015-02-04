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

import com.kwoksys.biz.base.BaseTemplate;
import com.kwoksys.biz.contacts.core.CompanySearch;
import com.kwoksys.biz.contacts.core.CompanyUtils;
import com.kwoksys.biz.contacts.dto.Company;
import com.kwoksys.biz.system.core.AppPaths;
import com.kwoksys.biz.system.core.AttributeManager;
import com.kwoksys.biz.system.core.Attributes;
import com.kwoksys.biz.system.core.ObjectTypes;
import com.kwoksys.framework.connections.database.QueryBits;
import com.kwoksys.framework.exceptions.DatabaseException;
import com.kwoksys.framework.ui.SelectOneLabelValueBean;

import java.util.ArrayList;
import java.util.List;

/**
 * ContractSearchTemplate
 */
public class ContractSearchTemplate extends BaseTemplate {

    private String formAction;
    private boolean hideSearchButton;

    public ContractSearchTemplate() {
        super(ContractSearchTemplate.class);
    }

    public void applyTemplate() throws DatabaseException {
        AttributeManager attributeManager = new AttributeManager(requestContext);

        // Get contract type options
        List typeOptions = new ArrayList();
        typeOptions.add(new SelectOneLabelValueBean(requestContext, "0"));
        attributeManager.getActiveAttrFieldOptionsCache(Attributes.CONTRACT_TYPE, typeOptions);

        // Get contract stage options
        List contractStageOptions = new ArrayList();
        contractStageOptions.add(new SelectOneLabelValueBean(requestContext, "0"));
        attributeManager.getAttrValueOptionsCache(Attributes.CONTRACT_STAGE, contractStageOptions);

        // Get contract provider list
        List providerOptions = new ArrayList();
        providerOptions.add(new SelectOneLabelValueBean(requestContext, "0"));

        CompanySearch companySearch = new CompanySearch();
        companySearch.put(CompanySearch.CONTRACT_PROVIDERS, true);

        QueryBits query = new QueryBits(companySearch);
        query.addSortColumn(Company.COMPANY_NAME);

        providerOptions.addAll(CompanyUtils.getCompanyOptions(requestContext, query));

        request.setAttribute("ContractSearchTemplate_formAction", AppPaths.ROOT + formAction);
        request.setAttribute("ContractSearchTemplate_hideSearchButton", hideSearchButton);
        request.setAttribute("customFieldsOptions", new AttributeManager(requestContext).getCustomFieldOptions(ObjectTypes.CONTRACT));
        request.setAttribute("contractTypeOptions", typeOptions);
        request.setAttribute("contractStageOptions", contractStageOptions);
        request.setAttribute("contractProviderOptions", providerOptions);
    }

    public String getFormAction() {
        return formAction;
    }
    public void setFormAction(String formAction) {
        this.formAction = formAction;
    }
    public boolean getHideSearchButton() {
        return hideSearchButton;
    }

    public void setHideSearchButton(boolean hideSearchButton) {
        this.hideSearchButton = hideSearchButton;
    }
}