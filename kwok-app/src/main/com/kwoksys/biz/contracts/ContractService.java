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
package com.kwoksys.biz.contracts;

import com.kwoksys.biz.ServiceProvider;
import com.kwoksys.biz.admin.core.AdminUtils;
import com.kwoksys.biz.admin.dto.Attribute;
import com.kwoksys.biz.contacts.ContactService;
import com.kwoksys.biz.contacts.dto.Contact;
import com.kwoksys.biz.contracts.dao.ContractDao;
import com.kwoksys.biz.contracts.dto.Contract;
import com.kwoksys.biz.files.FileService;
import com.kwoksys.biz.files.dto.File;
import com.kwoksys.biz.hardware.HardwareService;
import com.kwoksys.biz.hardware.dto.Hardware;
import com.kwoksys.biz.software.SoftwareService;
import com.kwoksys.biz.software.dto.Software;
import com.kwoksys.biz.system.SystemService;
import com.kwoksys.biz.system.dto.linking.ContractContactLink;
import com.kwoksys.biz.system.dto.linking.ContractHardwareLink;
import com.kwoksys.biz.system.dto.linking.ContractSoftwareLink;
import com.kwoksys.biz.system.dto.linking.ObjectLink;
import com.kwoksys.biz.system.core.configs.ConfigManager;
import com.kwoksys.framework.connections.database.QueryBits;
import com.kwoksys.framework.exceptions.DatabaseException;
import com.kwoksys.framework.exceptions.ObjectNotFoundException;
import com.kwoksys.framework.properties.Localizer;
import com.kwoksys.biz.system.core.ObjectTypes;
import com.kwoksys.biz.system.core.Schema;
import com.kwoksys.framework.http.RequestContext;
import org.apache.struts.action.ActionMessage;
import org.apache.struts.action.ActionMessages;

import java.util.List;
import java.util.Map;

/**
 * ContractService.
 */
public class ContractService {

    private RequestContext requestContext;

    public ContractService(RequestContext requestContext) {
        this.requestContext = requestContext;
    }

    public List<Contract> getContracts(QueryBits query) throws DatabaseException {
        return new ContractDao(requestContext).getContracts(query, null);
    }

    public List<Contract> getLinkedContracts(QueryBits query, ObjectLink objectMap) throws DatabaseException {
        return new ContractDao(requestContext).getLinkedContracts(query, objectMap);
    }

    public int getContractCount(QueryBits query) throws DatabaseException {
        return new ContractDao(requestContext).getContractCount(query);
    }

    public Contract getContract(Integer contractId) throws DatabaseException, ObjectNotFoundException {
        return new ContractDao(requestContext).getContract(contractId);
    }

    public List<Hardware> getContractHardwareList(QueryBits query, Integer contractId) throws DatabaseException {
        ContractHardwareLink contractHardware = new ContractHardwareLink(contractId);

        HardwareService hardwareService = ServiceProvider.getHardwareService(requestContext);
        return hardwareService.getLinkedHardwareList(query, contractHardware.createObjectMap());
    }

    public List<Software> getContractSoftwareList(QueryBits query, Integer contractId) throws DatabaseException {
        ContractSoftwareLink contractSoftware = new ContractSoftwareLink(contractId);

        SoftwareService softwareService = ServiceProvider.getSoftwareService(requestContext);
        return softwareService.getLinkedSoftwareList(query, contractSoftware.createObjectMap());
    }

    public List getContractsSummary() throws DatabaseException {
        ContractDao contractDao = new ContractDao(requestContext);
        return contractDao.getContractsSummary();
    }

