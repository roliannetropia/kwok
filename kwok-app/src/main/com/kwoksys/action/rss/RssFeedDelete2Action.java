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

import com.kwoksys.biz.ServiceProvider;
import com.kwoksys.biz.rss.RssService;
import com.kwoksys.biz.system.core.AppPaths;
import com.kwoksys.framework.http.RequestContext;
import com.kwoksys.framework.struts2.Action2;
import org.apache.struts.action.ActionMessages;

/**
 * Action class for deleting rss feed.
 */
public class RssFeedDelete2Action extends Action2 {

    public String execute() throws Exception {
        Integer feedId = requestContext.getParameter("feedId");

        RssService rssService = ServiceProvider.getRssService(requestContext);
        rssService.getRssFeed(feedId);

        ActionMessages errors = rssService.deleteRssFeed(feedId);
        if (!errors.isEmpty()) {
            saveActionErrors(errors);
            return redirect(AppPaths.RSS_FEED_DELETE + "?" + RequestContext.URL_PARAM_ERROR_TRUE + "&feedId=" + feedId);
        } else {
            return redirect(AppPaths.RSS_FEED_LIST + "?feedId=" + feedId);
        }
    }
}
