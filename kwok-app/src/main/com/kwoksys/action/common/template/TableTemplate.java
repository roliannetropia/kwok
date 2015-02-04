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
package com.kwoksys.action.common.template;

import com.kwoksys.biz.base.BaseTemplate;
import com.kwoksys.biz.auth.core.Access;
import com.kwoksys.framework.http.RequestContext;
import com.kwoksys.framework.properties.Localizer;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

/**
 * TableTemplate
 */
public class TableTemplate extends BaseTemplate {

    public static final String STYLE_TAB = "tabBody";
    public static final String STYLE_LIST = "listTable";

    // Headers
    private List<String> columnHeaders;
    private List<String> sortableColumnHeaders;
    private String columnPath;
    private String columnTextKey;
    private String rowCmd;
    private String orderBy;
    private String order;
    private String orderByParamName;
    private String orderParamName;
    private int colSpan;
    private boolean canRemoveItem;
    private String formRemoveItemAction;
    // Checkbox or radio buttons
    private boolean formSelectMultipleRows;
    private Map<String, String> formHiddenVariableMap = new LinkedHashMap();
    private Map<String, String> formButtons = new LinkedHashMap();
    private String formRowIdName;
    private String formName;

    // Table content
    private String style = STYLE_LIST;
    private List dataList;

    // Empty table
    private String emptyRowMsgKey = "";

    private TableHeaderTemplate tableHeader;
    private TableEmptyTemplate tableEmptyTemplate;

    public TableTemplate() {
        this(null);
    }

    public TableTemplate(String prefix) {
        super(TableTemplate.class, prefix);

        tableHeader = new TableHeaderTemplate(prefix);
        addTemplate(tableHeader);

        tableEmptyTemplate = new TableEmptyTemplate(prefix);
        addTemplate(tableEmptyTemplate);
    }

    public void applyTemplate() throws Exception {
        if (requestContext.getUser() != null && formRemoveItemAction != null) {
            canRemoveItem = Access.hasPermission(requestContext.getUser(), formRemoveItemAction);
        }

        if (canRemoveItem) {
            columnHeaders = new ArrayList(columnHeaders);
            // Add an extra blank column to the headers, that's for the radio button to remove hardware
            columnHeaders.add(0, "");
        }
        
        tableHeader.setColumnList(columnHeaders);
        tableHeader.setSortableColumnList(sortableColumnHeaders);
        tableHeader.setColumnPath(columnPath);
        tableHeader.setColumnTextKey(columnTextKey);
        tableHeader.setRowCmd(rowCmd);
        tableHeader.setOrderBy(orderBy);
        tableHeader.setOrder(order);
        if (orderByParamName != null) {
            tableHeader.setOrderByParamName(orderByParamName);
        }
        if (orderParamName != null) {
            tableHeader.setOrderParamName(orderParamName);
        }

        colSpan = columnHeaders.size();

        if (dataList == null || dataList.isEmpty()) {
            tableEmptyTemplate.setColSpan(columnHeaders.size());

            if (!emptyRowMsgKey.isEmpty()) {
                tableEmptyTemplate.setRowText(Localizer.getText(requestContext, emptyRowMsgKey));
            }
        }

        if (formButtons.isEmpty()) {
            formButtons.put("form.button.remove", "common.form.confirmRemove");
        }

        if (formName == null) {
            formName = RequestContext.FORM_KEY;
        }
    }

    @Override
    public String getJspPath() {
        return "/WEB-INF/jsp/common/template/Table.jsp";
    }

    public void setColumnPath(String columnPath) {
        this.columnPath = columnPath;
    }

    public void setColumnHeaders(List<String> columnHeaders) {
        this.columnHeaders = columnHeaders;
    }

    public void setSortableColumnHeaders(List<String> sortableColumnHeaders) {
        this.sortableColumnHeaders = sortableColumnHeaders;
    }

    public void setColumnTextKey(String columnTextKey) {
        this.columnTextKey = columnTextKey;
    }

    public void setRowCmd(String rowCmd) {
        this.rowCmd = rowCmd;
    }

    public void setOrderBy(String orderBy) {
        this.orderBy = orderBy;
    }

    public void setOrder(String order) {
        this.order = order;
    }

    public void setDataList(List dataList) {
        this.dataList = dataList;
    }

    public List getDataList() {
        return dataList;
    }

    public String getStyle() {
        return style;
    }

    public void setStyle(String style) {
        this.style = style;
    }
    public String getOrderByParamName() {
        return orderByParamName;
    }

    public void setOrderByParamName(String orderByParamName) {
        this.orderByParamName = orderByParamName;
    }

    public String getOrderParamName() {
        return orderParamName;
    }

    public void setOrderParamName(String orderParamName) {
        this.orderParamName = orderParamName;
    }

    public int getColSpan() {
        return colSpan;
    }

    public String getFormRemoveItemAction() {
        return formRemoveItemAction;
    }

    public void setFormRemoveItemAction(String formRemoveItemAction) {
        this.formRemoveItemAction = formRemoveItemAction;
    }

    public boolean isCanRemoveItem() {
        return canRemoveItem;
    }

    public String getFormRowIdName() {
        return formRowIdName;
    }

    public void setFormRowIdName(String formRowIdName) {
        this.formRowIdName = formRowIdName;
    }

    public Map<String, String> getFormButtons() {
        return formButtons;
    }

    public void setFormButtons(Map<String, String> formButtons) {
        this.formButtons = formButtons;
    }

    public Map<String, String> getFormHiddenVariableMap() {
        return formHiddenVariableMap;
    }

    public void setFormHiddenVariableMap(Map<String, String> formHiddenVariableMap) {
        this.formHiddenVariableMap = formHiddenVariableMap;
    }

    public void setEmptyRowMsgKey(String emptyRowMsgKey) {
        this.emptyRowMsgKey = emptyRowMsgKey;
    }

    public void setFormSelectMultipleRows(boolean formSelectMultipleRows) {
        this.formSelectMultipleRows = formSelectMultipleRows;
    }

    public boolean isFormSelectMultipleRows() {
        return formSelectMultipleRows;
    }

    public String getFormName() {
        return formName;
    }

    public void setFormName(String formName) {
        this.formName = formName;
    }
}
