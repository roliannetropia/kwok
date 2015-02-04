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
import com.kwoksys.action.common.template.ObjectDeleteTemplate;
import com.kwoksys.action.common.template.StandardV2Template;
import com.kwoksys.biz.ServiceProvider;
import com.kwoksys.biz.admin.AdminService;
import com.kwoksys.biz.admin.dto.AccessGroup;
import com.kwoksys.biz.system.core.AppPaths;
import com.kwoksys.framework.struts2.Action2;

/**
 * Action class for deleting group.
 */
public class GroupDeleteAction extends Action2 {

    public String execute() throws Exception {
        Integer groupId = requestContext.getParameter("groupId");

        AdminService adminService = ServiceProvider.getAdminService(requestContext);
        AccessGroup accessGroup = adminService.getGroup(groupId);

        //
        // Template: StandardV2Template
        //
        StandardV2Template standardTemplate = new StandardV2Template(requestContext);

        //
        // Template: HeaderTemplate
        //
        HeaderTemplate header = standardTemplate.getHeaderTemplate();
        header.setTitleKey("admin.groupDetail.header");

        //
        // Template: GroupDetailTemplate
        //
        GroupDetailTemplate groupDetailTemplate = new GroupDetailTemplate(accessGroup);
        standardTemplate.addTemplate(groupDetailTemplate);

        //
        // Template: ObjectDeleteTemplate
        //
        ObjectDeleteTemplate deleteTemplate = new ObjectDeleteTemplate();
        standardTemplate.addTemplate(deleteTemplate);
        deleteTemplate.setFormAction(AppPaths.ADMIN_GROUP_DELETE_2 + "?groupId=" + accessGroup.getId());
        deleteTemplate.setFormCancelAction(AppPaths.ADMIN_GROUP_DETAIL + "?groupId=" + accessGroup.getId());
        deleteTemplate.setConfirmationMsgKey("admin.groupDelete.confirm");
        deleteTemplate.setSubmitButtonKey("admin.groupDelete.buttonSubmit");

        return standardTemplate.findTemplate(STANDARD_TEMPLATE);
    }
}