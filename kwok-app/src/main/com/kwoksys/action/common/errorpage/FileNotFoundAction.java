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
package com.kwoksys.action.common.errorpage;

import com.kwoksys.action.common.template.StandardTemplate;
import com.kwoksys.framework.http.RequestContext;
import com.kwoksys.framework.struts2.Action2;

import javax.servlet.http.HttpServletResponse;

/**
 * FileNotFound.
 */
public class FileNotFoundAction extends Action2 {

    public String execute() throws Exception {

        //
        // Template: StandardTemplate
        //
        StandardTemplate standardTemplate = new StandardTemplate(requestContext);

        // Set HTTP response status code
        response.setStatus(HttpServletResponse.SC_NOT_FOUND);

        return standardTemplate.findTemplate(STANDARD_TEMPLATE);
    }
}
