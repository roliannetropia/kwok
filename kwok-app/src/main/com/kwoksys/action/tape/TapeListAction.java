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
import com.kwoksys.action.tape.TapeSearchForm;
import com.kwoksys.biz.ServiceProvider;
import com.kwoksys.biz.admin.dto.AccessUser;
import com.kwoksys.biz.admin.dto.AttributeField;
import com.kwoksys.biz.admin.dto.AttributeFieldCount;
import com.kwoksys.biz.auth.core.Access;
import com.kwoksys.biz.tape.TapeService;
import com.kwoksys.biz.tape.core.TapeSearch;
import com.kwoksys.biz.tape.core.TapeUtils;
import com.kwoksys.biz.tape.dao.TapeQueries;
import com.kwoksys.biz.tape.dto.Tape;
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
 * Action class for displaying tape list.
 */
public class TapeListAction extends Action2 {

    public String execute() throws Exception {
        AccessUser user = requestContext.getUser();
        TapeSearchForm actionForm = (TapeSearchForm) getSessionBaseForm(TapeSearchForm.class);

        String cmd = requestContext.getParameterString("cmd");
        String rowCmd = requestContext.getParameterString("rowCmd");
        String orderBy = SessionManager.getOrSetAttribute(requestContext, "orderBy", SessionManager.TAPE_ORDER_BY, Tape.TAPE_BARCODE_NUMBER);
        String order = SessionManager.getOrSetAttribute(requestContext, "order", SessionManager.TAPE_ORDER, QueryBits.ASCENDING);

//        Filter by tape type
        Integer typeFilter = requestContext.getParameterInteger("mediaType");
        boolean hasTypeFilter = typeFilter != null;
        if (hasTypeFilter) {
            actionForm.setMediaType(typeFilter);
        }

        int rowStart = 0;
        if (!cmd.isEmpty() || rowCmd.equals("showAll") || hasTypeFilter) {
            request.getSession().setAttribute(SessionManager.TAPE_ROW_START, rowStart);
        } else {
            rowStart = SessionManager.getOrSetAttribute(requestContext, "rowStart", SessionManager.TAPE_ROW_START, rowStart);
        }

        int rowLimit = requestContext.getParameter("rowLimit", ConfigManager.app.getTapeRowsToShow());
        if (rowCmd.equals("showAll")) {
            rowLimit = 0;
        }

        // Getting search criteria map from session variable.
        TapeSearch tapeSearch = new TapeSearch(requestContext, SessionManager.TAPE_SEARCH_CRITERIA_MAP);
        tapeSearch.prepareMap(actionForm);

        TapeService tapeService = ServiceProvider.getTapeService(requestContext);

        // Tape type filter
        AttributeManager attributeManager = new AttributeManager(requestContext);
        Map attrFieldTypeMap = attributeManager.getAttrFieldMapCache(Attributes.MEDIA_TYPE);
        List mediaTypeOptions = new ArrayList();
        mediaTypeOptions.add(new SelectOneLabelValueBean(requestContext));

        QueryBits typeQuery = new QueryBits();
        typeQuery.addSortColumn(TapeQueries.getOrderByColumn("attribute_field_name"));

        for (AttributeFieldCount tape : tapeService.getMediaTypeCount(typeQuery)) {
            AttributeField attrField = (AttributeField) attrFieldTypeMap.get(tape.getAttrFieldId());

            String mediaTypeName = attrField == null ? Localizer.getText(requestContext, "itMgmt.index.na") :
                    attrField.getName();

            mediaTypeOptions.add(new LabelValueBean(mediaTypeName, String.valueOf(tape.getAttrFieldId())));
        }

        QueryBits query = new QueryBits(tapeSearch);
        query.setLimit(rowLimit, rowStart);

        if (TapeUtils.isSortableColumn(orderBy)) {
            query.addSortColumn(TapeQueries.getOrderByColumn(orderBy), order);
        }

        int rowCount = tapeService.getTapeCount(query);
        List dataList = null;

        if (rowCount != 0) {
            List<Tape> tapeList = tapeService.getTapeList(query);
            dataList = TapeUtils.formatTapeList(requestContext, tapeList, new Counter(rowStart), AppPaths.TAPE_DETAIL);
        }

        //
        // Template: StandardTemplate
        //
        StandardTemplate standardTemplate = new StandardTemplate(requestContext);

        if (!tapeSearch.getSearchCriteriaMap().isEmpty()) {
            standardTemplate.setAttribute("searchResultText", Localizer.getText(requestContext, "itMgmt.tapeList.searchResult"));
        }
//        todo tape list to tape detail
        standardTemplate.setAttribute("ajaxTapeDetailPath", AppPaths.IT_MGMT_AJAX_GET_TAPE_DETAIL + "?tapeId=");
        standardTemplate.setPathAttribute("formAction", AppPaths.TAPE_LIST);
        standardTemplate.setAttribute("mediaTypeOptions", mediaTypeOptions);

        //
        // Template: TableTemplate
        //
        TableTemplate tableTemplate = new TableTemplate();
        standardTemplate.addTemplate(tableTemplate);
        tableTemplate.setDataList(dataList);
        tableTemplate.setColumnHeaders(TapeUtils.getColumnHeaderList());
        tableTemplate.setSortableColumnHeaders(TapeUtils.getSortableColumns());
        tableTemplate.setColumnPath(AppPaths.TAPE_LIST);
        tableTemplate.setColumnTextKey("common.column.");
        tableTemplate.setRowCmd(rowCmd);
        tableTemplate.setOrderBy(orderBy);
        tableTemplate.setOrder(order);
        tableTemplate.setEmptyRowMsgKey("itMgmt.tapeList.emptyTableMessage");

        //
        // Template: HeaderTemplate
        //
        HeaderTemplate header = standardTemplate.getHeaderTemplate();
        standardTemplate.addTemplate(header);
        header.setTitleKey("itMgmt.tapeList.title");
        header.setTitleClassNoLine();
        
        // Link to add tape.
        if (Access.hasPermission(user, AppPaths.TAPE_ADD)) {
            Link link = new Link(requestContext);
            link.setAjaxPath(AppPaths.TAPE_ADD);
            link.setTitleKey("itMgmt.cmd.tapeAdd");
            header.addHeaderCmds(link);
        }

//todo add new links to sub navbar here
        // Link to Tape export.
//        if (Access.hasPermission(user, AppPaths.TAPE_LIST_EXPORT)) {
//            Link link = new Link(requestContext);
//            link.setExportPath(AppPaths.TAPE_LIST_EXPORT + "?rowCmd=" + rowCmd +"&rowStart=" + rowStart);
//            link.setTitleKey("itMgmt.cmd.tapeListExport");
//            link.setImgSrc(Image.getInstance().getCsvFileIcon());
//            header.addHeaderCmds(link);
//        }

        //
        // Template: RecordsNavigationTemplate
        //
        RecordsNavigationTemplate nav = new RecordsNavigationTemplate();
        standardTemplate.addTemplate(nav);
        nav.setRowOffset(rowStart);
        nav.setRowLimit(rowLimit);
        nav.setRowCount(rowCount);
        nav.setRowCountMsgkey("core.template.recordsNav.rownum");
        nav.setShowAllRecordsText(Localizer.getText(requestContext, "itMgmt.tapeList.rowCount", new Object[]{rowCount}));
        nav.setShowAllRecordsPath(AppPaths.TAPE_LIST + "?rowCmd=showAll");
        nav.setPath(AppPaths.TAPE_LIST + "?rowStart=");

        return standardTemplate.findTemplate(STANDARD_TEMPLATE);
    }
}
