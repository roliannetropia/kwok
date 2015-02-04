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
import com.kwoksys.action.common.template.HeaderTemplate;
import com.kwoksys.action.common.template.StandardTemplate;
import com.kwoksys.biz.ServiceProvider;
import com.kwoksys.biz.admin.AdminService;
import com.kwoksys.biz.admin.dto.AccessUser;
import com.kwoksys.biz.system.core.AppPaths;
import com.kwoksys.biz.system.core.Links;
import com.kwoksys.biz.system.core.configs.ConfigManager;
import com.kwoksys.framework.properties.Localizer;
import com.kwoksys.framework.struts2.Action2;
import com.kwoksys.framework.util.HtmlUtils;

/**
 * Action class for resetting user password.
 */
public class UserPasswordResetAction extends Action2 {

    public String execute() throws Exception {
        UserPasswordForm actionForm = (UserPasswordForm) getBaseForm(UserPasswordForm.class);
        actionForm.setPassword(null);
        actionForm.setPasswordConfirm(null);

        AdminService adminService = ServiceProvider.getAdminService(requestContext);
        AccessUser user = adminService.getUser(actionForm.getUserId());

        //
        // Template: StandardTemplate
        //
        StandardTemplate standardTemplate = new StandardTemplate(requestContext);
        standardTemplate.setPathAttribute("formAction", AppPaths.ADMIN_USER_PASSWORD_RESET_2);
        standardTemplate.setAttribute("formCancelLink", Links.getCancelLink(requestContext, AppPaths.ADMIN_USER_DETAIL + "?userId=" + actionForm.getUserId()).getString());
        request.setAttribute("allowBlankPassword", ConfigManager.admin.isAllowBlankUserPassword());

        //
        // Template: HeaderTemplate
        //
        HeaderTemplate header = standardTemplate.getHeaderTemplate();
        header.setTitleKey("admin.userPasswordReset.title");

        //
        // Template: ActionErrorsTemplate
        //
        ActionErrorsTemplate errorsTemplate = new ActionErrorsTemplate();
        standardTemplate.addTemplate(errorsTemplate);
        errorsTemplate.setShowRequiredFieldMsg(true);
        errorsTemplate.setMessage(Localizer.getText(requestContext,
                        "admin.userPasswordReset.sectionHeader", new Object[]{HtmlUtils.encode(user.getDisplayName())}));

        return standardTemplate.findTemplate(STANDARD_TEMPLATE);
    }
}
