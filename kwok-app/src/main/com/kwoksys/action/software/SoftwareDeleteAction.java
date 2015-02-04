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
import com.kwoksys.action.common.template.ObjectDeleteTemplate;
import com.kwoksys.action.common.template.StandardV2Template;
import com.kwoksys.biz.ServiceProvider;
import com.kwoksys.biz.admin.dto.AccessUser;
import com.kwoksys.biz.auth.core.Access;
import com.kwoksys.biz.software.SoftwareService;
import com.kwoksys.biz.software.dto.Software;
import com.kwoksys.biz.system.core.AppPaths;
import com.kwoksys.framework.struts2.Action2;
import com.kwoksys.framework.ui.Link;

/**
 * Action class for deleting software.
 */
public class SoftwareDeleteAction extends Action2 {

    public String execute() throws Exception {
        Integer softwareId = requestContext.getParameter("softwareId");

        AccessUser user = requestContext.getUser();

        SoftwareService softwareService = ServiceProvider.getSoftwareService(requestContext);
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

        // Back to software list.
        if (Access.hasPermission(user, AppPaths.SOFTWARE_LIST)) {
            Link link = new Link(requestContext);
            link.setAjaxPath(AppPaths.SOFTWARE_LIST);
            link.setTitleKey("itMgmt.cmd.softwareList");
            header.addHeaderCmds(link);
        }

        //
        // Template: SoftwareSpecTemplate
        //
        SoftwareSpecTemplate tmpl = new SoftwareSpecTemplate(software);
        standardTemplate.addTemplate(tmpl);

        //
        // Template: ObjectDeleteTemplate
        //
        ObjectDeleteTemplate delete = new ObjectDeleteTemplate();
        standardTemplate.addTemplate(delete);
        delete.setFormAction(AppPaths.SOFTWARE_DELETE_2 + "?softwareId=" + softwareId);
        delete.setFormCancelAction(AppPaths.SOFTWARE_DETAIL + "?softwareId=" + softwareId);
        delete.setConfirmationMsgKey("itMgmt.softwareDelete.confirm");
        delete.setSubmitButtonKey("itMgmt.softwareDelete.submitButton");

        return standardTemplate.findTemplate(STANDARD_TEMPLATE);
    }
}
