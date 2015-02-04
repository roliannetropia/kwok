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
import com.kwoksys.biz.system.core.AppPaths;
import com.kwoksys.biz.admin.AdminService;
import com.kwoksys.biz.admin.dto.Attribute;
import com.kwoksys.biz.admin.dto.AttributeField;
import com.kwoksys.framework.session.CacheManager;
import com.kwoksys.framework.http.RequestContext;
import com.kwoksys.framework.struts2.Action2;
import org.apache.struts.action.ActionMessages;

/**
 * Action class for editing attribute.
 */
public class AttributeFieldEdit2Action extends Action2 {

    public String execute() throws Exception {
        AttributeForm actionForm = saveActionForm(new AttributeForm());

        AdminService adminService = ServiceProvider.getAdminService(requestContext);
        Attribute attr = adminService.getSystemAttribute(actionForm.getAttributeId());

        AttributeField attrField = new AttributeField();
        attrField.setAttributeId(actionForm.getAttributeId());
        attrField.setId(actionForm.getAttrFieldId());
        attrField.setName(actionForm.getAttributeFieldName());
        attrField.setDescription(actionForm.getAttributeFieldDescription());
        attrField.setIconId(actionForm.getIconId());
        attrField.setDisabled(actionForm.getDisabled());
        attrField.setLinkedAttrIds(actionForm.getCustomAttrs());

        ActionMessages errors = adminService.updateAttributeField(attrField);
        if (!errors.isEmpty()) {
            saveActionErrors(errors);
            return redirect(AppPaths.ADMIN_ATTR_FIELD_EDIT + "?attrFieldId=" + actionForm.getAttrFieldId() + "&" + RequestContext.URL_PARAM_ERROR_TRUE);

        } else {
            // Remove the cache
            new CacheManager(requestContext).removeAttributeFieldsCache(attr.getId());

            return redirect(AppPaths.ADMIN_ATTRIBUTE_DETAIL + "?attributeId=" + attr.getId());
        }
    }
}

