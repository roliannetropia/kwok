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
package com.kwoksys.action.common.template;

import com.kwoksys.biz.base.BaseTemplate;
import com.kwoksys.biz.admin.core.AdminUtils;
import com.kwoksys.biz.admin.dto.AccessUser;
import com.kwoksys.biz.auth.core.Access;
import com.kwoksys.biz.portal.dto.Site;
import com.kwoksys.biz.system.core.AppPaths;
import com.kwoksys.biz.system.core.Image;
import com.kwoksys.framework.properties.Localizer;
import com.kwoksys.biz.system.core.Modules;
import com.kwoksys.framework.http.RequestContext;
import com.kwoksys.biz.system.core.configs.ConfigManager;
import com.kwoksys.framework.exceptions.DatabaseException;
import com.kwoksys.framework.session.CacheManager;
import com.kwoksys.framework.session.SessionManager;
import com.kwoksys.framework.ui.CssStyles;
import com.kwoksys.framework.ui.Link;
import com.kwoksys.framework.util.HtmlUtils;
import com.kwoksys.framework.util.StringUtils;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * HeaderTemplate class.
 */
public class HeaderTemplate extends BaseTemplate {

    private String pageTitleKey;

    private Object[] pageTitleKeyParams;

    /** Page title and main title key under header command bar **/
    private String titleKey;

    private String titleText;

    private Object[] titleKeyParams;

    /** CSS style for title **/
    private String titleClass;

    private List headerCmds = new ArrayList();
    private List<Link> navLinks = new ArrayList();
    private String jqueryDateFormat;
    private String fontSize;
    private Integer moduleId;

    String appLogoPath;

    private String notificationMsg;

    public HeaderTemplate() {
        super(HeaderTemplate.class);
    }

    public void applyTemplate() throws DatabaseException {
        AccessUser user = requestContext.getUser();

        String sessionTheme = SessionManager.getAppSessionTheme(request.getSession());
        List moduleTabs = new ArrayList();

        moduleId = (Integer) request.getAttribute(RequestContext.MODULE_KEY);
        for (String strModuleId : ConfigManager.system.getModuleTabs()) {
            Integer thisModuleId = Integer.parseInt(strModuleId);
            String modulePath = Modules.getModulePath(thisModuleId);

            if (Access.hasPermission(user, modulePath)) {
                Map moduleMap = new HashMap();
                moduleMap.put("modulePath", AppPaths.ROOT + modulePath);
                moduleMap.put("moduleId", thisModuleId);
                moduleMap.put("moduleName", strModuleId);
                moduleMap.put("moduleActive", "true");
                moduleTabs.add(moduleMap);
            }
        }

        // Show portal sites that are shown as tabs
        List<Site> sites = new CacheManager(requestContext).getModuleTabsCache();
        for (Site site : sites) {
            Map moduleMap = new HashMap();
            moduleMap.put("modulePath", HtmlUtils.encode(site.getPath()));
            moduleMap.put("moduleName", site.getName());
            moduleMap.put("moduleActive", "custom");
            moduleTabs.add(moduleMap);
        }

        if (user.isLoggedOn()) {
            Object[] args = {AdminUtils.getSystemUsername(requestContext, user)};
            request.setAttribute("Header_welcomeUserMessage", Localizer.getText(requestContext, "core.template.header.welcomeUser", args));

            if (!ConfigManager.auth.isBasicAuth()) {
                Link link = new Link(requestContext);
                link.setImgSrc(Image.getInstance().getLogout());
                link.setAppPath(AppPaths.AUTH_LOGOUT);
                link.setTitleKey("core.template.header.logout");
                request.setAttribute("Header_logoutPath", link.getString());
            }

            // Let's see if the user has access to user preference module.
            if (Access.hasPermission(user, AppPaths.USER_PREF_INDEX)) {
                Link link = new Link(requestContext);
                link.setImgSrc(Image.getInstance().getPreference());
                link.setAjaxPath(AppPaths.USER_PREF_INDEX);
                link.setTitleKey("core.template.header.userPreference");
                request.setAttribute("Header_userPreferencePath", link.getString());
            }

            // Show admin path if user has permission to see the page
            if (Access.hasPermission(user, AppPaths.ADMIN_INDEX)) {
                Link link = new Link(requestContext);
                link.setImgSrc(Image.getInstance().getAdminIcon());
                link.setAjaxPath(AppPaths.ADMIN_INDEX);
                link.setTitleKey("admin.index.title");
                request.setAttribute("Header_adminPath", link.getString());
            }
        }

        // Navigation commands
        if (!navLinks.isEmpty()) {
            StringBuilder sb = new StringBuilder();
            for (Link link : navLinks) {
                if (sb.length() != 0) {
                    sb.append(" &raquo; ");
                }
                sb.append(link);
            }
            request.setAttribute("Header_navCmds", sb.toString());
        }

        Link link = new Link(requestContext);
        if (!ConfigManager.system.getCompanyLogoPath().isEmpty()) {
            link.setImgSrc(ConfigManager.system.getCompanyLogoPath());
        } else {
            link.setImgSrc(Image.getInstance().getAppLogo());
        }

        link.setExternalPath(ConfigManager.system.getCompanyPath());
        appLogoPath = link.getString();

        String pageTitle = null;

        if (titleKey != null) {
            pageTitle = Localizer.getText(requestContext, titleKey, titleKeyParams);
            request.setAttribute("Header_title", pageTitle);
        }

        if (pageTitleKey != null) {
            pageTitle = Localizer.getText(requestContext, pageTitleKey, pageTitleKeyParams);
        }

        if (fontSize == null || fontSize.isEmpty()) {
            fontSize = SessionManager.getAttribute(request, SessionManager.FONT_SIZE, String.valueOf(ConfigManager.system.getFontOptions()[0]));
        }

        titleText = AdminUtils.getTitleText(requestContext, pageTitle);

        request.setAttribute("Header_themeStylePath", AppPaths.getInstance().getThemeCss(sessionTheme));
        request.setAttribute("Header_stylesheet", ConfigManager.system.getSytlesheet());
        request.setAttribute("Header_moduleTabs", moduleTabs);

        // Replace yy as y because jquery treats y as two-digit year, yy becomes four digits
        jqueryDateFormat = ConfigManager.system.getDateFormat().toLowerCase().replace("yy", "y");
    }

