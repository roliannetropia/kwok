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
package com.kwoksys.biz.admin;

import com.kwoksys.biz.ServiceProvider;
import com.kwoksys.biz.admin.core.AdminUtils;
import com.kwoksys.biz.admin.core.AttributeSearch;
import com.kwoksys.biz.admin.core.PasswordUpdateValidator;
import com.kwoksys.biz.admin.core.UserSearch;
import com.kwoksys.biz.admin.dao.*;
import com.kwoksys.biz.admin.dto.*;
import com.kwoksys.biz.auth.core.Access;
import com.kwoksys.biz.auth.core.AuthUtils;
import com.kwoksys.biz.contacts.dto.Contact;
import com.kwoksys.biz.system.SystemService;
import com.kwoksys.biz.system.core.configs.ConfigManager;
import com.kwoksys.biz.system.core.configs.SystemConfigNames;
import com.kwoksys.framework.connections.database.QueryBits;
import com.kwoksys.framework.exceptions.DatabaseException;
import com.kwoksys.framework.exceptions.ObjectNotFoundException;
import com.kwoksys.framework.http.RequestContext;
import com.kwoksys.framework.properties.Localizer;
import com.kwoksys.framework.util.StringUtils;
import org.apache.struts.action.ActionMessage;
import org.apache.struts.action.ActionMessages;

import java.util.*;

/**
 * AdminService
 */
public class AdminService {

    private RequestContext requestContext;

    public AdminService(RequestContext requestContext) {
        this.requestContext = requestContext;
    }

    /**
     * Get database names
     * @return
     * @throws DatabaseException
     */
    public List getDatabases() throws DatabaseException {
        return new AdminDao(requestContext).getDatabases();
    }

    public ActionMessages updateConfig(List list) throws DatabaseException {
        ActionMessages errors = new AdminDao(requestContext).updateConfig(list);
        if (errors.isEmpty()) {
            // Reset system key cache if there is no errors.
            resetSystemCacheConfig();
        }
        return errors;
    }

    /**
     * Reset system cache key with unix timestamp
     * @return
     * @throws DatabaseException
     */
    private ActionMessages resetSystemCacheConfig() throws DatabaseException {
        SystemService systemService = ServiceProvider.getSystemService(requestContext);

        List list = new ArrayList();
        list.add(new SystemConfig(SystemConfigNames.SYSTEM_CACHE_KEY, String.valueOf(systemService.getSystemInfo().getSysdate().getTime())));

        return new AdminDao(requestContext).updateConfig(list);
    }

    // Attributes
    public Map<Integer, AttributeGroup> getAttributeGroups(Integer objectTypeId) throws DatabaseException {
        return new AttributeDao(requestContext).getAttributeGroupsQuery(objectTypeId);
    }

    public AttributeGroup getAttributeGroup(Integer attributeGroupId, Integer objectTypeId) throws DatabaseException, ObjectNotFoundException {
        return new AttributeDao(requestContext).getAttributeGroup(attributeGroupId, objectTypeId);
    }

    public Map<Integer, Attribute> getAttributes(QueryBits query) throws DatabaseException {
        return new AttributeDao(requestContext).getAttributeList(query);
    }

    public boolean hasCustomFields(Integer objectTypeId) throws DatabaseException {
        AttributeSearch attributeSearch = new AttributeSearch();
        attributeSearch.put(AttributeSearch.IS_CUSTOM_ATTR, true);
        attributeSearch.put(AttributeSearch.OBJECT_TYPE_ID_EUALS, objectTypeId);

        QueryBits query = new QueryBits(attributeSearch);

        return new AttributeDao(requestContext).hasCustomFields(query);
    }

    public Attribute getSystemAttribute(Integer attributeId) throws DatabaseException, ObjectNotFoundException {
        Attribute attr = new AttributeDao(requestContext).getAttribute(attributeId);

        // Make sure we allow adding attribute field for this attribute id
        if (attr.isCustomAttr() || !attr.isAttrFieldsEditable()) {
            throw new ObjectNotFoundException();
        }
        return attr;
    }

