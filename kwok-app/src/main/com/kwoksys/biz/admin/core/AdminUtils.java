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
package com.kwoksys.biz.admin.core;

import com.kwoksys.action.common.template.DetailTableTemplate;
import com.kwoksys.biz.ServiceProvider;
import com.kwoksys.biz.admin.AdminService;
import com.kwoksys.biz.admin.dao.AdminQueries;
import com.kwoksys.biz.admin.dto.*;
import com.kwoksys.biz.auth.core.Access;
import com.kwoksys.biz.base.BaseObject;
import com.kwoksys.biz.base.BaseObjectForm;
import com.kwoksys.biz.contacts.dto.Contact;
import com.kwoksys.biz.system.core.*;
import com.kwoksys.biz.system.core.configs.ConfigManager;
import com.kwoksys.framework.connections.database.QueryBits;
import com.kwoksys.framework.exceptions.DatabaseException;
import com.kwoksys.framework.http.RequestContext;
import com.kwoksys.framework.properties.AppProperties;
import com.kwoksys.framework.properties.Localizer;
import com.kwoksys.framework.session.CacheManager;
import com.kwoksys.framework.ui.Link;
import com.kwoksys.framework.ui.SelectOneLabelValueBean;
import com.kwoksys.framework.util.CurrencyUtils;
import com.kwoksys.framework.util.CustomFieldFormatter;
import com.kwoksys.framework.util.DatetimeUtils;
import com.kwoksys.framework.util.HtmlUtils;
import org.apache.struts.action.ActionMessage;
import org.apache.struts.action.ActionMessages;
import org.apache.struts.util.LabelValueBean;

import javax.swing.text.MaskFormatter;
import java.text.SimpleDateFormat;
import java.util.*;

/**
 * This class contain admin related functions.
 */
public class AdminUtils {

    public static final String ADMIN_APP_CMD = "app";
    public static final String ADMIN_APP_EDIT_CMD = "appEdit";
    public static final String ADMIN_APP_EDIT_2_CMD = "appEdit2";

    public static final String ADMIN_AUTH_CMD = "auth";
    public static final String ADMIN_AUTH_EDIT_CMD = "authEdit";
    public static final String ADMIN_AUTH_EDIT_2_CMD = "authEdit2";

    public static final String ADMIN_DB_BACKUP_CMD = "backup";
    public static final String ADMIN_DB_BACKUP_EDIT_CMD = "backupEdit";
    public static final String ADMIN_DB_BACKUP_EDIT_2_CMD = "backupEdit2";
    public static final String ADMIN_DB_BACKUP_EXECUTE = "backupExecute";

    public static final String ADMIN_COMPANY_CMD = "company";
    public static final String ADMIN_COMPANY_EDIT_CMD = "companyEdit";
    public static final String ADMIN_COMPANY_EDIT_2_CMD = "companyEdit2";

    public static final String ADMIN_EMAIL_CMD = "email";
    public static final String ADMIN_EMAIL_EDIT_CMD = "emailEdit";
    public static final String ADMIN_EMAIL_EDIT_2_CMD = "emailEdit2";

    public static final String ADMIN_FILE_CMD = "file";
    public static final String ADMIN_FILE_EDIT_CMD = "fileEdit";
    public static final String ADMIN_FILE_EDIT_2_CMD = "fileEdit2";

    public static final String ADMIN_POP_EMAIL_EDIT_CMD = "popEmailEdit";
    public static final String ADMIN_POP_EMAIL_EDIT_2_CMD = "popEmailEdit2";

    public static final String ADMIN_LDAP_TEST_CMD = "ldapTest";
    public static final String ADMIN_LDAP_TEST_2_CMD = "ldapTest2";

    public static final String ADMIN_LOOK_FEEL_CMD = "look";
    public static final String ADMIN_LOOK_FEEL_EDIT_CMD = "lookEdit";
    public static final String ADMIN_LOOK_FEEL_EDIT_2_CMD = "lookEdit2";

    public static final String ADMIN_LOGGING_EDIT_CMD = "loggingEdit";
    public static final String ADMIN_LOGGING_EDIT_2_CMD = "loggingEdit2";

