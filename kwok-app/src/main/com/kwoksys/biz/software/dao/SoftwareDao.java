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
package com.kwoksys.biz.software.dao;

import com.kwoksys.biz.admin.dao.AttributeDao;
import com.kwoksys.biz.admin.dto.AccessUser;
import com.kwoksys.biz.base.BaseDao;
import com.kwoksys.biz.software.dto.Software;
import com.kwoksys.biz.software.dto.SoftwareLicense;
import com.kwoksys.biz.system.dto.linking.ObjectLink;
import com.kwoksys.biz.system.core.ObjectTypes;
import com.kwoksys.framework.connections.database.QueryBits;
import com.kwoksys.framework.connections.database.QueryHelper;
import com.kwoksys.framework.exceptions.DatabaseException;
import com.kwoksys.framework.exceptions.ObjectNotFoundException;
import com.kwoksys.framework.http.RequestContext;
import com.kwoksys.framework.util.DatetimeUtils;
import com.kwoksys.framework.util.StringUtils;
import org.apache.struts.action.ActionMessages;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.Types;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * SoftwareDao.
 */
public class SoftwareDao extends BaseDao {

    public SoftwareDao(RequestContext requestContext) {
        super(requestContext);
    }

    /**
     * Return a Software list.
     *
     * @param query
     * @return ..
     */
    public List<Software> getSoftwareList(QueryBits query, QueryHelper queryHelper) throws DatabaseException {
        Connection conn = getConnection();

        if (queryHelper == null) {
            queryHelper = new QueryHelper(SoftwareQueries.selectSoftwareListQuery(query));
        }

        try {
            List softwareList = new ArrayList();
            ResultSet rs = queryHelper.executeQuery(conn);

            while (rs.next()) {
                Software software = new Software();
                software.setId(rs.getInt("software_id"));
                software.setName(StringUtils.replaceNull(rs.getString("software_name")));
                software.setExpireDate(DatetimeUtils.getDate(rs, "software_expire_date"));
                software.setVersion(StringUtils.replaceNull(rs.getString("software_version")));
                software.setDescription(StringUtils.replaceNull(rs.getString("software_description")));
                software.setType(rs.getInt("software_type_id"));
                software.setTypeName(StringUtils.replaceNull(rs.getString("software_type")));
                software.setOs(rs.getInt("software_platform_id"));
                software.setOsName(StringUtils.replaceNull(rs.getString("software_platform")));
                software.setQuotedRetailPrice(StringUtils.replaceNull(rs.getString("quoted_retail_price")));
                software.setQuotedOemPrice(StringUtils.replaceNull(rs.getString("quoted_oem_price")));
                software.setManufacturerId(rs.getInt("manufacturer_company_id"));
                software.setManufacturerName(StringUtils.replaceNull(rs.getString("software_manufacturer")));
                software.setVendorId(rs.getInt("vendor_company_id"));
                software.setVendorName(StringUtils.replaceNull(rs.getString("software_vendor")));
                software.setLicensePurchased(rs.getInt("license_purchased"));
                software.setLicenseInstalled(rs.getInt("license_installed"));
                software.setLicenseAvailable(rs.getInt("license_available"));

                software.setOwner(new AccessUser());
                software.getOwner().setId(rs.getInt("software_owner_id"));
                software.getOwner().setUsername(rs.getString("software_owner_username"));
                software.getOwner().setDisplayName(rs.getString("software_owner_display_name"));

                softwareList.add(software);
            }
            return softwareList;

        } catch (Exception e) {
            // Database problem
            throw new DatabaseException(e, queryHelper);

        } finally {
            queryHelper.closeRs();
            closeConnection(conn);
        }
    }

    public List<Software> getLinkedSoftwareList(QueryBits query, ObjectLink objectMap) throws DatabaseException {
        QueryHelper queryHelper;

        if (objectMap.getLinkedObjectId() == null || objectMap.getLinkedObjectId() == 0) {
            queryHelper = new QueryHelper(SoftwareQueries.selectLinkedSoftwareListQuery(query));
            queryHelper.addInputInt(objectMap.getObjectId());
            queryHelper.addInputInt(objectMap.getObjectTypeId());
            queryHelper.addInputInt(objectMap.getLinkedObjectTypeId());

        } else {
            queryHelper = new QueryHelper(SoftwareQueries.selectObjectSoftwareListQuery(query));
            queryHelper.addInputInt(objectMap.getLinkedObjectId());
            queryHelper.addInputInt(objectMap.getLinkedObjectTypeId());
            queryHelper.addInputInt(objectMap.getObjectTypeId());
        }
        return getSoftwareList(query, queryHelper);
    }

