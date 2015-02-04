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
package com.kwoksys.framework.servlets;

import com.kwoksys.biz.ServiceProvider;
import com.kwoksys.biz.system.SystemService;
import com.kwoksys.biz.system.core.AppPaths;
import com.kwoksys.framework.http.RequestContext;
import com.kwoksys.biz.system.core.configs.ConfigManager;
import com.kwoksys.framework.properties.Localizer;
import com.kwoksys.biz.system.core.configs.LogConfigManager;

import javax.servlet.ServletConfig;
import javax.servlet.ServletContext;
import javax.servlet.http.HttpServlet;
import java.sql.Driver;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.*;

/**
 * SystemInitServlet
 */
public class SystemInitServlet extends HttpServlet {

    private static final Logger logger = Logger.getLogger(SystemInitServlet.class.getName());

    public static boolean init = false;

    public static String initError = "";

    public void init(ServletConfig cfg) {
        ServletContext context = cfg.getServletContext();
        SystemService systemService = ServiceProvider.getSystemService(null);

        // Initialize root path
        AppPaths.init(context.getContextPath());

        logger.info(LogConfigManager.CONFIG_PREFIX + " Starting Kwok application");
        logger.info(LogConfigManager.CONFIG_PREFIX + " Kwok build number: " + systemService.getBuildNumber());

        // Log system properties
        Properties props = System.getProperties();
        for (Map.Entry entry : props.entrySet()) {
            logger.info(LogConfigManager.CONFIG_PREFIX + " " + entry.getKey() + ": " + entry.getValue());
        }

        // Initialize localization settings
        Localizer.init(context);

        // Initialize application settings
        init = ConfigManager.init();

        if (init) {
            // Compare app and schema versions
            if (!systemService.getVersion().equals(ConfigManager.getSchemaVersion())) {
                initError = Localizer.getText(new RequestContext(), "core.template.footer.versionMismatch",
                                    new String[]{systemService.getVersion(), ConfigManager.getSchemaVersion()});
                logger.severe(initError);
                init = false;
            }
        }
    }

    public void destroy() {
        Enumeration<Driver> drivers = DriverManager.getDrivers();
        while (drivers.hasMoreElements()) {
            Driver driver = drivers.nextElement();
            try {
                DriverManager.deregisterDriver(driver);
                logger.log(Level.INFO, LogConfigManager.SHUTDOWN_PREFIX + " Deregistering jdbc driver: " + driver);
            } catch (SQLException e) {
                logger.log(Level.WARNING, LogConfigManager.SHUTDOWN_PREFIX + " Error deregistering driver: " + driver, e);
            }
        }
    }
}