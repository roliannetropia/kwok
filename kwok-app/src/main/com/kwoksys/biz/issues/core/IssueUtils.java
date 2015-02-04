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
package com.kwoksys.biz.issues.core;

import com.kwoksys.biz.ServiceProvider;
import com.kwoksys.biz.admin.AdminService;
import com.kwoksys.biz.admin.core.AdminUtils;
import com.kwoksys.biz.admin.dto.AccessUser;
import com.kwoksys.biz.auth.core.Access;
import com.kwoksys.biz.auth.core.IssueAccess;
import com.kwoksys.biz.issues.IssueService;
import com.kwoksys.biz.issues.dto.Issue;
import com.kwoksys.biz.system.SystemService;
import com.kwoksys.biz.system.core.AppPaths;
import com.kwoksys.biz.system.core.Links;
import com.kwoksys.framework.properties.Localizer;
import com.kwoksys.biz.system.core.ObjectTypes;
import com.kwoksys.framework.util.Counter;
import com.kwoksys.framework.http.RequestContext;
import com.kwoksys.biz.system.core.configs.ConfigManager;
import com.kwoksys.framework.connections.mail.EmailMessage;
import com.kwoksys.framework.connections.mail.Smtp;
import com.kwoksys.framework.exceptions.DatabaseException;
import com.kwoksys.framework.exceptions.ObjectNotFoundException;
import com.kwoksys.framework.parsers.email.IssueEmailParser;
import com.kwoksys.framework.ui.Link;
import com.kwoksys.framework.ui.RowStyle;
import com.kwoksys.framework.util.HtmlUtils;

import java.util.*;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * Issue utilities.
 */
public class IssueUtils {

    private static final Logger logger = Logger.getLogger(IssueUtils.class.getName());

    public static final String ISSUE_TAB_HISTORY = "history";

    public static final String ISSUE_TAB_RELATIONSHIP = "rel";

    public static String[] getIssuesDefaultColumns() {
        return new String[] {Issue.ROWNUM, Issue.ID, Issue.TITLE, Issue.TYPE, Issue.STATUS, Issue.PRIORITY,
                Issue.ASSIGNEE_NAME, Issue.CREATOR_NAME, Issue.CREATION_DATE, Issue.MODIFICATION_DATE, Issue.DUE_DATE};
    }

    /**
     * Returns column headers for list view.
     *
     * @return ..
     */
    public static List getIssueColumnHeaders() {
        return Arrays.asList(ConfigManager.app.getIssuesColumns());
    }

    public static List<String> getSortableColumns() {
        return Arrays.asList(Issue.ID, Issue.ASSIGNEE_NAME, Issue.TITLE, Issue.STATUS, Issue.PRIORITY,
                Issue.CREATOR_NAME, Issue.CREATION_DATE, Issue.MODIFICATION_DATE, Issue.DUE_DATE);
    }

    /**
     * Returns whether a column is sortable.
     *
     * @param columnName
     * @return ..
     */
    public static boolean isSortableColumn(String columnName) {
        return getSortableColumns().contains(columnName);
    }

