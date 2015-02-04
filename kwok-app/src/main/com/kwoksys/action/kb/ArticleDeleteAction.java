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
import com.kwoksys.action.common.template.ObjectDeleteTemplate;
import com.kwoksys.action.common.template.StandardV2Template;
import com.kwoksys.biz.ServiceProvider;
import com.kwoksys.biz.kb.KbService;
import com.kwoksys.biz.kb.KbUtils;
import com.kwoksys.biz.kb.dto.Article;
import com.kwoksys.biz.system.core.AppPaths;
import com.kwoksys.biz.system.dto.Category;
import com.kwoksys.framework.struts2.Action2;

/**
 * Action class for deleting Article.
 */
public class ArticleDeleteAction extends Action2 {

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
        standardTemplate.addTemplate(new ArticleSpecTemplate(article));

        //
        // Template: ObjectDeleteTemplate
        //
        ObjectDeleteTemplate delete = new ObjectDeleteTemplate();
        standardTemplate.addTemplate(delete);
        delete.setFormAction(AppPaths.KB_ARTICLE_DELETE_2 + "?articleId=" + articleId);
        delete.setFormCancelAction(AppPaths.KB_ARTICLE_DETAIL + "?articleId=" + articleId);
        delete.setConfirmationMsgKey("kb.articleDelete.confirm");
        delete.setSubmitButtonKey("kb.articleDelete.submitButton");

        return standardTemplate.findTemplate(STANDARD_TEMPLATE);
    }
}