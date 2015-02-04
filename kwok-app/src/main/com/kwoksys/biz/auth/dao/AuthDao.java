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
package com.kwoksys.biz.auth.dao;

import com.kwoksys.biz.admin.dto.AccessUser;
import com.kwoksys.framework.http.RequestContext;
import com.kwoksys.framework.exceptions.UsernameNotFoundException;
import com.kwoksys.biz.auth.dto.AccessPage;
import com.kwoksys.biz.base.BaseDao;
import com.kwoksys.framework.connections.database.QueryHelper;
import com.kwoksys.framework.exceptions.DatabaseException;
import com.kwoksys.framework.util.StringUtils;
import com.kwoksys.framework.util.DatetimeUtils;
import com.kwoksys.biz.system.core.configs.ConfigManager;
import org.apache.struts.action.ActionMessages;

import java.sql.*;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

/**
 * AuthDao.
 */
public class AuthDao extends BaseDao {

    public AuthDao(RequestContext requestContext) {
        super(requestContext);
    }

    /**
     * Put pages in permissions map
     *
     * @return ..
     */
    public Map<Integer, Set> getAccessPermPages() throws DatabaseException {
        Connection conn = getConnection();

        QueryHelper queryHelper = new QueryHelper(AuthQueries.selectPermPages());

        try {
            Map<Integer, Set> permMap = new HashMap();

            ResultSet rs = queryHelper.executeQuery(conn);
            while (rs.next()) {
                Integer permId = rs.getInt("perm_id");
                Integer pageId = rs.getInt("page_id");

                Set pageSet = permMap.get(permId);
                if (pageSet == null) {
                    pageSet = new HashSet();
                }
                pageSet.add(pageId);
                permMap.put(permId, pageSet);
            }
            return permMap;

        } catch (Exception e) {
            // Database problem
            throw new DatabaseException(e, queryHelper);

        } finally {
            queryHelper.closeRs();
            closeConnection(conn);
        }
    }

    public Set<Integer> getAccessGroupPerms(Integer groupId) throws DatabaseException {
        Connection conn = getConnection();

        QueryHelper queryHelper = new QueryHelper(AuthQueries.selectGroupPerms());
        queryHelper.addInputInt(groupId);

        try {
            Set<Integer> permSet = new HashSet();

            ResultSet rs = queryHelper.executeQuery(conn);
            while (rs.next()) {
                permSet.add(rs.getInt("perm_id"));
            }
            return permSet;

        } catch (Exception e) {
            // Database problem
            throw new DatabaseException(e, queryHelper);

        } finally {
            queryHelper.closeRs();
            closeConnection(conn);
        }
    }

    public Set<Integer> getAccessUserPerms(Integer userId) throws DatabaseException {
        Connection conn = getConnection();

        QueryHelper queryHelper = new QueryHelper(AuthQueries.selectUserPerms());
        queryHelper.addInputInt(userId);

        try {
            Set<Integer> permSet = new HashSet();

            ResultSet rs = queryHelper.executeQuery(conn);
            while (rs.next()) {
                permSet.add(rs.getInt("perm_id"));
            }
            return permSet;

        } catch (Exception e) {
            // Database problem
            throw new DatabaseException(e, queryHelper);

        } finally {
            queryHelper.closeRs();
            closeConnection(conn);
        }
    }

    /**
     * Put all page names and ids in HashMap.
     *
     * @return ..
     */
    public Map getAccessPages() throws DatabaseException {
        Connection conn = getConnection();

        QueryHelper queryHelper = new QueryHelper(AuthQueries.selectAccessPages());

        try {
            Map map = new HashMap();

            ResultSet rs = queryHelper.executeQuery(conn);
            while (rs.next()) {
                // Add each column to the map
                AccessPage page = new AccessPage();
                page.setPageId(rs.getInt("page_id"));
                page.setModuleId(rs.getInt("module_id"));
                page.setPath(rs.getString("page_name") + ConfigManager.system.getExtension());
                map.put(page.getPath(), page);
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

    /**
     * Update User session when the user successfully logged on to the system.
     *
     * @param userId
     * @param sessionToken
     * @return ..
     */
    public ActionMessages updateUserLogonSession(AccessUser accessUser, String sessionToken) throws DatabaseException {
        Connection conn = getConnection();

        QueryHelper queryHelper = new QueryHelper(AuthQueries.updateUserLogonSessionQuery());
        queryHelper.addInputStringConvertNull(sessionToken);
        queryHelper.addInputInt(accessUser.getId());

        try {
            queryHelper.executeProcedure(conn);

            accessUser.setSessionToken(sessionToken);

        } catch (Exception e) {
            // Database problem
            handleError(e);

        } finally {
            closeConnection(conn);
        }
        return errors;
    }

    public ActionMessages updateUserInvalidLogon(AccessUser accessUser, int invalidLogonCount) throws DatabaseException {
        QueryHelper queryHelper = new QueryHelper(AuthQueries.updateUserInvalidLogonQuery());
        queryHelper.addInputInt(accessUser.getId());
        queryHelper.addInputInt(invalidLogonCount);

        return executeProcedure(queryHelper);
    }

    /**
     * Update User session when the user successfully logged out of the system.
     *
     * @param userId
     * @return ..
     */
    public ActionMessages updateUserLogoutSession(Integer userId) throws DatabaseException {
        QueryHelper queryHelper = new QueryHelper(AuthQueries.updateUserLogoutSessionQuery());
        queryHelper.addInputInt(userId);

        return executeProcedure(queryHelper);
    }

    /**
     * @param userId
     * @param sessionTokenCookie
     * @return the number of users match, or 0 if no record is found.
     */
    public boolean isValidUserSession(Integer userId, String sessionTokenCookie) throws DatabaseException {
        Connection conn = getConnection();

        QueryHelper queryHelper = new QueryHelper(AuthQueries.validateUserSessionQuery());
        queryHelper.addOutputParam(Types.INTEGER);
        queryHelper.addInputInt(userId);
        queryHelper.addInputString(sessionTokenCookie);
        queryHelper.addInputInt(ConfigManager.auth.getSessionTimeoutSeconds());

        try {
            queryHelper.executeProcedure(conn);

            return ((Integer) queryHelper.getSqlOutputs().get(0) != 0);

        } catch (Exception e) {
            // Database problem
            handleError(e);

            throw new DatabaseException(e);

        } finally {
            closeConnection(conn);
        }
    }

    public ActionMessages isValidUsername(AccessUser user) throws DatabaseException, UsernameNotFoundException {
        Connection conn = getConnection();

        QueryHelper queryHelper = new QueryHelper(AuthQueries.selectValidUsernameQuery());
        queryHelper.addInputString(user.getUsername());

        try {
            ResultSet rs = queryHelper.executeQuery(conn);

            if (rs.next()) {
                user.setId(rs.getInt("user_id"));
                user.setHashedPassword(StringUtils.replaceNull(rs.getString("password")));
                user.setStatus(rs.getInt("status"));
                user.setInvalidLogonCount(rs.getInt("invalid_logon_count"));
                user.setInvalidLogonDate(DatetimeUtils.getDate(rs, "invalid_logon_date"));
                return errors;
            }        
        } catch (Exception e) {
            // Database problem
            throw new DatabaseException(e, queryHelper);

        } finally {
            queryHelper.closeRs();
            closeConnection(conn);
        }
        throw new UsernameNotFoundException();
    }
}