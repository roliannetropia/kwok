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
package com.kwoksys.biz.contacts;

import com.kwoksys.biz.ServiceProvider;
import com.kwoksys.biz.admin.core.AdminUtils;
import com.kwoksys.biz.admin.core.AttributeSearch;
import com.kwoksys.biz.admin.dto.Attribute;
import com.kwoksys.biz.contacts.core.CompanySearch;
import com.kwoksys.biz.contacts.dao.*;
import com.kwoksys.biz.contacts.dto.Company;
import com.kwoksys.biz.contacts.dto.CompanyBookmark;
import com.kwoksys.biz.contacts.dto.CompanyNote;
import com.kwoksys.biz.contacts.dto.Contact;
import com.kwoksys.biz.files.FileService;
import com.kwoksys.biz.files.dto.File;
import com.kwoksys.biz.system.BookmarkService;
import com.kwoksys.biz.system.SystemService;
import com.kwoksys.biz.system.core.Attributes;
import com.kwoksys.framework.properties.Localizer;
import com.kwoksys.biz.system.core.ObjectTypes;
import com.kwoksys.biz.system.core.Schema;
import com.kwoksys.biz.system.dto.Bookmark;
import com.kwoksys.biz.system.dto.linking.CompanyIssueLink;
import com.kwoksys.biz.system.dto.linking.ObjectLink;
import com.kwoksys.framework.http.RequestContext;
import com.kwoksys.biz.system.core.configs.ConfigManager;
import com.kwoksys.framework.connections.database.QueryBits;
import com.kwoksys.framework.exceptions.DatabaseException;
import com.kwoksys.framework.exceptions.ObjectNotFoundException;
import org.apache.struts.action.ActionMessage;
import org.apache.struts.action.ActionMessages;

import java.util.List;
import java.util.Map;

/**
 * ContactServiceImpl
 */
public class ContactService {

    private RequestContext requestContext;

    public ContactService(RequestContext requestContext) {
        this.requestContext = requestContext;
    }

    // Company
    public List<Company> getCompanies(QueryBits query) throws DatabaseException {
        return new CompanyDao(requestContext).getList(query, null);
    }

    public int getCompanyCount(QueryBits query) throws DatabaseException {
        return new CompanyDao(requestContext).getCount(query);
    }

    public Company getCompany(Integer companyId) throws DatabaseException, ObjectNotFoundException {
        return new CompanyDao(requestContext).getCompany(companyId);
    }

    public List<Integer> getCompanyTypes(Integer companyId) throws DatabaseException {
        AttributeSearch attributeSearch = new AttributeSearch();
        attributeSearch.put(AttributeSearch.ATTRIBUTE_ID_EQUALS, Attributes.COMPANY_TYPE);
        attributeSearch.put(AttributeSearch.OBJECT_ID_EQUALS, companyId);

        QueryBits query = new QueryBits(attributeSearch);

        return new CompanyDao(requestContext).getCompanyTypes(query);
    }

    private void validateCompanyTags(ActionMessages errors, Company company) {
        // We only accept letters, numbers, commas, and spaces.
        if (!company.getTags().isEmpty()) {
            if (!company.isValidTagFormat()) {
                errors.add("invalidTagFormat", new ActionMessage("contactMgmt.companyAdd.error.invalidTagFormat"));

            } else if (company.getCompanyTagsCommaSeparated().length > company.numTagsAllowed()) {
                errors.add("tooManyTags", new ActionMessage("contactMgmt.companyAdd.error.tooManyTags"));
            }
        }
    }

    public ActionMessages addCompany(Company company, Map<Integer, Attribute> customAttributes) throws DatabaseException {
        ActionMessages errors = new ActionMessages();

        // Check inputs
        if (company.getName().isEmpty()) {
            errors.add("emptyName", new ActionMessage("common.form.fieldRequired",
                                Localizer.getText(requestContext, "common.column.company_name")));

        } else if (company.getName().length() > Schema.CompanyTable.COMPANY_NAME_MAX_LEN) {
            String fieldName = Localizer.getText(requestContext, "common.column.company_name");
            errors.add("nameMaxLength", new ActionMessage("common.form.fieldExceededMaxLen", new Object[]{fieldName, Schema.CompanyTable.COMPANY_NAME_MAX_LEN}));
        }

        validateCompanyTags(errors, company);

        // Validate attributes
        AdminUtils.validateAttributeValues(requestContext, errors, company, customAttributes);

        if (!errors.isEmpty()) {
            return errors;
        }

        return new CompanyDao(requestContext).add(company);
    }