    public Attribute getCustomAttribute(Integer attributeId) throws DatabaseException, ObjectNotFoundException {
        AttributeDao attrDao = new AttributeDao(requestContext);
        Attribute attr = attrDao.getAttribute(attributeId);

        // Make sure we allow custom attribute for this object type
        if (!attr.isCustomAttr()) {
            throw new ObjectNotFoundException();
        }
        return attr;
    }

    public Map<Integer, AttributeField> getEditAttributeFields(QueryBits query) throws DatabaseException {
        return new AttributeDao(requestContext).getAttributeFields(query);
    }

    public Map<Integer, AttributeField> getAttributeFields(Integer attributeId) throws DatabaseException {
        AttributeSearch attributeSearch = new AttributeSearch();
        attributeSearch.put("attributeIdEquals", attributeId);

        QueryBits query = new QueryBits(attributeSearch);
        query.addSortColumn(AdminQueries.getOrderByColumn(AttributeField.NAME));

        return new AttributeDao(requestContext).getAttributeFields(query);
    }

    public AttributeField getAttributeField(Integer attrFieldId) throws DatabaseException, ObjectNotFoundException {
        AttributeSearch attributeSearch = new AttributeSearch();
        attributeSearch.put("isEditable", true);

        return new AttributeDao(requestContext).getAttributeField(new QueryBits(attributeSearch), attrFieldId);
    }

    /**
     * Given an object_type_id, object_id, return a list of custom attributes.
     * @return
     * @throws DatabaseException
     */
    public Map getCustomAttributeValueMap(Integer objectTypeId, Integer objectId) throws DatabaseException {
        AttributeSearch attributeSearch = new AttributeSearch();
        attributeSearch.put("objectTypeId", objectTypeId);
        attributeSearch.put("objectId", objectId);

        QueryBits query = new QueryBits(attributeSearch);
        query.addSortColumn(AdminQueries.getOrderByColumn(AttributeValue.ATTR_KEY));

        return new AttributeDao(requestContext).getCustomAttributeValueMap(query);
    }

    public List<Integer> getAttributeFieldTypes(Integer attrFieldId) throws DatabaseException {
        if (attrFieldId == null || attrFieldId == 0) {
            return new ArrayList();
        }
        return new AttributeDao(requestContext).getAttributeFieldTypes(new QueryBits(), attrFieldId);
    }

    public Set getAttributeFieldTypesByField(Integer attrFieldId) throws DatabaseException {
        if (attrFieldId == null || attrFieldId == 0) {
            return new HashSet();
        }
        return new AttributeDao(requestContext).getAttributeFieldTypesByField(new QueryBits(), attrFieldId);
    }

    /**
     * Adds attribute group.
     * @param attr
     * @return
     * @throws DatabaseException
     */
    public ActionMessages addAttributeGroup(AttributeGroup attributeGroup) throws DatabaseException {
        ActionMessages errors = new ActionMessages();
        if (attributeGroup.getName().isEmpty()) {
            errors.add("emptyField", new ActionMessage("common.form.fieldRequired",
                    Localizer.getText(requestContext, "admin.attribute.attribute_group_name")));
        }
        if (!errors.isEmpty()) {
            return errors;
        }
        return new AttributeDao(requestContext).addAttributeGroup(attributeGroup);
    }

    /**
     * Updates attribute group.
     * @param attributeGroup
     * @return
     * @throws DatabaseException
     */
    public ActionMessages updateAttributeGroup(AttributeGroup attributeGroup) throws DatabaseException {
        ActionMessages errors = new ActionMessages();
        if (attributeGroup.getName().isEmpty()) {
            errors.add("emptyField", new ActionMessage("common.form.fieldRequired",
                    Localizer.getText(requestContext, "admin.attribute.attribute_group_name")));
        }
        if (!errors.isEmpty()) {
            return errors;
        }
        return new AttributeDao(requestContext).updateAttributeGroup(attributeGroup);
    }

