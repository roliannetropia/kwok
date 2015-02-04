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

import com.kwoksys.biz.ServiceProvider;
import com.kwoksys.biz.hardware.HardwareService;
import com.kwoksys.biz.system.core.AppPaths;
import com.kwoksys.biz.system.dto.linking.HardwareIssueLink;
import com.kwoksys.framework.http.RequestContext;
import com.kwoksys.framework.struts2.Action2;
import org.apache.struts.action.ActionMessages;

/**
 * Action class for adding a hardware component.
 */
public class HardwareIssueAdd2Action extends Action2 {

    public String execute() throws Exception {
        Integer hardwareId = requestContext.getParameter("hardwareId");
        Integer issueId = requestContext.getParameter("issueId");

        HardwareService hardwareService = ServiceProvider.getHardwareService(requestContext);
        hardwareService.getHardware(hardwareId);

        HardwareIssueLink issueMap = new HardwareIssueLink();
        issueMap.setHardwareId(hardwareId);
        issueMap.setIssueId(issueId);

        ActionMessages errors = hardwareService.addHardwareIssue(issueMap);
        if (!errors.isEmpty()) {
            saveActionErrors(errors);
            return redirect(AppPaths.HARDWARE_ISSUE_ADD + "?hardwareId=" + hardwareId + "&" + RequestContext.URL_PARAM_ERROR_TRUE);

        } else {
            return redirect(AppPaths.HARDWARE_ISSUE + "?hardwareId=" + hardwareId);
        }
    }
}