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

import com.kwoksys.biz.admin.core.dataimport.ImportManager;
import com.kwoksys.biz.system.core.AppPaths;
import com.kwoksys.framework.csv.CsvReader;
import com.kwoksys.framework.struts2.Action2;

/**
 * Data import.
 */
public class ExecuteAction extends Action2 {

    public String execute() throws Exception {
        DataImportForm actionForm = (DataImportForm) getSessionBaseForm(DataImportForm.class);

        CsvReader csvReader = actionForm.getCsvReader();

        ImportManager importManager = ImportManager.newInstance(requestContext, actionForm.getImportType());
        importManager.execute(csvReader.getData());

        csvReader.cleanup();

        return redirect(AppPaths.ADMIN_DATA_IMPORT_RESULTS);
    }
}