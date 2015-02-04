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
package com.kwoksys.biz.system.dao;

import com.kwoksys.biz.system.dto.linking.ObjectLink;
import com.kwoksys.biz.system.dto.SystemInfo;
import com.kwoksys.biz.base.BaseDao;
import com.kwoksys.framework.http.RequestContext;
import com.kwoksys.framework.connections.database.QueryHelper;
import com.kwoksys.framework.exceptions.DatabaseException;
import com.kwoksys.framework.util.StringUtils;
import com.kwoksys.framework.util.DatetimeUtils;
import com.kwoksys.biz.system.core.configs.LogConfigManager;
import org.apache.struts.action.ActionMessages;

import java.sql.Connection;
import java.sql.DatabaseMetaData;
import java.sql.ResultSet;
import java.util.*;
import java.util.logging.Logger;
import java.util.logging.Level;

/**
 * SystemDao
 */
public class SystemDao extends BaseDao {

    public SystemDao(RequestContext requestContext) {
        super(requestContext);
    }

    private static final Logger logger = Logger.getLogger(SystemDao.class.getName());

    public Map getDatabaseInfo() throws DatabaseException {
        Connection conn = getConnection();

        Map map = new HashMap();

        try {
            DatabaseMetaData meta = conn.getMetaData();
            map.put("DatabaseProductName", meta.getDatabaseProductName());
            map.put("DatabaseProductVersion", meta.getDatabaseProductVersion());

        } catch (Exception e) {
            // This doesn't go through QueryHelper. Don't know how to log errors.
            logger.log(Level.SEVERE, LogConfigManager.DATABASE_ACCESS_PREFIX + " Exception: " + e.getMessage());

        } finally {
            closeConnection(conn);
        }
        return map;
    }

    public SystemInfo getSystemInfo() throws DatabaseException {
        Connection conn = getConnection();

        QueryHelper queryHelper = new QueryHelper(SystemQueries.selectSystemValues());

        try {
            ResultSet rs = queryHelper.executeQuery(conn);
            rs.next();

            SystemInfo systemInfo = new SystemInfo();
            systemInfo.setCacheKey(StringUtils.replaceNull(rs.getString("cache_key")));
            systemInfo.setSysdate(DatetimeUtils.getDate(rs, "sysdate"));
            return systemInfo;

        } catch (Exception e) {
            // Database problem
            throw new DatabaseException(e, queryHelper);

        } finally {
            queryHelper.closeRs();
            closeConnection(conn);
        }
    }

    /**
     * Gets a list of cache keys that need to be flushed
     * @return
     * @throws DatabaseException
     */
    public List<String> getFlushSystemCacheKeys(Long cacheTime) throws DatabaseException {
        Connection conn = getConnection();

        QueryHelper queryHelper = new QueryHelper(SystemQueries.selectFlushSystemCaches());
        queryHelper.addInputLong(cacheTime);

        try {
            List<String> cacheKeys = new ArrayList();

            ResultSet rs = queryHelper.executeQuery(conn);
            while (rs.next()) {
                cacheKeys.add(rs.getString("cache_key"));
            }
            return cacheKeys;

        } catch (Exception e) {
            // Database problem
            throw new DatabaseException(e, queryHelper);

        } finally {
            queryHelper.closeRs();
            closeConnection(conn);
        }
    }

    public Map getSystemConfig() throws DatabaseException {
        Connection conn = getConnection();

        QueryHelper queryHelper = new QueryHelper(SystemQueries.selectSystemConfig());

        try {
            Map map = new HashMap();

            ResultSet rs = queryHelper.executeQuery(conn);
            while (rs.next()) {
                // Add each column to the map
                map.put(rs.getString("config_key"), StringUtils.replaceNull(rs.getString("config_value")));
            }
            return map;

        } catch (Exception e) {
            // Database problem
            throw new DatabaseException(e, queryHelper);

        } finally {
            queryHelper.closeRs();
            closeConnection(conn);
        }
    }

    public int getObjectMapCount(List objectTypeIds, Integer linkedObjectId, List linkedObjectTypeIds) throws DatabaseException {
        String objectTypeIdOption = StringUtils.join(objectTypeIds, ",");
        String linkedTypeIdOption = StringUtils.join(linkedObjectTypeIds, ",");
        String sqlQuery = SystemQueries.selectObjectMapCount(objectTypeIdOption, linkedTypeIdOption);

        Connection conn = getConnection();

        QueryHelper queryHelper = new QueryHelper(sqlQuery);
        queryHelper.addInputInt(linkedObjectId);

        try {
            ResultSet rs = queryHelper.executeQuery(conn);
            rs.next();
            return rs.getInt("obj_count");

        } catch (Exception e) {
            // Database problem
            throw new DatabaseException(e, queryHelper);

        } finally {
            queryHelper.closeRs();
            closeConnection(conn);
        }
    }

    public int getLinkedObjectMapCount(List linkedObjectTypeIds, Integer objectId, Integer objectTypeId) throws DatabaseException {
        String linkedTypeIdOption = StringUtils.join(linkedObjectTypeIds, ",");

        Connection conn = getConnection();

        QueryHelper queryHelper = new QueryHelper(SystemQueries.selectLinkedObjectMapCount(linkedTypeIdOption));
        queryHelper.addInputInt(objectId);
        queryHelper.addInputInt(objectTypeId);

        try {
            ResultSet rs = queryHelper.executeQuery(conn);
            rs.next();
            return rs.getInt("obj_count");

        } catch (Exception e) {
            // Database problem
            throw new DatabaseException(e, queryHelper);

        } finally {
            queryHelper.closeRs();
            closeConnection(conn);
        }
    }

    public ActionMessages validateSystemCaches(Long cacheTime) throws DatabaseException {
        QueryHelper queryHelper = new QueryHelper(SystemQueries.validateSystemCaches());
        queryHelper.addInputLong(cacheTime);

        return executeProcedure(queryHelper);
    }

    public ActionMessages resetSystemCache(String cacheKey, Long cacheTime) throws DatabaseException {
        QueryHelper queryHelper = new QueryHelper(SystemQueries.updateSystemCache());
        queryHelper.addInputString(cacheKey);
        queryHelper.addInputLong(cacheTime);

        return executeProcedure(queryHelper);
    }

    /**
     * Add a object mapping
     */
    public ActionMessages addObjectMapping(ObjectLink objectMap) throws DatabaseException {
        QueryHelper queryHelper = new QueryHelper(SystemQueries.insertObjectMapQuery());
        queryHelper.addInputInt(objectMap.getObjectTypeId());
        queryHelper.addInputInt(objectMap.getObjectId());
        queryHelper.addInputInt(objectMap.getLinkedObjectTypeId());
        queryHelper.addInputInt(objectMap.getLinkedObjectId());
        queryHelper.addInputInt(requestContext.getUser().getId());
        queryHelper.addInputStringConvertNull(objectMap.getRelDescription());

        return executeProcedure(queryHelper);
    }

    /**
     * Delete a object mapping.
     *
     * @return ..
     */
    public ActionMessages deleteObjectMapping(ObjectLink objectMap) throws DatabaseException {
        if (objectMap.getLinkedObjectId() == 0) {
            return errors;
        }
        
        QueryHelper queryHelper = new QueryHelper(SystemQueries.deleteObjectMapQuery());
        queryHelper.addInputInt(objectMap.getObjectTypeId());
        queryHelper.addInputInt(objectMap.getObjectId());
        queryHelper.addInputInt(objectMap.getLinkedObjectTypeId());
        queryHelper.addInputInt(objectMap.getLinkedObjectId());

        return executeProcedure(queryHelper);
    }
}
