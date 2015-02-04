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
import com.kwoksys.biz.files.FileService;
import com.kwoksys.biz.files.dto.File;
import com.kwoksys.biz.hardware.HardwareService;
import com.kwoksys.biz.hardware.dto.Hardware;
import com.kwoksys.biz.system.core.AppPaths;
import com.kwoksys.framework.http.RequestContext;
import com.kwoksys.framework.struts2.Action2;
import org.apache.struts.action.ActionMessages;

/**
 * Action class for deleting hardware file.
 */
public class HardwareFileDelete2Action extends Action2 {

    public String execute() throws Exception {
        Integer hardwareId = requestContext.getParameter("hardwareId");

        HardwareService hardwareService = ServiceProvider.getHardwareService(requestContext);
        Hardware hardware = hardwareService.getHardware(hardwareId);

        // Instantiate File.
        Integer fileId = requestContext.getParameter("fileId");
        File file = hardwareService.getHardwareFile(hardware.getId(), fileId);

        FileService fileService = ServiceProvider.getFileService(requestContext);

        // Delete the file
        ActionMessages errors = fileService.deleteFile(file);
        if (!errors.isEmpty()) {
            saveActionErrors(errors);
            return redirect(AppPaths.HARDWARE_FILE_DELETE + "?hardwareId=" + hardware.getId() + "&fileId=" + fileId + "&" + RequestContext.URL_PARAM_ERROR_TRUE);

        } else {
            // Reset Hardware File count.
            hardwareService.resetHardwareFileCount(hardwareId);
            return redirect(AppPaths.HARDWARE_FILE + "?hardwareId=" + hardwareId);
        }
    }
}
