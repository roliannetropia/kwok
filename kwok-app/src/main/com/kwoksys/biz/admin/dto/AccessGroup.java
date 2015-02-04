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
package com.kwoksys.biz.admin.dto;

import com.kwoksys.biz.base.BaseObject;

import java.util.Arrays;
import java.util.List;

/**
 * Group
 */
public class AccessGroup extends BaseObject {

    public static final String GROUP_ID = "group_id";
    public static final String GROUP_NAME = "group_name";
    public static final String GROUP_DESCRIPTION = "group_description";
    public static final String ORDER_NUM = "order_num";

    private Integer id;
    private String name;
    private String description;
    private List<Integer> selectedMembers;

    public AccessGroup() {}
    
    public AccessGroup(Integer groupId) {
        this.id = groupId;
    }

    /**
     * Return group member column headers.
     */
    public static List getGroupMembersColumnHeader() {
        return Arrays.asList("username");
    }

    public Integer getId() {
        return id;
    }
    public void setId(Integer id) {
        this.id = id;
    }
    public String getName() {
        return name;
    }
    public void setName(String name) {
        this.name = name;
    }
    public String getDescription() {
        return description;
    }
    public void setDescription(String description) {
        this.description = description;
    }
    public List<Integer> getSelectedMembers() {
        return selectedMembers;
    }
    public void setSelectedMembers(List<Integer> selectedMembers) {
        this.selectedMembers = selectedMembers;
    }
}
