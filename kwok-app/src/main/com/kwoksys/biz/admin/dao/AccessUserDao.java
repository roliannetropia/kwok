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

import com.kwoksys.biz.admin.dto.AccessGroup;
import com.kwoksys.biz.admin.dto.AccessUser;
import com.kwoksys.biz.admin.dto.UserPermissionMap;
import com.kwoksys.biz.auth.core.AuthUtils;
import com.kwoksys.biz.base.BaseDao;
import com.kwoksys.biz.contacts.dto.Contact;
import com.kwoksys.biz.system.core.ObjectTypes;
import com.kwoksys.framework.http.RequestContext;
import com.kwoksys.framework.connections.database.QueryBits;
import com.kwoksys.framework.connections.database.QueryHelper;
import com.kwoksys.framework.exceptions.DatabaseException;
import com.kwoksys.framework.exceptions.ObjectNotFoundException;
import com.kwoksys.framework.util.DatetimeUtils;
import com.kwoksys.framework.util.StringUtils;
import org.apache.struts.action.ActionMessage;
import org.apache.struts.action.ActionMessages;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.Types;
import java.util.ArrayList;
import java.util.List;

/**
 * AccessUserDao.
 */
public class AccessUserDao extends BaseDao {

    public AccessUserDao(RequestContext requestContext) {
        super(requestContext);
    }

    /**
     * Gets users.
     *
     * @param query
     * @return ..
     */
    public List<AccessUser> getUsers(QueryBits query) throws DatabaseException {
        Connection conn = getConnection();

        QueryHelper queryHelper = new QueryHelper(AdminQueries.selectUserListQuery(query));

        try {
            List users = new ArrayList();
            ResultSet rs = queryHelper.executeQuery(conn);

            while (rs.next()) {
                AccessUser user = new AccessUser();
                user.setId(rs.getInt("user_id"));
                user.setUsername(StringUtils.replaceNull(rs.getString("username")));
                user.setDisplayName(StringUtils.replaceNull(rs.getString("display_name")));
                user.setFirstName(StringUtils.replaceNull(rs.getString("first_name")));
                user.setLastName(StringUtils.replaceNull(rs.getString("last_name")));
                user.setEmail(StringUtils.replaceNull(rs.getString("email")));
                user.setStatus(rs.getInt("status"));
                users.add(user);
            }
            return users;

        } catch (Exception e) {
            // Database problem
            throw new DatabaseException(e, queryHelper);

        } finally {
            queryHelper.closeRs();
            closeConnection(conn);
        }
    }

    public List<AccessUser> getExtendedUsers(QueryBits query) throws DatabaseException {
        Connection conn = getConnection();

        QueryHelper queryHelper = new QueryHelper(AdminQueries.selectUserExportListQuery(query));

        try {
            List users = new ArrayList();
            ResultSet rs = queryHelper.executeQuery(conn);

            while (rs.next()) {
                AccessUser user = new AccessUser();
                user.setId(rs.getInt("user_id"));
                user.setUsername(StringUtils.replaceNull(rs.getString("username")));
                user.setDisplayName(StringUtils.replaceNull(rs.getString("display_name")));
                user.setFirstName(StringUtils.replaceNull(rs.getString("first_name")));
                user.setLastName(StringUtils.replaceNull(rs.getString("last_name")));
                user.setEmail(StringUtils.replaceNull(rs.getString("email")));
                user.setStatus(rs.getInt("status"));
                user.setLastLogonTime(DatetimeUtils.getDate(rs, "last_logon"));
                user.setLastVisitTime(DatetimeUtils.getDate(rs, "last_visit"));
                user.setContactId(rs.getInt("contact_id"));
                user.setCreationDate(DatetimeUtils.getDate(rs, "creation_date"));
                user.setModificationDate(DatetimeUtils.getDate(rs, "modification_date"));

                user.setCreator(new AccessUser());
                user.getCreator().setId(rs.getInt("creator"));
                user.getCreator().setUsername(rs.getString("creator_username"));
                user.getCreator().setDisplayName(rs.getString("creator_display_name"));

                user.setModifier(new AccessUser());
                user.getModifier().setId(rs.getInt("modifier"));
                user.getModifier().setUsername(rs.getString("modifier_username"));
                user.getModifier().setDisplayName(rs.getString("modifier_display_name"));
                users.add(user);
            }
            return users;

        } catch (Exception e) {
            // Database problem
            throw new DatabaseException(e, queryHelper);

        } finally {
            queryHelper.closeRs();
            closeConnection(conn);
        }
    }

