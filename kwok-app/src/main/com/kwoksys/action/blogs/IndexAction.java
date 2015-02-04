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
import com.kwoksys.action.common.template.StandardTemplate;
import com.kwoksys.biz.ServiceProvider;
import com.kwoksys.biz.admin.dto.AccessUser;
import com.kwoksys.biz.auth.core.Access;
import com.kwoksys.biz.blogs.BlogService;
import com.kwoksys.biz.blogs.dao.BlogQueries;
import com.kwoksys.biz.blogs.dto.BlogPost;
import com.kwoksys.biz.system.core.AppPaths;
import com.kwoksys.biz.system.dto.Category;
import com.kwoksys.framework.connections.database.QueryBits;
import com.kwoksys.framework.properties.Localizer;
import com.kwoksys.framework.struts2.Action2;
import com.kwoksys.framework.ui.Link;
import com.kwoksys.framework.ui.SelectOneLabelValueBean;
import org.apache.struts.util.LabelValueBean;

import java.util.*;

/**
 * Action class for portal index page.
 */
public class IndexAction extends Action2 {

    public String execute() throws Exception {
        getSessionBaseForm(PostSearchForm.class);

        AccessUser user = requestContext.getUser();

        // Allow comment options.
        List commentOptions = Arrays.asList(
                new SelectOneLabelValueBean(requestContext, String.valueOf(BlogPost.POST_ALLOW_COMMENT_SELECT_ONE)),
                new LabelValueBean(Localizer.getText(requestContext, "common.boolean.yes_no.yes"),
                        String.valueOf(BlogPost.POST_ALLOW_COMMENT_YES)),
                new LabelValueBean(Localizer.getText(requestContext, "common.boolean.yes_no.no"),
                        String.valueOf(BlogPost.POST_ALLOW_COMMENT_NO)));

        // Show categories.
        QueryBits query = new QueryBits();
        query.addSortColumn(BlogQueries.getOrderByColumn(Category.CATEGORY_NAME));

        List postCategoryIdList = new ArrayList();
        List postCategoryIdLabel = new ArrayList();
        postCategoryIdLabel.add(new SelectOneLabelValueBean(requestContext));

        BlogService blogService = ServiceProvider.getBlogService(requestContext);

        for (Category category : blogService.getCategories(query)) {
            Map map = new HashMap();
            map.put("postListParam", "?cmd=search&categoryId=" + category.getId());
            map.put("categoryName", category.getName());
            map.put("categoryId", category.getId());
            map.put("postCount", category.getCountObjects());
            postCategoryIdList.add(map);

            postCategoryIdLabel.add(new LabelValueBean(category.getName(), String.valueOf(category.getId())));
        }

        List linkList = new ArrayList();

        // Link to hardware list.
        if (Access.hasPermission(user, AppPaths.BLOG_POST_LIST)) {
            Map linkMap = new HashMap();
            linkMap.put("urlPath", AppPaths.BLOG_POST_LIST + "?cmd=showAll");
            linkMap.put("urlText", Localizer.getText(requestContext, "portal.index.showAllPosts"));
            linkList.add(linkMap);
        }

        //
        // Template: StandardTemplate
        //
        StandardTemplate standardTemplate = new StandardTemplate(requestContext);
        standardTemplate.setAttribute("postAllowCommentOptions", commentOptions);
        standardTemplate.setAttribute("postListPath", AppPaths.BLOG_POST_LIST);
        standardTemplate.setAttribute("postCategoryIdList", postCategoryIdList);
        standardTemplate.setAttribute("postCategoryIdLabel", postCategoryIdLabel);
        standardTemplate.setAttribute("linkList", linkList);

        //
        // Template: HeaderTemplate
        //
        HeaderTemplate header = standardTemplate.getHeaderTemplate();
        header.setTitleKey("core.moduleName.6");
        header.setTitleClassNoLine();
        
        // Manage categories
        if (Access.hasPermission(user, AppPaths.BLOG_CATEGORY_LIST)) {
            Link link = new Link(requestContext);
            link.setAjaxPath(AppPaths.BLOG_CATEGORY_LIST);
            link.setTitleKey("blogs.categoryList.header");
            header.addHeaderCmds(link);
        }

        // Add blog post
        if (Access.hasPermission(user, AppPaths.BLOG_POST_ADD)) {
            Link link = new Link(requestContext);
            link.setAjaxPath(AppPaths.BLOG_POST_ADD);
            link.setTitleKey("portal.cmd.blogPostAdd");
            header.addHeaderCmds(link);
        }

        return standardTemplate.findTemplate(STANDARD_TEMPLATE);
    }
}
