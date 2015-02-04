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
import com.kwoksys.biz.contacts.ContactService;
import com.kwoksys.biz.contacts.dto.Contact;
import com.kwoksys.biz.system.core.AppPaths;
import com.kwoksys.framework.http.RequestContext;
import com.kwoksys.framework.struts2.Action2;
import org.apache.struts.action.ActionMessages;

/**
 * Action class for adding company contact.
 */
public class CompanyContactAdd2Action extends Action2 {

    public String execute() throws Exception {
        // Get request parameters
        CompanyContactForm actionForm = saveActionForm(new CompanyContactForm());

        Contact contact = new Contact();
        contact.setCompanyId(actionForm.getCompanyId());
        contact.setTitle(actionForm.getContactTitle());
        contact.setPhoneWork(actionForm.getContactPhoneWork());
        contact.setFax(actionForm.getContactFax());
        contact.setEmailPrimary(actionForm.getContactEmailPrimary());
        contact.setHomepageUrl(actionForm.getContactHomepageUrl());
        contact.setDescription(actionForm.getContactDescription());
        contact.setAddressStreetPrimary(actionForm.getAddressStreet());
        contact.setAddressCityPrimary(actionForm.getAddressCity());
        contact.setAddressStatePrimary(actionForm.getAddressState());
        contact.setAddressZipcodePrimary(actionForm.getAddressZipcode());
        contact.setAddressCountryPrimary(actionForm.getAddressCountry());

        ContactService contactService = ServiceProvider.getContactService(requestContext);

        ActionMessages errors = contactService.addCompanyContact(contact);
        if (!errors.isEmpty()) {
            saveActionErrors(errors);
            return redirect(AppPaths.CONTACTS_COMPANY_CONTACT_ADD + "?companyId=" + contact.getCompanyId() + "&" + RequestContext.URL_PARAM_ERROR_TRUE);

        } else {
            return redirect(AppPaths.CONTACTS_COMPANY_DETAIL + "?companyId=" + contact.getCompanyId());
        }
    }
}
