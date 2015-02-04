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
package com.kwoksys.action.hardware;

import com.kwoksys.action.common.template.ActionErrorsTemplate;
import com.kwoksys.action.common.template.CustomFieldsTemplate;
import com.kwoksys.action.common.template.HeaderTemplate;
import com.kwoksys.action.common.template.StandardTemplate;
import com.kwoksys.biz.ServiceProvider;
import com.kwoksys.biz.admin.core.AdminUtils;
import com.kwoksys.biz.admin.core.CalendarUtils;
import com.kwoksys.biz.contacts.core.CompanySearch;
import com.kwoksys.biz.contacts.core.CompanyUtils;
import com.kwoksys.biz.contacts.dto.Company;
import com.kwoksys.biz.hardware.HardwareService;
import com.kwoksys.biz.hardware.dto.Hardware;
import com.kwoksys.biz.system.core.*;
import com.kwoksys.biz.system.core.configs.ConfigManager;
import com.kwoksys.framework.connections.database.QueryBits;
import com.kwoksys.framework.properties.Localizer;
import com.kwoksys.framework.struts2.Action2;
import com.kwoksys.framework.ui.SelectOneLabelValueBean;
import org.apache.struts.util.LabelValueBean;

import java.util.ArrayList;
import java.util.List;

/**
 * Action class for adding hardware.
 */
public class HardwareAddAction extends Action2 {

