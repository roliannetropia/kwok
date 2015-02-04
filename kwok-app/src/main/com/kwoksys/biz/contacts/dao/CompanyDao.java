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

import com.kwoksys.biz.admin.dao.AdminQueries;
import com.kwoksys.biz.admin.dao.AttributeDao;
import com.kwoksys.biz.admin.dto.AccessUser;
import com.kwoksys.biz.base.BaseDao;
import com.kwoksys.biz.contacts.dto.Company;
import com.kwoksys.biz.contacts.dto.CompanyTag;
import com.kwoksys.biz.system.core.Attributes;
import com.kwoksys.biz.system.core.ObjectTypes;
import com.kwoksys.biz.system.dto.linking.ObjectLink;
import com.kwoksys.framework.http.RequestContext;
import com.kwoksys.framework.connections.database.QueryBits;
import com.kwoksys.framework.connections.database.QueryHelper;
import com.kwoksys.framework.exceptions.DatabaseException;
import com.kwoksys.framework.exceptions.ObjectNotFoundException;
import com.kwoksys.framework.util.DatetimeUtils;
import com.kwoksys.framework.util.StringUtils;
import org.apache.struts.action.ActionMessages;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.Types;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * CompanyDao.
 */
public class CompanyDao extends BaseDao {

    public CompanyDao(RequestContext requestContext) {
        super(requestContext);
    }

    /**
     * Return all Companies.
     */
    public List getList(QueryBits query, QueryHelper queryHelper) throws DatabaseException {
        Connection conn = getConnection();

        if (queryHelper == null) {
            queryHelper = new QueryHelper(ContactQueries.selectCompanyListQuery(query));
        }

        try {
            List list = new ArrayList();
            ResultSet rs = queryHelper.executeQuery(conn);

            while (rs.next()) {
                Company company = new Company();
                company.setId(rs.getInt("company_id"));
                company.setName(rs.getString("company_name"));
                company.setDescription(StringUtils.replaceNull(rs.getString("company_description")));
                list.add(company);
            }
            return list;

        } catch (Exception e) {
            // Database problem
            throw new DatabaseException(e, queryHelper);

        } finally {
            queryHelper.closeRs();
            closeConnection(conn);
        }
    }

    public int getCount(QueryBits query) throws DatabaseException {
        return getRowCount(ContactQueries.getCompanyCountQuery(query));
    }

