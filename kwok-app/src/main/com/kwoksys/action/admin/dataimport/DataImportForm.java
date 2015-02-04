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
package com.kwoksys.action.admin.dataimport;

import com.kwoksys.action.files.FileUploadForm;
import com.kwoksys.framework.csv.CsvReader;
import com.kwoksys.framework.http.RequestContext;
import com.kwoksys.framework.properties.Localizer;
import org.apache.struts.action.ActionMessage;

import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * DataImportForm
 */
public class DataImportForm extends FileUploadForm {

    private static final Logger logger = Logger.getLogger(DataImportForm.class.getName());

    private CsvReader csvReader;

    private String importType;

    private ActionMessage error;

    @Override
    public void setRequest(RequestContext requestContext) {
        super.setRequest(requestContext);

        importType = requestContext.getParameterString("importType", importType);
        error = null;

        try {
            if (files == null) {
                error = new ActionMessage("common.form.fieldRequired",
                        Localizer.getText(requestContext, "import.selectImportFile"));
                return;
            }

            csvReader = new CsvReader(files[0]);

        } catch (Exception e) {
            // Problem with uploaded file.
            logger.log(Level.WARNING, "Problem with uploaded file", e);

            error = new ActionMessage("import.validate.message.unknown");
        }
    }

    public String getImportType() {
        return importType;
    }

    public void setImportType(String importType) {
        this.importType = importType;
    }

    public CsvReader getCsvReader() {
        return csvReader;
    }

    public ActionMessage getError() {
        return error;
    }
}