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
import com.kwoksys.action.common.template.HeaderTemplate;
import com.kwoksys.action.common.template.StandardTemplate;
import com.kwoksys.biz.ServiceProvider;
import com.kwoksys.biz.admin.AdminService;
import com.kwoksys.biz.admin.core.AdminUtils;
import com.kwoksys.biz.admin.dto.Attribute;
import com.kwoksys.biz.admin.dto.AttributeField;
import com.kwoksys.biz.system.core.AppPaths;
import com.kwoksys.biz.system.core.AttributeManager;
import com.kwoksys.biz.system.core.Keywords;
import com.kwoksys.biz.system.core.Links;
import com.kwoksys.framework.session.CacheManager;
import com.kwoksys.framework.struts2.Action2;
import com.kwoksys.framework.ui.WidgetUtils;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Action class for editing attribute.
 */
public class CustomAttributeEditAction extends Action2 {

    public String execute() throws Exception {
        CustomAttributeForm actionForm = (CustomAttributeForm) getBaseForm(CustomAttributeForm.class);

        // Make sure the object exists
        AdminService adminService = ServiceProvider.getAdminService(requestContext);
        Attribute attr = adminService.getCustomAttribute(actionForm.getAttrId());

        // If not a resubmit, set some defaults
        if (!actionForm.isResubmit()) {
            actionForm.setAttribute(attr);

            // Get a list of System Fields supposed to be for this attribute id
            actionForm.getSystemFieldIds().addAll(adminService.getAttributeFieldTypesByField(attr.getId()));
        }

        Integer objectAttrType = AdminUtils.getObjectTypeMap().get(attr.getObjectTypeId());

        // Get a list of System Fields for a particular object type
        if (AdminUtils.isAttributeTypeMappingEnabled(objectAttrType)) {
            List systemFields = new ArrayList();
            for (AttributeField attrField : new CacheManager(requestContext).getAttributeFieldsCache(objectAttrType).values()) {
                Map map = new HashMap();
                map.put("fieldName", attrField.getName());
                map.put("fieldId", attrField.getId());
                map.put("checked", actionForm.getSystemFieldIds().contains(attrField.getId()) ? "checked" : "");
                systemFields.add(map);
            }
            request.setAttribute("systemFields", systemFields);
        }

        //
        // Template: StandardTemplate
        //
        StandardTemplate standardTemplate = new StandardTemplate(requestContext);
        standardTemplate.setPathAttribute("formAction", AppPaths.ADMIN_CUSTOM_ATTR_EDIT_2);
        standardTemplate.setAttribute("formCancelLink", Links.getCancelLink(requestContext, AppPaths.ADMIN_CUSTOM_ATTR_DETAIL + "?attrId=" + attr.getId()).getString());
        request.setAttribute("attrTypeId", actionForm.getAttrType());
        request.setAttribute("attrTypeOptions", AttributeManager.getAttrDataTypeOptions(requestContext, attr.getType()));
        request.setAttribute("attrConvertUrlOptions", WidgetUtils.getYesNoOptions(requestContext));
        request.setAttribute("attrGroupOptions", AdminUtils.getAttributeGroupOptions(requestContext, attr.getObjectTypeId()));
        request.setAttribute("attrUrlExample", Keywords.CUSTOM_FIELD_VALUE_VAR_EXAMPLE);
        standardTemplate.getFooterTemplate().setOnloadJavascript("attrOptions(" +attr.getType() + ");");

        //
        // Template: HeaderTemplate
        //
        HeaderTemplate headerTemplate = standardTemplate.getHeaderTemplate();
        headerTemplate.setTitleKey("admin.customAttrEdit.header");

        //
        // Template: ActionErrorsTemplate
        //
        ActionErrorsTemplate errorsTemplate = new ActionErrorsTemplate();
        standardTemplate.addTemplate(errorsTemplate);
        errorsTemplate.setShowRequiredFieldMsg(true);

        return standardTemplate.findTemplate(STANDARD_TEMPLATE);
    }
}
