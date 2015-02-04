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
import com.kwoksys.framework.exceptions.ObjectNotFoundException;
import com.kwoksys.framework.http.ResponseContext;
import com.kwoksys.framework.struts2.Action2;

import java.io.FileNotFoundException;

/**
 * Action class for Company File download.
 */
public class CompanyFileDownloadAction extends Action2 {

    public String execute() throws Exception {
        ResponseContext responseContext = new ResponseContext(response);
        Integer companyId = requestContext.getParameter("companyId");

        try {
            ContactService contactService = ServiceProvider.getContactService(requestContext);
            contactService.getCompany(companyId);

            Integer fileId = requestContext.getParameter("fileId");
            File file = contactService.getCompanyFile(companyId, fileId);

            FileService fileService = ServiceProvider.getFileService(requestContext);
            fileService.download(responseContext, file);

        } catch (ObjectNotFoundException e) {
            throw new FileNotFoundException();
        }
        return null;
    }
}
