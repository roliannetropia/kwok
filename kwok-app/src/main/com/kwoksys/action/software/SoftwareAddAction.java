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
package com.kwoksys.action.software;

import com.kwoksys.action.common.template.ActionErrorsTemplate;
import com.kwoksys.action.common.template.CustomFieldsTemplate;
import com.kwoksys.action.common.template.HeaderTemplate;
import com.kwoksys.action.common.template.StandardTemplate;
import com.kwoksys.biz.admin.core.AdminUtils;
import com.kwoksys.biz.admin.core.CalendarUtils;
import com.kwoksys.biz.contacts.core.CompanySearch;
import com.kwoksys.biz.contacts.core.CompanyUtils;
import com.kwoksys.biz.contacts.dto.Company;
import com.kwoksys.biz.software.dto.Software;
import com.kwoksys.biz.system.core.*;
import com.kwoksys.framework.connections.database.QueryBits;
import com.kwoksys.framework.properties.Localizer;
import com.kwoksys.framework.struts2.Action2;
import com.kwoksys.framework.ui.SelectOneLabelValueBean;
import org.apache.struts.util.LabelValueBean;

import java.util.ArrayList;
import java.util.List;

/**
 * Action class for adding software.
 */
public class SoftwareAddAction extends Action2 {

    public String execute() throws Exception {
        Software software = new Software();

        // Load attributes
        software.loadAttrs(requestContext);
        
        SoftwareForm actionForm = (SoftwareForm) getBaseForm(SoftwareForm.class);

        // Not a resubmit, set some defaults
        if (!actionForm.isResubmit()) {
            actionForm.setSoftware(software);
        }

        LabelValueBean selectOneLabel = new SelectOneLabelValueBean(requestContext, "0");

        // Software owner options
        List<LabelValueBean> softwareOwnerOptions = new ArrayList();
        softwareOwnerOptions.add(new SelectOneLabelValueBean(requestContext));
        softwareOwnerOptions.addAll(AdminUtils.getUserOptions(requestContext));

        // Get software maker/vendor list.
        List softwareMakers = new ArrayList();
        softwareMakers.add(selectOneLabel);

        CompanySearch makerSearch = new CompanySearch();
        makerSearch.put(CompanySearch.COMPANY_TYPE_EQUALS, AttributeFieldIds.COMPANY_TYPE_SOFTWARE_MAKER);

        QueryBits makerQuery = new QueryBits(makerSearch);
        makerQuery.addSortColumn(Company.COMPANY_NAME);

        softwareMakers.addAll(CompanyUtils.getCompanyOptions(requestContext, makerQuery));

        List softwareVendors = new ArrayList();
        softwareVendors.add(selectOneLabel);

        CompanySearch vendorSearch = new CompanySearch();
        vendorSearch.put(CompanySearch.COMPANY_TYPE_EQUALS, AttributeFieldIds.COMPANY_TYPE_SOFTWARE_VENDOR);

        QueryBits vendorQuery = new QueryBits(vendorSearch);
        vendorQuery.addSortColumn(Company.COMPANY_NAME);

        softwareVendors.addAll(CompanyUtils.getCompanyOptions(requestContext, vendorQuery));

        AttributeManager attributeManager = new AttributeManager(requestContext);

        // Get software_type options
        List softwareTypeOptions = new ArrayList();
        softwareTypeOptions.add(selectOneLabel);
        attributeManager.getActiveAttrFieldOptionsCache(Attributes.SOFTWARE_TYPE, softwareTypeOptions);

        // Get software os options.
        List softwareOsOptions = new ArrayList();
        softwareOsOptions.add(selectOneLabel);
        attributeManager.getActiveAttrFieldOptionsCache(Attributes.SOFTWARE_OS, softwareOsOptions);

        //
        // Template: StandardTemplate
        //
        StandardTemplate standardTemplate = new StandardTemplate(requestContext);
        request.setAttribute("software", software);
        standardTemplate.setPathAttribute("formAction", AppPaths.SOFTWARE_ADD_2);
        standardTemplate.setPathAttribute("formThisAction", AppPaths.SOFTWARE_ADD);
        standardTemplate.setAttribute("formCancelLink", Links.getCancelLink(requestContext, AppPaths.SOFTWARE_LIST).getString());
        request.setAttribute("softwareOwnerOptions", softwareOwnerOptions);
        request.setAttribute("softwareTypeOptions", softwareTypeOptions);
        request.setAttribute("softwareOsOptions", softwareOsOptions);
        request.setAttribute("manufacturersOptions", softwareMakers);
        request.setAttribute("vendorsOptions", softwareVendors);
        request.setAttribute("yearOptions", CalendarUtils.getYearOptions(requestContext));
        request.setAttribute("monthOptions", CalendarUtils.getMonthOptions(requestContext));
        request.setAttribute("dateOptions", CalendarUtils.getDateOptions(requestContext));

        //
        // Template: HeaderTemplate
        //
        HeaderTemplate header = standardTemplate.getHeaderTemplate();
        header.setTitleKey("itMgmt.cmd.softwareAdd");

        //
        // Template: ActionErrorsTemplate
        //
        ActionErrorsTemplate errorsTemplate = new ActionErrorsTemplate();
        standardTemplate.addTemplate(errorsTemplate);
        errorsTemplate.setShowRequiredFieldMsg(true);
        errorsTemplate.setMessage(Localizer.getText(requestContext, "itMgmt.softwareAdd.sectionHeader"));

        //
        // Template: CustomFieldsTemplate
        //
        CustomFieldsTemplate customFieldsTemplate = new CustomFieldsTemplate();
        standardTemplate.addTemplate(customFieldsTemplate);
        customFieldsTemplate.setObjectTypeId(ObjectTypes.SOFTWARE);
        customFieldsTemplate.setObjectAttrTypeId(actionForm.getSoftwareType());
        customFieldsTemplate.setForm(actionForm);

        return standardTemplate.findTemplate(STANDARD_TEMPLATE);
    }
}