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

import com.kwoksys.action.common.template.HeaderTemplate;
import com.kwoksys.action.common.template.StandardV2Template;
import com.kwoksys.action.files.FileDeleteTemplate;
import com.kwoksys.biz.ServiceProvider;
import com.kwoksys.biz.files.dto.File;
import com.kwoksys.biz.hardware.HardwareService;
import com.kwoksys.biz.hardware.dto.Hardware;
import com.kwoksys.biz.system.core.AppPaths;
import com.kwoksys.framework.struts2.Action2;

/**
 * Action class for deleting hardware file.
 */
public class HardwareFileDeleteAction extends Action2 {

    public String execute() throws Exception {
        Integer hardwareId = requestContext.getParameter("hardwareId");

        HardwareService hardwareService = ServiceProvider.getHardwareService(requestContext);
        Hardware hardware = hardwareService.getHardware(hardwareId);

        Integer fileId = requestContext.getParameter("fileId");
        File file = hardwareService.getHardwareFile(hardwareId, fileId);

        //
        // Template: StandardV2Template
        //
        StandardV2Template standardTemplate = new StandardV2Template(requestContext);
        standardTemplate.setAttribute("hardwareId", hardwareId);

        //
        // Template: HardwareSpecTemplate
        //
        HardwareSpecTemplate tmpl = new HardwareSpecTemplate(hardware);
        standardTemplate.addTemplate(tmpl);

        //
        // Template: FileDeleteTemplate
        //
        FileDeleteTemplate fileDelete = new FileDeleteTemplate();
        standardTemplate.addTemplate(fileDelete);
        fileDelete.setFile(file);
        fileDelete.setFormAction(AppPaths.HARDWARE_FILE_DELETE_2 + "?hardwareId=" + hardwareId + "&fileId=" + file.getId());
        fileDelete.setFormCancelAction(AppPaths.HARDWARE_FILE + "?hardwareId=" + hardwareId);

        //
        // Template: HeaderTemplate
        //
        HeaderTemplate header = standardTemplate.getHeaderTemplate();
        header.setPageTitleKey("itMgmt.hardwareDetail.header", new Object[] {hardware.getName()});

        return standardTemplate.findTemplate(STANDARD_TEMPLATE);
    }
}
