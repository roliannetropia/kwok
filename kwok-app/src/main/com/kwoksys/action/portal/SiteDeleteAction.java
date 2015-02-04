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

import com.kwoksys.action.common.template.HeaderTemplate;
import com.kwoksys.action.common.template.ObjectDeleteTemplate;
import com.kwoksys.action.common.template.StandardV2Template;
import com.kwoksys.biz.ServiceProvider;
import com.kwoksys.biz.portal.PortalService;
import com.kwoksys.biz.portal.dto.Site;
import com.kwoksys.biz.system.core.AppPaths;
import com.kwoksys.framework.struts2.Action2;

/**
 * Action class for deleting site.
 */
public class SiteDeleteAction extends Action2 {

    public String execute() throws Exception {
        PortalService portalService = ServiceProvider.getPortalService(requestContext);

        Integer siteId = requestContext.getParameter("siteId");
        Site site = portalService.getSite(siteId);

        //
        // Template: StandardV2Template
        //
        StandardV2Template standardTemplate = new StandardV2Template(requestContext);

        //
        // Template: HeaderTemplate
        //
        HeaderTemplate header = standardTemplate.getHeaderTemplate();
        header.setPageTitleKey("admin.portalSiteDelete.title");

        //
        // Template: SiteSpecTemplate
        //
        SiteSpecTemplate tmpl = new SiteSpecTemplate(site);
        standardTemplate.addTemplate(tmpl);

        //
        // Template: ObjectDeleteTemplate
        //
        ObjectDeleteTemplate delete = new ObjectDeleteTemplate();
        standardTemplate.addTemplate(delete);
        delete.setFormAction(AppPaths.PORTAL_SITE_DELETE_2 + "?siteId=" + siteId);
        delete.setFormCancelAction(AppPaths.PORTAL_SITE_DETAIL + "?siteId=" + siteId);
        delete.setConfirmationMsgKey("admin.portalSiteDelete.confirm");
        delete.setSubmitButtonKey("admin.portalSiteDelete.submitButton");

        return standardTemplate.findTemplate(STANDARD_TEMPLATE);
    }
}
