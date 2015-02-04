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

import com.kwoksys.action.common.template.ActionErrorsTemplate;
import com.kwoksys.action.common.template.CustomFieldsTemplate;
import com.kwoksys.action.common.template.HeaderTemplate;
import com.kwoksys.action.common.template.StandardTemplate;
import com.kwoksys.biz.ServiceProvider;
import com.kwoksys.biz.software.SoftwareService;
import com.kwoksys.biz.software.dto.Software;
import com.kwoksys.biz.software.dto.SoftwareLicense;
import com.kwoksys.biz.system.core.AppPaths;
import com.kwoksys.biz.system.core.Links;
import com.kwoksys.biz.system.core.ObjectTypes;
import com.kwoksys.framework.struts2.Action2;

/**
 * Action class for editing software license.
 */
public class SoftwareLicenseEditAction extends Action2 {

    public String execute() throws Exception {
        SoftwareLicenseForm actionForm = (SoftwareLicenseForm) getBaseForm(SoftwareLicenseForm.class);

        SoftwareService softwareService = ServiceProvider.getSoftwareService(requestContext);
        Software software = softwareService.getSoftware(actionForm.getSoftwareId());
        SoftwareLicense softwareLicense = softwareService.getSoftwareLicense(actionForm.getSoftwareId(),
                actionForm.getLicenseId());

        // Not a resubmit, set some defaults
        if (!actionForm.isResubmit()) {
            actionForm.setLicense(softwareLicense);
        }

        //
        // Template: StandardTemplate
        //
        StandardTemplate standardTemplate = new StandardTemplate(requestContext);
        request.setAttribute("software", software);
        request.setAttribute("softwareLicense", softwareLicense);
        standardTemplate.setPathAttribute("formAction", AppPaths.SOFTWARE_LICENSE_EDIT_2);
        standardTemplate.setAttribute("formCancelLink", Links.getCancelLink(requestContext, AppPaths.SOFTWARE_DETAIL + "?softwareId=" + software.getId()).getString());

        //
        // Template: HeaderTemplate
        //
        HeaderTemplate header = standardTemplate.getHeaderTemplate();
        header.setPageTitleKey("software.license.updateLicenseHeader");

        //
        // Template: SoftwareSpecTemplate
        //
        SoftwareSpecTemplate softwareSpecTemplate = new SoftwareSpecTemplate(software);
        standardTemplate.addTemplate(softwareSpecTemplate);

        //
        // Template: ActionErrorsTemplate
        //
        ActionErrorsTemplate errorsTemplate = new ActionErrorsTemplate();
        standardTemplate.addTemplate(errorsTemplate);
        errorsTemplate.setShowRequiredFieldMsg(true);

        //
        // Template: CustomFieldsTemplate
        //
        CustomFieldsTemplate customFieldsTemplate = new CustomFieldsTemplate();
        standardTemplate.addTemplate(customFieldsTemplate);
        customFieldsTemplate.setObjectTypeId(ObjectTypes.SOFTWARE_LICENSE);
        customFieldsTemplate.setObjectId(softwareLicense.getId());
        customFieldsTemplate.setForm(actionForm);
        customFieldsTemplate.setPartialTable(true);

        return standardTemplate.findTemplate(STANDARD_TEMPLATE);
    }
}