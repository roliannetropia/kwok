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
import com.kwoksys.biz.ServiceProvider;
import com.kwoksys.biz.admin.core.AdminUtils;
import com.kwoksys.biz.admin.dto.AccessUser;
import com.kwoksys.biz.auth.core.Access;
import com.kwoksys.biz.system.SystemService;
import com.kwoksys.biz.system.core.AppPaths;
import com.kwoksys.biz.system.core.Image;
import com.kwoksys.biz.system.core.Links;
import com.kwoksys.biz.system.core.configs.ConfigManager;
import com.kwoksys.framework.license.LicenseManager;
import com.kwoksys.framework.properties.Localizer;
import com.kwoksys.framework.struts2.Action2;
import com.kwoksys.framework.ui.Link;
import com.kwoksys.framework.util.DatetimeUtils;
import com.kwoksys.framework.util.StringUtils;

import java.util.ArrayList;
import java.util.List;

/**
 * ConfigListGeneralAction
 */
public class ConfigAppAction extends Action2 {

    public String execute() throws Exception {
        AccessUser user = requestContext.getUser();

        SystemService systemService = ServiceProvider.getSystemService(requestContext);

        //
        // Template: StandardTemplate
        //
        StandardTemplate standardTemplate = new StandardTemplate(requestContext, AdminUtils.ADMIN_APP_CMD);
        request.setAttribute("buildNumber", systemService.getBuildNumber());

        Link link = new Link(requestContext);
        link.setTitleKey(LicenseManager.getContentKey());
        if (!LicenseManager.isCommercialEdition()) {
            link.setExternalPath(AppPaths.SITE_COMPARE_EDITIONS);
            link.setImgSrc(Image.getInstance().getExternalPopupIcon());
        }
        request.setAttribute("appEdition", link);

        link = new Link(requestContext);
        link.setExternalPath(AppPaths.getInstance().getSiteLicenseKeys());
        link.setTitleKey("admin.config.app.viewLicenseKeys");
        link.setImgSrc(Image.getInstance().getExternalPopupIcon());
        request.setAttribute("viewLicenseKeysLink", link);

        request.setAttribute("licenseKey", ConfigManager.system.getLicenseKey());
        request.setAttribute("applicationUrl", ConfigManager.system.getAppUrl());
        request.setAttribute("timezoneLocal", Localizer.getText(requestContext, "admin.config.timezone." +
                ConfigManager.system.getTimezoneLocal()));
        request.setAttribute("serverTime", DatetimeUtils.toLocalDatetime(requestContext.getSysdate()));
        request.setAttribute("shortDateFormat", ConfigManager.system.getDateFormat());
        request.setAttribute("locale", ConfigManager.system.getLocaleString());
        request.setAttribute("timeFormat", ConfigManager.system.getTimeFormat());
        request.setAttribute("currencyOptions", ConfigManager.system.getCurrencySymbol());
        request.setAttribute("numberOfPastYearsToShow", ConfigManager.app.getNumPastYears());
        request.setAttribute("numberOfFutureYearsToShow", ConfigManager.app.getNumFutureYears());
        request.setAttribute("usernameDisplay", ConfigManager.system.getUsernameDisplay());
        request.setAttribute("usersRowsToShow", ConfigManager.app.getUserRows());
        request.setAttribute("companiesRowsToShow", ConfigManager.app.getCompanyRows());
        request.setAttribute("contactsRowsToShow", ConfigManager.app.getContactRows());
        request.setAttribute("contractsRowsToShow", ConfigManager.app.getContractsRowsToShow());
        request.setAttribute("issuesRowsToShow", ConfigManager.app.getIssueRows());
        request.setAttribute("issuesMultipleDeleteEnabled", ConfigManager.app.isIssuesMultipleDeleteEnabled());
        request.setAttribute("hardwareRowsToShow", ConfigManager.app.getHardwareRowsToShow());
        request.setAttribute("softwareRowsToShow", ConfigManager.app.getSoftwareRowsToShow());
        request.setAttribute("softwareLicneseNotesNumChars", ConfigManager.app.getSoftwareLicenseNotesNumChars());

        List hardwareColumns = new ArrayList();
        for (String column : ConfigManager.app.getHardwareColumns()) {
            hardwareColumns.add(Localizer.getText(requestContext, "common.column." + column));
        }
        request.setAttribute("hardwareColumns", StringUtils.join(hardwareColumns, ", "));

        List softwareColumns = new ArrayList();
        for (String column : ConfigManager.app.getSoftwareColumns()) {
            softwareColumns.add(Localizer.getText(requestContext, "common.column." + column));
        }
        request.setAttribute("softwareColumns", StringUtils.join(softwareColumns, ", "));

        request.setAttribute("hardwareExpirationCountdown", ConfigManager.app.getHardwareWarrantyExpireCountdown());
        request.setAttribute("checkUniqueHardwareName",ConfigManager.app.isCheckUniqueHardwareName());
        request.setAttribute("checkUniqueSerialNumber", ConfigManager.app.isCheckUniqueSerialNumber());

        List issuesColumns = new ArrayList();
        for (String column : ConfigManager.app.getIssuesColumns()) {
            issuesColumns.add(Localizer.getText(requestContext, "issueMgmt.colName." + column));
        }
        request.setAttribute("issuesColumns", StringUtils.join(issuesColumns, ", "));

        request.setAttribute("issuesGuestSubmitModuleEnabled", ConfigManager.app.isIssuesGuestSubmitModuleEnabled());
        request.setAttribute("issuesGuestSubmitEnabled", ConfigManager.app.isIssuesGuestSubmitFooterEnabled());

        List contractsColumns = new ArrayList();
        for (String column : ConfigManager.app.getContractsColumns()) {
            contractsColumns.add(Localizer.getText(requestContext, "common.column." + column));
        }
        request.setAttribute("contractsColumns", StringUtils.join(contractsColumns, ", "));
        request.setAttribute("contractsExpirationCountdown", ConfigManager.app.getContractsExpireCountdown());

        List<String> kbColumns = new ArrayList();
        for (String column : ConfigManager.app.getKbArticleColumns()) {
            kbColumns.add(Localizer.getText(requestContext, "common.column." + column));
        }
        request.setAttribute("kbColumns", StringUtils.join(kbColumns, ", "));

        request.setAttribute("portal_numberOfBlogPostsToShowOnList", ConfigManager.app.getBlogsNumPosts());
        request.setAttribute("portal_numberOfBlogPostCharactersOnList", ConfigManager.app.getBlogsNumberOfPostCharacters());

        standardTemplate.setAttribute("loadCustomFields", Localizer.getText(requestContext, "common.boolean.true_false." +
                ConfigManager.app.isLoadCustomFields()));

        //
        // Template: HeaderTemplate
        //
        HeaderTemplate header = standardTemplate.getHeaderTemplate();
        header.setTitleKey("admin.config.app");

        if (Access.hasPermission(user, AppPaths.ADMIN_CONFIG_WRITE)) {
            link = new Link(requestContext);
            link.setAjaxPath(AppPaths.ADMIN_CONFIG_WRITE + "?cmd=" + AdminUtils.ADMIN_APP_EDIT_CMD);
            link.setTitleKey("common.command.Edit");
            header.addHeaderCmds(link);
        }

        // Back to admin home
        header.addNavLink(Links.getAdminHomeLink(requestContext));
        header.addNavLink(new Link(requestContext).setTitleKey("admin.config.app"));

        return standardTemplate.findTemplate(STANDARD_TEMPLATE);
    }
}
