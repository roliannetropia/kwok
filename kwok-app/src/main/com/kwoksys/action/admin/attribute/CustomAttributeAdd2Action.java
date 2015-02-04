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
import com.kwoksys.biz.system.core.AttributeManager;
import com.kwoksys.framework.exceptions.ObjectNotFoundException;
import com.kwoksys.framework.http.RequestContext;
import com.kwoksys.framework.struts2.Action2;
import org.apache.struts.action.ActionMessages;

import java.util.List;

/**
 * Action class for adding custom attribute.
 */
public class CustomAttributeAdd2Action extends Action2 {

    public String execute() throws Exception {
        Integer objectTypeId = requestContext.getParameter("objectTypeId");
        List systemFieldIds = requestContext.getParameters("systemFields");

        // Make sure we allow custom attribute for this object type
        if (!AttributeManager.getCustomFieldObjectTypes().contains(objectTypeId)) {
            throw new ObjectNotFoundException();
        }

        CustomAttributeForm actionForm = saveActionForm(new CustomAttributeForm());
        Attribute attr = new Attribute();
        attr.setObjectTypeId(actionForm.getObjectTypeId());
        attr.setName(actionForm.getAttrName());
        attr.setDescription(actionForm.getDescription());
        attr.setType(actionForm.getAttrType());
        attr.setAttributeOption(actionForm.getAttrOption());
        attr.setConvertUrl(actionForm.getAttrConvertUrl());
        attr.setUrl(actionForm.getAttrUrl());
        attr.setAttributeGroupId(actionForm.getAttrGroupId());
        attr.setInputMask(actionForm.getInputMask());
        attr.setAttrFieldIds(systemFieldIds);
        attr.setTypeCurrencySymbol(actionForm.getCurrencySymbol());
        
        // Call the service
        AdminService adminService = ServiceProvider.getAdminService(requestContext);
        ActionMessages errors = adminService.addAttribute(attr);

        if (!errors.isEmpty()) {
            saveActionErrors(errors);
            return redirect(AppPaths.ADMIN_CUSTOM_ATTR_ADD + "?objectTypeId=" + attr.getObjectTypeId() + "&" + RequestContext.URL_PARAM_ERROR_TRUE);

        } else {
            return redirect(AppPaths.ADMIN_CUSTOM_ATTR_LIST);
        }
    }
}