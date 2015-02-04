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
import com.kwoksys.biz.admin.dto.AttributeGroup;
import com.kwoksys.biz.system.core.AppPaths;
import com.kwoksys.biz.system.core.AttributeManager;
import com.kwoksys.biz.system.core.Links;
import com.kwoksys.framework.exceptions.ObjectNotFoundException;
import com.kwoksys.framework.struts2.Action2;

/**
 * Action class for adding custom attribute groups.
 */
public class AttributeGroupAddAction extends Action2 {

    public String execute() throws Exception {
        CustomAttributeForm actionForm = (CustomAttributeForm) getBaseForm(CustomAttributeForm.class);
        Integer objectTypeId = actionForm.getObjectTypeId();

        // Make sure we allow custom attribute for this object type
        if (!AttributeManager.getCustomFieldObjectTypes().contains(objectTypeId)) {
            throw new ObjectNotFoundException();
        }

        // If not a resubmit, set some defaults
        if (!actionForm.isResubmit()) {
            AttributeGroup group = new AttributeGroup();
            actionForm.setAttrGroupName(group.getName());
        }

        //
        // Template: StandardTemplate
        //
        StandardTemplate standardTemplate = new StandardTemplate(requestContext);
        standardTemplate.setPathAttribute("formAction", AppPaths.ADMIN_ATTRIBUTE_GROUP_ADD_2);
        standardTemplate.setAttribute("formCancelLink", Links.getCancelLink(requestContext, AppPaths.ADMIN_CUSTOM_ATTR_LIST).getString());
        request.setAttribute("objectTypeId", objectTypeId);

        //
        // Template: ActionErrorsTemplate
        //
        ActionErrorsTemplate errorsTemplate = new ActionErrorsTemplate();
        standardTemplate.addTemplate(errorsTemplate);
        errorsTemplate.setShowRequiredFieldMsg(true);

        return standardTemplate.findTemplate(STANDARD_TEMPLATE);
    }
}