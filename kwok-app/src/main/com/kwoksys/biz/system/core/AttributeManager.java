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
package com.kwoksys.biz.system.core;

import com.kwoksys.biz.ServiceProvider;
import com.kwoksys.biz.admin.AdminService;
import com.kwoksys.biz.admin.core.AttributeSearch;
import com.kwoksys.biz.admin.dao.AdminQueries;
import com.kwoksys.biz.admin.dto.Attribute;
import com.kwoksys.biz.admin.dto.AttributeField;
import com.kwoksys.biz.base.BaseManager;
import com.kwoksys.biz.base.BaseObject;
import com.kwoksys.framework.http.RequestContext;
import com.kwoksys.framework.connections.database.QueryBits;
import com.kwoksys.framework.exceptions.DatabaseException;
import com.kwoksys.framework.license.LicenseManager;
import com.kwoksys.framework.properties.Localizer;
import com.kwoksys.framework.session.CacheManager;
import org.apache.struts.util.LabelValueBean;

import java.util.*;

/**
 * AttributeManager.
 */
public class AttributeManager extends BaseManager {

    public AttributeManager(RequestContext requestContext) {
        super(requestContext);
    }

    /**
     * Returns Attribute data type, such as One line text, Drop-down, Date, etc.
     * @param requestContext
     * @return
     */
    public static List<LabelValueBean> getAttrDataTypeOptions(RequestContext requestContext, Integer selectedAttrType) {
        List attrTypeOptions = new ArrayList();
        for (Integer option : Attribute.ATTR_TYPE_OPTION_LIST) {
            if (option.equals(Attribute.ATTR_TYPE_CURRENCY)
                    && !Attribute.ATTR_TYPE_CURRENCY.equals(selectedAttrType)
                    && !LicenseManager.isCommercialEdition()) {
                continue;
            }

            attrTypeOptions.add(new LabelValueBean(Localizer.getText(requestContext, "admin.attribute.attribute_type."
                    + option), String.valueOf(option)));
        }
        return attrTypeOptions;
    }

    public List<LabelValueBean> getAttrValueOptionsCache(Integer attributeId) throws DatabaseException {
        return getAttrValueOptionsCache(attributeId, new ArrayList());
    }

    /**
     * Gets an attribute field list and returns the list in List<LabelValueBean> format.
     */
    public List<LabelValueBean> getAttrValueOptionsCache(Integer attributeId, List options) throws DatabaseException {
        // Get object attributes from cache.
        for (AttributeField attrField : new CacheManager(requestContext).getAttributeFieldsCache(attributeId).values()) {
            if (attrField.getId() < 0) {
                options.add(new LabelValueBean(Localizer.getText(requestContext, "system.attribute_field." + attrField.getName()), String.valueOf(attrField.getId())));
            } else {
                options.add(new LabelValueBean(attrField.getName(), String.valueOf(attrField.getId())));
            }
        }
        return options;
    }

    /**
     * Gets an attribute field list and returns the list in Object format.
     *
     * @return ..
     */
    public Map<Integer, AttributeField> getAttrFieldMapCache(Integer attributeId) throws DatabaseException {
        // Get object attributes from cache.
        return new CacheManager(requestContext).getAttributeFieldsCache(attributeId);
    }

    /**
     * Returns a name/AttributeField map for lookup by names.
     * @param attributeId
     * @return
     * @throws DatabaseException
     */
    public Map<String, AttributeField> getAttrNameMapCache(Integer attributeId) throws DatabaseException {
        Map<String, AttributeField> map = new HashMap();
        Collection<AttributeField> values = new CacheManager(requestContext).getAttributeFieldsCache(attributeId).values();

        for (AttributeField value : values) {
            map.put(value.getName(), value);
        }
        return map;
    }

    /**
     * Gets an attribute field list, and returns the list in Options format.
     * This only shows attribute fields that are not disabled, or in some cases, attribute field is disabled but has
     * been used previously, we want to show it too.
     */
    public List<LabelValueBean> getActiveAttrFieldOptionsCache(Integer attributeId, Integer selectedAttrFieldId, List options)
            throws DatabaseException {

        // Get object attributes from cache.
        for (AttributeField attrField : getAttrFieldMapCache(attributeId).values()) {
            if (!attrField.isDisabled() || attrField.getId().equals(selectedAttrFieldId)) {
                options.add(new LabelValueBean(attrField.getName(), String.valueOf(attrField.getId())));
            }
        }
        return options;
    }

