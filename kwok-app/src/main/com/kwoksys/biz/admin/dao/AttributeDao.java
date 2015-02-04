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
package com.kwoksys.biz.admin.dao;

import com.kwoksys.biz.admin.core.AdminUtils;
import com.kwoksys.biz.admin.dto.*;
import com.kwoksys.biz.base.BaseDao;
import com.kwoksys.framework.http.RequestContext;
import com.kwoksys.framework.connections.database.QueryBits;
import com.kwoksys.framework.connections.database.QueryHelper;
import com.kwoksys.framework.exceptions.DatabaseException;
import com.kwoksys.framework.exceptions.ObjectNotFoundException;
import com.kwoksys.framework.util.StringUtils;
import org.apache.struts.action.ActionMessages;

import java.sql.*;
import java.util.*;

/**
 * AttributeDao.
 */
public class AttributeDao extends BaseDao {

    public AttributeDao(RequestContext requestContext) {
        super(requestContext);
    }

    /**
     * Gets a list of attribute groups
     */
    public Map<Integer, AttributeGroup> getAttributeGroupsQuery(Integer objectTypeId) throws DatabaseException {
        Connection conn = getConnection();
        QueryHelper queryHelper = new QueryHelper(AdminQueries.selectAttributeGroupsQuery());
        queryHelper.addInputInt(objectTypeId);

        try {
            Map groups = new LinkedHashMap();
            ResultSet rs = queryHelper.executeQuery(conn);

            while (rs.next()) {
                AttributeGroup attributeGroup = new AttributeGroup();
                attributeGroup.setId(rs.getInt("attribute_group_id"));
                attributeGroup.setName(rs.getString("attribute_group_name"));
                groups.put(attributeGroup.getId(), attributeGroup);
            }

            return groups;

        } catch (Exception e) {
            // Database problem
            throw new DatabaseException(e, queryHelper);

        } finally {
            queryHelper.closeRs();
            closeConnection(conn);
        }
    }

