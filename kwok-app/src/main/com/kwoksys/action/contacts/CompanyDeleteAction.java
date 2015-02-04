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
import com.kwoksys.action.common.template.ObjectDeleteTemplate;
import com.kwoksys.action.common.template.StandardV2Template;
import com.kwoksys.biz.ServiceProvider;
import com.kwoksys.biz.contacts.ContactService;
import com.kwoksys.biz.contacts.dto.Company;
import com.kwoksys.biz.system.core.AppPaths;
import com.kwoksys.framework.struts2.Action2;

/**
 * Action class for deleting company.
 */
public class CompanyDeleteAction extends Action2 {

    public String execute() throws Exception {
        Integer companyId = requestContext.getParameter("companyId");

        ContactService contactService = ServiceProvider.getContactService(requestContext);
        Company company = contactService.getCompany(companyId);

        //
        // Template: StandardV2Template
        //
        StandardV2Template standardTemplate = new StandardV2Template(requestContext);

        //
        // Template: HeaderTemplate
        //
        HeaderTemplate header = standardTemplate.getHeaderTemplate();
        header.setPageTitleKey("contactMgmt.companyDelete.title");

        //
        // Template: CompanySpecTemplate
        //
        CompanySpecTemplate tmpl = new CompanySpecTemplate(company);
        standardTemplate.addTemplate(tmpl);

        //
        // Template: ObjectDeleteTemplate
        //
        ObjectDeleteTemplate delete = new ObjectDeleteTemplate();
        standardTemplate.addTemplate(delete);
        delete.setFormAction(AppPaths.CONTACTS_COMPANY_DELETE_2 + "?companyId=" + companyId);
        delete.setFormCancelAction(AppPaths.CONTACTS_COMPANY_DETAIL + "?companyId=" + companyId);
        delete.setConfirmationMsgKey("contactMgmt.companyDelete.confirm");
        delete.setSubmitButtonKey("contactMgmt.companyDelete.submitButton");

        return standardTemplate.findTemplate(STANDARD_TEMPLATE);
    }
}
