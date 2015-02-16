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

import com.kwoksys.action.tape.TapeForm;
import com.kwoksys.biz.ServiceProvider;
import com.kwoksys.biz.admin.core.AdminUtils;
import com.kwoksys.biz.admin.dto.Attribute;
import com.kwoksys.biz.tape.TapeService;
import com.kwoksys.biz.tape.dto.Tape;
import com.kwoksys.biz.system.core.AppPaths;
import com.kwoksys.biz.system.core.AttributeManager;
import com.kwoksys.biz.system.core.ObjectTypes;
import com.kwoksys.framework.http.RequestContext;
import com.kwoksys.framework.struts2.Action2;
import org.apache.struts.action.ActionMessages;

import java.util.Map;

/**
 * Action class for adding tape.
 */
public class TapeAdd2Action extends Action2 {

    public String execute() throws Exception {
        TapeForm actionForm = saveActionForm(new TapeForm());

        Tape tape = new Tape();
        tape.setForm(actionForm);

        // Get custom field values from request
        Map<Integer, Attribute> customAttributes = new AttributeManager(requestContext).getCustomFieldMap(ObjectTypes.TAPE);
        AdminUtils.populateCustomFieldValues(requestContext, actionForm, tape, customAttributes);
        
        TapeService tapeService = ServiceProvider.getTapeService(requestContext);

        // Add the tape
        ActionMessages errors = tapeService.addTape(tape, customAttributes);
        if (!errors.isEmpty()) {
            saveActionErrors(errors);
            return redirect(AppPaths.TAPE_ADD + "?" + RequestContext.URL_PARAM_ERROR_TRUE);
        } else {
            System.out.println("tape id tape add 2 action: "+tape.getId());
            System.out.println("tape name tape add 2 action: "+tape.getTapeName());
            return redirect(AppPaths.TAPE_DETAIL + "?tapeId=" + tape.getId());
        }
    }
}