    public static final String ADMIN_SYSTEM_INFO_CMD = "system";

    public static final String USER_DISPLAY_NAME = "user_display_name";
    public static final String USER_USERNAME = "username";

    static final Map objectTypeMap = new HashMap();
    static {
        objectTypeMap.put(ObjectTypes.HARDWARE, Attributes.HARDWARE_TYPE);
        objectTypeMap.put(ObjectTypes.SOFTWARE, Attributes.SOFTWARE_TYPE);
        objectTypeMap.put(ObjectTypes.ISSUE, Attributes.ISSUE_TYPE);
        objectTypeMap.put(ObjectTypes.CONTRACT, Attributes.CONTRACT_TYPE);
    }

    public static boolean isAttributeTypeMappingEnabled(Integer attrId) {
        if (attrId == null) {
            return false;
        }
        return objectTypeMap.containsValue(attrId);
    }

    /**
     * Given an objectTypeId, get a list of values from requests.
     * @param request
     * @param objectTypeId
     * @return
     * @throws DatabaseException
     */
    public static void populateCustomFieldValues(RequestContext requestContext, BaseObjectForm form, BaseObject baseObject,
                                                 Map<Integer, Attribute> customAttributes) {

        for (Attribute attr : customAttributes.values()) {
            AttributeValue value = new AttributeValue();
            value.setAttributeId(attr.getId());
            value.setAttributeValue(requestContext.getParameterString("attrId"+attr.getId()));
            baseObject.getCustomValues().put(attr.getId(), value);
        }

        form.setCustomValues(baseObject.getCustomValues());
    }

    public static void populateCustomFieldValues(Map<String, String> importCustomFields, BaseObject baseObject,
                                                 Map<Integer, Attribute> customAttributes) {

        for (Attribute attr : customAttributes.values()) {
            String attrValue = importCustomFields.get(attr.getName());
            if (attrValue != null) {
                AttributeValue value = new AttributeValue();
                value.setAttributeId(attr.getId());
                value.setAttributeValue(attrValue);
                baseObject.getCustomValues().put(attr.getId(), value);
            }
        }
    }

