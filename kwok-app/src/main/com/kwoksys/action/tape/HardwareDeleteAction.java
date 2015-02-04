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

import com.kwoksys.action.common.template.HeaderTemplate;
import com.kwoksys.action.common.template.ObjectDeleteTemplate;
import com.kwoksys.action.common.template.StandardV2Template;
import com.kwoksys.biz.ServiceProvider;
import com.kwoksys.biz.admin.dto.AccessUser;
import com.kwoksys.biz.auth.core.Access;
import com.kwoksys.biz.hardware.HardwareService;
import com.kwoksys.biz.hardware.dto.Hardware;
import com.kwoksys.biz.system.core.AppPaths;
import com.kwoksys.framework.struts2.Action2;
import com.kwoksys.framework.ui.Link;

/**
 * Action class for deleting hardware.
 */
public class HardwareDeleteAction extends Action2 {

    public String execute() throws Exception {
        Integer hardwareId = requestContext.getParameter("hardwareId");

        AccessUser user = requestContext.getUser();

        HardwareService hardwareService = ServiceProvider.getHardwareService(requestContext);
        Hardware hardware = hardwareService.getHardware(hardwareId);

        //
        // Template: StandardV2Template
        //
        StandardV2Template standardTemplate = new StandardV2Template(requestContext);

        HeaderTemplate header = standardTemplate.getHeaderTemplate();
        header.setPageTitleKey("itMgmt.hardwareDetail.header", new Object[] {hardware.getName()});

        // Back to Hardware list link.
        if (Access.hasPermission(user, AppPaths.HARDWARE_LIST)) {
            Link link = new Link(requestContext);
            link.setAjaxPath(AppPaths.HARDWARE_LIST);
            link.setTitleKey("itMgmt.cmd.hardwareList");
            header.addHeaderCmds(link);
        }

        //
        // Template: HardwareSpecTemplate
        //
        HardwareSpecTemplate tmpl = new HardwareSpecTemplate(hardware);
        standardTemplate.addTemplate(tmpl);

        //
        // Template: ObjectDeleteTemplate
        //
        ObjectDeleteTemplate delete = new ObjectDeleteTemplate();
        standardTemplate.addTemplate(delete);
        delete.setFormAction(AppPaths.HARDWARE_DELETE_2 + "?hardwareId=" + hardwareId);
        delete.setFormCancelAction(AppPaths.HARDWARE_DETAIL + "?hardwareId=" + hardwareId);
        delete.setConfirmationMsgKey("itMgmt.hardwareDelete.confirm");
        delete.setSubmitButtonKey("itMgmt.hardwareDelete.submitButton");

        return standardTemplate.findTemplate(STANDARD_TEMPLATE);
    }
}