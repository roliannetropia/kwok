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

import com.kwoksys.biz.base.BaseTemplate;
import com.kwoksys.action.common.template.DetailTableTemplate;
import com.kwoksys.biz.admin.dto.AccessUser;
import com.kwoksys.biz.auth.core.Access;
import com.kwoksys.biz.system.core.AppPaths;
import com.kwoksys.biz.system.core.AttributeManager;
import com.kwoksys.biz.system.core.Attributes;
import com.kwoksys.biz.system.core.Image;
import com.kwoksys.framework.ui.Link;
import com.kwoksys.framework.ui.WidgetUtils;
import com.kwoksys.framework.util.HtmlUtils;

/**
 * User spec template.
 * columnText
 * columnValue
 * rowClass
 */
public class UserSpecTemplate extends BaseTemplate {

    private AccessUser recordUser;
    private DetailTableTemplate detailTableTemplate = new DetailTableTemplate();

    public UserSpecTemplate(AccessUser recordUser) {
        super(UserSpecTemplate.class);
        this.recordUser = recordUser;

        addTemplate(detailTableTemplate);
    }

    public void applyTemplate() throws Exception {
        AccessUser accessUser = requestContext.getUser();
        AttributeManager attributeManager = new AttributeManager(requestContext);

        detailTableTemplate.setNumColumns(2);

        DetailTableTemplate.Td td = detailTableTemplate.new Td();
        td.setHeaderKey("common.column.user_id");
        td.setValue(String.valueOf(recordUser.getId()));
        detailTableTemplate.addTd(td);

        td = detailTableTemplate.new Td();
        td.setHeaderKey("common.column.username");
        td.setValue(HtmlUtils.encode(recordUser.getUsername()));
        detailTableTemplate.addTd(td);

        td = detailTableTemplate.new Td();
        td.setHeaderKey("common.column.user_first_name");
        td.setValue(HtmlUtils.encode(recordUser.getFirstName()));
        detailTableTemplate.addTd(td);

        td = detailTableTemplate.new Td();
        td.setHeaderKey("common.column.user_last_name");
        td.setValue(HtmlUtils.encode(recordUser.getLastName()));
        detailTableTemplate.addTd(td);

        td = detailTableTemplate.new Td();
        td.setHeaderKey("common.column.user_display_name");
        td.setValue(HtmlUtils.encode(recordUser.getDisplayName()));
        detailTableTemplate.addTd(td);

        td = detailTableTemplate.new Td();
        td.setHeaderKey("common.column.user_email");
        td.setValue(HtmlUtils.formatMailtoLink(recordUser.getEmail()));
        detailTableTemplate.addTd(td);

        td = detailTableTemplate.new Td();
        td.setHeaderKey("common.column.user_status");
        td.setValue(attributeManager.getAttrFieldNameCache(Attributes.USER_STATUS_TYPE, recordUser.getStatus()));
        detailTableTemplate.addTd(td);

        td = detailTableTemplate.new Td();
        td.setHeaderKey("common.column.user_group");

        Link link = new Link(requestContext);
        if (recordUser.getGroupId() != 0) {
            link.setImgSrc(Image.getInstance().getGroupIcon());
            link.setTitle(recordUser.getGroupName());
            if (Access.hasPermission(accessUser, AppPaths.ADMIN_GROUP_DETAIL)) {
                link.setAjaxPath(AppPaths.ADMIN_GROUP_DETAIL + "?groupId=" + recordUser.getGroupId());
            }
        }
        td.setValue(link.getString());
        detailTableTemplate.addTd(td);

        td = detailTableTemplate.new Td();
        td.setHeaderKey("common.column.user_last_logon");
        td.setValue(recordUser.getLastLogonTime());
        detailTableTemplate.addTd(td);

        td = detailTableTemplate.new Td();
        td.setHeaderKey("common.column.user_last_visit");
        td.setValue(recordUser.getLastVisitTime());
        detailTableTemplate.addTd(td);

        td = detailTableTemplate.new Td();
        td.setHeaderKey("common.column.creator");
        td.setValue(WidgetUtils.formatCreatorInfo(requestContext, recordUser.getCreationDate(), recordUser.getCreator()));
        detailTableTemplate.addTd(td);

        td = detailTableTemplate.new Td();
        td.setHeaderKey("common.column.modifier");
        td.setValue(WidgetUtils.formatCreatorInfo(requestContext, recordUser.getModificationDate(), recordUser.getModifier()));
        detailTableTemplate.addTd(td);
    }

    @Override
    public String getJspPath() {
        return "/WEB-INF/jsp/admin/manageusers/UserSpecTemplate.jsp";
    }
}