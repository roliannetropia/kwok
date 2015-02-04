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

import com.kwoksys.action.common.template.*;
import com.kwoksys.biz.ServiceProvider;
import com.kwoksys.biz.admin.AdminService;
import com.kwoksys.biz.admin.core.AdminUtils;
import com.kwoksys.biz.admin.dto.AccessUser;
import com.kwoksys.biz.contacts.ContactService;
import com.kwoksys.biz.contacts.core.CompanyUtils;
import com.kwoksys.biz.contacts.dto.Contact;
import com.kwoksys.biz.system.core.*;
import com.kwoksys.biz.system.core.configs.ConfigManager;
import com.kwoksys.framework.properties.Localizer;
import com.kwoksys.framework.struts2.Action2;
import com.kwoksys.framework.ui.SelectOneLabelValueBean;

import java.util.ArrayList;
import java.util.List;

/**
 * Action class for adding user.
 */
public class UserAddAction extends Action2 {

    public String execute() throws Exception {
        UserForm actionForm = (UserForm) getBaseForm(UserForm.class);
        AccessUser requestUser = new AccessUser();

        AdminService adminService = ServiceProvider.getAdminService(requestContext);
        ContactService contactService = ServiceProvider.getContactService(requestContext);
        AttributeManager attributeManager = new AttributeManager(requestContext);

        Integer contactId = requestContext.getParameter("contactId", 0);
        Contact contact = contactId == 0 ? new Contact() : contactService.getContact(contactId);

        actionForm.setContactId(contactId);
        actionForm.setPassword(null);
        actionForm.setConfirmPassword(null);

        // If not a resubmit, set some defaults
        if (!actionForm.isResubmit()) {
            // Default user info
            actionForm.setUsername(requestUser.getUsername());
            actionForm.setDisplayName(requestUser.getDisplayName());
            actionForm.setStatus(requestUser.getStatus());
            actionForm.setGroupId(requestUser.getGroupId());
            
            // Default contact info
            actionForm.setFirstName(contact.getFirstName());
            actionForm.setLastName(contact.getLastName());
            actionForm.setEmail(contact.getEmailPrimary());
            actionForm.setCompanyId(contact.getCompanyId());

            actionForm.setContact(contact);
        }

        List companyIdOptions = new ArrayList();
        companyIdOptions.add(new SelectOneLabelValueBean(requestContext, "0"));
        companyIdOptions.addAll(CompanyUtils.getCompanyOptions(requestContext));

        List messengerTypeOptions = new ArrayList();
        messengerTypeOptions.add(new SelectOneLabelValueBean(requestContext, "0"));
        attributeManager.getActiveAttrFieldOptionsCache(Attributes.CONTACT_IM, messengerTypeOptions);

        //
        // Template: StandardTemplate
        //
        StandardTemplate standardTemplate = new StandardTemplate(requestContext);
        standardTemplate.setPathAttribute("formAction", AppPaths.ADMIN_USER_ADD_2);
        standardTemplate.setAttribute("formCancelLink", Links.getCancelLink(requestContext, AppPaths.ADMIN_USER_LIST).getString());
        request.setAttribute("allowPasswordUpdate", adminService.allowPasswordUpdate());
        request.setAttribute("groupIdOptions", AdminUtils.getGroupOptions(requestContext));
        request.setAttribute("optionStatus", attributeManager.getAttrValueOptionsCache(Attributes.USER_STATUS_TYPE));
        request.setAttribute("optionsCompanyId", companyIdOptions);
        request.setAttribute("messengerTypeOptions", messengerTypeOptions);
        request.setAttribute("allowBlankPassword", ConfigManager.admin.isAllowBlankUserPassword());

        //
        // Template: HeaderTemplate
        //
        HeaderTemplate header = standardTemplate.getHeaderTemplate();
        header.setTitleKey("admin.userAdd.title");

        //
        // Template: FooterTemplate
        //
        FooterTemplate footerTemplate = standardTemplate.getFooterTemplate();
        footerTemplate.setOnloadJavascript("tooglePasswordFields(" + actionForm.getStatus() + ", 'passwordSpan', 'confirmPasswordSpan');");

        //
        // Template: ActionErrorsTemplate
        //
        ActionErrorsTemplate errorsTemplate = new ActionErrorsTemplate();
        standardTemplate.addTemplate(errorsTemplate);
        errorsTemplate.setShowRequiredFieldMsg(true);
        errorsTemplate.setMessage(Localizer.getText(requestContext, "admin.userAdd.sectionHeader"));

        //
        // Template: CustomFieldsTemplate
        //
        CustomFieldsTemplate customFieldsTemplate = new CustomFieldsTemplate();
        standardTemplate.addTemplate(customFieldsTemplate);
        customFieldsTemplate.setObjectTypeId(ObjectTypes.USER);
        customFieldsTemplate.setForm(actionForm);

        return standardTemplate.findTemplate(STANDARD_TEMPLATE);
    }
}