    public static List<Map> formatIssues(RequestContext requestContext, List<Issue> issueList, IssueAccess access, Counter counter)
            throws DatabaseException {

        AccessUser user = requestContext.getUser();
        List list = new ArrayList();
        RowStyle ui = new RowStyle();
        List<String> columnHeaders = new ArrayList(getIssueColumnHeaders());

        boolean hasUserDetailAccess = Access.hasPermission(user, AppPaths.ADMIN_USER_DETAIL);
        boolean hasIssueDetailAccess = Access.hasPermission(user, AppPaths.ISSUES_DETAIL);

        for (Issue issue : issueList) {
            List columns = new ArrayList();

            for (String column : columnHeaders) {
                if (column.equals(Issue.ROWNUM)) {
                    columns.add(counter.incrCounter() + ".");

                } else if (column.equals(Issue.ID)) {
                    columns.add(issue.getId());
                    
                } else if (column.equals(Issue.TITLE)) {
                    Link issueLink = new Link(requestContext);
                    issueLink.setTitle(IssueUtils.truncateSubjectText(issue.getSubject()));

                    if (hasIssueDetailAccess && access.hasPermission(issue.getId())) {
                        issueLink.setAjaxPath(AppPaths.ISSUES_DETAIL + "?issueId=" + issue.getId());
                    }

                    columns.add(issueLink);

                } else if (column.equals(Issue.ASSIGNEE_NAME)) {
                    String assigneeName = IssueUtils.getAssigneeIconLink(requestContext, hasUserDetailAccess, issue.getAssignee());
                    columns.add(assigneeName);

                } else if (column.equals(Issue.CREATOR_NAME)) {
                    columns.add(Links.getUserIconLink(requestContext, issue.getCreator(), hasUserDetailAccess, true));
                    
                } else if (column.equals(Issue.CREATION_DATE)) {
                    columns.add(issue.getCreationDate());

                } else if (column.equals(Issue.MODIFIER_NAME)) {
                    columns.add(Links.getUserIconLink(requestContext, issue.getModifier(), hasUserDetailAccess, true));

                } else if (column.equals(Issue.MODIFICATION_DATE)) {
                    columns.add(issue.getModificationDate());

                } else if (column.equals(Issue.DUE_DATE)) {
                    columns.add(issue.getDueDateShort());

                } else if (column.equals(Issue.STATUS)) {
                    columns.add(HtmlUtils.encode(issue.getStatusName()));

                } else if (column.equals(Issue.TYPE)) {
                    columns.add(HtmlUtils.encode(issue.getTypeName()));

                } else if (column.equals(Issue.PRIORITY)) {
                    columns.add(HtmlUtils.encode(issue.getPriorityName()));
                }
            }
            Map map = new HashMap();
            map.put("issueId", issue.getId());
            map.put("rowId", issue.getId());
            map.put("rowClass", ui.getRowClass());
            map.put("columns", columns);
            list.add(map);
        }
        return list;
    }

    /**
     * @param request
     * @param issue
     * @return
     * @throws DatabaseException
     */
    public static List getIssueTabs(RequestContext requestContext, Issue issue, Integer relationshipCount)
            throws DatabaseException {

        AccessUser user = requestContext.getUser();

        List tabList = new ArrayList();

        // Issue History tab
        if (Access.hasPermission(user, AppPaths.ISSUES_DETAIL)) {
            Map tabMap = new HashMap();
            tabMap.put("tabName", ISSUE_TAB_HISTORY);
            tabMap.put("tabPath", AppPaths.ISSUES_DETAIL + "?issueId=" + issue.getId());
            tabMap.put("tabText", Localizer.getText(requestContext, "issues.tab.history"));
            tabList.add(tabMap);
        }
        
        // relationshipCount is optional, if not given, run a new query to get it.
        if (relationshipCount == null) {
            SystemService systemService = ServiceProvider.getSystemService(requestContext);
            List types = Arrays.asList(String.valueOf(ObjectTypes.COMPANY), String.valueOf(ObjectTypes.HARDWARE),
                    String.valueOf(ObjectTypes.SOFTWARE));
            List linkedTypes = Arrays.asList(String.valueOf(ObjectTypes.ISSUE));
            relationshipCount = systemService.getObjectMapCount(types, issue.getId(), linkedTypes);
        }

        // Issue History tab
        if (Access.hasPermission(user, AppPaths.ISSUES_RELATIONSHIP)) {
            Map tabMap = new HashMap();
            tabMap.put("tabName", ISSUE_TAB_RELATIONSHIP);
            tabMap.put("tabPath", AppPaths.ISSUES_RELATIONSHIP + "?issueId=" + issue.getId());
            tabMap.put("tabText", Localizer.getText(requestContext, "common.tab.relationships", new Integer[]{relationshipCount}));
            tabList.add(tabMap);
        }
        return tabList;
    }

    public static String formatAssigneeName(RequestContext requestContext, AccessUser assignee) {
        if (assignee == null || assignee.getId() == 0) {
            return Localizer.getText(requestContext, "issueMgmt.colName.unassigned");
        } else {
            return AdminUtils.getSystemUsername(requestContext, assignee);
        }
    }

