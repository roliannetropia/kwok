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
package com.kwoksys.biz.blogs.dao;

import com.kwoksys.biz.admin.dto.AccessUser;
import com.kwoksys.biz.base.BaseDao;
import com.kwoksys.biz.blogs.dto.BlogPost;
import com.kwoksys.biz.blogs.dto.BlogPostComment;
import com.kwoksys.framework.connections.database.QueryBits;
import com.kwoksys.framework.connections.database.QueryHelper;
import com.kwoksys.framework.exceptions.DatabaseException;
import com.kwoksys.framework.exceptions.ObjectNotFoundException;
import com.kwoksys.framework.http.RequestContext;
import com.kwoksys.framework.util.DatetimeUtils;
import com.kwoksys.framework.util.StringUtils;
import org.apache.struts.action.ActionMessages;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.Types;
import java.util.ArrayList;
import java.util.List;

/**
 * BlogDao.
 */
public class BlogDao extends BaseDao {

    public BlogDao(RequestContext requestContext) {
        super(requestContext);
    }

    /**
     * This is to return a list of Portal Blog.
     *
     * @param query
     * @return ..
     */
    public List<BlogPost> getPostList(QueryBits query) throws DatabaseException {
        Connection conn = getConnection();

        QueryHelper queryHelper = new QueryHelper(BlogQueries.selectBlogPostListQuery(query));

        try {
            List posts = new ArrayList();
            ResultSet rs = queryHelper.executeQuery(conn);

            while (rs.next()) {
                BlogPost post = new BlogPost();
                post.setId(rs.getInt("post_id"));
                post.setPostTitle(StringUtils.replaceNull(rs.getString("post_name")));
                post.setPostBody(StringUtils.replaceNull(rs.getString("post_description")));
                post.setPostType(rs.getInt("post_type"));
                post.setPostIp(StringUtils.replaceNull(rs.getString("post_ip")));
                post.setPostCommentCount(rs.getInt("comment_count"));
                post.setCategoryId(rs.getInt("category_id"));
                post.setCategoryName(StringUtils.replaceNull(rs.getString("category_name")));
                post.setCreationDate(DatetimeUtils.getDate(rs, "creation_date"));
                post.setModificationDate(DatetimeUtils.getDate(rs, "modification_date"));

                post.setCreator(new AccessUser());
                post.getCreator().setId(rs.getInt("creator"));
                post.getCreator().setUsername(rs.getString("creator_username"));
                post.getCreator().setDisplayName(rs.getString("creator_display_name"));

                post.setModifier(new AccessUser());
                post.getModifier().setId(rs.getInt("modifier"));
                post.getModifier().setUsername(rs.getString("modifier_username"));
                post.getModifier().setDisplayName(rs.getString("modifier_display_name"));

                posts.add(post);
            }
            return posts;

        } catch (Exception e) {
            // Database problem
            throw new DatabaseException(e, queryHelper);

        } finally {
            queryHelper.closeRs();
            closeConnection(conn);
        }
    }

    /**
     * This is to return Portal Blog count.
     *
     * @param query
     * @return ..
     */
    public int getPostCount(QueryBits query) throws DatabaseException {
        return getRowCount(BlogQueries.getBlogPostCountQuery(query));
    }

    /**
     * This is for getting Portal Blog Post detail.
     *
     * @param postId
     * @return ..
     */
    public BlogPost getPost(Integer postId) throws DatabaseException, ObjectNotFoundException {
        Connection conn = getConnection();

        QueryHelper queryHelper = new QueryHelper(BlogQueries.selectBlogPostDetailQuery());
        queryHelper.addInputInt(postId);

        try {
            ResultSet rs = queryHelper.executeQuery(conn);
            if (rs.next()) {
                BlogPost post = new BlogPost();
                post.setId(rs.getInt("post_id"));
                post.setPostTitle(StringUtils.replaceNull(rs.getString("post_name")));
                post.setPostBody(StringUtils.replaceNull(rs.getString("post_description")));
                post.setPostType(rs.getInt("post_type"));
                post.setPostIp(StringUtils.replaceNull(rs.getString("post_ip")));
                post.setPostAllowComment(rs.getInt("post_allow_comment"));
                post.setCategoryId(rs.getInt("category_id"));
                post.setCategoryName(StringUtils.replaceNull(rs.getString("category_name")));
                post.setCreationDate(DatetimeUtils.getDate(rs, "creation_date"));
                post.setModificationDate(DatetimeUtils.getDate(rs, "modification_date"));
                post.setPostCommentCount(rs.getInt("comment_count"));

                post.setCreator(new AccessUser());
                post.getCreator().setId(rs.getInt("creator"));
                post.getCreator().setUsername(rs.getString("creator_username"));
                post.getCreator().setDisplayName(rs.getString("creator_display_name"));

                post.setModifier(new AccessUser());
                post.getModifier().setId(rs.getInt("modifier"));
                post.getModifier().setUsername(rs.getString("modifier_username"));
                post.getModifier().setDisplayName(rs.getString("modifier_display_name"));

                return post;
            }
        } catch (Exception e) {
            // Database problem
            throw new DatabaseException(e, queryHelper);

        } finally {
            queryHelper.closeRs();
            closeConnection(conn);
        }
        throw new ObjectNotFoundException();
    }

