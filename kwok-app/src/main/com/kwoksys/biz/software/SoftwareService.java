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
package com.kwoksys.biz.software;

import com.kwoksys.biz.ServiceProvider;
import com.kwoksys.biz.admin.core.AdminUtils;
import com.kwoksys.biz.admin.dto.Attribute;
import com.kwoksys.biz.contacts.ContactService;
import com.kwoksys.biz.contacts.dto.Contact;
import com.kwoksys.biz.contracts.ContractService;
import com.kwoksys.biz.contracts.dto.Contract;
import com.kwoksys.biz.files.FileService;
import com.kwoksys.biz.files.dto.File;
import com.kwoksys.biz.software.dao.SoftwareDao;
import com.kwoksys.biz.software.dto.Software;
import com.kwoksys.biz.software.dto.SoftwareBookmark;
import com.kwoksys.biz.software.dto.SoftwareLicense;
import com.kwoksys.biz.system.BookmarkService;
import com.kwoksys.biz.system.SystemService;
import com.kwoksys.biz.system.core.ObjectTypes;
import com.kwoksys.biz.system.dto.Bookmark;
import com.kwoksys.biz.system.dto.linking.ContractSoftwareLink;
import com.kwoksys.biz.system.dto.linking.ObjectLink;
import com.kwoksys.biz.system.dto.linking.SoftwareContactLink;
import com.kwoksys.biz.system.dto.linking.SoftwareIssueLink;
import com.kwoksys.biz.system.core.configs.ConfigManager;
import com.kwoksys.framework.connections.database.QueryBits;
import com.kwoksys.framework.exceptions.DatabaseException;
import com.kwoksys.framework.exceptions.ObjectNotFoundException;
import com.kwoksys.framework.properties.Localizer;
import com.kwoksys.framework.http.RequestContext;
import org.apache.struts.action.ActionMessage;
import org.apache.struts.action.ActionMessages;

import java.util.List;
import java.util.Map;

/**
 * SoftwareService
 */
public class SoftwareService {

    private RequestContext requestContext;

    public SoftwareService(RequestContext requestContext) {
        this.requestContext = requestContext;
    }

    // Software
    public List<Software> getSoftwareList(QueryBits query) throws DatabaseException {
        SoftwareDao softwareDao = new SoftwareDao(requestContext);
        return softwareDao.getSoftwareList(query, null);
    }

    public List<Software> getLinkedSoftwareList(QueryBits query, ObjectLink objectMap) throws DatabaseException {
        SoftwareDao softwareDao = new SoftwareDao(requestContext);
        return softwareDao.getLinkedSoftwareList(query, objectMap);
    }

    public int getSoftwareCount(QueryBits query) throws DatabaseException {
        return new SoftwareDao(requestContext).getCount(query);
    }

    public Software getSoftware(Integer softwareId) throws DatabaseException, ObjectNotFoundException {
        SoftwareDao softwareDao = new SoftwareDao(requestContext);
        return softwareDao.getSoftware(softwareId);
    }

    public List<Map> getCompanySoftwareList(QueryBits query, Integer companyId) throws DatabaseException {
        SoftwareDao softwareDao = new SoftwareDao(requestContext);
        return softwareDao.getCompanySoftwareList(query, companyId);
    }

    public List<Map> getSoftwareCountGroupByCompany(QueryBits query) throws DatabaseException {
        SoftwareDao softwareDao = new SoftwareDao(requestContext);
        return softwareDao.getSoftwareCountGroupByCompany(query);
    }

