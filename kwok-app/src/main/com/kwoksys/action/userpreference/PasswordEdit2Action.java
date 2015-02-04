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

import com.kwoksys.biz.ServiceProvider;
import com.kwoksys.biz.admin.AdminService;
import com.kwoksys.biz.admin.dto.AccessUser;
import com.kwoksys.biz.system.core.AppPaths;
import com.kwoksys.framework.http.RequestContext;
import com.kwoksys.framework.struts2.Action2;
import org.apache.struts.action.ActionMessages;

/**
 * Action class for updating password.
 */
public class PasswordEdit2Action extends Action2 {

    public String execute() throws Exception {
        AccessUser user = requestContext.getUser();

        AdminService adminService = ServiceProvider.getAdminService(requestContext);

        PasswordEditForm actionForm = saveActionForm(new PasswordEditForm());
        user.setRequestedPassword(actionForm.getPasswordOld());
        user.setPasswordNew(actionForm.getPasswordNew());
        user.setPasswordConfirm(actionForm.getPasswordNewConfirm());

        ActionMessages errors = adminService.updateUserPassword(user);
        if (!errors.isEmpty()) {
            saveActionErrors(errors);
            return redirect(AppPaths.USER_PREF_PASSWORD_EDIT + "?" + RequestContext.URL_PARAM_ERROR_TRUE);
        } else {
            return redirect(AppPaths.USER_PREF_INDEX);
        }
    }
}

