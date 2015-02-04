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
package com.kwoksys.action.admin.manageusers;

import com.kwoksys.action.common.template.HeaderTemplate;
import com.kwoksys.action.common.template.RecordsNavigationTemplate;
import com.kwoksys.action.common.template.StandardTemplate;
import com.kwoksys.action.common.template.TableTemplate;
import com.kwoksys.biz.ServiceProvider;
import com.kwoksys.biz.admin.AdminService;
import com.kwoksys.biz.admin.core.AdminUtils;
import com.kwoksys.biz.admin.core.UserSearch;
import com.kwoksys.biz.admin.dao.AdminQueries;
import com.kwoksys.biz.admin.dto.AccessUser;
import com.kwoksys.biz.auth.core.Access;
import com.kwoksys.biz.system.core.*;
import com.kwoksys.biz.system.core.configs.ConfigManager;
import com.kwoksys.framework.connections.database.QueryBits;
import com.kwoksys.framework.properties.Localizer;
import com.kwoksys.framework.session.SessionManager;
import com.kwoksys.framework.struts2.Action2;
import com.kwoksys.framework.ui.Link;
import com.kwoksys.framework.ui.RowStyle;
import com.kwoksys.framework.util.Counter;
import com.kwoksys.framework.util.HtmlUtils;
import org.apache.struts.util.LabelValueBean;

import javax.servlet.http.HttpSession;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Action class for showing user list.
 */
public class UserListAction extends Action2 {

