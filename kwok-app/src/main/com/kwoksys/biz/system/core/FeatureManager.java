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
package com.kwoksys.biz.system.core;

import com.kwoksys.biz.admin.dto.AccessUser;
import com.kwoksys.biz.system.core.configs.ConfigManager;
import com.kwoksys.framework.license.LicenseManager;

/**
 * FeatureManager
 */
public class FeatureManager {

    /**
     * Determines whether to turn on Report Issue links.
     * @return
     */
    public static boolean isIssueGuestSubmitModuleEnabled(AccessUser accessUser) {
        return accessUser.isLoggedOn() && ConfigManager.app.isIssuesGuestSubmitModuleEnabled();
    }

    /**
     * Determines whether to turn on Report Issue links.
     * @return
     */
    public static boolean isIssueGuestSubmitFooterEnabled() {
        return ConfigManager.app.isIssuesGuestSubmitFooterEnabled();
    }

    /**
     * Returns whether this instance in multi-instance environment.
     * @param user
     * @return
     */
    public static boolean isMultiAppsInstance() {
        return (ConfigManager.admin.isMultiAppsInstance());
    }

    /**
     * Returns whether to enable sponsor ad.
     * @return
     */
    public static boolean isSponsorAdEnabled() {
        return !LicenseManager.isCommercialEdition();
    }

    public static boolean isWikiFeatureEnabled() {
        return LicenseManager.isCommercialEdition();
    }
}
