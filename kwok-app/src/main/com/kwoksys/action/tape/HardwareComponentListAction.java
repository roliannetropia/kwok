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

import com.kwoksys.action.common.template.*;
import com.kwoksys.biz.ServiceProvider;
import com.kwoksys.biz.admin.AdminService;
import com.kwoksys.biz.admin.dto.AccessUser;
import com.kwoksys.biz.auth.core.Access;
import com.kwoksys.biz.hardware.HardwareService;
import com.kwoksys.biz.hardware.core.HardwareUtils;
import com.kwoksys.biz.hardware.dao.HardwareQueries;
import com.kwoksys.biz.hardware.dto.Hardware;
import com.kwoksys.biz.hardware.dto.HardwareComponent;
import com.kwoksys.biz.system.core.AppPaths;
import com.kwoksys.biz.system.core.Image;
import com.kwoksys.biz.system.core.ObjectTypes;
import com.kwoksys.framework.connections.database.QueryBits;
import com.kwoksys.framework.properties.Localizer;
import com.kwoksys.framework.struts2.Action2;
import com.kwoksys.framework.ui.Link;
import com.kwoksys.framework.ui.RowStyle;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Action class for showing hardware components.
 */
public class HardwareComponentListAction extends Action2 {

    public String execute() throws Exception {
        AccessUser user = requestContext.getUser();

        // Get request parameters
        Integer hardwareId = requestContext.getParameter("hardwareId");

        HardwareService hardwareService = ServiceProvider.getHardwareService(requestContext);
        Hardware hardware = hardwareService.getHardware(hardwareId);

        HardwareComponentForm actionForm = (HardwareComponentForm) getBaseForm(HardwareComponentForm.class);
        actionForm.setCompId(0);

        // Hardware components
        boolean canEditComponent = Access.hasPermission(user, AppPaths.HARDWARE_COMP_EDIT);
        boolean canDeleteComponent = Access.hasPermission(user, AppPaths.HARDWARE_COMP_DELETE_2);

        AdminService adminService = ServiceProvider.getAdminService(requestContext);
        boolean hasCompCustomFields = adminService.hasCustomFields(ObjectTypes.HARDWARE_COMPONENT);

        List components = new ArrayList();

        QueryBits query = new QueryBits();
        query.addSortColumn(HardwareQueries.getOrderByColumn(HardwareComponent.COMP_NAME));

        RowStyle ui = new RowStyle();
        int colspan = 4;

        List<HardwareComponent> componentList = hardwareService.getHardwareComponents(query, hardware.getId());
        if (!componentList.isEmpty()) {
            for (HardwareComponent component : componentList) {
                Map map = new HashMap();
                map.put("rowClass", ui.getRowClass());
                map.put("component", component);
                if (hasCompCustomFields) {
                    Link link = new Link(requestContext);
                    link.setJavascript("toggleViewUpdate('cf"+component.getId()+"','"
                                                + AppPaths.HARDWARE_AJAX_COMP_CUSTOM_FIELDS + "?hardwareId=" + hardwareId + "&compId=" + component.getId() + "')");
                    link.setImgSrc(Image.getInstance().getMagGlassIcon());
                    map.put("compPath", link.getString());
                }
                if (canEditComponent) {
                    map.put("editPath", AppPaths.HARDWARE_COMP_EDIT + "?hardwareId=" + hardwareId + "&compId=" + component.getId());
                }
                components.add(map);
            }
        }

        //
        // Template: StandardTemplate
        //
        StandardTemplate standardTemplate = new StandardTemplate(requestContext);
        standardTemplate.setAttribute("hardwareId", hardwareId);
        standardTemplate.setAttribute("components", components);
        standardTemplate.setAttribute("colspan", colspan);
        standardTemplate.setAttribute("canEditComponent", canEditComponent);
        standardTemplate.setAttribute("canDeleteComponent", canDeleteComponent);
        standardTemplate.setAttribute("deletePath", AppPaths.HARDWARE_COMP_DELETE_2);

        if (componentList.isEmpty()) {
            //
            // Template: TableEmptyTemplate
            //
            TableEmptyTemplate empty = new TableEmptyTemplate();
            standardTemplate.addTemplate(empty);
            empty.setColSpan(colspan);
            empty.setRowText(Localizer.getText(requestContext, "itMgmt.hardwareComp.emptyTableMessage"));
        }

        //
        // Template: HardwareSpecTemplate
        //
        HardwareSpecTemplate tmpl = new HardwareSpecTemplate(hardware);
        standardTemplate.addTemplate(tmpl);

        //
        // Template: HeaderTemplate
        //
        HeaderTemplate header = standardTemplate.getHeaderTemplate();
        header.setPageTitleKey("itMgmt.hardwareDetail.header", new Object[] {hardware.getName()});
        HardwareUtils.addHardwareHeaderCommands(requestContext, header, hardwareId);
        
        // Add hardware components.
        if (Access.hasPermission(user, AppPaths.HARDWARE_COMP_ADD)) {
            Link link = new Link(requestContext);
            link.setAjaxPath(AppPaths.HARDWARE_COMP_ADD + "?hardwareId=" + hardwareId);
            link.setTitleKey("itMgmt.cmd.hardwareComponentAdd");
            header.addHeaderCmds(link);
        }

        //
        // Template: TabsTemplate
        //
        TabsTemplate tabs = new TabsTemplate();
        standardTemplate.addTemplate(tabs);
        tabs.setTabList(HardwareUtils.hardwareTabList(hardware, requestContext));
        tabs.setTabActive(HardwareUtils.HARDWARE_COMP_TAB);

        //
        // Template: ActionErrorsTemplate
        //
        standardTemplate.addTemplate(new ActionErrorsTemplate());

        return standardTemplate.findTemplate(STANDARD_TEMPLATE);
    }
}