    /**
     * @param contact
     * @param request
     * @return ..
     */
    public static DetailTableTemplate formatUserContact(Contact contact, RequestContext requestContext, boolean showAdminNotes) throws DatabaseException {
        DetailTableTemplate template = new DetailTableTemplate();
        template.setNumColumns(2);

        DetailTableTemplate.Td td = template.new Td();
        td.setHeaderKey("common.column.contact_id");
        td.setValue(contact.getId() == 0 ? "" : String.valueOf(contact.getId()));
        template.addTd(td);

        td = template.new Td();
        td.setHeaderKey("common.column.company_name");
        td.setValue(Links.getCompanyDetailsLink(requestContext, contact.getCompanyName(), contact.getCompanyId()).getString());
        template.addTd(td);

        td = template.new Td();
        td.setHeaderKey("common.column.contact_title");
        td.setValue(HtmlUtils.encode(contact.getTitle()));
        template.addTd(td);

        td = template.new Td();
        td.setHeaderKey("common.column.contact_phone_work");
        td.setValue(HtmlUtils.encode(contact.getPhoneWork()));
        template.addTd(td);

        td = template.new Td();
        td.setHeaderKey("common.column.contact_phone_home");
        td.setValue(HtmlUtils.encode(contact.getPhoneHome()));
        template.addTd(td);

        td = template.new Td();
        td.setHeaderKey("common.column.contact_phone_mobile");
        td.setValue(HtmlUtils.encode(contact.getPhoneMobile()));
        template.addTd(td);

        td = template.new Td();
        td.setHeaderKey("common.column.contact_fax");
        td.setValue(HtmlUtils.encode(contact.getFax()));
        template.addTd(td);

        td = template.new Td();
        td.setHeaderKey("common.column.contact_address_street_primary");
        td.setValue(HtmlUtils.formatMultiLineDisplay(contact.getAddressStreetPrimary()));
        template.addTd(td);

        td = template.new Td();
        td.setHeaderKey("common.column.contact_address_city_primary");
        td.setValue(HtmlUtils.formatMultiLineDisplay(contact.getAddressCityPrimary()));
        template.addTd(td);

        td = template.new Td();
        td.setHeaderKey("common.column.contact_address_state_primary");
        td.setValue(HtmlUtils.formatMultiLineDisplay(contact.getAddressStatePrimary()));
        template.addTd(td);

        td = template.new Td();
        td.setHeaderKey("common.column.contact_address_zipcode_primary");
        td.setValue(HtmlUtils.formatMultiLineDisplay(contact.getAddressZipcodePrimary()));
        template.addTd(td);

        td = template.new Td();
        td.setHeaderKey("common.column.contact_address_country_primary");
        td.setValue(HtmlUtils.formatMultiLineDisplay(contact.getAddressCountryPrimary()));
        template.addTd(td);

        td = template.new Td();
        td.setHeaderKey("common.column.contact_email_secondary");
        td.setValue(HtmlUtils.formatMailtoLink(contact.getEmailSecondary()));
        template.addTd(td);

        td = template.new Td();
        td.setHeaderKey("common.column.contact_homepage_url");
        td.setValue(HtmlUtils.formatExternalLink(requestContext, contact.getHomepageUrl()));
        template.addTd(td);

        td = template.new Td();
        if (contact.getMessenger1TypeAttribute(requestContext).isEmpty()) {
            td.setHeaderKey("common.column.contact_im");
        } else {
            td.setHeaderText(Localizer.getText(requestContext, "common.column.contact_im_not_null", new Object[]{HtmlUtils.encode(contact.getMessenger1TypeAttribute(requestContext))}));
        }
        td.setValue(HtmlUtils.encode(contact.getMessenger1Id()));
        template.addTd(td);

        td = template.new Td();
        if (contact.getMessenger2TypeAttribute(requestContext).isEmpty()) {
            td.setHeaderKey("common.column.contact_im");
        } else {
            td.setHeaderText(Localizer.getText(requestContext, "common.column.contact_im_not_null", new Object[]{HtmlUtils.encode(contact.getMessenger2TypeAttribute(requestContext))}));
        }
        td.setValue(HtmlUtils.encode(contact.getMessenger2Id()));
        template.addTd(td);

        if (showAdminNotes) {
            td = template.new Td();
            td.setHeaderKey("common.column.contact_description");
            td.setValue(HtmlUtils.formatMultiLineDisplay(contact.getDescription()));
            template.addTd(td);
        }
        return template;
    }

    public static List userTabList(RequestContext requestContext, AccessUser appUser) throws DatabaseException {
        AccessUser user = requestContext.getUser();

        List tabList = new ArrayList();

        // Link to User Contact view.
        if (Access.hasPermission(user, AppPaths.ADMIN_USER_DETAIL)) {
            Map map = new HashMap();
            map.put("tabName", "contactTab");
            map.put("tabPath", AppPaths.ADMIN_USER_DETAIL + "?userId=" + appUser.getId());
            map.put("tabText", Localizer.getText(requestContext, "admin.user.tab.contact"));
            tabList.add(map);
        }

        // Link to User Access view.
        if (Access.hasPermission(user, AppPaths.ADMIN_USER_ACCESS)) {
            Map map = new HashMap();
            map.put("tabName", AdminTabs.USER_ACCESS_TAB);
            map.put("tabPath", AppPaths.ADMIN_USER_ACCESS + "?userId=" + appUser.getId());
            map.put("tabText", Localizer.getText(requestContext, "admin.cmd.userAccess"));
            tabList.add(map);
        }

        // Link to Assigned Hardware tab.
        if (Access.hasPermission(user, AppPaths.ADMIN_USER_HARDWARE)) {
            Map map = new HashMap();
            map.put("tabName", "hardwareTab");
            map.put("tabPath", AppPaths.ADMIN_USER_HARDWARE + "?userId=" + appUser.getId());
            map.put("tabText", Localizer.getText(requestContext, "admin.user.tab.hardware", new Object[]{appUser.getHardwareCount()}));
            tabList.add(map);
        }
        return tabList;
    }

