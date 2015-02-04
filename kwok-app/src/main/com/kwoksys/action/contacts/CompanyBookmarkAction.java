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
package com.kwoksys.action.contacts;

import com.kwoksys.action.common.template.HeaderTemplate;
import com.kwoksys.action.common.template.StandardTemplate;
import com.kwoksys.action.common.template.TableEmptyTemplate;
import com.kwoksys.action.common.template.TabsTemplate;
import com.kwoksys.biz.ServiceProvider;
import com.kwoksys.biz.admin.dto.AccessUser;
import com.kwoksys.biz.auth.core.Access;
import com.kwoksys.biz.contacts.ContactService;
import com.kwoksys.biz.contacts.core.CompanyTabs;
import com.kwoksys.biz.contacts.core.CompanyUtils;
import com.kwoksys.biz.contacts.dto.Company;
import com.kwoksys.biz.system.core.AppPaths;
import com.kwoksys.biz.system.core.Image;
import com.kwoksys.framework.properties.Localizer;
import com.kwoksys.biz.system.dao.BookmarkQueries;
import com.kwoksys.biz.system.dto.Bookmark;
import com.kwoksys.framework.connections.database.QueryBits;
import com.kwoksys.framework.struts2.Action2;
import com.kwoksys.framework.ui.Link;
import com.kwoksys.framework.ui.RowStyle;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Action class for showing company bookmark.
 */
public class CompanyBookmarkAction extends Action2 {

    public String execute() throws Exception {
        AccessUser user = requestContext.getUser();

        RowStyle ui = new RowStyle();

        // Bookmarks
        int bookmarkRowSpan = 2;

        CompanyBookmarkForm actionForm = (CompanyBookmarkForm) getBaseForm(CompanyBookmarkForm.class);

        // Call the service
        ContactService contactService = ServiceProvider.getContactService(requestContext);

        Company company = contactService.getCompany(actionForm.getCompanyId());

        // These are for Company Bookmarks.
        boolean canDeleteBookmark = Access.hasPermission(user, AppPaths.CONTACTS_COMPANY_BOOKMARK_DELETE_2);
        String deleteBookmarkPath = AppPaths.CONTACTS_COMPANY_BOOKMARK_DELETE_2;
        if (canDeleteBookmark) {
            bookmarkRowSpan++;
        }

        boolean canEditBookmark = Access.hasPermission(user, AppPaths.CONTACTS_COMPANY_BOOKMARK_EDIT);
        String editBookmarkPath = AppPaths.CONTACTS_COMPANY_BOOKMARK_EDIT;
        if (canEditBookmark) {
            bookmarkRowSpan++;
        }

        QueryBits query = new QueryBits();
        query.addSortColumn(BookmarkQueries.getOrderByColumn(Bookmark.NAME));

        List<Bookmark> bookmarks = contactService.getCompanyBookmarks(query, company.getId());
        List bookmarkList = new ArrayList();

        if (!bookmarks.isEmpty()) {
            for (Bookmark bookmark : bookmarks) {
                Map map = new HashMap();
                map.put("bookmarkPath", new Link(requestContext).setExternalPath(bookmark.getPath()).setTitle(bookmark.getName()).setImgSrc(Image.getInstance().getExternalPopupIcon()));
                if (canEditBookmark) {
                    map.put("bookmarkEditPath", new Link(requestContext).setAjaxPath(editBookmarkPath + "?companyId=" + company.getId() + "&bookmarkId=" + bookmark.getId()).setTitleKey("form.button.edit"));
                }
                map.put("bookmarkId", bookmark.getId());
                map.put("rowClass", ui.getRowClass());
                bookmarkList.add(map);
            }
        }

        //
        // Template: StandardTemplate
        //
        StandardTemplate standardTemplate = new StandardTemplate(requestContext);
        standardTemplate.setAttribute("companyId", company.getId());
        standardTemplate.setAttribute("canDeleteBookmark", canDeleteBookmark);
        standardTemplate.setAttribute("deleteBookmarkPath", deleteBookmarkPath);
        standardTemplate.setAttribute("canEditBookmark", canEditBookmark);
        standardTemplate.setAttribute("bookmarkRowSpan", bookmarkRowSpan);
        standardTemplate.setAttribute("bookmarkList", bookmarkList);

        if (bookmarks.isEmpty()) {
            //
            // Template: TableEmptyTemplate
            //
            TableEmptyTemplate empty = new TableEmptyTemplate();
            standardTemplate.addTemplate(empty);
            empty.setColSpan(bookmarkRowSpan);
            empty.setRowText(Localizer.getText(requestContext, "contactMgmt.companyBookmark_emptyTableMessage"));
        }

        //
        // Template: CompanySpecTemplate
        //
        standardTemplate.addTemplate(new CompanySpecTemplate(company));

        //
        // Template: HeaderTemplate
        //
        HeaderTemplate header = standardTemplate.getHeaderTemplate();
        header.setPageTitleKey("contactMgmt.companyDetail.header", new Object[] {company.getName()});

        // Link to add company bookmarks page
        if (Access.hasPermission(user, AppPaths.CONTACTS_COMPANY_BOOKMARK_ADD)) {
            Link link = new Link(requestContext);
            link.setAjaxPath(AppPaths.CONTACTS_COMPANY_BOOKMARK_ADD + "?companyId=" + company.getId());
            link.setTitleKey("bookmarkMgmt.bookmarkAdd");
            header.addHeaderCmds(link);
        }

        // Link to company list page
        if (Access.hasPermission(user, AppPaths.CONTACTS_COMPANY_LIST)) {
            Link link = new Link(requestContext);
            link.setAjaxPath(AppPaths.CONTACTS_COMPANY_LIST);
            link.setTitleKey("contactMgmt.cmd.companyList");
            header.addHeaderCmds(link);
        }

        //
        //  Template: TabsTemplate
        //
        TabsTemplate tabs = new TabsTemplate();
        standardTemplate.addTemplate(tabs);
        tabs.setTabList(CompanyUtils.companyTabList(requestContext, company));
        tabs.setTabActive(CompanyTabs.BOOKMAKRS_TAB);

        return standardTemplate.findTemplate(STANDARD_TEMPLATE);
    }
}
