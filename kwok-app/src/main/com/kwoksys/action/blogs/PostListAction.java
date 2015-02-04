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
package com.kwoksys.action.blogs;

import com.kwoksys.action.common.template.HeaderTemplate;
import com.kwoksys.action.common.template.RecordsNavigationTemplate;
import com.kwoksys.action.common.template.StandardTemplate;
import com.kwoksys.biz.ServiceProvider;
import com.kwoksys.biz.admin.core.AdminUtils;
import com.kwoksys.biz.admin.dto.AccessUser;
import com.kwoksys.biz.auth.core.Access;
import com.kwoksys.biz.blogs.BlogService;
import com.kwoksys.biz.blogs.core.BlogPostSearch;
import com.kwoksys.biz.blogs.dao.BlogQueries;
import com.kwoksys.biz.blogs.dto.BlogPost;
import com.kwoksys.biz.portal.PortalUtils;
import com.kwoksys.biz.system.core.AppPaths;
import com.kwoksys.biz.system.core.Image;
import com.kwoksys.biz.system.core.configs.ConfigManager;
import com.kwoksys.biz.system.dto.Category;
import com.kwoksys.framework.connections.database.QueryBits;
import com.kwoksys.framework.properties.Localizer;
import com.kwoksys.framework.session.SessionManager;
import com.kwoksys.framework.struts2.Action2;
import com.kwoksys.framework.ui.Link;

import javax.servlet.http.HttpSession;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Action class for showing blog posts.
 */
public class PostListAction extends Action2 {

    public String execute() throws Exception {
        PostSearchForm actionForm = (PostSearchForm) getSessionBaseForm(PostSearchForm.class);

        AccessUser user = requestContext.getUser();

        String cmd = requestContext.getParameterString("cmd");
        int rowLimit = requestContext.getParameter("rowLimit", ConfigManager.app.getBlogsNumPosts());
        Integer categoryId = requestContext.getParameter("categoryId");

        int rowStart = 0;
        if (!cmd.isEmpty()) {
            request.getSession().setAttribute(SessionManager.BLOG_POSTS_ROW_START, rowStart);
        } else {
            rowStart = SessionManager.getOrSetAttribute(requestContext, "rowStart", SessionManager.BLOG_POSTS_ROW_START, rowStart);
        }

        HttpSession session = request.getSession();

        // Getting search criteria map from session variable.
        BlogPostSearch blogPostSearch = new BlogPostSearch();
        if (!cmd.isEmpty()) {
            if (cmd.equals("search")) {
                // We are expecting user to enter some search criteria.
                blogPostSearch.prepareMap(actionForm);

            } else if (cmd.equals("showAll")) {
                // We're expecting to reset the search criteria.
            }

            // This is for so that we know what the user is searching for.
            //searchCriteriaMap.put("cmd", cmd);
            session.setAttribute(SessionManager.BLOG_POST_SEARCH_CRITERIA_MAP, blogPostSearch.getSearchCriteriaMap());

        } else if (session.getAttribute(SessionManager.BLOG_POST_SEARCH_CRITERIA_MAP) != null) {
            blogPostSearch.setSearchCriteriaMap((Map) session.getAttribute(SessionManager.BLOG_POST_SEARCH_CRITERIA_MAP));
        }

        // Pass variables to query.
        QueryBits query = new QueryBits(blogPostSearch);
        query.addSortColumn(BlogQueries.getOrderByColumn(BlogPost.CREATION_DATE), QueryBits.DESCENDING);
        query.setLimit(rowLimit, rowStart);

        boolean hasBlogDetailAccess = Access.hasPermission(user, AppPaths.BLOG_POST_DETAIL);
        List posts = new ArrayList();

        BlogService blogService = ServiceProvider.getBlogService(requestContext);

        int rowCount = blogService.getPostCount(query);

        if (rowCount != 0) {
            for (BlogPost post : blogService.getPosts(query)) {
                Map map = new HashMap();
                map.put("postTitle", post.getPostTitle());
                map.put("postBody", PortalUtils.truncateBodyTextOnList(post.getPostBody()));
                Object[] args = {AdminUtils.getSystemUsername(requestContext, post.getCreator()), post.getCreationDate()};
                map.put("postCreator", Localizer.getText(requestContext, "blogs.colName.creation_info", args));

                String postCategoryPath = new Link(requestContext).setAjaxPath(AppPaths.BLOG_POST_LIST + "?cmd=search&categoryId=" + post.getCategoryId()).setTitle(post.getCategoryName()).getString();
                map.put("postCategoryPath", Localizer.getText(requestContext, "blogs.colName.category_info", new Object[]{postCategoryPath}));

                int countComment = post.getPostCommentCount();
                Link link = new Link(requestContext);
                link.setTitle(Localizer.getText(requestContext, "blogs.postList.commentPath", new Object[]{countComment}));

                if (hasBlogDetailAccess) {
                    // Only turn comment into a link when it has more than 0 comment.
                    if (countComment != 0) {
                        link.setAppPath(AppPaths.BLOG_POST_DETAIL + "?postId=" + post.getId() + "#comment");
                    }
                }
                map.put("postPermalinkPath", AppPaths.ROOT + AppPaths.BLOG_POST_DETAIL + "?postId=" + post.getId());
                map.put("postCommentPath", link.getString());

                posts.add(map);
            }
        }

        //
        // Template: StandardTemplate
        //
        StandardTemplate standardTemplate = new StandardTemplate(requestContext);
        standardTemplate.setAttribute("postList", posts);

        //
        // Template: RecordsNavigationTemplate
        //
        RecordsNavigationTemplate nav = new RecordsNavigationTemplate();
        standardTemplate.addTemplate(nav);
        nav.setRowOffset(rowStart);
        nav.setRowLimit(rowLimit);
        nav.setRowCount(rowCount);
        nav.setFirstText(Localizer.getText(requestContext, "blogs.postList.recordsNav.first"));
        nav.setPath(AppPaths.BLOG_POST_LIST + "?rowStart=");

        // {0} posts - Showing {1} through {2}
        standardTemplate.setAttribute("rowCountText", Localizer.getText(requestContext, "blogs.postList.rowCount",
                new Object[]{rowCount, nav.getRowStart(), nav.getRowEnd()}));

        //
        // Template: HeaderTemplate
        //
        HeaderTemplate header = standardTemplate.getHeaderTemplate();
        header.setTitleClassNoLine();
        
        if (categoryId != 0) {
            // Showing category
            Category category = blogService.getCategory(categoryId);
            header.setTitleKey("blogs.postList.headerWithCategory", new Object[]{category.getName()});
        } else {
            header.setTitleKey("blogs.postList.header");
        }
        
        // Add blog post.
        if (Access.hasPermission(user, AppPaths.BLOG_POST_ADD)) {
            Link link = new Link(requestContext);
            link.setAjaxPath(AppPaths.BLOG_POST_ADD);
            link.setTitleKey("portal.cmd.blogPostAdd");
            header.addHeaderCmds(link);
        }
        // Blog post rss.
        if (Access.hasPermission(user, AppPaths.BLOG_POST_LIST_RSS)) {
            Link link = new Link(requestContext);
            link.setAppPath(AppPaths.BLOG_POST_LIST_RSS);
            link.setTitleKey("portal.cmd.rssFeedSubscribe");
            link.setImgSrc(Image.getInstance().getRssFeedIcon());
            header.addHeaderCmds(link);
        }

        return standardTemplate.findTemplate(STANDARD_TEMPLATE);
    }
}
