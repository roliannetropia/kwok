package com.kwoksys.biz.tape;

import com.kwoksys.biz.ServiceProvider;
import com.kwoksys.biz.admin.core.AdminUtils;
import com.kwoksys.biz.admin.dto.Attribute;
import com.kwoksys.biz.admin.dto.AttributeFieldCount;
import com.kwoksys.biz.contracts.ContractService;
import com.kwoksys.biz.contracts.dto.Contract;
import com.kwoksys.biz.files.FileService;
import com.kwoksys.biz.files.dto.File;

import com.kwoksys.biz.software.dto.SoftwareLicense;
import com.kwoksys.biz.system.SystemService;
import com.kwoksys.biz.system.core.ObjectTypes;
import com.kwoksys.biz.system.core.Schema;
import com.kwoksys.biz.system.core.configs.ConfigManager;
import com.kwoksys.biz.system.dto.linking.ContractTapeLink;
import com.kwoksys.biz.system.dto.linking.TapeIssueLink;
import com.kwoksys.biz.system.dto.linking.TapeMemberLink;
import com.kwoksys.biz.system.dto.linking.ObjectLink;
import com.kwoksys.biz.tape.core.TapeSearch;
import com.kwoksys.biz.tape.dao.TapeDao;
import com.kwoksys.biz.tape.dto.Tape;
import com.kwoksys.biz.tape.dto.TapeComponent;
import com.kwoksys.biz.tape.dto.TapeSoftwareMap;
import com.kwoksys.framework.connections.database.QueryBits;
import com.kwoksys.framework.exceptions.DatabaseException;
import com.kwoksys.framework.exceptions.ObjectNotFoundException;
import com.kwoksys.framework.http.RequestContext;
import com.kwoksys.framework.properties.Localizer;
import com.kwoksys.framework.util.StringUtils;
import org.apache.struts.action.ActionMessage;
import org.apache.struts.action.ActionMessages;

import java.util.List;
import java.util.Map;

/**
 * TapeService
 */
public class TapeService {

    private RequestContext requestContext;

    public TapeService(RequestContext requestContext) {
        this.requestContext = requestContext;
    }

    // Tape
    public List<Tape> getTapeList(QueryBits query) throws DatabaseException {
        return new TapeDao(requestContext).getTapeList(query);
    }

    public List<Tape> getLinkedTapeList(QueryBits query, ObjectLink objectMap) throws DatabaseException {
        return new TapeDao(requestContext).getLinkedTapeList(query, objectMap);
    }

    public int getTapeCount(QueryBits query) throws DatabaseException {
        return new TapeDao(requestContext).getCount(query);
    }

    public List<AttributeFieldCount> getMediaTypeCount(QueryBits query) throws DatabaseException {
        return new TapeDao(requestContext).getMediaTypeCount(query);
    }
//
//    public List<AttributeFieldCount> getTapeStatusCount(QueryBits query) throws DatabaseException {
//        return new TapeDao(requestContext).getTapeStatusCount(query);
//    }
//
//    public List<AttributeFieldCount> getTapeLocationCount(QueryBits query) throws DatabaseException {
//        return new TapeDao(requestContext).getTapeLocationCount(query);
//    }

    public Tape getTape(Integer tapeId) throws DatabaseException, ObjectNotFoundException {
        return new TapeDao(requestContext).getTape(tapeId);
    }

    public List<Tape> getTapeParents(QueryBits query, Integer tapeId) throws DatabaseException {
        TapeMemberLink memberMap = new TapeMemberLink();
        memberMap.setMemberTapeId(tapeId);

        return new TapeDao(requestContext).getLinkedTapeList(query, memberMap.createObjectMap());
    }

    /**
     * Get a list of Tape Members.
     * @param query
     * @param tapeId
     * @return
     * @throws com.kwoksys.framework.exceptions.DatabaseException
     */
//    public List<Tape> getTapeMembers(QueryBits query, Integer tapeId) throws DatabaseException {
//        TapeMemberLink memberMap = new TapeMemberLink(tapeId);
//        return new TapeDao(requestContext).getLinkedTapeList(query, memberMap.createObjectMap());
//    }

    /**
     * Given a tape id, get linked contracts.
//     * @param query
//     * @param tapeId
     * @return
     * @throws com.kwoksys.framework.exceptions.DatabaseException
     */
//    public List<Contract> getLinkedContracts(QueryBits query, Integer tapeId) throws DatabaseException {
//        ContractTapeLink contractMap = new ContractTapeLink();
//        contractMap.setTapeId(tapeId);
//
//        ContractService contractService = ServiceProvider.getContractService(requestContext);
//        return contractService.getLinkedContracts(query, contractMap.createObjectMap());
//    }

