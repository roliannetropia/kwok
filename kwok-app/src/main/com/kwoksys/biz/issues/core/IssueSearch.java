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

import com.kwoksys.action.issues.IssueSearchForm;
import com.kwoksys.biz.admin.dto.AccessUser;
import com.kwoksys.biz.base.BaseSearch;
import com.kwoksys.framework.connections.database.QueryBits;
import com.kwoksys.framework.connections.database.SqlUtils;
import com.kwoksys.framework.http.RequestContext;
import com.kwoksys.framework.util.DatetimeUtils;

import java.util.List;

/**
 * This is for building search queries.
 */
public class IssueSearch extends BaseSearch {

    public static final String ISSUE_PERMITTED_USER_ID = "issuePermittedUserId";

    public static final String ISSUE_ID_EQUALS = "issueIdEquals";

    public IssueSearch(RequestContext requestContext, String sessionKey) {
        super(requestContext, sessionKey);
    }

    public IssueSearch() {}

    /**
     * This would generate searchCriteriaMap.
     *
     * @return ..
     */
    public void prepareMap(IssueSearchForm issueSearchForm) {
        String cmd = requestContext.getParameterString("cmd");
        String attrId = issueSearchForm.getAttrId();
        String attrValue = issueSearchForm.getAttrValue();
        AccessUser accessUser = requestContext.getUser();

        if (!cmd.isEmpty()) {
            issueSearchForm.setCmd(cmd);

            if (cmd.equals("showNonClosed")) {
                searchCriteriaMap.put("statusNotEquals", "closed");

            } else if (cmd.equals("showOpenUnassigned")) {
                searchCriteriaMap.put("unassigned", "");
                searchCriteriaMap.put("statusNotEquals", "closed");

            } else if (cmd.equals("showMyIssues")) {
                // We're searching for all non-closed Issues assigned to this user.
                searchCriteriaMap.put("assignedToId", accessUser.getId());
                searchCriteriaMap.put("statusNotEquals", "closed");

            } else if (cmd.equals("showMyReportedIssues")) {
                // We're searching for all non-closed Issues reported by to this user.
                searchCriteriaMap.put("reportedById", accessUser.getId());
                searchCriteriaMap.put("statusNotEquals", "closed");

            } else {
                reset();
                issueSearchForm.setRequest(requestContext);

                if (cmd.equals("search")) {
                    // Search by Issue id.
                    String issueId = issueSearchForm.getIssueId();
                    if (!issueId.isEmpty()) {
                        searchCriteriaMap.put(ISSUE_ID_EQUALS, issueId);
                    }
                    // Search by Issue title.
                    String subject = issueSearchForm.getSubject();
                    if (!subject.isEmpty()) {
                        searchCriteriaMap.put("subjectContains", subject);
                    }
                    // Search by issue description.
                    String description = issueSearchForm.getDescription();
                    if (!description.isEmpty()) {
                        searchCriteriaMap.put("descriptionContains", description);
                    }
                    // Search by Issue URL.
                    String url = issueSearchForm.getUrl();
                    if (!url.isEmpty()) {
                        searchCriteriaMap.put("urlContains", url);
                    }
                    // Search by Issue status not equal something.
                    String statusNotEquals = requestContext.getParameterString("statusNotEquals");
                    if (!statusNotEquals.isEmpty()) {
                        searchCriteriaMap.put("statusNotEquals", statusNotEquals);
                    }
                    // Search by Issue Status.
                    List<Integer> statusList = issueSearchForm.getStatus();
                    if (!statusList.isEmpty()) {
                        searchCriteriaMap.put("statusContains", statusList);
                    }
                    // Search by Issue Priority.
                    List<Integer> priorityList = issueSearchForm.getPriority();
                    if (!priorityList.isEmpty()) {
                        searchCriteriaMap.put("priorityContains", priorityList);
                    }
                    // Search by Issue Type.
                    List<Integer> typeList = issueSearchForm.getType();
                    if (!typeList.isEmpty()) {
                        searchCriteriaMap.put("typeContains", typeList);
                    }
                    // Search by assignee.
                    int assignedTo = requestContext.getParameter("assignedTo", 0);
                    if (assignedTo != 0) {
                        searchCriteriaMap.put("assignedToId", assignedTo);
                    }
                    // Search by submitter.
                    Integer submitter = requestContext.getParameter("submitter");
                    if (submitter != 0) {
                        searchCriteriaMap.put("reportedById", submitter);
                    }
                    // Search by Issue submission date range.
                    String submissionDate = requestContext.getParameterString("submissionDate");
                    if (!submissionDate.isEmpty()) {
                        if (submissionDate.equals("last1days")) {
                            searchCriteriaMap.put("submittedWithinLast", 1);

                        } else if (submissionDate.equals("last7days")) {
                            searchCriteriaMap.put("submittedWithinLast", 7);

                        } else if (submissionDate.equals("last14days")) {
                            searchCriteriaMap.put("submittedWithinLast", 14);

                        } else if (submissionDate.equals("last30days")) {
                            searchCriteriaMap.put("submittedWithinLast", 30);
                        }
                    }

                    // Search by submitted after
                    String submittedAfterDate = requestContext.getParameterString("submittedAfterDate");
                    String submittedAfterMonth = requestContext.getParameterString("submittedAfterMonth");
                    String submittedAfterYear = requestContext.getParameterString("submittedAfterYear");

                    if (DatetimeUtils.isValidDate(submittedAfterYear, submittedAfterMonth, submittedAfterDate)) {
                        searchCriteriaMap.put("submittedAfter", submittedAfterYear + "-" + submittedAfterMonth + "-" + submittedAfterDate);
                    }

                    // Search by submitted before
                    String submittedBeforeDate = requestContext.getParameterString("submittedBeforeDate");
                    String submittedBeforeMonth = requestContext.getParameterString("submittedBeforeMonth");
                    String submittedBeforeYear = requestContext.getParameterString("submittedBeforeYear");

                    if (DatetimeUtils.isValidDate(submittedBeforeYear, submittedBeforeMonth, submittedBeforeDate)) {
                        searchCriteriaMap.put("submittedBefore", submittedBeforeYear + "-" + submittedBeforeMonth + "-" + submittedBeforeDate);
                    }

                    // Search by Issue due date.
                    String dueIn = requestContext.getParameterString("dueWithin");
                    if (!dueIn.isEmpty()) {
                        if (dueIn.equals("1day")) {
                            searchCriteriaMap.put("dueIn", 1);

                        } else if (dueIn.equals("7days")) {
                            searchCriteriaMap.put("dueIn", 7);

                        } else if (dueIn.equals("30days")) {
                            searchCriteriaMap.put("dueIn", 30);
                        }
                    }

                    // Search by custom fields
                    if (!attrId.isEmpty() && !attrValue.isEmpty()) {
                        searchCriteriaMap.put("attrId", attrId);
                        searchCriteriaMap.put("attrValue", attrValue);
                    }
                }
            }
        }
    }

