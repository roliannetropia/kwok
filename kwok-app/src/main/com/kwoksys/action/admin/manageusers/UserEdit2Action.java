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
package com.kwoksys.action.admin.manageusers;

import com.kwoksys.biz.ServiceProvider;
import com.kwoksys.biz.admin.AdminService;
import com.kwoksys.biz.admin.core.AdminUtils;
import com.kwoksys.biz.admin.dto.AccessGroup;
import com.kwoksys.biz.admin.dto.AccessUser;
import com.kwoksys.biz.admin.dto.Attribute;
import com.kwoksys.biz.contacts.ContactService;
import com.kwoksys.biz.contacts.dto.Contact;
import com.kwoksys.biz.system.core.AppPaths;
import com.kwoksys.biz.system.core.AttributeManager;
import com.kwoksys.biz.system.core.ObjectTypes;
import com.kwoksys.framework.http.RequestContext;
import com.kwoksys.framework.struts2.Action2;
import org.apache.struts.action.ActionMessages;

import java.util.Map;

/**
 * Action class for editing user.
 */
public class UserEdit2Action extends Action2 {

    public String execute() throws Exception {
        UserForm actionForm = saveActionForm(new UserForm());

        Integer reqUserId = requestContext.getParameter("userId");

        AdminService adminService = ServiceProvider.getAdminService(requestContext);

        AccessUser requestUser = adminService.getUser(reqUserId);
        requestUser.setUsername(actionForm.getUsername());
        requestUser.setFirstName(actionForm.getFirstName());
        requestUser.setLastName(actionForm.getLastName());
        requestUser.setEmail(actionForm.getEmail());
        requestUser.setDisplayName(actionForm.getDisplayName());
        requestUser.setStatus(actionForm.getStatus());

        AccessGroup group = new AccessGroup();
        group.setId(actionForm.getGroupId());

        ContactService contactService = ServiceProvider.getContactService(requestContext);
        Contact contact = contactService.getOptionalContact(requestUser.getContactId());
        contact.setCompanyId(actionForm.getCompanyId());
        contact.setEmailPrimary(actionForm.getEmail());
        contact.setFirstName(actionForm.getFirstName());
        contact.setLastName(actionForm.getLastName());
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
        contact.setDescription(actionForm.getContactDescription());
        contact.setAddressStreetPrimary(actionForm.getAddressStreet());
        contact.setAddressCityPrimary(actionForm.getAddressCity());
        contact.setAddressStatePrimary(actionForm.getAddressState());
        contact.setAddressZipcodePrimary(actionForm.getAddressZipcode());
        contact.setAddressCountryPrimary(actionForm.getAddressCountry());

        // Get custom field values from request
        Map<Integer, Attribute> customAttributes = new AttributeManager(requestContext).getCustomFieldMap(ObjectTypes.USER);
        AdminUtils.populateCustomFieldValues(requestContext, actionForm, requestUser, customAttributes);

        ActionMessages errors = adminService.updateUser(requestUser, group, contact, customAttributes);
        if (!errors.isEmpty()) {
            saveActionErrors(errors);
            return redirect(AppPaths.ADMIN_USER_EDIT + "?userId=" + requestUser.getId() + "&" + RequestContext.URL_PARAM_ERROR_TRUE);
        } else {
            return redirect(AppPaths.ADMIN_USER_DETAIL + "?userId=" + requestUser.getId());
        }
    }
}