    public ActionMessages updateCompany(Company company, Map<Integer, Attribute> customAttributes) throws DatabaseException {
        ActionMessages errors = new ActionMessages();

        // Check inputs
        if (company.getName().isEmpty()) {
            errors.add("emptyName", new ActionMessage("common.form.fieldRequired",
                                Localizer.getText(requestContext, "common.column.company_name")));

        } else if (company.getName().length() > Schema.CompanyTable.COMPANY_NAME_MAX_LEN) {
            String fieldName = Localizer.getText(requestContext, "common.column.company_name");
            errors.add("nameMaxLength", new ActionMessage("common.form.fieldExceededMaxLen", new Object[]{fieldName, Schema.CompanyTable.COMPANY_NAME_MAX_LEN}));
        }

        validateCompanyTags(errors, company);

        // Validate attributes
        AdminUtils.validateAttributeValues(requestContext, errors, company, customAttributes);

        if (!errors.isEmpty()) {
            return errors;
        }

        return new CompanyDao(requestContext).update(company);
    }

    public ActionMessages deleteCompany(Company company) throws DatabaseException {
        ActionMessages errors = new ActionMessages();

        // Check inputs
        if (company.getId() == 0) {
            errors.add("emptyCompanyId", new ActionMessage("contactMgmt.error.emptyCompanyId"));
        }
        if (!errors.isEmpty()) {
            return errors;
        }

        /**
         * Here is what i can do. First, collect a list of file names to be deleted (probably in a dataset).
         * Then, delete the Company, which would also delete the File records. Next, delete the actual files.
         */
        List<File> deleteFileList = getCompanyFiles(new QueryBits(), company.getId());

        // Delete records.
        errors = new CompanyDao(requestContext).delete(company);

        FileService fileService = ServiceProvider.getFileService(requestContext);

        // Delete actual files
        if (errors.isEmpty()) {
            fileService.bulkDelete(ConfigManager.file.getCompanyFileRepositoryLocation(), deleteFileList);
        }

        return errors;
    }

    public Company getSingleCompanyByName(String companyName) throws DatabaseException {
        CompanySearch companySearch = new CompanySearch();
        companySearch.put(CompanySearch.COMPANY_NAME_EQUALS, companyName);

        QueryBits queryBits = new QueryBits(companySearch);
        queryBits.setLimit(2, 0);

        List<Company> companyList = getCompanies(queryBits);

        if (companyList.size() == 1) {
            return companyList.get(0);
        } else {
            return null;
        }
    }
    
    // Company files
    public List<File> getCompanyFiles(QueryBits query, Integer companyId) throws DatabaseException {
        FileService fileService = ServiceProvider.getFileService(requestContext);
        return fileService.getFiles(query, ObjectTypes.COMPANY, companyId);
    }

    public ActionMessages resetCompanyFileCount(Integer companyId) throws DatabaseException {
        return new CompanyDao(requestContext).resetFileCount(companyId);
    }

    public File getCompanyFile(Integer companyId, Integer fileId) throws DatabaseException, ObjectNotFoundException {
        FileService fileService = ServiceProvider.getFileService(requestContext);
        File file = fileService.getFile(ObjectTypes.COMPANY, companyId, fileId);
        file.setConfigRepositoryPath(ConfigManager.file.getCompanyFileRepositoryLocation());
        file.setConfigUploadedFilePrefix(ConfigManager.file.getCompanyUploadedFilePrefix());
        return file;
    }

    // Company tags
    public List getCompanyTags(QueryBits query, Integer companyId) throws DatabaseException {
        return new CompanyDao(requestContext).getTagList(query, companyId);
    }

    public List getExistingCompanyTags(QueryBits query) throws DatabaseException {
        return new CompanyDao(requestContext).getExistingTagList(query);
    }

    // Company notes
    public List<CompanyNote> getCompanyNotes(QueryBits query, Integer companyId) throws DatabaseException {
        return new CompanyNoteDao(requestContext).getNoteList(query, companyId);
    }

