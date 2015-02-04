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

import com.kwoksys.action.common.template.ActionErrorsTemplate;
import com.kwoksys.action.common.template.HeaderTemplate;
import com.kwoksys.action.common.template.StandardTemplate;
import com.kwoksys.biz.ServiceProvider;
import com.kwoksys.biz.portal.PortalService;
import com.kwoksys.biz.portal.dto.Site;
import com.kwoksys.biz.system.core.AppPaths;
import com.kwoksys.biz.system.core.Links;
import com.kwoksys.framework.properties.Localizer;
import com.kwoksys.framework.struts2.Action2;
import com.kwoksys.framework.ui.WidgetUtils;

/**
 * Action class for editing site.
 */
public class SiteEditAction extends Action2 {

    public String execute() throws Exception {
        PortalService portalService = ServiceProvider.getPortalService(requestContext);
        SiteForm actionForm = (SiteForm) getBaseForm(SiteForm.class);

        Site site = portalService.getSite(actionForm.getSiteId());

        // If not a resubmit, set some defaults
        if (!actionForm.isResubmit()) {
            actionForm.setSite(site);
        }

        //
        // Template: StandardTemplate
        //
        StandardTemplate standardTemplate = new StandardTemplate(requestContext);
        standardTemplate.setPathAttribute("formAction", AppPaths.PORTAL_SITE_EDIT_2);
        standardTemplate.setAttribute("formCancelLink", Links.getCancelLink(requestContext, AppPaths.PORTAL_SITE_DETAIL + "?siteId=" + actionForm.getSiteId()).getString());
        standardTemplate.setAttribute("siteId", actionForm.getSiteId());
        standardTemplate.setAttribute("optionSitePlacement", Site.getSitePlacementList(requestContext));
        standardTemplate.setAttribute("optionSiteSupportIframe", WidgetUtils.getYesNoOptions(requestContext));
        standardTemplate.setAttribute("optionCategories", Site.getSiteCategories(requestContext));

        //
        // Template: HeaderTemplate
        //
        HeaderTemplate headerTemplate = standardTemplate.getHeaderTemplate();
        headerTemplate.setTitleKey("admin.portalSiteEdit.title");

        //
        // Template: ActionErrorsTemplate
        //
        ActionErrorsTemplate errorsTemplate = new ActionErrorsTemplate();
        standardTemplate.addTemplate(errorsTemplate);
        errorsTemplate.setShowRequiredFieldMsg(true);
        errorsTemplate.setMessage(Localizer.getText(requestContext, "admin.portalSiteEdit.sectionHeader"));

        return standardTemplate.findTemplate(STANDARD_TEMPLATE);
    }
}