    public ActionMessages validateTape(Tape tape, Map<Integer, Attribute> customAttributes)
            throws DatabaseException {

        ActionMessages errors = new ActionMessages();

        // Check inputs
        if (StringUtils.isEmpty(tape.getTapeName())) {
            errors.add("emptyName", new ActionMessage("common.form.fieldRequired",
                    Localizer.getText(requestContext, "common.column.tape_name")));

        } else if (tape.getTapeName().length() > Schema.AssetTapeTable.TAPE_NAME_MAX_LEN) {
            String fieldName = Localizer.getText(requestContext, "common.column.tape_name");
            errors.add("nameMaxLength", new ActionMessage("common.form.fieldExceededMaxLen", new Object[]{fieldName, Schema.AssetTapeTable.TAPE_NAME_MAX_LEN}));

//        } else if (isDuplicatedTapeName(tape.getId(), tape.getName())) {
//            // Check unique name
//            errors.add("duplicatedName", new ActionMessage("itMgmt.tapeAdd.error.nameDuplicated", tape.getName()));
        }
//        if (tape.getModelName().length() > Schema.AssetTapeTable.TAPE_MODEL_NAME_MAX_LEN) {
//            String fieldName = Localizer.getText(requestContext, "common.column.tape_model_name");
//            errors.add("modelNameMaxLength", new ActionMessage("common.form.fieldExceededMaxLen", new Object[]{fieldName, Schema.AssetTapeTable.TAPE_MODEL_NAME_MAX_LEN}));
//        }
//        if (tape.getModelNumber().length() > Schema.AssetTapeTable.TAPE_MODEL_NUMBER_MAX_LEN) {
//            String fieldName = Localizer.getText(requestContext, "common.column.tape_model_number");
//            errors.add("modelNumberMaxLength", new ActionMessage("common.form.fieldExceededMaxLen", new Object[]{fieldName, Schema.AssetTapeTable.TAPE_MODEL_NUMBER_MAX_LEN}));
//        }
        if (tape.getTapeSerialNumber().length() > Schema.AssetTapeTable.TAPE_SERIAL_NUMBER_MAX_LEN) {
            String fieldName = Localizer.getText(requestContext, "common.column.tape_serial_number");
            errors.add("serialNumberMaxLength", new ActionMessage("common.form.fieldExceededMaxLen", new Object[]{fieldName, Schema.AssetTapeTable.TAPE_SERIAL_NUMBER_MAX_LEN}));
        } else if (validateDuplicatedTapeSerialNumber(tape)) {
            errors.add("duplicatedSerialNumber", new ActionMessage("itMgmt.tapeAdd.error.duplicatedSerialNumber", new String[]{tape.getTapeSerialNumber()}));
        }
//        if (!tape.isValidTapeCost()) {
//            errors.add("validCostFormat", new ActionMessage("common.form.fieldFormatError",
//                    Localizer.getText(requestContext, "common.column.tape_purchase_price")));
//        }
//        if (tape.hasTapePurchaseDate() && !tape.isValidPurchaseDate()) {
//            errors.add("validPurchaseDateFormat", new ActionMessage("common.form.fieldDateInvalid",
//                    Localizer.getText(requestContext, "common.column.tape_purchase_date")));
//        }
//        if (tape.hasTapeWarrantyExpireDate() && !tape.isValidWarrantyExpireDate()) {
//            errors.add("validWarrantyExpireDateFormat", new ActionMessage("common.form.fieldDateInvalid",
//                    Localizer.getText(requestContext, "common.column.tape_warranty_expire_date")));
//        }

        // Validate attributes
        AdminUtils.validateAttributeValues(requestContext, errors, tape, customAttributes);
        return errors;
    }

    public ActionMessages addTape(Tape tape, Map<Integer, Attribute> customAttributes)
            throws DatabaseException {

        ActionMessages errors = validateTape(tape, customAttributes);

        if (!errors.isEmpty()) {
            return errors;
        }

        return new TapeDao(requestContext).addTape(tape);
    }

    public ActionMessages updateTape(Tape tape, Map<Integer, Attribute> customAttributes)
            throws DatabaseException {

        ActionMessages errors = validateTape(tape, customAttributes);

        if (!errors.isEmpty()) {
            return errors;
        }

        return new TapeDao(requestContext).update(tape);
    }

    public ActionMessages deleteTape(Tape tape) throws DatabaseException {
        ActionMessages errors = new ActionMessages();

        // Check inputs
        if (tape.getId() == 0) {
            errors.add("emptyTapeId", new ActionMessage("itMgmt.tapeEdit.error.emptyTapeId"));
        }
        if (!errors.isEmpty()) {
            return errors;
        }

        /**
         * Here is what i can do. First, collect a list of file names to be deleted (probably in a dataset).
         * Then, delete the Tape, which would also delete the File records. Next, delete the actual files.
         */
//        List<File> deleteFileList = getTapeFiles(new QueryBits(), tape.getId());

        errors = new TapeDao(requestContext).delete(tape);

        FileService fileService = ServiceProvider.getFileService(requestContext);

        // Delete actual files
//        if (errors.isEmpty()) {
//            fileService.bulkDelete(ConfigManager.file.getTapeFileRepositoryLocation(), deleteFileList);
//        }

        return errors;
    }

