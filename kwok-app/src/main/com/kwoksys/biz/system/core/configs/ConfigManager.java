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
package com.kwoksys.biz.system.core.configs;

import com.kwoksys.framework.license.LicenseManager;
import com.kwoksys.framework.session.CacheManager;

import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.logging.Logger;

/**
 * System configuration. Please remember that some of these configuration may not exist if users forgot to upgrade
 * the database repository.
 */
public class ConfigManager {

    private static final Logger logger = Logger.getLogger(ConfigManager.class.getName());

    private static String schemaVersion;

    public static AdminConfigManager admin = AdminConfigManager.getInstance();
    public static AuthConfigManager auth = AuthConfigManager.getInstance();
    public static EmailConfigManager email = EmailConfigManager.getInstance();
    public static FileConfigManager file = FileConfigManager.getInstance();
    public static AppConfigManager app = AppConfigManager.getInstance();
    public static ReportConfigManager reports = ReportConfigManager.getInstance();
    public static SystemConfigManager system = SystemConfigManager.getInstance();

    public static List<BaseConfigManager> list = Arrays.asList(admin, auth, email, file, reports, app, system);

    public static boolean init() {
        logger.info(LogConfigManager.CONFIG_PREFIX + " Initializing " + ConfigManager.class.getSimpleName() + "...");
        Map<String, String> configMap = new CacheManager().cacheSystemConfigs();

        try {
            schemaVersion = configMap.get(SystemConfigNames.SCHEMA_VERSION);

            for (BaseConfigManager configMgr : list) {
                configMgr.init(configMap);
            }

            LogConfigManager.init(configMap);
            LicenseManager.init();
            
            return true;

        } catch (NullPointerException e) {
            logger.severe("Problem setting system config values. Message: " + e.getMessage());
            return false;
        }
    }

    public static String getSchemaVersion() {
        return schemaVersion;
    }
}
