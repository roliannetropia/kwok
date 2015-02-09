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
package com.kwoksys.action.tape;

import com.kwoksys.biz.ServiceProvider;
import com.kwoksys.biz.admin.core.CalendarUtils;
import com.kwoksys.biz.admin.dto.AttributeField;
import com.kwoksys.biz.admin.dto.AttributeFieldCount;
import com.kwoksys.biz.base.BaseTemplate;
import com.kwoksys.biz.contacts.core.CompanyUtils;
import com.kwoksys.biz.hardware.HardwareService;
import com.kwoksys.biz.hardware.core.HardwareSearch;
import com.kwoksys.biz.hardware.dao.HardwareQueries;
import com.kwoksys.biz.system.core.AppPaths;
import com.kwoksys.biz.system.core.AttributeManager;
import com.kwoksys.biz.system.core.Attributes;
import com.kwoksys.biz.system.core.ObjectTypes;
import com.kwoksys.framework.connections.database.QueryBits;
import com.kwoksys.framework.exceptions.DatabaseException;
import com.kwoksys.framework.properties.Localizer;
import com.kwoksys.framework.session.SessionManager;
import com.kwoksys.framework.ui.SelectOneLabelValueBean;
import org.apache.struts.util.LabelValueBean;

import java.util.*;

/**
 * HardwareSearchTemplate
 */
public class TapeSearchTemplate extends BaseTemplate {

    private String formAction;
    private List<AttributeFieldCount> hardwareTypeData;
    private List<AttributeFieldCount> hardwareStatusData;
    private List<AttributeFieldCount> hardwareLocationData;
    private boolean hideSearchButton;

    public TapeSearchTemplate() {
        super(TapeSearchTemplate.class);
    }

