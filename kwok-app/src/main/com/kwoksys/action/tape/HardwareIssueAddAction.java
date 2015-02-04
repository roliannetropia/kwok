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
import com.kwoksys.action.common.template.StandardV2Template;
import com.kwoksys.action.issues.IssueAssociateTemplate;
import com.kwoksys.biz.ServiceProvider;
import com.kwoksys.biz.hardware.HardwareService;
import com.kwoksys.biz.hardware.dto.Hardware;
import com.kwoksys.biz.system.core.AppPaths;
import com.kwoksys.biz.system.core.ObjectTypes;
import com.kwoksys.framework.struts2.Action2;

/**
 * Action class for adding Hardware Issue.
 */
public class HardwareIssueAddAction extends Action2 {

    public String execute() throws Exception {
        HardwareForm actionForm = (HardwareForm) getBaseForm(HardwareForm.class);

        HardwareService hardwareService = ServiceProvider.getHardwareService(requestContext);
        Hardware hardware = hardwareService.getHardware(actionForm.getHardwareId());

        //
        // Template: StandardV2Template
        //
        StandardV2Template standardTemplate = new StandardV2Template(requestContext);
        standardTemplate.setAttribute("hardwareId", hardware.getId());

        //
        // Template: HeaderTemplate
        //
        HeaderTemplate header = standardTemplate.getHeaderTemplate();
        header.setPageTitleKey("itMgmt.hardwareDetail.header", new Object[] {hardware.getName()});

        //
        // Template: HardwareSpecTemplate
        //
        HardwareSpecTemplate tmpl = new HardwareSpecTemplate(hardware);
        standardTemplate.addTemplate(tmpl);

        //
        // Template: IssueAssociateTemplate
        //
        IssueAssociateTemplate issueAdd = new IssueAssociateTemplate();
        standardTemplate.addTemplate(issueAdd);
        issueAdd.setIssueId(actionForm.getIssueId());
        issueAdd.setLinkedObjectTypeId(ObjectTypes.HARDWARE);
        issueAdd.setLinkedObjectId(hardware.getId());
        issueAdd.setFormSearchAction(AppPaths.HARDWARE_ISSUE_ADD);
        issueAdd.setFormSaveAction(AppPaths.HARDWARE_ISSUE_ADD_2);
        issueAdd.setFormCancelAction(AppPaths.HARDWARE_ISSUE + "?hardwareId=" + hardware.getId());

        return standardTemplate.findTemplate(STANDARD_TEMPLATE);
    }
}