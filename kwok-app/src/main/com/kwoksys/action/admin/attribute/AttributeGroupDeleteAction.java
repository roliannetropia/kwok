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

import com.kwoksys.action.common.template.HeaderTemplate;
import com.kwoksys.action.common.template.ObjectDeleteTemplate;
import com.kwoksys.action.common.template.StandardV2Template;
import com.kwoksys.biz.ServiceProvider;
import com.kwoksys.biz.admin.AdminService;
import com.kwoksys.biz.admin.dto.AttributeGroup;
import com.kwoksys.biz.system.core.AppPaths;
import com.kwoksys.framework.properties.Localizer;
import com.kwoksys.framework.struts2.Action2;
import com.kwoksys.framework.util.HtmlUtils;

/**
 * Action class for deleting attribute group.
 */
public class AttributeGroupDeleteAction extends Action2 {

    public String execute() throws Exception {
        CustomAttributeForm actionForm = (CustomAttributeForm) getBaseForm(CustomAttributeForm.class);

        // Make sure the object exists
        AdminService adminService = ServiceProvider.getAdminService(requestContext);
        AttributeGroup attrGroup = adminService.getAttributeGroup(actionForm.getAttrGroupId(), actionForm.getObjectTypeId());

        //
        // Template: StandardV2Template
        //
        StandardV2Template standardTemplate = new StandardV2Template(requestContext);

        //
        // Template: HeaderTemplate
        //
        HeaderTemplate header = standardTemplate.getHeaderTemplate();
        header.setTitleKey("admin.attributeGroupDelete.header");

        //
        // Template: ObjectDeleteTemplate
        //
        ObjectDeleteTemplate deleteTemplate = new ObjectDeleteTemplate();
        standardTemplate.addTemplate(deleteTemplate);
        deleteTemplate.setFormAction(AppPaths.ADMIN_ATTRIBUTE_GROUP_DELETE_2);
        deleteTemplate.setFormCancelAction(AppPaths.ADMIN_CUSTOM_ATTR_LIST);
        deleteTemplate.getFormHiddenVariableMap().put("attrGroupId", actionForm.getAttrGroupId());
        deleteTemplate.getFormHiddenVariableMap().put("objectTypeId", actionForm.getObjectTypeId());
        deleteTemplate.setConfirmationMsgKey("admin.attributeGroupDelete.confirm");
        deleteTemplate.setSubmitButtonKey("admin.attributeGroupDelete.submitButton");
        deleteTemplate.getErrorsTemplate().setMessage(Localizer.getText(requestContext, "admin.attribute.attribute_group_name")
                + ": " + HtmlUtils.encode(attrGroup.getName()));

        return standardTemplate.findTemplate(STANDARD_TEMPLATE);
    }
}