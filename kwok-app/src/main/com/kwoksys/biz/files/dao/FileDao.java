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
package com.kwoksys.biz.files.dao;

import com.kwoksys.biz.files.dto.File;
import com.kwoksys.biz.base.BaseDao;
import com.kwoksys.framework.http.RequestContext;
import com.kwoksys.framework.connections.database.QueryBits;
import com.kwoksys.framework.connections.database.QueryHelper;
import com.kwoksys.framework.exceptions.DatabaseException;
import com.kwoksys.framework.exceptions.ObjectNotFoundException;
import com.kwoksys.framework.util.StringUtils;
import com.kwoksys.framework.util.DatetimeUtils;
import org.apache.struts.action.ActionMessages;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

/**
 * File
 * Format: get_, add_, update_, delete, _Service
 */
public class FileDao extends BaseDao {

    public FileDao(RequestContext requestContext) {
        super(requestContext);
    }

    /**
     * Return all Files for a particular object.
     */
    public List getList(QueryBits query, Integer objectTypeId, Integer objectId) throws DatabaseException {
        Connection conn = getConnection();

        QueryHelper queryHelper = new QueryHelper(FileQueries.selectFileListQuery(query));
        queryHelper.addInputInt(objectTypeId);
        queryHelper.addInputInt(objectId);

        try {
            List files = new ArrayList();
            ResultSet rs = queryHelper.executeQuery(conn);

            while (rs.next()) {
                File file = new File();
                file.setId(rs.getInt("file_id"));
                file.setLogicalName(StringUtils.replaceNull(rs.getString("file_name")));
                file.setTitle(StringUtils.replaceNull(rs.getString("file_friendly_name")));
                file.setDescription(StringUtils.replaceNull(rs.getString("file_description")));
                file.setMimeType(StringUtils.replaceNull(rs.getString("file_mime_type")));
                file.setSize(rs.getInt("file_byte_size"));
                file.setFileuploadedFileName(StringUtils.replaceNull(rs.getString("file_physical_name")));
                file.setCreationDate(DatetimeUtils.getDate(rs, "creation_date"));

                files.add(file);
            }
            return files;

        } catch (Exception e) {
            // Database problem
            throw new DatabaseException(e, queryHelper);

        } finally {
            queryHelper.closeRs();
            closeConnection(conn);
        }
    }

    /**
     * Return details of a File for a particular object.
     */
    public File getFile(Integer objectTypeId, Integer objectId, Integer fileId) throws DatabaseException,
            ObjectNotFoundException {

        Connection conn = getConnection();

        QueryHelper queryHelper = new QueryHelper(FileQueries.selectFileDetailQuery());
        queryHelper.addInputInt(objectTypeId);
        queryHelper.addInputInt(objectId);
        queryHelper.addInputInt(fileId);

        try {
            ResultSet rs = queryHelper.executeQuery(conn);

            if (rs.next()) {
                File file = new File();
                file.setObjectTypeId(objectTypeId);
                file.setObjectId(objectId);
                file.setId(rs.getInt("file_id"));
                file.setLogicalName(StringUtils.replaceNull(rs.getString("file_name")));
                file.setTitle(StringUtils.replaceNull(rs.getString("file_friendly_name")));
                file.setDescription(StringUtils.replaceNull(rs.getString("file_description")));
                file.setMimeType(StringUtils.replaceNull(rs.getString("file_mime_type")));
                file.setSize(rs.getInt("file_byte_size"));
                file.setFileuploadedFileName(StringUtils.replaceNull(rs.getString("file_physical_name")));

                return file;
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
     * This method is to add a File.
     *
     * @param file
     * @return ..
     */
    public ActionMessages add(File file) throws DatabaseException {
        QueryHelper queryHelper = new QueryHelper(FileQueries.insertNewFileQuery());
        queryHelper.addOutputParam(Types.INTEGER);
        queryHelper.addInputStringConvertNull(file.getTempFileName());
        queryHelper.addInputInt(requestContext.getUser().getId());

        executeProcedure(queryHelper);

        // Put some values in the result.
        if (errors.isEmpty()) {
            file.setId((Integer)queryHelper.getSqlOutputs().get(0));
        }

        return errors;
    }

    /**
     * This method is used to edit details of a File.
     *
     * @param file
     * @return ..
     */
    public ActionMessages update(File file) throws DatabaseException {
        QueryHelper queryHelper = new QueryHelper(FileQueries.updateNewFileDetailQuery());
        queryHelper.addInputInt(file.getId());
        queryHelper.addInputStringConvertNull(file.getLogicalName());
        queryHelper.addInputStringConvertNull(file.getTitle());
        queryHelper.addInputStringConvertNull(file.getDescription());
        queryHelper.addInputStringConvertNull(file.getMimeType());
        queryHelper.addInputInt(file.getSize());
        queryHelper.addInputStringConvertNull(file.getPhysicalName());
        queryHelper.addInputInt(file.getObjectTypeId());
        queryHelper.addInputInt(file.getObjectId());
        queryHelper.addInputInt(requestContext.getUser().getId());

        return executeProcedure(queryHelper);
    }

    /**
     * Delete a dummy File, probably created during failed file upload.
     *
     * @return ..
     */
    public ActionMessages deleteNew(Integer fileId) throws DatabaseException {
        QueryHelper queryHelper = new QueryHelper(FileQueries.deleteNewfileQuery());
        queryHelper.addInputInt(fileId);

        return executeProcedure(queryHelper);
    }

    /**
     * Delete a File.
     *
     * @return ..
     */
    public ActionMessages delete(File file) throws DatabaseException {
        QueryHelper queryHelper = new QueryHelper(FileQueries.deleteFileQuery());
        queryHelper.addInputInt(file.getObjectTypeId());
        queryHelper.addInputInt(file.getObjectId());
        queryHelper.addInputInt(file.getId());

        return executeProcedure(queryHelper);
    }
}
