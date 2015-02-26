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

import com.kwoksys.action.common.template.AjaxTemplate;
import com.kwoksys.action.tape.TapeSpecTemplate;
import com.kwoksys.biz.ServiceProvider;
import com.kwoksys.biz.tape.TapeService;
import com.kwoksys.biz.tape.dto.Tape;
import com.kwoksys.framework.struts2.Action2;

/**
 */
public class AjaxGetTapeDetailAction extends Action2 {

    public String execute() throws Exception {
        Integer tapeId = requestContext.getParameter("tapeId");

        TapeService tapeService = ServiceProvider.getTapeService(requestContext);
        Tape tape = tapeService.getTape(tapeId);

        //
        // Template: AjaxTemplate
        //
        AjaxTemplate ajaxTemplate = new AjaxTemplate(requestContext);
        ajaxTemplate.setMethodName("getTapeDetail");
        ajaxTemplate.setAttribute("tapeId", tapeId);

        //
        // Template: TapeSpecTemplate
        //
        TapeSpecTemplate tmpl = new TapeSpecTemplate(tape);
        ajaxTemplate.addTemplate(tmpl);
        tmpl.setDisableHeader(true);
        tmpl.setColumns(1);

        return ajaxTemplate.findTemplate(AJAX_TEMPLATE);
    }
}