    /**
     * Returns an attribute field list. This only shows attribute fields that are not disabled
     */
    public List<LabelValueBean> getActiveAttrFieldOptionsCache(Integer attributeId) throws DatabaseException {
        return getActiveAttrFieldOptionsCache(attributeId, 0, new ArrayList());
    }

    /**
     * Gets an attribute field list, and returns the list in Options format.
     * This only shows attribute fields that are not disabled
     */
    public List<LabelValueBean> getActiveAttrFieldOptionsCache(Integer attributeId, List options) throws DatabaseException {
        return getActiveAttrFieldOptionsCache(attributeId, 0, options);
    }

    /**
     * Gets an attribute field list, including the selectedAttrFieldId.
     * This only shows attribute fields that are not disabled
     */
    public List<LabelValueBean> getActiveAttrFieldOptionsCache(Integer attributeId, Integer selectedAttrFieldId) throws DatabaseException {
        return getActiveAttrFieldOptionsCache(attributeId, selectedAttrFieldId, new ArrayList());
    }

    /**
     * Gets attribute field name for a given attrFieldId.
     *
     * @return ..
     */
    public String getAttrFieldNameCache(Integer attributeId, Integer attrFieldId) throws DatabaseException {
        AttributeField attrField = getAttrFieldMapCache(attributeId).get(attrFieldId);
        if (attrField == null) {
            return "";
        } else if (attrField.getId() < 0) {
            return Localizer.getText(requestContext, "system.attribute_field." + attrField.getName());
        } else {
            return attrField.getName();
        }
    }

    public Map<Integer, Attribute> getSystemAttributes(BaseObject baseObject) {
        // Get system attributes for this object type
        AttributeSearch attributeSearch = new AttributeSearch();
        attributeSearch.put(AttributeSearch.OBJECT_TYPE_ID_EUALS, baseObject.getObjectTypeId());
        attributeSearch.put(AttributeSearch.IS_CUSTOM_ATTR, false);

        QueryBits query = new QueryBits(attributeSearch);

        AdminService adminService = ServiceProvider.getAdminService(requestContext);

        Map<Integer, Attribute> map = new LinkedHashMap();
        try {
            Collection<Attribute> attrs = adminService.getAttributes(query).values();
            for (Attribute attr : attrs) {
                map.put(attr.getId(), attr);
            }
        } catch (Exception e) {
            /* ignored */
        }
        return map;
    }

    /**
     * Returns a list of object types allowed for custom fields.
     * @return
     */
    public static List<Integer> getCustomFieldObjectTypes() {
        return Arrays.asList(ObjectTypes.HARDWARE, ObjectTypes.HARDWARE_COMPONENT, ObjectTypes.SOFTWARE, ObjectTypes.SOFTWARE_LICENSE,
                ObjectTypes.COMPANY, ObjectTypes.CONTRACT, ObjectTypes.ISSUE, ObjectTypes.USER);
    }

    public Map<Integer, Attribute> getCustomFieldMap(Integer objectTypeId) throws DatabaseException {
        AttributeSearch attributeSearch = new AttributeSearch();
        attributeSearch.put(AttributeSearch.OBJECT_TYPE_ID_EUALS, objectTypeId);
        attributeSearch.put(AttributeSearch.IS_CUSTOM_ATTR, true);

        QueryBits query = new QueryBits(attributeSearch);
        query.addSortColumn(AdminQueries.getOrderByColumn(Attribute.ATTR_NAME));

        AdminService adminService = ServiceProvider.getAdminService(requestContext);
        return adminService.getAttributes(query);
    }

    /**
     * Gets a list of custom field objects.
     * @param objectTypeId
     * @return
     * @throws Exception
     */
    public Collection<Attribute> getCustomFieldList(Integer objectTypeId) throws DatabaseException {
        return getCustomFieldMap(objectTypeId).values();
    }

    /**
     * This is a wrapper to put custom fields in LabelValueBean format.
     * @param objectTypeId
     * @return
     * @throws Exception
     */
    public List<LabelValueBean> getCustomFieldOptions(Integer objectTypeId) throws DatabaseException {
        List fieldOptions = new ArrayList();

        for (Attribute attr : getCustomFieldList(objectTypeId)) {
            fieldOptions.add(new LabelValueBean(attr.getName(), String.valueOf(attr.getId())));
        }
        return fieldOptions;
    }    
}