    public ActionMessages deleteAttributeGroup(AttributeGroup attributeGroup) throws DatabaseException {
        return new AttributeDao(requestContext).deleteAttributeGroup(attributeGroup);
    }

    /**
     * This is for adding custom attributes.
     * @param attr
     * @return
     * @throws DatabaseException
     */
    public ActionMessages addAttribute(Attribute attr) throws DatabaseException {
        ActionMessages errors = new ActionMessages();
        if (attr.getName().isEmpty()) {
            errors.add("emptyField", new ActionMessage("common.form.fieldRequired",
                    Localizer.getText(requestContext, "admin.attribute.attribute_name")));
        }
        if (!errors.isEmpty()) {
            return errors;
        }
        return new AttributeDao(requestContext).addAttribute(attr);
    }

    /**
     * This is for updating custom attributes.
     * @param attr
     * @return
     * @throws DatabaseException
     */
    public ActionMessages updateAttribute(Attribute attr) throws DatabaseException {
        ActionMessages errors = new ActionMessages();
        if (attr.getName().isEmpty()) {
            errors.add("emptyField", new ActionMessage("common.form.fieldRequired",
                    Localizer.getText(requestContext, "admin.attribute.attribute_name")));
        }
        if (!errors.isEmpty()) {
            return errors;
        }
        return new AttributeDao(requestContext).updateCustomAttribute(attr);
    }

    public ActionMessages updateSystemAttribute(Attribute attr) throws DatabaseException {
        return new AttributeDao(requestContext).updateSystemAttribute(attr);
    }

    /**
     * This is for deleting custom attributes.
     * @param attr
     * @return
     * @throws DatabaseException
     */
    public ActionMessages deleteAttribute(Attribute attr) throws DatabaseException {
        ActionMessages errors = new ActionMessages();
        if (!errors.isEmpty()) {
            return errors;
        }
        return new AttributeDao(requestContext).deleteAttribute(attr);
    }

    public ActionMessages addAttributeField(AttributeField attrField) throws DatabaseException {
        ActionMessages errors = new ActionMessages();

        if (attrField.getName().isEmpty()) {
            errors.add("emptyField", new ActionMessage("admin.attributeAdd.error.emptyField"));
        }
        if (!errors.isEmpty()) {
            return errors;
        }
        return new AttributeDao(requestContext).addAttributeField(attrField);
    }

    public ActionMessages updateAttributeField(AttributeField attrField) throws DatabaseException {
        ActionMessages errors = new ActionMessages();
        if (attrField.getName().isEmpty()) {
            errors.add("emptyField", new ActionMessage("admin.attributeAdd.error.emptyField"));
        }
        if (!errors.isEmpty()) {
            return errors;
        }
        return new AttributeDao(requestContext).updateAttributeField(attrField);
    }

    /**
     * Get icons.
     */
    public List<Icon> getIcons(Integer attributeId) throws DatabaseException {
        return new AttributeDao(requestContext).getIcons(attributeId);
    }

    /**
     * Get group list
     * @param query
     * @return
     * @throws DatabaseException
     */
    public List<AccessGroup> getGroups(QueryBits query) throws DatabaseException {
        return new AccessGroupDao(requestContext).getGroups(query);
    }

    public AccessGroup getGroup(Integer groupId) throws DatabaseException, ObjectNotFoundException {
        return new AccessGroupDao(requestContext).getGroup(groupId);
    }

    public ActionMessages addGroup(AccessGroup group) throws DatabaseException {
        ActionMessages errors = new ActionMessages();

        if (group.getName().isEmpty()) {
            errors.add("groupName", new ActionMessage("admin.groupEdit.error.groupName"));
        }
        if (!errors.isEmpty()) {
            return errors;
        }
        return new AccessGroupDao(requestContext).addGroup(group);
    }

