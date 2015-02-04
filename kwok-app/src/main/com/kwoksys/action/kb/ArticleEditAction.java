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

import com.kwoksys.action.common.template.ActionErrorsTemplate;
import com.kwoksys.action.common.template.HeaderTemplate;
import com.kwoksys.action.common.template.StandardTemplate;
import com.kwoksys.biz.ServiceProvider;
import com.kwoksys.biz.kb.KbService;
import com.kwoksys.biz.kb.KbUtils;
import com.kwoksys.biz.kb.dao.KbQueries;
import com.kwoksys.biz.kb.dto.Article;
import com.kwoksys.biz.system.core.AppPaths;
import com.kwoksys.biz.system.core.FeatureManager;
import com.kwoksys.biz.system.core.Links;
import com.kwoksys.biz.system.dto.Category;
import com.kwoksys.framework.connections.database.QueryBits;
import com.kwoksys.framework.struts2.Action2;
import com.kwoksys.framework.util.StringUtils;
import org.apache.struts.util.LabelValueBean;

import java.util.ArrayList;
import java.util.List;

/**
 * Action class for editing KB Article.
 */
public class ArticleEditAction extends Action2 {

    public String execute() throws Exception {
        ArticleForm actionForm = (ArticleForm) getBaseForm(ArticleForm.class);

        KbService kbService = ServiceProvider.getKbService(requestContext);
        Article article = kbService.getArticle(actionForm.getArticleId());

        // Not a resubmit, set some defaults
        if (!actionForm.isResubmit()) {
            actionForm.setArticle(article);
        }

        QueryBits query = new QueryBits();
        query.addSortColumn(KbQueries.getOrderByColumn(Category.CATEGORY_NAME));

        List categoryIdOptions = new ArrayList();
        for (Category category : kbService.getCategories(query)) {
            categoryIdOptions.add(new LabelValueBean(category.getName(),
                    String.valueOf(category.getId())));
        }

        //
        // Template: StandardTemplate
        //
        StandardTemplate standardTemplate = new StandardTemplate(requestContext);
        standardTemplate.setPathAttribute("formAction", AppPaths.KB_ARTICLE_EDIT_2);
        standardTemplate.setPathAttribute("formThisAction", AppPaths.KB_ARTICLE_EDIT + "?articleId=" + article.getId());
        standardTemplate.setAttribute("formCancelLink", Links.getCancelLink(requestContext, AppPaths.KB_ARTICLE_DETAIL + "?articleId=" + article.getId()).getString());
        request.setAttribute("categoryIdOptions", categoryIdOptions);

        boolean isWikiType = KbUtils.isWikiType(actionForm.getArticleSyntax());
        String articleText = actionForm.getArticleText();
        if (!isWikiType) {
            articleText = StringUtils.encodeCkeditorValue(articleText);
        }
        request.setAttribute("articleText", articleText);
        request.setAttribute("isWikiSyntax", isWikiType);

        if (FeatureManager.isWikiFeatureEnabled()) {
            request.setAttribute("articleSyntaxOptions", KbUtils.getArticleSyntaxOptions(requestContext));
        }

        //
        // Template: HeaderTemplate
        //
        HeaderTemplate header = standardTemplate.getHeaderTemplate();
        header.setTitleKey("kb.articleEdit.header");

        //
        // Template: ActionErrorsTemplate
        //
        ActionErrorsTemplate errorsTemplate = new ActionErrorsTemplate();
        standardTemplate.addTemplate(errorsTemplate);
        errorsTemplate.setShowRequiredFieldMsg(true);

        return standardTemplate.findTemplate(STANDARD_TEMPLATE);
    }
}