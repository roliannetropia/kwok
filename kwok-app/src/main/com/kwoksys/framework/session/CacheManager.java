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
package com.kwoksys.framework.session;

import com.kwoksys.biz.ServiceProvider;
import com.kwoksys.biz.admin.dto.AttributeGroup;
import com.kwoksys.biz.base.BaseManager;
import com.kwoksys.biz.portal.SiteSearch;
import com.kwoksys.biz.portal.PortalService;
import com.kwoksys.biz.portal.dto.Site;
import com.kwoksys.biz.portal.dao.PortalQueries;
import com.kwoksys.biz.admin.AdminService;
import com.kwoksys.biz.admin.core.AttributeSearch;
import com.kwoksys.biz.admin.dto.Attribute;
import com.kwoksys.biz.admin.dto.AttributeField;
import com.kwoksys.biz.auth.AuthService;
import com.kwoksys.biz.auth.dto.AccessPage;
import com.kwoksys.biz.system.SystemService;
import com.kwoksys.biz.system.core.AppPaths;
import com.kwoksys.framework.http.RequestContext;
import com.kwoksys.biz.system.core.configs.LogConfigManager;
import com.kwoksys.framework.exceptions.DatabaseException;
import com.kwoksys.framework.connections.database.QueryBits;
import com.kwoksys.biz.system.core.configs.ConfigManager;
import org.apache.jcs.JCS;
import org.apache.jcs.access.exception.CacheException;

import java.util.*;
import java.util.logging.Logger;
import java.util.logging.Level;

/**
 * This provides APIs to access Apache's Java Caching System.
 * Naming convention is get<Object>Cache(), cache<Object>(), recache<Object>(), purge<Object>Cache().
 * To use one of the methods from other classes, name it in a way as getCached<Object>().
 */
public class CacheManager extends BaseManager {

    // These caches don't change
    private static final String PAGES_INFO_CACHE = "pagesInfo";

    private static final String PERMISSION_PAGES_CACHE = "permissionPages";

    // These caches change but static
    private static final String SYSTEM_CONFIG_CACHE = "sysconfig";

    private static final String MODULE_TABS_CACHE = "moduleTabs";

    private static final String SYSTEM_ATTR_MAP_CACHE = "systemAttrMap";

    // These caches change and are dynamic based on ids
    private static final String SYSTEM_ATTR_FIELDS_CACHE = "systemAttrFields_";

    private static final String CUSTOM_ATTR_GROUPS_CACHE = "customAttrGroups_";

    private static final String GROUP_PERMISSION_CACHE = "groupPermissions_";

    private static final String USER_PERMISSION_CACHE = "userPermissions_";

    private static final Logger logger = Logger.getLogger(CacheManager.class.getName());

    private static JCS jcs;

    public CacheManager() {}

    public CacheManager(RequestContext requestContext) {
        super(requestContext);
    }

    static {
        try {
            logger.info(LogConfigManager.CONFIG_PREFIX + " Loading " + AppPaths.CACHE_CONFIG + " config file...");
            JCS.setConfigFilename(AppPaths.CACHE_CONFIG);
            jcs = JCS.getInstance("appCache");

        } catch (CacheException e) {
            logger.log(Level.SEVERE, "Failed to get instance of appCache.", e);
        }
    }

    public void checkRemoveCaches(Long newCacheTime) {
        try {
            Long cacheTime = ConfigManager.system.getCacheTime();

            if (cacheTime == null) {
                ConfigManager.system.setCacheTime(newCacheTime);
                return;
            }

            SystemService systemService = ServiceProvider.getSystemService(requestContext);
            List<String> cacheKeys = systemService.getFlushSystemCacheKeys(cacheTime);

            if (!cacheKeys.isEmpty()) {
                for (String cacheKey : cacheKeys) {
                    logger.info(LogConfigManager.CACHE_PREFIX + " Removing cache: " + cacheKey);
                    jcs.remove(cacheKey);
                }
                systemService.validateSystemCaches(newCacheTime);
                ConfigManager.system.setCacheTime(newCacheTime);
            }
        } catch (Exception e) {
            logger.severe("Problem removing cache... " + e.getMessage());
        }
    }

    private void updateCacheRecord(String cacheKey) {
        try {
            logger.info(LogConfigManager.CACHE_PREFIX + " Removing cache: " + cacheKey);
            jcs.remove(cacheKey);

            SystemService systemService = ServiceProvider.getSystemService(requestContext);
            systemService.resetSystemCache(cacheKey);

        } catch (Exception e) {
            logger.severe("Problem removing cache... " + e.getMessage());
        }
    }

    private static void addCache(String cacheKey, Object cacheObject) {
        logger.info(LogConfigManager.CACHE_PREFIX + " Creating cache: " + cacheKey);

        try {
            jcs.put(cacheKey, cacheObject);
        } catch (Exception e) {
            logger.log(Level.SEVERE, "Problem creating cache.", e);
        }
    }

    /**
     * Caches system configs.
     * @return
     */
    public Map cacheSystemConfigs() {
        Map configMap = null;
        try {
            jcs.remove(SYSTEM_CONFIG_CACHE);

            SystemService systemService = ServiceProvider.getSystemService(requestContext);
            configMap = systemService.getSystemConfig();

            // We put configMap in cache only when it's not null.
            // This helps detect whether database is unavailable in request processor servlet.
            if (!configMap.isEmpty()) {
                addCache(SYSTEM_CONFIG_CACHE, configMap);
            }
        } catch (Exception e) {
            logger.log(Level.SEVERE, "Problem creating cache.", e);
        }
        return configMap;
    }

