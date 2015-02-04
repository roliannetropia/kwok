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

import com.kwoksys.biz.ServiceProvider;
import com.kwoksys.biz.system.SystemService;
import com.kwoksys.biz.system.core.FeatureManager;
import com.kwoksys.framework.http.RequestContext;

/**
 * StandardTemplate
 */
public class StandardTemplate extends RootTemplate {

    private HeaderTemplate headerTemplate;
    private FooterTemplate footerTemplate;
    private boolean adEnabled;
    private String appVersion;

    public StandardTemplate(RequestContext requestContext) {
        super(StandardTemplate.class, requestContext);

        headerTemplate = new HeaderTemplate();
        addTemplate(headerTemplate);

        ajax = requestContext.isAjax();

        adEnabled = FeatureManager.isSponsorAdEnabled();

        SystemService systemService = ServiceProvider.getSystemService(requestContext);
        appVersion = systemService.getVersion();

        footerTemplate = new FooterTemplate();
    }

    public StandardTemplate(RequestContext requestContext, String templateName) {
        this(requestContext);
        setTemplateName(templateName);
    }

    public HeaderTemplate getHeaderTemplate() {
        return headerTemplate;
    }

    public FooterTemplate getFooterTemplate() {
        return footerTemplate;
    }

    public boolean isAdEnabled() {
        return adEnabled;
    }

    public void setAdEnabled(boolean adEnabled) {
        this.adEnabled = adEnabled;
    }

    public String getAppVersion() {
        return appVersion;
    }

    public String findTemplate(String templateName) throws Exception {
        addTemplate(footerTemplate);
        return super.findTemplate(templateName);
    }
}