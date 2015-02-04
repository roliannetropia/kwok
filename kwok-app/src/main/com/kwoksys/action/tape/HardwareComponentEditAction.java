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
package com.kwoksys.action.tape;

import com.kwoksys.action.common.template.ActionErrorsTemplate;
import com.kwoksys.action.common.template.CustomFieldsTemplate;
import com.kwoksys.action.common.template.HeaderTemplate;
import com.kwoksys.action.common.template.StandardTemplate;
import com.kwoksys.biz.ServiceProvider;
import com.kwoksys.biz.admin.dto.AccessUser;
import com.kwoksys.biz.auth.core.Access;
import com.kwoksys.biz.hardware.HardwareService;
import com.kwoksys.biz.hardware.dto.Hardware;
import com.kwoksys.biz.hardware.dto.HardwareComponent;
import com.kwoksys.biz.system.core.*;
import com.kwoksys.framework.struts2.Action2;
import com.kwoksys.framework.ui.Link;

/**
 * Action class for showing hardware components.
 */
public class HardwareComponentEditAction extends Action2 {

    public String execute() throws Exception {
        AccessUser user = requestContext.getUser();

        Integer hardwareId = requestContext.getParameter("hardwareId");
        Integer compId = requestContext.getParameter("compId");

        HardwareService hardwareService = ServiceProvider.getHardwareService(requestContext);
        Hardware hardware = hardwareService.getHardware(hardwareId);

        // Hardware component
        HardwareComponent component = hardwareService.getHardwareComponent(hardwareId, compId);

        AttributeManager attributeManager = new AttributeManager(requestContext);

        // If not a resubmit, set some defaults
        HardwareComponentForm actionForm = (HardwareComponentForm) getBaseForm(HardwareComponentForm.class);
        if (!actionForm.isResubmit()) {
            actionForm.setHardwareComponentType(component.getType());
            actionForm.setCompDescription(component.getDescription());
        }

        //
        // Template: StandardTemplate
        //
        StandardTemplate standardTemplate = new StandardTemplate(requestContext);
        request.setAttribute("hardwareId", hardwareId);
        standardTemplate.setPathAttribute("formAction", AppPaths.HARDWARE_COMP_EDIT_2);
        standardTemplate.setAttribute("formCancelLink", Links.getCancelLink(requestContext, AppPaths.HARDWARE_COMP + "?hardwareId=" + hardwareId).getString());
        request.setAttribute("compId", component.getId());
        request.setAttribute("compOptions", attributeManager.getActiveAttrFieldOptionsCache(Attributes.HARDWARE_COMPONENT_TYPE));

        //
        // Template: ActionErrorsTemplate
        //
        ActionErrorsTemplate errorsTemplate = new ActionErrorsTemplate();
        standardTemplate.addTemplate(errorsTemplate);
        errorsTemplate.setShowRequiredFieldMsg(true);

        //
        // Template: HardwareSpecTemplate
        //
        standardTemplate.addTemplate(new HardwareSpecTemplate(hardware));

        //
        // Template: HeaderTemplate
        //
        HeaderTemplate header = standardTemplate.getHeaderTemplate();
        header.setPageTitleKey("itMgmt.hardwareDetail.header", new Object[] {hardware.getName()});

        // Back to hardware list.
        if (Access.hasPermission(user, AppPaths.HARDWARE_LIST)) {
            Link link = new Link(requestContext);
            link.setAjaxPath(AppPaths.HARDWARE_LIST);
            link.setTitleKey("itMgmt.cmd.hardwareList");
            header.addHeaderCmds(link);
        }

        //
        // Template: CustomFieldsTemplate
        //
        CustomFieldsTemplate customFieldsTemplate = new CustomFieldsTemplate();
        standardTemplate.addTemplate(customFieldsTemplate);
        customFieldsTemplate.setObjectTypeId(ObjectTypes.HARDWARE_COMPONENT);
        customFieldsTemplate.setObjectId(component.getId());
        customFieldsTemplate.setForm(actionForm);
        customFieldsTemplate.setPartialTable(true);

        return standardTemplate.findTemplate(STANDARD_TEMPLATE);
    }
}