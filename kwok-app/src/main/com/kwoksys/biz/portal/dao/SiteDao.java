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
package com.kwoksys.biz.portal.dao;

import com.kwoksys.biz.portal.dto.Site;
import com.kwoksys.biz.admin.dto.AccessUser;
import com.kwoksys.biz.base.BaseDao;
import com.kwoksys.framework.http.RequestContext;
import com.kwoksys.framework.connections.database.QueryBits;
import com.kwoksys.framework.connections.database.QueryHelper;
import com.kwoksys.framework.exceptions.DatabaseException;
import com.kwoksys.framework.exceptions.ObjectNotFoundException;
import com.kwoksys.framework.util.DatetimeUtils;
import com.kwoksys.framework.util.StringUtils;
import org.apache.struts.action.ActionMessages;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

/**
 * SiteDao.
 */
public class SiteDao extends BaseDao {

    public SiteDao(RequestContext requestContext) {
        super(requestContext);
    }

    /**
     * This is to return a list of Portal Site.
     *
     * @param query
     * @return .. ..
     */
    public List<Site> getSiteList(QueryBits query) throws DatabaseException {
        Connection conn = getConnection();

        QueryHelper queryHelper = new QueryHelper(PortalQueries.selectSiteListQuery(query));

        try {
            List sites = new ArrayList();
            ResultSet rs = queryHelper.executeQuery(conn);

            while (rs.next()) {
                Site site = new Site();
                site.setId(rs.getInt("site_id"));
                site.setName(StringUtils.replaceNull(rs.getString("site_name")));
                site.setPath(StringUtils.replaceNull(rs.getString("site_path")));
                site.setPlacement(rs.getInt("site_placement"));
                site.setSupportIframe(rs.getInt("site_support_iframe"));
                site.setCategoryId(rs.getInt("category_id"));
                site.setCategoryName(StringUtils.replaceNull(rs.getString("category_name")));

                sites.add(site);
            }
            return sites;

        } catch (Exception e) {
            // Database problem
            throw new DatabaseException(e, queryHelper);

        } finally {
            queryHelper.closeRs();
            closeConnection(conn);
        }
    }

    /**
     * Get Portal Sites count.
     *
     * @param query
     * @return ..
     */
    public int getCount(QueryBits query) throws DatabaseException {
        return getRowCount(PortalQueries.selectSiteCountQuery(query));
    }

    /**
     * Get website detail.
     * @param siteId
     * @return
     * @throws DatabaseException
     * @throws ObjectNotFoundException
     */
    public Site getSite(Integer siteId) throws DatabaseException, ObjectNotFoundException {
        Connection conn = getConnection();

        QueryHelper queryHelper = new QueryHelper(PortalQueries.selectSiteDetailQuery());
        queryHelper.addInputInt(siteId);

        try {
            ResultSet rs = queryHelper.executeQuery(conn);
            if (rs.next()) {
                Site site = new Site();
                site.setId(rs.getInt("site_id"));
                site.setName(StringUtils.replaceNull(rs.getString("site_name")));
                site.setPath(StringUtils.replaceNull(rs.getString("site_path")));
                site.setDescription(StringUtils.replaceNull(rs.getString("site_description")));
                site.setPlacement(rs.getInt("site_placement"));
                site.setSupportIframe(rs.getInt("site_support_iframe"));
                site.setCategoryId(rs.getInt("category_id"));
                site.setCategoryName(StringUtils.replaceNull(rs.getString("category_name")));
                site.setCreationDate(DatetimeUtils.getDate(rs, "creation_date"));
                site.setModificationDate(DatetimeUtils.getDate(rs, "modification_date"));

                site.setCreator(new AccessUser());
                site.getCreator().setId(rs.getInt("creator"));
                site.getCreator().setUsername(rs.getString("creator_username"));
                site.getCreator().setDisplayName(rs.getString("creator_display_name"));

                site.setModifier(new AccessUser());
                site.getModifier().setId(rs.getInt("modifier"));
                site.getModifier().setUsername(rs.getString("modifier_username"));
                site.getModifier().setDisplayName(rs.getString("modifier_display_name"));
                return site;
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
     * Add Portal Site.
     *
     * @return ..
     */
    public ActionMessages add(Site site) throws DatabaseException {
        QueryHelper queryHelper = new QueryHelper(PortalQueries.insertSiteQuery());
        queryHelper.addOutputParam(Types.INTEGER);
        queryHelper.addInputStringConvertNull(site.getName());
        queryHelper.addInputStringConvertNull(site.getPath());
        queryHelper.addInputStringConvertNull(site.getDescription());
        queryHelper.addInputInt(site.getPlacement());
        queryHelper.addInputInt(site.getSupportIframe());
        queryHelper.addInputInt(site.getCategoryId());
        queryHelper.addInputInt(requestContext.getUser().getId());

        executeProcedure(queryHelper);

        // Put some values in the result
        if (errors.isEmpty()) {
            site.setId((Integer)queryHelper.getSqlOutputs().get(0));
        }

        return errors;
    }

    /**
     * Update Portal Site.
     *
     * @return ..
     */
    public ActionMessages update(Site site) throws DatabaseException {
        QueryHelper queryHelper = new QueryHelper(PortalQueries.updateSiteQuery());
        queryHelper.addInputInt(site.getId());
        queryHelper.addInputStringConvertNull(site.getName());
        queryHelper.addInputStringConvertNull(site.getPath());
        queryHelper.addInputStringConvertNull(site.getDescription());
        queryHelper.addInputInt(site.getPlacement());
        queryHelper.addInputInt(site.getSupportIframe());
        queryHelper.addInputInt(site.getCategoryId());
        queryHelper.addInputInt(requestContext.getUser().getId());

        return executeProcedure(queryHelper);
    }

    /**
     * Delete Portal Site.
     *
     * @return ..
     */
    public ActionMessages delete(Integer siteId) throws DatabaseException {
        QueryHelper queryHelper = new QueryHelper(PortalQueries.deleteSiteQuery());
        queryHelper.addInputInt(siteId);

        return executeProcedure(queryHelper);
    }
}
