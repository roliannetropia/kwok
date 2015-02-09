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
import com.kwoksys.biz.tape.TapeService;
import com.kwoksys.biz.tape.core.TapeSearch;
import com.kwoksys.biz.tape.dao.TapeQueries;
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
 * TapeSearchTemplate
 */
public class TapeSearchTemplate extends BaseTemplate {

    private String formAction;
    private List<AttributeFieldCount> tapeTypeData;
    private List<AttributeFieldCount> tapeStatusData;
    private List<AttributeFieldCount> tapeLocationData;
    private boolean hideSearchButton;

    public TapeSearchTemplate() {
        super(TapeSearchTemplate.class);
    }

    public void applyTemplate() throws DatabaseException {
        // Tape name criteria
        List nameCriteriaOptions = Arrays.asList(
                new LabelValueBean(Localizer.getText(requestContext, "core.search.criteria.exactMatch"), "equals"),
                new LabelValueBean(Localizer.getText(requestContext, "core.search.criteria.contains"), "contains"),
                new LabelValueBean(Localizer.getText(requestContext, "core.search.criteria.beginsWith"), "begins"));

        // Get company list
        List companyOptions = new ArrayList();
        companyOptions.add(new SelectOneLabelValueBean(requestContext, "0"));
        companyOptions.addAll(CompanyUtils.getCompanyOptions(requestContext));

        TapeService tapeService = ServiceProvider.getTapeService(requestContext);

        // We'll use the same queryBits for a few different queries below
        QueryBits query = new QueryBits();
        query.addSortColumn(TapeQueries.getOrderByColumn(AttributeField.NAME));

        AttributeManager attributeManager = new AttributeManager(requestContext);

        Map attrFieldTypeMap = attributeManager.getAttrFieldMapCache(Attributes.TAPE_TYPE);
        Map attrFieldStatusMap = attributeManager.getAttrFieldMapCache(Attributes.TAPE_STATUS);
        Map attrFieldLocMap = attributeManager.getAttrFieldMapCache(Attributes.TAPE_LOCATION);

        // Group by Tape type.
        List tapeTypeOptions = new ArrayList();
        tapeTypeOptions.add(new SelectOneLabelValueBean(requestContext));

        if (tapeTypeData == null) {
            tapeTypeData = tapeService.getTapeTypeCount(query);
        }
        for (AttributeFieldCount tape : tapeTypeData) {
            AttributeField attrField = (AttributeField) attrFieldTypeMap.get(tape.getAttrFieldId());
            String tapeTypeName;

            if (attrField != null) {
                tapeTypeName = attrField.getName();
            } else {
                tapeTypeName = Localizer.getText(requestContext, "itMgmt.index.na");
            }
            tapeTypeOptions.add(new LabelValueBean(tapeTypeName, String.valueOf(tape.getAttrFieldId())));
        }

        // Group by Tape status.
        List statusOptions = new ArrayList();
        statusOptions.add(new SelectOneLabelValueBean(requestContext));

        if (tapeStatusData == null) {
            tapeStatusData = tapeService.getTapeStatusCount(query);
        }
        for (AttributeFieldCount tape : tapeStatusData) {
            AttributeField attrField = (AttributeField) attrFieldStatusMap.get(tape.getAttrFieldId());
            String tapeStatusName;

            if (attrField != null) {
                tapeStatusName = attrField.getName();
            } else {
                tapeStatusName = Localizer.getText(requestContext, "itMgmt.index.na");
            }
            statusOptions.add(new LabelValueBean(tapeStatusName, String.valueOf(tape.getAttrFieldId())));
        }

        // Group by Tape location.
        List locationOptions = new ArrayList();
        locationOptions.add(new SelectOneLabelValueBean(requestContext));

        if (tapeLocationData == null) {
            tapeLocationData = tapeService.getTapeLocationCount(query);
        }
        for (AttributeFieldCount tape : tapeLocationData) {
            AttributeField attrField = (AttributeField) attrFieldLocMap.get(tape.getAttrFieldId());
            String tapeLocName;

            if (attrField != null) {
                tapeLocName = attrField.getName();
            } else {
                tapeLocName = Localizer.getText(requestContext, "itMgmt.index.na");
            }
            locationOptions.add(new LabelValueBean(tapeLocName, String.valueOf(tape.getAttrFieldId())));
        }

        request.setAttribute("TapeSearchTemplate_formAction", AppPaths.ROOT + formAction);
        request.setAttribute("tapeNameCriteriaOptions", nameCriteriaOptions);
        request.setAttribute("manufacturersOptions", companyOptions);
        request.setAttribute("vendorsOptions", companyOptions);
        request.setAttribute("monthOptions", CalendarUtils.getMonthOptions(requestContext));
        request.setAttribute("dateOptions", CalendarUtils.getDateOptions(requestContext));
        request.setAttribute("yearOptions", CalendarUtils.getPastYearOptions(requestContext));
        request.setAttribute("tapeTypeOptions", tapeTypeOptions);
        request.setAttribute("tapeStatusOptions", statusOptions);
        request.setAttribute("tapeLocationOptions", locationOptions);
        request.setAttribute("customFieldsOptions", new AttributeManager(requestContext).getCustomFieldOptions(ObjectTypes.TAPE));
        request.setAttribute("TapeSearchTemplate_hideSearchButton", hideSearchButton);

        TapeSearch tapeSearch = new TapeSearch(requestContext, SessionManager.TAPE_SEARCH_CRITERIA_MAP);

        List warrantyOptions = new ArrayList();
        for (String key : new String[]{TapeSearch.TAPE_WARRANTY_EXPIRED,
                TapeSearch.TAPE_WARRANTY_NOT_EXPIRED,
                TapeSearch.TAPE_WARRANTY_NOT_SET}) {

            Map map = new HashMap();
            map.put("key", key);
            map.put("checked", tapeSearch.getSearchCriteriaMap().containsKey(key) ? "checked" : "");
            warrantyOptions.add(map);
        }
        request.setAttribute("warrantyOptions", warrantyOptions);

        List options = new ArrayList();
        options.add(new SelectOneLabelValueBean(requestContext, "0"));
        request.setAttribute("componentTypeOptions",
                attributeManager.getActiveAttrFieldOptionsCache(Attributes.TAPE_COMPONENT_TYPE, options));
    }

    public String getFormAction() {
        return formAction;
    }
    public void setFormAction(String formAction) {
        this.formAction = formAction;
    }
    public void setTapeTypeData(List<AttributeFieldCount> tapeTypeData) {
        this.tapeTypeData = tapeTypeData;
    }
    public void setTapeStatusData(List<AttributeFieldCount> tapeStatusData) {
        this.tapeStatusData = tapeStatusData;
    }
    public void setTapeLocationData(List<AttributeFieldCount> tapeLocationData) {
        this.tapeLocationData = tapeLocationData;
    }

    public boolean getHideSearchButton() {
        return hideSearchButton;
    }

    public void setHideSearchButton(boolean hideSearchButton) {
        this.hideSearchButton = hideSearchButton;
    }
}