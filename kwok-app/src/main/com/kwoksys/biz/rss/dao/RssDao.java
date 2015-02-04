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
package com.kwoksys.biz.rss.dao;

import com.kwoksys.biz.base.BaseDao;
import com.kwoksys.biz.rss.dto.RssFeed;
import com.kwoksys.framework.http.RequestContext;
import com.kwoksys.framework.connections.database.QueryBits;
import com.kwoksys.framework.connections.database.QueryHelper;
import com.kwoksys.framework.exceptions.DatabaseException;
import com.kwoksys.framework.exceptions.ObjectNotFoundException;
import com.kwoksys.framework.util.DatetimeUtils;
import com.kwoksys.framework.util.StringUtils;
import org.apache.struts.action.ActionMessages;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.Types;
import java.util.ArrayList;
import java.util.List;

/**
 * RssDao
 */
public class RssDao extends BaseDao {

    public RssDao(RequestContext requestContext) {
        super(requestContext);
    }

    /**
     * Gets a list of RSS Feeds.
     *
     * @param query
     * @return ..
     */
    public List<RssFeed> getRssFeedList(QueryBits query) throws DatabaseException {
        Connection conn = getConnection();

        QueryHelper queryHelper = new QueryHelper(RssQueries.selectRssFeedListQuery());

        try {
            List rssFeeds = new ArrayList();
            ResultSet rs = queryHelper.executeQuery(conn);

            while (rs.next()) {
                RssFeed rssFeed = new RssFeed();
                rssFeed.setId(rs.getInt("feed_id"));
                rssFeed.setName(StringUtils.replaceNull(rs.getString("feed_name")));
                rssFeed.setUrl(StringUtils.replaceNull(rs.getString("feed_url")));
                rssFeed.setCache(StringUtils.replaceNull(rs.getString("feed_cache")));
                rssFeed.setItemCount(rs.getInt("feed_item_count"));

                rssFeeds.add(rssFeed);
            }
            return rssFeeds;

        } catch (Exception e) {
            // Database problem
            throw new DatabaseException(e, queryHelper);

        } finally {
            queryHelper.closeRs();
            closeConnection(conn);
        }
    }

    /**
     * Gets a RSS Feed.
     * @param feedId
     * @return
     * @throws DatabaseException
     * @throws ObjectNotFoundException
     */
    public RssFeed getRssFeed(Integer feedId) throws DatabaseException, ObjectNotFoundException {
        Connection conn = getConnection();

        QueryHelper queryHelper = new QueryHelper(RssQueries.selectRssFeedDetailQuery());
        queryHelper.addInputInt(feedId);

        try {
            ResultSet rs = queryHelper.executeQuery(conn);

            if (rs.next()) {
                RssFeed rssFeed = new RssFeed();
                rssFeed.setId(rs.getInt("feed_id"));
                rssFeed.setName(StringUtils.replaceNull(rs.getString("feed_name")));
                rssFeed.setUrl(StringUtils.replaceNull(rs.getString("feed_url")));
                rssFeed.setCache(StringUtils.replaceNull(rs.getString("feed_cache")));
                rssFeed.setCacheDate(DatetimeUtils.getDate(rs, "feed_cache_date"));
                rssFeed.setItemCount(rs.getInt("feed_item_count"));

                return rssFeed;
            }
        } catch (Exception e) {
            // Database problem
            throw new DatabaseException(e, queryHelper);

        } finally {
            queryHelper.closeRs();
            closeConnection(conn);
        }
        throw new ObjectNotFoundException();
    }

    /**
     * Adds RSS Feed.
     *
     * @return ..
     */
    public ActionMessages add(RssFeed rssFeed) throws DatabaseException {
        QueryHelper queryHelper = new QueryHelper(RssQueries.insertRssFeedQuery());
        queryHelper.addOutputParam(Types.INTEGER);
        queryHelper.addInputStringConvertNull(rssFeed.getUrl());
        queryHelper.addInputStringConvertNull(rssFeed.getName());
        queryHelper.addInputInt(rssFeed.getItemCount());
        queryHelper.addInputStringConvertNull(rssFeed.getCache());
        queryHelper.addInputInt(requestContext.getUser().getId());

        executeProcedure(queryHelper);

        // Put some values in the result
        if (errors.isEmpty()) {
            rssFeed.setId((Integer) queryHelper.getSqlOutputs().get(0));
        }

        return errors;
    }

    /**
     * Updates RSS Feed.
     *
     * @return ..
     */
    public ActionMessages update(RssFeed rssFeed) throws DatabaseException {
        QueryHelper queryHelper = new QueryHelper(RssQueries.updateRssFeedQuery());
        queryHelper.addInputInt(rssFeed.getId());
        queryHelper.addInputStringConvertNull(rssFeed.getUrl());
        queryHelper.addInputStringConvertNull(rssFeed.getName());
        queryHelper.addInputInt(requestContext.getUser().getId());

        return executeProcedure(queryHelper);
    }

    /**
     * Updates RSS Feed.
     *
     * @return ..
     */
    public ActionMessages updateContent(RssFeed rssFeed) throws DatabaseException {
        QueryHelper queryHelper = new QueryHelper(RssQueries.updateRssFeedCacheQuery());
        queryHelper.addInputInt(rssFeed.getId());
        queryHelper.addInputInt(rssFeed.getItemCount());
        queryHelper.addInputStringConvertNull(rssFeed.getCache());

        return executeProcedure(queryHelper);
    }

    /**
     * Deletes RSS Feed.
     *
     * @return ..
     */
    public ActionMessages delete(Integer feedId) throws DatabaseException {
        QueryHelper queryHelper = new QueryHelper(RssQueries.deleteRssFeedQuery());
        queryHelper.addInputInt(feedId);

        return executeProcedure(queryHelper);
    }
}