    /**
     * This is to validate currency symbol.
     * For now, we accept everything except comma.
     *
     * @param input
     * @return ..
     */
    public static boolean validCurrencySymbol(Object input) {
        if (input != null) {
            if (input.toString().contains(",")) {
                return false;
            }
        }
        return true;
    }

    /**
     * This determines whether the Edit Access page should disable the
     * editing of permission.
     * @param user
     * @return
     */
    public static boolean disableAccessEdit(AccessUser user) {
        if (user.getId() == 1) {
            return true;

        } else if (user.getGroupId() != 0) {
            return true;
        }
        return false;
    }

    /**
     * Returns allowed sortable columns.
     * @return
     */
    public static List getSortableUserColumns() {
        return Arrays.asList(AccessUser.USERNAME, AccessUser.FIRST_NAME, AccessUser.LAST_NAME, AccessUser.DISPLAY_NAME,
                AccessUser.EMAIL);
    }

    /**
     * Returns whether a column can be sorted.
     *
     * @param columnName
     * @return ..
     */
    public static boolean isSortableUserColumn(String columnName) {
        return getSortableUserColumns().contains(columnName);
    }

    /**
     * Return the column header for user list page.
     */
    public static List getUserColumnHeaders() {
        return Arrays.asList(AccessUser.ROWNUM, AccessUser.USERNAME, AccessUser.FIRST_NAME, AccessUser.LAST_NAME,
                AccessUser.DISPLAY_NAME, AccessUser.EMAIL, AccessUser.STATUS);
    }

    public static List getUserAccessOptionList() {
        return Arrays.asList(new LabelValueBean("<img src=\"" + Image.getInstance().getPermissionYesIcon() + "\">", "1"),
                new LabelValueBean("<img src=\"" + Image.getInstance().getPermissionNoIcon() + "\">", "0"));
    }

    public static String getUserAccessIcon(boolean hasPermission) {
        if (hasPermission) {
            return "<img src=\"" + Image.getInstance().getPermissionYesIcon() + "\">";
        } else {
            return "<img src=\"" + Image.getInstance().getPermissionNoIcon() + "\">";
        }
    }

    public static List getAttributeStatusList(RequestContext requestContext) {
        return getAttributeStatusList(requestContext, false);
    }

    public static List getAttributeStatusList(RequestContext requestContext, boolean isDefaultAttr) {
        List<LabelValueBean> list = new ArrayList();
        list.add(new LabelValueBean(Localizer.getText(requestContext, "common.boolean.enabled_disabled.enabled"), "0"));

        if (!isDefaultAttr) {
            list.add(new LabelValueBean(Localizer.getText(requestContext, "common.boolean.enabled_disabled.disabled"), "1"));
        }
        return list;
    }

    public static String[] getBackupCommand() {
        SimpleDateFormat formatter = new SimpleDateFormat("yyyyMMddHHmm");

        String backupFileLocation = ConfigManager.file.getDbBackupRepositoryPath()
                + ConfigManager.system.getTrailingSlash()
                + "BACKUP-"
                + AppProperties.get(AppProperties.DB_NAME_KEY)
                + "-"
                + formatter.format(Calendar.getInstance().getTime())
                + ConfigManager.file.getPostgresBackupExtension();

        return new String[]{ConfigManager.file.getDbPostgresProgramPath(),
                "-f", backupFileLocation,
                "-h", AppProperties.get(AppProperties.DB_SERVERHOST_KEY),
                "-p", AppProperties.get(AppProperties.DB_SERVERPORT_KEY),
                "-U", AppProperties.get(AppProperties.DB_USERNAME_KEY),
                "-i", "-F", "p",
                AppProperties.get(AppProperties.DB_NAME_KEY)
        };
    }