    @Override
    public String getJspPath() {
        return "/WEB-INF/jsp/common/template/HeaderTemplate.jsp";
    }

    public void setTitleClassNoLine() {
        this.titleClass = CssStyles.NO_LINE;
    }

    public void addNavLink(Link link) {
        if (link != null) {
            navLinks.add(link);
        }
    }

    public void addHeaderCmds(Link headerCmd) {
        headerCmd.setStyleClass(CssStyles.BTN);
        headerCmds.add(headerCmd);
    }

    public List<Link> getHeaderCmds() {
        return headerCmds;
    }

    public void setTitleKey(String titleKey, Object[] params) {
        this.titleKey = titleKey;
        this.titleKeyParams = params;
    }

    public String getTitleKey() {
        return titleKey;
    }

    public void setTitleKey(String titleKey) {
        this.titleKey = titleKey;
    }

    public String getTitleClass() {
        return titleClass;
    }

    public void setPageTitleKey(String pageTitleKey) {
        this.pageTitleKey = pageTitleKey;
    }

    public void setPageTitleKey(String pageTitleKey, Object[] params) {
        this.pageTitleKey = pageTitleKey;
        this.pageTitleKeyParams = params;
    }

    public String getJqueryDateFormat() {
        return jqueryDateFormat;
    }

    public String getFontSize() {
        return fontSize;
    }

    public void setFontSize(String fontSize) {
        this.fontSize = fontSize;
    }

    public String getTitleText() {
        return titleText;
    }

    public Integer getModuleId() {
        return moduleId;
    }

    public String getModuleIds() {
        return StringUtils.join(ConfigManager.system.getModuleTabs(), ",");
    }

    public String getAppLogoPath() {
        return appLogoPath;
    }

    public String getNotificationMsg() {
        return notificationMsg;
    }

    public void setNotificationMsg(String notificationMsg) {
        this.notificationMsg = notificationMsg;
    }
}
