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
package com.kwoksys.biz.issues;

import com.kwoksys.action.files.FileUploadForm;
import com.kwoksys.biz.ServiceProvider;
import com.kwoksys.biz.admin.AdminService;
import com.kwoksys.biz.admin.core.AdminUtils;
import com.kwoksys.biz.admin.core.UserSearch;
import com.kwoksys.biz.admin.dao.AdminQueries;
import com.kwoksys.biz.admin.dto.AccessUser;
import com.kwoksys.biz.admin.dto.Attribute;
import com.kwoksys.biz.admin.dto.AttributeFieldCount;
import com.kwoksys.biz.auth.core.Permissions;
import com.kwoksys.biz.contacts.dao.CompanyDao;
import com.kwoksys.biz.contacts.dto.Company;
import com.kwoksys.biz.files.FileService;
import com.kwoksys.biz.files.dto.File;
import com.kwoksys.biz.hardware.HardwareService;
import com.kwoksys.biz.hardware.dto.Hardware;
import com.kwoksys.biz.issues.core.IssueSearch;
import com.kwoksys.biz.issues.dao.IssueDao;
import com.kwoksys.biz.issues.dto.Issue;
import com.kwoksys.biz.issues.dto.IssueFile;
import com.kwoksys.biz.software.SoftwareService;
import com.kwoksys.biz.software.dto.Software;
import com.kwoksys.biz.system.dto.linking.CompanyIssueLink;
import com.kwoksys.biz.system.dto.linking.HardwareIssueLink;
import com.kwoksys.biz.system.dto.linking.ObjectLink;
import com.kwoksys.biz.system.dto.linking.SoftwareIssueLink;
import com.kwoksys.biz.system.core.ObjectTypes;
import com.kwoksys.framework.exceptions.AccessDeniedException;
import com.kwoksys.framework.properties.Localizer;
import com.kwoksys.biz.system.core.AttributeFieldIds;
import com.kwoksys.biz.system.core.Schema;
import com.kwoksys.biz.system.core.configs.ConfigManager;
import com.kwoksys.framework.connections.database.QueryBits;
import com.kwoksys.framework.connections.mail.EmailMessage;
import com.kwoksys.framework.connections.mail.PopConnection;
import com.kwoksys.framework.connections.mail.Pop;
import com.kwoksys.framework.exceptions.DatabaseException;
import com.kwoksys.framework.exceptions.ObjectNotFoundException;
import com.kwoksys.framework.parsers.email.IssueEmailParser;
import com.kwoksys.framework.http.RequestContext;
import org.apache.struts.action.ActionMessage;
import org.apache.struts.action.ActionMessages;

import java.util.List;
import java.util.Map;
import java.util.Set;

/**
 * IssueService
 */
public class IssueService {

    private RequestContext requestContext;

    public IssueService(RequestContext requestContext) {
        this.requestContext = requestContext;
    }

    public List<AttributeFieldCount> getGoupByStatusCount(QueryBits query) throws DatabaseException {
        IssueDao issueDao = new IssueDao(requestContext);
        return issueDao.getGoupByStatusCount(query);
    }

    public List<AttributeFieldCount> getGoupByPriorityCount(QueryBits query) throws DatabaseException {
        IssueDao issueDao = new IssueDao(requestContext);
        return issueDao.getGoupByPriorityCount(query);
    }

    public List<AttributeFieldCount> getGoupByTypeCount(QueryBits query) throws DatabaseException {
        IssueDao issueDao = new IssueDao(requestContext);
        return issueDao.getGoupByTypeCount(query);
    }

    public List<Map> getGoupByAssigneeCount(QueryBits query) throws DatabaseException {
        IssueDao issueDao = new IssueDao(requestContext);
        return issueDao.getGoupByAssigneeCount(query);
    }

    public List<Issue> getIssues(QueryBits query) throws DatabaseException {
        IssueDao issueDao = new IssueDao(requestContext);
        return issueDao.getIssues(query, null);
    }

    public List<Issue> getLinkedIssues(QueryBits query, ObjectLink objectMap) throws DatabaseException {
        IssueDao issueDao = new IssueDao(requestContext);
        return issueDao.getLinkedIssueList(query, objectMap);
    }

