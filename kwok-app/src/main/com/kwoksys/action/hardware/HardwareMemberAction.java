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
package com.kwoksys.action.hardware;

import com.kwoksys.action.common.template.*;
import com.kwoksys.biz.ServiceProvider;
import com.kwoksys.biz.admin.dto.AccessUser;
import com.kwoksys.biz.auth.core.Access;
import com.kwoksys.biz.hardware.HardwareService;
import com.kwoksys.biz.hardware.core.HardwareUtils;
import com.kwoksys.biz.hardware.dao.HardwareQueries;
import com.kwoksys.biz.hardware.dto.Hardware;
import com.kwoksys.biz.system.core.AppPaths;
import com.kwoksys.framework.connections.database.QueryBits;
import com.kwoksys.framework.properties.Localizer;
import com.kwoksys.framework.session.SessionManager;
import com.kwoksys.framework.struts2.Action2;
import com.kwoksys.framework.ui.Link;

import java.util.ArrayList;
import java.util.List;

/**
 * Action class for showing hardware association.
 */
public class HardwareMemberAction extends Action2 {

    public String execute() throws Exception {
        getBaseForm(HardwareMemberForm.class);

        AccessUser user = requestContext.getUser();

        Integer hardwareId = requestContext.getParameter("hardwareId");

        HardwareService hardwareService = ServiceProvider.getHardwareService(requestContext);
        Hardware hardware = hardwareService.getHardware(hardwareId);

        // Hardware members
        String moOrderBy = SessionManager.getOrSetAttribute(requestContext, "moOrderBy", SessionManager.HARDWARE_MEMBER_OF_ORDER_BY, Hardware.HARDWARE_NAME);
        String moOrder = SessionManager.getOrSetAttribute(requestContext, "moOrder", SessionManager.HARDWARE_MEMBER_OF_ORDER, QueryBits.ASCENDING);

        String mOrderBy = SessionManager.getOrSetAttribute(requestContext, "mOrderBy", SessionManager.HARDWARE_MEMBER_ORDER_BY, Hardware.HARDWARE_NAME);
        String mOrder = SessionManager.getOrSetAttribute(requestContext, "mOrder", SessionManager.HARDWARE_MEMBER_ORDER, QueryBits.ASCENDING);

        boolean canRemoveHardware = Access.hasPermission(user, AppPaths.HARDWARE_MEMBER_REMOVE_2);

        // Get column headers
        List hwColumnHeaders = new ArrayList();
        if (canRemoveHardware) {
            // Add an extra blank column to the headers, that's for the radio button to remove hardware
            hwColumnHeaders.add("");
        }
        hwColumnHeaders.addAll(HardwareUtils.getColumnHeaderList());

        //
        // Template: StandardTemplate
        //
        StandardTemplate standardTemplate = new StandardTemplate(requestContext);
        request.setAttribute("formRemoveHardwareAction", AppPaths.HARDWARE_MEMBER_REMOVE_2);
        request.setAttribute("ajaxHardwareDetailPath", AppPaths.IT_MGMT_AJAX_GET_HARDWARE_DETAIL + "?hardwareId=");

        //
        // Template: HardwareSpecTemplate
        //
        HardwareSpecTemplate tmpl = new HardwareSpecTemplate(hardware);
        standardTemplate.addTemplate(tmpl);

        //
        // Template: HeaderTemplate
        //
        HeaderTemplate headerTemplate = standardTemplate.getHeaderTemplate();
        headerTemplate.setPageTitleKey("itMgmt.hardwareDetail.header", new Object[] {hardware.getName()});
        HardwareUtils.addHardwareHeaderCommands(requestContext, headerTemplate, hardwareId);

        // Add hardware member
        if (Access.hasPermission(user, AppPaths.HARDWARE_MEMBER_ADD)) {
            Link link = new Link(requestContext);
            link.setAjaxPath(AppPaths.HARDWARE_MEMBER_ADD + "?hardwareId=" + hardwareId);
            link.setTitleKey("hardware.cmd.hardwareMemberAdd");
            headerTemplate.addHeaderCmds(link);
        }

        //
        // Template: TabsTemplate
        //
        TabsTemplate tabs = new TabsTemplate();
        standardTemplate.addTemplate(tabs);
        tabs.setTabList(HardwareUtils.hardwareTabList(hardware, requestContext));
        tabs.setTabActive(HardwareUtils.HARDWARE_MEMBER_TAB);

        //
        // Member of
        //
        
        //
        // Template: TableEmptyTemplate
        //
        TableEmptyTemplate empty = new TableEmptyTemplate("_memberOf");
        standardTemplate.addTemplate(empty);
        empty.setColSpan(HardwareUtils.getColumnHeaderList().size());
        empty.setRowText(Localizer.getText(requestContext, "hardware.members.emptyMemberOfMessage"));

        //
        // Template: TableHeaderTemplate
        //
        TableHeaderTemplate tableHeader = new TableHeaderTemplate("_memberOf");
        standardTemplate.addTemplate(tableHeader);
        tableHeader.setColumnList(HardwareUtils.getColumnHeaderList());
        tableHeader.setSortableColumnList(HardwareUtils.getSortableColumns());
        tableHeader.setColumnPath(AppPaths.HARDWARE_MEMBER + "?hardwareId=" + hardwareId);
        tableHeader.setColumnTextKey("common.column.");
        tableHeader.setOrderBy(moOrderBy);
        tableHeader.setOrderByParamName("moOrderBy");
        tableHeader.setOrder(moOrder);
        tableHeader.setOrderParamName("moOrder");

        // Do some sorting.
        QueryBits query = new QueryBits();

        if (HardwareUtils.isSortableColumn(moOrderBy)) {
            query.addSortColumn(HardwareQueries.getOrderByColumn(moOrderBy), moOrder);
        }

        //
        // Template: HardwareListTemplate
        //
        HardwareListTemplate listTemplate = new HardwareListTemplate("_memberOf");
        standardTemplate.addTemplate(listTemplate);
        listTemplate.setHardwareList(hardwareService.getHardwareParents(query, hardwareId));
        listTemplate.setHardwarePath(AppPaths.HARDWARE_MEMBER);
        listTemplate.setColspan(HardwareUtils.getColumnHeaderList().size());
        listTemplate.setListHeader("hardware.members.header.memberOf");
        listTemplate.getFormHiddenVariableMap().put("hardwareId", String.valueOf(hardware.getId()));

        //
        // Hardware Members
        //

        //
        // Template: TableEmptyTemplate
        //
        empty = new TableEmptyTemplate("_members");
        standardTemplate.addTemplate(empty);
        empty.setColSpan(hwColumnHeaders.size());
        empty.setRowText(Localizer.getText(requestContext, "hardware.members.emptyMemberMessage"));

        //
        // Template: TableHeaderTemplate
        //
        tableHeader = new TableHeaderTemplate("_members");
        standardTemplate.addTemplate(tableHeader);
        tableHeader.setColumnList(hwColumnHeaders);
        tableHeader.setSortableColumnList(HardwareUtils.getSortableColumns());
        tableHeader.setColumnPath(AppPaths.HARDWARE_MEMBER + "?hardwareId=" + hardwareId);
        tableHeader.setColumnTextKey("common.column.");
        tableHeader.setOrderBy(mOrderBy);
        tableHeader.setOrderByParamName("mOrderBy");
        tableHeader.setOrder(mOrder);
        tableHeader.setOrderParamName("mOrder");

        // Do some sorting.
        query = new QueryBits();

        if (HardwareUtils.isSortableColumn(mOrderBy)) {
            query.addSortColumn(HardwareQueries.getOrderByColumn(mOrderBy), mOrder);
        }

        //
        // Template: HardwareListTemplate
        //
        listTemplate = new HardwareListTemplate("_members");
        standardTemplate.addTemplate(listTemplate);
        listTemplate.setHardwareList(hardwareService.getHardwareMembers(query, hardwareId));
        listTemplate.setHardwarePath(AppPaths.HARDWARE_MEMBER);
        listTemplate.setCanRemoveHardware(canRemoveHardware);        
        listTemplate.setColspan(hwColumnHeaders.size());
        listTemplate.getFormHiddenVariableMap().put("hardwareId", String.valueOf(hardware.getId()));



        //
        // Template: ActionErrorsTemplate
        //
        standardTemplate.addTemplate(new ActionErrorsTemplate());

        return standardTemplate.findTemplate(STANDARD_TEMPLATE);
    }
}