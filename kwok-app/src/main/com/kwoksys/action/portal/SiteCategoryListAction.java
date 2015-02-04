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
package com.kwoksys.action.portal;

import com.kwoksys.action.common.template.HeaderTemplate;
import com.kwoksys.action.common.template.StandardV2Template;
import com.kwoksys.action.common.template.TableTemplate;
import com.kwoksys.biz.ServiceProvider;
import com.kwoksys.biz.admin.dto.AccessUser;
import com.kwoksys.biz.auth.core.Access;
import com.kwoksys.biz.base.BaseObject;
import com.kwoksys.biz.portal.PortalService;
import com.kwoksys.biz.portal.PortalUtils;
import com.kwoksys.biz.portal.dao.PortalQueries;
import com.kwoksys.biz.system.core.AppPaths;
import com.kwoksys.biz.system.dto.Category;
import com.kwoksys.framework.connections.database.QueryBits;
import com.kwoksys.framework.struts2.Action2;
import com.kwoksys.framework.ui.Link;
import com.kwoksys.framework.ui.RowStyle;
import com.kwoksys.framework.util.Counter;
import com.kwoksys.framework.util.HtmlUtils;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Action class for displaying category list.
 */
public class SiteCategoryListAction extends Action2 {

    public String execute() throws Exception {
        AccessUser user = requestContext.getUser();

        // Initialize rownum.
        Counter counter = new Counter();

        // Get column headers
        List<String> columnHeaders = PortalUtils.getCategoryColumnHeaderList();

        // Ready to pass variables to query.
        QueryBits query = new QueryBits();
        if (PortalUtils.isSortableCategoryColumn(Category.CATEGORY_NAME)) {
            query.addSortColumn(PortalQueries.getOrderByColumn(Category.CATEGORY_NAME));
        }

        PortalService portalService = ServiceProvider.getPortalService(requestContext);

        List<Category> categories = portalService.getCategories(query);

        List dataList = new ArrayList();

        if (!categories.isEmpty()) {
            boolean canEditCategory = Access.hasPermission(user, AppPaths.PORTAL_CATEGORY_EDIT);
            RowStyle ui = new RowStyle();

            for (Category category : categories) {
                List columns = new ArrayList();

                for (String column : columnHeaders) {
                    if (column.equals(Category.CATEGORY_NAME)) {
                        columns.add(HtmlUtils.encode(category.getName()));

                    } else if (column.equals(Category.CATEGORY_DESCRIPTION)) {
                        columns.add(HtmlUtils.formatMultiLineDisplay(category.getDescription()));

                    } else if (column.equals(Category.CATEGORY_ACTIONS)) {
                        if (canEditCategory) {
                            Link link = new Link(requestContext)
                                    .setTitleKey("portal.cmd.categoryEdit")
                                    .setAjaxPath(AppPaths.PORTAL_CATEGORY_EDIT + "?categoryId=" + category.getId());
                            columns.add(link.getString());
                        } else {
                            columns.add("");
                        }
                    } else if (column.equals(BaseObject.ROWNUM)) {
                        columns.add(counter.incrCounter() + ".");
                    }
                }
                Map map = new HashMap();
                map.put("rowClass", ui.getRowClass());
                map.put("columns", columns);
                dataList.add(map);
            }
        }

        //
        // Template: StandardV2Template
        //
        StandardV2Template standardTemplate = new StandardV2Template(requestContext);

        //
        // Template: HeaderTemplate
        //
        HeaderTemplate header = standardTemplate.getHeaderTemplate();
        header.setTitleKey("portal.siteCategoryList.header");
        header.setTitleClassNoLine();

        // Link to add category.
        if (Access.hasPermission(user, AppPaths.PORTAL_CATEGORY_ADD)) {
            Link link = new Link(requestContext);
            link.setAjaxPath(AppPaths.PORTAL_CATEGORY_ADD);
            link.setTitleKey("portal.siteCategoryAdd.header");
            header.addHeaderCmds(link);
        }

        //
        // Template: TableTemplate
        //
        TableTemplate tableTemplate = new TableTemplate();
        standardTemplate.addTemplate(tableTemplate);
        tableTemplate.setDataList(dataList);
        tableTemplate.setColumnHeaders(columnHeaders);
        tableTemplate.setColumnPath(AppPaths.PORTAL_CATEGORY_LIST);
        tableTemplate.setColumnTextKey("common.column.");

        return standardTemplate.findTemplate(STANDARD_TEMPLATE);
    }
}