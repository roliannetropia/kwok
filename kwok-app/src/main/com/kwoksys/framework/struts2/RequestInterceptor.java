/*
 * ====================================================================
 * Copyright 2012 Fishdox
 * ====================================================================
 */
package com.kwoksys.framework.struts2;

import com.kwoksys.biz.ServiceProvider;
import com.kwoksys.biz.admin.AdminService;
import com.kwoksys.biz.admin.dto.AccessUser;
import com.kwoksys.biz.auth.AuthService;
import com.kwoksys.biz.auth.core.Access;
import com.kwoksys.biz.auth.core.AuthUtils;
import com.kwoksys.biz.auth.dto.AccessPage;
import com.kwoksys.biz.system.SystemService;
import com.kwoksys.biz.system.core.AppPaths;
import com.kwoksys.biz.system.core.configs.ConfigManager;
import com.kwoksys.biz.system.core.configs.LogConfigManager;
import com.kwoksys.biz.system.dto.SystemInfo;
import com.kwoksys.framework.exceptions.AccessDeniedException;
import com.kwoksys.framework.exceptions.DatabaseException;
import com.kwoksys.framework.exceptions.ObjectNotFoundException;
import com.kwoksys.framework.http.RequestContext;
import com.kwoksys.framework.http.ResponseContext;
import com.kwoksys.framework.properties.Localizer;
import com.kwoksys.framework.servlets.SystemInitServlet;
import com.kwoksys.framework.session.CacheManager;
import com.kwoksys.framework.session.CookieManager;
import com.kwoksys.framework.session.SessionManager;
import com.opensymphony.xwork2.ActionInvocation;
import com.opensymphony.xwork2.ActionProxy;
import com.opensymphony.xwork2.interceptor.AbstractInterceptor;
import org.apache.struts2.ServletActionContext;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.FileNotFoundException;
import java.util.Enumeration;
import java.util.logging.Level;
import java.util.logging.Logger;

public class RequestInterceptor extends AbstractInterceptor {

    private static final Logger logger = Logger.getLogger(RequestInterceptor.class.getName());

	public String intercept(ActionInvocation actionInvocation) throws Exception {
        RequestContext requestContext = new RequestContext(ServletActionContext.getRequest());
        ResponseContext responseContext = new ResponseContext(ServletActionContext.getResponse());

        ActionProxy actionProxy = actionInvocation.getProxy();
        requestContext.setActionConfig(actionProxy.getConfig());

        Action2 action2 = (Action2) actionProxy.getAction();
        action2.setRequestContext(requestContext);

        try {
            requestContext.setPageName("/" + actionProxy.getActionName() + ConfigManager.system.getExtension());

            if (!validate(requestContext, responseContext)) {
                return null;
            }

            return actionProxy.getInvocation().invoke();

        } catch (ObjectNotFoundException e) {
            throw e;
        } catch (AccessDeniedException e) {
            throw e;
        } catch (FileNotFoundException e) {
            throw e;
        } catch (Exception e) {
            logger.log(Level.SEVERE, "Problem processing request.", e);
            responseContext.sendServerError();
            return null;
        }
    }

