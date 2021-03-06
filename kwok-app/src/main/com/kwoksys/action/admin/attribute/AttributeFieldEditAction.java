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

import com.kwoksys.action.common.template.ActionErrorsTemplate;
import com.kwoksys.action.common.template.StandardTemplate;
import com.kwoksys.biz.ServiceProvider;
import com.kwoksys.biz.admin.AdminService;
import com.kwoksys.biz.admin.core.AdminUtils;
import com.kwoksys.biz.admin.dto.Attribute;
import com.kwoksys.biz.admin.dto.AttributeField;
import com.kwoksys.biz.system.core.AppPaths;
import com.kwoksys.biz.system.core.AttributeManager;
import com.kwoksys.biz.system.core.Links;
import com.kwoksys.framework.struts2.Action2;

import java.util.*;

/**
 * Action class for editing attribute.
 */
public class AttributeFieldEditAction extends Action2 {

    public String execute() throws Exception {
        AttributeForm actionForm = (AttributeForm) getBaseForm(AttributeForm.class);

        AdminService adminService = ServiceProvider.getAdminService(requestContext);

        AttributeField attrField = adminService.getAttributeField(actionForm.getAttrFieldId());
        Attribute attr = adminService.getSystemAttribute(attrField.getAttributeId());

        actionForm.setAttributeId(attrField.getAttributeId());

        // If not a resubmit, set some defaults
        if (!actionForm.isResubmit()) {
            actionForm.setAttributeField(attrField);

            // Get a list of attributes supposed to be shown for this attribute type
            actionForm.setCustomAttrs(adminService.getAttributeFieldTypes(attrField.getId()));
        }

        if (AdminUtils.isAttributeTypeMappingEnabled(attrField.getAttributeId())) {
            List customAttrs = new ArrayList();
            for (Attribute custAttr : new AttributeManager(requestContext).getCustomFieldList(attr.getObjectTypeId())) {
                Map map = new HashMap();
                map.put("attr", custAttr);
                map.put("checked", actionForm.getCustomAttrs().contains(custAttr.getId()) ? "checked" : "");
                customAttrs.add(map);
            }
            request.setAttribute("customAttrs", customAttrs);
        }
        
        //
        // Template: StandardTemplate
        //
        StandardTemplate standardTemplate = new StandardTemplate(requestContext);
        standardTemplate.setPathAttribute("formAction", AppPaths.ADMIN_ATTR_FIELD_EDIT_2);
        standardTemplate.setAttribute("formCancelLink", Links.getCancelLink(requestContext, AppPaths.ADMIN_ATTRIBUTE_DETAIL
                + "?attributeId=" + attr.getId()).getString());
        request.setAttribute("attribute", attr);
        request.setAttribute("iconList", adminService.getIcons(attr.getId()));
        request.setAttribute("statusList", AdminUtils.getAttributeStatusList(requestContext,
                attr.getDefaultAttrFieldId().equals(attrField.getId())));

        //
        // Template: ActionErrorsTemplate
        //
        ActionErrorsTemplate errorsTemplate = new ActionErrorsTemplate();
        standardTemplate.addTemplate(errorsTemplate);
        errorsTemplate.setShowRequiredFieldMsg(true);

        return standardTemplate.findTemplate(STANDARD_TEMPLATE);
    }
}