    public ActionMessages updateGroup(AccessGroup group) throws DatabaseException {
        ActionMessages errors = new ActionMessages();

        if (group.getName().isEmpty()) {
            errors.add("groupName", new ActionMessage("admin.groupEdit.error.groupName"));
        }
        if (!errors.isEmpty()) {
            return errors;
        }
        return new AccessGroupDao(requestContext).editGroup(group);
    }

    public ActionMessages deleteGroup(AccessGroup group) throws DatabaseException {
        return new AccessGroupDao(requestContext).deleteGroup(group);
    }

    public List<GroupPermissionMap> getGroupAccess(QueryBits query, Integer groupId) throws DatabaseException {
        return new AccessGroupDao(requestContext).getGroupAccess(query, groupId);
    }

    public ActionMessages updateGroupAccess(GroupPermissionMap groupperm) throws DatabaseException {
        return new AccessGroupDao(requestContext).updateGroupAccess(groupperm);
    }

    public List<AccessUser> getAvailableMembers(Integer groupId) throws DatabaseException {
        return new AccessGroupDao(requestContext).getAvailableMembers(groupId);
    }

    public List<AccessUser> getGroupMembers(Integer groupId) throws DatabaseException {
        return new AccessGroupDao(requestContext).getGroupMembers(groupId);
    }

    /**
     * Get user list
     * @param query
     * @return
     * @throws DatabaseException
     */
    public List<AccessUser> getUsers(QueryBits query) throws DatabaseException {
        return new AccessUserDao(requestContext).getUsers(query);
    }

    public List<AccessUser> getExtendedUsers(QueryBits query) throws DatabaseException {
        return new AccessUserDao(requestContext).getExtendedUsers(query);
    }

    public int getUserCount(QueryBits query) throws DatabaseException {
        return new AccessUserDao(requestContext).getUserCount(query);
    }

    public AccessUser getUser(Integer userId) throws DatabaseException, ObjectNotFoundException {
        return new AccessUserDao(requestContext).getUser(userId);
    }

    public Integer getUserIdByUsername(String username) throws DatabaseException {
        return new AccessUserDao(requestContext).getUserIdByUsername(username);
    }

    public Integer getUserIdByEmail(String email) throws DatabaseException {
        return new AccessUserDao(requestContext).getUserIdByEmail(email);
    }

    public AccessUser getUserByUsername(String username) throws DatabaseException {
        UserSearch userSearch = new UserSearch();
        userSearch.put(UserSearch.USERNAME, username);
        QueryBits queryBits = new QueryBits(userSearch);
        queryBits.setLimit(2, 0);

        List<AccessUser> list = new AccessUserDao(requestContext).getUsers(queryBits);
        if (list.size() == 1) {
            return list.get(0);
        } else {
            return null;
        }
    }

    public List<UserPermissionMap> getUserAccess(QueryBits query, Integer userId) throws DatabaseException {
        return new AccessUserDao(requestContext).getUserAccess(query, userId);
    }

    public ActionMessages addUser(AccessUser user, AccessGroup group, Contact contact,
                                  Map<Integer, Attribute> customAttributes) throws DatabaseException {

        ActionMessages errors = validateUser(user, contact, customAttributes);

        // Validate new password
        if (user.passwordsNotMatch()) {
            errors.add("password", new ActionMessage("admin.userEdit.error.passwordMismatch"));

        } else if (allowPasswordUpdate()
                && user.isAccountEnabled()
                && !ConfigManager.admin.isAllowBlankUserPassword()) {
            
            if (user.getPasswordNew().isEmpty()) {
                errors.add("password", new ActionMessage("admin.userEdit.error.passwordEmpty"));

            } else if (!PasswordUpdateValidator.validateLength(user.getPasswordNew())) {
                errors.add("password", new ActionMessage("admin.userEdit.error.passwordMinLen", ConfigManager.auth.getSecurityMinPasswordLength()));

            } else if (!PasswordUpdateValidator.validateComplexity(user.getPasswordNew(), user)) {
                errors.add("password", new ActionMessage("admin.config.security.passwordComplexity.desc"));
            }
        }

        if (!errors.isEmpty()) {
            return errors;
        }
        return new AccessUserDao(requestContext).addUser(requestContext, user, group, contact);
    }

