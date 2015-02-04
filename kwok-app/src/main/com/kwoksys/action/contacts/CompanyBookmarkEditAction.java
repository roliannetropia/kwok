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

import com.kwoksys.action.common.template.ActionErrorsTemplate;
import com.kwoksys.action.common.template.HeaderTemplate;
import com.kwoksys.action.common.template.StandardTemplate;
import com.kwoksys.biz.ServiceProvider;
import com.kwoksys.biz.contacts.ContactService;
import com.kwoksys.biz.contacts.dto.Company;
import com.kwoksys.biz.system.core.AppPaths;
import com.kwoksys.biz.system.core.Links;
import com.kwoksys.biz.system.dto.Bookmark;
import com.kwoksys.framework.struts2.Action2;

/**
 * Action class for editing company bookmark.
 */
public class CompanyBookmarkEditAction extends Action2 {

    public String execute() throws Exception {
        CompanyBookmarkForm actionForm = (CompanyBookmarkForm) getBaseForm(CompanyBookmarkForm.class);

        ContactService contactService = ServiceProvider.getContactService(requestContext);

        Integer companyId = actionForm.getCompanyId();
        Company company = contactService.getCompany(companyId);

        Integer bookmarkId = actionForm.getBookmarkId();
        Bookmark bookmark = contactService.getCompanyBookmark(companyId, bookmarkId);

        // If not a resubmit, set some defaults
        if (!actionForm.isResubmit()) {
            actionForm.setBookmark(bookmark);
        }

        //
        // Template: StandardTemplate
        //
        StandardTemplate standardTemplate = new StandardTemplate(requestContext);
        standardTemplate.setAttribute("companyId", companyId);
        standardTemplate.setPathAttribute("formAction", AppPaths.CONTACTS_COMPANY_BOOKMARK_EDIT_2);
        standardTemplate.setAttribute("formCancelLink", Links.getCancelLink(requestContext, AppPaths.CONTACTS_COMPANY_BOOKMARK + "?companyId=" + companyId).getString());
        standardTemplate.setAttribute("bookmarkId", bookmarkId);

        //
        // Template: CompanySpecTemplate
        //
        CompanySpecTemplate tmpl = new CompanySpecTemplate(company);
        standardTemplate.addTemplate(tmpl);

        //
        // Template: HeaderTemplate
        //
        HeaderTemplate header = standardTemplate.getHeaderTemplate();
        header.setPageTitleKey("contactMgmt.companyDetail.header", new Object[] {company.getName()});

        //
        // Template: ActionErrorsTemplate
        //
        ActionErrorsTemplate errorsTemplate = new ActionErrorsTemplate();
        standardTemplate.addTemplate(errorsTemplate);
        errorsTemplate.setShowRequiredFieldMsg(true);

        return standardTemplate.findTemplate(STANDARD_TEMPLATE);
    }
}