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

import com.kwoksys.biz.ServiceProvider;
import com.kwoksys.biz.files.FileService;
import com.kwoksys.biz.files.dto.File;
import com.kwoksys.biz.software.SoftwareService;
import com.kwoksys.framework.exceptions.ObjectNotFoundException;
import com.kwoksys.framework.http.ResponseContext;
import com.kwoksys.framework.struts2.Action2;

import java.io.FileNotFoundException;

/**
 * Action class for downloading software file.
 */
public class SoftwareFileDownloadAction extends Action2 {

    public String execute() throws Exception {
        ResponseContext responseContext = new ResponseContext(response);

        try {
            SoftwareService softwareService = ServiceProvider.getSoftwareService(requestContext);

            // Get request parameters
            Integer softwareId= requestContext.getParameter("softwareId");
            softwareService.getSoftware(softwareId);

            Integer fileId = requestContext.getParameter("fileId");
            File file = softwareService.getSoftwareFile(softwareId, fileId);

            FileService fileService = ServiceProvider.getFileService(requestContext);
            fileService.download(responseContext, file);

        } catch (ObjectNotFoundException e) {
            throw new FileNotFoundException();
        }
        return null;
    }
}

