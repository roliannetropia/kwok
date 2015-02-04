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

import com.kwoksys.action.common.template.HeaderTemplate;
import com.kwoksys.action.common.template.StandardTemplate;
import com.kwoksys.biz.ServiceProvider;
import com.kwoksys.biz.admin.AdminService;
import com.kwoksys.biz.admin.dto.AccessUser;
import com.kwoksys.biz.auth.core.Access;
import com.kwoksys.biz.system.core.*;
import com.kwoksys.framework.connections.database.QueryBits;
import com.kwoksys.framework.struts2.Action2;
import com.kwoksys.framework.ui.Link;
import com.kwoksys.framework.ui.SelectOneLabelValueBean;

import java.util.ArrayList;
import java.util.List;

/**
 * Action class for user index page.
 */
public class UserIndexAction extends Action2 {

    public String execute() throws Exception {
        getSessionBaseForm(UserSearchForm.class);

        AccessUser user = requestContext.getUser();

        AdminService adminService = ServiceProvider.getAdminService(requestContext);

        List links = new ArrayList();
        links.add(new Link(requestContext).setAjaxPath(AppPaths.ADMIN_USER_LIST + "?cmd=showAll").setTitleKey("admin.config.users.all"));
        links.add(new Link(requestContext).setAjaxPath(AppPaths.ADMIN_USER_LIST + "?cmd=showEnabled").setTitleKey("admin.config.users.enabled"));
        links.add(new Link(requestContext).setAjaxPath(AppPaths.ADMIN_USER_LIST + "?cmd=showDisabled").setTitleKey("admin.config.users.disabled"));
        links.add(new Link(requestContext).setAjaxPath(AppPaths.ADMIN_USER_LIST + "?cmd=showLoggedIn").setTitleKey("admin.config.users.loggedIn"));

        // User status selectbox
        List statusOptions = new ArrayList();
        statusOptions.add(new SelectOneLabelValueBean(requestContext));

        AttributeManager attributeManager = new AttributeManager(requestContext);

        //
        // Template: StandardTemplate
        //
        StandardTemplate standardTemplate = new StandardTemplate(requestContext);
        standardTemplate.setAttribute("numUserRecords", adminService.getUserCount(new QueryBits()));
        standardTemplate.setPathAttribute("formUserSearchAction", AppPaths.ADMIN_USER_LIST);
        standardTemplate.setAttribute("statusOptions", attributeManager.getAttrValueOptionsCache(Attributes.USER_STATUS_TYPE, statusOptions));
        standardTemplate.setAttribute("links", links);

        //
        // Template: HeaderTemplate
        //
        HeaderTemplate header = standardTemplate.getHeaderTemplate();
        header.setTitleKey("admin.config.users");
        header.setTitleClassNoLine();
        
        // Add User
        if (Access.hasPermission(user, AppPaths.ADMIN_USER_ADD)) {
            Link link = new Link(requestContext);
            link.setAjaxPath(AppPaths.ADMIN_USER_ADD);
            link.setTitleKey("admin.userAdd.title");
            link.setImgSrc(Image.getInstance().getUserAddIcon());
            header.addHeaderCmds(link);
        }

        // Back to admin home
        header.addNavLink(Links.getAdminHomeLink(requestContext));
        header.addNavLink(new Link(requestContext).setTitleKey("admin.userIndex.userSearchTitle"));

        return standardTemplate.findTemplate(STANDARD_TEMPLATE);
    }
}