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
import com.kwoksys.biz.contacts.dto.CompanyBookmark;
import com.kwoksys.biz.system.core.AppPaths;
import com.kwoksys.framework.http.RequestContext;
import com.kwoksys.framework.struts2.Action2;
import org.apache.struts.action.ActionMessages;

/**
 * Action class for adding company bookmark.
 */
public class CompanyBookmarkAdd2Action extends Action2 {

    public String execute() throws Exception {
        ContactService contactService = ServiceProvider.getContactService(requestContext);

        CompanyBookmarkForm actionForm = saveActionForm(new CompanyBookmarkForm());

        Integer companyId = actionForm.getCompanyId();

        // Check whether the object exists
        contactService.getCompany(companyId);

        // Instantiate CompanyBookmarkDAO class
        CompanyBookmark bookmark = new CompanyBookmark(companyId);
        bookmark.setName(actionForm.getBookmarkName());
        bookmark.setPath(actionForm.getBookmarkPath());
        bookmark.setDescription("");

        ActionMessages errors = contactService.addCompanyBookmark(bookmark);

        if (!errors.isEmpty()) {
            saveActionErrors(errors);
            return redirect(AppPaths.CONTACTS_COMPANY_BOOKMARK_ADD + "?companyId=" + companyId + "&" + RequestContext.URL_PARAM_ERROR_TRUE);

        } else {
            // Reset company bookmark count.
            contactService.resetCompanyBookmarkCount(companyId);

            return redirect(AppPaths.CONTACTS_COMPANY_BOOKMARK + "?companyId=" + companyId + "&bookmarkId=" + bookmark.getId());
        }
    }
}
