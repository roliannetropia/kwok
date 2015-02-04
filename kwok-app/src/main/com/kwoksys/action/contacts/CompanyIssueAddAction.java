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

import com.kwoksys.action.common.template.HeaderTemplate;
import com.kwoksys.action.common.template.StandardV2Template;
import com.kwoksys.action.issues.IssueAssociateTemplate;
import com.kwoksys.biz.ServiceProvider;
import com.kwoksys.biz.contacts.ContactService;
import com.kwoksys.biz.contacts.dto.Company;
import com.kwoksys.biz.system.core.AppPaths;
import com.kwoksys.biz.system.core.ObjectTypes;
import com.kwoksys.framework.struts2.Action2;

/**
 * Action class for adding Company Issue.
 */
public class CompanyIssueAddAction extends Action2 {

    public String execute() throws Exception {
        Integer companyId = requestContext.getParameter("companyId");
        String issueId = requestContext.getParameterString("issueId");

        ContactService contactService = ServiceProvider.getContactService(requestContext);
        Company company = contactService.getCompany(companyId);

        CompanyForm actionForm = (CompanyForm) getBaseForm(CompanyForm.class);
        actionForm.setIssueId(issueId);

        //
        // Template: StandardV2Template
        //
        StandardV2Template standardTemplate = new StandardV2Template(requestContext);
        standardTemplate.setAttribute("companyId", companyId);

        //
        // Template: HeaderTemplate
        //
        HeaderTemplate header = standardTemplate.getHeaderTemplate();
        header.setPageTitleKey("contactMgmt.companyDetail.header", new Object[] {company.getName()});

        //
        // Template: CompanySpecTemplate
        //
        CompanySpecTemplate tmpl = new CompanySpecTemplate(company);
        standardTemplate.addTemplate(tmpl);

        //
        // Template: IssueAssociateTemplate
        //
        IssueAssociateTemplate issueAdd = new IssueAssociateTemplate();
        standardTemplate.addTemplate(issueAdd);
        issueAdd.setIssueId(actionForm.getIssueId());
        issueAdd.setLinkedObjectTypeId(ObjectTypes.COMPANY);
        issueAdd.setLinkedObjectId(companyId);
        issueAdd.setFormSearchAction(AppPaths.CONTACTS_COMPANY_ISSUE_ADD + "?companyId=" + companyId);
        issueAdd.setFormSaveAction(AppPaths.CONTACTS_COMPANY_ISSUE_ADD_2 + "?companyId=" + companyId);
        issueAdd.setFormCancelAction(AppPaths.CONTACTS_COMPANY_ISSUES + "?companyId=" + companyId);

        return standardTemplate.findTemplate(STANDARD_TEMPLATE);
    }
}