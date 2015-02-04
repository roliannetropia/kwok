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

/**
 * User Permission Map Object.
 */
public class UserPermissionMap {

    private Integer userId;      // UserId of the user whose access is being updated.
    private boolean hasPermission;
    private Integer permId;     // PermissionId of User being granted/revoked access.
    private String permName;
    private int cmd;

    //
    // Getter and Setter
    //
    public Integer getUserId() {
        return userId;
    }
    public void setUserId(Integer userId) {
        this.userId = userId;
    }
    public boolean isHasPermission() {
        return hasPermission;
    }
    public void setHasPermission(boolean hasPermission) {
        this.hasPermission = hasPermission;
    }
    public Integer getPermId() {
        return permId;
    }
    public void setPermId(Integer permId) {
        this.permId = permId;
    }
    public String getPermName() {
        return permName;
    }
    public void setPermName(String permName) {
        this.permName = permName;
    }
    public void setCmd(int cmd) {
        this.cmd = cmd;
    }
    public int getCmd() {
        return cmd;
    }
}