    public ActionMessages addSoftware(Software software, Map<Integer, Attribute> customAttributes) throws DatabaseException {
        ActionMessages errors = new ActionMessages();

        if (software.getName().isEmpty()) {
            errors.add("emptyName", new ActionMessage("common.form.fieldRequired",
                    Localizer.getText(requestContext, "common.column.software_name")));

        } else if (software.getName().length() > SoftwareUtils.SOFTWARE_NAME_MAX_LEN) {
            String fieldName = Localizer.getText(requestContext, "common.column.software_name");
            errors.add("nameMaxLength", new ActionMessage("common.form.fieldExceededMaxLen", new Object[]{fieldName, SoftwareUtils.SOFTWARE_NAME_MAX_LEN}));
        }

        if (software.hasExpireDate() && !software.isValidExpireDate()) {
            errors.add("invalidExpireDate", new ActionMessage("common.form.fieldDateInvalid",
                    Localizer.getText(requestContext, "common.column.software_expire_date")));
        }

        // Validate attributes
        AdminUtils.validateAttributeValues(requestContext, errors, software, customAttributes);

        if (!errors.isEmpty()) {
            return errors;
        }

        SoftwareDao softwareDao = new SoftwareDao(requestContext);
        return softwareDao.add(software);
    }

    public ActionMessages updateSoftware(Software software, Map<Integer, Attribute> customAttributes) throws DatabaseException {
        ActionMessages errors = new ActionMessages();

        // Check inputs
        if (software.getName().isEmpty()) {
            errors.add("emptyName", new ActionMessage("common.form.fieldRequired",
                    Localizer.getText(requestContext, "common.column.software_name")));

        } else if (software.getName().length() > SoftwareUtils.SOFTWARE_NAME_MAX_LEN) {
            String fieldName = Localizer.getText(requestContext, "common.column.software_name");
            errors.add("nameMaxLength", new ActionMessage("common.form.fieldExceededMaxLen", new Object[]{fieldName, SoftwareUtils.SOFTWARE_NAME_MAX_LEN}));
        }

        if (software.hasExpireDate() && !software.isValidExpireDate()) {
            errors.add("invalidExpireDate", new ActionMessage("common.form.fieldDateInvalid",
                    Localizer.getText(requestContext, "common.column.software_expire_date")));
        }

        // Validate attributes
        AdminUtils.validateAttributeValues(requestContext, errors, software, customAttributes);

        if (!errors.isEmpty()) {
            return errors;
        }

        SoftwareDao softwareDao = new SoftwareDao(requestContext);
        return softwareDao.update(software);
    }

    public ActionMessages deleteSoftware(Software software) throws DatabaseException {
        /**
         * Here is what i can do. First, collect a list of file names to be deleted (probably in a dataset).
         * Then, delete the Software, which would also delete the File records. Next, delete the actual files.
         */
        List<File> deleteFileList = getSoftwareFiles(new QueryBits(), software.getId());

        // Delete file records
        SoftwareDao softwareDao = new SoftwareDao(requestContext);
        ActionMessages errors = softwareDao.delete(software);

        FileService fileService = ServiceProvider.getFileService(requestContext);

        // Delete actual files
        if (errors.isEmpty()) {
            fileService.bulkDelete(ConfigManager.file.getSoftwareFileRepositoryLocation(), deleteFileList);
        }
        return errors;
    }

    // Software licenses
    public List getSoftwareLicenses(QueryBits query, Integer softwareId) throws DatabaseException {
        SoftwareDao softwareDao = new SoftwareDao(requestContext);
        return softwareDao.getLicenseList(query, softwareId);
    }

    public SoftwareLicense getSoftwareLicense(Integer softwareId, Integer licenseId)
            throws DatabaseException, ObjectNotFoundException {
        SoftwareDao softwareDao = new SoftwareDao(requestContext);
        return softwareDao.getSoftwareLicense(softwareId, licenseId);
    }

    public Map getSoftwareLicenseCount(Integer softwareId) throws DatabaseException {
        SoftwareDao softwareDao = new SoftwareDao(requestContext);
        return softwareDao.getSoftwareLicenseCount(softwareId);
    }

    public List getSoftwareLicenseHardwareList(QueryBits query, Integer softwareId) throws DatabaseException {
        SoftwareDao softwareDao = new SoftwareDao(requestContext);
        return softwareDao.getSoftwareLicenseHardwareList(query, softwareId);
    }