    public int getUserCount(QueryBits query) throws DatabaseException {
        return getRowCount(AdminQueries.selectUserCountQuery(query));
    }

    /**
     * Gets user details.
     *
     * @param reqUserId
     * @return ..
     */
    public AccessUser getUser(Integer reqUserId) throws DatabaseException, ObjectNotFoundException {
        Connection conn = getConnection();

        QueryHelper queryHelper = new QueryHelper(AdminQueries.selectUserDetailQuery());
        queryHelper.addInputInt(reqUserId);

        try {
            ResultSet rs = queryHelper.executeQuery(conn);

            if (rs.next()) {
                AccessUser user = new AccessUser();
                user.setId(rs.getInt("user_id"));
                user.setUsername(StringUtils.replaceNull(rs.getString("username")));
                user.setDisplayName(StringUtils.replaceNull(rs.getString("display_name")));
                user.setFirstName(StringUtils.replaceNull(rs.getString("first_name")));
                user.setLastName(StringUtils.replaceNull(rs.getString("last_name")));
                user.setEmail(StringUtils.replaceNull(rs.getString("email")));
                user.setStatus(rs.getInt("status"));
                user.setHashedPassword(StringUtils.replaceNull(rs.getString("password")));
                user.setLastLogonTime(DatetimeUtils.getDate(rs, "last_logon"));
                user.setLastVisitTime(DatetimeUtils.getDate(rs, "last_visit"));
                user.setHardwareCount(rs.getInt("hardware_count"));
                user.setContactId(rs.getInt("contact_id"));
                user.setDefaultUser(rs.getBoolean("is_default_user"));
                user.setGroupId(rs.getInt("group_id"));
                user.setGroupName(StringUtils.replaceNull(rs.getString("group_name")));
                user.setCreationDate(DatetimeUtils.getDate(rs, "creation_date"));
                user.setModificationDate(DatetimeUtils.getDate(rs, "modification_date"));

                user.setCreator(new AccessUser());
                user.getCreator().setId(rs.getInt("creator"));
                user.getCreator().setUsername(rs.getString("creator_username"));
                user.getCreator().setDisplayName(rs.getString("creator_display_name"));

                user.setModifier(new AccessUser());
                user.getModifier().setId(rs.getInt("modifier"));
                user.getModifier().setUsername(rs.getString("modifier_username"));
                user.getModifier().setDisplayName(rs.getString("modifier_display_name"));

                return user;
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
     * Returns user id given a username.
     *
     * @return ..
     */
    public Integer getUserIdByUsername(String username) throws DatabaseException {
        Connection conn = getConnection();

        QueryHelper queryHelper = new QueryHelper(AdminQueries.selectUserIdByNameQuery());
        queryHelper.addInputString(username);

        try {
            ResultSet rs = queryHelper.executeQuery(conn);
            
            if (rs.next()) {
                return rs.getInt("user_id");
            } else {
                return null;
            }
        } catch (Exception e) {
            // Database problem
            throw new DatabaseException(e, queryHelper);

        } finally {
            queryHelper.closeRs();
            closeConnection(conn);
        }
    }

    public Integer getUserIdByEmail(String email) throws DatabaseException {
        Connection conn = getConnection();

        QueryHelper queryHelper = new QueryHelper(AdminQueries.selectUserIdByEmailQuery());
        queryHelper.addInputString(email);

        try {
            ResultSet rs = queryHelper.executeQuery(conn);

            if (rs.next()) {
                return rs.getInt("user_id");
            } else {
                return null;
            }
        } catch (Exception e) {
            // Database problem
            throw new DatabaseException(e, queryHelper);

        } finally {
            queryHelper.closeRs();
            closeConnection(conn);
        }
    }

    public List<UserPermissionMap> getUserAccess(QueryBits query, Integer userId) throws DatabaseException {
        Connection conn = getConnection();

        QueryHelper queryHelper = new QueryHelper(AdminQueries.selectUserAccessQuery(query));
        queryHelper.addInputInt(userId);

        try {
            List access = new ArrayList();
            ResultSet rs = queryHelper.executeQuery(conn);

            while (rs.next()) {
                UserPermissionMap userperm = new UserPermissionMap();
                userperm.setUserId(userId);
                userperm.setPermId(rs.getInt("perm_id"));
                userperm.setPermName(rs.getString("perm_name"));
                userperm.setHasPermission(rs.getInt("has_permission")!=0);
                access.add(userperm);
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

    public ActionMessages addUser(RequestContext requestContext, AccessUser user, AccessGroup group, Contact contact) throws DatabaseException {
        Connection conn = getConnection();

        QueryHelper queryHelper = new QueryHelper(AdminQueries.insertUserQuery());

        try {
            queryHelper.addOutputParam(Types.INTEGER);
            queryHelper.addInputStringConvertNull(user.getUsername());
            queryHelper.addInputStringConvertNull(user.getFirstName());
            queryHelper.addInputStringConvertNull(user.getLastName());
            queryHelper.addInputStringConvertNull(user.getDisplayName());
            queryHelper.addInputStringConvertNull(user.getEmail());
            queryHelper.addInputInt(user.getStatus());
            queryHelper.addInputStringConvertNull(AuthUtils.hashPassword(user.getPasswordNew()));
            queryHelper.addInputInt(requestContext.getUser().getId());

            queryHelper.executeProcedure(conn);
            // Put some values in the result
            user.setId((Integer) queryHelper.getSqlOutputs().get(0));

            // Update contact
            updateUserContact(requestContext, conn, user, contact);

            // Update group
            updateUserGroup(requestContext, conn, group, user);

            // Update custom fields
            if (!user.getCustomValues().isEmpty()) {
                AttributeDao attributeDao = new AttributeDao(requestContext);
                attributeDao.updateAttributeValue(conn, user.getId(), user.getCustomValues());
            }
        } catch (Exception e) {
            // Database problem
            handleError(e);

        } finally {
            closeConnection(conn);
        }
        return errors;
    }

    /**
     * Updates user detail.
     *
     * @return ..
     */
    public ActionMessages updateUser(RequestContext requestContext, AccessUser user, AccessGroup group, Contact contact) throws DatabaseException {
        Connection conn = getConnection();

        QueryHelper queryHelper = new QueryHelper(AdminQueries.updateUserQuery());
        queryHelper.addInputInt(user.getId());
        queryHelper.addInputStringConvertNull(user.getUsername());
        queryHelper.addInputStringConvertNull(user.getFirstName());
        queryHelper.addInputStringConvertNull(user.getLastName());
        queryHelper.addInputStringConvertNull(user.getDisplayName());
        queryHelper.addInputStringConvertNull(user.getEmail());
        queryHelper.addInputInt(user.getStatus());
        queryHelper.addInputInt(requestContext.getUser().getId());

        try {
            queryHelper.executeProcedure(conn);

            // Update contact
            updateUserContact(requestContext, conn, user, contact);

            // Update group
            updateUserGroup(requestContext, conn, group, user);

            // Update custom fields
            if (!user.getCustomValues().isEmpty()) {
                AttributeDao attributeDao = new AttributeDao(requestContext);
                attributeDao.updateAttributeValue(conn, user.getId(), user.getCustomValues());
            }
        } catch (Exception e) {
            // Database problem
            handleError(e);

        } finally {
            closeConnection(conn);
        }
        return errors;
    }

    public ActionMessages updateUserContact(RequestContext requestContext, AccessUser user, Contact contact) throws DatabaseException {
        Connection conn = getConnection();

        try {
            updateUserContact(requestContext, conn, user, contact);
        } catch (Exception e) {
            // Database problem
            handleError(e);

        } finally {
            closeConnection(conn);
        }
        return errors;
    }

    /**
     * Updates user password.
     *
     * @return ..
     */
    public ActionMessages editUserPassword(AccessUser user) throws DatabaseException {
        QueryHelper queryHelper = new QueryHelper(AdminQueries.updateUserPasswordQuery());
        try {
            queryHelper.addInputStringConvertNull(AuthUtils.hashPassword(user.getPasswordNew()));
            queryHelper.addInputInt(user.getId());

        } catch (Exception e) {
            errors.add("application", new ActionMessage("common.error.application"));
            return errors;
        }

        return executeProcedure(queryHelper);
    }

    /**
     * Deletes a user
     * @param user
     * @return
     * @throws DatabaseException
     */
    public ActionMessages deleteUser(AccessUser user) throws DatabaseException {
        QueryHelper queryHelper = new QueryHelper(AdminQueries.deleteUserQuery());
        queryHelper.addInputInt(ObjectTypes.USER);
        queryHelper.addInputInt(user.getId());
        queryHelper.addInputInt(requestContext.getUser().getId());

        return executeProcedure(queryHelper);
    }

    public ActionMessages updateUserAccess(UserPermissionMap userGroup) throws DatabaseException {
        QueryHelper queryHelper = new QueryHelper(AdminQueries.updateUserAccessQuery());
        queryHelper.addInputInt(userGroup.getUserId());
        queryHelper.addInputInt(userGroup.getPermId());
        queryHelper.addInputInt(userGroup.getCmd());

        return executeProcedure(queryHelper);
    }

    public void updateUserContact(RequestContext requestContext, Connection conn, AccessUser user, Contact contact) throws DatabaseException {
        if (contact == null) {
            return;
        }

        QueryHelper queryHelper = new QueryHelper(AdminQueries.updateUserContact());
        queryHelper.addInputInt(user.getId());
        queryHelper.addInputInt(contact.getId());
        queryHelper.addInputStringConvertNull(contact.getFirstName());
        queryHelper.addInputStringConvertNull(contact.getLastName());
        queryHelper.addInputStringConvertNull(contact.getEmailPrimary());
        queryHelper.addInputStringConvertNull(contact.getTitle());
        queryHelper.addInputIntegerConvertNull(contact.getCompanyId());
        queryHelper.addInputInt(ObjectTypes.COMPANY_EMPLOYEE_CONTACT);
        queryHelper.addInputStringConvertNull(contact.getPhoneHome());
        queryHelper.addInputStringConvertNull(contact.getPhoneMobile());
        queryHelper.addInputStringConvertNull(contact.getPhoneWork());
        queryHelper.addInputStringConvertNull(contact.getFax());
        queryHelper.addInputStringConvertNull(contact.getEmailSecondary());
        queryHelper.addInputInt(contact.getMessenger1Type());
        queryHelper.addInputStringConvertNull(contact.getMessenger1Id());
        queryHelper.addInputInt(contact.getMessenger2Type());
        queryHelper.addInputStringConvertNull(contact.getMessenger2Id());
        queryHelper.addInputStringConvertNull(contact.getHomepageUrl());
        queryHelper.addInputStringConvertNull(contact.getDescription());
        queryHelper.addInputStringConvertNull(contact.getAddressStreetPrimary());
        queryHelper.addInputStringConvertNull(contact.getAddressCityPrimary());
        queryHelper.addInputStringConvertNull(contact.getAddressStatePrimary());
        queryHelper.addInputStringConvertNull(contact.getAddressZipcodePrimary());
        queryHelper.addInputStringConvertNull(contact.getAddressCountryPrimary());
        queryHelper.addInputInt(requestContext.getUser().getId());

        queryHelper.executeProcedure(conn);
    }

    private void updateUserGroup(RequestContext requestContext, Connection conn, AccessGroup group, AccessUser user) throws DatabaseException {
        // Only runs the add/remove when user changes group
        if (group != null && !group.getId().equals(user.getGroupId())) {
            AccessGroupDao groupDao = new AccessGroupDao(requestContext);
            groupDao.removeGroupMember(conn, new AccessGroup(user.getGroupId()), user.getId());

            if (group.getId() != 0) {
                groupDao.addGroupMember(conn, group, user.getId());
            }
        }
    }
}
