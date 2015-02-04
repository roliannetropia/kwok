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
package com.kwoksys.action.issues;

import com.kwoksys.action.files.FileUploadForm;
import com.kwoksys.biz.ServiceProvider;
import com.kwoksys.biz.issues.IssueService;
import com.kwoksys.biz.issues.dto.Issue;
import com.kwoksys.biz.system.core.AppPaths;
import com.kwoksys.framework.http.RequestContext;
import com.kwoksys.framework.struts2.Action2;
import org.apache.struts.action.ActionMessages;

/**
 * Action class for adding files.
 */
public class IssueFileAdd2Action extends Action2 {

    public String execute() throws Exception {
        FileUploadForm actionForm = saveActionForm(new FileUploadForm());

        // Instantiate Issue class.
        Issue issue = new Issue();
        issue.setId(requestContext.getParameter("issueId"));

        // Add the file
        IssueService issueService = ServiceProvider.getIssueService(requestContext);
        ActionMessages errors = issueService.addIssueFile(issue, actionForm);
        if (!errors.isEmpty()) {
            saveActionErrors(errors);
            return redirect(AppPaths.ISSUES_FILE_ADD + "?" + RequestContext.URL_PARAM_ERROR_TRUE + "&issueId=" + issue.getId());

        } else {
            return redirect(AppPaths.ISSUES_DETAIL + "?issueId=" + issue.getId());
        }
    }
}
