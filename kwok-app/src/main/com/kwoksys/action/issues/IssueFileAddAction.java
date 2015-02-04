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

import com.kwoksys.action.common.template.StandardV2Template;
import com.kwoksys.action.files.FileAddTemplate;
import com.kwoksys.action.files.FileUploadForm;
import com.kwoksys.biz.ServiceProvider;
import com.kwoksys.biz.issues.IssueService;
import com.kwoksys.biz.issues.dto.Issue;
import com.kwoksys.biz.system.core.AppPaths;
import com.kwoksys.framework.struts2.Action2;

/**
 * Action class for adding issue file.
 */
public class IssueFileAddAction extends Action2 {

    public String execute() throws Exception {
        Integer issueId = requestContext.getParameter("issueId");

        IssueService issueService = ServiceProvider.getIssueService(requestContext);
        Issue issue = issueService.getIssue(issueId);

        //
        // Template: StandardV2Template
        //
        StandardV2Template standardTemplate = new StandardV2Template(requestContext);

        //
        // Template: IssueSpecTemplate
        //
        IssueSpecTemplate spec = new IssueSpecTemplate(issue);
        standardTemplate.addTemplate(spec);
        spec.setHeaderText(issue.getSubject());

        //
        // Template: FileAddTemplate
        //
        FileAddTemplate fileAdd = new FileAddTemplate(getBaseForm(FileUploadForm.class));
        standardTemplate.addTemplate(fileAdd);
        fileAdd.setFileName(requestContext.getParameterString("fileName0"));
        fileAdd.setFormAction(AppPaths.ISSUES_FILE_ADD_2 + "?issueId=" + issueId);
        fileAdd.setFormCancelAction(AppPaths.ISSUES_DETAIL + "?issueId=" + issueId);
        fileAdd.getErrorsTemplate().setShowRequiredFieldMsg(true);

        return standardTemplate.findTemplate(STANDARD_TEMPLATE);
    }
}