    public ActionMessages addContract(Contract contract, Map<Integer, Attribute> customAttributes) throws DatabaseException {
        ActionMessages errors = new ActionMessages();

        // Check inputs
        if (contract.getName().isEmpty()) {
            errors.add("emptyName", new ActionMessage("common.form.fieldRequired",
                    Localizer.getText(requestContext, "common.column.contract_name")));

        } else if (contract.getName().length() > Schema.ContractTable.CONTRACT_NAME_MAX_LEN) {
            String fieldName = Localizer.getText(requestContext, "common.column.contract_name");
            errors.add("nameMaxLength", new ActionMessage("common.form.fieldExceededMaxLen", new Object[]{fieldName, Schema.ContractTable.CONTRACT_NAME_MAX_LEN}));
        }
        if (contract.hasContractEffectiveDate() && !contract.isValidContractEffectiveDate()) {
            errors.add("invalidEffectiveDate", new ActionMessage("common.form.fieldDateInvalid",
                    Localizer.getText(requestContext, "common.column.contract_effective_date")));
        }
        if (contract.hasContractExpirationDate() && !contract.isValidContractExpirationDate()) {
            errors.add("invalidExpirationDate", new ActionMessage("common.form.fieldDateInvalid",
                    Localizer.getText(requestContext, "common.column.contract_expiration_date")));
        }
        if (contract.hasContractRenewalDate() && !contract.isValidContractRenewalDate()) {
            errors.add("invalidRenewalDate", new ActionMessage("common.form.fieldDateInvalid",
                    Localizer.getText(requestContext, "common.column.contract_renewal_date")));
        }

        // Validate attributes
        AdminUtils.validateAttributeValues(requestContext, errors, contract, customAttributes);

        if (!errors.isEmpty()) {
            return errors;
        }

        ContractDao contractDao = new ContractDao(requestContext);
        return contractDao.addContract(contract);
    }

    public ActionMessages updateContract(Contract contract, Map<Integer, Attribute> customAttributes) throws DatabaseException {
        ActionMessages errors = new ActionMessages();

        // Check inputs
        if (contract.getName().isEmpty()) {
            errors.add("emptyName", new ActionMessage("common.form.fieldRequired",
                    Localizer.getText(requestContext, "common.column.contract_name")));

        } else if (contract.getName().length() > Schema.ContractTable.CONTRACT_NAME_MAX_LEN) {
            String fieldName = Localizer.getText(requestContext, "common.column.contract_name");
            errors.add("nameMaxLength", new ActionMessage("common.form.fieldExceededMaxLen", new Object[]{fieldName, Schema.ContractTable.CONTRACT_NAME_MAX_LEN}));
        }
        if (contract.hasContractEffectiveDate() && !contract.isValidContractEffectiveDate()) {
            errors.add("invalidEffectiveDate", new ActionMessage("common.form.fieldDateInvalid",
                    Localizer.getText(requestContext, "common.column.contract_effective_date")));
        }
        if (contract.hasContractExpirationDate() && !contract.isValidContractExpirationDate()) {
            errors.add("invalidExpirationDate", new ActionMessage("common.form.fieldDateInvalid",
                    Localizer.getText(requestContext, "common.column.contract_expiration_date")));
        }
        if (contract.hasContractRenewalDate() && !contract.isValidContractRenewalDate()) {
            errors.add("invalidRenewalDate", new ActionMessage("common.form.fieldDateInvalid",
                    Localizer.getText(requestContext, "common.column.contract_renewal_date")));
        }

        // Validate attributes
        AdminUtils.validateAttributeValues(requestContext, errors, contract, customAttributes);

        if (!errors.isEmpty()) {
            return errors;
        }

        ContractDao contractDao = new ContractDao(requestContext);
        return contractDao.updateContract(contract);
    }

    public ActionMessages deleteContract(Integer contractId) throws DatabaseException {
        /**
         * Here is what i can do. First, collect a list of file names to be deleted (probably in a dataset).
         * Then, delete the object, which would also delete the File records. Next, delete the actual files.
         */
        List<File> deleteFileList = getContractFiles(new QueryBits(), contractId);

        ContractDao contractDao = new ContractDao(requestContext);
        ActionMessages errors = contractDao.delete(contractId);

        FileService fileService = ServiceProvider.getFileService(requestContext);

        // Delete actual files
        if (errors.isEmpty()) {
            fileService.bulkDelete(ConfigManager.file.getContractFileRepositoryLocation(), deleteFileList);
        }

        return errors;
    }

