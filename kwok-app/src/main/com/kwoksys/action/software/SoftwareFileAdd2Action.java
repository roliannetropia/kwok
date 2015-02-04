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

import com.kwoksys.action.files.FileUploadForm;
import com.kwoksys.biz.ServiceProvider;
import com.kwoksys.biz.files.FileService;
import com.kwoksys.biz.files.dto.File;
import com.kwoksys.biz.software.SoftwareService;
import com.kwoksys.biz.software.dto.SoftwareFile;
import com.kwoksys.biz.system.core.AppPaths;
import com.kwoksys.framework.http.RequestContext;
import com.kwoksys.framework.struts2.Action2;
import org.apache.struts.action.ActionMessages;

/**
 * Action class for adding software file.
 */
public class SoftwareFileAdd2Action extends Action2 {

    public String execute() throws Exception {
        Integer softwareId = requestContext.getParameter("softwareId");

        SoftwareService softwareService = ServiceProvider.getSoftwareService(requestContext);
        softwareService.getSoftware(softwareId);

        File file = new SoftwareFile(softwareId);

        FileService fileService = ServiceProvider.getFileService(requestContext);
        ActionMessages errors = fileService.addFile(file, saveActionForm(new FileUploadForm()));

        if (!errors.isEmpty()) {
            saveActionErrors(errors);
            return redirect(AppPaths.SOFTWARE_FILE_ADD + "?softwareId=" + softwareId + "&" + RequestContext.URL_PARAM_ERROR_TRUE);

        } else {
            // Reset Software File counter.
            softwareService.resetSoftwareFileCount(softwareId);

            return redirect(AppPaths.SOFTWARE_FILE + "?softwareId=" + softwareId);
        }
    }
}
