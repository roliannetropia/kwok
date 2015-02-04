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

import com.kwoksys.action.common.template.ActionErrorsTemplate;
import com.kwoksys.action.common.template.HeaderTemplate;
import com.kwoksys.action.common.template.StandardTemplate;
import com.kwoksys.biz.ServiceProvider;
import com.kwoksys.biz.admin.dto.AccessUser;
import com.kwoksys.biz.auth.core.BlogPostAccess;
import com.kwoksys.biz.blogs.BlogService;
import com.kwoksys.biz.blogs.core.BlogUtils;
import com.kwoksys.biz.blogs.dao.BlogQueries;
import com.kwoksys.biz.blogs.dto.BlogPost;
import com.kwoksys.biz.system.core.AppPaths;
import com.kwoksys.biz.system.core.Links;
import com.kwoksys.biz.system.dto.Category;
import com.kwoksys.framework.connections.database.QueryBits;
import com.kwoksys.framework.exceptions.AccessDeniedException;
import com.kwoksys.framework.properties.Localizer;
import com.kwoksys.framework.struts2.Action2;
import org.apache.struts.util.LabelValueBean;

import java.util.ArrayList;
import java.util.List;

/**
 * Action class for editing blog post.
 */
public class PostEditAction extends Action2 {

    public String execute() throws Exception {
        AccessUser user = requestContext.getUser();

        PostForm actionForm = (PostForm) getBaseForm(PostForm.class);

        BlogService blogService = ServiceProvider.getBlogService(requestContext);
        BlogPost post = blogService.getPost(actionForm.getPostId());

        // Advance access control
        if (!BlogPostAccess.hasEditPermission(user, post)) {
            throw new AccessDeniedException();
        }

        // If not a resubmit, set some defaults
        if (!actionForm.isResubmit()) {
            actionForm.setPost(post);
        }

        QueryBits query = new QueryBits();
        query.addSortColumn(BlogQueries.getOrderByColumn(Category.CATEGORY_NAME));

        List categoryIdOptions = new ArrayList();

        for (Category category : blogService.getCategories(query)) {
            categoryIdOptions.add(new LabelValueBean(category.getName(),
                    String.valueOf(category.getId())));
        }

        //
        // Template: StandardTemplate
        //
        StandardTemplate standardTemplate = new StandardTemplate(requestContext);
        standardTemplate.setPathAttribute("formAction", AppPaths.BLOG_POST_EDIT_2);
        standardTemplate.setAttribute("formCancelLink", Links.getCancelLink(requestContext, AppPaths.BLOG_POST_DETAIL + "?postId=" + post.getId()).getString());
        request.setAttribute("postAllowCommentOptions", BlogUtils.getCommentOptions(requestContext));
        request.setAttribute("postCategoryIdOptions", categoryIdOptions);

        //
        // Template: HeaderTemplate
        //
        HeaderTemplate header = standardTemplate.getHeaderTemplate();
        header.setTitleKey("blogs.postEdit.header");

        //
        // Template: ActionErrorsTemplate
        //
        ActionErrorsTemplate errorsTemplate = new ActionErrorsTemplate();
        standardTemplate.addTemplate(errorsTemplate);
        errorsTemplate.setShowRequiredFieldMsg(true);
        errorsTemplate.setMessage(Localizer.getText(requestContext, "blogs.postAdd.sectionHeader"));

        return standardTemplate.findTemplate(STANDARD_TEMPLATE);
    }
}