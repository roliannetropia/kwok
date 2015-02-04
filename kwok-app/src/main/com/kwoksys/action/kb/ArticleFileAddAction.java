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
package com.kwoksys.action.kb;

import com.kwoksys.action.common.template.HeaderTemplate;
import com.kwoksys.action.common.template.StandardV2Template;
import com.kwoksys.action.files.FileAddTemplate;
import com.kwoksys.action.files.FileUploadForm;
import com.kwoksys.biz.ServiceProvider;
import com.kwoksys.biz.kb.KbService;
import com.kwoksys.biz.kb.KbUtils;
import com.kwoksys.biz.kb.dto.Article;
import com.kwoksys.biz.system.core.AppPaths;
import com.kwoksys.biz.system.dto.Category;
import com.kwoksys.framework.struts2.Action2;

/**
 * Action class for adding article file.
 */
public class ArticleFileAddAction extends Action2 {

    public String execute() throws Exception {
        Integer articleId = requestContext.getParameter("articleId");

        KbService kbService = ServiceProvider.getKbService(requestContext);
        Article article = kbService.getArticle(articleId);

        Category category = kbService.getCategory(article.getCategoryId());

        //
        // Template: StandardV2Template
        //
        StandardV2Template standardTemplate = new StandardV2Template(requestContext);

        //
        // Template: HeaderTemplate
        //
        HeaderTemplate header = standardTemplate.getHeaderTemplate();
        KbUtils.generatePath(header, requestContext, category);

        //
        // Template: ArticleSpecTemplate
        //
        ArticleSpecTemplate articleTemplate = new ArticleSpecTemplate(article);
        standardTemplate.addTemplate(articleTemplate);
        
        //
        // Template: FileAddTemplate
        //
        FileUploadForm actionForm = (FileUploadForm) getBaseForm(FileUploadForm.class);

        FileAddTemplate fileAdd = new FileAddTemplate(actionForm);
        standardTemplate.addTemplate(fileAdd);
        fileAdd.setFileName(requestContext.getParameterString("fileName0"));
        fileAdd.setFormAction(AppPaths.KB_ARTICLE_FILE_ADD_2 + "?articleId=" + articleId);
        fileAdd.setFormCancelAction(AppPaths.KB_ARTICLE_DETAIL + "?articleId=" + articleId);
        fileAdd.getErrorsTemplate().setShowRequiredFieldMsg(true);

        return standardTemplate.findTemplate(STANDARD_TEMPLATE);
    }
}