    public static boolean validate(RequestContext requestContext, ResponseContext responseContext) throws Exception {
        HttpServletRequest request = requestContext.getRequest();
        HttpServletResponse response = responseContext.getResponse();

        // This line is required for utf-8 encoding to work.
        request.setCharacterEncoding(ConfigManager.system.getCharacterEncoding());

        // These lines are for making the app not to cache pages.
        // Especially important for the AJAX to work correctly.
        response.setHeader("Pragma", "no-cache"); // For HTTP/1.0 backward compatibility.
        response.setHeader("Cache-Control", "no-cache"); // For HTTP/1.1.

        response.setCharacterEncoding(ConfigManager.system.getCharacterEncoding());

        // Pass pageStartTime to jsp pages.
        request.setAttribute(RequestContext.PAGE_START_TIME, System.currentTimeMillis());

        // Checks database availability by executing a query, also check to see if the current cache key matches
        // cached cache key. This is for cache flushing across multiple servers.
        SystemService systemService = ServiceProvider.getSystemService(requestContext);

        if (!SystemInitServlet.init) {
            logger.warning("System not initialized. " + SystemInitServlet.initError);
            responseContext.sendServiceUnavailable();
            return false;
        }

        SystemInfo systemInfo;
        try {
            systemInfo = systemService.getSystemInfo();
            if (!systemInfo.getCacheKey().equals(ConfigManager.system.getCacheKey())) {
                // Refresh the cache.
                ConfigManager.init();
            }

            // Run a process to check if there are any caches to remove
            new CacheManager(requestContext).checkRemoveCaches(systemInfo.getSysdate().getTime());

        } catch (DatabaseException e) {
            // Don't log anything
            responseContext.sendServiceUnavailable();
            return false;
        }

        HttpSession session = request.getSession();

        // Do this once for a session
        initSession(session);

        String pageName = requestContext.getPageName();

        AccessPage accessPage = Access.getAccessPage(pageName);
        Integer moduleId = accessPage == null ? 0 : accessPage.getModuleId();

        request.setAttribute(RequestContext.MODULE_KEY, moduleId);
        request.setAttribute(RequestContext.SYSDATE, systemInfo.getSysdate());

        Cookie[] cookies = request.getCookies();
        AccessUser user = Access.getCookieUser(cookies);

        String sessionToken = CookieManager.getSessionToken(cookies).trim();

        // Ready to serve the page, let's log it first.
        logger.info(LogConfigManager.PAGE_REQUEST_PREFIX + " " + AppPaths.ROOT + requestContext.getPageName() + ", user ID: " + user.getId());

        AuthService authService = ServiceProvider.getAuthService(requestContext);
        boolean isValidSessionToken = authService.isValidUserSession(user.getId(), sessionToken);

        // Check if the session is valid
        if (!isValidSessionToken) {
            if (ConfigManager.auth.isBasicAuth()) {
                if (!authService.isValidBasicAuthentication(user)) {
                    Access.requestBasicAuthCredential(requestContext, responseContext);
                    return false;
                } else {
                    // Initialize user session
                    authService.initializeUserSession(request, response, user);
                }
            } else if (user.isLoggedOn()) {
                // If the user is already logged on, without a valid session token, empty auth cookies
                AuthUtils.resetAuthCookies(response);
            }
        }

        // This sounds un-necessary but during basic authentication, we could get a different id than
        // what's stored in cookies.
        AdminService adminService = ServiceProvider.getAdminService(requestContext);
        user = adminService.getUser(user.getId());

        request.setAttribute(RequestContext.USER_KEY, user);

        if (Access.isPublicPage(pageName)) {
            // No permission checking required for public pages.

        } else if (accessPage == null) {
            // We don't have such page, show a 404.
            throw new FileNotFoundException();

        } else if (!Access.hasSpecialPagePermission(user, accessPage)) {
            throw new AccessDeniedException();

        } else if (!isValidSessionToken && !ConfigManager.auth.isBasicAuth() && user.isLoggedOn()) {
            Access.requestCredential(requestContext, responseContext, "sessionExpired");
            return false;

        // We check whether the user is allowed to see pages that require permission.
        } else if (!Access.hasPermission(user, pageName)) {
            if (!user.isLoggedOn()) {
                // Request username/password with error code loginRequired.
                Access.requestCredential(requestContext, responseContext, "loginRequired");
                return false;
            } else {
                throw new AccessDeniedException();
            }
        }
        return true;
    }

    private static void initSession(HttpSession session) {
        if (session.getAttribute(SessionManager.SESSION_INIT) == null) {
            session.setAttribute(SessionManager.SESSION_INIT, true);

            Localizer.setSessionLocale(session, ConfigManager.system.getLocaleString());
        }
    }

    private static void printRequest(HttpServletRequest request) {
        System.out.println("\n=== Headers ===");
        Enumeration headerNames = request.getHeaderNames();
        while (headerNames.hasMoreElements()) {
            String name = (String)headerNames.nextElement();
            System.out.println(name + ": " + request.getHeader(name));
        }

        System.out.println("\n=== Parameters ===");
        Enumeration parameterNames = request.getParameterNames();
        while (parameterNames.hasMoreElements()) {
            String name = (String)parameterNames.nextElement();
            System.out.println(name + ": " + request.getParameter(name));
        }
    }
}
