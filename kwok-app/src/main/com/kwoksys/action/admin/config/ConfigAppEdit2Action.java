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

import com.kwoksys.biz.ServiceProvider;
import com.kwoksys.biz.admin.AdminService;
import com.kwoksys.biz.admin.core.AdminUtils;
import com.kwoksys.biz.admin.dto.SystemConfig;
import com.kwoksys.biz.system.core.AppPaths;
import com.kwoksys.biz.system.core.configs.SystemConfigNames;
import com.kwoksys.framework.http.RequestContext;
import com.kwoksys.framework.properties.Localizer;
import com.kwoksys.framework.struts2.Action2;
import com.kwoksys.framework.util.NumberUtils;
import com.kwoksys.framework.util.StringUtils;
import org.apache.struts.action.ActionMessage;
import org.apache.struts.action.ActionMessages;

import java.util.ArrayList;
import java.util.List;

/**
 * Action class for editing application configurations.
 */
public class ConfigAppEdit2Action extends Action2 {

    public String execute() throws Exception {
        ConfigForm actionForm = saveActionForm(new ConfigForm());
        ActionMessages errors = new ActionMessages();

        // Check inputs
        if (!AdminUtils.validCurrencySymbol(actionForm.getCurrency())) {
            errors.add("invalidCurrency", new ActionMessage("admin.config.error.invalidCurrency"));
        }
        if (!NumberUtils.isInteger(actionForm.getNumberOfPastYears())) {
            errors.add("invalidNumPastYears", new ActionMessage("common.form.fieldNumberInvalid",
                    Localizer.getText(requestContext, "admin.config.numberOfPastYears")));
        }
        if (!NumberUtils.isInteger(actionForm.getNumberOfFutureYears())) {
            errors.add("invalidNumFutureYears", new ActionMessage("common.form.fieldNumberInvalid",
                    Localizer.getText(requestContext, "admin.config.numberOfFutureYears")));
        }
        if (!NumberUtils.isInteger(actionForm.getSoftwareLicneseNotesNumChars())) {
            errors.add("invalidSoftwareLicneseNotesNumChars", new ActionMessage("common.form.fieldNumberInvalid",
                    Localizer.getText(requestContext, "admin.config.software.licenseNotesCharacters")));
        }
        if (errors.isEmpty()) {
            String issuesColumns = StringUtils.join(actionForm.getIssuesColumns(), ",");
            String hardwareColumns = StringUtils.join(actionForm.getHardwareColumns(), ",");
            String softwareColumns = StringUtils.join(actionForm.getSoftwareColumns(), ",");
            String contractColumns = StringUtils.join(actionForm.getContractColumns(), ",");
            String kbColumns = StringUtils.join(actionForm.getKbColumns(), ",");

            List list = new ArrayList();
            list.add(new SystemConfig(SystemConfigNames.SYSTEM_LICENSE_KEY, actionForm.getLicenseKey()));
            list.add(new SystemConfig(SystemConfigNames.APP_URL, actionForm.getApplicationUrl()));
            list.add(new SystemConfig(SystemConfigNames.CURRENCY_OPTION, actionForm.getCurrency()));
            list.add(new SystemConfig(SystemConfigNames.LOCAL_TIMEZONE, actionForm.getTimezone()));
            list.add(new SystemConfig(SystemConfigNames.LOCALE, actionForm.getLocale()));
            list.add(new SystemConfig(SystemConfigNames.SHORT_DATE, actionForm.getShortDateFormat()));
            list.add(new SystemConfig(SystemConfigNames.TIME_FORMAT, actionForm.getTimeFormat()));
            list.add(new SystemConfig(SystemConfigNames.USER_ROWS, String.valueOf(actionForm.getUsersNumRows())));
            list.add(new SystemConfig(SystemConfigNames.USER_NAME_DISPLAY, actionForm.getUserNameDisplay()));
            list.add(new SystemConfig(SystemConfigNames.COMPANY_ROWS, String.valueOf(actionForm.getCompaniesNumRows())));
            list.add(new SystemConfig(SystemConfigNames.CONTACT_ROWS, String.valueOf(actionForm.getContactsNumRows())));
            list.add(new SystemConfig(SystemConfigNames.CONTRACT_COLUMNS, contractColumns));
            list.add(new SystemConfig(SystemConfigNames.CONTRACT_ROWS, String.valueOf(actionForm.getContractsNumRows())));
            list.add(new SystemConfig(SystemConfigNames.CONTRACT_EXPIRE_COUNTDOWN, String.valueOf(actionForm.getContractsExpireCountdown())));
            list.add(new SystemConfig(SystemConfigNames.ISSUE_ROWS, String.valueOf(actionForm.getIssuesNumRows())));
            list.add(new SystemConfig(SystemConfigNames.ISSUE_COLUMNS, issuesColumns));
            list.add(new SystemConfig(SystemConfigNames.ISSUE_GUEST_SUBMIT_MODULE_ENABLED, actionForm.getIssuesGuestSubmitModuleEnabled()));
            list.add(new SystemConfig(SystemConfigNames.ISSUE_GUEST_SUBMIT_FOOTER_ENABLED, actionForm.getIssuesGuestSubmitEnabled()));
            list.add(new SystemConfig(SystemConfigNames.ISSUE_MULTIPLE_DELETE_ENABLED, actionForm.getIssuesMultipleDeleteEnabled()));
            list.add(new SystemConfig(SystemConfigNames.KB_ARTICLE_COLUMNS, kbColumns));
            list.add(new SystemConfig(SystemConfigNames.HARDWARE_ROWS, String.valueOf(actionForm.getHardwareNumRows())));
            list.add(new SystemConfig(SystemConfigNames.HARDWARE_COLUMNS, hardwareColumns));
            list.add(new SystemConfig(SystemConfigNames.HARDWARE_WARRANTY_EXPIRE_COUNTDOWN, String.valueOf(actionForm.getHardwareExpireCountdown())));
            list.add(new SystemConfig(SystemConfigNames.BLOG_NUM_POSTS, String.valueOf(actionForm.getBlogPostsListNumRows())));
            list.add(new SystemConfig(SystemConfigNames.BLOG_NUM_POST_CHARS, String.valueOf(actionForm.getBlogPostCharactersList())));
            list.add(new SystemConfig(SystemConfigNames.NUM_PAST_YEARS, actionForm.getNumberOfPastYears()));
            list.add(new SystemConfig(SystemConfigNames.NUM_FUTURE_YEARS, actionForm.getNumberOfFutureYears()));
            list.add(new SystemConfig(SystemConfigNames.SOFTWARE_COLUMNS, softwareColumns));
            list.add(new SystemConfig(SystemConfigNames.SOFTWARE_ROWS, String.valueOf(actionForm.getSoftwareNumRows())));
            list.add(new SystemConfig(SystemConfigNames.SOFTWARE_LICENSE_NOTES_NUM_CHARS, String.valueOf(actionForm.getSoftwareLicneseNotesNumChars())));
            list.add(new SystemConfig(SystemConfigNames.HARDWARE_CHECK_UNIQUE_NAME, actionForm.getCheckUniqueHardwareName()));
            list.add(new SystemConfig(SystemConfigNames.HARDWARE_CHECK_SERIAL_NUMBER, actionForm.isCheckUniqueSerialNumber()));
            list.add(new SystemConfig(SystemConfigNames.CUSTOM_FIELDS_EXPAND, actionForm.isLoadCustomFields()));

            AdminService adminService = ServiceProvider.getAdminService(requestContext);
            errors = adminService.updateConfig(list);
        }

        if (!errors.isEmpty()) {
            saveActionErrors(errors);
            return redirect(AppPaths.ADMIN_CONFIG_WRITE + "?cmd=" + AdminUtils.ADMIN_APP_EDIT_CMD + "&" + RequestContext.URL_PARAM_ERROR_TRUE);

        } else {
            Localizer.setSessionLocale(request.getSession(), actionForm.getLocale());

            return redirect(AppPaths.ADMIN_CONFIG + "?cmd=" + AdminUtils.ADMIN_APP_CMD);
        }
    }
}
