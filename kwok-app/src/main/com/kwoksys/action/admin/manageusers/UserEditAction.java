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

import com.kwoksys.action.common.template.ActionErrorsTemplate;
import com.kwoksys.action.common.template.CustomFieldsTemplate;
import com.kwoksys.action.common.template.HeaderTemplate;
import com.kwoksys.action.common.template.StandardTemplate;
import com.kwoksys.biz.ServiceProvider;
import com.kwoksys.biz.admin.AdminService;
import com.kwoksys.biz.admin.core.AdminUtils;
import com.kwoksys.biz.admin.dto.AccessUser;
import com.kwoksys.biz.contacts.ContactService;
import com.kwoksys.biz.contacts.core.CompanyUtils;
import com.kwoksys.biz.contacts.dto.Contact;
import com.kwoksys.biz.system.core.*;
import com.kwoksys.framework.properties.Localizer;
import com.kwoksys.framework.struts2.Action2;
import com.kwoksys.framework.ui.SelectOneLabelValueBean;

import java.util.ArrayList;
import java.util.List;

/**
 * Action class for updating a user.
 */
public class UserEditAction extends Action2 {

    public String execute() throws Exception {
        UserForm actionForm = (UserForm) getBaseForm(UserForm.class);

        AdminService adminService = ServiceProvider.getAdminService(requestContext);
        AccessUser requestUser = adminService.getUser(actionForm.getUserId());

        ContactService contactService = ServiceProvider.getContactService(requestContext);
        Contact contact = contactService.getOptionalContact(requestUser.getContactId());

        // If not a resubmit, set some defaults
        if (!actionForm.isResubmit()) {
            // Default user info
            actionForm.setUsername(requestUser.getUsername());
            actionForm.setDisplayName(requestUser.getDisplayName());
            actionForm.setStatus(requestUser.getStatus());
            actionForm.setGroupId(requestUser.getGroupId());
            actionForm.setFirstName(requestUser.getFirstName());
            actionForm.setLastName(requestUser.getLastName());
            actionForm.setEmail(requestUser.getEmail());

            // Default contact info
            actionForm.setCompanyId(contact.getCompanyId());

            actionForm.setContact(contact);
        }

        List companyIdOptions = new ArrayList();
        companyIdOptions.add(new SelectOneLabelValueBean(requestContext, "0"));
        companyIdOptions.addAll(CompanyUtils.getCompanyOptions(requestContext));

        AttributeManager attributeManager = new AttributeManager(requestContext);

        // Get data access object
        List messengerTypeOptions = new ArrayList();
        messengerTypeOptions.add(new SelectOneLabelValueBean(requestContext, "0"));
        attributeManager.getActiveAttrFieldOptionsCache(Attributes.CONTACT_IM,
                contact.getMessenger1Type(), messengerTypeOptions);

        //
        // Template: StandardTemplate
        //
        StandardTemplate standardTemplate = new StandardTemplate(requestContext);
        request.setAttribute("userId", actionForm.getUserId());
        standardTemplate.setPathAttribute("formAction", AppPaths.ADMIN_USER_EDIT_2);
        standardTemplate.setAttribute("formCancelLink", Links.getCancelLink(requestContext, AppPaths.ADMIN_USER_DETAIL + "?userId=" + actionForm.getUserId()).getString());
        request.setAttribute("groupIdOptions", AdminUtils.getGroupOptions(requestContext));
        request.setAttribute("optionStatus", attributeManager.getAttrValueOptionsCache(Attributes.USER_STATUS_TYPE));
        request.setAttribute("optionsCompanyId", companyIdOptions);
        request.setAttribute("messengerTypeOptions", messengerTypeOptions);

        //
        // Template: HeaderTemplate
        //
        HeaderTemplate header = standardTemplate.getHeaderTemplate();
        header.setTitleKey("admin.cmd.userEdit");

        //
        // Template: CustomFieldsTemplate
        //
        CustomFieldsTemplate customFieldsTemplate = new CustomFieldsTemplate();
        standardTemplate.addTemplate(customFieldsTemplate);
        customFieldsTemplate.setObjectTypeId(ObjectTypes.USER);
        customFieldsTemplate.setObjectId(requestUser.getId());
        customFieldsTemplate.setForm(actionForm);

        //
        // Template: ActionErrorsTemplate
        //
        ActionErrorsTemplate errorsTemplate = new ActionErrorsTemplate();
        standardTemplate.addTemplate(errorsTemplate);
        errorsTemplate.setShowRequiredFieldMsg(true);
        errorsTemplate.setMessage(Localizer.getText(requestContext, "admin.userEdit.sectionHeader"));

        return standardTemplate.findTemplate(STANDARD_TEMPLATE);
    }
}
