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
package com.kwoksys.biz.contacts.dao;

import com.kwoksys.biz.contacts.dto.Contact;
import com.kwoksys.biz.admin.dto.AccessUser;
import com.kwoksys.biz.base.BaseDao;
import com.kwoksys.biz.system.dto.linking.ObjectLink;
import com.kwoksys.framework.http.RequestContext;
import com.kwoksys.framework.connections.database.QueryBits;
import com.kwoksys.framework.connections.database.QueryHelper;
import com.kwoksys.framework.exceptions.DatabaseException;
import com.kwoksys.framework.exceptions.ObjectNotFoundException;
import com.kwoksys.framework.util.DatetimeUtils;
import com.kwoksys.framework.util.StringUtils;
import com.kwoksys.biz.system.core.ObjectTypes;
import org.apache.struts.action.ActionMessages;

import java.sql.Connection;
import java.sql.ResultSet;
import java.util.List;
import java.util.ArrayList;

/**
 * ContactDao.
 */
public class ContactDao extends BaseDao {

    public ContactDao(RequestContext requestContext) {
        super(requestContext);
    }

    /**
     * Return details for a specific Contact.
     *
     * @param contactId
     * @return ..
     */
    public Contact getContact(Integer contactId) throws DatabaseException, ObjectNotFoundException {
        Connection conn = getConnection();

        QueryHelper queryHelper = new QueryHelper(ContactQueries.selectContactDetailQuery());
        queryHelper.addInputInt(contactId);

        try {
            ResultSet rs = queryHelper.executeQuery(conn);

            if (rs.next()) {
                Contact contact = new Contact();
                contact.setContactId(rs.getInt("contact_id"));
                contact.setUserId(rs.getInt("user_id"));
                contact.setCompanyId(rs.getInt("company_id"));
                contact.setCompanyName(StringUtils.replaceNull(rs.getString("company_name")));
                contact.setFirstName(StringUtils.replaceNull(rs.getString("contact_first_name")));
                contact.setLastName(StringUtils.replaceNull(rs.getString("contact_last_name")));
                contact.setDescription(StringUtils.replaceNull(rs.getString("contact_description")));
                contact.setTitle(StringUtils.replaceNull(rs.getString("contact_title")));
                contact.setPhoneHome(StringUtils.replaceNull(rs.getString("contact_phone_home")));
                contact.setPhoneMobile(StringUtils.replaceNull(rs.getString("contact_phone_mobile")));
                contact.setPhoneWork(StringUtils.replaceNull(rs.getString("contact_phone_work")));
                contact.setFax(StringUtils.replaceNull(rs.getString("contact_fax")));
                contact.setEmailPrimary(StringUtils.replaceNull(rs.getString("contact_email_primary")));
                contact.setEmailSecondary(StringUtils.replaceNull(rs.getString("contact_email_secondary")));
                contact.setHomepageUrl(StringUtils.replaceNull(rs.getString("contact_homepage_url")));
                contact.setAddressStreetPrimary(StringUtils.replaceNull(rs.getString("address_street_primary")));
                contact.setAddressCityPrimary(StringUtils.replaceNull(rs.getString("address_city_primary")));
                contact.setAddressStatePrimary(StringUtils.replaceNull(rs.getString("address_state_primary")));
                contact.setAddressZipcodePrimary(StringUtils.replaceNull(rs.getString("address_zipcode_primary")));
                contact.setAddressCountryPrimary(StringUtils.replaceNull(rs.getString("address_country_primary")));
                contact.setMessenger1Type(rs.getInt("messenger_1_type"));
                contact.setMessenger1Id(StringUtils.replaceNull(rs.getString("messenger_1_id")));
                contact.setMessenger2Type(rs.getInt("messenger_2_type"));
                contact.setMessenger2Id(StringUtils.replaceNull(rs.getString("messenger_2_id")));
                contact.setCreationDate(DatetimeUtils.getDate(rs, "creation_date"));
                contact.setModificationDate(DatetimeUtils.getDate(rs, "modification_date"));

                contact.setCreator(new AccessUser());
                contact.getCreator().setId(rs.getInt("creator"));
                contact.getCreator().setUsername(rs.getString("creator_username"));
                contact.getCreator().setDisplayName(rs.getString("creator_display_name"));

                contact.setModifier(new AccessUser());
                contact.getModifier().setId(rs.getInt("modifier"));
                contact.getModifier().setUsername(rs.getString("modifier_username"));
                contact.getModifier().setDisplayName(rs.getString("modifier_display_name"));
                return contact;
            }
        } catch (Exception e) {
            // Database problem
            throw new DatabaseException(e, queryHelper);

        } finally {
            queryHelper.closeRs();
            closeConnection(conn);
        }
        throw new ObjectNotFoundException();
    }

