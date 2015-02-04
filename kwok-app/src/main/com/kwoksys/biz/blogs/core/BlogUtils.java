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
package com.kwoksys.biz.blogs.core;

import com.kwoksys.biz.system.dto.Category;
import com.kwoksys.biz.base.BaseObject;
import com.kwoksys.biz.blogs.dto.BlogPost;
import com.kwoksys.framework.properties.Localizer;
import com.kwoksys.framework.http.RequestContext;

import java.util.Arrays;
import java.util.List;
import java.util.ArrayList;

import org.apache.struts.util.LabelValueBean;

/**
 * BlogUtil.
 */
public class BlogUtils {

    /**
     * Specifies the column header for the list page.
     */
    public static List getCategoryColumnHeaderList() {
        return Arrays.asList(BaseObject.ROWNUM, Category.CATEGORY_NAME,
                Category.CATEGORY_DESCRIPTION, BlogPost.POST_OBJECT_COUNT,
                Category.CATEGORY_ACTIONS);
    }

    /**
     * Specifies the sortable columns allowed.
     */
    public static List getCategorySortableColumnList() {
        return Arrays.asList(Category.CATEGORY_NAME);
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

    public static List getCommentOptions(RequestContext requestContext) {
        List commentOptions = new ArrayList();
        commentOptions.add(new LabelValueBean(Localizer.getText(requestContext,
                "blogs.colData.post_allow_comment.1"),
                String.valueOf(BlogPost.POST_ALLOW_COMMENT_YES)));
        commentOptions.add(new LabelValueBean(Localizer.getText(requestContext,
                "blogs.colData.post_allow_comment.0"),
                String.valueOf(BlogPost.POST_ALLOW_COMMENT_NO)));
        return commentOptions;
    }
}
