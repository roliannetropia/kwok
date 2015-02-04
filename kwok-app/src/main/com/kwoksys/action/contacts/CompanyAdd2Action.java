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
package com.kwoksys.action.contacts;

import com.kwoksys.biz.ServiceProvider;
import com.kwoksys.biz.admin.core.AdminUtils;
import com.kwoksys.biz.admin.dto.Attribute;
import com.kwoksys.biz.contacts.ContactService;
import com.kwoksys.biz.contacts.dto.Company;
import com.kwoksys.biz.system.core.AppPaths;
import com.kwoksys.biz.system.core.ObjectTypes;
import com.kwoksys.biz.system.core.AttributeManager;
import com.kwoksys.framework.http.RequestContext;
import com.kwoksys.framework.struts2.Action2;
import org.apache.struts.action.ActionMessages;

import java.util.Map;

/**
 * Action class for adding company.
 */
public class CompanyAdd2Action extends Action2 {

    public String execute() throws Exception {
        CompanyForm actionForm = saveActionForm(new CompanyForm());

        Company company = new Company();
        company.setName(actionForm.getCompanyName());
        company.setDescription(actionForm.getCompanyDescription());
        company.setTags(actionForm.getCompanyTags());
        company.setTypeIds(actionForm.getCompanyTypes());

        // Get custom field values from request
        Map<Integer, Attribute> customAttributes = new AttributeManager(requestContext).getCustomFieldMap(ObjectTypes.COMPANY);
        AdminUtils.populateCustomFieldValues(requestContext, actionForm, company, customAttributes);

        // Call the service
        ContactService contactService = ServiceProvider.getContactService(requestContext);
        ActionMessages errors = contactService.addCompany(company, customAttributes);

        if (!errors.isEmpty()) {
            saveActionErrors(errors);
            return redirect(AppPaths.CONTACTS_COMPANY_ADD + "?" + RequestContext.URL_PARAM_ERROR_TRUE);

        } else {
            return redirect(AppPaths.CONTACTS_COMPANY_DETAIL + "?companyId=" + company.getId());
        }
    }
}
