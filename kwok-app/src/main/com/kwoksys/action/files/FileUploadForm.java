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
package com.kwoksys.action.files;

import com.kwoksys.biz.base.BaseForm;
import com.kwoksys.framework.http.RequestContext;
import org.apache.struts2.dispatcher.multipart.MultiPartRequestWrapper;

import java.io.File;

/**
 * ActionForm for uploading file.
 */
public class FileUploadForm extends BaseForm {

    private String fileName0;

    protected java.io.File[] files;

    private String[] fileNames;
    private String[] mineTypes;

    @Override
    public void setRequest(RequestContext requestContext) {
        fileName0 = requestContext.getParameterString("fileName0");

        if (requestContext.getRequest() instanceof MultiPartRequestWrapper) {
            MultiPartRequestWrapper multiPartRequestWrapper = (MultiPartRequestWrapper)requestContext.getRequest();

            String formFileName = "file0";

            files = multiPartRequestWrapper.getFiles(formFileName);
            fileNames = multiPartRequestWrapper.getFileNames(formFileName);
            mineTypes = multiPartRequestWrapper.getContentTypes(formFileName);
        }
    }

    public String getFileName0() {
        return fileName0;
    }

    public File[] getFiles() {
        return files;
    }

    public String[] getFileNames() {
        return fileNames;
    }

    public String[] getMineTypes() {
        return mineTypes;
    }
}
