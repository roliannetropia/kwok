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
import com.kwoksys.action.common.template.StandardV2Template;
import com.kwoksys.action.common.template.TableTemplate;
import com.kwoksys.action.common.template.TabsTemplate;
import com.kwoksys.biz.ServiceProvider;
import com.kwoksys.biz.admin.AdminService;
import com.kwoksys.biz.admin.core.AdminTabs;
import com.kwoksys.biz.admin.core.AdminUtils;
import com.kwoksys.biz.admin.dto.AccessGroup;
import com.kwoksys.biz.admin.dto.AccessUser;
import com.kwoksys.biz.auth.core.Access;
import com.kwoksys.biz.system.core.AppPaths;
import com.kwoksys.biz.system.core.Image;
import com.kwoksys.biz.system.core.Links;
import com.kwoksys.framework.struts2.Action2;
import com.kwoksys.framework.ui.Link;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Action class for showing group detail
 */
public class GroupDetailAction extends Action2 {

    public String execute() throws Exception {
        AccessUser user = requestContext.getUser();
        Integer groupId = requestContext.getParameter("groupId");

        AdminService adminService = ServiceProvider.getAdminService(requestContext);
        AccessGroup group = adminService.getGroup(groupId);

        //
        // Template: StandardV2Template
        //
        StandardV2Template standardTemplate = new StandardV2Template(requestContext);

        List groupMembers = new ArrayList();
        List<AccessUser> users = adminService.getGroupMembers(groupId);
        if (!users.isEmpty()) {
            for (AccessUser requestUser : users) {
                List columns = new ArrayList();
                columns.add(new Link(requestContext).setAjaxPath(AppPaths.ADMIN_USER_DETAIL + "?userId="
                                        + requestUser.getId()).setTitle(AdminUtils.getSystemUsername(requestContext, requestUser)));
                Map map = new HashMap();
                map.put("columns", columns);
                groupMembers.add(map);
            }
        }

        //
        // Template: HeaderTemplate
        //
        HeaderTemplate header = standardTemplate.getHeaderTemplate();
        header.setTitleKey("admin.groupDetail.header");

        if (Access.hasPermission(user, AppPaths.ADMIN_GROUP_EDIT)) {
            Link link = new Link(requestContext);
            link.setAjaxPath(AppPaths.ADMIN_GROUP_EDIT + "?groupId=" + group.getId());
            link.setTitleKey("admin.groupEdit.title");
            link.setImgSrc(Image.getInstance().getGroupEditIcon());
            header.addHeaderCmds(link);
        }

        if (user.hasPermission(AppPaths.ADMIN_GROUP_DELETE)) {
            Link link = new Link(requestContext);
            link.setAjaxPath(AppPaths.ADMIN_GROUP_DELETE + "?groupId=" + group.getId());
            link.setTitleKey("admin.groupDelete.title");
            link.setImgSrc(Image.getInstance().getGroupDeleteIcon());
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
        // Template: GroupDetailTemplate
        //
        GroupDetailTemplate template = new GroupDetailTemplate(group);
        standardTemplate.addTemplate(template);

        //
        // Template: TabsTemplate
        //
        TabsTemplate tabs = new TabsTemplate();
        standardTemplate.addTemplate(tabs);
        tabs.setTabList(AdminTabs.getGroupTabList(requestContext, groupId));
        tabs.setTabActive(AdminTabs.GROUP_MEMBERS_TAB);

        //
        // Template: TableTemplate
        //
        TableTemplate tableTemplate = new TableTemplate();
        standardTemplate.addTemplate(tableTemplate);
        tableTemplate.setColumnHeaders(AccessGroup.getGroupMembersColumnHeader());
        tableTemplate.setColumnTextKey("common.column.");
        tableTemplate.setStyle(TableTemplate.STYLE_TAB);
        tableTemplate.setDataList(groupMembers);
        tableTemplate.setEmptyRowMsgKey("admin.groupMembers.emptyTableMessage");

        return standardTemplate.findTemplate(STANDARD_TEMPLATE);
    }
}