    public ActionMessages updateUser(AccessUser user, AccessGroup group, Contact contact,
                                     Map<Integer, Attribute> customAttributes) throws DatabaseException {

        ActionMessages errors = validateUser(user, contact, customAttributes);

        if (!errors.isEmpty()) {
            return errors;
        }
        return new AccessUserDao(requestContext).updateUser(requestContext, user, group, contact);
    }

    public ActionMessages validateUser(AccessUser user, Contact contact, Map<Integer, Attribute> customAttributes)
            throws DatabaseException {

        ActionMessages errors = new ActionMessages();

        if (StringUtils.isEmpty(user.getUsername())) {
            errors.add("username", new ActionMessage("common.form.fieldRequired", Localizer.getText(requestContext, "common.column.username")));
        } else {
            Integer queryUserId = getUserIdByUsername(user.getUsername());
            if (queryUserId != null && !queryUserId.equals(user.getId())) {
                errors.add("usernameInUse", new ActionMessage("admin.userEdit.error.usernameInUse"));
            }
        }

        if (StringUtils.isEmpty(user.getFirstName())) {
            errors.add("firstName", new ActionMessage("common.form.fieldRequired", Localizer.getText(requestContext,
                    "common.column.user_first_name")));
        }
        if (StringUtils.isEmpty(user.getLastName())) {
            errors.add("lastName", new ActionMessage("common.form.fieldRequired", Localizer.getText(requestContext,
                    "common.column.user_last_name")));
        }
        if (StringUtils.isEmpty(user.getDisplayName())) {
            errors.add("displayName", new ActionMessage("common.form.fieldRequired", Localizer.getText(requestContext,
                    "common.column.user_display_name")));
        }
        if (StringUtils.isEmpty(user.getEmail())) {
            errors.add("email", new ActionMessage("common.form.fieldRequired", Localizer.getText(requestContext,
                    "common.column.user_email")));
        }
        validateMessengerInputs(errors, contact);

        // Validate attributes
        AdminUtils.validateAttributeValues(requestContext, errors, user, customAttributes);

        return errors;
    }

    public ActionMessages updateUserContact(AccessUser user, Contact contact) throws DatabaseException {
        ActionMessages errors = new ActionMessages();
        validateMessengerInputs(errors, contact);

        if (!errors.isEmpty()) {
            return errors;
        }
        return new AccessUserDao(requestContext).updateUserContact(requestContext, user, contact);
    }

    private void validateMessengerInputs(ActionMessages errors, Contact contact) {
        if (contact != null) {
            if ((contact.getMessenger1Type() != 0 && contact.getMessenger1Id().isEmpty())
                    || (contact.getMessenger1Type() == 0 && !contact.getMessenger1Id().isEmpty())
                    || (contact.getMessenger2Type() != 0 && contact.getMessenger2Id().isEmpty())
                    || (contact.getMessenger2Type() == 0 && !contact.getMessenger2Id().isEmpty())) {
                errors.add("invalidMessengerInput", new ActionMessage("admin.userEdit.error.invalidMessengerInput"));
            }
        }
    }

