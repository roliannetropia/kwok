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
import com.kwoksys.biz.hardware.dto.HardwareSoftwareMap;
import com.kwoksys.biz.system.core.AppPaths;
import com.kwoksys.framework.struts2.Action2;

/**
 * Action class for removing hardware license.
 */
public class HardwareLicenseRemove2Action extends Action2 {

    public String execute() throws Exception {
        HardwareLicenseForm actionForm = saveActionForm(new HardwareLicenseForm());
        HardwareSoftwareMap hsm = new HardwareSoftwareMap();
        hsm.setHardwareId(actionForm.getHardwareId());
        hsm.setMapId(actionForm.getMapId());

        HardwareService hardwareService = ServiceProvider.getHardwareService(requestContext);

        // Do the deletion
        hardwareService.unassignSoftwareLicense(hsm);

        // Reset Hardware assigned Software counter
        hardwareService.resetHardwareSoftwareCount(hsm.getHardwareId());

        return redirect(AppPaths.HARDWARE_DETAIL + "?hardwareId=" + hsm.getHardwareId());
    }
}
