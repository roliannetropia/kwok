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
import com.kwoksys.biz.admin.dto.AttributeGroup;
import com.kwoksys.biz.system.core.AppPaths;
import com.kwoksys.biz.system.core.AttributeManager;
import com.kwoksys.framework.http.RequestContext;
import com.kwoksys.framework.exceptions.ObjectNotFoundException;
import com.kwoksys.framework.session.CacheManager;
import com.kwoksys.framework.struts2.Action2;
import org.apache.struts.action.ActionMessages;

/**
 * Action class for adding custom attribute group.
 */
public class AttributeGroupAdd2Action extends Action2 {

    public String execute() throws Exception {
        CustomAttributeForm actionForm = saveActionForm(new CustomAttributeForm());
        Integer objectTypeId = actionForm.getObjectTypeId();

        // Make sure we allow custom attribute for this object type
        if (!AttributeManager.getCustomFieldObjectTypes().contains(objectTypeId)) {
            throw new ObjectNotFoundException();
        }

        AttributeGroup attributeGroup = new AttributeGroup();
        attributeGroup.setObjectTypeId(actionForm.getObjectTypeId());
        attributeGroup.setName(actionForm.getAttrGroupName());

        // Call the service
        AdminService adminService = ServiceProvider.getAdminService(requestContext);
        ActionMessages errors = adminService.addAttributeGroup(attributeGroup);

        if (!errors.isEmpty()) {
            saveActionErrors(errors);
            return redirect(AppPaths.ADMIN_ATTRIBUTE_GROUP_ADD + "?" + RequestContext.URL_PARAM_ERROR_TRUE);

        } else {
            new CacheManager(requestContext).removeCustomAttrGroupsCache(attributeGroup.getObjectTypeId());
            return redirect(AppPaths.ADMIN_CUSTOM_ATTR_LIST);
        }
    }
}