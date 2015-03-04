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

import com.kwoksys.action.common.template.ActionErrorsTemplate;
import com.kwoksys.action.common.template.CustomFieldsTemplate;
import com.kwoksys.action.common.template.HeaderTemplate;
import com.kwoksys.action.common.template.StandardTemplate;
import com.kwoksys.action.tape.TapeForm;
import com.kwoksys.biz.ServiceProvider;
import com.kwoksys.biz.admin.core.AdminUtils;
import com.kwoksys.biz.admin.core.CalendarUtils;
import com.kwoksys.biz.contacts.core.CompanySearch;
import com.kwoksys.biz.contacts.core.CompanyUtils;
import com.kwoksys.biz.contacts.dto.Company;
import com.kwoksys.biz.tape.TapeService;
import com.kwoksys.biz.tape.dto.Tape;
import com.kwoksys.biz.system.core.*;
import com.kwoksys.biz.system.core.configs.ConfigManager;
import com.kwoksys.framework.connections.database.QueryBits;
import com.kwoksys.framework.properties.Localizer;
import com.kwoksys.framework.struts2.Action2;
import com.kwoksys.framework.ui.SelectOneLabelValueBean;
import com.kwoksys.framework.util.DatetimeUtils;
import org.apache.struts.util.LabelValueBean;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

/**
 * Action class for adding tape.
 */
public class TapeAddAction extends Action2 {

