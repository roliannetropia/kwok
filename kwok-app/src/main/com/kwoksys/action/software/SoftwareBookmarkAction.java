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
package com.kwoksys.action.software;

import com.kwoksys.action.common.template.HeaderTemplate;
import com.kwoksys.action.common.template.StandardTemplate;
import com.kwoksys.action.common.template.TableEmptyTemplate;
import com.kwoksys.action.common.template.TabsTemplate;
import com.kwoksys.biz.ServiceProvider;
import com.kwoksys.biz.admin.dto.AccessUser;
import com.kwoksys.biz.auth.core.Access;
import com.kwoksys.biz.software.SoftwareService;
import com.kwoksys.biz.software.SoftwareUtils;
import com.kwoksys.biz.software.dto.Software;
import com.kwoksys.biz.system.core.AppPaths;
import com.kwoksys.biz.system.core.Image;
import com.kwoksys.biz.system.dao.BookmarkQueries;
import com.kwoksys.biz.system.dto.Bookmark;
import com.kwoksys.framework.connections.database.QueryBits;
import com.kwoksys.framework.properties.Localizer;
import com.kwoksys.framework.struts2.Action2;
import com.kwoksys.framework.ui.Link;
import com.kwoksys.framework.ui.RowStyle;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Action class for displaying software bookmark.
 */
public class SoftwareBookmarkAction extends Action2 {

    public String execute() throws Exception {
        SoftwareBookmarkForm actionForm = (SoftwareBookmarkForm) getBaseForm(SoftwareBookmarkForm.class);

        AccessUser accessUser = requestContext.getUser();

        SoftwareService softwareService = ServiceProvider.getSoftwareService(requestContext);
        Software software = softwareService.getSoftware(actionForm.getSoftwareId());

        String editBookmarkText = Localizer.getText(requestContext, "form.button.edit");

        int bookmarkRowSpan = 2;

        // These are for Software Bookmarks.
        boolean canDeleteBookmark = Access.hasPermission(accessUser, AppPaths.SOFTWARE_BOOKMARK_DELETE_2);
        if (canDeleteBookmark) {
            bookmarkRowSpan++;
        }

        boolean canEditBookmark = Access.hasPermission(accessUser, AppPaths.SOFTWARE_BOOKMARK_EDIT);
        String editBookmarkPath = canEditBookmark ? AppPaths.SOFTWARE_BOOKMARK_EDIT : "";
        if (canEditBookmark) {
            bookmarkRowSpan++;
        }
        QueryBits query = new QueryBits();
        query.addSortColumn(BookmarkQueries.getOrderByColumn(Bookmark.NAME));

        List<Bookmark> bookmarks = softwareService.getSoftwareBookmarks(query, software.getId());
        List bookmarkList = new ArrayList();

        //
        // Template: StandardTemplate
        //
        StandardTemplate standardTemplate = new StandardTemplate(requestContext);
        standardTemplate.setAttribute("bookmarks", bookmarkList);
        standardTemplate.setAttribute("canDeleteBookmark", canDeleteBookmark);
        standardTemplate.setAttribute("deleteBookmarkPath", AppPaths.SOFTWARE_BOOKMARK_DELETE_2);
        standardTemplate.setAttribute("canEditBookmark", canEditBookmark);
        standardTemplate.setAttribute("bookmarkRowSpan", bookmarkRowSpan);

        if (!bookmarks.isEmpty()) {
            RowStyle ui = new RowStyle();
            for (Bookmark bookmark : bookmarks) {
                Map map = new HashMap();
                map.put("bookmarkPath", new Link(requestContext).setExternalPath(bookmark.getPath())
                        .setTitle(bookmark.getName()).setImgSrc(Image.getInstance().getExternalPopupIcon()).setImgAlignRight());
                if (canEditBookmark) {
                    map.put("bookmarkEditPath", new Link(requestContext).setAjaxPath(editBookmarkPath + "?softwareId="
                            + software.getId() + "&bookmarkId=" + bookmark.getId()).setEscapeTitle(editBookmarkText));
                }
                map.put("bookmarkId", bookmark.getId());
                map.put("rowClass", ui.getRowClass());
                bookmarkList.add(map);
            }
        } else {
            //
            // Template: TableEmptyTemplate
            //
            TableEmptyTemplate empty = new TableEmptyTemplate();
            standardTemplate.addTemplate(empty);
            empty.setColSpan(bookmarkRowSpan);
            empty.setRowText(Localizer.getText(requestContext, "itMgmt.softwareBookmark.emptyTableMessage"));
        }

        //
        // Template: SoftwareSpecTemplate
        //
        SoftwareSpecTemplate tmpl = new SoftwareSpecTemplate(software);
        standardTemplate.addTemplate(tmpl);

        //
        // Template: HeaderTemplate
        //
        HeaderTemplate header = standardTemplate.getHeaderTemplate();
        SoftwareUtils.addSoftwareHeaderCommands(requestContext, header, software.getId());
        header.setPageTitleKey("itMgmt.softwareDetail.header", new Object[] {software.getName()});

        // Add Software Bookmark.
        if (Access.hasPermission(accessUser, AppPaths.SOFTWARE_BOOKMARK_ADD)) {
            Link link = new Link(requestContext);
            link.setAjaxPath(AppPaths.SOFTWARE_BOOKMARK_ADD + "?softwareId=" + software.getId());
            link.setTitleKey("bookmarkMgmt.bookmarkAdd");
            header.addHeaderCmds(link);
        }

        //
        // Template: TabsTemplate
        //
        TabsTemplate tabs = new TabsTemplate();
        standardTemplate.addTemplate(tabs);
        tabs.setTabList(SoftwareUtils.softwareTabList(requestContext, software));
        tabs.setTabActive(SoftwareUtils.BOOKMARKS_TAB);

        return standardTemplate.findTemplate(STANDARD_TEMPLATE);
    }
}
