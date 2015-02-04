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
 * Action class for editing custom attribute.
 */
public class CustomAttributeEdit2Action extends Action2 {

    public String execute() throws Exception {
        CustomAttributeForm actionForm = saveActionForm(new CustomAttributeForm());

        AdminService adminService = ServiceProvider.getAdminService(requestContext);

        // Make sure the object exists
        Attribute attr = adminService.getCustomAttribute(actionForm.getAttrId());
        attr.setName(actionForm.getAttrName());
        attr.setDescription(actionForm.getDescription());
        attr.setType(actionForm.getAttrType());
        attr.setAttributeOption(actionForm.getAttrOption());
        attr.setConvertUrl(actionForm.getAttrConvertUrl());
        attr.setUrl(actionForm.getAttrUrl());
        attr.setAttributeGroupId(actionForm.getAttrGroupId());
        attr.setInputMask(actionForm.getInputMask());
        attr.setAttrFieldIds(actionForm.getSystemFieldIds());
        attr.setTypeCurrencySymbol(actionForm.getCurrencySymbol());

        ActionMessages errors = adminService.updateAttribute(attr);
        
        if (!errors.isEmpty()) {
            saveActionErrors(errors);
            return redirect(AppPaths.ADMIN_CUSTOM_ATTR_EDIT + "?objectTypeId=" + attr.getObjectTypeId() + "&attrId=" + attr.getId() + "&" + RequestContext.URL_PARAM_ERROR_TRUE);

        } else {
            return redirect(AppPaths.ADMIN_CUSTOM_ATTR_DETAIL + "?attrId=" + attr.getId());
        }
    }
}