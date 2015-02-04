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
import com.kwoksys.biz.software.dto.Software;
import com.kwoksys.biz.system.core.AppPaths;
import com.kwoksys.biz.system.dto.linking.SoftwareContactLink;
import com.kwoksys.framework.http.RequestContext;
import com.kwoksys.framework.struts2.Action2;
import org.apache.struts.action.ActionMessages;

/**
 * Action class for adding Software Contact.
 */
public class SoftwareContactAdd2Action extends Action2 {

    public String execute() throws Exception {
        SoftwareContactForm contactForm = saveActionForm(new SoftwareContactForm());
        SoftwareService softwareService = ServiceProvider.getSoftwareService(requestContext);

        // Check to make sure the software exists
        Software software = softwareService.getSoftware(contactForm.getSoftwareId());

        SoftwareContactLink softwareContactLink = new SoftwareContactLink();
        softwareContactLink.setSoftwareId(software.getId());
        softwareContactLink.setContactId(contactForm.getContactId());
        softwareContactLink.setRelDescription(contactForm.getRelationshipDescription());

        ActionMessages errors = softwareService.addSoftwareContact(softwareContactLink);
        if (!errors.isEmpty()) {
            saveActionErrors(errors);
            return redirect(AppPaths.SOFTWARE_CONTACT_ADD + "?softwareId=" + software.getId() + "&" + RequestContext.URL_PARAM_ERROR_TRUE);

        } else {
            return redirect(AppPaths.SOFTWARE_CONTACTS + "?softwareId=" + software.getId());
        }
    }
}