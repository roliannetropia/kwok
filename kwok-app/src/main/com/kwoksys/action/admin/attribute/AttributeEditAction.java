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
import com.kwoksys.biz.admin.dto.Attribute;
import com.kwoksys.biz.system.core.AppPaths;
import com.kwoksys.biz.system.core.AttributeManager;
import com.kwoksys.biz.system.core.Links;
import com.kwoksys.framework.struts2.Action2;
import com.kwoksys.framework.ui.WidgetUtils;

/**
 * Action class for editing attribute.
 */
public class AttributeEditAction extends Action2 {

    public String execute() throws Exception {
        AdminService adminService = ServiceProvider.getAdminService(requestContext);

        Attribute attr = adminService.getSystemAttribute(requestContext.getParameter("attributeId"));

        AttributeForm actionForm = (AttributeForm) getBaseForm(AttributeForm.class);
        actionForm.setAttributeId(attr.getId());

        // If not a resubmit, set some defaults
        if (!actionForm.isResubmit()) {
            actionForm.setRequired(attr.isRequired() ? 1 : 0);
            actionForm.setDefaultAttrField(attr.getDefaultAttrFieldId());
        }

        AttributeManager attributeManager = new AttributeManager(requestContext);

        //
        // Template: StandardTemplate
        //
        StandardTemplate standardTemplate = new StandardTemplate(requestContext);
        standardTemplate.setPathAttribute("formAction", AppPaths.ADMIN_ATTRIBUTE_EDIT_2);
        standardTemplate.setAttribute("formCancelLink", Links.getCancelLink(requestContext, AppPaths.ADMIN_ATTRIBUTE_DETAIL + "?attributeId=" + attr.getId()).getString());
        standardTemplate.setAttribute("attribute", attr);

        if (attr.isRequiredFieldEditable()) {
            request.setAttribute("isRequiredOptions", WidgetUtils.getYesNoOptions(requestContext));
        }

        if (attr.isDefaultAttrFieldEditable()) {
            request.setAttribute("defaultAttrFieldOptions", attributeManager.getActiveAttrFieldOptionsCache(attr.getId()));
        }

        standardTemplate.addTemplate(new ActionErrorsTemplate());

        return standardTemplate.findTemplate(STANDARD_TEMPLATE);
    }
}