    // Tape installed license
//    public List getAvailableSoftware(QueryBits query) throws DatabaseException {
//        return new TapeDao(requestContext).getAvailableSoftware(query);
//    }

//    public List<SoftwareLicense> getAvailableLicenses(QueryBits query, Integer softwareId) throws DatabaseException {
//        return new TapeDao(requestContext).getAvailableLicense(query, softwareId);
//    }

//    public List<TapeSoftwareMap> getInstalledLicense(QueryBits query, Integer tapeId) throws DatabaseException {
//        return new TapeDao(requestContext).getInstalledLicense(query, tapeId);
//    }

//    public ActionMessages assignSoftwareLicense(TapeSoftwareMap hsm) throws DatabaseException {
//        ActionMessages errors = new ActionMessages();
//
//        // Check inputs
//        if (hsm.getTapeId() == 0) {
//            errors.add("emptyTapeId", new ActionMessage("tape.error.emptyTapeId"));
//        }
//        if (hsm.getSoftwareId() == 0) {
//            errors.add("emptySoftwareId", new ActionMessage("tape.error.emptySoftwareId"));
//        }
//        if (!errors.isEmpty()) {
//            return errors;
//        }
//
//        return new TapeDao(requestContext).assignSoftwareLicense(hsm);
//    }

//    public ActionMessages unassignSoftwareLicense(TapeSoftwareMap hsm) throws DatabaseException {
//        ActionMessages errors = new ActionMessages();
//
//        // Check inputs
//        if (hsm.getMapId() == 0) {
//            errors.add("emptyMapId", new ActionMessage("tape.error.emptyMapId"));
//        }
//        if (!errors.isEmpty()) {
//            return errors;
//        }
//
//        return new TapeDao(requestContext).unassignSoftwareLicense(hsm);
//    }

//    public ActionMessages resetTapeSoftwareCount(Integer tapeId) throws DatabaseException {
//        return new TapeDao(requestContext).resetTapeSoftwareCount(tapeId);
//    }

    /**
     * Get tape components.
     */
//    public List getTapeComponents(QueryBits query, Integer tapeId) throws DatabaseException {
//        return new TapeDao(requestContext).getTapeComponents(query, tapeId);
//    }

    /**
     * Get a tape component.
     */
//    public TapeComponent getTapeComponent(Integer tapeId, Integer componentId) throws DatabaseException,
//            ObjectNotFoundException {
//        return new TapeDao(requestContext).getTapeComponentDetail(tapeId, componentId);
//    }

    /**
     * This is for adding tape component
     * @param component
     * @return
     * @throws com.kwoksys.framework.exceptions.DatabaseException
     */
//    public ActionMessages addTapeComponent(TapeComponent component,
//                                               Map<Integer, Attribute> customAttributes) throws DatabaseException {
//         ActionMessages errors = new ActionMessages();

        // Check inputs
//        if (component.getType() == 0) {
//            errors.add("emptyComponentType", new ActionMessage("common.form.fieldRequired",
//                    Localizer.getText(requestContext, "common.column.tape_component_type")));
//        }
//
//        Validate attributes
//        AdminUtils.validateAttributeValues(requestContext, errors, component, customAttributes);

//        if (!errors.isEmpty()) {
//            return errors;
//        }

//        return new TapeDao(requestContext).addTapeComponent(component);
//    }

    /**
     * This is for updating tape component
     * @param component
     * @return
     * @throws com.kwoksys.framework.exceptions.DatabaseException
     */
//    public ActionMessages updateTapeComponent(TapeComponent component, Map<Integer, Attribute> customAttributes) throws DatabaseException {
//        ActionMessages errors = new ActionMessages();
//
//        // Validate attributes
//        AdminUtils.validateAttributeValues(requestContext, errors, component, customAttributes);
//
//        if (!errors.isEmpty()) {
//            return errors;
//        }
//
//        return new TapeDao(requestContext).updateTapeComponent(component);
//    }

    /**
     * This is for deleting a tape component
     * @param component
     * @return
     * @throws com.kwoksys.framework.exceptions.DatabaseException
     */
//    public ActionMessages deleteTapeComponent(TapeComponent component) throws DatabaseException {
//        return new TapeDao(requestContext).deleteTapeComponent(component);
//    }

