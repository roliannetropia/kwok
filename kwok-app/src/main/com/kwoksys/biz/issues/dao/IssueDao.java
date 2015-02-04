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
package com.kwoksys.biz.issues.dao;

import com.kwoksys.biz.ServiceProvider;
import com.kwoksys.biz.admin.AdminService;
import com.kwoksys.biz.admin.core.AdminUtils;
import com.kwoksys.biz.admin.core.UserSearch;
import com.kwoksys.biz.admin.dao.AdminQueries;
import com.kwoksys.biz.admin.dao.AttributeDao;
import com.kwoksys.biz.admin.dto.AccessUser;
import com.kwoksys.biz.admin.dto.AttributeFieldCount;
import com.kwoksys.biz.base.BaseDao;
import com.kwoksys.biz.issues.dto.Issue;
import com.kwoksys.biz.system.core.ObjectTypes;
import com.kwoksys.biz.system.dto.linking.ObjectLink;
import com.kwoksys.framework.http.RequestContext;
import com.kwoksys.framework.connections.database.QueryBits;
import com.kwoksys.framework.connections.database.QueryHelper;
import com.kwoksys.framework.exceptions.DatabaseException;
import com.kwoksys.framework.exceptions.ObjectNotFoundException;
import com.kwoksys.framework.util.DatetimeUtils;
import com.kwoksys.framework.util.StringUtils;
import org.apache.struts.action.ActionMessages;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.Types;
import java.util.*;

/**
 * IssueDao class.
 */
public class IssueDao extends BaseDao {

    public IssueDao(RequestContext requestContext) {
        super(requestContext);
    }

    /**
     * Get number of issues group by status.
     *
     * @return ..
     */
    public List<AttributeFieldCount> getGoupByStatusCount(QueryBits query) throws DatabaseException {
        Connection conn = getConnection();

        QueryHelper queryHelper = new QueryHelper(IssueQueries.selectIssueCountGoupByStatusQuery(query));

        try {
            List counts = new ArrayList();
            ResultSet rs = queryHelper.executeQuery(conn);

            while (rs.next()) {
                AttributeFieldCount count = new AttributeFieldCount();
                count.setAttrFieldId(rs.getInt("attribute_field_id"));
                count.setObjectCount(rs.getInt("status_count"));

                counts.add(count);
            }
            return counts;

        } catch (Exception e) {
            // Database problem
            throw new DatabaseException(e, queryHelper);

        } finally {
            queryHelper.closeRs();
            closeConnection(conn);
        }
    }

    /**
     * Get open issues by priority.
     *
     * @return ..
     */
    public List<AttributeFieldCount> getGoupByPriorityCount(QueryBits query) throws DatabaseException {
        Connection conn = getConnection();

        QueryHelper queryHelper = new QueryHelper(IssueQueries.selectIssueCountByPriorityQuery(query));

        try {
            List counts = new ArrayList();
            ResultSet rs = queryHelper.executeQuery(conn);

            while (rs.next()) {
                AttributeFieldCount count = new AttributeFieldCount();
                count.setAttrFieldId(rs.getInt("attribute_field_id"));
                count.setObjectCount(rs.getInt("priority_count"));

                counts.add(count);
            }
            return counts;

        } catch (Exception e) {
            // Database problem
            throw new DatabaseException(e, queryHelper);

        } finally {
            queryHelper.closeRs();
            closeConnection(conn);
        }
    }

    /**
     * Get open issues by type.
     *
     * @return ..
     */
    public List<AttributeFieldCount> getGoupByTypeCount(QueryBits query) throws DatabaseException {
        Connection conn = getConnection();

        QueryHelper queryHelper = new QueryHelper(IssueQueries.selectIssueCountByTypeQuery(query));

        try {
            List counts = new ArrayList();
            ResultSet rs = queryHelper.executeQuery(conn);

            while (rs.next()) {
                AttributeFieldCount count = new AttributeFieldCount();
                count.setAttrFieldId(rs.getInt("attribute_field_id"));
                count.setObjectCount(rs.getInt("type_count"));

                counts.add(count);
            }
            return counts;

        } catch (Exception e) {
            // Database problem
            throw new DatabaseException(e, queryHelper);

        } finally {
            queryHelper.closeRs();
            closeConnection(conn);
        }
    }

