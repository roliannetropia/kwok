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
import com.kwoksys.biz.system.core.AppPaths;
import com.kwoksys.biz.admin.core.AdminUtils;
import com.kwoksys.biz.admin.dto.Attribute;
import com.kwoksys.biz.software.SoftwareService;
import com.kwoksys.biz.software.dto.Software;
import com.kwoksys.biz.system.core.ObjectTypes;
import com.kwoksys.biz.system.core.AttributeManager;
import com.kwoksys.framework.http.RequestContext;
import com.kwoksys.framework.struts2.Action2;
import org.apache.struts.action.ActionMessages;

import java.util.Map;

/**
 * Action class for editing software detail.
 */
public class SoftwareEdit2Action extends Action2 {

    public String execute() throws Exception {

        SoftwareForm actionForm = saveActionForm(new SoftwareForm());
        Software software = new Software();
        software.setId(actionForm.getSoftwareId());
        software.setName(actionForm.getSoftwareName());
        software.setDescription(actionForm.getSoftwareDescription());
        software.setOwnerId(actionForm.getSoftwareOwner());
        software.setType(actionForm.getSoftwareType());
        software.setOs(actionForm.getSoftwareOS());
        software.setQuotedRetailPrice(actionForm.getRetailPrice());
        software.setQuotedOemPrice(actionForm.getOemPrice());
        software.setManufacturerId(actionForm.getManufacturerId());
        software.setVendorId(actionForm.getVendorId());
        software.setVersion(actionForm.getVersion());
        software.setExpireDateY(actionForm.getExpireDateY());
        software.setExpireDateM(actionForm.getExpireDateM());
        software.setExpireDateD(actionForm.getExpireDateD());

        // Get custom field values from request
        Map<Integer, Attribute> customAttributes = new AttributeManager(requestContext).getCustomFieldMap(ObjectTypes.SOFTWARE);
        AdminUtils.populateCustomFieldValues(requestContext, actionForm, software, customAttributes);

        SoftwareService softwareService = ServiceProvider.getSoftwareService(requestContext);

        ActionMessages errors = softwareService.updateSoftware(software, customAttributes);
        if (!errors.isEmpty()) {
            saveActionErrors(errors);
            return redirect(AppPaths.SOFTWARE_EDIT + "?softwareId=" + software.getId() + "&" + RequestContext.URL_PARAM_ERROR_TRUE);

        } else {
            return redirect(AppPaths.SOFTWARE_DETAIL + "?softwareId=" + software.getId());
        }
    }
}
