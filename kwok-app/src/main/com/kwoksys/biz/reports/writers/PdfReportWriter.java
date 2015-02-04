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
import com.kwoksys.framework.util.HttpUtils;
import com.kwoksys.framework.util.StringUtils;
import com.lowagie.text.Document;
import com.lowagie.text.Font;
import com.lowagie.text.Paragraph;
import com.lowagie.text.Phrase;
import com.lowagie.text.pdf.PdfWriter;

import javax.servlet.ServletOutputStream;
import java.io.ByteArrayOutputStream;
import java.util.List;

/**
 * PdfReportWriter
 */
public class PdfReportWriter extends ReportWriter {

    private ResponseContext responseContext;
    private ByteArrayOutputStream baos = new ByteArrayOutputStream();
    private Document document = new Document();
    private Font font = new Font();
    private Font fontHeader = new Font();

    @Override
    public void init(ResponseContext responseContext, Report report) throws Exception {
        this.responseContext = responseContext;
        HttpUtils.setDownloadResponseHeaders(responseContext.getResponse(), report.getPdfFilename());

        PdfWriter.getInstance(document, baos);
        document.open();

        fontHeader.setStyle(Font.BOLD);

        if (report.getTitle() != null && !report.getTitle().isEmpty()) {
            document.add(new Paragraph(report.getTitle(), fontHeader));
            document.add(new Paragraph(" "));
        }
    }

    @Override
    public void addHeaderRow(List<String> columnHeaders) {
        this.columnHeaders = columnHeaders;
    }

    @Override
    public void addRow(List<String> row) throws Exception {
        for (int i=0; i<row.size(); i++) {
            Paragraph para = new Paragraph();
            para.add(new Phrase(columnHeaders.get(i), fontHeader));
            para.add(new Phrase(": " + StringUtils.replaceNull(row.get(i)), font));
            document.add(para);
        }
        document.add(new Paragraph(" "));
    }

    @Override
    public String close() throws Exception {
        document.close();
        responseContext.getResponse().setContentLength(baos.size());
        ServletOutputStream out = responseContext.getResponse().getOutputStream();
        baos.writeTo(out);
        out.flush();

        return null;
    }
}
