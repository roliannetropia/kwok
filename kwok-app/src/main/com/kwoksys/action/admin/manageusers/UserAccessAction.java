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
import com.kwoksys.biz.admin.dao.AdminQueries;
import com.kwoksys.biz.admin.dto.AccessUser;
import com.kwoksys.biz.admin.dto.UserPermissionMap;
import com.kwoksys.biz.auth.core.Access;
import com.kwoksys.biz.system.core.AppPaths;
import com.kwoksys.biz.system.core.Links;
import com.kwoksys.framework.connections.database.QueryBits;
import com.kwoksys.framework.struts2.Action2;
import com.kwoksys.framework.ui.Link;
import org.apache.commons.lang.StringUtils;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Action class for getting user access info.
 */
public class UserAccessAction extends Action2 {

    public String execute() throws Exception {
        AccessUser user = requestContext.getUser();

        Integer reqUserId = requestContext.getParameter("userId");
        String cmd = requestContext.getParameterString("cmd");

        AdminService adminService = ServiceProvider.getAdminService(requestContext);
        AccessUser requestUser = adminService.getUser(reqUserId);

        if (!cmd.equals("edit")) {
            cmd = "detail";
        }

        // Do some sorting.
        QueryBits query = new QueryBits();
        query.addSortColumn(AdminQueries.getOrderByColumn("order_num"));

        List accessList = new ArrayList();

        // For javascript select all
        List<Integer> permissions = new ArrayList();

        for (UserPermissionMap userperm : adminService.getUserAccess(query, reqUserId)) {
            if (!Access.isPermissionEnabled(userperm.getPermId())) {
                continue;
            }

            Map accessMap = new HashMap();
            accessMap.put("accessText", AdminUtils.getPermissionLabel(requestContext, userperm.getPermName()));

            if (cmd.equals("edit")) {
                String accessName = "formAccess_" + userperm.getPermId();
                // This would determine which button is checked.
                accessMap.put(accessName, userperm.isHasPermission() ? 1 : 0);
                // Name of the radio button.
                accessMap.put("accessName", accessName);
                accessMap.put("accessOptions", AdminUtils.getUserAccessOptionList());

                permissions.add(userperm.getPermId());

            } else {
                accessMap.put("accessValue", AdminUtils.getUserAccessIcon(userperm.isHasPermission()));
            }
            accessList.add(accessMap);
        }

        //
        // Template: StandardTemplate
        //
        StandardTemplate standardTemplate = new StandardTemplate(requestContext);
        standardTemplate.setAttribute("cmd", cmd);
        standardTemplate.setAttribute("accessList", accessList);
        standardTemplate.setAttribute("permissionList", StringUtils.join(permissions.iterator(), ","));
        standardTemplate.setAttribute("accessOptions", AdminUtils.getUserAccessOptionList());
        standardTemplate.setPathAttribute("formAction", AppPaths.ADMIN_USER_ACCESS_EDIT_2 + "?userId=" + reqUserId);
        standardTemplate.setAttribute("formCancelLink", Links.getCancelLink(requestContext, AppPaths.ADMIN_USER_ACCESS + "?userId=" + reqUserId).getString());
        standardTemplate.setAttribute("formDisabled", AdminUtils.disableAccessEdit(requestUser));

        //
        // Template: UserSpecTemplate
        //
        standardTemplate.addTemplate(new UserSpecTemplate(requestUser));

        //
        // Template: TabsTemplate
        //
        TabsTemplate tabs = new TabsTemplate();
        standardTemplate.addTemplate(tabs);
        tabs.setTabList(AdminUtils.userTabList(requestContext, requestUser));
        tabs.setTabActive(AdminTabs.USER_ACCESS_TAB);

        //
        // Template: HeaderTemplate
        //
        HeaderTemplate header = standardTemplate.getHeaderTemplate();

        if (Access.hasPermission(user, AppPaths.ADMIN_USER_ACCESS_EDIT)) {
            Link link = new Link(requestContext);
            link.setAppPath(AppPaths.ADMIN_USER_ACCESS + "?userId=" + reqUserId + "&cmd=edit");
            link.setTitleKey("admin.cmd.userAccessEdit");
            header.addHeaderCmds(link);
        }

        // Back to admin home
        header.addNavLink(Links.getAdminHomeLink(requestContext));

        if (Access.hasPermission(user, AppPaths.ADMIN_USER_INDEX)) {
            Link link = new Link(requestContext);
            link.setAjaxPath(AppPaths.ADMIN_USER_INDEX);
            link.setTitleKey("admin.userIndex.userSearchTitle");
            header.addNavLink(link);
        }

        if (Access.hasPermission(user, AppPaths.ADMIN_USER_LIST)) {
            Link link = new Link(requestContext);
            link.setAjaxPath(AppPaths.ADMIN_USER_LIST);
            link.setTitleKey("admin.userList.title");
            header.addNavLink(link);
        }

        //
        // Template: ActionErrorsTemplate
        //
        standardTemplate.addTemplate(new ActionErrorsTemplate());

        return standardTemplate.findTemplate(STANDARD_TEMPLATE);
    }
}