    /**
     * Given an attributeId, returns attribute object.
     *
     * @return ..
     */
    public Attribute getSystemAttrCache(Integer attributeId) throws DatabaseException {
        Map attrMap = (Map) jcs.get(SYSTEM_ATTR_MAP_CACHE);

        if (attrMap == null) {
            AdminService adminService = ServiceProvider.getAdminService(requestContext);

            attrMap = new HashMap();

            AttributeSearch attributeSearch = new AttributeSearch();
            attributeSearch.put(AttributeSearch.IS_CUSTOM_ATTR, false);

            for (Attribute attr : adminService.getAttributes(new QueryBits(attributeSearch)).values()) {
                attrMap.put(attr.getId(), attr);
            }

            addCache(SYSTEM_ATTR_MAP_CACHE, attrMap);
        }

        return (Attribute)attrMap.get(attributeId);
    }

    public void removeSystemAttrCache() {
        updateCacheRecord(SYSTEM_ATTR_MAP_CACHE);
    }

    /**
     * Returns objectKey attributes cache.
     *
     * @return ..
     */
    public Map<Integer, AttributeField> getAttributeFieldsCache(Integer attributeId) throws DatabaseException {
        Map<Integer, AttributeField> attrFields = (Map) jcs.get(SYSTEM_ATTR_FIELDS_CACHE + attributeId);

        if (attrFields == null) {
            AdminService adminService = ServiceProvider.getAdminService(requestContext);
            attrFields = adminService.getAttributeFields(attributeId);

            addCache(SYSTEM_ATTR_FIELDS_CACHE + attributeId, attrFields);
        }
        return attrFields;
    }

    public void removeAttributeFieldsCache(Integer attributeId) {
        updateCacheRecord(SYSTEM_ATTR_FIELDS_CACHE + attributeId);
    }

    public Map<Integer, AttributeGroup> getCustomAttrGroupsCache(Integer objectTypeId) throws DatabaseException {
        Map<Integer, AttributeGroup> groupMap = (Map) jcs.get(CUSTOM_ATTR_GROUPS_CACHE + objectTypeId);

        if (groupMap == null) {
            groupMap = ServiceProvider.getAdminService(requestContext).getAttributeGroups(objectTypeId);
            addCache(CUSTOM_ATTR_GROUPS_CACHE + objectTypeId, groupMap);
        }
        return groupMap;
    }

    public void removeCustomAttrGroupsCache(Integer objectTypeId) {
        updateCacheRecord(CUSTOM_ATTR_GROUPS_CACHE + objectTypeId);
    }

    /**
     * Checks whether permission pages is empty, if so, redo the caching.
     *
     * @return ..
     */
    public Map<Integer, Set> getPermissionPagesCache() throws DatabaseException {
        Map<Integer, Set> permPages = (Map) jcs.get(PERMISSION_PAGES_CACHE);

        if (permPages == null) {
            AuthService authService = ServiceProvider.getAuthService(requestContext);
            permPages = authService.getAccessPermPages();

            addCache(PERMISSION_PAGES_CACHE, permPages);
        }
        return permPages;
    }

    /**
     * Returns cached group permissions
     *
     * @return ..
     */
    public Set<Integer> getGroupPermissionsCache(Integer groupId) throws DatabaseException {
        Set<Integer> permSet = (Set) jcs.get(GROUP_PERMISSION_CACHE + groupId);

        if (permSet == null) {
            AuthService authService = ServiceProvider.getAuthService(requestContext);
            permSet = authService.getAccessGroupPerms(groupId);

            addCache(GROUP_PERMISSION_CACHE + groupId, permSet);
        }
        return permSet;
    }

    public void removeGroupPermissionsCache(Integer groupId) {
        updateCacheRecord(GROUP_PERMISSION_CACHE + groupId);
    }

    /**
     * Returns cached user permissions
     *
     * @return ..
     */
    public Set<Integer> getUserPermissionsCache(Integer userId) throws DatabaseException {
        Set<Integer> permSet = (Set) jcs.get(USER_PERMISSION_CACHE + userId);

        if (permSet == null) {
            AuthService authService = ServiceProvider.getAuthService(requestContext);
            permSet = authService.getAccessUserPerms(userId);

            addCache(USER_PERMISSION_CACHE + userId, permSet);
        }
        return permSet;
    }

    public void removeUserPermissionsCache(Integer userId) {
        updateCacheRecord(USER_PERMISSION_CACHE + userId);
    }

    /**
     * Returns cached pages info map.
     *
     * @return ..
     */
    public Map<String, AccessPage> getPagesInfoCache() throws DatabaseException {
        Map pageMap = (Map) jcs.get(PAGES_INFO_CACHE);

        if (pageMap == null) {
            AuthService authService = ServiceProvider.getAuthService(requestContext);
            pageMap = authService.getAccessPages();

            addCache(PAGES_INFO_CACHE, pageMap);
        }
        return pageMap;
    }
   
    /**
     * Get module tabs cache, mostly for HeaderTemplate's module tabs.
     * @return
     */
    public List<Site> getModuleTabsCache() throws DatabaseException {
        List<Site> list = (List) jcs.get(MODULE_TABS_CACHE);

        if (list == null) {
            // Show portal sites that are shown as tabs
            SiteSearch siteSearch = new SiteSearch();
            siteSearch.put(SiteSearch.SHOW_ON_TAB, "");

            QueryBits query = new QueryBits(siteSearch);
            query.addSortColumn(PortalQueries.getOrderByColumn(Site.SITE_NAME));

            PortalService portalService = ServiceProvider.getPortalService(requestContext);

            list = portalService.getSites(query);

            addCache(MODULE_TABS_CACHE, list);
        }
        return list;
    }

    public void removeModuleTabsCache() {
        updateCacheRecord(MODULE_TABS_CACHE);
    }
}