    public String execute() throws Exception {
        UserSearchForm actionForm = (UserSearchForm) getSessionBaseForm(UserSearchForm.class);
        AccessUser user = requestContext.getUser();

        String cmd = requestContext.getParameterString("cmd");
        String rowCmd = requestContext.getParameterString("rowCmd");
        String orderBy = SessionManager.getOrSetAttribute(requestContext, "orderBy", SessionManager.USERS_ORDER_BY, AccessUser.USERNAME);
        String order = SessionManager.getOrSetAttribute(requestContext, "order", SessionManager.USERS_ORDER, QueryBits.ASCENDING);

        int rowStart = 0;
        if (!cmd.isEmpty() || rowCmd.equals("showAll")) {
            request.getSession().setAttribute(SessionManager.USERS_ROW_START, rowStart);
        } else {
            rowStart = SessionManager.getOrSetAttribute(requestContext, "rowStart", SessionManager.USERS_ROW_START, rowStart);
        }

        int rowLimit = requestContext.getParameter("rowLimit", ConfigManager.app.getUserRows());
        if (rowCmd.equals("showAll")) {
            rowLimit = 0;
        }

        // Get session variables
        HttpSession session = request.getSession();

        // Getting search criteria map from session variable.
        UserSearch userSearch = new UserSearch();

        if (!cmd.isEmpty()) {
            if (cmd.equals("search")) {
                // We are expecting user to enter some search criteria.
                userSearch.prepareMap(actionForm, requestContext);

            } else if (cmd.equals("showEnabled")) {
                userSearch.put(UserSearch.USER_STATUS, AttributeFieldIds.USER_STATUS_ENABLED);

            } else if (cmd.equals("showDisabled")) {
                userSearch.put(UserSearch.USER_STATUS, AttributeFieldIds.USER_STATUS_DISABLED);

            } else if (cmd.equals("showLoggedIn")) {
                userSearch.put("loggedInUsers", "");
            }

            // Store search criteria in session.
            userSearch.put("cmd", cmd);
            session.setAttribute(SessionManager.USER_SEARCH_CRITERIA_MAP, userSearch.getSearchCriteriaMap());

        } else if (session.getAttribute(SessionManager.USER_SEARCH_CRITERIA_MAP) != null) {
            userSearch.setSearchCriteriaMap((Map) session.getAttribute(SessionManager.USER_SEARCH_CRITERIA_MAP));
        }

        // Pass variables to query.
        QueryBits query = new QueryBits(userSearch);
        query.setLimit(rowLimit, rowStart);

        if (AdminUtils.isSortableUserColumn(orderBy)) {
            query.addSortColumn(AdminQueries.getOrderByColumn(orderBy), order);
        }

        // Get column headers
        List<String> columnHeaders = AdminUtils.getUserColumnHeaders();

        // Call the service
        AdminService adminService = ServiceProvider.getAdminService(requestContext);

        // Get a list of users
        List<AccessUser> userDataset = adminService.getUsers(query);
        List dataList = new ArrayList();

        AttributeManager attributeManager = new AttributeManager(requestContext);

        if (!userDataset.isEmpty()) {
            RowStyle ui = new RowStyle();
            Counter counter = new Counter(rowStart);

            for (AccessUser appUser : userDataset) {
                List columns = new ArrayList();

                for (String column : columnHeaders) {
                    if (column.equals(AccessUser.ROWNUM)) {
                        columns.add(counter.incrCounter() + ".");

                    } else if (column.equals(AccessUser.USERNAME)) {
                        columns.add(new Link(requestContext).setAjaxPath(AppPaths.ADMIN_USER_DETAIL
                                + "?userId=" + appUser.getId()).setTitle(appUser.getUsername()));

                    } else if (column.equals(AccessUser.STATUS)) {
                        columns.add(attributeManager.getAttrFieldNameCache(Attributes.USER_STATUS_TYPE, appUser.getStatus()));

                    } else if (column.equals(AccessUser.FIRST_NAME)) {
                        columns.add(HtmlUtils.encode(appUser.getFirstName()));

                    } else if (column.equals(AccessUser.LAST_NAME)) {
                        columns.add(HtmlUtils.encode(appUser.getLastName()));

                    } else if (column.equals(AccessUser.DISPLAY_NAME)) {
                        columns.add(HtmlUtils.encode(appUser.getDisplayName()));

                    } else if (column.equals(AccessUser.EMAIL)) {
                        columns.add(HtmlUtils.encode(appUser.getEmail()));
                    }
                }
                Map map = new HashMap();
                map.put("rowClass", ui.getRowClass());
                map.put("columns", columns);
                dataList.add(map);
            }
        }

        // Selectbox for filtering users.
        List filterOptions = new ArrayList();
        filterOptions.add(new LabelValueBean(Localizer.getText(requestContext, "admin.userList.filter.showAll"), "showAll"));
        filterOptions.add(new LabelValueBean(Localizer.getText(requestContext, "admin.userList.filter.showEnabled"), "showEnabled"));
        filterOptions.add(new LabelValueBean(Localizer.getText(requestContext, "admin.userList.filter.showDisabled"), "showDisabled"));
        filterOptions.add(new LabelValueBean(Localizer.getText(requestContext, "admin.userList.filter.showLoggedIn"), "showLoggedIn"));

        //
        // Template: StandardTemplate
        //
        StandardTemplate standardTemplate = new StandardTemplate(requestContext);
        standardTemplate.setPathAttribute("formAction", AppPaths.ADMIN_USER_LIST);
        standardTemplate.setAttribute("filterOptions", filterOptions);

        //
        // Template: HeaderTemplate
        //
        HeaderTemplate header = standardTemplate.getHeaderTemplate();
        header.setTitleKey("admin.userList.title");
        header.setTitleClassNoLine();

        // Add User
        if (Access.hasPermission(user, AppPaths.ADMIN_USER_ADD)) {
            Link link = new Link(requestContext);
            link.setAjaxPath(AppPaths.ADMIN_USER_ADD);
            link.setTitleKey("admin.userAdd.title");
            link.setImgSrc(Image.getInstance().getUserAddIcon());
            header.addHeaderCmds(link);
        }

        // Link to User export
        if (Access.hasPermission(user, AppPaths.ADMIN_USER_LIST_EXPORT)) {
            Link link = new Link(requestContext);
            link.setExportPath(AppPaths.ADMIN_USER_LIST_EXPORT + "?rowCmd=" + rowCmd +
                    "&rowStart=" + rowStart + "&rowLimit" + rowLimit);
            link.setTitleKey("admin.cmd.userListExport");
            link.setImgSrc(Image.getInstance().getCsvFileIcon());
            header.addHeaderCmds(link);
        }

        // Back to admin home
        header.addNavLink(Links.getAdminHomeLink(requestContext));

        if (Access.hasPermission(user, AppPaths.ADMIN_USER_INDEX)) {
            Link link = new Link(requestContext);
            link.setAjaxPath(AppPaths.ADMIN_USER_INDEX);
            link.setTitleKey("admin.userIndex.userSearchTitle");
            header.addNavLink(link);
        }

        //
        // Template: TableTemplate
        //
        TableTemplate tableTemplate = new TableTemplate();
        standardTemplate.addTemplate(tableTemplate);
        tableTemplate.setColumnHeaders(columnHeaders);
        tableTemplate.setSortableColumnHeaders(AdminUtils.getSortableUserColumns());
        tableTemplate.setColumnPath(AppPaths.ADMIN_USER_LIST);
        tableTemplate.setColumnTextKey("common.column.");
        tableTemplate.setRowCmd(rowCmd);
        tableTemplate.setOrderBy(orderBy);
        tableTemplate.setOrder(order);
        tableTemplate.setDataList(dataList);
        tableTemplate.setEmptyRowMsgKey("admin.userList.emptyTableMessage");

        //
        // Template: RecordsNavigationTemplate
        //
        int rowCount = adminService.getUserCount(query);
        Object[] args = {rowCount};

        RecordsNavigationTemplate nav = new RecordsNavigationTemplate();
        standardTemplate.addTemplate(nav);
        nav.setRowOffset(rowStart);
        nav.setRowLimit(rowLimit);
        nav.setRowCount(rowCount);
        nav.setRowCountMsgkey("core.template.recordsNav.rownum");
        nav.setShowAllRecordsText(Localizer.getText(requestContext, "admin.userList.rowCount", args));
        nav.setShowAllRecordsPath(AppPaths.ADMIN_USER_LIST + "?rowCmd=showAll");
        nav.setPath(AppPaths.ADMIN_USER_LIST + "?rowStart=");

        return standardTemplate.findTemplate(STANDARD_TEMPLATE);
    }
}