    public static String getBackupCommandDisplay() {
        String backupFileLocation = ConfigManager.file.getDbBackupRepositoryPath()
                + ConfigManager.system.getTrailingSlash()
                + "BACKUP-"
                + AppProperties.get(AppProperties.DB_NAME_KEY)
                + "-<timestamp>"
                + ConfigManager.file.getPostgresBackupExtension();

        StringBuilder command = new StringBuilder();
        command.append("\"").append(ConfigManager.file.getDbPostgresProgramPath()).append("\"");
        command.append(" -f ").append("\"").append(backupFileLocation).append("\"");
        command.append(" -h ").append(AppProperties.get(AppProperties.DB_SERVERHOST_KEY));
        command.append(" -p ").append(AppProperties.get(AppProperties.DB_SERVERPORT_KEY));
        command.append(" -U ").append(AppProperties.get(AppProperties.DB_USERNAME_KEY));
        command.append(" -i -F p ");
        command.append(AppProperties.get(AppProperties.DB_NAME_KEY));

        return command.toString();
    }

    public static String getTitleText(RequestContext requestContext, String titleText) {
        StringBuilder title = new StringBuilder();
        title.append(ConfigManager.system.getCompanyName().isEmpty() ?
                Localizer.getText(requestContext, "common.app.shortName") : ConfigManager.system.getCompanyName());

        if (titleText != null) {
            title.append(" - ");
            title.append(titleText);
        }
        return title.toString();
    }

    public static List getGroupOptions(RequestContext requestContext) throws DatabaseException {
        AdminService adminService = ServiceProvider.getAdminService(requestContext);
        List<AccessGroup> groups = adminService.getGroups(new QueryBits());
        List groupIdOptions = new ArrayList();
        groupIdOptions.add(new SelectOneLabelValueBean(requestContext, "0"));
        for (AccessGroup group : groups) {
            groupIdOptions.add(new LabelValueBean(group.getName(), String.valueOf(group.getId())));
        }
        return groupIdOptions;
    }

    /**
     * Returns system username, this function also handles users who have been removed.
     * @param request
     * @param username
     * @return
     */
    public static String getSystemUsername(RequestContext requestContext, AccessUser accessUser) {
        if (accessUser == null || accessUser.getId() == 0) {
            return "";

        } else {
            String username = ConfigManager.system.getUsernameDisplay().equals(AdminUtils.USER_USERNAME) ? accessUser.getUsername() : accessUser.getDisplayName();
            return getSystemUsername(requestContext, accessUser.getId(), username);
        }
    }

    public static String getSystemUsername(RequestContext requestContext, Integer userId, String username) {
        if (userId == 0) {
            return "";

        } else {
            // We have user id but not username. This means the user has been removed.
            if (username == null || username.isEmpty()) {
                username = Localizer.getText(requestContext, "admin.userDelete.userRemoved");
            }
            return username;
        }
    }

    public static String getUsernameSort() {
        return ConfigManager.system.getUsernameDisplay().equals(AdminUtils.USER_USERNAME) ? AccessUser.USERNAME : AccessUser.DISPLAY_NAME;
    }

    public static Map<Integer, Integer> getObjectTypeMap() {
        return objectTypeMap;
    }

    /**
     * Enabled users sort by display name.
     * @return
     */
    public static List<LabelValueBean> getUserOptions(RequestContext requestContext) throws Exception {
        return getUserOptions(requestContext, null);
    }

    /**
     * Returns a list of enabled users (plus the one specified), sort by display name.
     * @param request
     * @param owner
     * @return
     * @throws Exception
     */
    public static List<LabelValueBean> getUserOptions(RequestContext requestContext, AccessUser includedUser) throws Exception {
        // We only want Users whose status is "Enable"
        UserSearch userSearch = new UserSearch();

        if (includedUser != null) {
            userSearch.put(UserSearch.NON_DISABLED, includedUser.getId());
        } else {
            userSearch.put(UserSearch.USER_STATUS, AttributeFieldIds.USER_STATUS_ENABLED);
        }

        // Sort by display name
        QueryBits query = new QueryBits(userSearch);
        query.addSortColumn(AdminQueries.getOrderByColumn(getUsernameSort()));

        AdminService adminService = ServiceProvider.getAdminService(requestContext);

        // User options
        List<LabelValueBean> userOptions = new ArrayList();

        for (AccessUser user : adminService.getUsers(query)) {
            userOptions.add(new LabelValueBean(getSystemUsername(requestContext, user), String.valueOf(user.getId())));
        }
        return userOptions;
    }