    /**
     * Returns an assignee icon with name
     * @param request
     * @param hasPermission
     * @param assigneeName
     * @param assigneeId
     * @return
     */
    public static String getAssigneeIconLink(RequestContext requestContext, boolean hasPermission, AccessUser assignee) {
        if (assignee != null && assignee.getId() != 0) {
            return Links.getUserIconLink(requestContext, assignee, hasPermission, true).getString();
        } else {
            return IssueUtils.formatAssigneeName(requestContext, assignee);
        }
    }

    public static String truncateSubjectText(String subject) {
        int limit = 60;
        if (subject.length() > limit) {
            subject = subject.substring(0, limit) + "...";
        }
        return subject;
    }

    /**
     * Returns formatted email subject.
     * @param subject
     * @return
     */
    public static String formatEmailSubject(RequestContext requestContext, Issue issue) {
        return Localizer.getText(requestContext, "issues.issueAdd.emailSubject",
                new String[]{String.valueOf(issue.getId()), issue.getSubject()});
    }

    public static String formatEmailBody(RequestContext requestContext, String bodyField) {
        return IssueEmailParser.EMAIL_BODY_SEPARATOR
                + "\n"
                + Localizer.getText(requestContext, "issues.email.bodySeparatorMessage")
                + "\n\n"
                + bodyField;
    }

    public static void sendMail(RequestContext requestContext, Issue issue, Integer prevAssigneeId, String bodyField) {
        try {
            IssueService issueService = ServiceProvider.getIssueService(requestContext);

            EmailMessage message = new EmailMessage();
            AdminService adminService = ServiceProvider.getAdminService(requestContext);

            // Set FROM field
            message.setFromField(ConfigManager.email.getSmtpFrom());

            if (issue.hasAssignee()) {
                try {
                    // User may not exist anymore
                    AccessUser assignee = adminService.getUser(issue.getAssignee().getId());
                    message.getToField().add(assignee.getEmail());
                } catch (Exception e) {
                    /* ignore */
                }
            }

            if (prevAssigneeId != null) {
                try {
                    // User may not exist anymore
                    AccessUser prevAssignee = adminService.getUser(prevAssigneeId);
                    message.addCcField(prevAssignee.getEmail());
                } catch (Exception e) {
                    /* ignore */
                }
            }

            // Email to issue reporter if reporter is not default guest user
            AccessUser creator = issue.getCreator();
            if (!creator.getId().equals(Access.GUEST_USER_ID)) {
                try {
                    // User may not exist anymore
                    creator = adminService.getUser(creator.getId());
                    message.getToField().add(creator.getEmail());
                } catch (ObjectNotFoundException e) {
                    /* ignore */
                }
            }

            // Get a list of selected subscribers.
            List<AccessUser> selectedSubscribers = issueService.getSelectedSubscribers(issue.getId());
            if (!selectedSubscribers.isEmpty()) {
                for (AccessUser subscriber : selectedSubscribers) {
                    message.addCcField(subscriber.getEmail());
                }
            }

            if (message.getToField().isEmpty() && !message.getCcField().isEmpty()) {
                message.getToField().addAll(message.getCcField());
                message.getCcField().clear();
            }

            // Set SUBJECT field
            message.setSubjectField(IssueUtils.formatEmailSubject(requestContext, issue));

            // Set BODY field
            message.setBodyField(IssueUtils.formatEmailBody(requestContext, bodyField));

            // Debug:
    //        logger.log(Level.INFO, "From: " + message.getFromField());
    //        logger.log(Level.INFO, "To: " + message.getToField());
    //        logger.log(Level.INFO, "CC: " + message.getCcField());
    //        logger.log(Level.INFO, "Subject: " + message.getSubjectField());
    //        logger.log(Level.INFO, "Message: " + message.getBodyField());

            Smtp.send(message);
            
        } catch (Exception e) {
            logger.log(Level.SEVERE, "Problem sending email", e);
        }
    }

    public static boolean isHtmlEmail(String content) {
        content = content.toLowerCase();
        return (content.startsWith("<html") || content.startsWith("<!doctype html")) && content.endsWith("</html>");
    }
}