    /**
     * Updates user password. This is for user to update his/her password. We check old password in this case.
     * @param user
     * @return
     * @throws DatabaseException
     */
    public ActionMessages updateUserPassword(AccessUser user) throws DatabaseException {
        ActionMessages errors = new ActionMessages();

        if (user.passwordsNotMatch()) {
            // Check whether the new passwords match.
            errors.add("password", new ActionMessage("userPref.passwordEdit.error.newPasswordMismatch"));

        } else if (!ConfigManager.admin.isAllowBlankUserPassword()) {
            // Check whether the old password is empty.
            if (user.getRequestedPassword().isEmpty()) {
                errors.add("password", new ActionMessage("common.form.fieldRequired",
                                                    Localizer.getText(requestContext, "userPref.passwordEdit.passwordOld")));
            }
            // Check whether the user provided a new password.
            if (user.getPasswordNew().isEmpty()) {
                errors.add("password", new ActionMessage("common.form.fieldRequired",
                                                                    Localizer.getText(requestContext, "userPref.passwordEdit.passwordNew")));
            }
            if (errors.isEmpty()) {
                if (!PasswordUpdateValidator.validateLength(user.getPasswordNew())) {
                    errors.add("password", new ActionMessage("admin.userEdit.error.passwordMinLen", ConfigManager.auth.getSecurityMinPasswordLength()));
                    
                } else if (!PasswordUpdateValidator.validateComplexity(user.getPasswordNew(), user)) {
                    errors.add("password", new ActionMessage("admin.config.security.passwordComplexity.desc"));
                }
            }
        }
        if (!errors.isEmpty()) {
            return errors;
        }

        // Run a queries to check whether the old password is the same as what we have in the database.
        try {
            if (!AuthUtils.hashPassword(user.getRequestedPassword()).equals(user.getHashedPassword())) {
                errors.add("oldPasswordMismatch", new ActionMessage("userPref.passwordEdit.error.oldPasswordMismatch"));
            }
        } catch (Exception e) {
            errors.add("application", new ActionMessage("common.error.application"));
        }

        if (!errors.isEmpty()) {
            return errors;
        }

        return new AccessUserDao(requestContext).editUserPassword(user);
    }

    /**
     * Updates user password. This is for admin to reset a user's password. Therefore, no need to check old password.
     * @param user
     * @return
     * @throws DatabaseException
     */

    public ActionMessages resetUserPassword(AccessUser user) throws DatabaseException {
        ActionMessages errors = new ActionMessages();

        if (user.passwordsNotMatch()) {
            // Check whether the password matches.
            errors.add("password", new ActionMessage("admin.userPasswordReset.error.passwordsMismatch"));

        } else if (!ConfigManager.admin.isAllowBlankUserPassword()) {
            // Check whether the user provided a new password.
            if (user.getPasswordNew().isEmpty()) {
                errors.add("password", new ActionMessage("admin.userPasswordReset.error.emptyPassword"));
            }
            if (!PasswordUpdateValidator.validateLength(user.getPasswordNew())) {
                errors.add("password", new ActionMessage("admin.userEdit.error.passwordMinLen", ConfigManager.auth.getSecurityMinPasswordLength()));

            } else if (!PasswordUpdateValidator.validateComplexity(user.getPasswordNew(), user)) {
                errors.add("password", new ActionMessage("admin.config.security.passwordComplexity.desc"));
            }
        }
        if (!errors.isEmpty()) {
            return errors;
        }

        return new AccessUserDao(requestContext).editUserPassword(user);
    }

    public ActionMessages deleteUser(AccessUser user) throws DatabaseException {
        return new AccessUserDao(requestContext).deleteUser(user);
    }

    public ActionMessages updateUserAccess(UserPermissionMap userperm) throws DatabaseException {
        return new AccessUserDao(requestContext).updateUserAccess(userperm);
    }

    /**
     * Returns true if the application allows user password update.
     * When LDAP authentication is turned on, and when all authentication are done via LDAP, no user password update
     * needed anyway.
     * @return
     */
    public boolean allowPasswordUpdate() {
        return !ConfigManager.auth.getAuthMethod().equals(Access.AUTH_LDAP);
    }
}