    public ActionMessages addCompanyNote(CompanyNote note) throws DatabaseException {
        ActionMessages errors = new ActionMessages();

        // Check inputs
        if (note.getNoteName().isEmpty()) {
            errors.add("emptyName", new ActionMessage("common.form.fieldRequired",
                    Localizer.getText(requestContext, "common.column.company_note_name")));
        }

        if (!errors.isEmpty()) {
            return errors;
        }

        CompanyNoteDao companyNoteDao = new CompanyNoteDao(requestContext);
        return companyNoteDao.addNote(note);
    }

    public ActionMessages resetCompanyNoteCount(Integer companyId) throws DatabaseException {
        CompanyDao companyDao = new CompanyDao(requestContext);
        return companyDao.resetNoteCount(companyId);
    }

    // Company bookmark
    public List<Bookmark> getCompanyBookmarks(QueryBits query, Integer companyId) throws DatabaseException {
        BookmarkService bookmarkService = ServiceProvider.getBookmarkService(requestContext);
        return bookmarkService.getBookmarks(query, ObjectTypes.COMPANY, companyId);
    }

    public Bookmark getCompanyBookmark(Integer companyId, Integer bookmarkId) throws DatabaseException,
            ObjectNotFoundException {

        BookmarkService bookmarkService = ServiceProvider.getBookmarkService(requestContext);
        return bookmarkService.getBookmark(ObjectTypes.COMPANY, companyId, bookmarkId);
    }

    public ActionMessages addCompanyBookmark(CompanyBookmark bookmark) throws DatabaseException {
        BookmarkService bookmarkService = ServiceProvider.getBookmarkService(requestContext);
        return bookmarkService.addBookmark(bookmark);
    }

    public ActionMessages updateCompanyBookmark(Bookmark bookmark) throws DatabaseException {
        BookmarkService bookmarkService = ServiceProvider.getBookmarkService(requestContext);
        return bookmarkService.updateBookmark(bookmark);
    }

    public ActionMessages deleteCompanyBookmark(Bookmark bookmark) throws DatabaseException {
        BookmarkService bookmarkService = ServiceProvider.getBookmarkService(requestContext);
        return bookmarkService.deleteBookmark(bookmark);
    }

    public ActionMessages resetCompanyBookmarkCount(Integer companyId) throws DatabaseException {
        CompanyBookmarkDao companyBookmarkDao = new CompanyBookmarkDao(requestContext);
        return companyBookmarkDao.resetCount(companyId);
    }

    // Company contact
    public List<Contact> getCompanyContacts(QueryBits query, Integer companyId, Integer companyContactType)
            throws DatabaseException {

        return new CompanyContactDao(requestContext).getList(query, companyId, companyContactType);
    }

    public ActionMessages addCompanyContact(Contact contact) throws DatabaseException {
        ActionMessages errors = new ActionMessages();

        // Check inputs
        if (contact.getTitle().isEmpty()) {
            errors.add("emptyContactTitle", new ActionMessage("common.form.fieldRequired",
                    Localizer.getText(requestContext, "common.column.contact_main_label")));
        }
        if (contact.getCompanyId() == 0) {
            errors.add("emptyCompanyId", new ActionMessage("contactMgmt.companyContactEdit.error.emptyCompanyId"));
        }
        if (!errors.isEmpty()) {
            return errors;
        }

        return new CompanyContactDao(requestContext).addCompanyContact(contact);
    }

    public ActionMessages updateCompanyContact(Contact contact) throws DatabaseException {
        ActionMessages errors = new ActionMessages();

        // Check inputs
        if (contact.getTitle().isEmpty()) {
            errors.add("emptyContactTitle", new ActionMessage("common.form.fieldRequired",
                    Localizer.getText(requestContext, "common.column.contact_main_label")));
        }
        if (!errors.isEmpty()) {
            return errors;
        }

        return new CompanyContactDao(requestContext).updateCompanyContact(contact);
    }

