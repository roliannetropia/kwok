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
import com.kwoksys.biz.system.core.AppPaths;
import com.kwoksys.framework.http.RequestContext;
import com.kwoksys.framework.struts2.Action2;
import org.apache.struts.action.ActionMessages;

/**
 * Action class for deleting site.
 */
public class SiteDelete2Action extends Action2 {

    public String execute() throws Exception {
        Integer siteId = requestContext.getParameter("siteId");

        PortalService portalService = ServiceProvider.getPortalService(requestContext);
        portalService.getSite(siteId);

        ActionMessages errors = portalService.deleteSite(siteId);
        if (!errors.isEmpty()) {
            saveActionErrors(errors);
            return redirect(AppPaths.PORTAL_SITE_DELETE + "?" + RequestContext.URL_PARAM_ERROR_TRUE + "&siteId=" + siteId);
        } else {
            return redirect(AppPaths.PORTAL_SITE_LIST);
        }
    }
}