    /**
     * Returns AttributeGroup object
     * @param attributeGroupId
     * @return
     * @throws DatabaseException
     * @throws ObjectNotFoundException
     */
    public AttributeGroup getAttributeGroup(Integer attributeGroupId, Integer objectTypeId) throws DatabaseException, ObjectNotFoundException {
        QueryHelper queryHelper = new QueryHelper(AdminQueries.selectAttributeGroupQuery());
        queryHelper.addInputInt(attributeGroupId);
        queryHelper.addInputInt(objectTypeId);

        Connection conn = getConnection();

        try {
            ResultSet rs = queryHelper.executeQuery(conn);
            if (rs.next()) {
                AttributeGroup attributeGroup = new AttributeGroup();
                attributeGroup.setId(attributeGroupId);
                attributeGroup.setName(rs.getString("attribute_group_name"));
                attributeGroup.setObjectTypeId(objectTypeId);

                return attributeGroup;
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
     * Gets attributes.
     *
     * @return ..
     */
    public Map<Integer, Attribute> getAttributeList(QueryBits query) throws DatabaseException {
        Connection conn = getConnection();
        QueryHelper queryHelper = new QueryHelper(AdminQueries.selectAttributeListQuery(query));

        try {
            Map<Integer, Attribute> attributes = new LinkedHashMap();
            ResultSet rs = queryHelper.executeQuery(conn);

            while (rs.next()) {
                Attribute attribute = new Attribute();
                attribute.setId(rs.getInt("attribute_id"));
                attribute.setName(StringUtils.replaceNull(rs.getString("attribute_name")));
                attribute.setUrl(StringUtils.replaceNull(rs.getString("attribute_url")));
                attribute.setType(rs.getInt("attribute_type"));
                attribute.setAttributeOptions(StringUtils.replaceNull(rs.getString("attribute_option")));
                attribute.setConvertUrl(rs.getBoolean("attribute_convert_url"));
                attribute.setObjectTypeId(rs.getInt("object_type_id"));
                attribute.setDefaultAttrFieldId(rs.getInt("default_attribute_field_id"));
                attribute.setAttributeGroupId(rs.getInt("attribute_group_id"));
                attribute.setRequired(rs.getInt("is_required") == 1);
                attribute.setInputMask(StringUtils.replaceNull(rs.getString("input_mask")));
                attribute.setDescription(StringUtils.replaceNull(rs.getString("description")));
                attribute.setTypeCurrencySymbol(StringUtils.replaceNull(rs.getString("type_currency_symbol")));
                attributes.put(attribute.getId(), attribute);
            }
            return attributes;

        } catch (Exception e) {
            // Database problem
            throw new DatabaseException(e, queryHelper);

        } finally {
            queryHelper.closeRs();
            closeConnection(conn);
        }
    }

    public boolean hasCustomFields(QueryBits query) throws DatabaseException {
        Connection conn = getConnection();
        QueryHelper queryHelper = new QueryHelper(AdminQueries.selectAttributeListQuery(query));

        try {
            ResultSet rs = queryHelper.executeQuery(conn);

            return rs.next();

        } catch (Exception e) {
            // Database problem
            throw new DatabaseException(e, queryHelper);

        } finally {
            queryHelper.closeRs();
            closeConnection(conn);
        }
    }

    /**
     * Get the attribute detail.
     *
     * @return ..
     */
    public Attribute getAttribute(Integer attributeId) throws DatabaseException, ObjectNotFoundException {
        Connection conn = getConnection();

        QueryHelper queryHelper = new QueryHelper(AdminQueries.selectAttributeDetailQuery());
        queryHelper.addInputInt(attributeId);

        try {
            ResultSet rs = queryHelper.executeQuery(conn);
            if (rs.next()) {
                Attribute attribute = new Attribute();
                attribute.setId(rs.getInt("attribute_id"));
                attribute.setName(rs.getString("attribute_name"));
                attribute.setCustomAttr(rs.getBoolean("is_custom_attr"));
                attribute.setUrl(StringUtils.replaceNull(rs.getString("attribute_url")));
                attribute.setType(rs.getInt("attribute_type"));
                attribute.setAttributeOptions(StringUtils.replaceNull(rs.getString("attribute_option")));
                attribute.setConvertUrl(rs.getBoolean("attribute_convert_url"));
                attribute.setObjectTypeId(rs.getInt("object_type_id"));
                attribute.setObjectKey(rs.getString("object_key"));
                attribute.setAttributeGroupId(rs.getInt("attribute_group_id"));
                // is_editable was originally for adding/editing attr fields
                attribute.setAttrFieldsEditable(rs.getBoolean("is_editable"));
                attribute.setInputMask(StringUtils.replaceNull(rs.getString("input_mask")));
                attribute.setDescription(StringUtils.replaceNull(rs.getString("description")));
                attribute.setTypeCurrencySymbol(StringUtils.replaceNull(rs.getString("type_currency_symbol")));

                int defaultAttrFieldId = rs.getInt("default_attribute_field_id");
                attribute.setDefaultAttrFieldId(defaultAttrFieldId);
                attribute.setDefaultAttrFieldEditable(defaultAttrFieldId != 0);

                int isRequired = rs.getInt("is_required");
                // As a workaround, is_required not equal to -1 is editable
                attribute.setRequired(isRequired == 1);
                attribute.setRequiredFieldEditable(isRequired != -1);

                return attribute;
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
     * Get a list custom attributes.
     *
     * @return ..
     */
    public Map<Integer, AttributeField> getAttributeFields(QueryBits query) throws DatabaseException {
        Connection conn = getConnection();

        QueryHelper queryHelper = new QueryHelper(AdminQueries.selectAttributeFieldListQuery(query));

        try {
            Map attrFields = new LinkedHashMap();
            ResultSet rs = queryHelper.executeQuery(conn);

            while (rs.next()) {
                AttributeField attrField = new AttributeField();
                attrField.setAttributeId(rs.getInt(Attribute.ATTR_ID));
                attrField.setId(rs.getInt("attribute_field_id"));
                attrField.setName(rs.getString("attribute_field_name"));
                attrField.setDescription(StringUtils.replaceNull(rs.getString("attribute_field_description")));
                attrField.setIconId(rs.getInt("icon_id"));
                attrField.setSystemIcon(rs.getBoolean("is_system_icon"));
                attrField.setIconPath(StringUtils.replaceNull(rs.getString("icon_path")));
                attrField.setDisabled(rs.getBoolean(AttributeField.IS_DISABLED));
                attrFields.put(attrField.getId(), attrField);
            }
            return attrFields;

        } catch (Exception e) {
            // Database problem
            throw new DatabaseException(e, queryHelper);

        } finally {
            queryHelper.closeRs();
            closeConnection(conn);
        }
    }

    public AttributeField getAttributeField(QueryBits query, Integer attrFieldId) throws DatabaseException, ObjectNotFoundException {
        Connection conn = getConnection();

        QueryHelper queryHelper = new QueryHelper(AdminQueries.selectAttributeFieldQuery(query));
        queryHelper.addInputInt(attrFieldId);

        try {
            ResultSet rs = queryHelper.executeQuery(conn);
            if (rs.next()) {
                AttributeField attrField = new AttributeField();
                attrField.setAttributeId(rs.getInt("attribute_id"));
                attrField.setId(rs.getInt("attribute_field_id"));
                attrField.setName(rs.getString("attribute_field_name"));
                attrField.setDescription(StringUtils.replaceNull(rs.getString("attribute_field_description")));
                attrField.setIconId(rs.getInt("icon_id"));
                attrField.setSystemIcon(rs.getBoolean("is_system_icon"));
                attrField.setIconPath(StringUtils.replaceNull(rs.getString("icon_path")));
                attrField.setDisabled(rs.getBoolean(AttributeField.IS_DISABLED));
                return attrField;
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
     * Gets custom attribute values map, this is more useful in map than list format.
     * @param query
     * @return
     * @throws DatabaseException
     */
    public Map getCustomAttributeValueMap(QueryBits query) throws DatabaseException {
        Connection conn = getConnection();

        QueryHelper queryHelper = new QueryHelper(AdminQueries.selectAttributeValuesQuery(query));

        try {
            Map values = new HashMap();
            ResultSet rs = queryHelper.executeQuery(conn);

            while (rs.next()) {
                Integer attrId = rs.getInt("attribute_id");
                String attrValue = StringUtils.replaceNull(rs.getString("attr_value"));
                Integer attributeType = rs.getInt("attribute_type");

                if (attributeType.equals(Attribute.ATTR_TYPE_MULTISELECT)) {
                    List list = (List) values.get(attrId);
                    if (list == null) {
                        list = new ArrayList();
                        values.put(attrId, list);
                    }
                    list.add(attrValue);
                } else {
                    values.put(attrId, attrValue);
                }
            }
            return values;

        } catch (Exception e) {
            // Database problem
            throw new DatabaseException(e, queryHelper);

        } finally {
            queryHelper.closeRs();
            closeConnection(conn);
        }
    }

    /**
     * Returns the mappings of an attribute type and custom attributes
     * @param query
     * @return
     * @throws DatabaseException
     */
    public List getAttributeFieldTypes(QueryBits query, Integer attrFieldId) throws DatabaseException {
        Connection conn = getConnection();

        QueryHelper queryHelper = new QueryHelper(AdminQueries.selectAttributeFieldTypesQuery(query));
        queryHelper.addInputInt(attrFieldId);

        try {
            List values = new ArrayList();
            ResultSet rs = queryHelper.executeQuery(conn);

            while (rs.next()) {
                values.add(rs.getInt("linked_attribute_id"));
            }
            return values;

        } catch (Exception e) {
            // Database problem
            throw new DatabaseException(e, queryHelper);

        } finally {
            queryHelper.closeRs();
            closeConnection(conn);
        }
    }

    /**
     * Returns the mappings of a Custom Field and System Fields
     * @param query
     * @return
     * @throws DatabaseException
     */
    public Set getAttributeFieldTypesByField(QueryBits query, Integer linkedAttrId) throws DatabaseException {
        Connection conn = getConnection();

        QueryHelper queryHelper = new QueryHelper(AdminQueries.selectAttributeFieldTypesByLinkedAttrQuery(query));
        queryHelper.addInputInt(linkedAttrId);

        try {
            Set values = new HashSet();
            ResultSet rs = queryHelper.executeQuery(conn);

            while (rs.next()) {
                values.add(rs.getInt("attribute_field_id"));
            }
            return values;

        } catch (Exception e) {
            // Database problem
            throw new DatabaseException(e, queryHelper);

        } finally {
            queryHelper.closeRs();
            closeConnection(conn);
        }
    }

    /**
     * Gets icons
     * @param attributeId
     * @return
     * @throws DatabaseException
     */
    public List<Icon> getIcons(Integer attributeId) throws DatabaseException {
        Connection conn = getConnection();

        QueryHelper queryHelper = new QueryHelper(AdminQueries.selectAttributeIconsQuery());
        queryHelper.addInputInt(attributeId);

        try {
            List icons = new ArrayList();
            ResultSet rs = queryHelper.executeQuery(conn);

            while (rs.next()) {
                Icon icon = new Icon();
                icon.setId(rs.getInt("icon_id"));
                icon.setSystemIcon(rs.getInt("is_system_icon") == 1);
                if (icon.isSystemIcon()) {
                    icon.setAppPath(StringUtils.replaceNull(rs.getString("icon_path")));
                } else {
                    icon.setPath(StringUtils.replaceNull(rs.getString("icon_path")));
                }
                icon.setAttributeId(rs.getInt("attribute_id"));
                icons.add(icon);
            }
            return icons;

        } catch (Exception e) {
            // Database problem
            throw new DatabaseException(e, queryHelper);

        } finally {
            queryHelper.closeRs();
            closeConnection(conn);
        }
    }

    /**
     * Adds attribute group.
     *
     * @return ..
     */
    public ActionMessages addAttributeGroup(AttributeGroup attributeGroup) throws DatabaseException {
        QueryHelper queryHelper = new QueryHelper(AdminQueries.addAttributeGroupQuery());
        queryHelper.addOutputParam(Types.INTEGER);
        queryHelper.addInputInt(attributeGroup.getObjectTypeId());
        queryHelper.addInputString(attributeGroup.getName());

        executeProcedure(queryHelper);

        // Put some values in the result.
        if (errors.isEmpty()) {
            attributeGroup.setId((Integer)queryHelper.getSqlOutputs().get(0));
        }

        return errors;
    }

    /**
     * Updates attribute group.
     *
     * @return ..
     */
    public ActionMessages updateAttributeGroup(AttributeGroup attributeGroup) throws DatabaseException {
        QueryHelper queryHelper = new QueryHelper(AdminQueries.updateAttributeGroupQuery());
        queryHelper.addInputInt(attributeGroup.getId());
        queryHelper.addInputInt(attributeGroup.getObjectTypeId());
        queryHelper.addInputString(attributeGroup.getName());

        return executeProcedure(queryHelper);
    }

    /**
     * Deletes attribute group.
     *
     * @return ..
     */
    public ActionMessages deleteAttributeGroup(AttributeGroup attributeGroup) throws DatabaseException {
        QueryHelper queryHelper = new QueryHelper(AdminQueries.deleteAttributeGroupQuery());
        queryHelper.addInputInt(attributeGroup.getId());
        queryHelper.addInputInt(attributeGroup.getObjectTypeId());

        return executeProcedure(queryHelper);
    }

    /**
     * Adds Custom Attribute.
     *
     * @return ..
     */
    public ActionMessages addAttribute(Attribute attr) throws DatabaseException {
        Connection conn = getConnection();

        QueryHelper queryHelper = new QueryHelper(AdminQueries.getAttributeAddQuery());
        queryHelper.addOutputParam(Types.INTEGER);
        queryHelper.addInputInt(attr.getObjectTypeId());
        queryHelper.addInputString(attr.getName());
        queryHelper.addInputStringConvertNull(attr.getDescription());
        queryHelper.addInputInt(attr.getType());
        queryHelper.addInputStringConvertNull(attr.getAttributeOption());
        queryHelper.addInputInt(attr.isConvertUrl() ? 1 : 0);
        queryHelper.addInputStringConvertNull(attr.getUrl());
        queryHelper.addInputIntegerConvertNull(null);
        queryHelper.addInputIntegerConvertNull(attr.getAttributeGroupId());
        queryHelper.addInputStringConvertNull(attr.getInputMask());
        queryHelper.addInputStringConvertNull(attr.getTypeCurrencySymbol());

        try {
            queryHelper.executeProcedure(conn);
            // Put some values in the result.
            attr.setId((Integer)queryHelper.getSqlOutputs().get(0));

            // Updates attribute type field mappings
            updateAttributeFieldMap(conn, attr);

        } catch (Exception e) {
            // Database problem
            handleError(e);

        } finally {
            closeConnection(conn);
        }
        return errors;
    }

    /**
     * Updates Custom Attribute.
     *
     * @return ..
     */
    public ActionMessages updateCustomAttribute(Attribute attr) throws DatabaseException {
        Connection conn = getConnection();

        QueryHelper queryHelper = new QueryHelper(AdminQueries.getAttributeUpdateQuery());
        queryHelper.addInputInt(attr.getId());
        queryHelper.addInputString(attr.getName());
        queryHelper.addInputStringConvertNull(attr.getDescription());
        queryHelper.addInputInt(attr.getType());
        queryHelper.addInputStringConvertNull(attr.getAttributeOption());
        queryHelper.addInputInt(attr.isConvertUrl() ? 1 : 0);
        queryHelper.addInputStringConvertNull(attr.getUrl());
        queryHelper.addInputIntegerConvertNull(attr.getAttributeGroupId());
        queryHelper.addInputStringConvertNull(attr.getInputMask());
        queryHelper.addInputStringConvertNull(attr.getTypeCurrencySymbol());

        try {
            queryHelper.executeProcedure(conn);

            // Updates attribute type field mappings
            updateAttributeFieldMap(conn, attr);

        } catch (Exception e) {
            // Database problem
            handleError(e);

        } finally {
            closeConnection(conn);
        }
        return errors;
    }

    /**
     * Updates system attribute.
     * @param attr
     * @return
     * @throws DatabaseException
     */
    public ActionMessages updateSystemAttribute(Attribute attr) throws DatabaseException {
        QueryHelper queryHelper = new QueryHelper(AdminQueries.updateSystemAttributeQuery());
        queryHelper.addInputInt(attr.getId());
        queryHelper.addInputInt(attr.isRequired() ? 1 : 0);
        queryHelper.addInputIntegerConvertNull(attr.getDefaultAttrFieldId());

        return executeProcedure(queryHelper);
    }

    /**
     * Deletes Custom Attribute.
     *
     * @return ..
     */
    public ActionMessages deleteAttribute(Attribute attr) throws DatabaseException {
        QueryHelper queryHelper = new QueryHelper(AdminQueries.deleteAttributeQuery());
        queryHelper.addInputInt(attr.getId());

        return executeProcedure(queryHelper);
    }

    /**
     * Adds Attribute.
     *
     * @return ..
     */
    public ActionMessages addAttributeField(AttributeField attrField) throws DatabaseException {
        Connection conn = getConnection();

        QueryHelper queryHelper = new QueryHelper(AdminQueries.insertAttributeFieldQuery());
        queryHelper.addOutputParam(Types.INTEGER);
        queryHelper.addInputInt(attrField.getAttributeId());
        queryHelper.addInputStringConvertNull(attrField.getName());
        queryHelper.addInputStringConvertNull(attrField.getDescription());

        if (attrField.isDisabled()) {
            queryHelper.addInputInt(1);
        } else {
            queryHelper.addInputIntegerConvertNull(null);
        }
        queryHelper.addInputIntegerConvertNull(attrField.getIconId());

        try {
            queryHelper.executeProcedure(conn);

            // Put some values in the result
            attrField.setId((Integer)queryHelper.getSqlOutputs().get(0));

            // Updates attribute type field mappings
            updateAttributeFieldMap(conn, attrField);

        } catch (Exception e) {
            // Database problem
            handleError(e);

        } finally {
            closeConnection(conn);
        }
        return errors;
    }

    /**
     * Updates Attribute.
     *
     * @return ..
     */
    public ActionMessages updateAttributeField(AttributeField attrField) throws DatabaseException {
        Connection conn = getConnection();

        QueryHelper queryHelper = new QueryHelper(AdminQueries.updateAttributeFieldQuery());
        queryHelper.addInputInt(attrField.getAttributeId());
        queryHelper.addInputInt(attrField.getId());
        queryHelper.addInputStringConvertNull(attrField.getName());
        queryHelper.addInputStringConvertNull(attrField.getDescription());

        if (attrField.isDisabled()) {
            queryHelper.addInputInt(1);
        } else {
            queryHelper.addInputIntegerConvertNull(null);
        }
        queryHelper.addInputIntegerConvertNull(attrField.getIconId());

        try {
            queryHelper.executeProcedure(conn);

            // Updates attribute type field mappings
            updateAttributeFieldMap(conn, attrField);

        } catch (Exception e) {
            // Database problem
            handleError(e);

        } finally {
            closeConnection(conn);
        }
        return errors;
    }

    /**
     * Updates attribute value.
     * @return
     * @throws DatabaseException
     */
    public void updateAttributeValue(Connection conn, Integer objectId, Map<Integer, AttributeValue> customValues) throws DatabaseException {
        String sqlQuery = AdminQueries.updateAttributeValueQuery();

        for (AttributeValue value : customValues.values()) {
            QueryHelper queryHelper = new QueryHelper(sqlQuery);
            queryHelper.addInputInt(objectId);
            queryHelper.addInputInt(value.getAttributeId());
            queryHelper.addInputStringConvertNull(value.getRawValue());

            queryHelper.executeProcedure(conn);
        }
    }

    /**
     * Updates attribute field maps
     * @param conn
     * @param attrField
     * @param customAttrIds
     * @throws SQLException
     */
    public void updateAttributeFieldMap(Connection conn, AttributeField attrField) throws DatabaseException {
        // Empty existing mappings
        QueryHelper queryHelper = new QueryHelper(AdminQueries.deleteAttributeFieldMapQuery());
        queryHelper.addInputInt(attrField.getAttributeId());
        queryHelper.addInputInt(attrField.getId());
        queryHelper.addInputIntegerConvertNull(0);

        queryHelper.executeProcedure(conn);

        // Inserts new mappings, one for each linked attribute id
        if (attrField.getLinkedAttrIds() != null) {
            for (Integer linkedAttrId : attrField.getLinkedAttrIds()) {
                updateAttributeFieldMap(conn, attrField.getAttributeId(), attrField.getId(), linkedAttrId);
            }
        }
    }

    public void updateAttributeFieldMap(Connection conn, Attribute attr) throws DatabaseException {
        // The update is not applicable
        if (attr.getAttrFieldIds() == null) {
            return;
        }

        Integer objectAttrType = AdminUtils.getObjectTypeMap().get(attr.getObjectTypeId());
        if (objectAttrType == null) {
            return;
        }
        
        // Empty existing mappings
        QueryHelper queryHelper = new QueryHelper(AdminQueries.deleteAttributeFieldMapQuery());
        queryHelper.addInputInt(objectAttrType);
        queryHelper.addInputIntegerConvertNull(0);
        queryHelper.addInputInt(attr.getId());

        queryHelper.executeProcedure(conn);

        // Inserts new mappings, one for each attribute field
        for (Integer attrFieldId : attr.getAttrFieldIds()) {
            updateAttributeFieldMap(conn, AdminUtils.getObjectTypeMap().get(attr.getObjectTypeId()),
                    attrFieldId, attr.getId());
        }
    }

     private void updateAttributeFieldMap(Connection conn, Integer attrId, Integer attrFieldId, Integer linkedAttrId)
            throws DatabaseException {

        QueryHelper queryHelper = new QueryHelper(AdminQueries.updateAttributeFieldMapQuery());
        queryHelper.addInputInt(attrId);
        queryHelper.addInputInt(attrFieldId);
        queryHelper.addInputInt(linkedAttrId);

        queryHelper.executeProcedure(conn);
    }
}