    /**
     * Get number of issues group by Assignee.
     *
     * @return ..
     */
    public List getGoupByAssigneeCount(QueryBits query) throws DatabaseException {
        QueryHelper queryHelper = new QueryHelper(IssueQueries.selectIssueCountGoupByAssigneeQuery(query));

        return executeQueryReturnList(queryHelper);
    }

    /**
     * Get a list of Issues
     * @param query
     * @return
     * @throws DatabaseException
     */
    public List<Issue> getIssues(QueryBits query, QueryHelper queryHelper) throws DatabaseException {
        Connection conn = getConnection();

        if (queryHelper == null) {
            queryHelper = new QueryHelper(IssueQueries.selectIssueListQuery(query));
        }

        try {
            List issues = new ArrayList();
            ResultSet rs = queryHelper.executeQuery(conn);

            while (rs.next()) {
                Issue issue = new Issue();
                issue.setId(rs.getInt("issue_id"));
                issue.setSubject(StringUtils.replaceNull(rs.getString("issue_name")));
                issue.setDescription(StringUtils.replaceNull(rs.getString("issue_description")));
                issue.setTypeName(StringUtils.replaceNull(rs.getString("issue_type_name")));
                issue.setStatusName(StringUtils.replaceNull(rs.getString("issue_status_name")));
                issue.setPriorityName(StringUtils.replaceNull(rs.getString("issue_priority_name")));
                issue.setUrl(StringUtils.replaceNull(rs.getString("issue_url")));
                issue.setCreationDate(DatetimeUtils.getDate(rs, "creation_date"));
                issue.setModificationDate(DatetimeUtils.getDate(rs, "modification_date"));
                issue.setDueDate(DatetimeUtils.getDate(rs, "issue_due_date"));

                issue.setAssignee(new AccessUser());
                issue.getAssignee().setId(rs.getInt("assignee_id"));
                issue.getAssignee().setUsername(rs.getString("assignee_username"));
                issue.getAssignee().setDisplayName(rs.getString("assignee_display_name"));

                issue.setCreator(new AccessUser());
                issue.getCreator().setId(rs.getInt("creator"));
                issue.getCreator().setUsername(rs.getString("creator_username"));
                issue.getCreator().setDisplayName(rs.getString("creator_display_name"));

                issue.setModifier(new AccessUser());
                issue.getModifier().setId(rs.getInt("modifier"));
                issue.getModifier().setUsername(rs.getString("modifier_username"));
                issue.getModifier().setDisplayName(rs.getString("modifier_display_name"));

                issues.add(issue);
            }
            return issues;

        } catch (Exception e) {
            // Database problem
            throw new DatabaseException(e, queryHelper);

        } finally {
            queryHelper.closeRs();
            closeConnection(conn);
        }
    }

    /**
     * Get a list of linked Issues.
     * @param query
     * @return
     * @throws DatabaseException
     */
    public List<Issue> getLinkedIssueList(QueryBits query, ObjectLink objectMap) throws DatabaseException {
        QueryHelper queryHelper = new QueryHelper(IssueQueries.selectLinkedIssuesQuery(query));
        queryHelper.addInputInt(objectMap.getObjectId());
        queryHelper.addInputInt(objectMap.getObjectTypeId());
        queryHelper.addInputInt(objectMap.getLinkedObjectTypeId());

        return getIssues(query, queryHelper);
    }

    public int getCount(QueryBits query) throws DatabaseException {
        return getRowCount(IssueQueries.selectIssueCountQuery(query));
    }

    public Set getIssueIds(QueryBits query) throws DatabaseException {
        Connection conn = getConnection();

        QueryHelper queryHelper = new QueryHelper(IssueQueries.selectIssueIdsQuery(query));

        try {
            Set set = new HashSet();

            ResultSet rs = queryHelper.executeQuery(conn);
            if (rs.next()) {
                set.add(rs.getInt("issue_id"));
            }
            return set;

        } catch (Exception e) {
            // Database problem
            throw new DatabaseException(e, queryHelper);

        } finally {
            queryHelper.closeRs();
            closeConnection(conn);
        }
    }

