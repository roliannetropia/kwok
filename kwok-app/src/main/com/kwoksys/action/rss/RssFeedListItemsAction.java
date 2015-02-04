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
package com.kwoksys.action.rss;

import com.kwoksys.action.common.template.StandardTemplate;
import com.kwoksys.biz.ServiceProvider;
import com.kwoksys.biz.admin.dto.AccessUser;
import com.kwoksys.biz.auth.core.Access;
import com.kwoksys.biz.portal.PortalUtils;
import com.kwoksys.biz.rss.RssService;
import com.kwoksys.biz.rss.dto.RssFeed;
import com.kwoksys.biz.system.core.AppPaths;
import com.kwoksys.biz.system.core.configs.ConfigManager;
import com.kwoksys.framework.parsers.rss.RssModel;
import com.kwoksys.framework.parsers.rss.RssModelHelper;
import com.kwoksys.framework.session.SessionManager;
import com.kwoksys.framework.struts2.Action2;
import com.kwoksys.framework.util.HttpUtils;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * Action class for geting rss items
 */
public class RssFeedListItemsAction extends Action2 {

    private static final Logger logger = Logger.getLogger(RssFeedListItemsAction.class.getName());

    public String execute() throws Exception {
        AccessUser user = requestContext.getUser();

        // Get request parameters
        Integer feedId = requestContext.getParameter("feedId");

        RssService rssService = ServiceProvider.getRssService(requestContext);
        RssFeed rssFeed = rssService.getRssFeed(feedId);

        long systemUnixtime = requestContext.getSysdate().getTime();
        long rssUnixtime = rssFeed.getCacheDate().getTime();

        try {
            RssModelHelper helper = new RssModelHelper();

            // If cache is old than specified minutes, refresh the cache
            if (systemUnixtime > (rssUnixtime + (ConfigManager.app.getPortalRssCacheTimeInMinutes())) ||
                    rssFeed.getCache().isEmpty()) {

                helper.xmlToModel(HttpUtils.getContent(rssFeed.getUrl()));

                rssFeed.setModel(helper.getRssModel());
                rssFeed.setItemCount(helper.getRssModel().getCount());
                rssFeed.setCache(helper.getXmlString());

                rssService.updateRssFeedContent(rssFeed);

            } else {
                helper.xmlToModel(rssFeed.getCache());
                rssFeed.setModel(helper.getRssModel());
            }

        } catch (Exception e) {
            logger.log(Level.WARNING, "Problem retrieving RSS feed.", e);

            RssModel rssModel = new RssModel();
            rssModel.setLink(rssFeed.getUrl());
            rssModel.setTitle("Problem retrieving RSS feed...");

            rssFeed.setModel(rssModel);
        }

        List items = new ArrayList();

        for (RssModel.Item item: rssFeed.getModel().getItems()) {
            Map map = new HashMap();
            map.put("title", item.getTitle());
            map.put("description", PortalUtils.formatRssItem(item.getDescription()));
            map.put("link", item.getLink());
            items.add(map);
        }

        //
        // Template: StandardTemplate
        //
        StandardTemplate standardTemplate = new StandardTemplate(requestContext);
        standardTemplate.setAttribute("rssFeed", rssFeed);
        standardTemplate.setAttribute("rssFeedItems", items);
        if (Access.hasPermission(user, AppPaths.RSS_FEED_EDIT)) {
            standardTemplate.setAttribute("editRssFeedPath", AppPaths.RSS_FEED_EDIT + "?feedId=");
        }
        if (Access.hasPermission(user, AppPaths.RSS_FEED_DELETE)) {
            standardTemplate.setAttribute("deleteRssFeedPath", AppPaths.RSS_FEED_DELETE + "?feedId=");
        }
        String sessionTheme = SessionManager.getAppSessionTheme(request.getSession());
        standardTemplate.setAttribute("themeStylePath", AppPaths.getInstance().getThemeCss(sessionTheme));

        return standardTemplate.findTemplate(SUCCESS);
    }
}
