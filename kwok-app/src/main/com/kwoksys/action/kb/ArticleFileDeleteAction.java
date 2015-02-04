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
import com.kwoksys.action.files.FileDeleteTemplate;
import com.kwoksys.biz.ServiceProvider;
import com.kwoksys.biz.files.dto.File;
import com.kwoksys.biz.kb.KbService;
import com.kwoksys.biz.kb.KbUtils;
import com.kwoksys.biz.kb.dto.Article;
import com.kwoksys.biz.system.core.AppPaths;
import com.kwoksys.biz.system.dto.Category;
import com.kwoksys.framework.struts2.Action2;

/**
 * Action class for deleting article file.
 */
public class ArticleFileDeleteAction extends Action2 {

    public String execute() throws Exception {
        Integer articleId = requestContext.getParameter("articleId");

        KbService kbService = ServiceProvider.getKbService(requestContext);
        Article article = kbService.getArticle(articleId);

        Integer fileId = requestContext.getParameter("fileId");
        File file = kbService.getArticleFile(articleId, fileId);

        //
        // Template: StandardV2Template
        //
        StandardV2Template standardTemplate = new StandardV2Template(requestContext);

        //
        // Template: HeaderTemplate
        //
        HeaderTemplate header = standardTemplate.getHeaderTemplate();
        Category category = kbService.getCategory(article.getCategoryId());
        KbUtils.generatePath(header, requestContext, category);

        //
        // Template: ArticleSpecTemplate
        //
        ArticleSpecTemplate articleTemplate = new ArticleSpecTemplate(article);
        standardTemplate.addTemplate(articleTemplate);

        //
        // Template: FileDeleteTemplate
        //
        FileDeleteTemplate fileDelete = new FileDeleteTemplate();
        standardTemplate.addTemplate(fileDelete);
        fileDelete.setFile(file);
        fileDelete.setFormAction(AppPaths.KB_ARTICLE_FILE_DELETE_2 + "?articleId=" + articleId + "&fileId=" + file.getId());
        fileDelete.setFormCancelAction(AppPaths.KB_ARTICLE_DETAIL + "?articleId=" + articleId);

        return standardTemplate.findTemplate(STANDARD_TEMPLATE);
    }
}