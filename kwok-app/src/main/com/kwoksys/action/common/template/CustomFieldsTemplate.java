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
package com.kwoksys.action.common.template;

import com.kwoksys.biz.ServiceProvider;
import com.kwoksys.biz.admin.AdminService;
import com.kwoksys.biz.admin.core.AdminUtils;
import com.kwoksys.biz.admin.dto.Attribute;
import com.kwoksys.biz.admin.dto.AttributeGroup;
import com.kwoksys.biz.base.BaseObject;
import com.kwoksys.biz.base.BaseObjectForm;
import com.kwoksys.biz.base.BaseTemplate;
import com.kwoksys.biz.system.core.AttributeManager;
import com.kwoksys.biz.system.core.Keywords;
import com.kwoksys.biz.system.core.configs.ConfigManager;
import com.kwoksys.framework.session.CacheManager;
import com.kwoksys.framework.ui.SelectOneLabelValueBean;
import com.kwoksys.framework.util.CustomFieldFormatter;
import com.kwoksys.framework.util.HtmlUtils;
import com.kwoksys.framework.util.StringUtils;
import org.apache.struts.util.LabelValueBean;

import java.util.*;

/**
 * CustomFieldsTemplate
 */
public class CustomFieldsTemplate extends BaseTemplate {

    private Map<String, List> customFields = new TreeMap();
    private Integer objectTypeId;
    private Integer objectId;
    private Integer objectAttrTypeId;
    private boolean expandCustomFields;
    private BaseObjectForm form;

    private boolean editPage = false;

    /**
     * Whether the edit page has open and close html table tags.
     */
    private boolean isPartialTable = false;

    /**
     * Determines whether to show the "Custom Fields" header. Default is true.
     * On some pages, it's duplicative to show the "Custom Fields" header.
     */
    private boolean showDefaultHeader = true;

    public CustomFieldsTemplate() {
        super(CustomFieldsTemplate.class);
    }

