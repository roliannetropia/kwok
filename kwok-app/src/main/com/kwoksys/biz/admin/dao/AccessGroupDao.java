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
package com.kwoksys.biz.admin.dao;

import com.kwoksys.biz.ServiceProvider;
import com.kwoksys.biz.admin.AdminService;
import com.kwoksys.biz.admin.core.AdminUtils;
import com.kwoksys.biz.admin.core.UserSearch;
import com.kwoksys.biz.admin.dto.AccessGroup;
import com.kwoksys.biz.admin.dto.AccessUser;
import com.kwoksys.biz.admin.dto.GroupPermissionMap;
import com.kwoksys.biz.base.BaseDao;
import com.kwoksys.framework.connections.database.QueryBits;
import com.kwoksys.framework.connections.database.QueryHelper;
import com.kwoksys.framework.exceptions.DatabaseException;
import com.kwoksys.framework.exceptions.ObjectNotFoundException;
import com.kwoksys.framework.http.RequestContext;
import com.kwoksys.framework.util.DatetimeUtils;
import com.kwoksys.framework.util.StringUtils;
import org.apache.struts.action.ActionMessages;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.Types;
import java.util.ArrayList;
import java.util.List;

/**
 * AccessGroupDao
 */
public class AccessGroupDao extends BaseDao {

    public AccessGroupDao(RequestContext requestContext) {
        super(requestContext);
    }

    /**
     * Gets user groups.
     *
     * @return ..
     */
    public List<AccessGroup> getGroups(QueryBits query) throws DatabaseException {
        Connection conn = getConnection();

        QueryHelper queryHelper = new QueryHelper(AdminQueries.selectGroupListQuery(query));

        try {
            List groups = new ArrayList();
            ResultSet rs = queryHelper.executeQuery(conn);

            while (rs.next()) {
                AccessGroup group = new AccessGroup();
                group.setId(rs.getInt("group_id"));
                group.setName(StringUtils.replaceNull(rs.getString("group_name")));
                group.setDescription(StringUtils.replaceNull(rs.getString("group_description")));
                groups.add(group);
            }
            return groups;

        } catch (Exception e) {
            // Database problem
            throw new DatabaseException(e, queryHelper);

        } finally {
            queryHelper.closeRs();
            closeConnection(conn);
        }
    }

