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
import com.kwoksys.action.common.template.ObjectDeleteTemplate;
import com.kwoksys.action.common.template.StandardV2Template;
import com.kwoksys.biz.ServiceProvider;
import com.kwoksys.biz.admin.AdminService;
import com.kwoksys.biz.admin.dto.AccessUser;
import com.kwoksys.biz.system.core.AppPaths;
import com.kwoksys.framework.exceptions.ObjectNotFoundException;
import com.kwoksys.framework.struts2.Action2;

/**
 * Action class for deleting user.
 */
public class UserDeleteAction extends Action2 {

    public String execute() throws Exception {
        Integer reqUserId = requestContext.getParameter("userId");

        AdminService adminService = ServiceProvider.getAdminService(requestContext);
        AccessUser requestUser = adminService.getUser(reqUserId);

        // Do not allow deleting default users.
        if (requestUser.isDefaultUser()) {
            throw new ObjectNotFoundException();
        }

        //
        // Template: StandardV2Template
        //
        StandardV2Template standardTemplate = new StandardV2Template(requestContext);

        //
        // Template: HeaderTemplate
        //
        HeaderTemplate header = standardTemplate.getHeaderTemplate();
        header.setPageTitleKey("admin.userDelete.title");

        //
        // Template: UserSpecTemplate
        //
        standardTemplate.addTemplate(new UserSpecTemplate(requestUser));

        //
        // Template: ObjectDeleteTemplate
        //
        ObjectDeleteTemplate objectDeleteTemplate = new ObjectDeleteTemplate();
        standardTemplate.addTemplate(objectDeleteTemplate);
        objectDeleteTemplate.setFormAction(AppPaths.ADMIN_USER_DELETE_2 + "?userId=" + requestUser.getId());
        objectDeleteTemplate.setFormCancelAction(AppPaths.ADMIN_USER_DETAIL + "?userId=" + requestUser.getId());
        objectDeleteTemplate.setTitleKey("admin.userDelete.title");
        objectDeleteTemplate.setConfirmationMsgKey("admin.userDelete.confirm");
        objectDeleteTemplate.setSubmitButtonKey("admin.userDelete.buttonSubmit");

        return standardTemplate.findTemplate(STANDARD_TEMPLATE);
    }
}