package com.kwoksys.biz.tape.dao;

import com.kwoksys.biz.admin.dao.AttributeDao;
import com.kwoksys.biz.admin.dto.AccessUser;
import com.kwoksys.biz.admin.dto.AttributeFieldCount;
import com.kwoksys.biz.base.BaseDao;
//import com.kwoksys.biz.hardware.dao.HardwareQueries;
//import com.kwoksys.biz.hardware.dto.Hardware;
//import com.kwoksys.biz.hardware.dto.HardwareComponent;
//import com.kwoksys.biz.hardware.dto.HardwareSoftwareMap;

import com.kwoksys.biz.tape.dao.TapeQueries;
import com.kwoksys.biz.tape.dto.Tape;
import com.kwoksys.biz.tape.dto.TapeComponent;
import com.kwoksys.biz.tape.dto.TapeSoftwareMap;

import com.kwoksys.biz.software.dto.SoftwareLicense;
import com.kwoksys.biz.system.core.ObjectTypes;
import com.kwoksys.biz.system.dto.linking.ObjectLink;
import com.kwoksys.framework.connections.database.QueryBits;
import com.kwoksys.framework.connections.database.QueryHelper;
import com.kwoksys.framework.exceptions.DatabaseException;
import com.kwoksys.framework.exceptions.ObjectNotFoundException;
import com.kwoksys.framework.http.RequestContext;
import com.kwoksys.framework.util.CurrencyUtils;
import com.kwoksys.framework.util.DatetimeUtils;
import com.kwoksys.framework.util.StringUtils;
import org.apache.struts.action.ActionMessages;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Types;
import java.util.ArrayList;
import java.util.List;

/**
 * TapeDao.
 */
public class TapeDao extends BaseDao {

    public TapeDao(RequestContext requestContext) {
        super(requestContext);
    }

    public List<Tape> getTapeList(QueryBits query) throws DatabaseException {
        return getTapeList(new QueryHelper(TapeQueries.selectTapeListQuery(query)));
    }

    private List<Tape> getTapeList(QueryHelper queryHelper) throws DatabaseException {
        Connection conn = getConnection();

        try {
            List list = new ArrayList();
            ResultSet rs = queryHelper.executeQuery(conn);

            while (rs.next()) {
                Tape tape = newTape(rs);
                list.add(tape);
            }
            return list;

        } catch (Exception e) {
            // Database problem
            throw new DatabaseException(e, queryHelper);

        } finally {
            queryHelper.closeRs();
            closeConnection(conn);
        }
    }

