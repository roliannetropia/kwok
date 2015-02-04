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
package com.kwoksys.biz.auth.core;

import com.kwoksys.biz.admin.dto.AccessUser;
import com.kwoksys.biz.auth.dto.AccessPage;
import com.kwoksys.biz.system.core.AppPaths;
import com.kwoksys.biz.system.core.FeatureManager;
import com.kwoksys.biz.system.core.configs.ConfigManager;
import com.kwoksys.framework.exceptions.DatabaseException;
import com.kwoksys.framework.http.RequestContext;
import com.kwoksys.framework.http.ResponseContext;
import com.kwoksys.framework.properties.Localizer;
import com.kwoksys.framework.session.CacheManager;
import com.kwoksys.framework.session.CookieManager;
import com.kwoksys.framework.util.NumberUtils;
import com.kwoksys.framework.util.UrlUtils;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import java.io.IOException;
import java.util.HashSet;
import java.util.Set;

/**
 * Access
 */
public class Access {

    public static final Integer ADMIN_USER_ID = 1;
    public static final Integer GUEST_USER_ID = -1001;

    public static final String AUTH_APP = "app";
    public static final String AUTH_LDAP = "ldap";
    public static final String AUTH_MIXED = "mixed";

    private static final Set publicPagesMap = new HashSet();

    private static final Set issuesGuestSubmitPages = new HashSet();

    public static final AccessUser GUESS_USER = new AccessUser(Access.GUEST_USER_ID);

    static {
        // Auth module
        publicPagesMap.add(AppPaths.AUTH_LOGOUT);
        publicPagesMap.add(AppPaths.AUTH_VERIFY);

        // Home module
        publicPagesMap.add(AppPaths.HOME_INDEX);
        publicPagesMap.add(AppPaths.HOME_UNAUTHORIZED);
        publicPagesMap.add(AppPaths.HOME_FORBIDDEN);
        publicPagesMap.add(AppPaths.HOME_FILE_NOT_FOUND);
        publicPagesMap.add(AppPaths.HOME_OBJECT_NOT_FOUND);

        // IssuePlugin module
        publicPagesMap.add(AppPaths.ISSUE_PLUGIN_ADD);
        publicPagesMap.add(AppPaths.ISSUE_PLUGIN_ADD_2);
        publicPagesMap.add(AppPaths.ISSUE_PLUGIN_ADD_3);
        publicPagesMap.add(AppPaths.ISSUE_PLUGIN_LEGEND_DETAIL);

        issuesGuestSubmitPages.add(AppPaths.ISSUE_PLUGIN_ADD);
        issuesGuestSubmitPages.add(AppPaths.ISSUE_PLUGIN_ADD_2);
        issuesGuestSubmitPages.add(AppPaths.ISSUE_PLUGIN_ADD_3);
        issuesGuestSubmitPages.add(AppPaths.ISSUE_PLUGIN_LEGEND_DETAIL);
    }

    /**
     * Return true if the module is a public page. Public page usually doesn't require authentication.
     * @return
     */
    public static boolean isPublicPage(String path) {
        return publicPagesMap.contains(path);
    }

    public static boolean hasSpecialPagePermission(AccessUser accessUser, AccessPage accessPage) {
        if (issuesGuestSubmitPages.contains(accessPage.getPath())) {
            return FeatureManager.isIssueGuestSubmitFooterEnabled() || FeatureManager.isIssueGuestSubmitModuleEnabled(accessUser);
        }
        return true;
    }

    /**
     * Returns whether a permission is enabled. Currently, we don't have any check.
     * @param permId
     * @return
     */
    public static boolean isPermissionEnabled(Integer permId) {
        return true;
    }

    /**
     * Returns whether a user is granted a given permission
     * @param request
     * @param permId
     * @return
     * @throws DatabaseException
     */
    public static boolean hasPermission(AccessUser user, Integer permId) throws DatabaseException {
        // We want default administrator to always have permission to see every page
        if (isDefaultAdmin(user.getId())) {
            return true;
        }
        Set permSet = getEffectivePermissions(user);
        return permSet.contains(permId);
    }

