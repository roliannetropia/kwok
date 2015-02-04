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

import com.kwoksys.biz.contacts.dto.CompanyNote;
import com.kwoksys.biz.base.BaseDao;
import com.kwoksys.framework.http.RequestContext;
import com.kwoksys.framework.connections.database.QueryBits;
import com.kwoksys.framework.connections.database.QueryHelper;
import com.kwoksys.framework.exceptions.DatabaseException;
import com.kwoksys.framework.util.DatetimeUtils;
import com.kwoksys.framework.util.StringUtils;
import org.apache.struts.action.ActionMessages;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.Types;
import java.util.ArrayList;
import java.util.List;

/**
 * CompanyNoteDao
 */
public class CompanyNoteDao extends BaseDao {

    public CompanyNoteDao(RequestContext requestContext) {
        super(requestContext);
    }

    /**
     * Return all notes associated with a company.
     *
     * @return ..
     */
    public List<CompanyNote> getNoteList(QueryBits query, Integer companyId) throws DatabaseException {
        Connection conn = getConnection();

        QueryHelper queryHelper = new QueryHelper(ContactQueries.selectCompanyNoteQuery(query));
        queryHelper.addInputInt(companyId);

        try {
            List companyNotes = new ArrayList();
            ResultSet rs = queryHelper.executeQuery(conn);

            while (rs.next()) {
                CompanyNote companyNote = new CompanyNote(companyId);
                companyNote.setNoteId(rs.getInt("note_id"));
                companyNote.setNoteName(StringUtils.replaceNull(rs.getString("note_name")));
                companyNote.setNoteDescription(StringUtils.replaceNull(rs.getString("note_description")));
                companyNote.setNoteTypeId(rs.getInt("note_type"));
                companyNote.setCompanyId(rs.getInt("company_id"));
                companyNote.setCreationDate(DatetimeUtils.getDate(rs, "creation_date"));

                companyNotes.add(companyNote);
            }
            return companyNotes;

        } catch (Exception e) {
            // Database problem
            throw new DatabaseException(e, queryHelper);

        } finally {
            queryHelper.closeRs();
            closeConnection(conn);
        }
    }

    /**
     * Add company note.
     *
     * @return ..
     */
    public ActionMessages addNote(CompanyNote note) throws DatabaseException {
        QueryHelper queryHelper = new QueryHelper(ContactQueries.insertCompanyNoteQuery());
        queryHelper.addOutputParam(Types.INTEGER);
        queryHelper.addInputStringConvertNull(note.getNoteName());
        queryHelper.addInputStringConvertNull(note.getNoteDescription());
        queryHelper.addInputInt(note.getNoteTypeId());
        queryHelper.addInputInt(note.getCompanyId());
        queryHelper.addInputInt(requestContext.getUser().getId());

        executeProcedure(queryHelper);

        if (errors.isEmpty()) {
            note.setNoteId((Integer)queryHelper.getSqlOutputs().get(0));
        }

        return errors;
    }
}
