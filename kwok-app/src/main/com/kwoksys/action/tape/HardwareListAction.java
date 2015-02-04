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
import com.kwoksys.action.common.template.RecordsNavigationTemplate;
import com.kwoksys.action.common.template.StandardTemplate;
import com.kwoksys.action.common.template.TableTemplate;
import com.kwoksys.biz.ServiceProvider;
import com.kwoksys.biz.admin.dto.AccessUser;
import com.kwoksys.biz.admin.dto.AttributeField;
import com.kwoksys.biz.admin.dto.AttributeFieldCount;
import com.kwoksys.biz.auth.core.Access;
import com.kwoksys.biz.hardware.HardwareService;
import com.kwoksys.biz.hardware.core.HardwareSearch;
import com.kwoksys.biz.hardware.core.HardwareUtils;
import com.kwoksys.biz.hardware.dao.HardwareQueries;
import com.kwoksys.biz.hardware.dto.Hardware;
import com.kwoksys.biz.system.core.AppPaths;
import com.kwoksys.biz.system.core.AttributeManager;
import com.kwoksys.biz.system.core.Attributes;
import com.kwoksys.biz.system.core.Image;
import com.kwoksys.biz.system.core.configs.ConfigManager;
import com.kwoksys.framework.connections.database.QueryBits;
import com.kwoksys.framework.properties.Localizer;
import com.kwoksys.framework.session.SessionManager;
import com.kwoksys.framework.struts2.Action2;
import com.kwoksys.framework.ui.Link;
import com.kwoksys.framework.ui.SelectOneLabelValueBean;
import com.kwoksys.framework.util.Counter;
import org.apache.struts.util.LabelValueBean;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * Action class for displaying hardware list.
 */
public class HardwareListAction extends Action2 {

