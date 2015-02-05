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
package com.kwoksys.biz.tape.dao;

import com.kwoksys.biz.admin.dao.AttributeDao;
import com.kwoksys.biz.admin.dto.AccessUser;
import com.kwoksys.biz.admin.dto.AttributeFieldCount;
import com.kwoksys.biz.base.BaseDao;
import com.kwoksys.biz.hardware.dao.HardwareQueries;
import com.kwoksys.biz.hardware.dto.Hardware;
import com.kwoksys.biz.hardware.dto.HardwareComponent;
import com.kwoksys.biz.hardware.dto.HardwareSoftwareMap;
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
 * HardwareDao.
 */
public class TapeDao extends BaseDao {

    public TapeDao(RequestContext requestContext) {
        super(requestContext);
    }

    public List<Hardware> getHardwareList(QueryBits query) throws DatabaseException {
        return getHardwareList(new QueryHelper(TapeQueries.selectHardwareListQuery(query)));
    }

    private List<Hardware> getHardwareList(QueryHelper queryHelper) throws DatabaseException {
        Connection conn = getConnection();

        try {
            List list = new ArrayList();
            ResultSet rs = queryHelper.executeQuery(conn);

            while (rs.next()) {
                Hardware hardware = newHardware(rs);
                list.add(hardware);
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

    public Hardware getHardware(Integer hardwareId) throws DatabaseException, ObjectNotFoundException {
        Connection conn = getConnection();

        QueryHelper queryHelper = new QueryHelper(com.kwoksys.biz.hardware.dao.HardwareQueries.selectHardwareDetailQuery());
        queryHelper.addInputInt(hardwareId);

        try {
            ResultSet rs = queryHelper.executeQuery(conn);
            if (rs.next()) {
                Hardware hardware = newHardware(rs);
                hardware.setCountSoftware(rs.getInt("software_count"));
                hardware.setCountFile(rs.getInt("file_count"));
                hardware.setCountComponent(rs.getInt("component_count"));
                return hardware;
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
        return getRowCount(com.kwoksys.biz.hardware.dao.HardwareQueries.getHardwareCountQuery(query));
    }

    public List<Hardware> getLinkedHardwareList(QueryBits query, ObjectLink objectMap) throws DatabaseException {
        QueryHelper queryHelper;

        if (objectMap.getLinkedObjectId() == null || objectMap.getLinkedObjectId() == 0) {
            queryHelper = new QueryHelper(com.kwoksys.biz.hardware.dao.HardwareQueries.selectLinkedHardwareListQuery(query));
            queryHelper.addInputInt(objectMap.getObjectId());
            queryHelper.addInputInt(objectMap.getObjectTypeId());
            queryHelper.addInputInt(objectMap.getLinkedObjectTypeId());

        } else {
            queryHelper = new QueryHelper(com.kwoksys.biz.hardware.dao.HardwareQueries.selectObjectHardwareListQuery(query));
            queryHelper.addInputInt(objectMap.getLinkedObjectId());
            queryHelper.addInputInt(objectMap.getLinkedObjectTypeId());
            queryHelper.addInputInt(objectMap.getObjectTypeId());
        }
        return getHardwareList(queryHelper);
    }

    /**
     * Return number of hardware grouped by type.
     *
     * @return ..
     */
    public List<AttributeFieldCount> getHardwareTypeCount(QueryBits query) throws DatabaseException {
        Connection conn = getConnection();

        QueryHelper queryHelper = new QueryHelper(com.kwoksys.biz.hardware.dao.HardwareQueries.selectHardwareTypeCountQuery(query));

        try {
            List list = new ArrayList();
            ResultSet rs = queryHelper.executeQuery(conn);

            while (rs.next()) {
                AttributeFieldCount count = new AttributeFieldCount();
                count.setAttrFieldId(rs.getInt("hardware_type"));
                count.setObjectCount(rs.getInt("hardware_count"));

                list.add(count);
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

    /**
     * Return number of hardware grouped by status.
     *
     * @return ..
     */
    public List<AttributeFieldCount> getHardwareStatusCount(QueryBits query) throws DatabaseException {
        Connection conn = getConnection();

        QueryHelper queryHelper = new QueryHelper(com.kwoksys.biz.hardware.dao.HardwareQueries.selectHardwareCountByStatusQuery(query));

        try {
            List list = new ArrayList();
            ResultSet rs = queryHelper.executeQuery(conn);

            while (rs.next()) {
                AttributeFieldCount count = new AttributeFieldCount();
                count.setAttrFieldId(rs.getInt("hardware_status"));
                count.setObjectCount(rs.getInt("hardware_count"));

                list.add(count);
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

    /**
     * Return number of hardware grouped by location.
     *
     * @return ..
     */
    public List<AttributeFieldCount> getHardwareLocationCount(QueryBits query) throws DatabaseException {
        Connection conn = getConnection();

        QueryHelper queryHelper = new QueryHelper(com.kwoksys.biz.hardware.dao.HardwareQueries.selectHardwareCountByLocationQuery(query));

        try {
            List list = new ArrayList();
            ResultSet rs = queryHelper.executeQuery(conn);

            while (rs.next()) {
                AttributeFieldCount count = new AttributeFieldCount();
                count.setAttrFieldId(rs.getInt("hardware_location"));
                count.setObjectCount(rs.getInt("hardware_count"));

                list.add(count);
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

    public List getAvailableSoftware(QueryBits query) throws DatabaseException {
        QueryHelper queryHelper = new QueryHelper(com.kwoksys.biz.hardware.dao.HardwareQueries.selectHardwareAvailableSoftwareQuery(query));

        return executeQueryReturnList(queryHelper);
    }

    public List<SoftwareLicense> getAvailableLicense(QueryBits query, Integer softwareId) throws DatabaseException {
        Connection conn = getConnection();

        QueryHelper queryHelper = new QueryHelper(com.kwoksys.biz.hardware.dao.HardwareQueries.selectHardwareAvailableLicensesQuery(query));
        queryHelper.addInputInt(softwareId);

        try {
            List list = new ArrayList();
            ResultSet rs = queryHelper.executeQuery(conn);

            while (rs.next()) {
                SoftwareLicense license = new SoftwareLicense();
                license.setId(rs.getInt("license_id"));
                license.setKey(rs.getString("license_key"));
                license.setNote(StringUtils.replaceNull(rs.getString("license_note")));

                list.add(license);
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

    /**
     * Returns Software Licenses installed on a Hardware.
     *
     */
    public List<HardwareSoftwareMap> getInstalledLicense(QueryBits query, Integer hardwareId) throws DatabaseException {
        Connection conn = getConnection();

        QueryHelper queryHelper = new QueryHelper(com.kwoksys.biz.hardware.dao.HardwareQueries.selectInstalledLicenseQuery(query));
        queryHelper.addInputInt(hardwareId);

        try {
            List list = new ArrayList();
            ResultSet rs = queryHelper.executeQuery(conn);

            while (rs.next()) {
                HardwareSoftwareMap map = new HardwareSoftwareMap();
                map.setMapId(rs.getInt("map_id"));
                map.setSoftwareId(rs.getInt("software_id"));
                map.getSoftware().setName(rs.getString("software_name"));
                map.setLicenseId(rs.getInt("license_id"));
                map.getLicense().setKey(rs.getString("license_key"));
                map.getLicense().setNote(StringUtils.replaceNull(rs.getString("license_note")));

                list.add(map);
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

    /**
     * Return Components for a particular Hardware.
     */
    public List<HardwareComponent> getHardwareComponents(QueryBits query, Integer hardwareId) throws DatabaseException {
        Connection conn = getConnection();

        QueryHelper queryHelper = new QueryHelper(com.kwoksys.biz.hardware.dao.HardwareQueries.selectHardwareComponentsQuery(query));
        queryHelper.addInputInt(hardwareId);

        try {
            List list = new ArrayList();
            ResultSet rs = queryHelper.executeQuery(conn);

            while (rs.next()) {
                HardwareComponent component = new HardwareComponent();
                component.setId(rs.getInt("comp_id"));
                component.setTypeName(rs.getString("comp_name"));
                component.setDescription(StringUtils.replaceNull(rs.getString("comp_description")));
                list.add(component);
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

    /**
     * Return a specified hardware component.
     */
    public HardwareComponent getHardwareComponentDetail(Integer hardwareId, Integer componentId) throws DatabaseException,
            ObjectNotFoundException {

        Connection conn = getConnection();

        QueryHelper queryHelper = new QueryHelper(com.kwoksys.biz.hardware.dao.HardwareQueries.selectHardwareComponentDetailQuery());
        queryHelper.addInputInt(hardwareId);
        queryHelper.addInputInt(componentId);

        try {
            ResultSet rs = queryHelper.executeQuery(conn);

            if (rs.next()) {
                HardwareComponent component = new HardwareComponent();
                component.setHardwareId(rs.getInt("hardware_id"));
                component.setId(rs.getInt("comp_id"));
                component.setType(rs.getInt("hardware_component_type"));
                component.setDescription(StringUtils.replaceNull(rs.getString("comp_description")));
                return component;
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

    public ActionMessages addHardware(Hardware hardware) throws DatabaseException {
        Connection conn = getConnection();

        QueryHelper queryHelper = new QueryHelper(com.kwoksys.biz.hardware.dao.HardwareQueries.insertHardwareQuery());
        queryHelper.addOutputParam(Types.INTEGER);
        queryHelper.addInputStringConvertNull(hardware.getName());
        queryHelper.addInputStringConvertNull("");
        queryHelper.addInputStringConvertNull(hardware.getDescription());
        queryHelper.addInputIntegerConvertNull(hardware.getManufacturerId());
        queryHelper.addInputIntegerConvertNull(hardware.getVendorId());

        // We don't want hardware type to be null before it's hard to search for it.
        queryHelper.addInputInt(hardware.getType());
        queryHelper.addInputInt(hardware.getStatus());
        queryHelper.addInputIntegerConvertNull(hardware.getOwnerId());
        queryHelper.addInputInt(hardware.getLocation());
        queryHelper.addInputStringConvertNull(hardware.getModelName());
        queryHelper.addInputStringConvertNull(hardware.getModelNumber());
        queryHelper.addInputStringConvertNull(hardware.getSerialNumber());
        if (hardware.getPurchasePriceRaw() == 0) {
            queryHelper.addInputDoubleConvertNull(null);
        } else {
            queryHelper.addInputDouble(hardware.getPurchasePriceRaw());
        }
        queryHelper.addInputInt(hardware.getResetLastServiceDate());
        queryHelper.addInputStringConvertNull(hardware.getHardwarePurchaseDateString());
        queryHelper.addInputStringConvertNull(hardware.getWarrantyExpireDateString());
        queryHelper.addInputInt(requestContext.getUser().getId());

        try {
            queryHelper.executeProcedure(conn);

            // Put some values in the result.
            hardware.setId((Integer) queryHelper.getSqlOutputs().get(0));

            // Update custom fields
            if (!hardware.getCustomValues().isEmpty()) {
                AttributeDao attributeDao = new AttributeDao(requestContext);
                attributeDao.updateAttributeValue(conn, hardware.getId(), hardware.getCustomValues());
            }
        } catch (Exception e) {
            // Database problem
            handleError(e);

        } finally {
            closeConnection(conn);
        }
        return errors;
    }

    public ActionMessages update(Hardware hardware) throws DatabaseException {
        Connection conn = getConnection();

        QueryHelper queryHelper = new QueryHelper(com.kwoksys.biz.hardware.dao.HardwareQueries.updateHardwareQuery());
        queryHelper.addInputInt(hardware.getId());
        queryHelper.addInputStringConvertNull(hardware.getName());
        queryHelper.addInputStringConvertNull(null);
        queryHelper.addInputStringConvertNull(hardware.getDescription());
        queryHelper.addInputIntegerConvertNull(hardware.getManufacturerId());
        queryHelper.addInputIntegerConvertNull(hardware.getVendorId());

        // We don't want hardware type to be null before it's hard to search for it.
        queryHelper.addInputInt(hardware.getType());
        queryHelper.addInputInt(hardware.getStatus());
        queryHelper.addInputIntegerConvertNull(hardware.getOwnerId());
        queryHelper.addInputInt(hardware.getLocation());
        queryHelper.addInputStringConvertNull(hardware.getModelName());
        queryHelper.addInputStringConvertNull(hardware.getModelNumber());
        queryHelper.addInputStringConvertNull(hardware.getSerialNumber());
        if (hardware.getPurchasePriceRaw() == 0) {
            queryHelper.addInputDoubleConvertNull(null);
        } else {
            queryHelper.addInputDouble(hardware.getPurchasePriceRaw());
        }
        queryHelper.addInputInt(hardware.getResetLastServiceDate());
        queryHelper.addInputStringConvertNull(hardware.hasHardwarePurchaseDate() ?
                hardware.getHardwarePurchaseDateString() : null);
        queryHelper.addInputStringConvertNull(hardware.hasHardwareWarrantyExpireDate() ?
                hardware.getWarrantyExpireDateString() : null);
        queryHelper.addInputInt(requestContext.getUser().getId());

        try {
            queryHelper.executeProcedure(conn);

            // Update custom fields
            if (!hardware.getCustomValues().isEmpty()) {
                AttributeDao attributeDao = new AttributeDao(requestContext);
                attributeDao.updateAttributeValue(conn, hardware.getId(), hardware.getCustomValues());
            }
        } catch (Exception e) {
            // Database problem
            handleError(e);

        } finally {
            closeConnection(conn);
        }
        return errors;
    }

    public ActionMessages delete(Hardware hardware) throws DatabaseException {
        QueryHelper queryHelper = new QueryHelper(com.kwoksys.biz.hardware.dao.HardwareQueries.deleteHardwareQuery());
        queryHelper.addInputInt(ObjectTypes.HARDWARE);
        queryHelper.addInputInt(hardware.getId());

        return executeProcedure(queryHelper);
    }

    public ActionMessages assignSoftwareLicense(HardwareSoftwareMap hsm) throws DatabaseException {
        QueryHelper queryHelper = new QueryHelper(com.kwoksys.biz.hardware.dao.HardwareQueries.insertAssignLicenseQuery());
        queryHelper.addOutputParam(Types.INTEGER);
        queryHelper.addInputInt(hsm.getHardwareId());
        queryHelper.addInputInt(hsm.getSoftwareId());
        queryHelper.addInputInt(hsm.getLicenseId());
        queryHelper.addInputInt(hsm.getLicenseEntitlement());

        executeProcedure(queryHelper);

        // Put some values in the result.
        if (errors.isEmpty()) {
            hsm.setMapId((Integer)queryHelper.getSqlOutputs().get(0));
        }

        return errors;
    }

    public ActionMessages unassignSoftwareLicense(HardwareSoftwareMap hsm) throws DatabaseException {
        QueryHelper queryHelper = new QueryHelper(com.kwoksys.biz.hardware.dao.HardwareQueries.deleteAssignedLicenseQuery());
        queryHelper.addInputInt(hsm.getMapId());

        return executeProcedure(queryHelper);
    }

    /**
     * Adds hardware component.
     * @param component
     * @return
     * @throws com.kwoksys.framework.exceptions.DatabaseException
     */
    public ActionMessages addHardwareComponent(HardwareComponent component) throws DatabaseException {
        Connection conn = getConnection();

        QueryHelper queryHelper = new QueryHelper(com.kwoksys.biz.hardware.dao.HardwareQueries.insertHardwareComponentQuery());
        queryHelper.addOutputParam(Types.INTEGER);
        queryHelper.addInputInt(component.getHardwareId());
        queryHelper.addInputStringConvertNull(component.getDescription());
        queryHelper.addInputInt(component.getType());
        queryHelper.addInputInt(requestContext.getUser().getId());

        try {
            queryHelper.executeProcedure(conn);
            // Put some values in the result.
            component.setId((Integer) queryHelper.getSqlOutputs().get(0));

            // Update custom fields
            if (!component.getCustomValues().isEmpty()) {
                AttributeDao attributeDao = new AttributeDao(requestContext);
                attributeDao.updateAttributeValue(conn, component.getId(), component.getCustomValues());
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
     * Updates hardware component.
     * @param component
     * @return
     * @throws com.kwoksys.framework.exceptions.DatabaseException
     */
    public ActionMessages updateHardwareComponent(HardwareComponent component) throws DatabaseException {
        Connection conn = getConnection();

        QueryHelper queryHelper = new QueryHelper(com.kwoksys.biz.hardware.dao.HardwareQueries.updateHardwareComponentQuery());
        queryHelper.addInputInt(component.getHardwareId());
        queryHelper.addInputInt(component.getId());
        queryHelper.addInputStringConvertNull(component.getDescription());
        queryHelper.addInputInt(component.getType());
        queryHelper.addInputInt(requestContext.getUser().getId());

        try {
            queryHelper.executeProcedure(conn);

            // Update custom fields
            if (!component.getCustomValues().isEmpty()) {
                AttributeDao attributeDao = new AttributeDao(requestContext);
                attributeDao.updateAttributeValue(conn, component.getId(), component.getCustomValues());
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
     * Deletes hardware component.
     * @param component
     * @return
     * @throws com.kwoksys.framework.exceptions.DatabaseException
     */
    public ActionMessages deleteHardwareComponent(HardwareComponent component) throws DatabaseException {
        QueryHelper queryHelper = new QueryHelper(com.kwoksys.biz.hardware.dao.HardwareQueries.deleteHardwareComponentQuery());
        queryHelper.addInputInt(ObjectTypes.HARDWARE_COMPONENT);
        queryHelper.addInputInt(component.getHardwareId());
        queryHelper.addInputInt(component.getId());

        return executeProcedure(queryHelper);
    }

    public ActionMessages resetHardwareSoftwareCount(Integer hardwareId) throws DatabaseException {
        QueryHelper queryHelper = new QueryHelper(com.kwoksys.biz.hardware.dao.HardwareQueries.updateHardwareSoftwareCountQuery());
        queryHelper.addInputInt(hardwareId);

        return executeProcedure(queryHelper);
    }

    /**
     * Resets hardware file count.
     * @param hardwareId
     * @return
     * @throws com.kwoksys.framework.exceptions.DatabaseException
     */
    public ActionMessages resetFileCount(Integer hardwareId) throws DatabaseException {
        QueryHelper queryHelper = new QueryHelper(com.kwoksys.biz.hardware.dao.HardwareQueries.updateHardwareFileCountQuery());
        queryHelper.addInputInt(ObjectTypes.HARDWARE);
        queryHelper.addInputInt(hardwareId);

        return executeProcedure(queryHelper);
    }

    /**
     * Resets hardware component count.
     * @param hardwareId
     * @return
     * @throws com.kwoksys.framework.exceptions.DatabaseException
     */
    public ActionMessages resetComponentCount(Integer hardwareId) throws DatabaseException {
        QueryHelper queryHelper = new QueryHelper(HardwareQueries.updateHardwareComponentCountQuery());
        queryHelper.addInputInt(hardwareId);

        return executeProcedure(queryHelper);
    }

    private Hardware newHardware(ResultSet rs) throws SQLException, DatabaseException {
        Hardware hardware = new Hardware();
        hardware.setId(rs.getInt("hardware_id"));
        hardware.setName(rs.getString("hardware_name"));
        hardware.setDescription(StringUtils.replaceNull(rs.getString("hardware_description")));
        hardware.setSerialNumber(StringUtils.replaceNull(rs.getString("hardware_serial_number")));
        hardware.setModelName(StringUtils.replaceNull(rs.getString("hardware_model_name")));
        hardware.setModelNumber(StringUtils.replaceNull(rs.getString("hardware_model_number")));
        hardware.setManufacturerId(rs.getInt("manufacturer_company_id"));
        hardware.setManufacturerName(StringUtils.replaceNull(rs.getString("hardware_manufacturer_name")));
        hardware.setVendorId(rs.getInt("vendor_company_id"));
        hardware.setVendorName(StringUtils.replaceNull(rs.getString("hardware_vendor_name")));

        hardware.setLocation(rs.getInt("hardware_location"));
        hardware.setType(rs.getInt("hardware_type"));
        hardware.setStatus(rs.getInt("hardware_status"));
        hardware.setPurchasePrice(CurrencyUtils.formatCurrency(rs.getDouble("hardware_purchase_price"), ""));
        hardware.setLastServicedOn(DatetimeUtils.getDate(rs, "hardware_last_service_date"));

        hardware.setHardwarePurchaseDate(DatetimeUtils.getDate(rs, "hardware_purchase_date"));
        if (hardware.getHardwarePurchaseDate() != null) {
            hardware.setHardwarePurchaseDate(
                    DatetimeUtils.toYearString(hardware.getHardwarePurchaseDate()),
                    DatetimeUtils.toMonthString(hardware.getHardwarePurchaseDate()),
                    DatetimeUtils.toDateString(hardware.getHardwarePurchaseDate()));
        }

        hardware.setWarrantyExpireDate(DatetimeUtils.getDate(rs, "hardware_warranty_expire_date"));
        if (hardware.getWarrantyExpireDate() != null) {
            hardware.setHardwareWarrantyExpireDate(
                    DatetimeUtils.toYearString(hardware.getWarrantyExpireDate()),
                    DatetimeUtils.toMonthString(hardware.getWarrantyExpireDate()),
                    DatetimeUtils.toDateString(hardware.getWarrantyExpireDate()));
        }

        hardware.setCreationDate(DatetimeUtils.getDate(rs, "creation_date"));
        hardware.setModificationDate(DatetimeUtils.getDate(rs, "modification_date"));

        hardware.setOwner(new AccessUser());
        hardware.getOwner().setId(rs.getInt("hardware_owner_id"));
        hardware.setOwnerId(hardware.getOwner().getId());
        hardware.getOwner().setUsername(rs.getString("hardware_owner_username"));
        hardware.getOwner().setDisplayName(rs.getString("hardware_owner_display_name"));

        hardware.setCreator(new AccessUser());
        hardware.getCreator().setId(rs.getInt("creator"));
        hardware.getCreator().setUsername(rs.getString("creator_username"));
        hardware.getCreator().setDisplayName(rs.getString("creator_display_name"));

        hardware.setModifier(new AccessUser());
        hardware.getModifier().setId(rs.getInt("modifier"));
        hardware.getModifier().setUsername(rs.getString("modifier_username"));
        hardware.getModifier().setDisplayName(rs.getString("modifier_display_name"));
        return hardware;
    }
}
