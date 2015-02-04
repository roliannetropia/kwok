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
package com.kwoksys.action.tape;

import com.kwoksys.action.common.template.HeaderTemplate;
import com.kwoksys.action.common.template.StandardTemplate;
import com.kwoksys.biz.ServiceProvider;
import com.kwoksys.biz.admin.dto.AccessUser;
import com.kwoksys.biz.admin.dto.AttributeField;
import com.kwoksys.biz.admin.dto.AttributeFieldCount;
import com.kwoksys.biz.auth.core.Access;
import com.kwoksys.biz.hardware.HardwareService;
import com.kwoksys.biz.hardware.dao.HardwareQueries;
import com.kwoksys.biz.system.core.AppPaths;
import com.kwoksys.biz.system.core.AttributeManager;
import com.kwoksys.biz.system.core.Attributes;
import com.kwoksys.biz.system.core.Links;
import com.kwoksys.framework.connections.database.QueryBits;
import com.kwoksys.framework.properties.Localizer;
import com.kwoksys.framework.session.SessionManager;
import com.kwoksys.framework.struts2.Action2;
import com.kwoksys.framework.ui.Link;
import com.kwoksys.framework.ui.RowStyle;

import javax.servlet.http.HttpSession;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Action class for hardware index page.
 */
public class IndexAction extends Action2 {

    public String execute() throws Exception {
        AccessUser user = requestContext.getUser();
        HttpSession session = request.getSession();

        getSessionBaseForm(HardwareSearchForm.class);

        // Link to hardware list.
        List links = new ArrayList();
        if (Access.hasPermission(user, AppPaths.HARDWARE_LIST)) {
            Map linkMap;

            if (session.getAttribute(SessionManager.HARDWARE_SEARCH_CRITERIA_MAP) != null) {
                linkMap = new HashMap();
                linkMap.put("urlPath", AppPaths.HARDWARE_LIST);
                linkMap.put("urlText", Localizer.getText(requestContext, "common.search.showLastSearch"));
                links.add(linkMap);
            }

            linkMap = new HashMap();
            linkMap.put("urlPath", AppPaths.HARDWARE_LIST + "?cmd=showAll");
            linkMap.put("urlText", Localizer.getText(requestContext, "itMgmt.index.showAllHardware"));
            links.add(linkMap);
        }

        // Get the number of records.
        HardwareService hardwareService = ServiceProvider.getHardwareService(requestContext);
        int numHardwareRecords = hardwareService.getHardwareCount(new QueryBits());

        AttributeManager attributeManager = new AttributeManager(requestContext);

        /*
         * Count Hardware section
         */
        // We'll use the same queryBits for a few different queries below
        QueryBits query = new QueryBits();
        query.addSortColumn(HardwareQueries.getOrderByColumn(AttributeField.NAME));

        Map attrFieldTypeMap = attributeManager.getAttrFieldMapCache(Attributes.HARDWARE_TYPE);
        Map attrFieldStatusMap = attributeManager.getAttrFieldMapCache(Attributes.HARDWARE_STATUS);
        Map attrFieldLocMap = attributeManager.getAttrFieldMapCache(Attributes.HARDWARE_LOCATION);

        // Group by Hardware type.
        List hardwareTypeCounts = new ArrayList();
        List<AttributeFieldCount> hardwareTypeData = hardwareService.getHardwareTypeCount(query);
        RowStyle ui = new RowStyle();

        for (AttributeFieldCount hardware : hardwareTypeData) {
            AttributeField attrField = (AttributeField) attrFieldTypeMap.get(hardware.getAttrFieldId());
            String hardwareTypeName = attrField != null ? Links.getAttrFieldIcon(requestContext, attrField).getString() : Localizer.getText(requestContext, "itMgmt.index.na");

            Map map = new HashMap();
            map.put("countKey", hardwareTypeName);
            map.put("countValue", hardware.getObjectCount());
            map.put("style", ui.getRowClass());
            map.put("path", AppPaths.ROOT + AppPaths.HARDWARE_LIST + "?cmd=groupBy&hardwareType=" + hardware.getAttrFieldId());
            hardwareTypeCounts.add(map);
        }

        // Group by Hardware status.
        List hardwareStatusCounts = new ArrayList();
        List<AttributeFieldCount> hardwareStatusData = hardwareService.getHardwareStatusCount(query);
        ui = new RowStyle();

        for (AttributeFieldCount hardware : hardwareStatusData) {
            AttributeField attrField = (AttributeField) attrFieldStatusMap.get(hardware.getAttrFieldId());
            String hardwareStatusName = attrField != null ? attrField.getName() : Localizer.getText(requestContext, "itMgmt.index.na");

            Map map = new HashMap();
            map.put("countKey", hardwareStatusName);
            map.put("countValue", hardware.getObjectCount());
            map.put("style", ui.getRowClass());
            map.put("path", AppPaths.ROOT + AppPaths.HARDWARE_LIST + "?cmd=groupBy&hardwareStatus=" + hardware.getAttrFieldId());
            hardwareStatusCounts.add(map);
        }

        // Group by Hardware location.
        List locationCounts = new ArrayList();
        List<AttributeFieldCount> hardwareLocationData = hardwareService.getHardwareLocationCount(query);
        ui = new RowStyle();

        for (AttributeFieldCount hardware : hardwareLocationData) {
            AttributeField attrField = (AttributeField) attrFieldLocMap.get(hardware.getAttrFieldId());
            String hardwareLocName = attrField != null ? attrField.getName() : Localizer.getText(requestContext, "itMgmt.index.na");

            Map map = new HashMap();
            map.put("countKey", hardwareLocName);
            map.put("countValue", hardware.getObjectCount());
            map.put("style", ui.getRowClass());
            map.put("path", AppPaths.ROOT + AppPaths.HARDWARE_LIST + "?cmd=groupBy&hardwareLocation=" + hardware.getAttrFieldId());
            locationCounts.add(map);
        }

        //
        // Template: StandardTemplate
        //
        StandardTemplate standardTemplate = new StandardTemplate(requestContext);
        standardTemplate.setAttribute("numHardwareRecords", numHardwareRecords);
        standardTemplate.setAttribute("linkList", links);
        standardTemplate.setAttribute("hardwareTypeCountList", hardwareTypeCounts);
        standardTemplate.setAttribute("hardwareStatusCounts", hardwareStatusCounts);
        standardTemplate.setAttribute("hardwareLocationCountList", locationCounts);

        //
        // Template: HeaderTemplate
        //
        HeaderTemplate header = standardTemplate.getHeaderTemplate();
        header.setTitleKey("core.moduleName.1");
        header.setTitleClassNoLine();
        
        // Link to add hardware.
        if (Access.hasPermission(user, AppPaths.HARDWARE_ADD)) {
            Link link = new Link(requestContext);
            link.setAjaxPath(AppPaths.HARDWARE_ADD);
            link.setTitleKey("itMgmt.cmd.hardwareAdd");
            header.addHeaderCmds(link);
        }

        //
        // Template: HardwareSearchTemplate
        //
        HardwareSearchTemplate searchTemplate = new HardwareSearchTemplate();
        standardTemplate.addTemplate(searchTemplate);
        searchTemplate.setFormAction(AppPaths.HARDWARE_LIST);
        searchTemplate.setHardwareTypeData(hardwareTypeData);
        searchTemplate.setHardwareStatusData(hardwareStatusData);
        searchTemplate.setHardwareLocationData(hardwareLocationData);

        return standardTemplate.findTemplate(STANDARD_TEMPLATE);
    }
}