    /**
     * This would take searchCriteriaMap and compose the sql queries.
     */
    public void applyMap(QueryBits query) {
        if (searchCriteriaMap == null) {
            return;
        }
        // Issues user allowed to see
        if (searchCriteriaMap.containsKey(ISSUE_PERMITTED_USER_ID)) {
            Integer userId = (Integer) searchCriteriaMap.get(ISSUE_PERMITTED_USER_ID);
            query.appendWhereClause("(i.assignee_id=" + userId + " or i.creator="+ userId
                    + " or i.issue_id in (select issue_id from issue_subscription where user_id="+userId+"))");
        }
        // For Issue Id
        if (searchCriteriaMap.containsKey(ISSUE_ID_EQUALS)) {
            query.appendWhereClause("i.issue_id = " + SqlUtils.encodeInteger(searchCriteriaMap.get(ISSUE_ID_EQUALS)));
        }
        // For Issue Subject
        if (searchCriteriaMap.containsKey("subjectContains")) {
            query.appendWhereClause("lower(i.issue_name) like lower('%" + SqlUtils.encodeString(searchCriteriaMap.get("subjectContains")) + "%')");
        } else if (searchCriteriaMap.containsKey("subjectEquals")) {
            query.appendWhereClause("lower(i.issue_name) = lower('" + SqlUtils.encodeString(searchCriteriaMap.get("subjectEquals")) + "')");
        }
        // For issue description
        if (searchCriteriaMap.containsKey("descriptionContains")) {
            query.appendWhereClause("lower(i.issue_description) like lower('%" + SqlUtils.encodeString(searchCriteriaMap.get("descriptionContains")) + "%')");
        }
        // For Issue URL
        if (searchCriteriaMap.containsKey("urlContains")) {
            query.appendWhereClause("lower(i.issue_url) like lower('%" + SqlUtils.encodeString(searchCriteriaMap.get("urlContains")) + "%')");
        }
        // Non-closed Status
        if (searchCriteriaMap.containsKey("statusNotEquals")) {
            query.appendWhereClause("i.issue_status not in (select attribute_field_id from attribute_field where field_key='" + SqlUtils.encodeString(searchCriteriaMap.get("statusNotEquals")) + "')");
        }
        // For Issue Status
        if (searchCriteriaMap.containsKey("statusContains")) {
            query.appendWhereClause("i.issue_status in (" + SqlUtils.encodeIntegers((List<Integer>) searchCriteriaMap.get("statusContains")) + ")");
        }
        // For Issue Priority
        if (searchCriteriaMap.containsKey("priorityContains")) {
            query.appendWhereClause("i.issue_priority in (" + SqlUtils.encodeIntegers((List<Integer>) searchCriteriaMap.get("priorityContains")) + ")");
        }
        // For Issue Type
        if (searchCriteriaMap.containsKey("typeContains")) {
            query.appendWhereClause("i.issue_type in (" + SqlUtils.encodeIntegers((List<Integer>) searchCriteriaMap.get("typeContains")) + ")");
        }
        // Issues assigned to a specific user.
        if (searchCriteriaMap.containsKey("assignedToId")) {
            query.appendWhereClause("i.assignee_id = " + SqlUtils.encodeInteger(searchCriteriaMap.get("assignedToId")));
        }
        // Issues unassigned.
        if (searchCriteriaMap.containsKey("unassigned")) {
            query.appendWhereClause("i.assignee_id is null");
        }
        // Issues reported by specific user.
        if (searchCriteriaMap.containsKey("reportedById")) {
            query.appendWhereClause("i.creator = " + SqlUtils.encodeInteger(searchCriteriaMap.get("reportedById")));
        }
        // Issues submitted within this timeframe.
        if (searchCriteriaMap.containsKey("submittedWithinLast")) {
            query.appendWhereClause("i.creation_date > now()::timestamp + '-" + SqlUtils.encodeInteger(searchCriteriaMap.get("submittedWithinLast")) + " day'::interval");
        }
        // Issues submitted after this date.
        if (searchCriteriaMap.containsKey("submittedAfter")) {
            query.appendWhereClause("i.creation_date > '" + SqlUtils.encodeString(searchCriteriaMap.get("submittedAfter")) + "'");
        }
        // Issues submitted before this date.
        if (searchCriteriaMap.containsKey("submittedBefore")) {
            query.appendWhereClause("i.creation_date < '" + SqlUtils.encodeString(searchCriteriaMap.get("submittedBefore")) + "'");
        }
        // Search by Issue due date
        if (searchCriteriaMap.containsKey("dueIn")) {
            query.appendWhereClause("i.issue_due_date < now()::timestamp + '" + SqlUtils.encodeInteger(searchCriteriaMap.get("dueIn")) + " day'::interval");
        }
        // For custom fields
        if (searchCriteriaMap.containsKey("attrId") && searchCriteriaMap.containsKey("attrValue")) {
            query.appendWhereClause("i.issue_id in (select object_id from object_attribute_value where attribute_id = "+
                    SqlUtils.encodeInteger(searchCriteriaMap.get("attrId")) + " and lower(attr_value) like lower('%"
                    + SqlUtils.encodeString(searchCriteriaMap.get("attrValue")) +"%'))");
        }
    }
}
