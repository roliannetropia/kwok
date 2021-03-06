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
import com.kwoksys.biz.software.SoftwareService;
import com.kwoksys.biz.system.core.AppPaths;
import com.kwoksys.biz.system.dto.linking.SoftwareContactLink;
import com.kwoksys.framework.http.RequestContext;
import com.kwoksys.framework.struts2.Action2;
import org.apache.struts.action.ActionMessages;

/**
 * Action class for removing a Software Contact mapping.
 */
public class SoftwareContactRemove2Action extends Action2 {

    public String execute() throws Exception {
        Integer softwareId = requestContext.getParameter("softwareId");
        Integer contactId = requestContext.getParameter("contactId");

        SoftwareService softwareService = ServiceProvider.getSoftwareService(requestContext);

        // Verify the Software exists
        softwareService.getSoftware(softwareId);

        SoftwareContactLink softwareContactLink = new SoftwareContactLink();
        softwareContactLink.setSoftwareId(softwareId);
        softwareContactLink.setContactId(contactId);

        // Delete contract software mapping.
        ActionMessages errors = softwareService.deleteSoftwareContact(softwareContactLink);

        if (!errors.isEmpty()) {
            saveActionErrors(errors);
            return redirect(AppPaths.SOFTWARE_CONTACTS + "?softwareId=" + softwareId + "&" + RequestContext.URL_PARAM_ERROR_TRUE);

        } else {
            return redirect(AppPaths.SOFTWARE_CONTACTS + "?softwareId=" + softwareId);
        }
    }
}