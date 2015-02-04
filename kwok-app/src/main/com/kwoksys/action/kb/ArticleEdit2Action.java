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

import com.kwoksys.biz.ServiceProvider;
import com.kwoksys.biz.system.core.AppPaths;
import com.kwoksys.biz.kb.KbService;
import com.kwoksys.biz.kb.KbUtils;
import com.kwoksys.biz.kb.dto.Article;
import com.kwoksys.framework.http.RequestContext;
import com.kwoksys.framework.struts2.Action2;
import org.apache.struts.action.ActionMessages;

/**
 * Action class for editing KB Article.
 */
public class ArticleEdit2Action extends Action2 {

    public String execute() throws Exception {

        ArticleForm actionForm = saveActionForm(new ArticleForm());

        KbService kbService = ServiceProvider.getKbService(requestContext);

        Article article = kbService.getArticle(actionForm.getArticleId());
        article.setName(actionForm.getArticleName());
        article.setContent(actionForm.getArticleText());
        article.setSyntaxType(actionForm.getArticleSyntax());
        article.setWikiNamespace(KbUtils.formatWikiNamespace(actionForm.getArticleSyntax(), actionForm.getArticleName()));
        article.setCategoryId(actionForm.getCategoryId());

        ActionMessages errors = kbService.updateArticle(article);
        if (!errors.isEmpty()) {
            saveActionErrors(errors);
            return redirect(AppPaths.KB_ARTICLE_EDIT + "?articleId=" + article.getId() + "&" + RequestContext.URL_PARAM_ERROR_TRUE);
        } else {
            return redirect(AppPaths.KB_ARTICLE_DETAIL + "?articleId=" + article.getId());
        }
    }
}