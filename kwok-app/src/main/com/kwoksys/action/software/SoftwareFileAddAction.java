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
import com.kwoksys.action.files.FileAddTemplate;
import com.kwoksys.action.files.FileUploadForm;
import com.kwoksys.biz.ServiceProvider;
import com.kwoksys.biz.software.SoftwareService;
import com.kwoksys.biz.software.dto.Software;
import com.kwoksys.biz.system.core.AppPaths;
import com.kwoksys.framework.struts2.Action2;

/**
 * Action class for adding software file.
 */
public class SoftwareFileAddAction extends Action2 {

    public String execute() throws Exception {
        SoftwareService softwareService = ServiceProvider.getSoftwareService(requestContext);

        Integer softwareId= requestContext.getParameter("softwareId");
        Software software = softwareService.getSoftware(softwareId);

        //
        // Template: StandardV2Template
        //
        StandardV2Template standardTemplate = new StandardV2Template(requestContext);

        //
        // Template: HeaderTemplate
        //
        HeaderTemplate header = standardTemplate.getHeaderTemplate();
        header.setPageTitleKey("itMgmt.softwareDetail.header", new Object[] {software.getName()});

        //
        // Template: SoftwareSpecTemplate
        //
        SoftwareSpecTemplate tmpl = new SoftwareSpecTemplate(software);
        standardTemplate.addTemplate(tmpl);

        //
        // Template: FileAddTemplate
        //
        FileAddTemplate fileAdd = new FileAddTemplate(getBaseForm(FileUploadForm.class));
        standardTemplate.addTemplate(fileAdd);
        fileAdd.setFileName(requestContext.getParameterString("fileName0"));
        fileAdd.setFormAction(AppPaths.SOFTWARE_FILE_ADD_2 + "?softwareId=" + software.getId());
        fileAdd.setFormCancelAction(AppPaths.SOFTWARE_FILE + "?softwareId=" + software.getId());
        fileAdd.getErrorsTemplate().setShowRequiredFieldMsg(true);

        return standardTemplate.findTemplate(STANDARD_TEMPLATE);
    }
}

