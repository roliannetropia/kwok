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

import com.kwoksys.biz.base.BaseTemplate;
import com.kwoksys.action.common.template.DetailTableTemplate;
import com.kwoksys.biz.ServiceProvider;
import com.kwoksys.biz.admin.dto.AccessUser;
import com.kwoksys.biz.admin.dto.AttributeField;
import com.kwoksys.biz.auth.core.Access;
import com.kwoksys.biz.contracts.dao.ContractQueries;
import com.kwoksys.biz.contracts.dto.Contract;
import com.kwoksys.biz.software.SoftwareService;
import com.kwoksys.biz.software.dto.Software;
import com.kwoksys.biz.system.core.*;
import com.kwoksys.framework.connections.database.QueryBits;
import com.kwoksys.framework.properties.Localizer;
import com.kwoksys.framework.ui.Link;
import com.kwoksys.framework.ui.WidgetUtils;
import com.kwoksys.framework.util.DatetimeUtils;
import com.kwoksys.framework.util.HtmlUtils;

import java.util.ArrayList;
import java.util.List;

/**
 * Template class for Software spec.
 * columnText
 * columnValue
 * rowClass
 */
public class SoftwareSpecTemplate extends BaseTemplate {

    private DetailTableTemplate detailTableTemplate = new DetailTableTemplate();

    private Software software;
    private boolean populateLinkedContract;

    private List linkedContracts;

    public SoftwareSpecTemplate(Software software) {
        super(SoftwareSpecTemplate.class);
        this.software = software;

        addTemplate(detailTableTemplate);
    }