    /**
     * Add Contract Hardware map.
     * @param contractHardware
     * @return
     * @throws DatabaseException
     */
    public ActionMessages addContractHardware(ContractHardwareLink contractHardware) throws DatabaseException {
        ActionMessages errors = new ActionMessages();

        // Check inputs
        if (contractHardware.getHardwareId() == 0) {
            errors.add("emptyHardwareId", new ActionMessage("common.form.fieldRequired",
                    Localizer.getText(requestContext, "common.column.hardware_id")));
        }
        if (!errors.isEmpty()) {
            return errors;
        }
        SystemService systemService = ServiceProvider.getSystemService(requestContext);
        return systemService.addObjectMapping(contractHardware.createObjectMap());
    }

    /**
     * Delete Contract Hardware map.
     * @param contractHardware
     * @return
     * @throws DatabaseException
     */
    public ActionMessages deleteContractHardware(ContractHardwareLink contractHardware) throws DatabaseException {
        SystemService systemService = ServiceProvider.getSystemService(requestContext);
        return systemService.deleteObjectMapping(contractHardware.createObjectMap());
    }

    public ActionMessages addContractSoftware(ContractSoftwareLink contractSoftware) throws DatabaseException {
        ActionMessages errors = new ActionMessages();

        // Check inputs
        if (contractSoftware.getSoftwareId() == 0) {
            errors.add("emptySoftwareId", new ActionMessage("common.form.fieldRequired",
                    Localizer.getText(requestContext, "common.column.software_id")));
        }
        if (!errors.isEmpty()) {
            return errors;
        }
        SystemService systemService = ServiceProvider.getSystemService(requestContext);
        return systemService.addObjectMapping(contractSoftware.createObjectMap());
    }

    /**
     * Delete Contract Software map.
     * @param contractSoftware
     * @return
     * @throws DatabaseException
     */
    public ActionMessages deleteContractSoftware(ContractSoftwareLink contractSoftware) throws DatabaseException {
        SystemService systemService = ServiceProvider.getSystemService(requestContext);
        return systemService.deleteObjectMapping(contractSoftware.createObjectMap());
    }

    public List<File> getContractFiles(QueryBits query, Integer contractId) throws DatabaseException {
        FileService fileService = ServiceProvider.getFileService(requestContext);
        return fileService.getFiles(query, ObjectTypes.CONTRACT, contractId);
    }

    public File getContractFile(Integer contractId, Integer fileId) throws DatabaseException, ObjectNotFoundException {
        FileService fileService = ServiceProvider.getFileService(requestContext);
        File file = fileService.getFile(ObjectTypes.CONTRACT, contractId, fileId);
        file.setConfigRepositoryPath(ConfigManager.file.getContractFileRepositoryLocation());
        file.setConfigUploadedFilePrefix(ConfigManager.file.getContractUploadedFilePrefix());
        return file;
    }

    /**
     * Contract contacts
     * @param query
     * @param contractId
     * @return
     * @throws DatabaseException
     */
    public List<Contact> getContractContacts(QueryBits query, Integer contractId) throws DatabaseException {
        ContractContactLink link = new ContractContactLink();
        link.setContractId(contractId);

        ContactService contactService = ServiceProvider.getContactService(requestContext);
        return contactService.getLinkedContacts(query, link.createObjectMap());
    }

    public ActionMessages addContractContact(ContractContactLink contactLink) throws DatabaseException {
        SystemService systemService = ServiceProvider.getSystemService(requestContext);
        return systemService.addObjectMapping(contactLink.createObjectMap());
    }

    public ActionMessages deleteContractContact(ContractContactLink contactLink) throws DatabaseException {
        SystemService systemService = ServiceProvider.getSystemService(requestContext);
        return systemService.deleteObjectMapping(contactLink.createObjectMap());
    }
}