    public void applyTemplate() throws Exception {
        expandCustomFields = ConfigManager.app.isLoadCustomFields();

        AdminService adminService = ServiceProvider.getAdminService(requestContext);

        Map valueMap = null;

        // If objectId is given, put attribute values in a map for retrieval later
        if (objectId != null) {
            valueMap = adminService.getCustomAttributeValueMap(objectTypeId, objectId);
        }

        Collection<Attribute> attrs = new AttributeManager(requestContext).getCustomFieldList(objectTypeId);
        if (!attrs.isEmpty()) {
        // Get a list of attributes supposed to be shown for this attribute type
        List<Integer> mappedAttrIds = adminService.getAttributeFieldTypes(objectAttrTypeId);

        Map<Integer, AttributeGroup> groupMap = new CacheManager(requestContext).getCustomAttrGroupsCache(objectTypeId);

        for (Attribute attr : attrs) {
            String value = StringUtils.replaceNull(getFieldValue(valueMap, attr));

            // Apply some logic to determine whether to include this attribute.
            // If the attribute is for the object type specified, it's included.
            // If the attribute value is not empty, it's included.
            if (objectAttrTypeId != null) {
                if (!mappedAttrIds.contains(attr.getId()) && value.isEmpty()) {
                    continue;
                }
            }

            if (form != null && form.isResubmit()) {
                if (form.getCustomValues() != null) {
                    value = form.getCustomValues().get(attr.getId()).getAttributeValue();
                } else {
                    value = requestContext.getParameterString("attrId"+attr.getId());
                }
            }

            Map attrMap = new HashMap();
            attrMap.put("name", attr.getName());
            attrMap.put("attrId", attr.getId());
            attrMap.put("attr", attr);
            attrMap.put("value", value);

            if (isEditPage()) {
                // This is for selectbox or radio button to select correct value
                attrMap.put("attrId" + attr.getId(), HtmlUtils.encode(value));

                // Populate select options
                if (Attribute.supportOptions(attr.getType())) {
                    List<LabelValueBean> newOptions = new ArrayList();
                    List<String> currentOptions = attr.getAttributeOptions();

                    // Make it a "select one" option if the option matches keyword "_blank"
                    if (currentOptions.contains(Keywords.OPTIONAL_CUSTOM_FIELD)) {
                        newOptions.add(new SelectOneLabelValueBean(requestContext));
                    }
                    // If the list doesn't contain existing value, add it to the new list.
                    if (!value.isEmpty() && !currentOptions.contains(value)) {
                        value = HtmlUtils.encode(value);
                        newOptions.add(new LabelValueBean(value, value));
                    }

                    for (String string : currentOptions) {
                        if (!string.equals(Keywords.OPTIONAL_CUSTOM_FIELD)) {
                            string = HtmlUtils.encode(string);
                            newOptions.add(new LabelValueBean(string, string));
                        }
                    }
                    attrMap.put("attrOptions", newOptions);
                }
            } else {
                // Display dynamic URL for custom fields
                // URL can be in one of the following formats:
                // 1) "http://www.kwoksys.com/?${CUSTOM_FIELD_VALUE}"
                // 2) "http://www.kwoksys.com/?${CUSTOM_FIELD_VALUE}|${CUSTOM_FIELD_VALUE}"
                if (!value.isEmpty()) {
                    if (!attr.getUrl().isEmpty()) {
                        String[] strings = attr.getUrl().split("\\|");
                        String attrLinkUrl = strings[0].replace(Keywords.CUSTOM_FIELD_VALUE_VAR, value);
                        String attrLinkName = strings.length==2 ? strings[1].replace(Keywords.CUSTOM_FIELD_VALUE_VAR, value) : attrLinkUrl;

                        attrMap.put("attrLinkUrl", attrLinkUrl);
                        attrMap.put("attrLinkName", attrLinkName);
                    }
                }
            }
            // Put attributes in groups
            AttributeGroup group = groupMap.get(attr.getAttributeGroupId());
            String key = AdminUtils.getAttributeGroupKey(showDefaultHeader ? requestContext : null, group);

            List list = customFields.get(key);
            if (list == null) {
                list = new ArrayList();
            }
            list.add(attrMap);
            customFields.put(key, list);
        }
        }
    }

    /**
     * Formats custom field value, this depends on attribute type.
     * @param valueMap
     * @param attr
     * @return
     */
    private String getFieldValue(Map valueMap, Attribute attr) {
        if (valueMap == null) {
            return "";
        } else {
            Object fieldValue = valueMap.get(attr.getId());

            CustomFieldFormatter formatter = new CustomFieldFormatter(attr, fieldValue);
            formatter.setEditPage(editPage);
            return formatter.getAttributeValue();
        }
    }

    public void setObject(BaseObject baseObject) {
        objectTypeId = baseObject.getObjectTypeId();
        objectId = baseObject.getId();
    }

    public String getDatePattern() {
        return ConfigManager.system.getDateFormat();
    }

    public Map getCustomFields() {
        return customFields;
    }
    public Integer getObjectTypeId() {
        return objectTypeId;
    }
    public void setObjectTypeId(Integer objectTypeId) {
        this.objectTypeId = objectTypeId;
    }
    public Integer getObjectId() {
        return objectId;
    }
    public void setObjectId(Integer objectId) {
        this.objectId = objectId;
    }

    public boolean isEditPage() {
        return editPage;
    }

    public void setObjectAttrTypeId(Integer objectAttrTypeId) {
        this.objectAttrTypeId = objectAttrTypeId;
    }

    public void setPartialTable(boolean partialTable) {
        isPartialTable = partialTable;
    }

    public boolean isPartialTable() {
        return isPartialTable;
    }

    public void setShowDefaultHeader(boolean showDefaultHeader) {
        this.showDefaultHeader = showDefaultHeader;
    }

    public boolean isExpandCustomFields() {
        return expandCustomFields;
    }

    public void setForm(BaseObjectForm form) {
        this.form = form;
        this.editPage = true;
    }
}