    public void applyTemplate() throws DatabaseException {
        // Hardware name criteria
        List nameCriteriaOptions = Arrays.asList(
                new LabelValueBean(Localizer.getText(requestContext, "core.search.criteria.exactMatch"), "equals"),
                new LabelValueBean(Localizer.getText(requestContext, "core.search.criteria.contains"), "contains"),
                new LabelValueBean(Localizer.getText(requestContext, "core.search.criteria.beginsWith"), "begins"));

        // Get company list
        List companyOptions = new ArrayList();
        companyOptions.add(new SelectOneLabelValueBean(requestContext, "0"));
        companyOptions.addAll(CompanyUtils.getCompanyOptions(requestContext));

        HardwareService hardwareService = ServiceProvider.getHardwareService(requestContext);

        // We'll use the same queryBits for a few different queries below
        QueryBits query = new QueryBits();
        query.addSortColumn(HardwareQueries.getOrderByColumn(AttributeField.NAME));

        AttributeManager attributeManager = new AttributeManager(requestContext);

        Map attrFieldTypeMap = attributeManager.getAttrFieldMapCache(Attributes.HARDWARE_TYPE);
        Map attrFieldStatusMap = attributeManager.getAttrFieldMapCache(Attributes.HARDWARE_STATUS);
        Map attrFieldLocMap = attributeManager.getAttrFieldMapCache(Attributes.HARDWARE_LOCATION);

        // Group by Hardware type.
        List hardwareTypeOptions = new ArrayList();
        hardwareTypeOptions.add(new SelectOneLabelValueBean(requestContext));

        if (hardwareTypeData == null) {
            hardwareTypeData = hardwareService.getHardwareTypeCount(query);
        }
        for (AttributeFieldCount hardware : hardwareTypeData) {
            AttributeField attrField = (AttributeField) attrFieldTypeMap.get(hardware.getAttrFieldId());
            String hardwareTypeName;

            if (attrField != null) {
                hardwareTypeName = attrField.getName();
            } else {
                hardwareTypeName = Localizer.getText(requestContext, "itMgmt.index.na");
            }
            hardwareTypeOptions.add(new LabelValueBean(hardwareTypeName, String.valueOf(hardware.getAttrFieldId())));
        }

        // Group by Hardware status.
        List statusOptions = new ArrayList();
        statusOptions.add(new SelectOneLabelValueBean(requestContext));

        if (hardwareStatusData == null) {
            hardwareStatusData = hardwareService.getHardwareStatusCount(query);
        }
        for (AttributeFieldCount hardware : hardwareStatusData) {
            AttributeField attrField = (AttributeField) attrFieldStatusMap.get(hardware.getAttrFieldId());
            String hardwareStatusName;

            if (attrField != null) {
                hardwareStatusName = attrField.getName();
            } else {
                hardwareStatusName = Localizer.getText(requestContext, "itMgmt.index.na");
            }
            statusOptions.add(new LabelValueBean(hardwareStatusName, String.valueOf(hardware.getAttrFieldId())));
        }

        // Group by Hardware location.
        List locationOptions = new ArrayList();
        locationOptions.add(new SelectOneLabelValueBean(requestContext));

        if (hardwareLocationData == null) {
            hardwareLocationData = hardwareService.getHardwareLocationCount(query);
        }
        for (AttributeFieldCount hardware : hardwareLocationData) {
            AttributeField attrField = (AttributeField) attrFieldLocMap.get(hardware.getAttrFieldId());
            String hardwareLocName;

            if (attrField != null) {
                hardwareLocName = attrField.getName();
            } else {
                hardwareLocName = Localizer.getText(requestContext, "itMgmt.index.na");
            }
            locationOptions.add(new LabelValueBean(hardwareLocName, String.valueOf(hardware.getAttrFieldId())));
        }

        request.setAttribute("HardwareSearchTemplate_formAction", AppPaths.ROOT + formAction);
        request.setAttribute("hardwareNameCriteriaOptions", nameCriteriaOptions);
        request.setAttribute("manufacturersOptions", companyOptions);
        request.setAttribute("vendorsOptions", companyOptions);
        request.setAttribute("monthOptions", CalendarUtils.getMonthOptions(requestContext));
        request.setAttribute("dateOptions", CalendarUtils.getDateOptions(requestContext));
        request.setAttribute("yearOptions", CalendarUtils.getPastYearOptions(requestContext));
        request.setAttribute("hardwareTypeOptions", hardwareTypeOptions);
        request.setAttribute("hardwareStatusOptions", statusOptions);
        request.setAttribute("hardwareLocationOptions", locationOptions);
        request.setAttribute("customFieldsOptions", new AttributeManager(requestContext).getCustomFieldOptions(ObjectTypes.HARDWARE));
        request.setAttribute("HardwareSearchTemplate_hideSearchButton", hideSearchButton);

        HardwareSearch hardwareSearch = new HardwareSearch(requestContext, SessionManager.HARDWARE_SEARCH_CRITERIA_MAP);

        List warrantyOptions = new ArrayList();
        for (String key : new String[]{HardwareSearch.HARDWARE_WARRANTY_EXPIRED,
                HardwareSearch.HARDWARE_WARRANTY_NOT_EXPIRED,
                HardwareSearch.HARDWARE_WARRANTY_NOT_SET}) {

            Map map = new HashMap();
            map.put("key", key);
            map.put("checked", hardwareSearch.getSearchCriteriaMap().containsKey(key) ? "checked" : "");
            warrantyOptions.add(map);
        }
        request.setAttribute("warrantyOptions", warrantyOptions);

        List options = new ArrayList();
        options.add(new SelectOneLabelValueBean(requestContext, "0"));
        request.setAttribute("componentTypeOptions",
                attributeManager.getActiveAttrFieldOptionsCache(Attributes.HARDWARE_COMPONENT_TYPE, options));
    }

    public String getFormAction() {
        return formAction;
    }
    public void setFormAction(String formAction) {
        this.formAction = formAction;
    }
    public void setHardwareTypeData(List<AttributeFieldCount> hardwareTypeData) {
        this.hardwareTypeData = hardwareTypeData;
    }
    public void setHardwareStatusData(List<AttributeFieldCount> hardwareStatusData) {
        this.hardwareStatusData = hardwareStatusData;
    }
    public void setHardwareLocationData(List<AttributeFieldCount> hardwareLocationData) {
        this.hardwareLocationData = hardwareLocationData;
    }

    public boolean getHideSearchButton() {
        return hideSearchButton;
    }

    public void setHideSearchButton(boolean hideSearchButton) {
        this.hideSearchButton = hideSearchButton;
    }
}