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

import com.kwoksys.action.common.template.ActionErrorsTemplate;
import com.kwoksys.action.common.template.HeaderTemplate;
import com.kwoksys.action.common.template.StandardTemplate;
import com.kwoksys.action.common.template.TabsTemplate;
import com.kwoksys.biz.ServiceProvider;
import com.kwoksys.biz.admin.AdminService;
import com.kwoksys.biz.admin.core.AdminTabs;
import com.kwoksys.biz.admin.core.AdminUtils;
import com.kwoksys.biz.admin.dto.AccessGroup;
import com.kwoksys.biz.admin.dto.GroupPermissionMap;
import com.kwoksys.biz.auth.core.Access;
import com.kwoksys.biz.system.core.AppPaths;
import com.kwoksys.biz.system.core.Links;
import com.kwoksys.framework.connections.database.QueryBits;
import com.kwoksys.framework.struts2.Action2;
import org.apache.commons.lang.StringUtils;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Action class for editing group access
 */
public class GroupAccessEditAction extends Action2 {

    public String execute() throws Exception {
        GroupAccessForm actionForm = (GroupAccessForm) getBaseForm(GroupAccessForm.class);

        AdminService adminService = ServiceProvider.getAdminService(requestContext);
        AccessGroup group = adminService.getGroup(actionForm.getGroupId());

        // Do some sorting.
        QueryBits query = new QueryBits();
        query.addSortColumn(AccessGroup.ORDER_NUM);

        List accessList = new ArrayList();

        // For javascript select all
        List<Integer> permissions = new ArrayList();

        for (GroupPermissionMap groupPerm : adminService.getGroupAccess(query, actionForm.getGroupId())) {
            if (!Access.isPermissionEnabled(groupPerm.getPermId())) {
                continue;
            }

            Map accessMap = new HashMap();
            accessMap.put("accessText", AdminUtils.getPermissionLabel(requestContext, groupPerm.getPermName()));
            String accessName = "formAccess_" + groupPerm.getPermId();
            // This would determine which button is checked.
            accessMap.put(accessName, groupPerm.isHasPermission() ? 1 : 0);
            // Name of the radio button.
            accessMap.put("accessName", accessName);
            accessList.add(accessMap);

            permissions.add(groupPerm.getPermId());
        }

        //
        // Template: StandardTemplate
        //
        StandardTemplate standardTemplate = new StandardTemplate(requestContext);
        standardTemplate.setAttribute("accessList", accessList);
        standardTemplate.setAttribute("permissionList", StringUtils.join(permissions.iterator(), ","));
        standardTemplate.setAttribute("accessOptions", AdminUtils.getUserAccessOptionList());
        standardTemplate.setPathAttribute("formAction", AppPaths.ADMIN_GROUP_ACCESS_EDIT_2 + "?groupId=" + actionForm.getGroupId());
        standardTemplate.setAttribute("formCancelLink", Links.getCancelLink(requestContext, AppPaths.ADMIN_GROUP_ACCESS + "?groupId=" + actionForm.getGroupId()).getString());

        //
        // Template: TemplateGroupDetail
        //
        GroupDetailTemplate template = new GroupDetailTemplate(group);
        standardTemplate.addTemplate(template);

        //
        // Template: HeaderTemplate
        //
        HeaderTemplate header = standardTemplate.getHeaderTemplate();
        header.setTitleKey("admin.groupDetail.header");

        //
        // Template: TabsTemplate
        //
        TabsTemplate tabs = new TabsTemplate();
        standardTemplate.addTemplate(tabs);
        tabs.setTabList(AdminTabs.getGroupTabList(requestContext, actionForm.getGroupId()));
        tabs.setTabActive(AdminTabs.GROUP_ACCESS_TAB);

        //
        // Template: ActionErrorsTemplate
        //
        standardTemplate.addTemplate(new ActionErrorsTemplate());

        return standardTemplate.findTemplate(STANDARD_TEMPLATE);
    }
}