    /**
     * Return Software count.
     *
     * @param query
     * @return ..
     */
    public int getCount(QueryBits query) throws DatabaseException {
        return getRowCount(SoftwareQueries.getSoftwareCountQuery(query));
    }

    public Software getSoftware(Integer softwareId) throws DatabaseException, ObjectNotFoundException {
        Connection conn = getConnection();

        QueryHelper queryHelper = new QueryHelper(SoftwareQueries.selectSoftwareDetailQuery());
        queryHelper.addInputInt(softwareId);
        queryHelper.addInputInt(softwareId);
        queryHelper.addInputInt(softwareId);

        try {
            ResultSet rs = queryHelper.executeQuery(conn);
            if (rs.next()) {
                Software software = new Software();
                software.setId(rs.getInt("software_id"));
                software.setName(StringUtils.replaceNull(rs.getString("software_name")));
                software.setVersion(StringUtils.replaceNull(rs.getString("software_version")));
                software.setDescription(StringUtils.replaceNull(rs.getString("software_description")));
                software.setType(rs.getInt("software_type"));
                software.setOs(rs.getInt("software_platform"));
                software.setQuotedRetailPrice(StringUtils.replaceNull(rs.getString("quoted_retail_price")));
                software.setQuotedOemPrice(StringUtils.replaceNull(rs.getString("quoted_oem_price")));
                software.setExpireDate(DatetimeUtils.getDate(rs, "software_expire_date"));
                software.setManufacturerId(rs.getInt("manufacturer_company_id"));
                software.setManufacturerName(StringUtils.replaceNull(rs.getString("software_manufacturer")));
                software.setVendorId(rs.getInt("vendor_company_id"));
                software.setVendorName(StringUtils.replaceNull(rs.getString("software_vendor")));
                software.setCountLicense(rs.getInt("license_count"));
                software.setCountFile(rs.getInt("file_count"));
                software.setCountBookmark(rs.getInt("bookmark_count"));
                software.setLicensePurchased(rs.getInt("license_purchased"));
                software.setLicenseInstalled(rs.getInt("license_installed"));
                software.setLicenseAvailable(rs.getInt("license_available"));
                software.setCreationDate(DatetimeUtils.getDate(rs, "creation_date"));
                software.setModificationDate(DatetimeUtils.getDate(rs, "modification_date"));

                software.setOwner(new AccessUser());
                software.getOwner().setId(rs.getInt("software_owner_id"));
                software.getOwner().setUsername(rs.getString("software_owner_username"));
                software.getOwner().setDisplayName(rs.getString("software_owner_display_name"));

                software.setCreator(new AccessUser());
                software.getCreator().setId(rs.getInt("creator"));
                software.getCreator().setUsername(rs.getString("creator_username"));
                software.getCreator().setDisplayName(rs.getString("creator_display_name"));

                software.setModifier(new AccessUser());
                software.getModifier().setId(rs.getInt("modifier"));
                software.getModifier().setUsername(rs.getString("modifier_username"));
                software.getModifier().setDisplayName(rs.getString("modifier_display_name"));

                return software;
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

    public List getCompanySoftwareList(QueryBits query, Integer companyId) throws DatabaseException {
        QueryHelper queryHelper = new QueryHelper(SoftwareQueries.selectCompanySoftwareQuery(query));
        queryHelper.addInputInt(companyId);

        return executeQueryReturnList(queryHelper);
    }

    public List getSoftwareCountGroupByCompany(QueryBits query) throws DatabaseException {
        QueryHelper queryHelper = new QueryHelper(SoftwareQueries.selectSoftwareCountGroupByCompanyQuery(query));

        return executeQueryReturnList(queryHelper);
    }

    public List getLicenseList(QueryBits query, Integer softwareId) throws DatabaseException {
        QueryHelper queryHelper = new QueryHelper(SoftwareQueries.selectSoftwareLicenseListQuery(query));
        queryHelper.addInputInt(softwareId);
        queryHelper.addInputInt(softwareId);

        return executeQueryReturnList(queryHelper);
    }

    public Map getSoftwareLicenseCount(Integer softwareId) throws DatabaseException {
        Connection conn = getConnection();

        QueryHelper queryHelper = new QueryHelper(SoftwareQueries.selectSoftwareLicenseCountQuery());
        queryHelper.addInputInt(softwareId);

        try {
            Map map = new HashMap();

            ResultSet rs = queryHelper.executeQuery(conn);
            while (rs.next()) {
                // Add each column to the map
                map.put(rs.getString("license_id"), rs.getString("license_id_count"));
            }
            return map;

        } catch (Exception e) {
            // Database problem
            throw new DatabaseException(e, queryHelper);

        } finally {
            queryHelper.closeRs();
            closeConnection(conn);
        }
    }

    public SoftwareLicense getSoftwareLicense(Integer softwareId, Integer licenseId)
            throws DatabaseException, ObjectNotFoundException {

        Connection conn = getConnection();

        QueryHelper queryHelper = new QueryHelper(SoftwareQueries.selectSoftwareLicense());
        queryHelper.addInputInt(softwareId);
        queryHelper.addInputInt(licenseId);

        try {
            ResultSet rs = queryHelper.executeQuery(conn);
            if (rs.next()) {
                SoftwareLicense softwareLicense = new SoftwareLicense();
                softwareLicense.setId(licenseId);
                softwareLicense.setSoftwareId(softwareId);
                softwareLicense.setKey(rs.getString("license_key"));
                softwareLicense.setNote(StringUtils.replaceNull(rs.getString("license_note")));
                softwareLicense.setEntitlement(rs.getInt("license_entitlement"));
                return softwareLicense;
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

    public List getSoftwareLicenseHardwareList(QueryBits query, Integer softwareId) throws DatabaseException {
        QueryHelper queryHelper = new QueryHelper(SoftwareQueries.selectSoftwareLicenseHardwareListQuery(query));
        queryHelper.addInputInt(softwareId);
        queryHelper.addInputInt(softwareId);

        return executeQueryReturnList(queryHelper);
    }

    public ActionMessages add(Software software) throws DatabaseException {
        Connection conn = getConnection();

        QueryHelper queryHelper = new QueryHelper(SoftwareQueries.insertSoftwareQuery());
        queryHelper.addOutputParam(Types.INTEGER);
        queryHelper.addInputStringConvertNull(software.getName());
        queryHelper.addInputStringConvertNull(software.getDescription());
        queryHelper.addInputIntegerConvertNull(software.getOwnerId());
        queryHelper.addInputIntegerConvertNull(software.getType());
        queryHelper.addInputIntegerConvertNull(software.getOs());
        queryHelper.addInputStringConvertNull(software.getQuotedRetailPrice());
        queryHelper.addInputStringConvertNull(software.getQuotedOemPrice());
        queryHelper.addInputIntegerConvertNull(software.getManufacturerId());
        queryHelper.addInputIntegerConvertNull(software.getVendorId());
        queryHelper.addInputStringConvertNull(software.getVersion());
        queryHelper.addInputStringConvertNull(DatetimeUtils.createDatetimeString(
                software.getExpireDateY(), software.getExpireDateM(), software.getExpireDateD()));
        queryHelper.addInputInt(requestContext.getUser().getId());

        try {
            queryHelper.executeProcedure(conn);
            // Put some values in the result.
            software.setId((Integer) queryHelper.getSqlOutputs().get(0));

            // Update custom fields
            if (!software.getCustomValues().isEmpty()) {
                AttributeDao attributeDao = new AttributeDao(requestContext);
                attributeDao.updateAttributeValue(conn, software.getId(), software.getCustomValues());
            }
        } catch (Exception e) {
            // Database problem
            handleError(e);

        } finally {
            closeConnection(conn);
        }
        return errors;
    }

    public ActionMessages update(Software software) throws DatabaseException {
        Connection conn = getConnection();

        QueryHelper queryHelper = new QueryHelper(SoftwareQueries.updateSoftwareQuery());
        queryHelper.addInputInt(software.getId());
        queryHelper.addInputStringConvertNull(software.getName());
        queryHelper.addInputStringConvertNull(software.getDescription());
        queryHelper.addInputIntegerConvertNull(software.getOwnerId());
        queryHelper.addInputIntegerConvertNull(software.getType());
        queryHelper.addInputIntegerConvertNull(software.getOs());
        queryHelper.addInputStringConvertNull(software.getQuotedRetailPrice());
        queryHelper.addInputStringConvertNull(software.getQuotedOemPrice());
        queryHelper.addInputIntegerConvertNull(software.getManufacturerId());
        queryHelper.addInputIntegerConvertNull(software.getVendorId());
        queryHelper.addInputStringConvertNull(software.getVersion());
        queryHelper.addInputStringConvertNull(DatetimeUtils.createDatetimeString(
                software.getExpireDateY(), software.getExpireDateM(), software.getExpireDateD()));
        queryHelper.addInputInt(requestContext.getUser().getId());

        try {
            queryHelper.executeProcedure(conn);

            // Update custom fields
            if (!software.getCustomValues().isEmpty()) {
                AttributeDao attributeDao = new AttributeDao(requestContext);
                attributeDao.updateAttributeValue(conn, software.getId(), software.getCustomValues());
            }
        } catch (Exception e) {
            // Database problem
            handleError(e);

        } finally {
            closeConnection(conn);
        }
        return errors;
    }

    public ActionMessages delete(Software software) throws DatabaseException {
        QueryHelper queryHelper = new QueryHelper(SoftwareQueries.deleteSoftwareQuery());
        queryHelper.addInputInt(ObjectTypes.SOFTWARE);
        queryHelper.addInputInt(software.getId());

        return executeProcedure(queryHelper);
    }

    public ActionMessages addLicense(RequestContext requestContext, SoftwareLicense license) throws DatabaseException {
        Connection conn = getConnection();

        QueryHelper queryHelper = new QueryHelper(SoftwareQueries.insertSoftwareLicenseQuery());
        queryHelper.addOutputParam(Types.INTEGER);
        queryHelper.addInputInt(license.getSoftwareId());
        queryHelper.addInputStringConvertNull(license.getKey());
        queryHelper.addInputStringConvertNull(license.getNote());
        queryHelper.addInputInt(license.getEntitlement());
        queryHelper.addInputInt(requestContext.getUser().getId());

        try {
            queryHelper.executeProcedure(conn);
            // Put some values in the result.
            license.setId((Integer) queryHelper.getSqlOutputs().get(0));

            // Update custom fields
            if (!license.getCustomValues().isEmpty()) {
                AttributeDao attributeDao = new AttributeDao(requestContext);
                attributeDao.updateAttributeValue(conn, license.getId(), license.getCustomValues());
            }
        } catch (Exception e) {
            // Database problem
            handleError(e);

        } finally {
            closeConnection(conn);
        }
        return errors;
    }

    public ActionMessages updateLicense(RequestContext requestContext, SoftwareLicense license) throws DatabaseException {
        Connection conn = getConnection();

        QueryHelper queryHelper = new QueryHelper(SoftwareQueries.updateSoftwareLicenseQuery());
        queryHelper.addInputInt(license.getSoftwareId());
        queryHelper.addInputInt(license.getId());
        queryHelper.addInputStringConvertNull(license.getKey());
        queryHelper.addInputStringConvertNull(license.getNote());
        queryHelper.addInputInt(license.getEntitlement());
        queryHelper.addInputInt(requestContext.getUser().getId());

        try {
            queryHelper.executeProcedure(conn);

            // Update custom fields
            if (!license.getCustomValues().isEmpty()) {
                AttributeDao attributeDao = new AttributeDao(requestContext);
                attributeDao.updateAttributeValue(conn, license.getId(), license.getCustomValues());
            }
        } catch (Exception e) {
            // Database problem
            handleError(e);

        } finally {
            closeConnection(conn);
        }
        return errors;
    }

    public ActionMessages deleteLicense(SoftwareLicense license) throws DatabaseException {
        QueryHelper queryHelper = new QueryHelper(SoftwareQueries.deleteSoftwareLicenseQuery());
        queryHelper.addInputInt(ObjectTypes.SOFTWARE_LICENSE);
        queryHelper.addInputInt(license.getSoftwareId());
        queryHelper.addInputInt(license.getId());

        return executeProcedure(queryHelper);
    }

    public ActionMessages resetSoftwareLicenseCount(SoftwareLicense license) throws DatabaseException {
        QueryHelper queryHelper = new QueryHelper(SoftwareQueries.updateSoftwareLicenseCountQuery());
        queryHelper.addInputInt(license.getSoftwareId());

        return executeProcedure(queryHelper);
    }

    public ActionMessages resetFileCount(Integer softwareId) throws DatabaseException {
        QueryHelper queryHelper = new QueryHelper(SoftwareQueries.updateSoftwareFileCountQuery());
        queryHelper.addInputInt(ObjectTypes.SOFTWARE);
        queryHelper.addInputInt(softwareId);

        return executeProcedure(queryHelper);
    }

    public ActionMessages resetBookmarkCount(Integer softwareId) throws DatabaseException {
        QueryHelper queryHelper = new QueryHelper(SoftwareQueries.updateSoftwareBookmarkCountQuery());
        queryHelper.addInputInt(ObjectTypes.SOFTWARE);
        queryHelper.addInputInt(softwareId);

        return executeProcedure(queryHelper);
    }
}