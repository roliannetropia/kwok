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
package com.kwoksys.action.admin.manageusers;

import com.kwoksys.action.common.template.HeaderTemplate;
import com.kwoksys.action.common.template.StandardTemplate;
import com.kwoksys.action.common.template.TabsTemplate;
import com.kwoksys.biz.ServiceProvider;
import com.kwoksys.biz.admin.AdminService;
import com.kwoksys.biz.admin.core.AdminTabs;
import com.kwoksys.biz.admin.core.AdminUtils;
import com.kwoksys.biz.admin.dto.AccessGroup;
import com.kwoksys.biz.admin.dto.AccessUser;
import com.kwoksys.biz.admin.dto.GroupPermissionMap;
import com.kwoksys.biz.auth.core.Access;
import com.kwoksys.biz.system.core.AppPaths;
import com.kwoksys.biz.system.core.Links;
import com.kwoksys.framework.connections.database.QueryBits;
import com.kwoksys.framework.struts2.Action2;
import com.kwoksys.framework.ui.Link;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Action class for showing group members.
 */
public class GroupAccessAction extends Action2 {

    public String execute() throws Exception {
        Integer groupId = requestContext.getParameter("groupId");
        AccessUser user = requestContext.getUser();

        AdminService adminService = ServiceProvider.getAdminService(requestContext);
        AccessGroup group = adminService.getGroup(groupId);

        //
        // Template: StandardTemplate
        //
        StandardTemplate standardTemplate = new StandardTemplate(requestContext);

        //
        // Template: HeaderTemplate
        //
        HeaderTemplate header = standardTemplate.getHeaderTemplate();
        header.setTitleKey("admin.groupDetail.header");

        if (Access.hasPermission(user, AppPaths.ADMIN_GROUP_ACCESS_EDIT)) {
            Link link = new Link(requestContext);
            link.setAppPath(AppPaths.ADMIN_GROUP_ACCESS_EDIT + "?groupId=" + group.getId());
            link.setTitleKey("admin.cmd.groupAccessEdit");
            header.addHeaderCmds(link);
        }

        header.addNavLink(Links.getAdminHomeLink(requestContext));

        if (Access.hasPermission(user, AppPaths.ADMIN_GROUP_LIST)) {
            Link link = new Link(requestContext);
            link.setAjaxPath(AppPaths.ADMIN_GROUP_LIST);
            link.setTitleKey("admin.index.groupList");
            header.addNavLink(link);
        }

        //
        // Template: TemplateGroupDetail
        //
        standardTemplate.addTemplate(new GroupDetailTemplate(group));

        //
        // Template: TabsTemplate
        //
        TabsTemplate tabs = new TabsTemplate();
        standardTemplate.addTemplate(tabs);
        tabs.setTabList(AdminTabs.getGroupTabList(requestContext, groupId));
        tabs.setTabActive(AdminTabs.GROUP_ACCESS_TAB);

        // Do some sorting.
        QueryBits query = new QueryBits();
        query.addSortColumn(AccessGroup.ORDER_NUM);

        List accessList = new ArrayList();

        for (GroupPermissionMap groupPerm : adminService.getGroupAccess(query, groupId)) {
            if (!Access.isPermissionEnabled(groupPerm.getPermId())) {
                continue;
            }

            Map accessMap = new HashMap();
            accessMap.put("accessText", AdminUtils.getPermissionLabel(requestContext, groupPerm.getPermName()));
            accessMap.put("accessValue", AdminUtils.getUserAccessIcon(groupPerm.isHasPermission()));
            accessList.add(accessMap);
        }
        request.setAttribute("accessList", accessList);

        return standardTemplate.findTemplate(STANDARD_TEMPLATE);
    }
}
