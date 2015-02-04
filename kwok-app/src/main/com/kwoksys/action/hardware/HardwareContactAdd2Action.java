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
package com.kwoksys.action.hardware;

import com.kwoksys.biz.ServiceProvider;
import com.kwoksys.biz.hardware.HardwareService;
import com.kwoksys.biz.hardware.dto.Hardware;
import com.kwoksys.biz.system.SystemService;
import com.kwoksys.biz.system.core.AppPaths;
import com.kwoksys.biz.system.dto.linking.HardwareContactLink;
import com.kwoksys.framework.http.RequestContext;
import com.kwoksys.framework.struts2.Action2;
import org.apache.struts.action.ActionMessages;

/**
 * Action class for adding a hardware contact.
 */
public class HardwareContactAdd2Action extends Action2 {

    public String execute() throws Exception {
        HardwareForm actionForm = saveActionForm(new HardwareForm());

        // Check to make sure the hardware exists
        HardwareService hardwareService = ServiceProvider.getHardwareService(requestContext);
        Hardware hardware = hardwareService.getHardware(actionForm.getHardwareId());

        HardwareContactLink hardwareContactLink = new HardwareContactLink();
        hardwareContactLink.setHardwareId(hardware.getId());
        hardwareContactLink.setContactId(Integer.valueOf(actionForm.getContactId()));
        hardwareContactLink.setRelDescription(actionForm.getRelationshipDescription());

        SystemService systemService = ServiceProvider.getSystemService(requestContext);

        ActionMessages errors = systemService.addObjectMapping(hardwareContactLink.createObjectMap());
        if (!errors.isEmpty()) {
            saveActionErrors(errors);
            return redirect(AppPaths.HARDWARE_CONTACT_ADD + "?hardwareId=" + hardware.getId() + "&" + RequestContext.URL_PARAM_ERROR_TRUE);

        } else {
            return redirect(AppPaths.HARDWARE_CONTACTS + "?hardwareId=" + hardware.getId());
        }
    }
}