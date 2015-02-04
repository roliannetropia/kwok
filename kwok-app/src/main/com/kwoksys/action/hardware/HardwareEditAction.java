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
import com.kwoksys.framework.util.NumberUtils;
import org.apache.struts.util.LabelValueBean;

import java.util.ArrayList;
import java.util.List;

/**
 * Action class for editing hardware.
 */
public class HardwareEditAction extends Action2 {

    public String execute() throws Exception {
        Integer hardwareId = requestContext.getParameter("hardwareId");

        HardwareService hardwareService = ServiceProvider.getHardwareService(requestContext);
        Hardware hardware = hardwareService.getHardware(hardwareId);

        // Load attributes
        hardware.loadAttrs(requestContext);

        HardwareForm actionForm = (HardwareForm) getBaseForm(HardwareForm.class);

        // Not a resubmit, set some defaults
        if (!actionForm.isResubmit()) {
            actionForm.setHardware(hardware);
        }

        LabelValueBean selectOneLabel = new SelectOneLabelValueBean(requestContext, "0");

        AttributeManager attributeManager = new AttributeManager(requestContext);

        // Get hardware_location options
        List locationOptions = new ArrayList();
        locationOptions.add(selectOneLabel);
        attributeManager.getActiveAttrFieldOptionsCache(Attributes.HARDWARE_LOCATION,
                hardware.getLocation(), locationOptions);

        // Get hardware_type options.
        List hardwareTypeOptions = new ArrayList();
        hardwareTypeOptions.add(selectOneLabel);
        attributeManager.getActiveAttrFieldOptionsCache(Attributes.HARDWARE_TYPE,
                hardware.getType(), hardwareTypeOptions);

        // Get hardware_status options.
        List statusOptions = new ArrayList();
        statusOptions.add(selectOneLabel);
        attributeManager.getActiveAttrFieldOptionsCache(Attributes.HARDWARE_STATUS,
                hardware.getStatus(), statusOptions);

        // Only shows users whose status is "Enable", plus hardware owner.
        List<LabelValueBean> hardwareOwnerOptions = new ArrayList();
        hardwareOwnerOptions.add(new SelectOneLabelValueBean(requestContext));
        hardwareOwnerOptions.addAll(AdminUtils.getUserOptions(requestContext, hardware.getOwner()));

        // Get company list
        List hardwareVendors = new ArrayList();
        hardwareVendors.add(selectOneLabel);

        CompanySearch vendorSearch = new CompanySearch();
        vendorSearch.put(CompanySearch.COMPANY_TYPE_EQUALS, AttributeFieldIds.COMPANY_TYPE_HARDWARE_VENDOR);
        vendorSearch.put(CompanySearch.COMPANY_ID_EQUALS, hardware.getVendorId());

        QueryBits vendorQuery = new QueryBits(vendorSearch);
        vendorQuery.addSortColumn(Company.COMPANY_NAME);

        hardwareVendors.addAll(CompanyUtils.getCompanyOptions(requestContext, vendorQuery));

        List hardwareManufacturers = new ArrayList();
        hardwareManufacturers.add(selectOneLabel);

        CompanySearch manufacturerSearch = new CompanySearch();
        manufacturerSearch.put(CompanySearch.COMPANY_TYPE_EQUALS, AttributeFieldIds.COMPANY_TYPE_HARDWARE_MANUFACTURER);
        manufacturerSearch.put(CompanySearch.COMPANY_ID_EQUALS, hardware.getManufacturerId());

        QueryBits manufacturerQuery = new QueryBits(manufacturerSearch);
        manufacturerQuery.addSortColumn(Company.COMPANY_NAME);

        hardwareManufacturers.addAll(CompanyUtils.getCompanyOptions(requestContext, manufacturerQuery));

        int purchaseYear = NumberUtils.replaceNull(hardware.getPurchaseYear(), 0);
        int warrantyYear = NumberUtils.replaceNull(hardware.getWarrantyYear(), 0);

        //
        // Template: StandardTemplate
        //
        StandardTemplate standardTemplate = new StandardTemplate(requestContext);
        standardTemplate.setAttribute("hardware", hardware);
        standardTemplate.setPathAttribute("formAction", AppPaths.HARDWARE_EDIT_2);
        standardTemplate.setPathAttribute("formThisAction", AppPaths.HARDWARE_EDIT);
        standardTemplate.setAttribute("formCancelLink", Links.getCancelLink(requestContext, AppPaths.HARDWARE_DETAIL + "?hardwareId=" + hardware.getId()).getString());
        standardTemplate.setAttribute("currencySymbol", ConfigManager.system.getCurrencySymbol());
        standardTemplate.setAttribute("locationOptions", locationOptions);
        standardTemplate.setAttribute("hardwareTypeOptions", hardwareTypeOptions);
        standardTemplate.setAttribute("hardwareStatusOptions", statusOptions);
        standardTemplate.setAttribute("purchaseYearOptions", CalendarUtils.getExtraPastYearOptions(requestContext, purchaseYear));
        standardTemplate.setAttribute("warrantyYearOptions", CalendarUtils.getExtraYearOptions(requestContext, warrantyYear));
        standardTemplate.setAttribute("monthOptions", CalendarUtils.getMonthOptions(requestContext));
        standardTemplate.setAttribute("dateOptions", CalendarUtils.getDateOptions(requestContext));
        standardTemplate.setAttribute("hardwareOwnerOptions", hardwareOwnerOptions);
        standardTemplate.setAttribute("manufacturersOptions", hardwareManufacturers);
        standardTemplate.setAttribute("vendorsOptions", hardwareVendors);

        HeaderTemplate header = standardTemplate.getHeaderTemplate();
        header.setTitleKey("itMgmt.cmd.hardwareEdit");

        //
        // Template: ActionErrorsTemplate
        //
        ActionErrorsTemplate errorsTemplate = new ActionErrorsTemplate();
        standardTemplate.addTemplate(errorsTemplate);
        errorsTemplate.setShowRequiredFieldMsg(true);
        errorsTemplate.setMessage(Localizer.getText(requestContext, "itMgmt.hardwareEdit.sectionHeader"));

        //
        // Template: CustomFieldsTemplate
        //
        CustomFieldsTemplate customFieldsTemplate = new CustomFieldsTemplate();
        standardTemplate.addTemplate(customFieldsTemplate);
        customFieldsTemplate.setObjectTypeId(ObjectTypes.HARDWARE);
        customFieldsTemplate.setObjectId(hardwareId);
        customFieldsTemplate.setObjectAttrTypeId(actionForm.getHardwareType());
        customFieldsTemplate.setForm(actionForm);

        return standardTemplate.findTemplate(STANDARD_TEMPLATE);
    }
}