    /**
     * Returns whether the user can view a specific page.
     * to exist.
     */
    public static boolean hasPermission(AccessUser user, String pageName) throws DatabaseException {
        // If the user hasn't logged on yet, the user doesn't have much access
        if (user == null) {
            return false;
        }

        if (Access.isPublicPage(pageName)) {
            return true;
        }

        // Check pageId first to make sure the page is valid, before even letting admin see it
        AccessPage accessPage = getAccessPage(pageName);
        if (accessPage == null) {
            return false;
        }

        if (!Access.hasSpecialPagePermission(user, accessPage)) {
            return false;
        }

        // We want default administrator to always have permission to see every page
        if (isDefaultAdmin(user.getId())) {
            return true;
        }

        Set<Integer> permSet = getEffectivePermissions(user);

        // If the perm map has the page id, the user has permission to see
        for (Integer permId : permSet) {
            Set set = getCachedPermissionPages(permId);
            if (set != null && set.contains(accessPage.getPageId())) {
                return true;
            }
        }
        return false;
    }

    private static Set getEffectivePermissions(AccessUser user) throws DatabaseException {
        // If user belongs to a group, use group permission, otherwise, use the user's permission
        if (user.getGroupId() != 0) {
            return new CacheManager().getGroupPermissionsCache(user.getGroupId());
        } else {
            return new CacheManager().getUserPermissionsCache(user.getId());
        }
    }

    /**
     * Get access page object from pageName.
     * @param pageName
     * @return
     */
    public static AccessPage getAccessPage(String pageName) throws DatabaseException {
        return new CacheManager().getPagesInfoCache().get(pageName);
    }

    /**
     * Permission's associations with pages
     */
    private static Set getCachedPermissionPages(Integer permissionId) throws DatabaseException {
        return new CacheManager().getPermissionPagesCache().get(permissionId);
    }

    public static AccessUser getCookieUser(Cookie[] cookies) {
        Integer cookieUserId = NumberUtils.replaceNull(CookieManager.getUserId(cookies), GUEST_USER_ID);

        return new AccessUser(cookieUserId);
    }

    public static boolean isDefaultAdmin(Integer userId) {
        return (userId.equals(ADMIN_USER_ID));
    }

    public static void requestCredential(RequestContext requestContext, ResponseContext responseContext,
                                         String errorCode) throws IOException {
        HttpServletRequest request = requestContext.getRequest();
        StringBuilder sb = new StringBuilder();
        sb.append(AppPaths.ROOT + AppPaths.HOME_INDEX).append("?redirectPath=");

        if (!(AppPaths.AUTH_LOGOUT).equals(requestContext.getPageName())
                && !request.getMethod().equals("POST")) {
            // Also remove _ajax=true.
            String queryString = request.getQueryString() == null ? null : request.getQueryString().replace(RequestContext.URL_PARAM_AJAX + "=true", "");
            sb.append(UrlUtils.encode(UrlUtils.formatQueryString(requestContext.getPageName(), queryString)));
        }
        sb.append("&errorCode=").append(errorCode).append("&").append(RequestContext.URL_PARAM_ERROR_TRUE);
        request.setAttribute("redirectUrl", sb.toString());
        responseContext.getResponse().sendRedirect(sb.toString());
    }

    public static void requestBasicAuthCredential(RequestContext requestContext, ResponseContext responseContext) throws IOException {
        String realm = ConfigManager.system.getCompanyName().isEmpty()
                    ? Localizer.getText(requestContext, "common.app.name") : ConfigManager.system.getCompanyName();

        responseContext.getResponse().setHeader("WWW-Authenticate", "Basic realm=\"" + realm + "\"");
        responseContext.sendUnauthorized();
    }
}
