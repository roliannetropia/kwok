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

import com.kwoksys.action.common.template.ActionErrorsTemplate;
import com.kwoksys.action.common.template.HeaderTemplate;
import com.kwoksys.action.common.template.StandardTemplate;
import com.kwoksys.biz.system.core.AppPaths;
import com.kwoksys.framework.session.SessionManager;
import com.kwoksys.framework.struts2.Action2;

/**
 * Data import.
 */
public class ResultsAction extends Action2 {

    public String execute() throws Exception {
        //
        // Template: StandardTemplate
        //
        StandardTemplate standardTemplate = new StandardTemplate(requestContext);
        standardTemplate.setAttribute("dataList", request.getSession().getAttribute(SessionManager.IMPORT_RESULTS));
        standardTemplate.setPathAttribute("formAction", AppPaths.ADMIN_DATA_IMPORT_EXECUTE);
        standardTemplate.setAttribute("formBackAction", AppPaths.ADMIN_DATA_IMPORT_INDEX);

        //
        // Template: HeaderTemplate
        //
        HeaderTemplate headerTemplate = standardTemplate.getHeaderTemplate();
        headerTemplate.setTitleKey("import.step.results");
        headerTemplate.setTitleClassNoLine();

        //
        // Template: ActionErrorsTemplate
        //
        ActionErrorsTemplate errorsTemplate = new ActionErrorsTemplate();
        standardTemplate.addTemplate(errorsTemplate);
        errorsTemplate.setMessage((String)request.getSession().getAttribute(SessionManager.IMPORT_RESULTS_MESSAGE));

        return standardTemplate.findTemplate(STANDARD_TEMPLATE);
    }
}