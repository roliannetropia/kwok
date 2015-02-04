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
import com.kwoksys.biz.contacts.dto.Contact;
import com.kwoksys.biz.system.core.Links;
import com.kwoksys.framework.util.HtmlUtils;

/**
 * CompanyContactTemplate
 */
public class CompanyContactSpecTemplate extends BaseTemplate {

    private Contact contact;

    private DetailTableTemplate detailTableTemplate = new DetailTableTemplate();

    public CompanyContactSpecTemplate(Contact contact) {
        super(CompanyContactSpecTemplate.class);
        this.contact = contact;

        addTemplate(detailTableTemplate);
    }

    public void applyTemplate() throws Exception {
        detailTableTemplate.setNumColumns(2);

        DetailTableTemplate.Td td = detailTableTemplate.new Td();
        td.setHeaderKey("common.column.contact_main_label");
        td.setValue(HtmlUtils.encode(contact.getTitle()));
        detailTableTemplate.addTd(td);

        td = detailTableTemplate.new Td();
        td.setHeaderKey("common.column.company_name");
        td.setValue(Links.getCompanyDetailsLink(requestContext, contact.getCompanyName(), contact.getCompanyId()).getString());
        detailTableTemplate.addTd(td);

        td = detailTableTemplate.new Td();
        td.setHeaderKey("common.column.contact_main_phone");
        td.setValue(HtmlUtils.encode(contact.getPhoneWork()));
        detailTableTemplate.addTd(td);

        td = detailTableTemplate.new Td();
        td.setHeaderKey("common.column.contact_fax");
        td.setValue(HtmlUtils.encode(contact.getFax()));
        detailTableTemplate.addTd(td);

        td = detailTableTemplate.new Td();
        td.setHeaderKey("common.column.contact_main_email");
        td.setValue(HtmlUtils.formatMailtoLink(contact.getEmailPrimary()));
        detailTableTemplate.addTd(td);

        td = detailTableTemplate.new Td();
        td.setHeaderKey("common.column.contact_main_website");
        td.setValue(HtmlUtils.formatExternalLink(requestContext, contact.getHomepageUrl()));
        detailTableTemplate.addTd(td);

        td = detailTableTemplate.new Td();
        td.setHeaderKey("common.column.contact_address_street_primary");
        td.setValue(HtmlUtils.encode(contact.getAddressStreetPrimary()));
        detailTableTemplate.addTd(td);

        td = detailTableTemplate.new Td();
        td.setHeaderKey("common.column.contact_address_city_primary");
        td.setValue(HtmlUtils.encode(contact.getAddressCityPrimary()));
        detailTableTemplate.addTd(td);

        td = detailTableTemplate.new Td();
        td.setHeaderKey("common.column.contact_address_state_primary");
        td.setValue(HtmlUtils.encode(contact.getAddressStatePrimary()));
        detailTableTemplate.addTd(td);

        td = detailTableTemplate.new Td();
        td.setHeaderKey("common.column.contact_address_zipcode_primary");
        td.setValue(HtmlUtils.encode(contact.getAddressZipcodePrimary()));
        detailTableTemplate.addTd(td);

        td = detailTableTemplate.new Td();
        td.setHeaderKey("common.column.contact_address_country_primary");
        td.setValue(HtmlUtils.encode(contact.getAddressCountryPrimary()));
        detailTableTemplate.addTd(td);

        td = detailTableTemplate.new Td();
        td.setHeaderKey("common.column.contact_description");
        td.setValue(HtmlUtils.formatMultiLineDisplay(contact.getDescription()));
        detailTableTemplate.addTd(td);
    }

    @Override
    public String getJspPath() {
        return "/WEB-INF/jsp/contacts/CompanyContactSpecTemplate.jsp";
    }
}