    /**
     * This is to return a list of Portal Blog Post Comment.
     *
     * @param query
     * @return .. ..
     */
    public List<BlogPostComment> getPostCommentList(QueryBits query, Integer postId) throws DatabaseException {
        Connection conn = getConnection();

        QueryHelper queryHelper = new QueryHelper(BlogQueries.selectBlogPostCommentListQuery(query));
        queryHelper.addInputInt(postId);

        try {
            List comments = new ArrayList();
            ResultSet rs = queryHelper.executeQuery(conn);

            while (rs.next()) {
                BlogPostComment comment = new BlogPostComment();
                comment.setCommentId(rs.getInt("comment_id"));
                comment.setCommentDescription(StringUtils.replaceNull(rs.getString("comment_description")));
                comment.setCommentIp(StringUtils.replaceNull(rs.getString("comment_ip")));
                comment.setCreationDate(DatetimeUtils.getDate(rs, "creation_date"));

                comment.setCreator(new AccessUser());
                comment.getCreator().setId(rs.getInt("creator"));
                comment.getCreator().setUsername(rs.getString("creator_username"));
                comment.getCreator().setDisplayName(rs.getString("creator_display_name"));

                comments.add(comment);
            }
            return comments;

        } catch (Exception e) {
            // Database problem
            throw new DatabaseException(e, queryHelper);

        } finally {
            queryHelper.closeRs();
            closeConnection(conn);
        }
    }

    /**
     * Add Blog Post.
     *
     * @return ..
     */
    public ActionMessages addPost(BlogPost post) throws DatabaseException {
        QueryHelper queryHelper = new QueryHelper(BlogQueries.insertBlogPostQuery());
        queryHelper.addOutputParam(Types.INTEGER);
        queryHelper.addInputStringConvertNull(post.getPostTitle());
        queryHelper.addInputStringConvertNull(post.getPostBody());
        queryHelper.addInputInt(post.getPostType());
        queryHelper.addInputStringConvertNull(post.getPostIp());
        queryHelper.addInputInt(post.getPostAllowComment());
        queryHelper.addInputInt(post.getCategoryId());
        queryHelper.addInputInt(requestContext.getUser().getId());

        executeProcedure(queryHelper);

        // Put some values in the result.
        if (errors.isEmpty()) {
            post.setId((Integer)queryHelper.getSqlOutputs().get(0));
        }

        return errors;
    }

    public ActionMessages editPost(BlogPost post) throws DatabaseException {
        QueryHelper queryHelper = new QueryHelper(BlogQueries.updateBlogPostQuery());
        queryHelper.addInputInt(post.getId());
        queryHelper.addInputStringConvertNull(post.getPostTitle());
        queryHelper.addInputStringConvertNull(post.getPostBody());
        queryHelper.addInputInt(post.getPostType());
        queryHelper.addInputInt(post.getPostAllowComment());
        queryHelper.addInputInt(post.getCategoryId());
        queryHelper.addInputInt(requestContext.getUser().getId());

        return executeProcedure(queryHelper);
    }

    /**
     * Deletes a post
     * @param post
     * @return
     * @throws DatabaseException
     */
    public ActionMessages deletePost(BlogPost post) throws DatabaseException {
        QueryHelper queryHelper = new QueryHelper(BlogQueries.deleteBlogPostQuery());
        queryHelper.addInputInt(post.getId());
        queryHelper.addInputInt(post.getCategoryId());

        return executeProcedure(queryHelper);
    }

    /**
     * Add a new Blog Post Comment.
     *
     * @return ..
     */
    public ActionMessages addPostComment(BlogPostComment comment) throws DatabaseException {
        QueryHelper queryHelper = new QueryHelper(BlogQueries.insertBlogPostCommentQuery());
        queryHelper.addOutputParam(Types.INTEGER);
        queryHelper.addInputStringConvertNull(comment.getCommentDescription());
        queryHelper.addInputStringConvertNull(comment.getCommentIp());
        queryHelper.addInputInt(comment.getPostId());
        queryHelper.addInputInt(requestContext.getUser().getId());

        executeProcedure(queryHelper);

        // Put some values in the result
        if (errors.isEmpty()) {
            comment.setCommentId((Integer)queryHelper.getSqlOutputs().get(0));
        }

        return errors;
    }
}
