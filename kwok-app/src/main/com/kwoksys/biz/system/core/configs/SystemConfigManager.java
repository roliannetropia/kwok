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

import java.util.List;
import java.util.Locale;
import java.util.Map;

/**
 * SystemConfigManager
 */
public class SystemConfigManager extends BaseConfigManager {

    private static SystemConfigManager instance = new SystemConfigManager();

    private String extension = ".htm";

    private String appUrl;

    private String cacheKey;

    private Long cacheTime;

    private String licenseKey;

    private String trailingSlash;

    private String characterEncoding = "UTF-8";

    private String currencySymbol;

    private String customHomeDescription;

    private String[] localeOptions;

    private String timezoneBase;

    private String timezoneLocal;

    private String[] timezoneLocalOptions;

    private String timeFormat;

    private String[] timeFormatOptions;

    private String usernameDisplay;

    private String dateFormat;

    private String datetimeBase;

    private List<String> moduleTabs;

    private String theme;

    private String[] themeOptions;

    private int[] fontOptions;

    private String[] dateFormatOptions;

    private String localeString;

    private Locale locale;

    private String companyName;

    private String companyPath;

    private String companyLogoPath;

    private String companyFooterNotes;

    private String sytlesheet;

    /**
     * Number of database connections allowed in the connection pool.
     */
    public static final int DB_MAX_CONNECTION_UNLIITED = -1;
    private int dbMaxConnection = DB_MAX_CONNECTION_UNLIITED;

    private SystemConfigManager() {}

    static SystemConfigManager getInstance() {
        return instance;
    }

    void init(Map<String, String> configMap) {
        setConfigMap(configMap);

        appUrl = configMap.get(SystemConfigNames.APP_URL);

        cacheKey = getString(SystemConfigNames.SYSTEM_CACHE_KEY);

        currencySymbol = getString(SystemConfigNames.CURRENCY_OPTION);

        companyName = getString(SystemConfigNames.COMPANY_NAME);

        companyPath = getString(SystemConfigNames.COMPANY_PATH);

        companyLogoPath = getString(SystemConfigNames.COMPANY_LOGO_PATH);

        companyFooterNotes = getString(SystemConfigNames.COMPANY_FOOTER_NOTES);

        customHomeDescription = getString(SystemConfigNames.CUSTOM_HOME_DESCRIPTION);

        datetimeBase = getString("datetime.base");

        dateFormat = getString(SystemConfigNames.SHORT_DATE);

        dateFormatOptions = getStringArray("datetime.shortDateFormat.options");

        licenseKey = getString(SystemConfigNames.SYSTEM_LICENSE_KEY);

        localeString = getString(SystemConfigNames.LOCALE);

        String[] strings = localeString.split("_");
        locale = new Locale(strings[0], strings[1]);

        localeOptions = getStringArray(SystemConfigNames.LOCALE_OPTIONS);

        moduleTabs = getStringList("template.moduleTabs");

        theme = getString(SystemConfigNames.THEME_DEFAULT);

        themeOptions = getStringArray(SystemConfigNames.THEME_OPTIONS);

        fontOptions = new int[]{12, 13, 14};

        timezoneBase = getString(SystemConfigNames.TIMEZONE_BASE);

        timezoneLocal = getString(SystemConfigNames.LOCAL_TIMEZONE);

        timezoneLocalOptions = getStringArray("timezone.local.options");

        trailingSlash = System.getProperty("file.separator");

        sytlesheet = getString(SystemConfigNames.UI_STYLESHEET);

        timeFormat = getString(SystemConfigNames.TIME_FORMAT);

        timeFormatOptions = getStringArray("datetime.timeFormat.options");

        usernameDisplay = getString(SystemConfigNames.USER_NAME_DISPLAY);

        dbMaxConnection = getInt(SystemConfigNames.SYSTEM_DB_MAX_CONNECTION);
    }

    public String getLicenseKey() {
        return licenseKey;
    }

    public String getCurrencySymbol() {
        return currencySymbol;
    }

    public String getLocaleString() {
        return localeString;
    }

    public String getTrailingSlash() {
        return trailingSlash;
    }

    public String[] getTimeFormatOptions() {
        return timeFormatOptions;
    }

    public String[] getDateFormatOptions() {
        return dateFormatOptions;
    }

    public String[] getLocaleOptions() {
        return localeOptions;
    }

    public String getAppUrl() {
        return appUrl;
    }

    public String[] getTimezoneLocalOptions() {
        return timezoneLocalOptions;
    }

    public String getDatetimeBase() {
        return datetimeBase;
    }

    public List<String> getModuleTabs() {
        return moduleTabs;
    }

    public String getCompanyName() {
        return companyName;
    }

    public String getCharacterEncoding() {
        return characterEncoding;
    }

    public String[] getThemeOptions() {
        return themeOptions;
    }

    public String getCustomHomeDescription() {
        return customHomeDescription;
    }

    public String getCacheKey() {
        return cacheKey;
    }

    public String getTheme() {
        return theme;
    }

    public String getTimezoneBase() {
        return timezoneBase;
    }

    public String getDateFormat() {
        return dateFormat;
    }

    public String getTimeFormat() {
        return timeFormat;
    }

    public String getCompanyLogoPath() {
        return companyLogoPath;
    }

    public String getCompanyPath() {
        return companyPath;
    }

    public String getCompanyFooterNotes() {
        return companyFooterNotes;
    }

    public String getSytlesheet() {
        return sytlesheet;
    }

    public String getTimezoneLocal() {
        return timezoneLocal;
    }

    public String getUsernameDisplay() {
        return usernameDisplay;
    }

    public Locale getLocale() {
        return locale;
    }

    public Long getCacheTime() {
        return cacheTime;
    }

    public void setCacheTime(Long cacheTime) {
        this.cacheTime = cacheTime;
    }

    public int[] getFontOptions() {
        return fontOptions;
    }

    public int getDbMaxConnection() {
        return dbMaxConnection;
    }

    public String getExtension() {
        return extension;
    }
}
