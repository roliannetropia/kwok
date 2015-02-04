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
import com.kwoksys.biz.admin.core.AdminUtils;
import com.kwoksys.biz.admin.dto.Attribute;
import com.kwoksys.biz.software.SoftwareService;
import com.kwoksys.biz.software.dto.SoftwareLicense;
import com.kwoksys.biz.system.core.AppPaths;
import com.kwoksys.biz.system.core.AttributeManager;
import com.kwoksys.biz.system.core.ObjectTypes;
import com.kwoksys.framework.http.RequestContext;
import com.kwoksys.framework.struts2.Action2;
import org.apache.struts.action.ActionMessages;

import java.util.Map;

/**
 * Action class for adding software license.
 */
public class SoftwareLicenseEdit2Action extends Action2 {

    public String execute() throws Exception {
        SoftwareLicenseForm actionForm = saveActionForm(new SoftwareLicenseForm());

        SoftwareLicense license = new SoftwareLicense();
        license.setSoftwareId(actionForm.getSoftwareId());
        license.setId(actionForm.getLicenseId());
        license.setKey(actionForm.getLicenseKey());
        license.setNote(actionForm.getLicenseNote());
        license.setEntitlement(actionForm.getLicenseEntitlement());

        // Get custom field values from request
        Map<Integer, Attribute> customAttributes = new AttributeManager(requestContext).getCustomFieldMap(ObjectTypes.SOFTWARE_LICENSE);
        AdminUtils.populateCustomFieldValues(requestContext, actionForm, license, customAttributes);

        SoftwareService softwareService = ServiceProvider.getSoftwareService(requestContext);

        ActionMessages errors = softwareService.updateLicense(license, customAttributes);
        if (!errors.isEmpty()) {
            saveActionErrors(errors);

            if (actionForm.hasCustomFields()) {
                return redirect(AppPaths.SOFTWARE_LICENSE_EDIT + "?softwareId=" + license.getSoftwareId() + "&licenseId=" + license.getId() + "&" + RequestContext.URL_PARAM_ERROR_TRUE);
            } else {
                return redirect(AppPaths.SOFTWARE_DETAIL + "?softwareId=" + license.getSoftwareId() + "&licenseId=" + license.getId() + "&cmd=edit&" + RequestContext.URL_PARAM_ERROR_TRUE);
            }
        } else {
            return redirect(AppPaths.SOFTWARE_DETAIL + "?softwareId=" + license.getSoftwareId());
        }
    }
}
