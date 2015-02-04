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
package com.kwoksys.action.admin.attribute;

import com.kwoksys.biz.ServiceProvider;
import com.kwoksys.biz.admin.AdminService;
import com.kwoksys.biz.admin.dto.Attribute;
import com.kwoksys.biz.system.core.AppPaths;
import com.kwoksys.framework.http.RequestContext;
import com.kwoksys.framework.struts2.Action2;
import org.apache.struts.action.ActionMessages;

/**
 * Action class for deleting custom attribute.
 */
public class CustomAttributeDelete2Action extends Action2 {

    public String execute() throws Exception {
        Integer attributeId = requestContext.getParameter("attrId");

        // Call the service
        AdminService adminService = ServiceProvider.getAdminService(requestContext);

        // Make sure the object exists
        Attribute attr = adminService.getCustomAttribute(attributeId);

        // Perform deletion
        ActionMessages errors = adminService.deleteAttribute(attr);

        if (!errors.isEmpty()) {
            saveActionErrors(errors);
            return redirect(AppPaths.ADMIN_CUSTOM_ATTR_DELETE + "?attrId=" + attributeId + "&" + RequestContext.URL_PARAM_ERROR_TRUE);

        } else {
            return redirect(AppPaths.ADMIN_CUSTOM_ATTR_LIST);
        }
    }
}