    /**
     * Validates required attributes
     * @param request
     * @param errors
     * @param baseObject
     */
    public static void validateAttributeValues(RequestContext requestContext, ActionMessages errors, BaseObject baseObject,
                                                Map<Integer, Attribute> customAttributes) {

        // Validate system attributes
        Map<Integer, Attribute> map = new AttributeManager(requestContext).getSystemAttributes(baseObject);

        for (Attribute attr : map.values()) {
            if (attr.isRequired() && baseObject.isAttrEmpty(attr.getName())) {
                String requiredFieldName = Localizer.getText(requestContext, "common.column." + attr.getName());
                errors.add(attr.getName(), new ActionMessage("common.form.fieldRequired", requiredFieldName));
            }
        }

        // Validate custom attributes
        if (baseObject.getCustomValues() != null) {
            for (AttributeValue attrValue : baseObject.getCustomValues().values()) {
                Attribute attr = customAttributes.get(attrValue.getAttributeId());
                if (attr.getType().equals(Attribute.ATTR_TYPE_DATE)) {
                    if (!DatetimeUtils.isValidDateValue(attrValue.getAttributeValue())) {
                        errors.add(attr.getName(), new ActionMessage("common.form.fieldDateInvalid", attr.getName()));
                    } else {
                        attrValue.setRawValue(new CustomFieldFormatter(attr, attrValue.getAttributeValue()).getAttributeRawValue());
                    }
                } else if (attr.getType().equals(Attribute.ATTR_TYPE_CURRENCY)) {
                    attrValue.setRawValue(new CustomFieldFormatter(attr, attrValue.getAttributeValue()).getAttributeRawValue());
                    if (!CurrencyUtils.isValidFormat(attrValue.getRawValue())) {
                        errors.add(attr.getName(), new ActionMessage("common.form.fieldFormatError", attr.getName()));
                    }
                } else if (!attr.getInputMask().isEmpty() && !attrValue.getAttributeValue().isEmpty()) {
                    try {
                        MaskFormatter formatter = new MaskFormatter(attr.getInputMask());
                        formatter.stringToValue(attrValue.getAttributeValue());
                        attrValue.setRawValue(formatter.valueToString(attrValue.getAttributeValue()));
                    } catch (Exception e) {
                        errors.add(attr.getName(), new ActionMessage("common.form.fieldFormatError", attr.getName(), attr.getDescription()));
                    }
                }
            }
        }
    }

    public static String getAttributeGroupKey(RequestContext requestContext, AttributeGroup group) {
        String key = "";

        if (group != null) {
            key = HtmlUtils.encode(group.getName()) + "<a href=\"" + group.getId() + "\"></a>";

        } else if (requestContext != null) {
            key = "<a href=\"0\"></a>" + Localizer.getText(requestContext, "common.template.customFields");
        }
        return key;
    }

    public static String getAttributeGroupKey(AttributeGroup group) {
        return getAttributeGroupKey(null, group);
    }

    public static List<LabelValueBean> getAttributeGroupOptions(RequestContext requestContext, Integer objectTypeId) throws Exception {
        Map<Integer, AttributeGroup> groupMap = new CacheManager(requestContext).getCustomAttrGroupsCache(objectTypeId);

        List<LabelValueBean> attrGroupOptions = new ArrayList();
        if (!groupMap.values().isEmpty()) {
            attrGroupOptions.add(new SelectOneLabelValueBean(requestContext, "0"));

            for (AttributeGroup group : groupMap.values()) {
                attrGroupOptions.add(new LabelValueBean(group.getName(), String.valueOf(group.getId())));
            }
        }
        return attrGroupOptions;
    }

    public static String getPermissionLabel(RequestContext requestContext, String permName) {
        StringBuilder accessText = new StringBuilder();
        accessText.append(Localizer.getText(requestContext, "system.permissions." + permName));
        accessText.append(": ");

        String permDesc = Localizer.getText(requestContext, "system.permissions." + permName + ".desc");
        if (permDesc != null) {
            Link link = new Link(requestContext);
            link.setImgSrc(Image.getInstance().getInfoIcon());
            link.setImgAltText(permDesc);
            accessText.append(link);
        }
        return accessText.toString();
    }
}
