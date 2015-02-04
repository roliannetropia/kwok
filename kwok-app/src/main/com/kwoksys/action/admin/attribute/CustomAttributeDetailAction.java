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
package com.kwoksys.action.admin.attribute;

import com.kwoksys.action.common.template.HeaderTemplate;
import com.kwoksys.action.common.template.StandardTemplate;
import com.kwoksys.biz.ServiceProvider;
import com.kwoksys.biz.admin.AdminService;
import com.kwoksys.biz.admin.core.AdminUtils;
import com.kwoksys.biz.admin.dto.AccessUser;
import com.kwoksys.biz.admin.dto.Attribute;
import com.kwoksys.biz.admin.dto.AttributeField;
import com.kwoksys.biz.admin.dto.AttributeGroup;
import com.kwoksys.biz.auth.core.Access;
import com.kwoksys.biz.system.core.AppPaths;
import com.kwoksys.biz.system.core.Links;
import com.kwoksys.framework.session.CacheManager;
import com.kwoksys.framework.struts2.Action2;
import com.kwoksys.framework.ui.Link;
import com.kwoksys.framework.util.StringUtils;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

/**
 * Action class for custom attribute detail.
 */
public class CustomAttributeDetailAction extends Action2 {

    public String execute() throws Exception {
        Integer attributeId = requestContext.getParameter("attrId");

        AccessUser user = requestContext.getUser();

        AdminService adminService = ServiceProvider.getAdminService(requestContext);
        Attribute attr = adminService.getCustomAttribute(attributeId);

        Integer objectAttrType = AdminUtils.getObjectTypeMap().get(attr.getObjectTypeId());

        // Get a list of System Fields for a particular object type
        if (AdminUtils.isAttributeTypeMappingEnabled(objectAttrType)) {
            // Get a list of System Fields supposed to be for this attribute id
            Set<Integer> mappedAttrIds = adminService.getAttributeFieldTypesByField(attr.getId());

            List systemFields = new ArrayList();
            for (AttributeField attrField : new CacheManager(requestContext).getAttributeFieldsCache(objectAttrType).values()) {
                if (mappedAttrIds.contains(attrField.getId())) {
                    systemFields.add(new Link(requestContext).setAjaxPath(AppPaths.ADMIN_ATTRIBUTE_DETAIL
                            + "?attributeId=" + attrField.getAttributeId()).setTitle(attrField.getName()));
                }
            }
            request.setAttribute("systemFields", systemFields);
        }

        //
        // Template: StandardTemplate
        //
        StandardTemplate standardTemplate = new StandardTemplate(requestContext);

        request.setAttribute("attr", attr);
        request.setAttribute("attrOption", StringUtils.join(attr.getAttributeOptions(), ", "));
        request.setAttribute("attrConvertUrl", attr.isConvertUrl() ? "common.boolean.yes_no.yes" : "common.boolean.yes_no.no");

        if (attr.getAttributeGroupId() != 0) {
            AttributeGroup attributeGroup = adminService.getAttributeGroup(attr.getAttributeGroupId(), attr.getObjectTypeId());
            request.setAttribute("attrGroup", attributeGroup);
        }

        //
        // Template: HeaderTemplate
        //
        HeaderTemplate header = standardTemplate.getHeaderTemplate();
        header.setTitleKey("admin.customAttrDetail.header");

        if (Access.hasPermission(user, AppPaths.ADMIN_CUSTOM_ATTR_EDIT)) {
            Link link = new Link(requestContext);
            link.setAjaxPath(AppPaths.ADMIN_CUSTOM_ATTR_EDIT + "?attrId=" + attr.getId());
            link.setTitleKey("admin.cmd.customAttrEdit");
            header.addHeaderCmds(link);
        }

        if (Access.hasPermission(user, AppPaths.ADMIN_CUSTOM_ATTR_DELETE)) {
            Link link = new Link(requestContext);
            link.setAjaxPath(AppPaths.ADMIN_CUSTOM_ATTR_DELETE + "?attrId=" + attr.getId());
            link.setTitleKey("admin.cmd.customAttrDelete");
            header.addHeaderCmds(link);
        }

        header.addNavLink(Links.getAdminHomeLink(requestContext));

        if (Access.hasPermission(user, AppPaths.ADMIN_CUSTOM_ATTR_LIST)) {
            Link link = new Link(requestContext);
            link.setAjaxPath(AppPaths.ADMIN_CUSTOM_ATTR_LIST);
            link.setTitleKey("admin.customAttrList");
            header.addNavLink(link);
        }

        return standardTemplate.findTemplate(STANDARD_TEMPLATE);
    }
}