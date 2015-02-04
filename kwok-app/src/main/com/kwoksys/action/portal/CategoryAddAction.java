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
package com.kwoksys.action.portal;

import com.kwoksys.action.common.template.ActionErrorsTemplate;
import com.kwoksys.action.common.template.HeaderTemplate;
import com.kwoksys.action.common.template.StandardTemplate;
import com.kwoksys.biz.system.core.AppPaths;
import com.kwoksys.biz.system.core.Links;
import com.kwoksys.biz.system.dto.Category;
import com.kwoksys.framework.struts2.Action2;

/**
 * Action class for adding site category
 */
public class CategoryAddAction extends Action2 {

    public String execute() throws Exception {
        Category category = new Category();

        // If not a resubmit, set some defaults
        SiteCategoryForm actionForm = (SiteCategoryForm) getBaseForm(SiteCategoryForm.class);
        if (!actionForm.isResubmit()) {
            actionForm.setCategoryName(category.getName());
        }

        //
        // Template: StandardTemplate
        //
        StandardTemplate standardTemplate = new StandardTemplate(requestContext);
        standardTemplate.setPathAttribute("formAction", AppPaths.PORTAL_CATEGORY_ADD_2);
        standardTemplate.setAttribute("formCancelLink", Links.getCancelLink(requestContext, AppPaths.PORTAL_CATEGORY_LIST).getString());

        //
        // Template: HeaderTemplate
        //
        HeaderTemplate headerTemplate = standardTemplate.getHeaderTemplate();
        headerTemplate.setTitleKey("portal.siteCategoryAdd.header");

        //
        // Template: ActionErrorsTemplate
        //
        ActionErrorsTemplate errorsTemplate = new ActionErrorsTemplate();
        standardTemplate.addTemplate(errorsTemplate);
        errorsTemplate.setShowRequiredFieldMsg(true);

        return standardTemplate.findTemplate(STANDARD_TEMPLATE);
    }
}
