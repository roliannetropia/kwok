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
package com.kwoksys.action.portal;

import com.kwoksys.biz.ServiceProvider;
import com.kwoksys.biz.portal.PortalService;
import com.kwoksys.biz.portal.dto.Site;
import com.kwoksys.biz.system.core.AppPaths;
import com.kwoksys.framework.http.RequestContext;
import com.kwoksys.framework.struts2.Action2;
import org.apache.struts.action.ActionMessages;

/**
 * Action class for editing Website.
 */
public class SiteEdit2Action extends Action2 {

    public String execute() throws Exception {

        // Get the inputs
        Site site = new Site();
        SiteForm actionForm = saveActionForm(new SiteForm());
        site.setId(actionForm.getSiteId());
        site.setName(actionForm.getSiteName());
        site.setPath(actionForm.getSitePath());
        site.setDescription(actionForm.getSiteDescription());
        site.setPlacement(actionForm.getSitePlacement());
        site.setSupportIframe(actionForm.getSiteSupportIframe());
        site.setCategoryId(actionForm.getCategoryId());

        PortalService portalService = ServiceProvider.getPortalService(requestContext);

        // Update site
        ActionMessages errors = portalService.updateSite(site);
        if (!errors.isEmpty()) {
            saveActionErrors(errors);
            return redirect(AppPaths.PORTAL_SITE_EDIT + "?siteId=" + site.getId() + "&" + RequestContext.URL_PARAM_ERROR_TRUE);
        } else {
            return redirect(AppPaths.PORTAL_SITE_DETAIL + "?siteId=" + site.getId());
        }
    }
}
