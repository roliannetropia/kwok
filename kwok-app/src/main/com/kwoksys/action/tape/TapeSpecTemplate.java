///*
// * ====================================================================
// * Copyright 2005-2011 Wai-Lun Kwok
// *
// * http://www.kwoksys.com/LICENSE
// *
// * Licensed under the Apache License, Version 2.0 (the "License");
// * you may not use this file except in compliance with the License.
// * You may obtain a copy of the License at
// *
// * http://www.apache.org/licenses/LICENSE-2.0
// *
// * Unless required by applicable law or agreed to in writing, software
// * distributed under the License is distributed on an "AS IS" BASIS,
// * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
// * See the License for the specific language governing permissions and
// * limitations under the License.
// * ====================================================================
// */
//package com.kwoksys.action.tape;
//
//import com.kwoksys.action.common.template.DetailTableTemplate;
//import com.kwoksys.biz.ServiceProvider;
//import com.kwoksys.biz.admin.dto.AccessUser;
//import com.kwoksys.biz.admin.dto.AttributeField;
//import com.kwoksys.biz.auth.core.Access;
//import com.kwoksys.biz.base.BaseTemplate;
//import com.kwoksys.biz.contracts.dao.ContractQueries;
//import com.kwoksys.biz.contracts.dto.Contract;
//import com.kwoksys.biz.tape.TapeService;
//import com.kwoksys.biz.tape.core.TapeUtils;
//import com.kwoksys.biz.tape.dto.Tape;
//import com.kwoksys.biz.system.core.AppPaths;
//import com.kwoksys.biz.system.core.AttributeManager;
//import com.kwoksys.biz.system.core.Attributes;
//import com.kwoksys.biz.system.core.Links;
//import com.kwoksys.biz.system.core.configs.ConfigManager;
//import com.kwoksys.framework.connections.database.QueryBits;
//import com.kwoksys.framework.properties.Localizer;
//import com.kwoksys.framework.ui.Link;
//import com.kwoksys.framework.ui.WidgetUtils;
//import com.kwoksys.framework.util.CurrencyUtils;
//import com.kwoksys.framework.util.DatetimeUtils;
//import com.kwoksys.framework.util.HtmlUtils;
//
//import java.util.List;
//
///**
// * TapeSpecTemplate
// */
//public class TapeSpecTemplate extends BaseTemplate {
//
//    private DetailTableTemplate detailTableTemplate = new DetailTableTemplate();
//
//    private Tape tape;
//    private boolean populateLinkedContract;
//    private int columns = 2;
//    private boolean disableHeader;
//    private String headerText;
//
//    public TapeSpecTemplate(Tape tape) {
//        super(TapeSpecTemplate.class);
//        this.tape = tape;
//
//        addTemplate(detailTableTemplate);
//    }
//
//    public void applyTemplate() throws Exception {
//        AccessUser accessUser = requestContext.getUser();
//        AttributeManager attributeManager = new AttributeManager(requestContext);
//
//        detailTableTemplate.setNumColumns(columns);
//
//        DetailTableTemplate.Td td = detailTableTemplate.new Td();
//        td.setHeaderKey("common.column.tape_id");
//        td.setValue(String.valueOf(tape.getId()));
//        detailTableTemplate.addTd(td);
//
//        td = detailTableTemplate.new Td();
//        td.setHeaderKey("common.column.tape_name");
//        td.setValue(HtmlUtils.encode(tape.getName()));
//        detailTableTemplate.addTd(td);
//
//        td = detailTableTemplate.new Td();
//        td.setHeaderKey("common.column.tape_description");
//        td.setValue(HtmlUtils.formatMultiLineDisplay(tape.getDescription()));
//        detailTableTemplate.addTd(td);
//
//        td = detailTableTemplate.new Td();
//        td.setHeaderKey("common.column.tape_manufacturer_name");
//        td.setValue(Links.getCompanyDetailsLink(requestContext, tape.getManufacturerName(),
//                tape.getManufacturerId()).getString());
//        detailTableTemplate.addTd(td);
//
//        td = detailTableTemplate.new Td();
//        td.setHeaderKey("common.column.tape_vendor_name");
//        td.setValue(Links.getCompanyDetailsLink(requestContext, tape.getVendorName(), tape.getVendorId()).getString());
//        detailTableTemplate.addTd(td);
//
//        AttributeField attrField = attributeManager.getAttrFieldMapCache(Attributes.TAPE_TYPE).get(tape.getType());
//
//        td = detailTableTemplate.new Td();
//        td.setHeaderKey("common.column.tape_type");
//        td.setValue(Links.getAttrFieldIcon(requestContext, attrField).getString());
//        detailTableTemplate.addTd(td);
//
//        td = detailTableTemplate.new Td();
//        td.setHeaderKey("common.column.tape_status");
//        td.setValue(HtmlUtils.encode(attributeManager.getAttrFieldNameCache(Attributes.TAPE_STATUS, tape.getStatus())));
//        detailTableTemplate.addTd(td);
//
//        td = detailTableTemplate.new Td();
//        td.setHeaderKey("common.column.tape_model_name");
//        td.setValue(HtmlUtils.encode(tape.getModelName()));
//        detailTableTemplate.addTd(td);
//
//        td = detailTableTemplate.new Td();
//        td.setHeaderKey("common.column.tape_model_number");
//        td.setValue(HtmlUtils.encode(tape.getModelNumber()));
//        detailTableTemplate.addTd(td);
//
//        td = detailTableTemplate.new Td();
//        td.setHeaderKey("common.column.tape_serial_number");
//        td.setValue(HtmlUtils.encode(tape.getSerialNumber()));
//        detailTableTemplate.addTd(td);
//
//        td = detailTableTemplate.new Td();
//        td.setHeaderKey("common.column.tape_purchase_price");
//        td.setValue(CurrencyUtils.formatCurrency(tape.getPurchasePriceRaw(), ConfigManager.system.getCurrencySymbol()));
//        detailTableTemplate.addTd(td);
//
//        td = detailTableTemplate.new Td();
//        td.setHeaderKey("common.column.tape_purchase_date");
//        td.setValue(DatetimeUtils.toShortDate(tape.getTapePurchaseDate()));
//        detailTableTemplate.addTd(td);
//
//        td = detailTableTemplate.new Td();
//        td.setHeaderKey("common.column.tape_warranty_expire_date");
//        td.setValue(TapeUtils.formatWarrantyExpirationDate(requestContext, requestContext.getSysdate().getTime(),
//                tape.getWarrantyExpireDate()));
//        detailTableTemplate.addTd(td);
//
//        td = detailTableTemplate.new Td();
//        td.setHeaderKey("common.column.tape_last_service_date");
//        td.setValue(tape.getLastServicedOn());
//        detailTableTemplate.addTd(td);
//
//        td = detailTableTemplate.new Td();
//        td.setHeaderKey("common.column.tape_location");
//        td.setValue(HtmlUtils.encode(attributeManager.getAttrFieldNameCache(Attributes.TAPE_LOCATION, tape.getLocation())));
//        detailTableTemplate.addTd(td);
//
//        td = detailTableTemplate.new Td();
//        td.setHeaderKey("common.column.tape_owner_name");
//        boolean canViewUserPage = Access.hasPermission(accessUser, AppPaths.ADMIN_USER_DETAIL);
//        td.setValue(Links.getUserIconLink(requestContext, tape.getOwner(), canViewUserPage, true).getString());
//        detailTableTemplate.addTd(td);
//
//        td = detailTableTemplate.new Td();
//        td.setHeaderKey("common.column.creator");
//        td.setValue(WidgetUtils.formatCreatorInfo(requestContext, tape.getCreationDate(), tape.getCreator()));
//        detailTableTemplate.addTd(td);
//
//        td = detailTableTemplate.new Td();
//        td.setHeaderKey("common.column.modifier");
//        td.setValue(WidgetUtils.formatCreatorInfo(requestContext, tape.getModificationDate(), tape.getModifier()));
//        detailTableTemplate.addTd(td);
//
//        if (populateLinkedContract) {
//            boolean canViewContract = Access.hasPermission(accessUser, AppPaths.CONTRACTS_DETAIL);
//
//            // Get linked contracts
//            TapeService tapeService = ServiceProvider.getTapeService(requestContext);
//
//            QueryBits contractQuery = new QueryBits();
//            contractQuery.addSortColumn(ContractQueries.getOrderByColumn(Contract.NAME));
//
//            List<Contract> contractDataset = tapeService.getLinkedContracts(contractQuery, tape.getId());
//
//            StringBuilder contracts = new StringBuilder();
//            for (Contract contract: contractDataset) {
//                Link link = new Link(requestContext);
//                link.setTitle(contract.getName());
//
//                if (canViewContract) {
//                    link.setAjaxPath(AppPaths.CONTRACTS_DETAIL + "?contractId=" + contract.getId());
//                }
//                if (contracts.length() != 0) {
//                    contracts.append(", ");
//                }
//                contracts.append(link.getString());
//            }
//            if (contracts.length() != 0) {
//	            request.setAttribute("TapeSpecTemplate_linkedContracts", contracts);
//            }
//        }
//
//        headerText = Localizer.getText(requestContext, "itMgmt.tapeDetail.header", new String[]{tape.getName()});
//    }
//
//    @Override
//    public String getJspPath() {
//        return "/WEB-INF/jsp/tape/TapeSpecTemplate.jsp";
//    }
//
//    public void setPopulateLinkedContract(boolean populateLinkedContract) {
//        this.populateLinkedContract = populateLinkedContract;
//    }
//
//    public int getColumns() {
//        return columns;
//    }
//
//    public void setColumns(int columns) {
//        this.columns = columns;
//    }
//
//    public void setDisableHeader(boolean disableHeader) {
//        this.disableHeader = disableHeader;
//    }
//
//    public boolean isDisableHeader() {
//        return disableHeader;
//    }
//
//    public String getHeaderText() {
//        return headerText;
//    }
//}
