/*
 * ====================================================================
 * Copyright 2005-2014 Wai-Lun Kwok
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
package com.kwoksys.biz.reports.writers;

import com.kwoksys.biz.reports.Report;
import com.kwoksys.framework.http.ResponseContext;

import java.util.ArrayList;
import java.util.List;

/**
 * HtmlReportWriter
 * Note: instead of writing html, this simply keeps
 */
public class HtmlReportWriter extends ReportWriter {

    private List<List> rows = new ArrayList();

    @Override
    public void init(ResponseContext responseContext, Report report) throws Exception {
    }

    @Override
    public void addHeaderRow(List<String> columnHeaders) {
        this.columnHeaders = columnHeaders;
    }

    @Override
    public void addRow(List<String> row) throws Exception {
        rows.add(row);
    }

    @Override
    public String close() throws Exception {
        return null;
    }

    public List<List> getRows() {
        return rows;
    }
}
