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
import com.kwoksys.framework.properties.Localizer;
import com.kwoksys.framework.ui.WidgetUtils;
import com.kwoksys.framework.util.HtmlUtils;

/**
 * Contact spec template.
 */
public class EmployeeContactSpecTemplate extends BaseTemplate {

    private DetailTableTemplate detailTableTemplate = new DetailTableTemplate();

    private String contactDetailHeader;
    private Contact contact;

    public EmployeeContactSpecTemplate() {
        super(EmployeeContactSpecTemplate.class);

        addTemplate(detailTableTemplate);
    }

    public void applyTemplate() throws Exception {
        detailTableTemplate.setNumColumns(2);

        DetailTableTemplate.Td td = detailTableTemplate.new Td();
        td.setHeaderKey("common.column.contact_id");
        td.setValue(String.valueOf(contact.getId()));
        detailTableTemplate.addTd(td);

        td = detailTableTemplate.new Td();
        td.setHeaderKey("common.column.company_name");
        td.setValue(Links.getCompanyDetailsLink(requestContext, contact.getCompanyName(), contact.getCompanyId()).getString());
        detailTableTemplate.addTd(td);

        td = detailTableTemplate.new Td();
        td.setHeaderKey("common.column.contact_first_name");
        td.setValue(HtmlUtils.encode(contact.getFirstName()));
        detailTableTemplate.addTd(td);

        td = detailTableTemplate.new Td();
        td.setHeaderKey("common.column.contact_last_name");
        td.setValue(HtmlUtils.encode(contact.getLastName()));
        detailTableTemplate.addTd(td);

        td = detailTableTemplate.new Td();
        td.setHeaderKey("common.column.contact_title");
        td.setValue(HtmlUtils.encode(contact.getTitle()));
        detailTableTemplate.addTd(td);

        td = detailTableTemplate.new Td();
        td.setHeaderKey("common.column.contact_phone_work");
        td.setValue(HtmlUtils.encode(contact.getPhoneWork()));
        detailTableTemplate.addTd(td);

        td = detailTableTemplate.new Td();
        td.setHeaderKey("common.column.contact_phone_home");
        td.setValue(HtmlUtils.encode(contact.getPhoneHome()));
        detailTableTemplate.addTd(td);

        td = detailTableTemplate.new Td();
        td.setHeaderKey("common.column.contact_phone_mobile");
        td.setValue(HtmlUtils.encode(contact.getPhoneMobile()));
        detailTableTemplate.addTd(td);

        td = detailTableTemplate.new Td();
        td.setHeaderKey("common.column.contact_fax");
        td.setValue(HtmlUtils.encode(contact.getFax()));
        detailTableTemplate.addTd(td);

        td = detailTableTemplate.new Td();
        td.setHeaderKey("common.column.contact_email_primary");
        td.setValue(HtmlUtils.formatMailtoLink(contact.getEmailPrimary()));
        detailTableTemplate.addTd(td);

        td = detailTableTemplate.new Td();
        td.setHeaderKey("common.column.contact_email_secondary");
        td.setValue(HtmlUtils.formatMailtoLink(contact.getEmailSecondary()));
        detailTableTemplate.addTd(td);

        td = detailTableTemplate.new Td();
        if (contact.getMessenger1TypeAttribute(requestContext).isEmpty()) {
            td.setHeaderKey("common.column.contact_im");
        } else {
            Object[] args = {HtmlUtils.encode(contact.getMessenger1TypeAttribute(requestContext))};
            td.setHeaderText(Localizer.getText(requestContext, "common.column.contact_im_not_null", args));
        }
        td.setValue(HtmlUtils.encode(contact.getMessenger1Id()));
        detailTableTemplate.addTd(td);

        td = detailTableTemplate.new Td();
        if (contact.getMessenger2TypeAttribute(requestContext).isEmpty()) {
            td.setHeaderKey("common.column.contact_im");
        } else {
            Object[] args = {HtmlUtils.encode(contact.getMessenger2TypeAttribute(requestContext))};
            td.setHeaderText(Localizer.getText(requestContext, "common.column.contact_im_not_null", args));
        }
        td.setValue(HtmlUtils.encode(contact.getMessenger2Id()));
        detailTableTemplate.addTd(td);

        td = detailTableTemplate.new Td();
        td.setHeaderKey("common.column.contact_homepage_url");
        td.setValue(HtmlUtils.formatExternalLink(requestContext, contact.getHomepageUrl()));
        detailTableTemplate.addTd(td);

        td = detailTableTemplate.new Td();
        td.setHeaderKey("common.column.contact_description");
        td.setValue(HtmlUtils.formatMultiLineDisplay(contact.getDescription()));
        detailTableTemplate.addTd(td);

        td = detailTableTemplate.new Td();
        td.setHeaderKey("common.column.creator");
        td.setValue(WidgetUtils.formatCreatorInfo(requestContext, contact.getCreationDate(), contact.getCreator()));
        detailTableTemplate.addTd(td);

        td = detailTableTemplate.new Td();
        td.setHeaderKey("common.column.modifier");
        td.setValue(WidgetUtils.formatCreatorInfo(requestContext, contact.getModificationDate(), contact.getModifier()));
        detailTableTemplate.addTd(td);

        request.setAttribute("TemplateContactSpec.contactDetailHeader", contactDetailHeader);
    }

    @Override
    public String getJspPath() {
        return "/WEB-INF/jsp/contacts/ContactSpecTemplate.jsp";
    }

    public void setContactDetailHeader(String contactDetailHeader) {
        this.contactDetailHeader = contactDetailHeader;
    }

    public void setContact(Contact contact) {
        this.contact = contact;
    }
}
