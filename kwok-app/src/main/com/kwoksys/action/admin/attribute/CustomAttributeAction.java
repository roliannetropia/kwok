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
import com.kwoksys.biz.admin.core.AttributeSearch;
import com.kwoksys.biz.admin.dao.AdminQueries;
import com.kwoksys.biz.admin.dto.AccessUser;
import com.kwoksys.biz.admin.dto.Attribute;
import com.kwoksys.biz.admin.dto.AttributeGroup;
import com.kwoksys.biz.auth.core.Access;
import com.kwoksys.biz.system.core.AppPaths;
import com.kwoksys.biz.system.core.AttributeManager;
import com.kwoksys.biz.system.core.Links;
import com.kwoksys.framework.connections.database.QueryBits;
import com.kwoksys.framework.struts2.Action2;
import com.kwoksys.framework.ui.Link;

import java.util.*;

/**
 * Action class for showing custom attributes.
 */
public class CustomAttributeAction extends Action2 {

    public String execute() throws Exception {
        AccessUser user = requestContext.getUser();

        // Do some sorting.
        AttributeSearch attributeSearch = new AttributeSearch();
        attributeSearch.put(AttributeSearch.IS_EDITABLE, true);
        attributeSearch.put(AttributeSearch.IS_CUSTOM_ATTR, true);

        QueryBits query = new QueryBits(attributeSearch);
        query.addSortColumn(AdminQueries.getOrderByColumn(Attribute.ATTR_NAME));

        AdminService adminService = ServiceProvider.getAdminService(requestContext);

        // The data looks like:
        //      Object types<ObjectTypeId,
        //          Map<GroupName + Id,
        //              Map<AttributeGroup, List<Custom fields>>>>
        Map<Integer, Map> objectTypesMap = new LinkedHashMap();
        Map<Integer, AttributeGroup> groupMap = new HashMap();

        for (Integer objectTypeId : AttributeManager.getCustomFieldObjectTypes()) {
            Map<Integer, AttributeGroup> tempGroupMap = adminService.getAttributeGroups(objectTypeId);
            groupMap.putAll(tempGroupMap);

            Map<String, Map> typeGroupMap = new TreeMap();

            for (AttributeGroup group : tempGroupMap.values()) {
                String key = AdminUtils.getAttributeGroupKey(group);
                Map map = new HashMap();
                map.put("group", group);
                map.put("attrGroupEditPath", AppPaths.ADMIN_ATTRIBUTE_GROUP_EDIT + "?objectTypeId=" + objectTypeId + "&attrGroupId=" + group.getId());
                map.put("attrGroupDeletePath", AppPaths.ADMIN_ATTRIBUTE_GROUP_DELETE + "?objectTypeId=" + objectTypeId + "&attrGroupId=" + group.getId());
                map.put("customFields", new ArrayList());
                typeGroupMap.put(key, map);
            }
            objectTypesMap.put(objectTypeId, typeGroupMap);
        }

        for (Attribute attr : adminService.getAttributes(query).values()) {
            Map<String, Map<String, List>> typeGroupMap = objectTypesMap.get(attr.getObjectTypeId());

            AttributeGroup group = groupMap.get(attr.getAttributeGroupId());
            String key = AdminUtils.getAttributeGroupKey(group);

            if (typeGroupMap.get(key) == null) {
                Map map = new HashMap();
                map.put("customFields", new ArrayList());
                typeGroupMap.put(key, map);
            }

            List list = typeGroupMap.get(key).get("customFields");
            if (list == null){
                list = new ArrayList();
            }

            Map attrMap = new HashMap();
            attrMap.put("attrName", new Link(requestContext).setAjaxPath(AppPaths.ADMIN_CUSTOM_ATTR_DETAIL
                    + "?attrId=" + attr.getId()).setTitle(attr.getName()));
            list.add(attrMap);
            
            typeGroupMap.get(key).put("customFields", list);
        }

        //
        // Template: StandardTemplate
        //
        StandardTemplate standardTemplate = new StandardTemplate(requestContext);

        request.setAttribute("objectTypesMap", objectTypesMap);
        if (Access.hasPermission(user, AppPaths.ADMIN_CUSTOM_ATTR_ADD)) {
            request.setAttribute("attrAddPath", AppPaths.ADMIN_CUSTOM_ATTR_ADD + "?objectTypeId=");
        }
        if (user.hasPermission(AppPaths.ADMIN_ATTRIBUTE_GROUP_ADD)) {
            request.setAttribute("attrGroupAddPath", AppPaths.ADMIN_ATTRIBUTE_GROUP_ADD + "?objectTypeId=");
        }

        //
        // Template: HeaderTemplate
        //
        HeaderTemplate header = standardTemplate.getHeaderTemplate();
        standardTemplate.addTemplate(header);
        header.setTitleKey("admin.customAttrList");

        // Back to admin home
        header.addNavLink(Links.getAdminHomeLink(requestContext));
        header.addNavLink(new Link(requestContext).setTitleKey("admin.customAttrList"));

        return standardTemplate.findTemplate(STANDARD_TEMPLATE);
    }
}