    /**
     * Return details for a specific Company
     *
     * @param companyId
     * @return ..
     */
    public Company getCompany(Integer companyId) throws DatabaseException, ObjectNotFoundException {
        Connection conn = getConnection();

        QueryHelper queryHelper = new QueryHelper(ContactQueries.selectCompanyDetailQuery());
        queryHelper.addInputInt(companyId);

        try {
            ResultSet rs = queryHelper.executeQuery(conn);

            if (rs.next()) {
                Company company = new Company();
                company.setId(rs.getInt("company_id"));
                company.setName(StringUtils.replaceNull(rs.getString("company_name")));
                company.setDescription(StringUtils.replaceNull(rs.getString("company_description")));
                company.setTags("");
                company.setCountMainContact(rs.getInt("main_contact_count"));
                company.setCountEmployeeContact(rs.getInt("employee_contact_count"));
                company.setCountFile(rs.getInt("file_count"));
                company.setCountBookmark(rs.getInt("bookmark_count"));
                company.setCountNote(rs.getInt("note_count"));
                company.setCreationDate(DatetimeUtils.getDate(rs, "creation_date"));
                company.setModificationDate(DatetimeUtils.getDate(rs, "modification_date"));

                company.setCreator(new AccessUser());
                company.getCreator().setId(rs.getInt("creator"));
                company.getCreator().setUsername(rs.getString("creator_username"));
                company.getCreator().setDisplayName(rs.getString("creator_display_name"));

                company.setModifier(new AccessUser());
                company.getModifier().setId(rs.getInt("modifier"));
                company.getModifier().setUsername(rs.getString("modifier_username"));
                company.getModifier().setDisplayName(rs.getString("modifier_display_name"));
                
                return company;
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

    public List<Company> getLinkedCompanyList(QueryBits query, ObjectLink objectMap) throws DatabaseException {
        QueryHelper queryHelper;

        if (objectMap.getLinkedObjectId() == 0) {
            queryHelper = new QueryHelper(ContactQueries.selectLinkedCompanyListQuery(query));
            queryHelper.addInputInt(objectMap.getObjectId());
            queryHelper.addInputInt(objectMap.getObjectTypeId());
            queryHelper.addInputInt(objectMap.getLinkedObjectTypeId());

        } else {
            queryHelper = new QueryHelper(ContactQueries.selectObjectCompanyListQuery(query));
            queryHelper.addInputInt(objectMap.getLinkedObjectId());
            queryHelper.addInputInt(objectMap.getLinkedObjectTypeId());
            queryHelper.addInputInt(objectMap.getObjectTypeId());
        }
        return getList(query, queryHelper);
    }

    /**
     * Return all available Company tags.
     *
     * @return ..
     */
    public List<Map> getTagList(QueryBits query, Integer companyId) throws DatabaseException {
        QueryHelper queryHelper = new QueryHelper(ContactQueries.selectCompanyTagsQuery(query));
        queryHelper.addInputInt(companyId);

        return executeQueryReturnList(queryHelper);
    }

    public List<Integer> getCompanyTypes(QueryBits query) throws DatabaseException {
        Connection conn = getConnection();

        QueryHelper queryHelper = new QueryHelper(AdminQueries.selectAttributeValuesQuery(query));
        
        try {
            List<Integer> list = new ArrayList();
            ResultSet rs = queryHelper.executeQuery(conn);

            while (rs.next()) {
                list.add(rs.getInt("attribute_field_id"));
            }
            return list;

        } catch (Exception e) {
            // Database problem
            throw new DatabaseException(e, queryHelper);

        } finally {
            queryHelper.closeRs();
            closeConnection(conn);
        }
    }

    /**
     * Get Company available tags.
     *
     * @return ..
     */
    public List getExistingTagList(QueryBits query) throws DatabaseException {
        QueryHelper queryHelper = new QueryHelper(ContactQueries.selectCompanyExistingTagsQuery(query));

        return executeQueryReturnList(queryHelper);
    }

    public ActionMessages resetFileCount(Integer companyId) throws DatabaseException {
        QueryHelper queryHelper = new QueryHelper(ContactQueries.updateCompanyFileCountQuery());
        queryHelper.addInputInt(ObjectTypes.COMPANY);
        queryHelper.addInputInt(companyId);

        return executeProcedure(queryHelper);
    }

    /**
     * This method is used to add a new Company tag.
     *
     * @param conn
     * @return ..
     */
    private Integer addCompanyTag(Connection conn, CompanyTag companyTag) throws DatabaseException {
        QueryHelper queryHelper = new QueryHelper(ContactQueries.insertCompanyTagQuery());
        queryHelper.addOutputParam(Types.INTEGER);
        queryHelper.addInputInt(companyTag.getCompanyId());
        queryHelper.addInputStringConvertNull(companyTag.getName());
        queryHelper.addInputInt(requestContext.getUser().getId());

        queryHelper.executeProcedure(conn);

        // Put some values in the result.
        return (Integer)queryHelper.getSqlOutputs().get(0);
    }

    /**
     * This is to delete a new Company.
     *
     * @param companyId
     * @return ..
     */
    public void deleteCompanyTag(Connection conn, Integer companyId, String tagName) throws DatabaseException {
        QueryHelper queryHelper = new QueryHelper(ContactQueries.deleteCompanyTagQuery());
        queryHelper.addInputInt(companyId);
        queryHelper.addInputStringConvertNull(tagName);

        queryHelper.executeProcedure(conn);
    }

    /**
     * Add a new Company.
     *
     * @return ..
     */
    public ActionMessages add(Company company) throws DatabaseException {
        Connection conn = getConnection();

        QueryHelper queryHelper = new QueryHelper(ContactQueries.insertCompanyQuery());
        queryHelper.addOutputParam(Types.INTEGER);
        queryHelper.addInputStringConvertNull(company.getName());
        queryHelper.addInputStringConvertNull(company.getDescription());
        queryHelper.addInputInt(requestContext.getUser().getId());

        try {
            queryHelper.executeProcedure(conn);
            // Put some values in the result
            company.setId((Integer) queryHelper.getSqlOutputs().get(0));

            // Update custom fields
            if (!company.getCustomValues().isEmpty()) {
                AttributeDao attributeDao = new AttributeDao(requestContext);
                attributeDao.updateAttributeValue(conn, company.getId(), company.getCustomValues());
            }

            // Add company types
            if (company.getTypeIds() != null) {
                for (Integer companyType : company.getTypeIds()) {
                    queryHelper = new QueryHelper(AdminQueries.addAttributeValueQuery());
                    queryHelper.addInputInt(company.getId());
                    queryHelper.addInputInt(Attributes.COMPANY_TYPE);
                    queryHelper.addInputInt(companyType);
                    queryHelper.addInputStringConvertNull("");

                    queryHelper.executeProcedure(conn);
                }
            }

            // This would loop through the tags, then insert into company_tag table.
            String[] companyTags = company.getCompanyTagsCommaSeparated();
            for (String tag : companyTags) {
                if (!tag.trim().isEmpty()) {
                    CompanyTag companyTag = new CompanyTag();
                    companyTag.setName(tag.trim());
                    companyTag.setCompanyId(company.getId());
                    addCompanyTag(conn, companyTag);
                }
            }
        } catch (Exception e) {
            // Database problem
            handleError(e);

        } finally {
            closeConnection(conn);
        }
        return errors;
    }

    public ActionMessages update(Company company) throws DatabaseException {
        Connection conn = getConnection();

        QueryHelper queryHelper = new QueryHelper(ContactQueries.updateCompanyQuery());
        queryHelper.addInputInt(company.getId());
        queryHelper.addInputStringConvertNull(company.getName());
        queryHelper.addInputStringConvertNull(company.getDescription());
        queryHelper.addInputInt(requestContext.getUser().getId());

        try {
            queryHelper.executeProcedure(conn);

            // Update custom fields
            if (!company.getCustomValues().isEmpty()) {
                AttributeDao attributeDao = new AttributeDao(requestContext);
                attributeDao.updateAttributeValue(conn, company.getId(), company.getCustomValues());
            }

            // Clear existing company types
            QueryHelper typesQueryHelper = new QueryHelper(AdminQueries.updateAttributeValueQuery());
            typesQueryHelper.addInputInt(company.getId());
            typesQueryHelper.addInputInt(Attributes.COMPANY_TYPE);
            typesQueryHelper.addInputStringConvertNull(null);

            typesQueryHelper.executeProcedure(conn);

            // Add company types
            if (company.getTypeIds() != null) {
                for (Integer companyType : company.getTypeIds()) {
                    typesQueryHelper = new QueryHelper(AdminQueries.addAttributeValueQuery());
                    typesQueryHelper.addInputInt(company.getId());
                    typesQueryHelper.addInputInt(Attributes.COMPANY_TYPE);
                    typesQueryHelper.addInputInt(companyType);
                    typesQueryHelper.addInputStringConvertNull("");

                    typesQueryHelper.executeProcedure(conn);
                }
            }

            // Need to update oldTags.
            List<String> oldTags = new ArrayList();
            for (Map companyTag : getTagList(new QueryBits(), company.getId())) {
                oldTags.add(companyTag.get("tag_name").toString());
            }

            /**
             * The concept here is we have an existing list of tags,
             * and we are given another list. So, we'll do some comparison.
             *
             * If the requested tag is in oldTags list, we remove it from there.
             * After the loop, the tags left in oldTags list would be the ones we
             * want to remove.
             *
             * If the requested tag is not in oldTags list, that means it's a new
             * tag, we'll append it to addTags list.
             */
            String[] reqTags = company.getCompanyTagsCommaSeparated();
            for (String tag : reqTags) {
                tag = tag.trim();
                if (!tag.isEmpty()) {
                    if (oldTags.contains(tag)) {
                        oldTags.remove(tag);
                    } else {
                        CompanyTag companyTag = new CompanyTag();
                        companyTag.setName(tag.trim());
                        companyTag.setCompanyId(company.getId());
                        addCompanyTag(conn, companyTag);
                    }
                }
            }

            for (String oldTag : oldTags) {
                deleteCompanyTag(conn, company.getId(), oldTag);
            }
        } catch (Exception e) {
            // Database problem
            handleError(e);

        } finally {
            closeConnection(conn);
        }
        return errors;
    }

    /**
     * Deletes company.
     *
     * @return ..
     */
    public ActionMessages delete(Company company) throws DatabaseException {
        QueryHelper queryHelper = new QueryHelper(ContactQueries.deleteCompanyQuery());
        queryHelper.addInputInt(ObjectTypes.COMPANY);
        queryHelper.addInputInt(ObjectTypes.CONTACT);
        queryHelper.addInputInt(company.getId());

        return executeProcedure(queryHelper);
    }

    public ActionMessages resetNoteCount(Integer companyId) throws DatabaseException {
        QueryHelper queryHelper = new QueryHelper(ContactQueries.updateCompanyNoteCountQuery());
        queryHelper.addInputInt(companyId);

        return executeProcedure(queryHelper);
    }
}