    public String execute() throws Exception {
        AttributeManager attributeManager = new AttributeManager(requestContext);

        Integer copyTapeId = requestContext.getParameter("copyTapeId");

        TapeService tapeService = ServiceProvider.getTapeService(requestContext);
        Tape tape;

        // If copyTapeId is provided, we get the tape spec of that id
        if (copyTapeId == 0) {
            tape = new Tape();
        } else {
            tape = tapeService.getTape(copyTapeId);
        }

        // Load attributes
        tape.loadAttrs(requestContext);
        TapeForm actionForm = (TapeForm) getBaseForm(TapeForm.class);

        // Not a resubmit, set some defaults
        if (!actionForm.isResubmit()) {
            actionForm.setTape(tape);
        }

        LabelValueBean selectOneLabel = new SelectOneLabelValueBean(requestContext, "0");

        // Get tape_type options
        List typeOptions = new ArrayList();
        typeOptions.add(selectOneLabel);
        attributeManager.getActiveAttrFieldOptionsCache(Attributes.MEDIA_TYPE, typeOptions);

        // Get tape_location options
        List locationOptions = new ArrayList();
        locationOptions.add(selectOneLabel);
        attributeManager.getActiveAttrFieldOptionsCache(Attributes.TAPE_LOCATION, locationOptions);

        // Get tape_retention options
        List retentionOptions = new ArrayList();
        retentionOptions.add(selectOneLabel);
        attributeManager.getActiveAttrFieldOptionsCache(Attributes.TAPE_RETENTION, retentionOptions);

//        Get tape_system options
        List systemOptions = new ArrayList();
        systemOptions.add(selectOneLabel);
        attributeManager.getActiveAttrFieldOptionsCache(Attributes.TAPE_SYSTEM, systemOptions);

        //        Get tape_status options
        List statusOptions = new ArrayList();
        statusOptions.add(selectOneLabel);
        attributeManager.getActiveAttrFieldOptionsCache(Attributes.TAPE_STATUS, statusOptions);

        // Tape owner options
//        List<LabelValueBean> tapeOwnerOptions = new ArrayList();
//        tapeOwnerOptions.add(new SelectOneLabelValueBean(requestContext));
//        tapeOwnerOptions.addAll(AdminUtils.getUserOptions(requestContext));

        // Get company list
        List tapeVendors = new ArrayList();
        tapeVendors.add(selectOneLabel);

        CompanySearch vendorSearch = new CompanySearch();
        vendorSearch.put(CompanySearch.COMPANY_TYPE_EQUALS, AttributeFieldIds.COMPANY_TYPE_HARDWARE_VENDOR);

        QueryBits vendorQuery = new QueryBits(vendorSearch);
        System.out.println("vendor search -> "+vendorSearch);

        vendorQuery.addSortColumn(Company.COMPANY_NAME);
        tapeVendors.addAll(CompanyUtils.getCompanyOptions(requestContext, vendorQuery));
        System.out.println("tape vendor query -> " + vendorQuery);
        System.out.println("tape vendor add all -> " + CompanyUtils.getCompanyOptions(requestContext, vendorQuery));

        List tapeManufacturers = new ArrayList();
        tapeManufacturers.add(selectOneLabel);

        CompanySearch manufacturerSearch = new CompanySearch();
        manufacturerSearch.put(CompanySearch.COMPANY_TYPE_EQUALS, AttributeFieldIds.COMPANY_TYPE_HARDWARE_MANUFACTURER);

        QueryBits manufacturerQuery = new QueryBits(manufacturerSearch);
        manufacturerQuery.addSortColumn(Company.COMPANY_NAME);
        tapeManufacturers.addAll(CompanyUtils.getCompanyOptions(requestContext, manufacturerQuery));
        System.out.println("tape manufacturer add all -> " + CompanyUtils.getCompanyOptions(requestContext, manufacturerQuery));


//        List warrantyOptions = new ArrayList();
//        warrantyOptions.add(new LabelValueBean(Localizer.getText(requestContext, "tape.selectWarrantyPeriod"), "0"));
//        for (int i=1; i<=3; i++) {
//            String numYear = String.valueOf(i);
//            warrantyOptions.add(new LabelValueBean(Localizer.getText(requestContext, "tape.predefinedWarranty",
//                    new String[]{numYear}), numYear));
//        }
        System.out.println("request context: "+requestContext);
        //
        // Template: StandardTemplate
        //
        StandardTemplate standardTemplate = new StandardTemplate(requestContext);
        request.setAttribute("tape", tape);
        standardTemplate.setPathAttribute("formAction", AppPaths.TAPE_ADD_2);
        standardTemplate.setPathAttribute("formThisAction", AppPaths.TAPE_ADD);
        standardTemplate.setAttribute("formCancelLink", Links.getCancelLink(requestContext, AppPaths.TAPE_LIST).getString());
//        request.setAttribute("currencySymbol", ConfigManager.system.getCurrencySymbol());
        request.setAttribute("mediaTypeOptions", typeOptions);
        System.out.println("typeOptions: " + typeOptions);
        request.setAttribute("tapeLocationOptions", locationOptions);
        System.out.println("locationOptions: " + locationOptions);
        request.setAttribute("tapeRetentionOptions", retentionOptions);
        System.out.println("retentionOptions: " + retentionOptions);
        request.setAttribute("tapeSystemOptions", systemOptions);
        System.out.println("systemOptions: " + systemOptions);
        request.setAttribute("tapeStatusOptions", statusOptions);
        System.out.println("statusOptions: " + statusOptions);
//        request.setAttribute("purchaseYearOptions", CalendarUtils.getPastYearOptions(requestContext));
//        request.setAttribute("purchaseMonthOptions", CalendarUtils.getMonthOptions(requestContext));
        request.setAttribute("manufacturedYearOptions", CalendarUtils.getPastYearOptions(requestContext));
        request.setAttribute("manufacturedMonthOptions", CalendarUtils.getMonthOptions(requestContext));
        request.setAttribute("manufacturedDateOptions", CalendarUtils.getDateOptions(requestContext));

        request.setAttribute("transactionYearOptions", CalendarUtils.getPastYearOptions(requestContext));
        request.setAttribute("transactionMonthOptions", CalendarUtils.getMonthOptions(requestContext));
        request.setAttribute("transactionDateOptions", CalendarUtils.getDateOptions(requestContext));

        request.setAttribute("moveYearOptions", CalendarUtils.getPastYearOptions(requestContext));
        request.setAttribute("moveMonthOptions", CalendarUtils.getMonthOptions(requestContext));
        request.setAttribute("moveDateOptions", CalendarUtils.getDateOptions(requestContext));

        request.setAttribute("expireYearOptions", CalendarUtils.getPastYearOptions(requestContext));
        request.setAttribute("expireMonthOptions", CalendarUtils.getMonthOptions(requestContext));
        request.setAttribute("expireDateOptions", CalendarUtils.getDateOptions(requestContext));

        request.setAttribute("transactionHourOptions", CalendarUtils.getHourOptions(requestContext));
        request.setAttribute("transactionMinOptions", CalendarUtils.getMinOptions(requestContext));

//        request.setAttribute("purchaseDateOptions", CalendarUtils.getDateOptions(requestContext));
//        request.setAttribute("warrantyYearOptions", CalendarUtils.getYearOptions(requestContext));
//        request.setAttribute("warrantyMonthOptions", CalendarUtils.getMonthOptions(requestContext));
//        request.setAttribute("warrantyDateOptions", CalendarUtils.getDateOptions(requestContext));
//        request.setAttribute("tapeOwnerOptions", tapeOwnerOptions);
        request.setAttribute("manufacturersOptions", tapeManufacturers);
        request.setAttribute("vendorsOptions", tapeVendors);
//        request.setAttribute("warrantyPeriodOptions", warrantyOptions);

        //
        // Template: HeaderTemplate
        //
        HeaderTemplate header = standardTemplate.getHeaderTemplate();
        header.setTitleKey("itMgmt.cmd.tapeAdd");

        //
        // Template: CustomFieldsTemplate
        //
        CustomFieldsTemplate customFieldsTemplate = new CustomFieldsTemplate();
        standardTemplate.addTemplate(customFieldsTemplate);
        customFieldsTemplate.setObjectTypeId(ObjectTypes.TAPE);
        if (copyTapeId != 0) {
            customFieldsTemplate.setObjectId(copyTapeId);
        }
        customFieldsTemplate.setObjectAttrTypeId(actionForm.getMediaType());
        customFieldsTemplate.setForm(actionForm);

        //
        // Template: ActionErrorsTemplate
        //
        ActionErrorsTemplate errorsTemplate = new ActionErrorsTemplate();
        standardTemplate.addTemplate(errorsTemplate);
        errorsTemplate.setShowRequiredFieldMsg(true);
        errorsTemplate.setMessage(Localizer.getText(requestContext, "itMgmt.tapeAdd.sectionHeader"));

        return standardTemplate.findTemplate(STANDARD_TEMPLATE);
    }
}