    public Set getIssueIds(QueryBits query) throws DatabaseException {
        IssueDao issueDao = new IssueDao(requestContext);
        return issueDao.getIssueIds(query);
    }

    public int getCount(QueryBits query) throws DatabaseException {
        return new IssueDao(requestContext).getCount(query);
    }

    public Issue getPublicIssue(Integer issueId) throws DatabaseException, ObjectNotFoundException {
        IssueDao issueDao = new IssueDao(requestContext);
        return issueDao.getIssue(issueId);
    }

    /**
     * Returns a given issue with advanced permission check. This is more expensive than getPublicIssue().
     * @param issueId
     * @return
     * @throws DatabaseException
     * @throws ObjectNotFoundException
     * @throws com.kwoksys.framework.exceptions.AccessDeniedException
     */
    public Issue getIssue(Integer issueId) throws DatabaseException, ObjectNotFoundException, AccessDeniedException {
        IssueDao issueDao = new IssueDao(requestContext);

        AccessUser accessUser = requestContext.getUser();

        if (!accessUser.hasPermission(Permissions.ISSUE_READ_PERMISSION)) {
            IssueSearch issueSearch = new IssueSearch();

            // For access control
            issueSearch.put(IssueSearch.ISSUE_PERMITTED_USER_ID, accessUser.getId());
            issueSearch.put(IssueSearch.ISSUE_ID_EQUALS, issueId);

            int count = getCount(new QueryBits(issueSearch));
            if (count < 1) {
                throw new AccessDeniedException();
            }
        }
        return issueDao.getIssue(issueId);
    }

    public List<AccessUser> getAvailableSubscribers(Integer issueId) throws DatabaseException {
        // Get a list of available subscribers.
        UserSearch userSearch = new UserSearch();
        userSearch.put(UserSearch.USER_STATUS, AttributeFieldIds.USER_STATUS_ENABLED);
        userSearch.put("issueAvailableSubscribers", issueId);

        QueryBits query = new QueryBits(userSearch);
        query.addSortColumn(AdminQueries.getOrderByColumn(AdminUtils.getUsernameSort()));

        AdminService adminService = ServiceProvider.getAdminService(requestContext);

        return adminService.getUsers(query);
    }

    public List<AccessUser> getSelectedSubscribers(Integer issueId) throws DatabaseException {
        IssueDao issueDao = new IssueDao(requestContext);
        return issueDao.getSelectedSubscribers(issueId);
    }

    public List getHistory(QueryBits query, Integer issueId) throws DatabaseException {
        IssueDao issueDao = new IssueDao(requestContext);
        return issueDao.getHistory(query, issueId);
    }

    public List<Hardware> getIssueHardwareList(QueryBits query, Integer issueId) throws DatabaseException {
        HardwareIssueLink map = new HardwareIssueLink();
        map.setIssueId(issueId);

        HardwareService hardwareService = ServiceProvider.getHardwareService(requestContext);
        return hardwareService.getLinkedHardwareList(query, map.createObjectMap());
    }

    public List<Software> getIssueSoftwareList(QueryBits query, Integer issueId) throws DatabaseException {
        SoftwareIssueLink link = new SoftwareIssueLink();
        link.setIssueId(issueId);

        SoftwareService softwareService = ServiceProvider.getSoftwareService(requestContext);
        return softwareService.getLinkedSoftwareList(query, link.createObjectMap());
    }

    public List<Company> getIssueCompanyList(QueryBits query, Integer issueId) throws DatabaseException {
        CompanyIssueLink map = new CompanyIssueLink();
        map.setIssueId(issueId);
        CompanyDao companyDao = new CompanyDao(requestContext);
        return companyDao.getLinkedCompanyList(query, map.createObjectMap());
    }

    public List<File> getIssueFiles(QueryBits query, Integer issueId) throws DatabaseException {
        FileService fileService = ServiceProvider.getFileService(requestContext);
        return fileService.getFiles(query, ObjectTypes.ISSUE, issueId);
    }

