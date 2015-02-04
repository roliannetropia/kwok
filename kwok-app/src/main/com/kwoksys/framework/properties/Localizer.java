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
package com.kwoksys.framework.properties;

import com.kwoksys.biz.system.core.configs.LogConfigManager;
import com.kwoksys.framework.http.RequestContext;
import com.kwoksys.framework.session.SessionManager;
import org.apache.struts.util.MessageResources;

import javax.servlet.ServletContext;
import javax.servlet.http.HttpSession;
import java.util.Locale;
import java.util.logging.Logger;

/**
 * Localizer
 */
public class Localizer {

    private static final Logger logger = Logger.getLogger(Localizer.class.getName());
    private static MessageResources messageResources;
    private static ServletContext context;

    public static void init(ServletContext aContext) {
        context = aContext;
        reload();
    }

    public static void reload() {
        // Load MessageResources
        logger.info(LogConfigManager.CONFIG_PREFIX + " Loading MessageResources from org.apache.struts.action.MESSAGE...");
        messageResources = MessageResources.getMessageResources("properties.Localization");
        context.setAttribute("org.apache.struts.action.MESSAGE", messageResources);
    }

    /**
     * Sets session locale
     */
    public static void setSessionLocale(HttpSession session, String localeString) {
        String[] strings = localeString.split("_");
        session.setAttribute(SessionManager.SESSION_LOCALE, new Locale(strings[0], strings[1]));
    }

    public static String getText(Locale locale, String contentKey) {
        return messageResources.getMessage(locale, contentKey, null);
    }

    /**
     * Get local content for a specific content key.
     *
     * @param contentKey
     * @return ..
     */
    public static String getText(RequestContext requestContext, String contentKey, Object[] params) {
        // Get content for the session locale
        return messageResources.getMessage(requestContext.getLocale(), contentKey, params);
    }

    /**
     * Call getLocalContent with arguments set to null.
     *
     * @param contentKey
     * @return ..
     */
    public static String getText(RequestContext requestContext, String contentKey) {
        return getText(requestContext, contentKey, null);
    }
}
