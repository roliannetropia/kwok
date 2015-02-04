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

import com.kwoksys.action.common.template.ActionErrorsTemplate;
import com.kwoksys.action.common.template.HeaderTemplate;
import com.kwoksys.action.common.template.StandardTemplate;
import com.kwoksys.biz.ServiceProvider;
import com.kwoksys.biz.contacts.ContactService;
import com.kwoksys.biz.contacts.dto.Company;
import com.kwoksys.biz.contacts.dto.Contact;
import com.kwoksys.biz.system.core.AppPaths;
import com.kwoksys.biz.system.core.Links;
import com.kwoksys.framework.properties.Localizer;
import com.kwoksys.framework.struts2.Action2;

/**
 * Action class for adding company contact.
 */
public class CompanyContactAddAction extends Action2 {

    public String execute() throws Exception {
        CompanyContactForm actionForm = (CompanyContactForm) getBaseForm(CompanyContactForm.class);

        ContactService contactService = ServiceProvider.getContactService(requestContext);
        Company company = contactService.getCompany(actionForm.getCompanyId());

        // If the form has error, we would like to get the values entered by the user.
        Contact contact = new Contact();
        actionForm.setCompanyId(company.getId());

        // If not a resubmit, set some defaults
        if (!actionForm.isResubmit()) {
            actionForm.setContact(contact);
        }

        //
        // Template: StandardTemplate
        //
        StandardTemplate standardTemplate = new StandardTemplate(requestContext);
        request.setAttribute("companyName", company.getName());
        standardTemplate.setPathAttribute("formAction", AppPaths.CONTACTS_COMPANY_CONTACT_ADD_2);
        standardTemplate.setAttribute("formCancelLink", Links.getCancelLink(requestContext, AppPaths.CONTACTS_COMPANY_DETAIL + "?companyId=" + company.getId()));

        //
        // Template: HeaderTemplate
        //
        HeaderTemplate header = standardTemplate.getHeaderTemplate();
        header.setTitleKey("contactMgmt.cmd.companyContactAdd");

        //
        // Template: ActionErrorsTemplate
        //
        ActionErrorsTemplate errorsTemplate = new ActionErrorsTemplate();
        standardTemplate.addTemplate(errorsTemplate);
        errorsTemplate.setShowRequiredFieldMsg(true);
        errorsTemplate.setMessage(Localizer.getText(requestContext, "contactMgmt.companyContactEdit.sectionHeader"));

        return standardTemplate.findTemplate(STANDARD_TEMPLATE);
    }
}
