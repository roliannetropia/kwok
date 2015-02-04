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

import com.kwoksys.biz.admin.dto.AccessGroup;
import com.kwoksys.biz.base.BaseForm;
import com.kwoksys.framework.http.RequestContext;

import java.util.List;

/**
 * ActionForm for adding/editing user group.
 */
public class GroupForm extends BaseForm {

    private Integer groupId;
    private String groupName;
    private String groupDescription;
    private List<Integer> availableMembers;
    private List<Integer> selectedMembers;

    @Override
    public void setRequest(RequestContext requestContext) {
        groupId = requestContext.getParameterInteger("groupId");
        groupName = requestContext.getParameterString("groupName");
        groupDescription = requestContext.getParameterString("groupDescription");
        availableMembers = requestContext.getParameters("availableMembers");
        selectedMembers = requestContext.getParameters("selectedMembers");
    }

    public void setGroup(AccessGroup group) {
        groupName = group.getName();
        groupDescription = group.getDescription();
    }

    public Integer getGroupId() {
        return groupId;
    }

    public String getGroupName() {
        return groupName;
    }

    public String getGroupDescription() {
        return groupDescription;
    }

    public List<Integer> getAvailableMembers() {
        return availableMembers;
    }
    public List<Integer> getSelectedMembers() {
        return selectedMembers;
    }
}