    public String execute() throws Exception {
        AttributeManager attributeManager = new AttributeManager(requestContext);

        Integer copyHardwareId = requestContext.getParameter("copyHardwareId");

        HardwareService hardwareService = ServiceProvider.getHardwareService(requestContext);
        Hardware hardware;

        // If copyHardwareId is provided, we get the hardware spec of that id
        if (copyHardwareId == 0) {
            hardware = new Hardware();
        } else {
            hardware = hardwareService.getHardware(copyHardwareId);
        }

        // Load attributes
        hardware.loadAttrs(requestContext);
        
        HardwareForm actionForm = (HardwareForm) getBaseForm(HardwareForm.class);
        
        // Not a resubmit, set some defaults
        if (!actionForm.isResubmit()) {
            actionForm.setHardware(hardware);
        }

        LabelValueBean selectOneLabel = new SelectOneLabelValueBean(requestContext, "0");

        // Get hardware_location options
        List locationOptions = new ArrayList();
        locationOptions.add(selectOneLabel);
        attributeManager.getActiveAttrFieldOptionsCache(Attributes.HARDWARE_LOCATION, locationOptions);

        // Get hardware_type options
        List typeOptions = new ArrayList();
        typeOptions.add(selectOneLabel);
        attributeManager.getActiveAttrFieldOptionsCache(Attributes.HARDWARE_TYPE, typeOptions);

        // Get hardware_status options
        List statusOptions = new ArrayList();
        statusOptions.add(selectOneLabel);
        attributeManager.getActiveAttrFieldOptionsCache(Attributes.HARDWARE_STATUS, statusOptions);

        // Hardware owner options
        List<LabelValueBean> hardwareOwnerOptions = new ArrayList();
        hardwareOwnerOptions.add(new SelectOneLabelValueBean(requestContext));
        hardwareOwnerOptions.addAll(AdminUtils.getUserOptions(requestContext));

        // Get company list
        List hardwareVendors = new ArrayList();
        hardwareVendors.add(selectOneLabel);

        CompanySearch vendorSearch = new CompanySearch();
        vendorSearch.put(CompanySearch.COMPANY_TYPE_EQUALS, AttributeFieldIds.COMPANY_TYPE_HARDWARE_VENDOR);

        QueryBits vendorQuery = new QueryBits(vendorSearch);
        vendorQuery.addSortColumn(Company.COMPANY_NAME);
        hardwareVendors.addAll(CompanyUtils.getCompanyOptions(requestContext, vendorQuery));

        List hardwareManufacturers = new ArrayList();
        hardwareManufacturers.add(selectOneLabel);

        CompanySearch manufacturerSearch = new CompanySearch();
        manufacturerSearch.put(CompanySearch.COMPANY_TYPE_EQUALS, AttributeFieldIds.COMPANY_TYPE_HARDWARE_MANUFACTURER);

        QueryBits manufacturerQuery = new QueryBits(manufacturerSearch);
        manufacturerQuery.addSortColumn(Company.COMPANY_NAME);
        hardwareManufacturers.addAll(CompanyUtils.getCompanyOptions(requestContext, manufacturerQuery));

        List warrantyOptions = new ArrayList();
        warrantyOptions.add(new LabelValueBean(Localizer.getText(requestContext, "hardware.selectWarrantyPeriod"), "0"));
        for (int i=1; i<=3; i++) {
            String numYear = String.valueOf(i);
            warrantyOptions.add(new LabelValueBean(Localizer.getText(requestContext, "hardware.predefinedWarranty",
                    new String[]{numYear}), numYear));
        }

        //
        // Template: StandardTemplate
        //
        StandardTemplate standardTemplate = new StandardTemplate(requestContext);
        request.setAttribute("hardware", hardware);
        standardTemplate.setPathAttribute("formAction", AppPaths.HARDWARE_ADD_2);
        standardTemplate.setPathAttribute("formThisAction", AppPaths.HARDWARE_ADD);
        standardTemplate.setAttribute("formCancelLink", Links.getCancelLink(requestContext, AppPaths.HARDWARE_LIST).getString());
        request.setAttribute("currencySymbol", ConfigManager.system.getCurrencySymbol());
        request.setAttribute("hardwareTypeOptions", typeOptions);
        request.setAttribute("hardwareStatusOptions", statusOptions);
        request.setAttribute("purchaseYearOptions", CalendarUtils.getPastYearOptions(requestContext));
        request.setAttribute("purchaseMonthOptions", CalendarUtils.getMonthOptions(requestContext));
        request.setAttribute("purchaseDateOptions", CalendarUtils.getDateOptions(requestContext));
        request.setAttribute("warrantyYearOptions", CalendarUtils.getYearOptions(requestContext));
        request.setAttribute("warrantyMonthOptions", CalendarUtils.getMonthOptions(requestContext));
        request.setAttribute("warrantyDateOptions", CalendarUtils.getDateOptions(requestContext));
        request.setAttribute("hardwareOwnerOptions", hardwareOwnerOptions);
        request.setAttribute("locationOptions", locationOptions);
        request.setAttribute("manufacturersOptions", hardwareManufacturers);
        request.setAttribute("vendorsOptions", hardwareVendors);
        request.setAttribute("warrantyPeriodOptions", warrantyOptions);

        //
        // Template: HeaderTemplate
        //
        HeaderTemplate header = standardTemplate.getHeaderTemplate();
        header.setTitleKey("itMgmt.cmd.hardwareAdd");

        //
        // Template: CustomFieldsTemplate
        //
        CustomFieldsTemplate customFieldsTemplate = new CustomFieldsTemplate();
        standardTemplate.addTemplate(customFieldsTemplate);
        customFieldsTemplate.setObjectTypeId(ObjectTypes.HARDWARE);
        if (copyHardwareId != 0) {
            customFieldsTemplate.setObjectId(copyHardwareId);
        }
        customFieldsTemplate.setObjectAttrTypeId(actionForm.getHardwareType());
        customFieldsTemplate.setForm(actionForm);

        //
        // Template: ActionErrorsTemplate
        //
        ActionErrorsTemplate errorsTemplate = new ActionErrorsTemplate();
        standardTemplate.addTemplate(errorsTemplate);
        errorsTemplate.setShowRequiredFieldMsg(true);
        errorsTemplate.setMessage(Localizer.getText(requestContext, "itMgmt.hardwareAdd.sectionHeader"));

        return standardTemplate.findTemplate(STANDARD_TEMPLATE);
    }
}
