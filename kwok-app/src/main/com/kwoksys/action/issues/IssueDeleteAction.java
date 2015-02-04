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

import com.kwoksys.action.common.template.HeaderTemplate;
import com.kwoksys.action.common.template.ObjectDeleteTemplate;
import com.kwoksys.action.common.template.StandardV2Template;
import com.kwoksys.biz.ServiceProvider;
import com.kwoksys.biz.issues.IssueService;
import com.kwoksys.biz.issues.dto.Issue;
import com.kwoksys.biz.system.core.AppPaths;
import com.kwoksys.framework.struts2.Action2;

/**
 * Action class for deleting issue.
 */
public class IssueDeleteAction extends Action2 {

    public String execute() throws Exception {
        Integer issueId = requestContext.getParameter("issueId");

        IssueService issueService = ServiceProvider.getIssueService(requestContext);
        Issue issue = issueService.getIssue(issueId);

        //
        // Template: StandardV2Template
        //
        StandardV2Template standardTemplate = new StandardV2Template(requestContext);

        //
        // Template: HeaderTemplate
        //
        HeaderTemplate header = standardTemplate.getHeaderTemplate();
        header.setPageTitleKey("issues.issueDelete.title");

        //
        // Template: IssueSpecTemplate
        //
        IssueSpecTemplate spec = new IssueSpecTemplate(issue);
        standardTemplate.addTemplate(spec);
        spec.setHeaderText(issue.getSubject());

        //
        // Template: ObjectDeleteTemplate
        //
        ObjectDeleteTemplate delete = new ObjectDeleteTemplate();
        standardTemplate.addTemplate(delete);
        delete.setFormAction(AppPaths.ISSUES_DELETE_2 + "?issueId=" + issueId);
        delete.setFormCancelAction(AppPaths.ISSUES_DETAIL + "?issueId=" + issueId);
        delete.setConfirmationMsgKey("issues.issueDelete.confirm");
        delete.setSubmitButtonKey("issues.issueDelete.submitButton");

        return standardTemplate.findTemplate(STANDARD_TEMPLATE);
    }
}