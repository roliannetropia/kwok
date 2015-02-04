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
import com.kwoksys.biz.ServiceProvider;
import com.kwoksys.biz.admin.AdminService;
import com.kwoksys.biz.admin.core.AdminUtils;
import com.kwoksys.biz.admin.dto.AccessGroup;
import com.kwoksys.biz.admin.dto.AccessUser;
import com.kwoksys.biz.system.core.AppPaths;
import com.kwoksys.biz.system.core.Links;
import com.kwoksys.framework.properties.Localizer;
import com.kwoksys.framework.struts2.Action2;
import org.apache.struts.util.LabelValueBean;

import java.util.ArrayList;
import java.util.List;

/**
 * Action class for updating a group
 */
public class GroupEditAction extends Action2 {

    public String execute() throws Exception {
        GroupForm actionForm = (GroupForm) getBaseForm(GroupForm.class);

        AdminService adminService = ServiceProvider.getAdminService(requestContext);
        AccessGroup group = adminService.getGroup(actionForm.getGroupId());

        // If not a resubmit, set some defaults
        if (!actionForm.isResubmit()) {
            actionForm.setGroup(group);
        }

        // Get a list of available users.
        List availableMembers = new ArrayList();
        for (AccessUser availableSubscriber : adminService.getAvailableMembers(group.getId())) {
            availableMembers.add(new LabelValueBean(AdminUtils.getSystemUsername(requestContext, availableSubscriber), String.valueOf(availableSubscriber.getId())));
        }

        // Get a list of group members.
        List selectedMembers = new ArrayList();
        for (AccessUser selectedSubscriber : adminService.getGroupMembers(group.getId())) {
            selectedMembers.add(new LabelValueBean(AdminUtils.getSystemUsername(requestContext, selectedSubscriber), String.valueOf(selectedSubscriber.getId())));
        }

        //
        // Template: StandardTemplate
        //
        StandardTemplate standardTemplate = new StandardTemplate(requestContext);
        standardTemplate.setPathAttribute("formAction", AppPaths.ADMIN_GROUP_EDIT_2);
        standardTemplate.setAttribute("formCancelLink", Links.getCancelLink(requestContext, AppPaths.ADMIN_GROUP_DETAIL + "?groupId=" + actionForm.getGroupId()).getString());
        request.setAttribute("availableMembersOptions", availableMembers);
        request.setAttribute("selectedMembersOptions", selectedMembers);

        //
        // Template: HeaderTemplate
        //
        HeaderTemplate header = standardTemplate.getHeaderTemplate();
        header.setTitleKey("admin.groupEdit.title");

        //
        // Template: ActionErrorsTemplate
        //
        ActionErrorsTemplate errorsTemplate = new ActionErrorsTemplate();
        standardTemplate.addTemplate(errorsTemplate);
        errorsTemplate.setShowRequiredFieldMsg(true);
        errorsTemplate.setMessage(Localizer.getText(requestContext, "admin.groupEdit.sectionHeader"));

        return standardTemplate.findTemplate(STANDARD_TEMPLATE);
    }
}
