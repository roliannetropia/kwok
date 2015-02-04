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
package com.kwoksys.action.contacts;

import com.kwoksys.biz.base.BaseTemplate;
import com.kwoksys.action.common.template.DetailTableTemplate;
import com.kwoksys.biz.ServiceProvider;
import com.kwoksys.biz.admin.dto.AttributeField;
import com.kwoksys.biz.contacts.ContactService;
import com.kwoksys.biz.contacts.dao.ContactQueries;
import com.kwoksys.biz.contacts.dto.Company;
import com.kwoksys.biz.contacts.dto.CompanyTag;
import com.kwoksys.biz.system.core.AttributeManager;
import com.kwoksys.biz.system.core.Attributes;
import com.kwoksys.framework.properties.Localizer;
import com.kwoksys.framework.connections.database.QueryBits;
import com.kwoksys.framework.ui.WidgetUtils;
import com.kwoksys.framework.util.HtmlUtils;
import com.kwoksys.framework.util.UrlUtils;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Template class for Company spec.
 * columnText
 * columnValue
 * rowClass
 */
public class CompanySpecTemplate extends BaseTemplate {

    private DetailTableTemplate detailTableTemplate = new DetailTableTemplate();

    private String companyHeaderText;
    private Company company;
    private boolean populateCompanyTagList;

    public CompanySpecTemplate(Company company) {
        super(CompanySpecTemplate.class);
        this.company = company;

        addTemplate(detailTableTemplate);
    }

    public void applyTemplate() throws Exception {
        AttributeManager attributeManager = new AttributeManager(requestContext);

        DetailTableTemplate.Td td = detailTableTemplate.new Td();
        td.setHeaderKey("common.column.company_id");
        td.setValue(String.valueOf(company.getId()));
        detailTableTemplate.addTd(td);

        td = detailTableTemplate.new Td();
        td.setHeaderKey("common.column.company_name");
        td.setValue(HtmlUtils.encode(company.getName()));
        detailTableTemplate.addTd(td);

        td = detailTableTemplate.new Td();
        td.setHeaderKey("common.column.company_description");
        td.setValue(HtmlUtils.formatMultiLineDisplay(company.getDescription()));
        detailTableTemplate.addTd(td);

        ContactService contactService = ServiceProvider.getContactService(requestContext);
        company.setTypeIds(contactService.getCompanyTypes(company.getId()));
        if (company.getTypeIds() != null) {
            StringBuilder sb = new StringBuilder();
            Map<Integer, AttributeField> map = attributeManager.getAttrFieldMapCache(Attributes.COMPANY_TYPE);
            for (Integer typeId : company.getTypeIds()) {
                if (!sb.toString().isEmpty()) {
                    sb.append(", ");
                }
                sb.append(Localizer.getText(requestContext, "system.attribute_field." + map.get(typeId).getName()));
            }

            td = detailTableTemplate.new Td();
            td.setHeaderKey("common.column.company_types");
            td.setValue(sb.toString());
            detailTableTemplate.addTd(td);
        }

        td = detailTableTemplate.new Td();
        td.setHeaderKey("common.column.creator");
        td.setValue(WidgetUtils.formatCreatorInfo(requestContext, company.getCreationDate(), company.getCreator()));
        detailTableTemplate.addTd(td);

        td = detailTableTemplate.new Td();
        td.setHeaderKey("common.column.modifier");
        td.setValue(WidgetUtils.formatCreatorInfo(requestContext, company.getModificationDate(), company.getModifier()));
        detailTableTemplate.addTd(td);

        if (populateCompanyTagList) {
            // Get a list of tags.
            QueryBits tagQuery = new QueryBits();
            tagQuery.addSortColumn(ContactQueries.getOrderByColumn(CompanyTag.TAG_NAME));

            List<Map> currentList = contactService.getCompanyTags(tagQuery, company.getId());
            List newList = new ArrayList();

            for (Map map : currentList) {
                Map newMap = new HashMap();
                newMap.put("tagName", map.get("tag_name"));
                newMap.put("tagUrl", UrlUtils.encode((String)map.get("tag_name")));
                newList.add(newMap);
            }

            request.setAttribute("TemplateCompanySpec_companyTagList", newList);
        }

        companyHeaderText = Localizer.getText(requestContext, "contactMgmt.companyDetail.header", new Object[] {company.getName()});
    }

    @Override
    public String getJspPath() {
        return "/WEB-INF/jsp/contacts/CompanySpecTemplate.jsp";
    }

    public String getCompanyHeaderText() {
        return companyHeaderText;
    }
    public void setPopulateCompanyTagList(boolean populateCompanyTagList) {
        this.populateCompanyTagList = populateCompanyTagList;
    }
}
