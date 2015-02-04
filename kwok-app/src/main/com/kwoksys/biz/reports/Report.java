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
package com.kwoksys.biz.reports;

import com.kwoksys.biz.admin.AdminService;
import com.kwoksys.biz.admin.dto.Attribute;
import com.kwoksys.biz.base.BaseObject;
import com.kwoksys.biz.reports.writers.ReportWriter;
import com.kwoksys.biz.system.core.AttributeManager;
import com.kwoksys.framework.exceptions.DatabaseException;
import com.kwoksys.framework.http.RequestContext;
import com.kwoksys.framework.util.CustomFieldFormatter;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Map;

/**
 * Report
 */
public abstract class Report {

    protected RequestContext requestContext;

    private List columnHeaders = new ArrayList();

    private List<String> reportColumns;

    private String reportColumnOrder;

    private String reportColumnOrderBy;

    private String title;

    public abstract void populateData(ReportWriter reportWriter) throws Exception;

    public abstract String getCsvFilename();

    public abstract String getPdfFilename();

    public abstract String getReportFormName();

    public abstract String getReportPath();

    public List<String> computeColumns(List<String> defaultColumns) {
        if (getReportColumns() != null) {
            List<String> formColumns = getReportColumns();
            List<String> returnColumns = new ArrayList(defaultColumns);
            returnColumns.retainAll(formColumns);
            return returnColumns;
        }
        return defaultColumns;
    }

    /**
     * Computes custom field columns based on
     * @param attrs
     * @return
     */
    public Collection<Attribute> computeCustFieldColumns(RequestContext requestContext, Integer objectTypeId) throws Exception {
        Collection<Attribute> attrs = new AttributeManager(requestContext).getCustomFieldList(objectTypeId);

        if (getReportColumns() != null) {
            List<String> formCustFields = getReportColumns();
            List<Attribute> returnCustFields = new ArrayList();

            for (Attribute attr : attrs) {
                if (formCustFields.contains(String.valueOf(attr.getId()))) {
                    returnCustFields.add(attr);
                }
            }
            return returnCustFields;
        }
        return attrs;
    }

    protected void addCustomFieldValues(AdminService adminService, Collection<Attribute> attrs, List columns,
                                        BaseObject object) throws DatabaseException {

        if (!attrs.isEmpty()) {
            Map valueMap = adminService.getCustomAttributeValueMap(object.getObjectTypeId(), object.getId());
            for (Attribute attr : attrs) {
                columns.add(new CustomFieldFormatter(attr, valueMap.get(attr.getId())).getAttributeTextValue());
            }
        }
    }

    //
    // Getters and setters
    //
    public List getColumnHeaders() {
        return columnHeaders;
    }

    public List<String> getReportColumns() {
        return reportColumns;
    }

    public void setReportColumns(List<String> reportColumns) {
        this.reportColumns = reportColumns;
    }

    public String getReportColumnOrder() {
        return reportColumnOrder;
    }

    public void setReportColumnOrder(String reportColumnOrder) {
        this.reportColumnOrder = reportColumnOrder;
    }

    public String getReportColumnOrderBy() {
        return reportColumnOrderBy;
    }

    public void setReportColumnOrderBy(String reportColumnOrderBy) {
        this.reportColumnOrderBy = reportColumnOrderBy;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }
}