    public ActionMessages addLicense(SoftwareLicense license, Map<Integer, Attribute> customAttributes) throws DatabaseException {
        ActionMessages errors = new ActionMessages();

        // Check inputs
        if (license.getKey().isEmpty()) {
            errors.add("emptyLicenseKey", new ActionMessage("common.form.fieldRequired",
                    Localizer.getText(requestContext, "common.column.license_key")));
        }
        if (!license.isCorrectLicenseEntitlementFormat()) {
            errors.add("emptyLicenseEntitlement", new ActionMessage("itMgmt.softwareDetail.error.emptyLicenseEntitlement"));
        }

        // Validate attributes
        AdminUtils.validateAttributeValues(requestContext, errors, license, customAttributes);

        if (!errors.isEmpty()) {
            return errors;
        }

        SoftwareDao softwareDao = new SoftwareDao(requestContext);
        return softwareDao.addLicense(requestContext, license);
    }

    public ActionMessages updateLicense(SoftwareLicense license, Map<Integer, Attribute> customAttributes) throws DatabaseException {
        ActionMessages errors = new ActionMessages();

        // Check inputs
        if (license.getKey().isEmpty()) {
            errors.add("emptyLicenseKey", new ActionMessage("common.form.fieldRequired",
                    Localizer.getText(requestContext, "common.column.license_key")));
        }
        if (!license.isCorrectLicenseEntitlementFormat()) {
            errors.add("emptyLicenseEntitlement", new ActionMessage("itMgmt.softwareDetail.error.emptyLicenseEntitlement"));
        }

        // Validate attributes
        AdminUtils.validateAttributeValues(requestContext, errors, license, customAttributes);

        if (!errors.isEmpty()) {
            return errors;
        }

        SoftwareDao softwareDao = new SoftwareDao(requestContext);
        return softwareDao.updateLicense(requestContext, license);
    }

    public ActionMessages deleteLicense(SoftwareLicense license) throws DatabaseException {
        ActionMessages errors = new ActionMessages();

        // Check inputs
        if (license.getId() == 0) {
            errors.add("emptySoftwareLicenseId", new ActionMessage("itMgmt.softwareDetail.error.emptySoftwareLicenseId"));
        }
        if (!errors.isEmpty()) {
            return errors;
        }

        SoftwareDao softwareDao = new SoftwareDao(requestContext);
        return softwareDao.deleteLicense(license);
    }

    public ActionMessages resetSoftwareLicenseCount(SoftwareLicense license) throws DatabaseException {
        SoftwareDao softwareDao = new SoftwareDao(requestContext);
        return softwareDao.resetSoftwareLicenseCount(license);
    }

    // Software files
    public List<File> getSoftwareFiles(QueryBits query, Integer softwareId) throws DatabaseException {
        FileService fileService = ServiceProvider.getFileService(requestContext);
        return fileService.getFiles(query, ObjectTypes.SOFTWARE, softwareId);
    }

    public ActionMessages resetSoftwareFileCount(Integer softwareId) throws DatabaseException {
        SoftwareDao softwareDao = new SoftwareDao(requestContext);
        return softwareDao.resetFileCount(softwareId);
    }

    public File getSoftwareFile(Integer softwareId, Integer fileId) throws DatabaseException, ObjectNotFoundException {
        FileService fileService = ServiceProvider.getFileService(requestContext);
        File file = fileService.getFile(ObjectTypes.SOFTWARE, softwareId, fileId);
        file.setConfigRepositoryPath(ConfigManager.file.getSoftwareFileRepositoryLocation());
        file.setConfigUploadedFilePrefix(ConfigManager.file.getSoftwareUploadedFilePrefix());
        return file;
    }

