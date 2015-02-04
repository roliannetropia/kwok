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

import com.kwoksys.action.common.template.HeaderTemplate;
import com.kwoksys.action.common.template.StandardV2Template;
import com.kwoksys.action.files.FileDeleteTemplate;
import com.kwoksys.biz.ServiceProvider;
import com.kwoksys.biz.files.dto.File;
import com.kwoksys.biz.software.SoftwareService;
import com.kwoksys.biz.software.dto.Software;
import com.kwoksys.biz.system.core.AppPaths;
import com.kwoksys.framework.struts2.Action2;

/**
 * Action class for showing software file description.
 */
public class SoftwareFileDeleteAction extends Action2 {

    public String execute() throws Exception {
        Integer softwareId = requestContext.getParameter("softwareId");

        SoftwareService softwareService = ServiceProvider.getSoftwareService(requestContext);
        Software software = softwareService.getSoftware(softwareId);

        Integer fileId = requestContext.getParameter("fileId");
        File file = softwareService.getSoftwareFile(softwareId, fileId);

        //
        // Template: StandardV2Template
        //
        StandardV2Template standardTemplate = new StandardV2Template(requestContext);
        standardTemplate.setAttribute("softwareId", softwareId);

        //
        // Template: HeaderTemplate
        //
        HeaderTemplate header = standardTemplate.getHeaderTemplate();
        header.setPageTitleKey("itMgmt.softwareDetail.header", new Object[]{software.getName()});

        //
        // Template: SoftwareSpecTemplate
        //
        SoftwareSpecTemplate tmpl = new SoftwareSpecTemplate(software);
        standardTemplate.addTemplate(tmpl);

        //
        // Template: FileDeleteTemplate
        //
        FileDeleteTemplate fileDelete = new FileDeleteTemplate();
        standardTemplate.addTemplate(fileDelete);
        fileDelete.setFile(file);
        fileDelete.setFormAction(AppPaths.SOFTWARE_FILE_DELETE_2 + "?softwareId=" + softwareId + "&fileId=" + fileId);
        fileDelete.setFormCancelAction(AppPaths.SOFTWARE_FILE + "?softwareId=" + softwareId);

        return standardTemplate.findTemplate(STANDARD_TEMPLATE);
    }
}
