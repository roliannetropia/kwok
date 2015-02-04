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
package com.kwoksys.biz.admin.core;

import com.kwoksys.biz.admin.dto.AccessUser;
import com.kwoksys.framework.exceptions.DatabaseException;
import com.kwoksys.biz.auth.core.Access;
import com.kwoksys.biz.system.core.AppPaths;
import com.kwoksys.framework.properties.Localizer;
import com.kwoksys.framework.http.RequestContext;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * AdminTabs
 */
public class AdminTabs {

    public static final String GROUP_ACCESS_TAB = "accessTab";

    public static final String GROUP_MEMBERS_TAB = "membersTab";

    public static final String USER_ACCESS_TAB = "accessTab";

    public static final String USER_CONACT_TAB = "contactTab";

    public static final String USER_HARDWARE_TAB = "hardwareTab";

    public static List getGroupTabList(RequestContext requestContext, Integer groupId) throws DatabaseException {
        AccessUser user = requestContext.getUser();

        List tabList = new ArrayList();

        // Link to User Members view.
        if (Access.hasPermission(user, AppPaths.ADMIN_GROUP_DETAIL)) {
            Map tabMap = new HashMap();
            tabMap.put("tabName", AdminTabs.GROUP_MEMBERS_TAB);
            tabMap.put("tabPath", AppPaths.ADMIN_GROUP_DETAIL + "?groupId=" + groupId);
            tabMap.put("tabText", Localizer.getText(requestContext, "admin.groupDetail.membersTab"));
            tabList.add(tabMap);
        }

        // Link to Group Access view.
        if (Access.hasPermission(user, AppPaths.ADMIN_GROUP_ACCESS)) {
            Map tabMap = new HashMap();
            tabMap.put("tabName", AdminTabs.GROUP_ACCESS_TAB);
            tabMap.put("tabPath", AppPaths.ADMIN_GROUP_ACCESS + "?groupId=" + groupId);
            tabMap.put("tabText", Localizer.getText(requestContext, "admin.groupDetail.accessTab"));
            tabList.add(tabMap);
        }
        return tabList;
    }
}
