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

import com.kwoksys.framework.util.StringUtils;

import java.util.Map;

/**
 * EmailConfigManager
 */
public class EmailConfigManager extends BaseConfigManager {

    private static EmailConfigManager instance = new EmailConfigManager();

    private int emailNotification;

    private boolean isEmailNotificationOn;

    private String[] emailNotificationOptions;

    private int emailDomainFiltering;

    private String[] emailDomainFilteringOptions;

    private String allowedDomains;

    private String[] allowedDomainOptions;

    private String smtpFrom;

    private String smtpTo;

    private String smtpHost;

    private String smtpPort;

    private String smtpUsername;

    private String smtpPassword;

    private String smtpStartTls;

    private String popPassword;

    private String popHost;

    private String popPort;

    private String popUsername;

    private boolean popSslEnabled;

    private String popSenderIgnoreList;

    private int popRepeatInterval;

    private int popMessagesLimit;

    private String issueReportEmailTemplate;
    private String issueAddEmailTemplate;
    private String issueUpdateEmailTemplate;

    private EmailConfigManager() {}

    static EmailConfigManager getInstance() {
        return instance;
    }

    /**
     * Initializes configurations.
     * @param configMap
     * @return
     */
    void init(Map<String, String> configMap) {
        setConfigMap(configMap);

        emailNotification = getInt(SystemConfigNames.EMAIL_NOTIFICATION);
        isEmailNotificationOn = (emailNotification == 2);
        emailNotificationOptions = configMap.get("email.notification.options").split(",");
        emailDomainFiltering = getInt(SystemConfigNames.EMAIL_FILTER);
        emailDomainFilteringOptions = configMap.get("email.domainFiltering.options").split(",");
        allowedDomains = configMap.get(SystemConfigNames.EMAIL_ALLOWED_DOMAINS);
        allowedDomainOptions = allowedDomains.split(",");
        issueReportEmailTemplate = getString(SystemConfigNames.ISSUE_REPORT_EMAIL_TEMPLATE);
        issueAddEmailTemplate = getString(SystemConfigNames.ISSUE_ADD_EMAIL_TEMPLATE);
        issueUpdateEmailTemplate = getString(SystemConfigNames.ISSUE_UPDATE_EMAIL_TEMPLATE);

        smtpFrom = getString(SystemConfigNames.SMTP_FROM);
        smtpTo = getString(SystemConfigNames.SMTP_TO);
        smtpHost = getString(SystemConfigNames.SMTP_HOST);
        smtpPort = getString(SystemConfigNames.SMTP_PORT);
        smtpUsername = getString(SystemConfigNames.SMTP_USERNAME);
        smtpPassword = StringUtils.decodeBase64Codec(configMap.get(SystemConfigNames.SMTP_PASSWORD));
        smtpStartTls = getString(SystemConfigNames.SMTP_STARTTLS);

        popHost = getString(SystemConfigNames.POP_HOST);
        popPort = getString(SystemConfigNames.POP_PORT);
        popUsername = getString(SystemConfigNames.POP_USERNAME);
        popPassword = StringUtils.decodeBase64Codec(configMap.get(SystemConfigNames.POP_PASSWORD));
        popSslEnabled = getBoolean(SystemConfigNames.POP_SSL_ENABLED);
        popSenderIgnoreList = getString(SystemConfigNames.POP_SENDER_IGNORE_LIST);
        popRepeatInterval = getInt(SystemConfigNames.POP_REPEAT_INTERVAL);
        popMessagesLimit = getInt(SystemConfigNames.POP_MESSAGES_LIMIT);
    }

    public String getSmtpFrom() {
        return smtpFrom;
    }

    public String getSmtpTo() {
        return smtpTo;
    }

    public int getEmailNotification() {
        return emailNotification;
    }

    public boolean isEmailNotificationOn() {
        return isEmailNotificationOn;
    }

    public String[] getEmailNotificationOptions() {
        return emailNotificationOptions;
    }
    public int getEmailDomainFiltering() {
        return emailDomainFiltering;
    }
    public String[] getAllowedDomainOptions() {
        return allowedDomainOptions;
    }
    public String getSmtpPassword() {
        return smtpPassword;
    }

    public String getPopPassword() {
        return popPassword;
    }
    public String[] getEmailDomainFilteringOptions() {
        return emailDomainFilteringOptions;
    }
    public String getAllowedDomains() {
        return allowedDomains;
    }

    public boolean isPopSslEnabled() {
        return popSslEnabled;
    }

    public String getSmtpHost() {
        return smtpHost;
    }

    public String getSmtpPort() {
        return smtpPort;
    }

    public String getSmtpUsername() {
        return smtpUsername;
    }

    public String getPopHost() {
        return popHost;
    }

    public String getPopPort() {
        return popPort;
    }

    public String getSmtpStartTls() {
        return smtpStartTls;
    }

    public String getPopUsername() {
        return popUsername;
    }

    public String getPopSenderIgnoreList() {
        return popSenderIgnoreList;
    }

    public int getPopRepeatInterval() {
        return popRepeatInterval;
    }

    public int getPopMessagesLimit() {
        return popMessagesLimit;
    }

    public String getIssueReportEmailTemplate() {
        return issueReportEmailTemplate;
    }

    public String getIssueAddEmailTemplate() {
        return issueAddEmailTemplate;
    }

    public String getIssueUpdateEmailTemplate() {
        return issueUpdateEmailTemplate;
    }
}
