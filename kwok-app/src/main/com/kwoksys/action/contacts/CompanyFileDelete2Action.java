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
import com.kwoksys.biz.files.FileService;
import com.kwoksys.biz.files.dto.File;
import com.kwoksys.biz.system.core.AppPaths;
import com.kwoksys.framework.struts2.Action2;

/**
 * Action class for Company File delete.
 */
public class CompanyFileDelete2Action extends Action2 {

    public String execute() throws Exception {
        Integer companyId = requestContext.getParameter("companyId");

        ContactService contactService = ServiceProvider.getContactService(requestContext);
        contactService.getCompany(companyId);

        // Instantiate File class
        Integer fileId = requestContext.getParameter("fileId");
        File file = contactService.getCompanyFile(companyId, fileId);

        FileService fileService = ServiceProvider.getFileService(requestContext);

        // Delete the file
        fileService.deleteFile(file);

        // Reset File count
        contactService.resetCompanyFileCount(companyId);

        return redirect(AppPaths.CONTACTS_COMPANY_FILES + "?companyId=" + companyId);
    }
}
