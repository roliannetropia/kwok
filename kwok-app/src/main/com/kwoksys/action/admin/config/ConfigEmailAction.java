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
package com.kwoksys.action.admin.config;

import com.kwoksys.action.common.template.HeaderTemplate;
import com.kwoksys.action.common.template.StandardTemplate;
import com.kwoksys.biz.admin.core.AdminUtils;
import com.kwoksys.biz.admin.dto.AccessUser;
import com.kwoksys.biz.auth.core.Access;
import com.kwoksys.biz.system.core.AppPaths;
import com.kwoksys.biz.system.core.Links;
import com.kwoksys.biz.system.core.configs.ConfigManager;
import com.kwoksys.framework.properties.Localizer;
import com.kwoksys.framework.struts2.Action2;
import com.kwoksys.framework.ui.Link;
import com.kwoksys.framework.util.HtmlUtils;

/**
 * ConfigEmailAction
 */
public class ConfigEmailAction extends Action2 {

    public String execute() throws Exception {
        AccessUser user = requestContext.getUser();

        //
        // Template: StandardTemplate
        //
        StandardTemplate standardTemplate = new StandardTemplate(requestContext, AdminUtils.ADMIN_EMAIL_CMD);

        standardTemplate.setAttribute("emailNotification", Localizer.getText(requestContext, "admin.config.email.notification." +
                ConfigManager.email.getEmailNotification()));
        standardTemplate.setAttribute("emailDomainFilter", Localizer.getText(requestContext, "admin.config.email.domainFiltering." +
                ConfigManager.email.getEmailDomainFiltering()));
        standardTemplate.setAttribute("emailAllowedDomains", ConfigManager.email.getAllowedDomains());
        standardTemplate.setAttribute("smtpHost", ConfigManager.email.getSmtpHost());
        standardTemplate.setAttribute("smtpPort", ConfigManager.email.getSmtpPort());
        standardTemplate.setAttribute("smtpUsername", ConfigManager.email.getSmtpUsername());
        standardTemplate.setAttribute("smtpPassword", ConfigManager.email.getSmtpPassword());
        standardTemplate.setAttribute("emailFrom", ConfigManager.email.getSmtpFrom());
        standardTemplate.setAttribute("emailTo", ConfigManager.email.getSmtpTo());
        standardTemplate.setAttribute("issueReportEmailTemplate", HtmlUtils.formatMultiLineDisplay(ConfigManager.email.getIssueReportEmailTemplate()));
        standardTemplate.setAttribute("issueAddEmailTemplate", HtmlUtils.formatMultiLineDisplay(ConfigManager.email.getIssueAddEmailTemplate()));
        standardTemplate.setAttribute("issueUpdateEmailTemplate", HtmlUtils.formatMultiLineDisplay(ConfigManager.email.getIssueUpdateEmailTemplate()));
        standardTemplate.setAttribute("starttls", Localizer.getText(requestContext, "admin.config.email.smtp.starttls."+ ConfigManager.email.getSmtpStartTls()));
        standardTemplate.setAttribute("popHost", ConfigManager.email.getPopHost());
        standardTemplate.setAttribute("popPort", ConfigManager.email.getPopPort());
        standardTemplate.setAttribute("popUsername", ConfigManager.email.getPopUsername());
        standardTemplate.setAttribute("popPassword", ConfigManager.email.getPopPassword());
        standardTemplate.setAttribute("popUseSSL", ConfigManager.email.isPopSslEnabled());
        standardTemplate.setAttribute("popIgnoreSender", HtmlUtils.formatMultiLineDisplay(ConfigManager.email.getPopSenderIgnoreList()));

        standardTemplate.setAttribute("canEditSmtpSettings", Access.hasPermission(user, AppPaths.ADMIN_CONFIG_WRITE));
        standardTemplate.setAttribute("editSmtpSettingsLink", new Link(requestContext).setAjaxPath(AppPaths.ADMIN_CONFIG_WRITE
                            + "?cmd=" + AdminUtils.ADMIN_EMAIL_EDIT_CMD).setTitleKey("admin.cmd.configEmailEdit"));
                
        standardTemplate.setAttribute("canEditPopSettings", Access.hasPermission(user, AppPaths.ADMIN_CONFIG_WRITE));
        standardTemplate.setAttribute("editPopSettingsLink", new Link(requestContext).setAjaxPath(AppPaths.ADMIN_CONFIG_WRITE
                    + "?cmd=" + AdminUtils.ADMIN_POP_EMAIL_EDIT_CMD).setTitleKey("admin.cmd.configEmailEdit"));

        //
        // Template: HeaderTemplate
        //
        HeaderTemplate header = standardTemplate.getHeaderTemplate();
        header.setTitleKey("admin.config.email.title");
        header.setTitleClassNoLine();
        
        // Back to admin home
        header.addNavLink(Links.getAdminHomeLink(requestContext));
        header.addNavLink(new Link(requestContext).setTitleKey("admin.config.email.title"));

        return standardTemplate.findTemplate(STANDARD_TEMPLATE);
    }
}