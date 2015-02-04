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
import com.kwoksys.action.common.template.StandardTemplate;
import com.kwoksys.action.common.template.TableEmptyTemplate;
import com.kwoksys.biz.ServiceProvider;
import com.kwoksys.biz.admin.dto.AccessUser;
import com.kwoksys.biz.auth.core.Access;
import com.kwoksys.biz.portal.PortalService;
import com.kwoksys.biz.portal.SiteSearch;
import com.kwoksys.biz.portal.dao.PortalQueries;
import com.kwoksys.biz.portal.dto.Site;
import com.kwoksys.biz.system.core.AppPaths;
import com.kwoksys.biz.system.core.Image;
import com.kwoksys.framework.connections.database.QueryBits;
import com.kwoksys.framework.properties.Localizer;
import com.kwoksys.framework.struts2.Action2;
import com.kwoksys.framework.ui.Link;
import com.kwoksys.framework.util.HtmlUtils;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Action class for showing portal sites.
 */
public class SiteListIndexAction extends Action2 {

    public String execute() throws Exception {
        AccessUser user = requestContext.getUser();

        // Create search criteria map
        SiteSearch siteSearch = new SiteSearch();
        siteSearch.put(SiteSearch.SHOW_ON_LIST, "");

        QueryBits query = new QueryBits(siteSearch);
        query.addSortColumn(PortalQueries.getOrderByColumn(Site.CATEGORY_NAME));
        query.addSortColumn(PortalQueries.getOrderByColumn(Site.SITE_NAME));

        PortalService portalService = ServiceProvider.getPortalService(requestContext);
        List<Site> sites = portalService.getSites(query);

        //
        // Template: StandardTemplate
        //
        StandardTemplate standardTemplate = new StandardTemplate(requestContext);
        standardTemplate.setAttribute("externalPopupImage", Image.getInstance().getExternalPopupIcon());

        if (!sites.isEmpty()) {
            //List siteList = new ArrayList();
            // A map with categoryId as key, site list matching categoryId as value
            Map categoriesMap = new HashMap();

            for (Site site : sites) {
                Map siteMap = new HashMap();
                // Since not web applications support being put in iframe, we'll need to
                // handle this differently.
                if (site.getSupportIframe() == 1) {
                    siteMap.put("appFrame", "iframe");
                } else {
                    siteMap.put("appFrame", "popup");
                }
                siteMap.put("sitePath", HtmlUtils.encode(site.getPath()));
                siteMap.put("siteName", site.getName());

                Map categoryMap = new HashMap();
                categoryMap.put(site.getCategoryId(), site.getCategoryName());

                if (categoriesMap.get(categoryMap) == null) {
                    List list = new ArrayList();
                    list.add(siteMap);
                    categoriesMap.put(categoryMap, list);
                } else {
                    ((List)categoriesMap.get(categoryMap)).add(siteMap);
                }
            }
            standardTemplate.setAttribute("dataList", categoriesMap);
        } else {
            //
            // Template: TableEmptyTemplate
            //
            TableEmptyTemplate empty = new TableEmptyTemplate();
            standardTemplate.addTemplate(empty);
            empty.setColSpan(1);
            empty.setRowText(Localizer.getText(requestContext, "portal.siteList.emptyTableMessage"));
        }

        //
        // Template: HeaderTemplate
        //
        HeaderTemplate header = standardTemplate.getHeaderTemplate();

        // Manage Websites
        if (Access.hasPermission(user, AppPaths.PORTAL_SITE_LIST)) {
            Link link = new Link(requestContext);
            link.setAjaxPath(AppPaths.PORTAL_SITE_LIST);
            link.setTitleKey("admin.index.portalSiteList");
            header.addHeaderCmds(link);
        }

        // Manage categories
        if (Access.hasPermission(user, AppPaths.PORTAL_CATEGORY_LIST)) {
            Link link = new Link(requestContext);
            link.setAjaxPath(AppPaths.PORTAL_CATEGORY_LIST);
            link.setTitleKey("portal.siteCategoryList.header");
            header.addHeaderCmds(link);
        }
        // Website rss.
        if (Access.hasPermission(user, AppPaths.PORTAL_SITE_LIST_RSS)) {
            Link link = new Link(requestContext);
            link.setAppPath(AppPaths.PORTAL_SITE_LIST_RSS);
            link.setTitleKey("portal.cmd.rssFeedSubscribe");
            link.setImgSrc(Image.getInstance().getRssFeedIcon());
            header.addHeaderCmds(link);
        }

        return standardTemplate.findTemplate(STANDARD_TEMPLATE);
    }
}
