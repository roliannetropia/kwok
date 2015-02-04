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

import com.kwoksys.biz.ServiceProvider;
import com.kwoksys.biz.auth.core.BlogPostAccess;
import com.kwoksys.biz.admin.dto.AccessUser;
import com.kwoksys.biz.blogs.BlogService;
import com.kwoksys.biz.blogs.dto.BlogPost;
import com.kwoksys.biz.system.core.AppPaths;
import com.kwoksys.framework.http.RequestContext;
import com.kwoksys.framework.exceptions.AccessDeniedException;
import com.kwoksys.framework.struts2.Action2;
import org.apache.struts.action.ActionMessages;

/**
 * Action class for editing post.
 */
public class PostEdit2Action extends Action2 {

    public String execute() throws Exception {
        AccessUser user = requestContext.getUser();

        PostForm actionForm = saveActionForm(new PostForm());

        BlogService blogService = ServiceProvider.getBlogService(requestContext);
        BlogPost post = blogService.getPost(actionForm.getPostId());

        post.setId(actionForm.getPostId());
        post.setPostTitle(actionForm.getPostTitle());
        post.setPostBody(actionForm.getPostBody());
        post.setPostAllowComment(actionForm.getPostAllowComment());
        post.setCategoryId(actionForm.getPostCategoryId());

        // Advance access control
        if (!BlogPostAccess.hasEditPermission(user, post)) {
            throw new AccessDeniedException();
        }

        ActionMessages errors = blogService.editPost(post);
        if (!errors.isEmpty()) {
            saveActionErrors(errors);
            return redirect(AppPaths.BLOG_POST_EDIT + "?postId=" + post.getId() + "&" + RequestContext.URL_PARAM_ERROR_TRUE);
        } else {
            return redirect(AppPaths.BLOG_POST_DETAIL + "?postId=" + post.getId());
        }
    }
}