    public ActionMessages addEmployeeContact(Contact contact) throws DatabaseException {
        ActionMessages errors = new ActionMessages();

        // Check inputs
        if (contact.getFirstName().isEmpty()) {
            errors.add("emptyContactFirstName", new ActionMessage("common.form.fieldRequired",
                    Localizer.getText(requestContext, "common.column.contact_first_name")));
        }
        if (contact.getLastName().isEmpty()) {
            errors.add("emptyContactLastName", new ActionMessage("common.form.fieldRequired",
                    Localizer.getText(requestContext, "common.column.contact_last_name")));
        }
        if (contact.getCompanyId() == 0) {
            errors.add("emptyCompanyId", new ActionMessage("contactMgmt.contactEdit.error.emptyCompanyId"));
        }
        if ((contact.getMessenger1Type() != 0 && contact.getMessenger1Id().isEmpty()) || (contact.getMessenger1Type() == 0 && !contact.getMessenger1Id().isEmpty()) || (contact.getMessenger2Type() != 0 && contact.getMessenger2Id().isEmpty()) || (contact.getMessenger2Type() == 0 && !contact.getMessenger2Id().isEmpty()))
        {
            errors.add("invalidMessengerInput", new ActionMessage("contactMgmt.contactEdit.error.invalidMessengerInput"));
        }
        if (!errors.isEmpty()) {
            return errors;
        }

        return new CompanyContactDao(requestContext).addEmployeeContact(contact);
    }

    public ActionMessages updateEmployeeContact(Contact contact) throws DatabaseException {
        ActionMessages errors = new ActionMessages();

        // Check inputs
        if (contact.getFirstName().isEmpty()) {
            errors.add("emptyContactFirstName", new ActionMessage("common.form.fieldRequired",
                    Localizer.getText(requestContext, "common.column.contact_first_name")));
        }
        if (contact.getLastName().isEmpty()) {
            errors.add("emptyContactLastName", new ActionMessage("common.form.fieldRequired",
                    Localizer.getText(requestContext, "common.column.contact_last_name")));
        }
        if ((contact.getMessenger1Type() != 0 && contact.getMessenger1Id().isEmpty()) || (contact.getMessenger1Type() == 0 && !contact.getMessenger1Id().isEmpty()) || (contact.getMessenger2Type() != 0 && contact.getMessenger2Id().isEmpty()) || (contact.getMessenger2Type() == 0 && !contact.getMessenger2Id().isEmpty()))
        {
            errors.add("invalidMessengerInput", new ActionMessage("contactMgmt.contactEdit.error.invalidMessengerInput"));
        }
        if (!errors.isEmpty()) {
            return errors;
        }

        CompanyContactDao companyContactDao = new CompanyContactDao(requestContext);
        return companyContactDao.updateEmployeeContact(contact);
    }

    /**
     * Add Company Issue map.
     * @param issueMap
     * @return
     * @throws DatabaseException
     */
    public ActionMessages addCompanyIssue(CompanyIssueLink issueMap) throws DatabaseException {
        SystemService systemService = ServiceProvider.getSystemService(requestContext);
        return systemService.addObjectMapping(issueMap.createObjectMap());
    }

    /**
     * Add Company Issue map.
     * @param issueMap
     * @return
     * @throws DatabaseException
     */
    public ActionMessages deleteCompanyIssue(CompanyIssueLink issueMap) throws DatabaseException {
        SystemService systemService = ServiceProvider.getSystemService(requestContext);
        return systemService.deleteObjectMapping(issueMap.createObjectMap());
    }

    // Contact
    public List<Contact> getContacts(QueryBits query) throws DatabaseException {
        return new ContactDao(requestContext).getContacts(query);
    }

    public List<Contact> getLinkedContacts(QueryBits query, ObjectLink objectMap) throws DatabaseException {
        return new ContactDao(requestContext).getLinkedContacts(query, objectMap);
    }

    public int getContactCount(QueryBits query) throws DatabaseException {
        return new ContactDao(requestContext).getCount(query);
    }

    public Contact getContact(Integer contactId) throws DatabaseException, ObjectNotFoundException {
        return new ContactDao(requestContext).getContact(contactId);
    }

    public Contact getOptionalContact(Integer contactId) throws DatabaseException, ObjectNotFoundException {
        if (contactId == 0) {
            return new Contact();
        } else {
            return new ContactDao(requestContext).getContact(contactId);
        }
    }

    public ActionMessages deleteContact(Contact contact) throws DatabaseException {
        ActionMessages errors = new ActionMessages();

        // Check inputs
        if (contact.getId() == 0) {
            errors.add("emptyContactId", new ActionMessage("contactMgmt.contactEdit.error.emptyContactId"));
        }
        if (!errors.isEmpty()) {
            return errors;
        }

        return new ContactDao(requestContext).delete(contact);
    }
}
