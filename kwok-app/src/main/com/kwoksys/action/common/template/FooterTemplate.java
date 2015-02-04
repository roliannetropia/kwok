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
package com.kwoksys.action.common.template;

import com.kwoksys.biz.base.BaseTemplate;
import com.kwoksys.biz.system.core.AppPaths;
import com.kwoksys.biz.system.core.FeatureManager;
import com.kwoksys.framework.license.LicenseManager;
import com.kwoksys.framework.properties.Localizer;
import com.kwoksys.biz.system.core.configs.ConfigManager;
import com.kwoksys.framework.ui.Link;

/**
 * Footer template.
 */
public class FooterTemplate extends BaseTemplate {

    private String reportIssuePath;
    private String timezone;
    private String copyrightNotice;
    private String edition;
    private String onloadJavascript;

    public FooterTemplate() {
        super(FooterTemplate.class);
    }

    public void applyTemplate() {
        boolean issueGuestSubmitFooterEnabled = FeatureManager.isIssueGuestSubmitFooterEnabled();
        if (issueGuestSubmitFooterEnabled) {
            reportIssuePath = new Link(requestContext).setAjaxPath(AppPaths.ISSUE_PLUGIN_ADD).setTitleKey("issuePlugin.issueAdd.title").getString();
        }

        timezone = Localizer.getText(requestContext, "admin.config.timezone." + ConfigManager.system.getTimezoneLocal());

        // We let user override this value
        copyrightNotice = ConfigManager.system.getCompanyFooterNotes();
        if (copyrightNotice.isEmpty()) {
            copyrightNotice = Localizer.getText(requestContext, "core.template.footer.copyright");
        }

        edition = LicenseManager.getContentKey();
    }

    @Override
    public String getJspPath() {
        return "/WEB-INF/jsp/common/template/FooterTemplate.jsp";
    }

    public String getReportIssuePath() {
        return reportIssuePath;
    }

    public String getOnloadJavascript() {
        return onloadJavascript;
    }

    public void setOnloadJavascript(String onloadJavascript) {
        this.onloadJavascript = onloadJavascript;
    }

    public String getTimezone() {
        return timezone;
    }
    public String getCopyrightNotice() {
        return copyrightNotice;
    }
    public String getEdition() {
        return edition;
    }
}