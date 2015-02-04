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

import com.kwoksys.biz.ServiceProvider;
import com.kwoksys.biz.issues.IssueService;
import com.kwoksys.biz.system.core.AppPaths;
import com.kwoksys.framework.http.RequestContext;
import com.kwoksys.framework.struts2.Action2;
import org.apache.struts.action.ActionMessages;

import java.util.List;

/**
 * Action class for deleting issue.
 */
public class IssueDelete2Action extends Action2 {

    public String execute() throws Exception {
        Integer issueId = requestContext.getParameter("issueId");
        List<Integer> issueIds = requestContext.getParameters("issueIds");

        IssueService issueService = ServiceProvider.getIssueService(requestContext);

        if (issueIds != null) {
            for (Integer deleteIssueId : issueIds) {
                // Check to make sure the issue exists
                issueService.getIssue(deleteIssueId);
                ActionMessages errors = issueService.deleteIssue(deleteIssueId);

                if (!errors.isEmpty()) {
                    saveActionErrors(errors);
                    return redirect(AppPaths.ISSUES_LIST + "?" + RequestContext.URL_PARAM_ERROR_TRUE);
                }
            }
        } else {
            // Check to make sure the issue exists
            issueService.getIssue(issueId);
            ActionMessages errors = issueService.deleteIssue(issueId);

            if (!errors.isEmpty()) {
                saveActionErrors(errors);
                return redirect(AppPaths.ISSUES_DELETE + "?issueId=" + issueId + "&" + RequestContext.URL_PARAM_ERROR_TRUE);
            }
        }
        return redirect(AppPaths.ISSUES_LIST);
    }
}