    // Software bookmarks
    public List<Bookmark> getSoftwareBookmarks(QueryBits query, Integer softwareId) throws DatabaseException {
        BookmarkService bookmarkService = ServiceProvider.getBookmarkService(requestContext);
        return bookmarkService.getBookmarks(query, ObjectTypes.SOFTWARE, softwareId);
    }

    public Bookmark getSoftwareBookmark(Integer softwareId, Integer bookmarkId) throws DatabaseException,
            ObjectNotFoundException {

        BookmarkService bookmarkService = ServiceProvider.getBookmarkService(requestContext);
        return bookmarkService.getBookmark(ObjectTypes.SOFTWARE, softwareId, bookmarkId);
    }

    public ActionMessages addSoftwareBookmark(SoftwareBookmark bookmark) throws DatabaseException {
        BookmarkService bookmarkService = ServiceProvider.getBookmarkService(requestContext);
        return bookmarkService.addBookmark(bookmark);
    }

    public ActionMessages updateSoftwareBookmark(Bookmark bookmark) throws DatabaseException {
        BookmarkService bookmarkService = ServiceProvider.getBookmarkService(requestContext);
        return bookmarkService.updateBookmark(bookmark);
    }

    public ActionMessages deleteSoftwareBookmark(Bookmark bookmark) throws DatabaseException {
        BookmarkService bookmarkService = ServiceProvider.getBookmarkService(requestContext);
        return bookmarkService.deleteBookmark(bookmark);
    }

    public ActionMessages resetSoftwareBookmarkCount(Integer softwareId) throws DatabaseException {
        SoftwareDao softwareDao = new SoftwareDao(requestContext);
        return softwareDao.resetBookmarkCount(softwareId);
    }

    /**
     * Add Software Issue map.
     * @param softwareIssue
     * @return
     * @throws DatabaseException
     */
    public ActionMessages addSoftwareIssue(SoftwareIssueLink softwareIssue) throws DatabaseException {
        SystemService systemService = ServiceProvider.getSystemService(requestContext);
        return systemService.addObjectMapping(softwareIssue.createObjectMap());
    }

    /**
     * Delete Software Issue map.
     * @param softwareIssue
     * @return
     * @throws DatabaseException
     */
    public ActionMessages deleteSoftwareIssue(SoftwareIssueLink softwareIssue) throws DatabaseException {
        SystemService systemService = ServiceProvider.getSystemService(requestContext);
        return systemService.deleteObjectMapping(softwareIssue.createObjectMap());
    }

    // Software Contacts
    public List<Contact> getSoftwareContacts(QueryBits query, Integer softwareId) throws DatabaseException {
        SoftwareContactLink contactLink = new SoftwareContactLink();
        contactLink.setSoftwareId(softwareId);

        ContactService contactService = ServiceProvider.getContactService(requestContext);
        return contactService.getLinkedContacts(query, contactLink.createObjectMap());
    }

    public ActionMessages addSoftwareContact(SoftwareContactLink softwareContact) throws DatabaseException {
        SystemService systemService = ServiceProvider.getSystemService(requestContext);
        return systemService.addObjectMapping(softwareContact.createObjectMap());
    }

    public ActionMessages deleteSoftwareContact(SoftwareContactLink softwareContact) throws DatabaseException {
        SystemService systemService = ServiceProvider.getSystemService(requestContext);
        return systemService.deleteObjectMapping(softwareContact.createObjectMap());
    }

    /**
     * Given a software id, get linked contracts.
     * @param query
     * @param softwareId
     * @return
     * @throws com.kwoksys.framework.exceptions.DatabaseException
     */
    public List<Contract> getSoftwareContracts(QueryBits query, Integer softwareId) throws DatabaseException {
        ContractSoftwareLink contractMap = new ContractSoftwareLink();
        contractMap.setSoftwareId(softwareId);

        ContractService contractService = ServiceProvider.getContractService(requestContext);
        return contractService.getLinkedContracts(query, contractMap.createObjectMap());
    }
}
