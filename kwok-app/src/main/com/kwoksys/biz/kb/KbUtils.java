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
package com.kwoksys.biz.kb;

import com.kwoksys.action.common.template.HeaderTemplate;
import com.kwoksys.biz.base.BaseObject;
import com.kwoksys.biz.kb.dto.Article;
import com.kwoksys.biz.system.core.AppPaths;
import com.kwoksys.biz.system.core.AttributeFieldIds;
import com.kwoksys.biz.system.core.AttributeManager;
import com.kwoksys.biz.system.core.Attributes;
import com.kwoksys.biz.system.dto.Category;
import com.kwoksys.framework.http.RequestContext;
import com.kwoksys.biz.system.core.configs.ConfigManager;
import com.kwoksys.framework.exceptions.DatabaseException;
import com.kwoksys.framework.parsers.wiki.Parser;
import com.kwoksys.framework.parsers.wiki.WikiParserFactory;
import com.kwoksys.framework.ui.Link;
import org.apache.struts.util.LabelValueBean;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * KBUtil.
 */
public class KbUtils {

    public static final int ARTICLE_NAME_MAX_LEN = 225;

    /**
     * Specifies the column header for the list page.
     */
    public static List getCategoryColumnHeaderList() {
        return Arrays.asList(BaseObject.ROWNUM, Category.CATEGORY_NAME,
                Category.CATEGORY_DESCRIPTION, Article.ARTICLE_OBJECT_COUNT,
                Category.CATEGORY_ACTIONS);
    }

    /**
     * Specifies the sortable columns allowed.
     */
    public static List getCategorySortableColumnList() {
        return Arrays.asList(Category.CATEGORY_NAME);
    }

    public static String[] getArticleColumnsDefault() {
        return new String[] {Article.ARTICLE_NAME, Article.VIEW_COUNT};
    }

    /**
     * Specify the column header for the list page.
     */
    public static List<String> getArticleColumnHeaderList() {
        return Arrays.asList(ConfigManager.app.getKbArticleColumns());
    }

    /**
     * Checks whether a column is sortable.
     *
     * @param columnName
     * @return ..
     */
    public static boolean isSortableCategoryColumn(String columnName) {
        return getCategorySortableColumnList().contains(columnName);
    }

    public static void generatePath(HeaderTemplate header, RequestContext requestContext, Category category) {
        Link link = new Link(requestContext);
        link.setAjaxPath(AppPaths.KB_INDEX);
        link.setTitleKey("kb.articleDetail.home");
        header.addNavLink(link);

        link = new Link(requestContext);
        link.setAjaxPath(AppPaths.KB_ARTICLE_LIST + "?categoryId=" + category.getId());
        link.setTitle(category.getName());
        header.addNavLink(link);
    }

    /**
     * Truncates kb article content. This truncates content for display in search results.
     *
     * @param input
     * @return ..
     */
    public static String truncateBodyText(String input) {
        // Could put this in system config table
        int limit = 300;

        if (input.length() > limit) {
            input = input.substring(0, limit) + "...";
        }
        return input;
    }

    public static String formatContent(Article article) throws Exception {
        Parser parser;
        if (article.getSyntaxType().equals(AttributeFieldIds.KB_ARTICLE_SYNTAX_MEDIAWIKI)) {
            parser = WikiParserFactory.getParser(WikiParserFactory.TYPE_MEDIAWIKI);
            return parser.parseHtml(article.getContent());

        } else if (article.getSyntaxType().equals(AttributeFieldIds.KB_ARTICLE_SYNTAX_TWIKI)) {
                parser = WikiParserFactory.getParser(WikiParserFactory.TYPE_TWIKI);
                return parser.parseHtml(article.getContent());
        }
        return article.getContent();
    }

    public static boolean isWikiType(int syntaxType) {
        return (syntaxType == AttributeFieldIds.KB_ARTICLE_SYNTAX_MEDIAWIKI
                    || syntaxType == AttributeFieldIds.KB_ARTICLE_SYNTAX_TWIKI);
    }

    /**
     * Generates an option dropdown box with article syntax types. This can be overridden by users.
     * @param request
     * @return
     */
    public static List getArticleSyntaxOptions(RequestContext requestContext) throws DatabaseException {
        return getDefaultArticleSyntaxOptions(requestContext);
    }

    /**
     * Generates an option dropdown box with article syntax types.
     * @param request
     * @return
     */
    public static List getDefaultArticleSyntaxOptions(RequestContext requestContext) throws DatabaseException {
        AttributeManager attributeManager = new AttributeManager(requestContext);
        List options = new ArrayList();
        for (Integer syntax : Article.ARTICLE_SYNTAX_OPTION_LIST) {
            String syntaxName = attributeManager.getAttrFieldNameCache(Attributes.KB_ARTICLE_SYNTAX_TYPE, syntax);
            options.add(new LabelValueBean(syntaxName, syntax.toString()));
        }
        return options;
    }

    public static String formatWikiNamespace(Integer wikiSyntaxType, String namespace) {
        if (wikiSyntaxType.equals(AttributeFieldIds.KB_ARTICLE_SYNTAX_MEDIAWIKI)) {
            return namespace.replace(" ", "_");
        } else {
            return namespace;
        }
    }
}