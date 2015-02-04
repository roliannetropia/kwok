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

import com.kwoksys.biz.base.BaseTemplate;
import com.kwoksys.action.common.template.ActionErrorsTemplate;
import com.kwoksys.biz.base.BaseForm;
import com.kwoksys.biz.system.core.AppPaths;

/**
 * Template for Add File.
 */
public class FileAddTemplate extends BaseTemplate {

    private ActionErrorsTemplate errorsTemplate = new ActionErrorsTemplate();
    private String formAction;
    private String formCancelAction;
    private String fileName;

    public FileAddTemplate(BaseForm form) {
        super(FileAddTemplate.class);

        addTemplate(errorsTemplate);
    }

    public void applyTemplate() {
    }

    @Override
    public String getJspPath() {
        return "/WEB-INF/jsp/files/FileAddTemplate.jsp";
    }

    public String getFormAction() {
        return formAction;
    }
    public void setFormAction(String formAction) {
        this.formAction = AppPaths.ROOT + formAction;
    }
    public String getFormCancelAction() {
        return formCancelAction;
    }
    public void setFormCancelAction(String formCancelAction) {
        this.formCancelAction = AppPaths.ROOT + formCancelAction;
    }
    public String getFileName() {
        return fileName;
    }
    public void setFileName(String fileName) {
        this.fileName = fileName;
    }

    public ActionErrorsTemplate getErrorsTemplate() {
        return errorsTemplate;
    }
}