    public File getIssueFile(Integer issueId, Integer fileId) throws DatabaseException, ObjectNotFoundException {
        FileService fileService = ServiceProvider.getFileService(requestContext);
        File file = fileService.getFile(ObjectTypes.ISSUE, issueId, fileId);
        file.setConfigRepositoryPath(ConfigManager.file.getIssueFileRepositoryLocation());
        file.setConfigUploadedFilePrefix(ConfigManager.file.getIssueUploadedFilePrefix());
        return file;
    }

    public ActionMessages addIssueSimple(Issue issue) throws DatabaseException {
        ActionMessages errors = new ActionMessages();

        if (issue.getSubject().isEmpty()) {
            errors.add("emptySubject", new ActionMessage("common.form.fieldRequired",
                    Localizer.getText(requestContext, "issueMgmt.colName.issue_name")));

        } else if (issue.getSubject().length() > Schema.IssueTable.ISSUE_NAME_MAX_LEN) {
            String fieldName = Localizer.getText(requestContext, "issueMgmt.colName.issue_name");
            errors.add("subjectMaxLength", new ActionMessage("common.form.fieldExceededMaxLen", new Object[]{fieldName, Schema.IssueTable.ISSUE_NAME_MAX_LEN}));
        }

        if (issue.getDescription().isEmpty()) {
            errors.add("emptyDescription", new ActionMessage("common.form.fieldRequired",
                    Localizer.getText(requestContext, "issueMgmt.colName.issue_description")));

        } else if (issue.getDescription().length() > Schema.IssueTable.ISSUE_DESCRIPTION_MAX_LEN) {
            String fieldName = Localizer.getText(requestContext, "issueMgmt.colName.issue_description");
            errors.add("subjectMaxLength", new ActionMessage("common.form.fieldExceededMaxLen", new Object[]{fieldName, Schema.IssueTable.ISSUE_DESCRIPTION_MAX_LEN}));
        }
        if (!errors.isEmpty()) {
            return errors;
        }

        IssueDao issueDao = new IssueDao(requestContext);
        return issueDao.addIssueSimple(issue);
    }

    public List<EmailMessage> retrieveIssueEmails(PopConnection conn) throws Exception {
        return Pop.receive(conn, this);
    }

    public ActionMessages retrieveIssueEmail(EmailMessage message) throws Exception {
        ActionMessages errors = new ActionMessages();

        // If the body field is empty, don't update. But there is also not much error message to return.
        if (message.getBodyField().isEmpty()) {
            return errors;
        }

        Integer issueId = IssueEmailParser.parseEmailIssueId(message.getSubjectField());

        RequestContext requestContext = new RequestContext();

        AdminService adminService = ServiceProvider.getAdminService(requestContext);
        Integer userId = adminService.getUserIdByEmail(message.getFromField());
        if (userId != null) {
            requestContext.setUser(new AccessUser(userId));
        }

        if (issueId == null) {
            // Add issue
            Issue issue = new Issue();
            issue.setSubject(message.getSubjectField().isEmpty() ?
                Localizer.getText(requestContext, "issues.email.emptySubject") : message.getSubjectField());

            issue.setDescription(message.getBodyField());
            issue.setFromEmail(message.getFromField());

            IssueDao issueDao = new IssueDao(requestContext);
            errors = issueDao.addIssueSimple(issue);

        } else {
            // Update an existing issue
            Issue issue = getPublicIssue(issueId);
            issue.setFollowup(message.getBodyField());
            issue.setFromEmail(message.getFromField());

            IssueDao issueDao = new IssueDao(requestContext);
            boolean updateSubscribers = false;
            issueDao.update(requestContext, issue, updateSubscribers);
        }
        return errors;
    }

