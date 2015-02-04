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
package com.kwoksys.action.userpreference;

import com.kwoksys.action.contacts.ContactForm;
import com.kwoksys.biz.ServiceProvider;
import com.kwoksys.biz.admin.AdminService;
import com.kwoksys.biz.admin.dto.AccessUser;
import com.kwoksys.biz.contacts.ContactService;
import com.kwoksys.biz.contacts.dto.Contact;
import com.kwoksys.biz.system.core.AppPaths;
import com.kwoksys.framework.http.RequestContext;
import com.kwoksys.framework.struts2.Action2;
import org.apache.struts.action.ActionMessages;

/**
 * Action class for editing user.
 */
public class ContactEdit2Action extends Action2 {

    public String execute() throws Exception {
        AccessUser accessUser = requestContext.getUser();

        ContactService contactService = ServiceProvider.getContactService(requestContext);
        ContactForm actionForm = saveActionForm(new ContactForm());

        Contact contact = contactService.getOptionalContact(accessUser.getContactId());

        contact.setTitle(actionForm.getContactTitle());
        contact.setPhoneHome(actionForm.getContactPhoneHome());
        contact.setPhoneMobile(actionForm.getContactPhoneMobile());
        contact.setPhoneWork(actionForm.getContactPhoneWork());
        contact.setFax(actionForm.getContactFax());
        contact.setEmailSecondary(actionForm.getContactEmailSecondary());
        contact.setMessenger1Id(actionForm.getMessenger1Id());
        contact.setMessenger1Type(actionForm.getMessenger1Type());
        contact.setMessenger2Id(actionForm.getMessenger2Id());
        contact.setMessenger2Type(actionForm.getMessenger2Type());
        contact.setHomepageUrl(actionForm.getContactHomepageUrl());
        contact.setAddressStreetPrimary(actionForm.getAddressStreet());
        contact.setAddressCityPrimary(actionForm.getAddressCity());
        contact.setAddressStatePrimary(actionForm.getAddressState());
        contact.setAddressZipcodePrimary(actionForm.getAddressZipcode());
        contact.setAddressCountryPrimary(actionForm.getAddressCountry());

        AdminService adminService = ServiceProvider.getAdminService(requestContext);
        ActionMessages errors = adminService.updateUserContact(accessUser, contact);

        if (!errors.isEmpty()) {
            saveActionErrors(errors);
            return redirect(AppPaths.USER_PREF_CONTACT_EDIT + "?" + RequestContext.URL_PARAM_ERROR_TRUE);
        } else {
            return redirect(AppPaths.USER_PREF_INDEX);
        }
    }
}
