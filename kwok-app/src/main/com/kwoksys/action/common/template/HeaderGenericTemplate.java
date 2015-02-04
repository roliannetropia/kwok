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
import com.kwoksys.biz.admin.core.AdminUtils;
import com.kwoksys.framework.session.SessionManager;
import com.kwoksys.biz.system.core.AppPaths;

/**
 * HeaderGenericTemplate class.
 */
public class HeaderGenericTemplate extends BaseTemplate {

    private String titleText;
    private String headerText;
    private String onloadJavascript;

    public HeaderGenericTemplate() {
        super(HeaderGenericTemplate.class, null);
    }

    public void setTitleText(String titleText) {
        this.titleText = titleText;
    }

    public void setHeaderText(String headerText) {
        this.headerText = headerText;
    }

    public void setOnloadJavascript(String onloadJavascript) {
        this.onloadJavascript = onloadJavascript;
    }

    public void applyTemplate() {
        String sessionTheme = SessionManager.getAppSessionTheme(request.getSession());

        request.setAttribute("HeaderGenericTemplate_titleText", AdminUtils.getTitleText(requestContext, titleText));
        request.setAttribute("HeaderGenericTemplate_themeStylePath", AppPaths.getInstance().getThemeCss(sessionTheme));
        request.setAttribute("HeaderGenericTemplate_onloadJavascript", onloadJavascript);
        request.setAttribute("HeaderGenericTemplate_headerText", headerText);
    }
}