    /**
     * Get AccessGroup object
     *
     * @param groupId
     * @return ..
     */
    public AccessGroup getGroup(Integer groupId) throws DatabaseException, ObjectNotFoundException {
        Connection conn = getConnection();

        QueryHelper queryHelper = new QueryHelper(AdminQueries.selectGroupDetailQuery());
        queryHelper.addInputInt(groupId);

        try {
            ResultSet rs = queryHelper.executeQuery(conn);

            if (rs.next()) {
                AccessGroup group = new AccessGroup();
                group.setId(rs.getInt("group_id"));
                group.setName(rs.getString("group_name"));
                group.setDescription(rs.getString("group_description"));
                group.setCreationDate(DatetimeUtils.getDate(rs, "creation_date"));
                group.setModificationDate(DatetimeUtils.getDate(rs, "modification_date"));

                group.setCreator(new AccessUser());
                group.getCreator().setId(rs.getInt("creator"));
                group.getCreator().setUsername(rs.getString("creator_username"));
                group.getCreator().setDisplayName(rs.getString("creator_display_name"));

                group.setModifier(new AccessUser());
                group.getModifier().setId(rs.getInt("modifier"));
                group.getModifier().setUsername(rs.getString("modifier_username"));
                group.getModifier().setDisplayName(rs.getString("modifier_display_name"));

                return group;
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
     * Add group.
     *
     * @return ..
     */
    public ActionMessages addGroup(AccessGroup group) throws DatabaseException {
        Connection conn = getConnection();

        QueryHelper queryHelper = new QueryHelper(AdminQueries.addGroupQuery());
        queryHelper.addOutputParam(Types.INTEGER);

        queryHelper.addInputStringConvertNull(group.getName());
        queryHelper.addInputStringConvertNull(group.getDescription());
        queryHelper.addInputInt(requestContext.getUser().getId());

        try {
            queryHelper.executeProcedure(conn);
            // Put some values in the result
            group.setId((Integer)queryHelper.getSqlOutputs().get(0));

            // Update group members
            updateGroupMembers(conn, group);

        } catch (Exception e) {
            // Database problem
            handleError(e);

        } finally {
            closeConnection(conn);
        }
        return errors;
    }

    /**
     * Update group.
     *
     * @return ..
     */
    public ActionMessages editGroup(AccessGroup group) throws DatabaseException {
        Connection conn = getConnection();

        QueryHelper queryHelper = new QueryHelper(AdminQueries.updateGroupQuery());
        queryHelper.addInputInt(group.getId());
        queryHelper.addInputStringConvertNull(group.getName());
        queryHelper.addInputStringConvertNull(group.getDescription());
        queryHelper.addInputInt(requestContext.getUser().getId());

        try {
            queryHelper.executeProcedure(conn);

            // Update group members
            updateGroupMembers(conn, group);

        } catch (Exception e) {
            // Database problem
            handleError(e);

        } finally {
            closeConnection(conn);
        }
        return errors;
    }

    public ActionMessages deleteGroup(AccessGroup group) throws DatabaseException {
        QueryHelper queryHelper = new QueryHelper(AdminQueries.deleteGroupQuery());
        queryHelper.addInputInt(group.getId());

        return executeProcedure(queryHelper);
    }

    public List<GroupPermissionMap> getGroupAccess(QueryBits query, Integer groupId) throws DatabaseException {
        Connection conn = getConnection();

        QueryHelper queryHelper = new QueryHelper(AdminQueries.selectGroupAccessQuery(query));
        queryHelper.addInputInt(groupId);

        try {
            List access = new ArrayList();
            ResultSet rs = queryHelper.executeQuery(conn);

            while (rs.next()) {
                GroupPermissionMap groupperm = new GroupPermissionMap();
                groupperm.setGroupId(groupId);
                groupperm.setPermId(rs.getInt("perm_id"));
                groupperm.setPermName(rs.getString("perm_name"));
                groupperm.setHasPermission(rs.getInt("has_permission")!=0);
                access.add(groupperm);
            }
            return access;

        } catch (Exception e) {
            // Database problem
            throw new DatabaseException(e, queryHelper);

        } finally {
            queryHelper.closeRs();
            closeConnection(conn);
        }
    }

    public ActionMessages updateGroupAccess(GroupPermissionMap groupperm) throws DatabaseException {
        QueryHelper queryHelper = new QueryHelper(AdminQueries.updateGroupAccessQuery());
        queryHelper.addInputInt(groupperm.getGroupId());
        queryHelper.addInputInt(groupperm.getPermId());
        queryHelper.addInputInt(groupperm.getCmd());

        return executeProcedure(queryHelper);
    }

    public List getAvailableMembers(Integer groupId) throws DatabaseException {
        // Get a list of available members.
        UserSearch userSearch = new UserSearch();
        userSearch.put("excludedDefaultAdmin", "");
        userSearch.put("notInGroup", groupId);

        QueryBits query = new QueryBits(userSearch);
        query.addSortColumn(AdminQueries.getOrderByColumn(AdminUtils.getUsernameSort()));

        AdminService adminService = ServiceProvider.getAdminService(requestContext);

        return adminService.getUsers(query);
    }

    public List<AccessUser> getGroupMembers(Integer groupId) throws DatabaseException {
        // Get a list of selected members.
        UserSearch userSearch = new UserSearch();
        userSearch.put("excludedDefaultAdmin", "");
        userSearch.put("inGroupId", groupId);

        QueryBits query = new QueryBits(userSearch);
        query.addSortColumn(AdminQueries.getOrderByColumn(AdminUtils.getUsernameSort()));

        AdminService adminService = ServiceProvider.getAdminService(requestContext);

        return adminService.getUsers(query);
    }

    public void updateGroupMembers(Connection conn, AccessGroup group) throws DatabaseException {
        List<Integer> selectedMemberList = group.getSelectedMembers() == null ? new ArrayList() :
            new ArrayList(group.getSelectedMembers());

        // Loop through the current member list
        // If the member is not the in list, run a procedure to remove the member
        for (AccessUser user: getGroupMembers(group.getId())) {
            if (selectedMemberList.contains(user.getId())) {
                selectedMemberList.remove(user.getId());
            } else {
                removeGroupMember(conn, group, user.getId());
            }
        }

        // The remaining list is the users we want to add
        for (Integer userId: selectedMemberList) {
            addGroupMember(conn, group, userId);
        }
    }

    public void addGroupMember(Connection conn, AccessGroup group, Integer userId) throws DatabaseException {
        QueryHelper queryHelper = new QueryHelper(AdminQueries.addGroupMembersQuery());
        queryHelper.addInputInt(group.getId());
        queryHelper.addInputInt(userId);
        queryHelper.addInputInt(requestContext.getUser().getId());

        queryHelper.executeProcedure(conn);
    }

    public void removeGroupMember(Connection conn, AccessGroup group, Integer userId) throws DatabaseException {
        QueryHelper queryHelper = new QueryHelper(AdminQueries.removeGroupMembersQuery());
        queryHelper.addInputInt(group.getId());
        queryHelper.addInputInt(userId);

        queryHelper.executeProcedure(conn);
    }
}