    public void applyTemplate() throws Exception {
        AccessUser accessUser = requestContext.getUser();

        detailTableTemplate.setNumColumns(2);

        DetailTableTemplate.Td td = detailTableTemplate.new Td();
        td.setHeaderKey("common.column.software_id");
        td.setValue(String.valueOf(software.getId()));
        detailTableTemplate.addTd(td);

        td = detailTableTemplate.new Td();
        td.setHeaderKey("common.column.software_name");
        td.setValue(HtmlUtils.encode(software.getName()));
        detailTableTemplate.addTd(td);

        td = detailTableTemplate.new Td();
        td.setHeaderKey("common.column.software_version");
        td.setValue(HtmlUtils.encode(software.getVersion()));
        detailTableTemplate.addTd(td);

        td = detailTableTemplate.new Td();
        td.setHeaderKey("common.column.software_expire_date");
        td.setValue(DatetimeUtils.toShortDate(software.getExpireDate()));
        detailTableTemplate.addTd(td);

        td = detailTableTemplate.new Td();
        td.setHeaderKey("common.column.software_description");
        td.setValue(HtmlUtils.formatMultiLineDisplay(software.getDescription()));
        detailTableTemplate.addTd(td);

        boolean canViewPage = Access.hasPermission(accessUser, AppPaths.SOFTWARE_DETAIL);
        td = detailTableTemplate.new Td();
        td.setHeaderKey("common.column.software_owner");
        td.setValue(Links.getUserIconLink(requestContext, software.getOwner(), canViewPage, true).getString());
        detailTableTemplate.addTd(td);

        td = detailTableTemplate.new Td();
        td.setHeaderKey("common.column.software_manufacturer");
        td.setValue(Links.getCompanyDetailsLink(requestContext, software.getManufacturerName(),
                software.getManufacturerId()).getString());
        detailTableTemplate.addTd(td);

        td = detailTableTemplate.new Td();
        td.setHeaderKey("common.column.software_vendor");
        td.setValue(Links.getCompanyDetailsLink(requestContext, software.getVendorName(),
                software.getVendorId()).getString());
        detailTableTemplate.addTd(td);

        AttributeManager attributeManager = new AttributeManager(requestContext);

        AttributeField attrFieldType = attributeManager.getAttrFieldMapCache(Attributes.SOFTWARE_TYPE).get(software.getType());

        td = detailTableTemplate.new Td();
        td.setHeaderKey("common.column.software_type");
        td.setValue(Links.getAttrFieldIcon(requestContext, attrFieldType).getString());
        detailTableTemplate.addTd(td);

        AttributeField attrFieldOS = attributeManager.getAttrFieldMapCache(Attributes.SOFTWARE_OS).get(software.getOs());

        td = detailTableTemplate.new Td();
        td.setHeaderKey("common.column.software_platform");
        td.setValue(Links.getAttrFieldIcon(requestContext, attrFieldOS).getString());
        detailTableTemplate.addTd(td);

        td = detailTableTemplate.new Td();
        td.setHeaderKey("common.column.software_quoted_retail_price");
        td.setValue(HtmlUtils.encode(software.getQuotedRetailPrice()));
        detailTableTemplate.addTd(td);

        td = detailTableTemplate.new Td();
        td.setHeaderKey("common.column.software_quoted_oem_price");
        td.setValue(HtmlUtils.encode(software.getQuotedOemPrice()));
        detailTableTemplate.addTd(td);

        td = detailTableTemplate.new Td();
        td.setHeaderKey("common.column.license_purchased");
        td.setValue(Localizer.getText(requestContext, "itMgmt.softwareDetail.numberOfLicenses", new Object[]{software.getLicensePurchased()}));
        detailTableTemplate.addTd(td);

        td = detailTableTemplate.new Td();
        td.setHeaderKey("common.column.license_installed");
        td.setValue(Localizer.getText(requestContext, "itMgmt.softwareDetail.numberOfLicenses", new Object[] {software.getLicenseInstalled()}));
        detailTableTemplate.addTd(td);

        td = detailTableTemplate.new Td();
        td.setHeaderKey("common.column.license_available");
        Object[] object3 = {software.getLicenseAvailable()};
        if (Software.isEnoughLicenses(software.getLicenseAvailable())) {
            td.setValue(Localizer.getText(requestContext, "itMgmt.softwareDetail.numberOfLicenses", object3));
        } else {
            td.setValue(Localizer.getText(requestContext, "itMgmt.softwareDetail.numberOfNeedLicenses", object3));
        }
        detailTableTemplate.addTd(td);

        td = detailTableTemplate.new Td();
        td.setHeaderKey("common.column.creator");
        td.setValue(WidgetUtils.formatCreatorInfo(requestContext, software.getCreationDate(), software.getCreator()));
        detailTableTemplate.addTd(td);

        td = detailTableTemplate.new Td();
        td.setHeaderKey("common.column.modifier");
        td.setValue(WidgetUtils.formatCreatorInfo(requestContext, software.getModificationDate(), software.getModifier()));
        detailTableTemplate.addTd(td);

        if (populateLinkedContract) {
            boolean canViewContract = Access.hasPermission(accessUser, AppPaths.CONTRACTS_DETAIL);
            linkedContracts = new ArrayList();

            // Get linked contracts
            SoftwareService softwareService = ServiceProvider.getSoftwareService(requestContext);

            QueryBits contractQuery = new QueryBits();
            contractQuery.addSortColumn(ContractQueries.getOrderByColumn(Contract.NAME));

            List<Contract> contractDataset = softwareService.getSoftwareContracts(contractQuery, software.getId());

            for (Contract contract: contractDataset) {
                Link link = new Link(requestContext);
                link.setTitle(contract.getName());

                if (canViewContract) {
                    link.setAjaxPath(AppPaths.CONTRACTS_DETAIL + "?contractId=" + contract.getId());
                }
                linkedContracts.add(link.getString());
            }
        }
    }

    @Override
    public String getJspPath() {
        return "/WEB-INF/jsp/software/SoftwareSpecTemplate.jsp";
    }

    public String getHeaderText() {
        return Localizer.getText(requestContext, "itMgmt.softwareDetail.header", new String[] {software.getName()});
    }

    public List getLinkedContracts() {
        return linkedContracts;
    }

    public void setPopulateLinkedContract(boolean populateLinkedContract) {
        this.populateLinkedContract = populateLinkedContract;
    }
}
