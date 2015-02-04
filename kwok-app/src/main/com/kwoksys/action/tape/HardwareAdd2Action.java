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

import com.kwoksys.biz.ServiceProvider;
import com.kwoksys.biz.admin.core.AdminUtils;
import com.kwoksys.biz.admin.dto.Attribute;
import com.kwoksys.biz.hardware.HardwareService;
import com.kwoksys.biz.hardware.dto.Hardware;
import com.kwoksys.biz.system.core.AppPaths;
import com.kwoksys.biz.system.core.AttributeManager;
import com.kwoksys.biz.system.core.ObjectTypes;
import com.kwoksys.framework.http.RequestContext;
import com.kwoksys.framework.struts2.Action2;
import org.apache.struts.action.ActionMessages;

import java.util.Map;

/**
 * Action class for adding hardware.
 */
public class HardwareAdd2Action extends Action2 {

    public String execute() throws Exception {
        HardwareForm actionForm = saveActionForm(new HardwareForm());

        Hardware hardware = new Hardware();
        hardware.setForm(actionForm);

        // Get custom field values from request
        Map<Integer, Attribute> customAttributes = new AttributeManager(requestContext).getCustomFieldMap(ObjectTypes.HARDWARE);
        AdminUtils.populateCustomFieldValues(requestContext, actionForm, hardware, customAttributes);
        
        HardwareService hardwareService = ServiceProvider.getHardwareService(requestContext);

        // Add the hardware
        ActionMessages errors = hardwareService.addHardware(hardware, customAttributes);
        if (!errors.isEmpty()) {
            saveActionErrors(errors);
            return redirect(AppPaths.HARDWARE_ADD + "?" + RequestContext.URL_PARAM_ERROR_TRUE);
        } else {
            return redirect(AppPaths.HARDWARE_DETAIL + "?hardwareId=" + hardware.getId());
        }
    }
}