    /**
     * Get a list of Tape files.
     *
     * @param query
     * @return ..
     */
//    public List<File> getTapeFiles(QueryBits query, Integer tapeId) throws DatabaseException {
//        FileService fileService = ServiceProvider.getFileService(requestContext);
//        return fileService.getFiles(query, ObjectTypes.TAPE, tapeId);
//    }

//    public ActionMessages resetTapeFileCount(Integer tapeId) throws DatabaseException {
//        return new TapeDao(requestContext).resetFileCount(tapeId);
//    }

    /**
     * This is for resetting tape component count.
     * @param tapeId
     * @return
     * @throws com.kwoksys.framework.exceptions.DatabaseException
     */
//    public ActionMessages resetTapeComponentCount(Integer tapeId) throws DatabaseException {
//        return new TapeDao(requestContext).resetComponentCount(tapeId);
//    }

//    public File getTapeFile(Integer tapeId, Integer fileId) throws DatabaseException, ObjectNotFoundException {
//        FileService fileService = ServiceProvider.getFileService(requestContext);
//        File file = fileService.getFile(ObjectTypes.TAPE, tapeId, fileId);
//        file.setConfigRepositoryPath(ConfigManager.file.getTapeFileRepositoryLocation());
//        file.setConfigUploadedFilePrefix(ConfigManager.file.getTapeUploadedFilePrefix());
//        return file;
//    }

    /**
     * Add Tape Issue map.
     * @param issueMap
     * @return
     * @throws com.kwoksys.framework.exceptions.DatabaseException
     */
//    public ActionMessages addTapeIssue(TapeIssueLink issueMap) throws DatabaseException {
//        SystemService systemService = ServiceProvider.getSystemService(requestContext);
//        return systemService.addObjectMapping(issueMap.createObjectMap());
//    }

    /**
     * Delete Tape Issue map.
     * @param issueMap
     * @return
     * @throws com.kwoksys.framework.exceptions.DatabaseException
     */
//    public ActionMessages deleteTapeIssue(TapeIssueLink issueMap) throws DatabaseException {
//        SystemService systemService = ServiceProvider.getSystemService(requestContext);
//        return systemService.deleteObjectMapping(issueMap.createObjectMap());
//    }

//    public ActionMessages addTapeMember(TapeMemberLink memberMap) throws DatabaseException {
//        SystemService systemService = ServiceProvider.getSystemService(requestContext);
//        return systemService.addObjectMapping(memberMap.createObjectMap());
//    }

//    public ActionMessages removeTapeMember(TapeMemberLink memberMap) throws DatabaseException {
//        SystemService systemService = ServiceProvider.getSystemService(requestContext);
//        return systemService.deleteObjectMapping(memberMap.createObjectMap());
//    }

    /**
     * Checks whether the given tapeName already exists in the database (case-insensitive) with a different id.
//     * @param tapeService
     * @param tapeId
     * @param tapeName
     * @return
     * @throws Exception
     */
    public boolean isDuplicatedTapeName(Integer tapeId, String tapeName)
            throws DatabaseException {

        if (ConfigManager.app.isCheckUniqueTapeName()) {
            TapeSearch tapeSearch = new TapeSearch();
            if (tapeId != null) {
                tapeSearch.put(TapeSearch.TAPE_ID_NOT_EQUALS, tapeId);
            }
            tapeSearch.put(TapeSearch.TAPE_NAME_EQUALS, tapeName);
            return getTapeCount(new QueryBits(tapeSearch)) > 0;
        } else {
            return false;
        }
    }

    private boolean validateDuplicatedTapeSerialNumber(Tape tape)
            throws DatabaseException {

        if (ConfigManager.app.isCheckUniqueSerialNumber()) {
            TapeSearch tapeSearch = new TapeSearch();
            if (tape.getId() != null) {
                tapeSearch.put(TapeSearch.TAPE_ID_NOT_EQUALS, tape.getId());
            }
            tapeSearch.put(TapeSearch.TAPE_MANUFACTURER_EQUALS, tape.getManufacturerId());
            tapeSearch.put(TapeSearch.TAPE_SERIAL_NUMBER_EQUALS, tape.getTapeSerialNumber());

            return getTapeCount(new QueryBits(tapeSearch)) > 0;
        } else {
            return false;
        }
    }

    public Tape getSingleTapeByName(String tapeName) throws DatabaseException {
        TapeSearch tapeSearch = new TapeSearch();
        tapeSearch.put(TapeSearch.TAPE_NAME_EQUALS, tapeName);
        QueryBits queryBits = new QueryBits(tapeSearch);
        queryBits.setLimit(2, 0);

        List<Tape> tapeList = getTapeList(queryBits);

        if (tapeList.size() == 1) {
            return tapeList.get(0);
        } else {
            return null;
        }
    }
}