    public Tape getTape(Integer tapeId) throws DatabaseException, ObjectNotFoundException {
        Connection conn = getConnection();

        QueryHelper queryHelper = new QueryHelper(TapeQueries.selectTapeDetailQuery());
        queryHelper.addInputInt(tapeId);

        try {
            ResultSet rs = queryHelper.executeQuery(conn);
            if (rs.next()) {
                Tape tape = newTape(rs);
//                tape.setCountSoftware(rs.getInt("software_count"));
//                tape.setCountFile(rs.getInt("file_count"));
//                tape.setCountComponent(rs.getInt("component_count"));
                return tape;
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

    public int getCount(QueryBits query) throws DatabaseException {
        return getRowCount(TapeQueries.getTapeCountQuery(query));
    }

    public List<Tape> getLinkedTapeList(QueryBits query, ObjectLink objectMap) throws DatabaseException {
        QueryHelper queryHelper;

        if (objectMap.getLinkedObjectId() == null || objectMap.getLinkedObjectId() == 0) {
            queryHelper = new QueryHelper(TapeQueries.selectLinkedTapeListQuery(query));
            queryHelper.addInputInt(objectMap.getObjectId());
            queryHelper.addInputInt(objectMap.getObjectTypeId());
            queryHelper.addInputInt(objectMap.getLinkedObjectTypeId());

        } else {
            queryHelper = new QueryHelper(TapeQueries.selectObjectTapeListQuery(query));
            queryHelper.addInputInt(objectMap.getLinkedObjectId());
            queryHelper.addInputInt(objectMap.getLinkedObjectTypeId());
            queryHelper.addInputInt(objectMap.getObjectTypeId());
        }
        return getTapeList(queryHelper);
    }

    /**
     * Return number of tape grouped by type.
     *
     * @return ..
     */
//    public List<AttributeFieldCount> getTapeTypeCount(QueryBits query) throws DatabaseException {
//        Connection conn = getConnection();
//
//        QueryHelper queryHelper = new QueryHelper(TapeQueries.selectTapeTypeCountQuery(query));
//
//        try {
//            List list = new ArrayList();
//            ResultSet rs = queryHelper.executeQuery(conn);
//
//            while (rs.next()) {
//                AttributeFieldCount count = new AttributeFieldCount();
//                count.setAttrFieldId(rs.getInt("media_type"));
//                count.setObjectCount(rs.getInt("tape_count"));
//
//                list.add(count);
//            }
//            return list;
//
//        } catch (Exception e) {
//            // Database problem
//            throw new DatabaseException(e, queryHelper);
//
//        } finally {
//            queryHelper.closeRs();
//            closeConnection(conn);
//        }
//    }

    /**
     * Return number of tape grouped by status.
     *
     * @return ..
     */
//    public List<AttributeFieldCount> getTapeStatusCount(QueryBits query) throws DatabaseException {
//        Connection conn = getConnection();
//
//        QueryHelper queryHelper = new QueryHelper(TapeQueries.selectTapeCountByStatusQuery(query));
//
//        try {
//            List list = new ArrayList();
//            ResultSet rs = queryHelper.executeQuery(conn);
//
//            while (rs.next()) {
//                AttributeFieldCount count = new AttributeFieldCount();
//                count.setAttrFieldId(rs.getInt("status"));
//                count.setObjectCount(rs.getInt("tape_count"));
//
//                list.add(count);
//            }
//            return list;
//
//        } catch (Exception e) {
//            // Database problem
//            throw new DatabaseException(e, queryHelper);
//
//        } finally {
//            queryHelper.closeRs();
//            closeConnection(conn);
//        }
//    }

    /**
     * Return number of tape grouped by location.
     *
     * @return ..
     */
//    public List<AttributeFieldCount> getTapeLocationCount(QueryBits query) throws DatabaseException {
//        Connection conn = getConnection();
//
//        QueryHelper queryHelper = new QueryHelper(TapeQueries.selectTapeCountByLocationQuery(query));
//
//        try {
//            List list = new ArrayList();
//            ResultSet rs = queryHelper.executeQuery(conn);
//
//            while (rs.next()) {
//                AttributeFieldCount count = new AttributeFieldCount();
//                count.setAttrFieldId(rs.getInt("location"));
//                count.setObjectCount(rs.getInt("tape_count"));
//
//                list.add(count);
//            }
//            return list;
//
//        } catch (Exception e) {
//            // Database problem
//            throw new DatabaseException(e, queryHelper);
//
//        } finally {
//            queryHelper.closeRs();
//            closeConnection(conn);
//        }
//    }

//    public List getAvailableSoftware(QueryBits query) throws DatabaseException {
//        QueryHelper queryHelper = new QueryHelper(TapeQueries.selectTapeAvailableSoftwareQuery(query));
//
//        return executeQueryReturnList(queryHelper);
//    }

//    public List<SoftwareLicense> getAvailableLicense(QueryBits query, Integer softwareId) throws DatabaseException {
//        Connection conn = getConnection();
//
//        QueryHelper queryHelper = new QueryHelper(TapeQueries.selectTapeAvailableLicensesQuery(query));
//        queryHelper.addInputInt(softwareId);
//
//        try {
//            List list = new ArrayList();
//            ResultSet rs = queryHelper.executeQuery(conn);
//
//            while (rs.next()) {
//                SoftwareLicense license = new SoftwareLicense();
//                license.setId(rs.getInt("license_id"));
//                license.setKey(rs.getString("license_key"));
//                license.setNote(StringUtils.replaceNull(rs.getString("license_note")));
//
//                list.add(license);
//            }
//            return list;
//
//        } catch (Exception e) {
//            // Database problem
//            throw new DatabaseException(e, queryHelper);
//
//        } finally {
//            queryHelper.closeRs();
//            closeConnection(conn);
//        }
//    }

    /**
     * Returns Software Licenses installed on a Tape.
     *
     */
//    public List<TapeSoftwareMap> getInstalledLicense(QueryBits query, Integer tapeId) throws DatabaseException {
//        Connection conn = getConnection();
//
//        QueryHelper queryHelper = new QueryHelper(TapeQueries.selectInstalledLicenseQuery(query));
//        queryHelper.addInputInt(tapeId);
//
//        try {
//            List list = new ArrayList();
//            ResultSet rs = queryHelper.executeQuery(conn);
//
//            while (rs.next()) {
//                TapeSoftwareMap map = new TapeSoftwareMap();
//                map.setMapId(rs.getInt("map_id"));
//                map.setSoftwareId(rs.getInt("software_id"));
//                map.getSoftware().setName(rs.getString("software_name"));
//                map.setLicenseId(rs.getInt("license_id"));
//                map.getLicense().setKey(rs.getString("license_key"));
//                map.getLicense().setNote(StringUtils.replaceNull(rs.getString("license_note")));
//
//                list.add(map);
//            }
//            return list;
//
//        } catch (Exception e) {
//            // Database problem
//            throw new DatabaseException(e, queryHelper);
//
//        } finally {
//            queryHelper.closeRs();
//            closeConnection(conn);
//        }
//    }

    /**
     * Return Components for a particular Tape.
     */
//    public List<TapeComponent> getTapeComponents(QueryBits query, Integer tapeId) throws DatabaseException {
//        Connection conn = getConnection();
//
//        QueryHelper queryHelper = new QueryHelper(TapeQueries.selectTapeComponentsQuery(query));
//        queryHelper.addInputInt(tapeId);
//
//        try {
//            List list = new ArrayList();
//            ResultSet rs = queryHelper.executeQuery(conn);
//
//            while (rs.next()) {
//                TapeComponent component = new TapeComponent();
//                component.setId(rs.getInt("comp_id"));
//                component.setTypeName(rs.getString("comp_name"));
//                component.setDescription(StringUtils.replaceNull(rs.getString("comp_description")));
//                list.add(component);
//            }
//            return list;
//
//        } catch (Exception e) {
//            // Database problem
//            throw new DatabaseException(e, queryHelper);
//
//        } finally {
//            queryHelper.closeRs();
//            closeConnection(conn);
//        }
//    }

    /**
     * Return a specified tape component.
     */
//    public TapeComponent getTapeComponentDetail(Integer tapeId, Integer componentId) throws DatabaseException,
//            ObjectNotFoundException {
//
//        Connection conn = getConnection();
//
//        QueryHelper queryHelper = new QueryHelper(TapeQueries.selectTapeComponentDetailQuery());
//        queryHelper.addInputInt(tapeId);
//        queryHelper.addInputInt(componentId);
//
//        try {
//            ResultSet rs = queryHelper.executeQuery(conn);
//
//            if (rs.next()) {
//                TapeComponent component = new TapeComponent();
//                component.setTapeId(rs.getInt("tape_id"));
//                component.setId(rs.getInt("comp_id"));
//                component.setType(rs.getInt("tape_component_type"));
//                component.setDescription(StringUtils.replaceNull(rs.getString("comp_description")));
//                return component;
//            }
//        } catch (Exception e) {
//            // Database problem
//            throw new DatabaseException(e, queryHelper);
//
//        } finally {
//            queryHelper.closeRs();
//            closeConnection(conn);
//        }
//        throw new ObjectNotFoundException();
//    }


//    ADD TAPE controller
    public ActionMessages addTape(Tape tape) throws DatabaseException {
        Connection conn = getConnection();

        QueryHelper queryHelper = new QueryHelper(TapeQueries.insertTapeQuery());
        queryHelper.addOutputParam(Types.INTEGER);
        queryHelper.addInputStringConvertNull(tape.getTapeName());
//        queryHelper.addInputStringConvertNull("");
        queryHelper.addInputStringConvertNull(tape.getTapeSerialNumber());
        queryHelper.addInputStringConvertNull(tape.getTapeBarcodeNumber());
//        queryHelper.addInputStringConvertNull(tape.getDescription());
//        queryHelper.addInputIntegerConvertNull(tape.getManufacturerId());
//        queryHelper.addInputIntegerConvertNull(tape.getVendorId());

        // We don't want tape type to be null before it's hard to search for it.
//        queryHelper.addInputInt(tape.getMediaType());
//        queryHelper.addInputInt(tape.getTapeStatus());
//        queryHelper.addInputIntegerConvertNull(tape.getOwnerId());
//        queryHelper.addInputInt(tape.getTapeLocation());
//        queryHelper.addInputStringConvertNull(tape.getModelName());
//        queryHelper.addInputStringConvertNull(tape.getModelNumber());
//        queryHelper.addInputStringConvertNull(tape.getTapeSerialNumber());
//        queryHelper.addInputStringConvertNull(tape.getTapeBarcodeNumber());
//        if (tape.getPurchasePriceRaw() == 0) {
//            queryHelper.addInputDoubleConvertNull(null);
//        } else {
//            queryHelper.addInputDouble(tape.getPurchasePriceRaw());
//        }
//        queryHelper.addInputInt(tape.getResetLastServiceDate());
//        queryHelper.addInputStringConvertNull(tape.getTapePurchaseDateString());
//        queryHelper.addInputStringConvertNull(tape.getWarrantyExpireDateString());
//        queryHelper.addInputInt(requestContext.getUser().getId());

        try {
            queryHelper.executeProcedure(conn);

            // Put some values in the result.
            tape.setId((Integer) queryHelper.getSqlOutputs().get(0));

            // Update custom fields
//            if (!tape.getCustomValues().isEmpty()) {
//                AttributeDao attributeDao = new AttributeDao(requestContext);
//                attributeDao.updateAttributeValue(conn, tape.getId(), tape.getCustomValues());
//            }
        } catch (Exception e) {
            // Database problem
            handleError(e);

        } finally {
            closeConnection(conn);
        }
        return errors;
    }

//    update tape controller
    public ActionMessages update(Tape tape) throws DatabaseException {
        Connection conn = getConnection();

        QueryHelper queryHelper = new QueryHelper(TapeQueries.updateTapeQuery());
        queryHelper.addInputInt(tape.getId());
        queryHelper.addInputStringConvertNull(tape.getTapeName());
//        queryHelper.addInputStringConvertNull(null);
//        queryHelper.addInputStringConvertNull(tape.getDescription());
//        queryHelper.addInputIntegerConvertNull(tape.getManufacturerId());
//        queryHelper.addInputIntegerConvertNull(tape.getVendorId());

        // We don't want tape type to be null before it's hard to search for it.
//        queryHelper.addInputInt(tape.getMediaType());
//        queryHelper.addInputInt(tape.getTapeStatus());
//        queryHelper.addInputIntegerConvertNull(tape.getOwnerId());
//        queryHelper.addInputInt(tape.getTapeLocation());
//        queryHelper.addInputStringConvertNull(tape.getModelName());
//        queryHelper.addInputStringConvertNull(tape.getModelNumber());
        queryHelper.addInputStringConvertNull(tape.getTapeSerialNumber());
        queryHelper.addInputStringConvertNull(tape.getTapeBarcodeNumber());
//        if (tape.getPurchasePriceRaw() == 0) {
//            queryHelper.addInputDoubleConvertNull(null);
//        } else {
//            queryHelper.addInputDouble(tape.getPurchasePriceRaw());
//        }
//        queryHelper.addInputInt(tape.getResetLastServiceDate());
//        queryHelper.addInputStringConvertNull(tape.hasTapePurchaseDate() ?
//                tape.getTapePurchaseDateString() : null);
//        queryHelper.addInputStringConvertNull(tape.hasTapeWarrantyExpireDate() ?
//                tape.getWarrantyExpireDateString() : null);
        queryHelper.addInputInt(requestContext.getUser().getId());

        try {
            queryHelper.executeProcedure(conn);

            // Update custom fields
            if (!tape.getCustomValues().isEmpty()) {
                AttributeDao attributeDao = new AttributeDao(requestContext);
                attributeDao.updateAttributeValue(conn, tape.getId(), tape.getCustomValues());
            }
        } catch (Exception e) {
            // Database problem
            handleError(e);

        } finally {
            closeConnection(conn);
        }
        return errors;
    }

//    delete tape controller
    public ActionMessages delete(Tape tape) throws DatabaseException {
        QueryHelper queryHelper = new QueryHelper(TapeQueries.deleteTapeQuery());
        queryHelper.addInputInt(ObjectTypes.TAPE);
        queryHelper.addInputInt(tape.getId());

        return executeProcedure(queryHelper);
    }

//    public ActionMessages assignSoftwareLicense(TapeSoftwareMap hsm) throws DatabaseException {
//        QueryHelper queryHelper = new QueryHelper(TapeQueries.insertAssignLicenseQuery());
//        queryHelper.addOutputParam(Types.INTEGER);
//        queryHelper.addInputInt(hsm.getTapeId());
//        queryHelper.addInputInt(hsm.getSoftwareId());
//        queryHelper.addInputInt(hsm.getLicenseId());
//        queryHelper.addInputInt(hsm.getLicenseEntitlement());
//
//        executeProcedure(queryHelper);
//
//        // Put some values in the result.
//        if (errors.isEmpty()) {
//            hsm.setMapId((Integer)queryHelper.getSqlOutputs().get(0));
//        }
//
//        return errors;
//    }

//    public ActionMessages unassignSoftwareLicense(TapeSoftwareMap hsm) throws DatabaseException {
//        QueryHelper queryHelper = new QueryHelper(TapeQueries.deleteAssignedLicenseQuery());
//        queryHelper.addInputInt(hsm.getMapId());
//
//        return executeProcedure(queryHelper);
//    }
//
    /**
     * Adds tape component.
     * @param component
     * @return
     * @throws com.kwoksys.framework.exceptions.DatabaseException
     */
//    public ActionMessages addTapeComponent(TapeComponent component) throws DatabaseException {
//        Connection conn = getConnection();
//
//        QueryHelper queryHelper = new QueryHelper(TapeQueries.insertTapeComponentQuery());
//        queryHelper.addOutputParam(Types.INTEGER);
//        queryHelper.addInputInt(component.getTapeId());
//        queryHelper.addInputStringConvertNull(component.getDescription());
//        queryHelper.addInputInt(component.getType());
//        queryHelper.addInputInt(requestContext.getUser().getId());
//
//        try {
//            queryHelper.executeProcedure(conn);
//            // Put some values in the result.
//            component.setId((Integer) queryHelper.getSqlOutputs().get(0));
//
//            // Update custom fields
//            if (!component.getCustomValues().isEmpty()) {
//                AttributeDao attributeDao = new AttributeDao(requestContext);
//                attributeDao.updateAttributeValue(conn, component.getId(), component.getCustomValues());
//            }
//        } catch (Exception e) {
//            // Database problem
//            handleError(e);
//
//        } finally {
//            closeConnection(conn);
//        }
//        return errors;
//    }

    /**
     * Updates tape component.
     * @param component
     * @return
     * @throws com.kwoksys.framework.exceptions.DatabaseException
     */
//    public ActionMessages updateTapeComponent(TapeComponent component) throws DatabaseException {
//        Connection conn = getConnection();
//
//        QueryHelper queryHelper = new QueryHelper(TapeQueries.updateTapeComponentQuery());
//        queryHelper.addInputInt(component.getTapeId());
//        queryHelper.addInputInt(component.getId());
//        queryHelper.addInputStringConvertNull(component.getDescription());
//        queryHelper.addInputInt(component.getType());
//        queryHelper.addInputInt(requestContext.getUser().getId());
//
//        try {
//            queryHelper.executeProcedure(conn);
//
//            // Update custom fields
//            if (!component.getCustomValues().isEmpty()) {
//                AttributeDao attributeDao = new AttributeDao(requestContext);
//                attributeDao.updateAttributeValue(conn, component.getId(), component.getCustomValues());
//            }
//        } catch (Exception e) {
//            // Database problem
//            handleError(e);
//
//        } finally {
//            closeConnection(conn);
//        }
//        return errors;
//    }

    /**
     * Deletes tape component.
     * @param component
     * @return
     * @throws com.kwoksys.framework.exceptions.DatabaseException
     */
//    public ActionMessages deleteTapeComponent(TapeComponent component) throws DatabaseException {
//        QueryHelper queryHelper = new QueryHelper(TapeQueries.deleteTapeComponentQuery());
//        queryHelper.addInputInt(ObjectTypes.HARDWARE_COMPONENT);
//        queryHelper.addInputInt(component.getTapeId());
//        queryHelper.addInputInt(component.getId());
//
//        return executeProcedure(queryHelper);
//    }

//    public ActionMessages resetTapeSoftwareCount(Integer tapeId) throws DatabaseException {
//        QueryHelper queryHelper = new QueryHelper(TapeQueries.updateTapeSoftwareCountQuery());
//        queryHelper.addInputInt(tapeId);
//
//        return executeProcedure(queryHelper);
//    }

    /**
     * Resets tape file count.
     * @param tapeId
     * @return
     * @throws com.kwoksys.framework.exceptions.DatabaseException
     */
//    public ActionMessages resetFileCount(Integer tapeId) throws DatabaseException {
//        QueryHelper queryHelper = new QueryHelper(TapeQueries.updateTapeFileCountQuery());
//        queryHelper.addInputInt(ObjectTypes.HARDWARE);
//        queryHelper.addInputInt(tapeId);
//
//        return executeProcedure(queryHelper);
//    }

    /**
     * Resets tape component count.
//     * @param tapeId
     * @return
     * @throws com.kwoksys.framework.exceptions.DatabaseException
     */
//    public ActionMessages resetComponentCount(Integer tapeId) throws DatabaseException {
//        QueryHelper queryHelper = new QueryHelper(TapeQueries.updateTapeComponentCountQuery());
//        queryHelper.addInputInt(tapeId);
//
//        return executeProcedure(queryHelper);
//    }

    private Tape newTape(ResultSet rs) throws SQLException, DatabaseException {
        Tape tape = new Tape();
        tape.setId(rs.getInt("tape_id"));
        System.out.println("tape id: " + rs.getInt("tape_id"));
        tape.setTapeName(rs.getString("tape_name"));
        System.out.println("tape name: "+rs.getString("tape_name"));
//        tape.setDescription(StringUtils.replaceNull(rs.getString("tape_description")));
        tape.setTapeSerialNumber(StringUtils.replaceNull(rs.getString("serial_number")));
        System.out.println("serial number: " + rs.getString("serial_number"));
        tape.setTapeBarcodeNumber(StringUtils.replaceNull(rs.getString("barcode_number")));
        System.out.println("barcode number: "+rs.getString("barcode_number"));
//        tape.setModelName(StringUtils.replaceNull(rs.getString("tape_model_name")));
//        tape.setModelNumber(StringUtils.replaceNull(rs.getString("tape_model_number")));
//        tape.setManufacturerId(rs.getInt("manufacturer_company_id"));
//        tape.setManufacturerName(StringUtils.replaceNull(rs.getString("tape_manufacturer_name")));
//        tape.setVendorId(rs.getInt("vendor_company_id"));
//        tape.setVendorName(StringUtils.replaceNull(rs.getString("tape_vendor_name")));
//
//        tape.setTapeLocation(rs.getInt("tape_location"));
//        tape.setMediaType(rs.getInt("media_type"));
//        tape.setTapeStatus(rs.getInt("tape_status"));

//        tape.setPurchasePrice(CurrencyUtils.formatCurrency(rs.getDouble("tape_purchase_price"), ""));
//        tape.setLastServicedOn(DatetimeUtils.getDate(rs, "tape_last_service_date"));

//        tape.setTapePurchaseDate(DatetimeUtils.getDate(rs, "tape_purchase_date"));
//        if (tape.getTapePurchaseDate() != null) {
//            tape.setTapePurchaseDate(
//                    DatetimeUtils.toYearString(tape.getTapePurchaseDate()),
//                    DatetimeUtils.toMonthString(tape.getTapePurchaseDate()),
//                    DatetimeUtils.toDateString(tape.getTapePurchaseDate()));
//        }

//        tape.setWarrantyExpireDate(DatetimeUtils.getDate(rs, "tape_warranty_expire_date"));
//        if (tape.getWarrantyExpireDate() != null) {
//            tape.setTapeWarrantyExpireDate(
//                    DatetimeUtils.toYearString(tape.getWarrantyExpireDate()),
//                    DatetimeUtils.toMonthString(tape.getWarrantyExpireDate()),
//                    DatetimeUtils.toDateString(tape.getWarrantyExpireDate()));
//        }

//        tape.setCreationDate(DatetimeUtils.getDate(rs, "creation_date"));
//        tape.setModificationDate(DatetimeUtils.getDate(rs, "modification_date"));
//
//        tape.setOwner(new AccessUser());
//        tape.getOwner().setId(rs.getInt("tape_owner_id"));
//        tape.setOwnerId(tape.getOwner().getId());
//        tape.getOwner().setUsername(rs.getString("tape_owner_username"));
//        tape.getOwner().setDisplayName(rs.getString("tape_owner_display_name"));
//
//        tape.setCreator(new AccessUser());
//        tape.getCreator().setId(rs.getInt("creator"));
//        tape.getCreator().setUsername(rs.getString("creator_username"));
//        tape.getCreator().setDisplayName(rs.getString("creator_display_name"));
//
//        tape.setModifier(new AccessUser());
//        tape.getModifier().setId(rs.getInt("modifier"));
//        tape.getModifier().setUsername(rs.getString("modifier_username"));
//        tape.getModifier().setDisplayName(rs.getString("modifier_display_name"));
        return tape;
    }
}