    public ActionMessages addIssue(Issue issue, Map<Integer, Attribute> customAttributes) throws DatabaseException {
        ActionMessages errors = new ActionMessages();

        if (issue.getSubject().isEmpty()) {
            errors.add("emptySubject", new ActionMessage("common.form.fieldRequired",
                    Localizer.getText(requestContext, "issueMgmt.colName.issue_name")));

        } else if (issue.getSubject().length() > Schema.IssueTable.ISSUE_NAME_MAX_LEN) {
            String fieldName = Localizer.getText(requestContext, "issueMgmt.colName.issue_name");
            errors.add("subjectMaxLength", new ActionMessage("common.form.fieldExceededMaxLen", new Object[]{fieldName, Schema.IssueTable.ISSUE_NAME_MAX_LEN}));
        }

        if (issue.getDescription().isEmpty()) {
            errors.add("emptyDescription", new ActionMessage("common.form.fieldRequired",
                    Localizer.getText(requestContext, "issueMgmt.colName.issue_description")));
        }
        if (issue.isHasDueDate() && !issue.isValidDueDate()) {
            errors.add("invalidDueDate", new ActionMessage("common.form.fieldDateInvalid",
                    Localizer.getText(requestContext, "issueMgmt.colName.issue_due_date")));
        }

        // Validate attributes
        AdminUtils.validateAttributeValues(requestContext, errors, issue, customAttributes);

        if (!errors.isEmpty()) {
            return errors;
        }

        IssueDao issueDao = new IssueDao(requestContext);
        return issueDao.add(requestContext, issue);
    }

    public ActionMessages updateIssue(Issue issue, Map<Integer, Attribute> customAttributes,
                                      boolean updateSubscribers) throws DatabaseException {

        ActionMessages errors = new ActionMessages();

        if (issue.getSubject().isEmpty()) {
            errors.add("emptySubject", new ActionMessage("common.form.fieldRequired",
                    Localizer.getText(requestContext, "issueMgmt.colName.issue_name")));

        } else if (issue.getSubject().length() > Schema.IssueTable.ISSUE_NAME_MAX_LEN) {
            String fieldName = Localizer.getText(requestContext, "issueMgmt.colName.issue_name");
            errors.add("subjectMaxLength", new ActionMessage("common.form.fieldExceededMaxLen", new Object[]{fieldName, Schema.IssueTable.ISSUE_NAME_MAX_LEN}));
        }

        if (issue.getFollowup().isEmpty()) {
            errors.add("emptyFollowup", new ActionMessage("common.form.fieldRequired",
                    Localizer.getText(requestContext, "issueMgmt.colName.comment")));
        }
        if (issue.isHasDueDate() && !issue.isValidDueDate()) {
            errors.add("invalidDueDate", new ActionMessage("common.form.fieldDateInvalid",
                    Localizer.getText(requestContext, "issueMgmt.colName.issue_due_date")));
        }

        // Validate attributes
        AdminUtils.validateAttributeValues(requestContext, errors, issue, customAttributes);

        if (!errors.isEmpty()) {
            return errors;
        }

        IssueDao issueDao = new IssueDao(requestContext);
        return issueDao.update(requestContext, issue, updateSubscribers);
    }

    public ActionMessages deleteIssue(Integer issueId) throws DatabaseException {
        /**
         * Here is what i can do. First, collect a list of file names to be deleted (probably in a dataset).
         * Then, delete the object, which would also delete the File records. Next, delete the actual files.
         */
        List deleteFileList = getIssueFiles(new QueryBits(), issueId);

        IssueDao issueDao = new IssueDao(requestContext);
        ActionMessages errors = issueDao.delete(issueId);

        FileService fileService = ServiceProvider.getFileService(requestContext);

        // Delete actual files
        if (errors.isEmpty()) {
            fileService.bulkDelete(ConfigManager.file.getIssueFileRepositoryLocation(), deleteFileList);
        }

        return errors;
    }

    public ActionMessages addIssueFile(Issue issue, FileUploadForm actionForm) throws DatabaseException {
        FileService fileService = ServiceProvider.getFileService(requestContext);

        File file = new IssueFile(issue.getId());
        ActionMessages errors = fileService.addFile(file, actionForm);
        if (!errors.isEmpty()) {
            // We have error uploading the file, we're done.
            return errors;
        } else {
            // We get the uploaded file id, set it to fileId.
            issue.setFileId(file.getId());

            // File uploaded fine, add an entry in issue_history.
            IssueDao issueDao = new IssueDao(requestContext);
            return issueDao.addIssueFile(issue);
        }
    }
}