    public String execute() throws Exception {
        AccessUser user = requestContext.getUser();
        HardwareSearchForm actionForm = (HardwareSearchForm) getSessionBaseForm(HardwareSearchForm.class);

        String cmd = requestContext.getParameterString("cmd");
        String rowCmd = requestContext.getParameterString("rowCmd");
        String orderBy = SessionManager.getOrSetAttribute(requestContext, "orderBy", SessionManager.HARDWARE_ORDER_BY, Hardware.HARDWARE_NAME);
        String order = SessionManager.getOrSetAttribute(requestContext, "order", SessionManager.HARDWARE_ORDER, QueryBits.ASCENDING);

        // Filter by hardware type
        Integer typeFilter = requestContext.getParameterInteger("hardwareType");
        boolean hasTypeFilter = typeFilter != null;
        if (hasTypeFilter) {
            actionForm.setHardwareType(typeFilter);
        }

        int rowStart = 0;
        if (!cmd.isEmpty() || rowCmd.equals("showAll") || hasTypeFilter) {
            request.getSession().setAttribute(SessionManager.HARDWARE_ROW_START, rowStart);
        } else {
            rowStart = SessionManager.getOrSetAttribute(requestContext, "rowStart", SessionManager.HARDWARE_ROW_START, rowStart);
        }

        int rowLimit = requestContext.getParameter("rowLimit", ConfigManager.app.getHardwareRowsToShow());
        if (rowCmd.equals("showAll")) {
            rowLimit = 0;
        }

        // Getting search criteria map from session variable.
        HardwareSearch hardwareSearch = new HardwareSearch(requestContext, SessionManager.HARDWARE_SEARCH_CRITERIA_MAP);
        hardwareSearch.prepareMap(actionForm);

        HardwareService hardwareService = ServiceProvider.getHardwareService(requestContext);

        // Hardware type filter
        AttributeManager attributeManager = new AttributeManager(requestContext);
        Map attrFieldTypeMap = attributeManager.getAttrFieldMapCache(Attributes.HARDWARE_TYPE);
        List hardwareTypeOptions = new ArrayList();
        hardwareTypeOptions.add(new SelectOneLabelValueBean(requestContext));

        QueryBits typeQuery = new QueryBits();
        typeQuery.addSortColumn(HardwareQueries.getOrderByColumn("attribute_field_name"));

        for (AttributeFieldCount hardware : hardwareService.getHardwareTypeCount(typeQuery)) {
            AttributeField attrField = (AttributeField) attrFieldTypeMap.get(hardware.getAttrFieldId());

            String hardwareTypeName = attrField == null ? Localizer.getText(requestContext, "itMgmt.index.na") :
                    attrField.getName();

            hardwareTypeOptions.add(new LabelValueBean(hardwareTypeName, String.valueOf(hardware.getAttrFieldId())));
        }

        QueryBits query = new QueryBits(hardwareSearch);
        query.setLimit(rowLimit, rowStart);

        if (HardwareUtils.isSortableColumn(orderBy)) {
            query.addSortColumn(HardwareQueries.getOrderByColumn(orderBy), order);
        }

        int rowCount = hardwareService.getHardwareCount(query);
        List dataList = null;

        if (rowCount != 0) {
            List<Hardware> hardwareList = hardwareService.getHardwareList(query);
            dataList = HardwareUtils.formatHardwareList(requestContext, hardwareList, new Counter(rowStart), AppPaths.HARDWARE_DETAIL);
        }

        //
        // Template: StandardTemplate
        //
        StandardTemplate standardTemplate = new StandardTemplate(requestContext);

        if (!hardwareSearch.getSearchCriteriaMap().isEmpty()) {
            standardTemplate.setAttribute("searchResultText", Localizer.getText(requestContext, "itMgmt.hardwareList.searchResult"));
        }
        standardTemplate.setAttribute("ajaxHardwareDetailPath", AppPaths.IT_MGMT_AJAX_GET_HARDWARE_DETAIL + "?hardwareId=");
        standardTemplate.setPathAttribute("formAction", AppPaths.HARDWARE_LIST);
        standardTemplate.setAttribute("hardwareTypeOptions", hardwareTypeOptions);

        //
        // Template: TableTemplate
        //
        TableTemplate tableTemplate = new TableTemplate();
        standardTemplate.addTemplate(tableTemplate);
        tableTemplate.setDataList(dataList);
        tableTemplate.setColumnHeaders(HardwareUtils.getColumnHeaderList());
        tableTemplate.setSortableColumnHeaders(HardwareUtils.getSortableColumns());
        tableTemplate.setColumnPath(AppPaths.HARDWARE_LIST);
        tableTemplate.setColumnTextKey("common.column.");
        tableTemplate.setRowCmd(rowCmd);
        tableTemplate.setOrderBy(orderBy);
        tableTemplate.setOrder(order);
        tableTemplate.setEmptyRowMsgKey("itMgmt.hardwareList.emptyTableMessage");

        //
        // Template: HeaderTemplate
        //
        HeaderTemplate header = standardTemplate.getHeaderTemplate();
        standardTemplate.addTemplate(header);
        header.setTitleKey("itMgmt.hardwareList.title");
        header.setTitleClassNoLine();
        
        // Link to add hardware.
        if (Access.hasPermission(user, AppPaths.HARDWARE_ADD)) {
            Link link = new Link(requestContext);
            link.setAjaxPath(AppPaths.HARDWARE_ADD);
            link.setTitleKey("itMgmt.cmd.hardwareAdd");
            header.addHeaderCmds(link);
        }

        // Link to Hardware export.
        if (Access.hasPermission(user, AppPaths.HARDWARE_LIST_EXPORT)) {
            Link link = new Link(requestContext);
            link.setExportPath(AppPaths.HARDWARE_LIST_EXPORT + "?rowCmd=" + rowCmd +"&rowStart=" + rowStart);
            link.setTitleKey("itMgmt.cmd.hardwareListExport");
            link.setImgSrc(Image.getInstance().getCsvFileIcon());
            header.addHeaderCmds(link);
        }

        //
        // Template: RecordsNavigationTemplate
        //
        RecordsNavigationTemplate nav = new RecordsNavigationTemplate();
        standardTemplate.addTemplate(nav);
        nav.setRowOffset(rowStart);
        nav.setRowLimit(rowLimit);
        nav.setRowCount(rowCount);
        nav.setRowCountMsgkey("core.template.recordsNav.rownum");
        nav.setShowAllRecordsText(Localizer.getText(requestContext, "itMgmt.hardwareList.rowCount", new Object[]{rowCount}));
        nav.setShowAllRecordsPath(AppPaths.HARDWARE_LIST + "?rowCmd=showAll");
        nav.setPath(AppPaths.HARDWARE_LIST + "?rowStart=");

        return standardTemplate.findTemplate(STANDARD_TEMPLATE);
    }
}
