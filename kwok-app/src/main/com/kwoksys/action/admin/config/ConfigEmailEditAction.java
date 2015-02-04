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

import com.kwoksys.action.common.template.ActionErrorsTemplate;
import com.kwoksys.action.common.template.HeaderTemplate;
import com.kwoksys.action.common.template.StandardTemplate;
import com.kwoksys.biz.admin.core.AdminUtils;
import com.kwoksys.biz.system.core.AppPaths;
import com.kwoksys.biz.system.core.Links;
import com.kwoksys.biz.system.core.configs.ConfigManager;
import com.kwoksys.framework.properties.Localizer;
import com.kwoksys.framework.struts2.Action2;
import com.kwoksys.framework.util.HtmlUtils;
import org.apache.struts.util.LabelValueBean;

import java.util.ArrayList;
import java.util.List;

/**
 * Action class editing notification configuratons.
 */
public class ConfigEmailEditAction extends Action2 {

    public String execute() throws Exception {
        ConfigForm actionForm = (ConfigForm) getBaseForm(ConfigForm.class);

        // If not a resubmit, set some defaults
        if (!actionForm.isResubmit()) {
            actionForm.setNotificationMethod(String.valueOf(ConfigManager.email.getEmailNotification()));
            actionForm.setDomainFiltering(String.valueOf(ConfigManager.email.getEmailDomainFiltering()));
            actionForm.setAllowedDomains(ConfigManager.email.getAllowedDomains());
            actionForm.setSmtpHost(ConfigManager.email.getSmtpHost());
            actionForm.setSmtpPort(ConfigManager.email.getSmtpPort());
            actionForm.setSmtpUsername(ConfigManager.email.getSmtpUsername());
            actionForm.setSmtpFrom(ConfigManager.email.getSmtpFrom());
            actionForm.setSmtpTo(ConfigManager.email.getSmtpTo());
            actionForm.setSmtpPassword("");
            actionForm.setSmtpStarttls(ConfigManager.email.getSmtpStartTls());
            actionForm.setReportIssueEmailTemplate(ConfigManager.email.getIssueReportEmailTemplate());
            actionForm.setIssueAddEmailTemplate(ConfigManager.email.getIssueAddEmailTemplate());
            actionForm.setIssueUpdateEmailTemplate(ConfigManager.email.getIssueUpdateEmailTemplate());
        } 

        List notificationOptions = new ArrayList();
        for (String option : ConfigManager.email.getEmailNotificationOptions()) {
            notificationOptions.add(new LabelValueBean(Localizer.getText(requestContext, "admin.config.email.notification." + option), option));
        }

        List domainFilterOptions = new ArrayList();
        for (String option : ConfigManager.email.getEmailDomainFilteringOptions()) {
            domainFilterOptions.add(new LabelValueBean(Localizer.getText(requestContext, "admin.config.email.domainFiltering." + option), option));
        }

        List starttlsOptions = new ArrayList();
        starttlsOptions.add(new LabelValueBean(Localizer.getText(requestContext, "admin.config.email.smtp.starttls.true"), "true"));
        starttlsOptions.add(new LabelValueBean(Localizer.getText(requestContext, "admin.config.email.smtp.starttls.false"), "false"));

        //
        // Template: StandardTemplate
        //
        StandardTemplate standardTemplate = new StandardTemplate(requestContext, AdminUtils.ADMIN_EMAIL_EDIT_CMD);
        standardTemplate.setPathAttribute("formAction", AppPaths.ADMIN_CONFIG_WRITE);
        standardTemplate.setAttribute("formCancelLink", Links.getCancelLink(requestContext, AppPaths.ADMIN_CONFIG + "?cmd=" + AdminUtils.ADMIN_EMAIL_CMD).getString());
        standardTemplate.setAttribute("cmd", AdminUtils.ADMIN_EMAIL_EDIT_2_CMD);
        standardTemplate.setAttribute("notificationMethodOptions", notificationOptions);
        standardTemplate.setAttribute("domainFilteringOptions", domainFilterOptions);
        standardTemplate.setAttribute("smtpStarttlsOptions", starttlsOptions);
        standardTemplate.setAttribute("defaultReportIssueTemplate", HtmlUtils.formatMultiLineDisplay(Localizer.getText(requestContext, "issuePlugin.issueAdd2.emailBody")));
        standardTemplate.setAttribute("defaultAddIssueTemplate", HtmlUtils.formatMultiLineDisplay(Localizer.getText(requestContext, "issues.issueAdd2.emailBody")));
        standardTemplate.setAttribute("defaultUpdateIssueTemplate", HtmlUtils.formatMultiLineDisplay(Localizer.getText(requestContext, "issues.issueEdit2.emailBody")));

        //
        // Template: HeaderTemplate
        //
        HeaderTemplate headerTemplate = standardTemplate.getHeaderTemplate();
        headerTemplate.setTitleKey("admin.configHeader.notification_config_edit");

        //
        // Template: ActionErrorsTemplate
        //
        standardTemplate.addTemplate(new ActionErrorsTemplate());

        return standardTemplate.findTemplate(STANDARD_TEMPLATE);
    }
}
