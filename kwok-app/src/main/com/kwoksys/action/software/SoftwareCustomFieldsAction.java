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
package com.kwoksys.action.software;

import com.kwoksys.action.common.template.AjaxTemplate;
import com.kwoksys.action.common.template.CustomFieldsTemplate;
import com.kwoksys.biz.ServiceProvider;
import com.kwoksys.biz.software.SoftwareService;
import com.kwoksys.biz.software.dto.Software;
import com.kwoksys.framework.struts2.Action2;

/**
 * Action class for software custom fields.
 */
public class SoftwareCustomFieldsAction extends Action2 {

    public String execute() throws Exception {
        Integer softwareId = requestContext.getParameter("softwareId");

        SoftwareService softwareService = ServiceProvider.getSoftwareService(requestContext);
        Software software = softwareService.getSoftware(softwareId);

        //
        // Template: AjaxTemplate
        //
        AjaxTemplate ajaxTemplate = new AjaxTemplate(requestContext);
        ajaxTemplate.setMethodName(this.getClass().getName());

        //
        // Template: CustomFieldsTemplate
        //
        CustomFieldsTemplate customFieldsTemplate = new CustomFieldsTemplate();
        ajaxTemplate.addTemplate(customFieldsTemplate);
        customFieldsTemplate.setObject(software);
        customFieldsTemplate.setObjectAttrTypeId(software.getType());
        customFieldsTemplate.setShowDefaultHeader(false);

        return ajaxTemplate.findTemplate(AJAX_TEMPLATE);
    }
}