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

import com.kwoksys.action.common.template.AjaxTemplate;
import com.kwoksys.action.common.template.CustomFieldsTemplate;
import com.kwoksys.biz.ServiceProvider;
import com.kwoksys.biz.hardware.HardwareService;
import com.kwoksys.biz.hardware.dto.Hardware;
import com.kwoksys.biz.system.core.ObjectTypes;
import com.kwoksys.framework.struts2.Action2;

/**
 * Hardware custom fields page
 */
public class HardwareCustomFieldsAction extends Action2 {

    public String execute() throws Exception {
        Integer hardwareId = requestContext.getParameter("hardwareId");

        HardwareService hardwareService = ServiceProvider.getHardwareService(requestContext);
        Hardware hardware = hardwareService.getHardware(hardwareId);

        //
        // Template: AjaxTemplate
        //
        AjaxTemplate ajaxTemplate = new AjaxTemplate(requestContext);
        ajaxTemplate.setMethodName(this.getClass().getName());

        //
        // Template: HardwareSpecTemplate
        //
        HardwareSpecTemplate hardwareSpecTemplate = new HardwareSpecTemplate(hardware);
        ajaxTemplate.addTemplate(hardwareSpecTemplate);

        //
        // Template: CustomFieldsTemplate
        //
        CustomFieldsTemplate customFieldsTemplate = new CustomFieldsTemplate();
        ajaxTemplate.addTemplate(customFieldsTemplate);
        customFieldsTemplate.setObjectTypeId(ObjectTypes.HARDWARE);
        customFieldsTemplate.setObjectId(hardwareId);
        customFieldsTemplate.setObjectAttrTypeId(hardware.getType());
        customFieldsTemplate.setShowDefaultHeader(false);

        return ajaxTemplate.findTemplate(AJAX_TEMPLATE);
    }
}