    /**
     * Return details for a specific issue
     *
     * @param requestedIssueId
     * @return ..
     */
    public Issue getIssue(Integer issueId) throws DatabaseException, ObjectNotFoundException {
        Connection conn = getConnection();

        QueryHelper queryHelper = new QueryHelper(IssueQueries.selectIssueDetailQuery());
        queryHelper.addInputInt(issueId);

        try {
            ResultSet rs = queryHelper.executeQuery(conn);
            if (rs.next()) {
                Issue issue = new Issue();
                issue.setId(rs.getInt("issue_id"));
                issue.setSubject(StringUtils.replaceNull(rs.getString("issue_name")));
                issue.setDescription(StringUtils.replaceNull(rs.getString("issue_description")));
                issue.setStatus(rs.getInt("issue_status"));
                issue.setType(rs.getInt("issue_type"));
                issue.setPriority(rs.getInt("issue_priority"));
                issue.setResolution(rs.getInt("issue_resolution"));
                issue.setUrl(StringUtils.replaceNull(rs.getString("issue_url")));
                issue.setDueDate(DatetimeUtils.getDate(rs, "issue_due_date"));
                issue.setHasDueDate(issue.getDueDate() != null);
                issue.setFromEmail(StringUtils.replaceNull(rs.getString("issue_created_from_email")));
                issue.setCreatorIP(StringUtils.replaceNull(rs.getString("creator_ip")));
                issue.setCreationDate(DatetimeUtils.getDate(rs, "creation_date"));
                issue.setModificationDate(DatetimeUtils.getDate(rs, "modification_date"));

                issue.setAssignee(new AccessUser());
                issue.getAssignee().setId(rs.getInt("assignee_id"));
                issue.getAssignee().setUsername(rs.getString("assignee_username"));
                issue.getAssignee().setDisplayName(rs.getString("assignee_display_name"));

                issue.setCreator(new AccessUser());
                issue.getCreator().setId(rs.getInt("creator"));
                issue.getCreator().setUsername(rs.getString("creator_username"));
                issue.getCreator().setDisplayName(rs.getString("creator_display_name"));

                issue.setModifier(new AccessUser());
                issue.getModifier().setId(rs.getInt("modifier"));
                issue.getModifier().setUsername(rs.getString("modifier_username"));
                issue.getModifier().setDisplayName(rs.getString("modifier_display_name"));

                return issue;
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
     * Get a list of issue history for a given issueId.
     *
     * @param query
     * @param issueId
     * @return ..
     */
    public List getHistory(QueryBits query, Integer issueId) throws DatabaseException {
        QueryHelper queryHelper = new QueryHelper(IssueQueries.selectIssueHistoryQuery(query));
        queryHelper.addInputInt(ObjectTypes.ISSUE);
        queryHelper.addInputInt(issueId);
        queryHelper.addInputInt(issueId);
        queryHelper.addInputInt(issueId);

        return executeQueryReturnList(queryHelper);
    }

    public ActionMessages addIssueSimple(Issue issue) throws DatabaseException {
        QueryHelper queryHelper = new QueryHelper(IssueQueries.insertIssueQuery());
        queryHelper.addOutputParam(Types.INTEGER);
        queryHelper.addInputStringConvertNull(issue.getSubject());
        queryHelper.addInputStringConvertNull(issue.getDescription());
        queryHelper.addInputStringConvertNull(issue.getUrl());
        queryHelper.addInputIntegerConvertNull(issue.getType());
        queryHelper.addInputIntegerConvertNull(issue.getStatus());
        queryHelper.addInputIntegerConvertNull(issue.getPriority());
        queryHelper.addInputIntegerConvertNull(issue.getResolution());
        queryHelper.addInputIntegerConvertNull(issue.getAssignee().getId());
        queryHelper.addInputStringConvertNull(issue.isHasDueDate() ? DatetimeUtils.createDatetimeString(
                issue.getDueDateYear(), issue.getDueDateMonth(), issue.getDueDateDate()) : null);
        queryHelper.addInputStringConvertNull(issue.getCreatorIP());
        queryHelper.addInputStringConvertNull(issue.getFromEmail());
        queryHelper.addInputInt(requestContext.getUser().getId());
        queryHelper.addInputIntegerConvertNull(null);

        executeProcedure(queryHelper);

        // Put some values in the result.
        if (errors.isEmpty()) {
            issue.setId((Integer)queryHelper.getSqlOutputs().get(0));
        }

        return errors;
    }

    public ActionMessages add(RequestContext requestContext, Issue issue) throws DatabaseException {
        Connection conn = getConnection();

        QueryHelper queryHelper = new QueryHelper(IssueQueries.insertIssueQuery());
        queryHelper.addOutputParam(Types.INTEGER);
        queryHelper.addInputStringConvertNull(issue.getSubject());
        queryHelper.addInputStringConvertNull(issue.getDescription());
        queryHelper.addInputStringConvertNull(issue.getUrl());
        queryHelper.addInputIntegerConvertNull(issue.getType());
        queryHelper.addInputIntegerConvertNull(issue.getStatus());
        queryHelper.addInputIntegerConvertNull(issue.getPriority());
        queryHelper.addInputIntegerConvertNull(issue.getResolution());
        queryHelper.addInputIntegerConvertNull(issue.getAssignee().getId());
        queryHelper.addInputStringConvertNull(issue.isHasDueDate() ? DatetimeUtils.createDatetimeString(
                issue.getDueDateYear(), issue.getDueDateMonth(), issue.getDueDateDate()) : null);
        queryHelper.addInputStringConvertNull(issue.getCreatorIP());
        queryHelper.addInputStringConvertNull(issue.getFromEmail());

        Integer creator = requestContext.getUser().getId();
        Integer proxyCreator = null;

        if (issue.getProxyUserId() != null && issue.getProxyUserId() != 0) {
            creator = issue.getProxyUserId();
            proxyCreator = requestContext.getUser().getId();
        }

        queryHelper.addInputInt(creator);
        queryHelper.addInputIntegerConvertNull(proxyCreator);

        try {
            queryHelper.executeProcedure(conn);

            // Put some values in the result.
            issue.setId((Integer) queryHelper.getSqlOutputs().get(0));

            // Update issue subscriber list
            addSubscribers(requestContext, conn, issue);

            // Update custom fields
            if (!issue.getCustomValues().isEmpty()) {
                AttributeDao attributeDao = new AttributeDao(requestContext);
                attributeDao.updateAttributeValue(conn, issue.getId(), issue.getCustomValues());
            }
        } catch (Exception e) {
            // Database problem
            handleError(e);

        } finally {
            closeConnection(conn);
        }
        return errors;
    }

    /**
     * This is to edit a issue.
     * We also update issue log after editing a issue.
     *
     * @param issue
     * @return ..
     */
    public ActionMessages update(RequestContext requestContext, Issue issue, boolean updateSubscribers) throws DatabaseException {
        Connection conn = getConnection();

        QueryHelper queryHelper = new QueryHelper(IssueQueries.updateIssueQuery());
        queryHelper.addInputInt(issue.getId());
        queryHelper.addInputStringConvertNull(issue.getSubject());
        queryHelper.addInputStringConvertNull(issue.getFollowup());
        queryHelper.addInputIntegerConvertNull(issue.getType());
        queryHelper.addInputIntegerConvertNull(issue.getStatus());
        queryHelper.addInputIntegerConvertNull(issue.getPriority());
        queryHelper.addInputIntegerConvertNull(issue.getResolution());
        queryHelper.addInputIntegerConvertNull(issue.getAssignee().getId());
        queryHelper.addInputStringConvertNull(issue.isHasDueDate() ? DatetimeUtils.createDatetimeString(
                issue.getDueDateYear(), issue.getDueDateMonth(), issue.getDueDateDate()) : null);
        queryHelper.addInputStringConvertNull(issue.getFromEmail());
        queryHelper.addInputInt(requestContext.getUser().getId());

        try {
            queryHelper.executeProcedure(conn);

            if (updateSubscribers) {
                // Update issue subscriber list
                updateSubscribers(requestContext, conn, issue);
            }

            // Update custom fields
            if (issue.getCustomValues() != null && !issue.getCustomValues().isEmpty()) {
                AttributeDao attributeDao = new AttributeDao(requestContext);
                attributeDao.updateAttributeValue(conn, issue.getId(), issue.getCustomValues());
            }
        } catch (Exception e) {
            // Database problem
            handleError(e);

        } finally {
            closeConnection(conn);
        }
        return errors;
    }

    /**
     * Delete an Issue.
     *
     * @return ..
     */
    public ActionMessages delete(Integer issueId) throws DatabaseException {
        QueryHelper queryHelper = new QueryHelper(IssueQueries.deleteIssueQuery());
        queryHelper.addInputInt(ObjectTypes.ISSUE);
        queryHelper.addInputInt(issueId);

        return executeProcedure(queryHelper);
    }

    public List<AccessUser> getSelectedSubscribers(Integer issueId) throws DatabaseException {
        // Get a list of selected subscribers.
        UserSearch userSearch = new UserSearch();
        userSearch.put("issueSelectedSubscribers", issueId);

        QueryBits query = new QueryBits(userSearch);
        query.addSortColumn(AdminQueries.getOrderByColumn(AdminUtils.getUsernameSort()));

        AdminService adminService = ServiceProvider.getAdminService(requestContext);
        return adminService.getUsers(query);
    }

    /**
     * Updates subscribers
     * @param requestContext
     * @param conn
     * @param issue
     * @throws DatabaseException
     */
    private void updateSubscribers(RequestContext requestContext, Connection conn, Issue issue) throws DatabaseException {
        List<Integer> selectedSubscribers = issue.getSelectedSubscribers() == null ? new ArrayList() :
            new ArrayList(issue.getSelectedSubscribers());

        // Loop through the current subscriber list
        // If the subscriber is not the in list, run a procedure to remove the member
        for (AccessUser user: getSelectedSubscribers(issue.getId())) {
            if (selectedSubscribers.contains(user.getId())) {
                selectedSubscribers.remove(user.getId());
            } else {
                deleteSubscriber(conn, issue, user.getId());
            }
        }

        // The remaining list has the users we want to add
        for (Integer userId: selectedSubscribers) {
            addSubscriber(requestContext, conn, issue, userId);
        }
    }

    /**
     * Adds subscribers.
     */
    private void addSubscribers(RequestContext requestContext, Connection conn, Issue issue) throws DatabaseException {
        if (issue.getSelectedSubscribers() != null) {
            for (Integer subscriberUserId : issue.getSelectedSubscribers()) {
                addSubscriber(requestContext, conn, issue, subscriberUserId);
            }
        }
    }

    /**
     * Add issue subscriber.
     *
     * @param issue
     * @return ..
     */
    private void addSubscriber(RequestContext requestContext, Connection conn, Issue issue, Integer subscriberUserId) throws DatabaseException {
        QueryHelper queryHelper = new QueryHelper(IssueQueries.addSubscriberQuery());
        queryHelper.addInputInt(issue.getId());
        queryHelper.addInputInt(subscriberUserId);
        queryHelper.addInputInt(requestContext.getUser().getId());

        queryHelper.executeProcedure(conn);
    }

    private void deleteSubscriber(Connection conn, Issue issue, Integer subscriberUserId) throws DatabaseException {
        QueryHelper queryHelper = new QueryHelper(IssueQueries.deleteSubscriberQuery());
        queryHelper.addInputInt(issue.getId());
        queryHelper.addInputInt(subscriberUserId);

        queryHelper.executeProcedure(conn);
    }

    /**
     * This is to add an Issue file.
     *
     * @param issue
     * @return ..
     */
    public ActionMessages addIssueFile(Issue issue) throws DatabaseException {
        QueryHelper queryHelper = new QueryHelper(IssueQueries.insertIssueFileQuery());
        queryHelper.addInputInt(issue.getId());
        queryHelper.addInputIntegerConvertNull(issue.getFileId());
        queryHelper.addInputInt(requestContext.getUser().getId());

        return executeProcedure(queryHelper);
    }
}
