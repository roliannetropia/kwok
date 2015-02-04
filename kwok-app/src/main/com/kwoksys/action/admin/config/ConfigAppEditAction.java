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
import com.kwoksys.biz.contracts.core.ContractUtils;
import com.kwoksys.biz.contracts.dto.Contract;
import com.kwoksys.biz.hardware.core.HardwareUtils;
import com.kwoksys.biz.hardware.dto.Hardware;
import com.kwoksys.biz.issues.core.IssueUtils;
import com.kwoksys.biz.issues.dto.Issue;
import com.kwoksys.biz.kb.KbUtils;
import com.kwoksys.biz.kb.dto.Article;
import com.kwoksys.biz.software.SoftwareUtils;
import com.kwoksys.biz.software.dto.Software;
import com.kwoksys.biz.system.core.AppPaths;
import com.kwoksys.biz.system.core.Links;
import com.kwoksys.biz.system.core.configs.ConfigManager;
import com.kwoksys.framework.properties.Localizer;
import com.kwoksys.framework.struts2.Action2;
import com.kwoksys.framework.ui.WidgetUtils;
import org.apache.struts.util.LabelValueBean;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Action class for editing application configurations.
 */
public class ConfigAppEditAction extends Action2 {

    public String execute() throws Exception {
        ConfigForm actionForm = (ConfigForm) getBaseForm(ConfigForm.class);

        // If not a resubmit, set some defaults
        if (!actionForm.isResubmit()) {
            actionForm.setLicenseKey(ConfigManager.system.getLicenseKey());
            actionForm.setApplicationUrl(ConfigManager.system.getAppUrl());
            actionForm.setTimezone(ConfigManager.system.getTimezoneLocal());
            actionForm.setLocale(ConfigManager.system.getLocaleString());
            actionForm.setShortDateFormat(ConfigManager.system.getDateFormat());
            actionForm.setTimeFormat(ConfigManager.system.getTimeFormat());
            actionForm.setTheme(ConfigManager.system.getTheme());
            actionForm.setCurrency(ConfigManager.system.getCurrencySymbol());
            actionForm.setNumberOfPastYears(String.valueOf(ConfigManager.app.getNumPastYears()));
            actionForm.setNumberOfFutureYears(String.valueOf(ConfigManager.app.getNumFutureYears()));
            actionForm.setUserNameDisplay(ConfigManager.system.getUsernameDisplay());
            actionForm.setUsersNumRows(ConfigManager.app.getUserRows());
            actionForm.setCompaniesNumRows(ConfigManager.app.getCompanyRows());
            actionForm.setContactsNumRows(ConfigManager.app.getContactRows());
            actionForm.setContractsNumRows(ConfigManager.app.getContractsRowsToShow());
            actionForm.setContractsExpireCountdown(ConfigManager.app.getContractsExpireCountdown());
            actionForm.setIssuesNumRows(ConfigManager.app.getIssueRows());
            actionForm.setIssuesMultipleDeleteEnabled(String.valueOf(ConfigManager.app.isIssuesMultipleDeleteEnabled()));
            actionForm.setHardwareNumRows(ConfigManager.app.getHardwareRowsToShow());
            actionForm.setCheckUniqueHardwareName(ConfigManager.app.isCheckUniqueHardwareName());
            actionForm.setCheckUniqueSerialNumber(ConfigManager.app.isCheckUniqueSerialNumber());
            actionForm.setHardwareExpireCountdown(ConfigManager.app.getHardwareWarrantyExpireCountdown());
            actionForm.setSoftwareNumRows(ConfigManager.app.getSoftwareRowsToShow());
            actionForm.setSoftwareLicneseNotesNumChars(String.valueOf(ConfigManager.app.getSoftwareLicenseNotesNumChars()));
            actionForm.setBlogPostsListNumRows(ConfigManager.app.getBlogsNumPosts());
            actionForm.setBlogPostCharactersList(ConfigManager.app.getBlogsNumberOfPostCharacters());
            actionForm.setIssuesGuestSubmitModuleEnabled(String.valueOf(ConfigManager.app.isIssuesGuestSubmitModuleEnabled()));
            actionForm.setIssuesGuestSubmitEnabled(String.valueOf(ConfigManager.app.isIssuesGuestSubmitFooterEnabled()));
            actionForm.setLoadCustomFields(ConfigManager.app.isLoadCustomFields());
            actionForm.setIssuesColumns(IssueUtils.getIssueColumnHeaders());
            actionForm.setHardwareColumns(HardwareUtils.getColumnHeaderList());
            actionForm.setSoftwareColumns(SoftwareUtils.getColumnHeaderList());
            actionForm.setContractColumns(ContractUtils.getColumnHeaderList());
            actionForm.setKbColumns(KbUtils.getArticleColumnHeaderList());
        }

        List timezoneOptions = new ArrayList();
        for (String timezone : ConfigManager.system.getTimezoneLocalOptions()) {
            timezoneOptions.add(new LabelValueBean(Localizer.getText(requestContext, "admin.config.timezone." + timezone), timezone));
        }

        List dateFormatOptions = new ArrayList();
        for (String option : ConfigManager.system.getDateFormatOptions()) {
            dateFormatOptions.add(new LabelValueBean(option, option));
        }

        List timeFormatOptions = new ArrayList();
        for (String option : ConfigManager.system.getTimeFormatOptions()) {
            timeFormatOptions.add(new LabelValueBean(option, option));
        }

        List numrowOptions = new ArrayList();
        for (String option : ConfigManager.admin.getNumberOfRowsToShowOptions()) {
            numrowOptions.add(new LabelValueBean(option, option));
        }

        List numBlogPostsOptions = new ArrayList();
        for (String option : ConfigManager.app.getBlogsNumberOfPostsOptions()) {
            numBlogPostsOptions.add(new LabelValueBean(option, option));
        }

        List blogPostCharOptions = new ArrayList();
        for (String option : ConfigManager.app.getBlogsNumberOfPostCharactersOptions()) {
            blogPostCharOptions.add(new LabelValueBean(option, option));
        }

        List issuesColumnOptions = new ArrayList();
        for (String column : IssueUtils.getIssuesDefaultColumns()) {
            Map map = new HashMap();
            map.put("name", column);
            if (column.equals(Issue.TITLE)) {
                map.put("disabled", "disabled");
            }
            if (actionForm.getIssuesColumns().contains(column)) {
                map.put("checked", "checked=\"checked\" ");
            }
            issuesColumnOptions.add(map);
        }

        List hwColumnOptions = new ArrayList();
        for (String column : HardwareUtils.getHardwareDefaultColumns()) {
            Map map = new HashMap();
            map.put("name", column);
            if (column.equals(Hardware.HARDWARE_NAME)) {
                map.put("disabled", "disabled");
            }
            if (actionForm.getHardwareColumns().contains(column)) {
                map.put("checked", "checked=\"checked\" ");
            }
            hwColumnOptions.add(map);
        }

        List swColumnOptions = new ArrayList();
        for (String column : SoftwareUtils.getSoftwareDefaultColumns()) {
            Map map = new HashMap();
            map.put("name", column);
            if (column.equals(Software.NAME)) {
                map.put("disabled", "disabled");
            }
            if (actionForm.getSoftwareColumns().contains(column)) {
                map.put("checked", "checked=\"checked\" ");
            }
            swColumnOptions.add(map);
        }

        List contractColumns = new ArrayList();
        for (String column : ContractUtils.getContractsColumnsDefault()) {
            Map map = new HashMap();
            map.put("name", column);
            if (column.equals(Contract.NAME)) {
                map.put("disabled", "disabled");
            }
            if (actionForm.getContractColumns().contains(column)) {
                map.put("checked", "checked=\"checked\" ");
            }
            contractColumns.add(map);
        }

        List kbColumnOptions = new ArrayList();
        for (String column : KbUtils.getArticleColumnsDefault()) {
            Map map = new HashMap();
            map.put("name", column);
            if (column.equals(Article.ARTICLE_NAME)) {
                map.put("disabled", "disabled");
            }
            if (actionForm.getKbColumns().contains(column)) {
                map.put("checked", "checked=\"checked\" ");
            }
            kbColumnOptions.add(map);
        }

        List expireCountdownOptions = new ArrayList();
        for (String option : ConfigManager.app.getExpireCountdownOptions()) {
            expireCountdownOptions.add(new LabelValueBean(option, option));
        }

        List userNameDisplayOptions = new ArrayList();
        for (String option : ConfigManager.admin.getUsernameDisplayOptions()) {
            userNameDisplayOptions.add(new LabelValueBean(Localizer.getText(requestContext, "common.column." + option), option));
        }

        //
        // Template: StandardTemplate
        //
        StandardTemplate standardTemplate = new StandardTemplate(requestContext, AdminUtils.ADMIN_APP_EDIT_CMD);
        standardTemplate.setPathAttribute("formAction", AppPaths.ADMIN_CONFIG_WRITE);
        standardTemplate.setAttribute("formCancelLink", Links.getCancelLink(requestContext, AppPaths.ADMIN_CONFIG + "?cmd="
                + AdminUtils.ADMIN_APP_CMD).getString());
        request.setAttribute("cmd", AdminUtils.ADMIN_APP_EDIT_2_CMD);
        request.setAttribute("shortDateFormatOptions", dateFormatOptions);
        request.setAttribute("timeFormatOptions", timeFormatOptions);
        request.setAttribute("timezoneOptions", timezoneOptions);
        request.setAttribute("localeOptions", WidgetUtils.getLocaleOptions(requestContext));
        request.setAttribute("numrowOptions", numrowOptions);
        request.setAttribute("numBlogPostsOptions", numBlogPostsOptions);
        request.setAttribute("numBlogPostCharactersOptions", blogPostCharOptions);
        request.setAttribute("contractsColumnOptions", contractColumns);
        request.setAttribute("expireCountdownOptions", expireCountdownOptions);
        request.setAttribute("issuesColumnOptions", issuesColumnOptions);
        request.setAttribute("kbColumnOptions", kbColumnOptions);
        request.setAttribute("hardwareColumnOptions", hwColumnOptions);
        request.setAttribute("softwareColumnOptions", swColumnOptions);
        request.setAttribute("userNameDisplayOptions", userNameDisplayOptions);
        standardTemplate.setAttribute("booleanOptions", WidgetUtils.getBooleanOptions(requestContext));

        //
        // Template: HeaderTemplate
        //
        HeaderTemplate header = standardTemplate.getHeaderTemplate();
        header.setTitleKey("admin.config.app.edit");

        //
        // Template: ActionErrorsTemplate
        //
        standardTemplate.addTemplate(new ActionErrorsTemplate());

        return standardTemplate.findTemplate(STANDARD_TEMPLATE);
    }
}
