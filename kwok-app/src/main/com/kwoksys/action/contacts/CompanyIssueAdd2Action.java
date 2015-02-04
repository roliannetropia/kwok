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
package com.kwoksys.action.contacts;

import com.kwoksys.biz.ServiceProvider;
import com.kwoksys.biz.contacts.ContactService;
import com.kwoksys.biz.system.core.AppPaths;
import com.kwoksys.biz.system.dto.linking.CompanyIssueLink;
import com.kwoksys.framework.http.RequestContext;
import com.kwoksys.framework.struts2.Action2;
import org.apache.struts.action.ActionMessages;

/**
 * Action class for adding a Company Issue.
 */
public class CompanyIssueAdd2Action extends Action2 {

    public String execute() throws Exception {
        Integer companyId = requestContext.getParameter("companyId");
        Integer issueId = requestContext.getParameter("issueId");

        ContactService contactService = ServiceProvider.getContactService(requestContext);
        contactService.getCompany(companyId);

        CompanyIssueLink issueMap = new CompanyIssueLink();
        issueMap.setCompanyId(companyId);
        issueMap.setIssueId(issueId);

        ActionMessages errors = contactService.addCompanyIssue(issueMap);
        if (!errors.isEmpty()) {
            saveActionErrors(errors);
            return redirect(AppPaths.CONTACTS_COMPANY_ISSUE_ADD + "?companyId=" + companyId + "&" + RequestContext.URL_PARAM_ERROR_TRUE);

        } else {
            return redirect(AppPaths.CONTACTS_COMPANY_ISSUES + "?companyId=" + companyId);
        }
    }
}