    /**
     * Return all contacts.
     *
     * @param query
     * @return ..
     */
    public List<Contact> getContacts(QueryBits query) throws DatabaseException {
        Connection conn = getConnection();

        QueryHelper queryHelper = new QueryHelper(ContactQueries.selectContactListQuery(query));

        try {
            List<Contact> contacts = new ArrayList();
            ResultSet rs = queryHelper.executeQuery(conn);

            while (rs.next()) {
                Contact contact = new Contact();
                contact.setContactId(rs.getInt("contact_id"));
                contact.setFirstName(StringUtils.replaceNull(rs.getString("contact_first_name")));
                contact.setLastName(StringUtils.replaceNull(rs.getString("contact_last_name")));
                contact.setTitle(StringUtils.replaceNull(rs.getString("contact_title")));
                contact.setEmailPrimary(StringUtils.replaceNull(rs.getString("contact_email_primary")));
                contact.setCompanyId(rs.getInt("company_id"));
                contact.setCompanyName(StringUtils.replaceNull(rs.getString("company_name")));
                contacts.add(contact);
            }
            return contacts;

        } catch (Exception e) {
            // Database problem
            throw new DatabaseException(e, queryHelper);

        } finally {
            queryHelper.closeRs();
            closeConnection(conn);
        }
    }

    /**
     * Returns a list of linked Contacts.
     * @param query
     * @return
     * @throws DatabaseException
     */
    public List<Contact> getLinkedContacts(QueryBits query, ObjectLink objectMap) throws DatabaseException {
        Connection conn = getConnection();

        QueryHelper queryHelper = new QueryHelper(ContactQueries.selectLinkedContactsQuery(query));
        queryHelper.addInputInt(objectMap.getObjectId());
        queryHelper.addInputInt(objectMap.getObjectTypeId());
        queryHelper.addInputInt(objectMap.getLinkedObjectTypeId());

        try {
            List<Contact> contacts = new ArrayList();
            ResultSet rs = queryHelper.executeQuery(conn);

            while (rs.next()) {
                Contact contact = new Contact();
                contact.setContactId(rs.getInt("contact_id"));
                contact.setFirstName(StringUtils.replaceNull(rs.getString("contact_first_name")));
                contact.setLastName(StringUtils.replaceNull(rs.getString("contact_last_name")));
                contact.setTitle(StringUtils.replaceNull(rs.getString("contact_title")));
                contact.setEmailPrimary(StringUtils.replaceNull(rs.getString("contact_email_primary")));
                contact.setCompanyId(rs.getInt("company_id"));
                contact.setCompanyName(StringUtils.replaceNull(rs.getString("company_name")));
                contact.setRelDescription(StringUtils.replaceNull(rs.getString("relationship_name")));
                contacts.add(contact);
            }
            return contacts;

        } catch (Exception e) {
            // Database problem
            throw new DatabaseException(e, queryHelper);

        } finally {
            queryHelper.closeRs();
            closeConnection(conn);
        }
    }

    /**
     * Get Contact count.
     *
     * @param query
     * @return ..
     */
    public int getCount(QueryBits query) throws DatabaseException {
        return getRowCount(ContactQueries.getContactCountQuery(query));
    }

    public ActionMessages delete(Contact contact) throws DatabaseException {
        QueryHelper queryHelper = new QueryHelper(ContactQueries.deleteContactQuery());
        queryHelper.addInputInt(ObjectTypes.CONTACT);
        queryHelper.addInputInt(contact.getId());
        queryHelper.addInputInt(contact.getCompanyId());
        queryHelper.addInputInt(contact.getCompanyContactType());

        return executeProcedure(queryHelper);
    }
}
