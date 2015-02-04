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
import com.kwoksys.biz.admin.core.AdminTabs;
import com.kwoksys.biz.admin.core.AdminUtils;
import com.kwoksys.biz.admin.dto.AccessUser;
import com.kwoksys.biz.auth.core.Access;
import com.kwoksys.biz.contacts.ContactService;
import com.kwoksys.biz.contacts.dto.Contact;
import com.kwoksys.biz.system.core.AppPaths;
import com.kwoksys.biz.system.core.Image;
import com.kwoksys.biz.system.core.Links;
import com.kwoksys.biz.system.core.ObjectTypes;
import com.kwoksys.framework.struts2.Action2;
import com.kwoksys.framework.ui.Link;

/**
 * Action class for user detail page.
 */
public class UserDetailAction extends Action2 {

    public String execute() throws Exception {
        AccessUser accessUser = requestContext.getUser();

        Integer reqUserId = requestContext.getParameter("userId");

        AdminService adminService = ServiceProvider.getAdminService(requestContext);
        AccessUser requestUser = adminService.getUser(reqUserId);

        ContactService contactService = ServiceProvider.getContactService(requestContext);
        Contact contact = contactService.getOptionalContact(requestUser.getContactId());

        //
        // Template: StandardTemplate
        //
        StandardTemplate standardTemplate = new StandardTemplate(requestContext);

        //
        // Template: DetailTableTemplate
        //
        DetailTableTemplate detailTableTemplate = AdminUtils.formatUserContact(contact, requestContext, true);
        detailTableTemplate.setPrefix("_contact");
        standardTemplate.addTemplate(detailTableTemplate);

        //
        // Template: UserSpecTemplate
        //
        standardTemplate.addTemplate(new UserSpecTemplate(requestUser));

        //
        // Template: TabsTemplate
        //
        TabsTemplate tabs = new TabsTemplate();
        standardTemplate.addTemplate(tabs);
        tabs.setTabList(AdminUtils.userTabList(requestContext, requestUser));
        tabs.setTabActive(AdminTabs.USER_CONACT_TAB);

        //
        // Template: HeaderTemplate
        //
        HeaderTemplate header = standardTemplate.getHeaderTemplate();
        header.setPageTitleKey("admin.userDetail.title", new Object[] {requestUser.getId()});

        if (Access.hasPermission(accessUser, AppPaths.ADMIN_USER_EDIT)) {
            Link link = new Link(requestContext);
            link.setAjaxPath(AppPaths.ADMIN_USER_EDIT + "?userId=" + requestUser.getId());
            link.setTitleKey("admin.cmd.userEdit");
            link.setImgSrc(Image.getInstance().getUserEditIcon());
            header.addHeaderCmds(link);
        }

        if (Access.hasPermission(accessUser, AppPaths.ADMIN_USER_PASSWORD_RESET)) {
            Link link = new Link(requestContext);
            link.setAjaxPath(AppPaths.ADMIN_USER_PASSWORD_RESET + "?userId=" + requestUser.getId());
            link.setTitleKey("admin.cmd.userPasswordReset");
            header.addHeaderCmds(link);
        }

        if (Access.hasPermission(accessUser, AppPaths.ADMIN_USER_DELETE)) {
            Link link = new Link(requestContext);
            if (requestUser.isDefaultUser() || accessUser.getId().equals(requestUser.getId())) {
                link.setImgSrc(Image.getInstance().getInfoIcon());
                link.setImgAlt("admin.userDelete.cmdWarning");
            } else {
                link.setAjaxPath(AppPaths.ADMIN_USER_DELETE + "?userId=" + requestUser.getId());
                link.setImgSrc(Image.getInstance().getUserDeleteIcon());
            }
            link.setTitleKey("admin.userDelete.title");
            header.addHeaderCmds(link);
        }

        // Back to admin home
        header.addNavLink(Links.getAdminHomeLink(requestContext));

        if (Access.hasPermission(accessUser, AppPaths.ADMIN_USER_INDEX)) {
            Link link = new Link(requestContext);
            link.setAjaxPath(AppPaths.ADMIN_USER_INDEX);
            link.setTitleKey("admin.userIndex.userSearchTitle");
            header.addNavLink(link);
        }

        if (Access.hasPermission(accessUser, AppPaths.ADMIN_USER_LIST)) {
            Link link = new Link(requestContext);
            link.setAjaxPath(AppPaths.ADMIN_USER_LIST);
            link.setTitleKey("admin.userList.title");
            header.addNavLink(link);
        }

        //
        // Template: CustomFieldsTemplate
        //
        CustomFieldsTemplate customFieldsTemplate = new CustomFieldsTemplate();
        standardTemplate.addTemplate(customFieldsTemplate);
        customFieldsTemplate.setObjectTypeId(ObjectTypes.USER);
        customFieldsTemplate.setObjectId(reqUserId);
        customFieldsTemplate.setShowDefaultHeader(false);

        return standardTemplate.findTemplate(STANDARD_TEMPLATE);
    }
}
