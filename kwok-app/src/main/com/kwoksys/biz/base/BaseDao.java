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
package com.kwoksys.biz.base;

import com.kwoksys.framework.http.RequestContext;
import com.kwoksys.biz.system.core.configs.LogConfigManager;
import com.kwoksys.framework.connections.database.DatabaseManager;
import com.kwoksys.framework.connections.database.QueryHelper;
import com.kwoksys.framework.exceptions.DatabaseException;
import org.apache.struts.action.ActionMessage;
import org.apache.struts.action.ActionMessages;

import java.sql.Connection;
import java.sql.ResultSet;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * BaseDao
 */
public class BaseDao {

    private static final Logger logger = Logger.getLogger(BaseDao.class.getName());

    protected RequestContext requestContext;

    private DatabaseManager databaseManager;

    protected final ActionMessages errors = new ActionMessages();

    public BaseDao(RequestContext requestContext) {
        this.requestContext = requestContext;
        databaseManager = DatabaseManager.getInstance();
    }

    public Connection getConnection() throws DatabaseException {
        return databaseManager.createConnection();
    }

    public void closeConnection(Connection conn) {
        databaseManager.closeConnection(conn, !errors.isEmpty());
    }

    public ActionMessages executeProcedure(QueryHelper queryHelper) throws DatabaseException {
        Connection conn = getConnection();

        try {
            queryHelper.executeProcedure(conn);

        } catch (Exception e) {
            // Database problem
            handleError(e);

        } finally {
            closeConnection(conn);
        }
        return errors;
    }

    public List executeQueryReturnList(QueryHelper queryHelper) throws DatabaseException {
        Connection conn = getConnection();

        try {
            return queryHelper.executeQueryReturnList(conn);

        } catch (Exception e) {
            // Database problem
            throw new DatabaseException(e, queryHelper);

        } finally {
            queryHelper.closeRs();
            closeConnection(conn);
        }
    }

    public int getRowCount(String sql) throws DatabaseException {
        Connection conn = getConnection();

        QueryHelper queryHelper = new QueryHelper(sql);

        try {
            ResultSet rs = queryHelper.executeQuery(conn);
            rs.next();
            return rs.getInt("row_count");

        } catch (Exception e) {
            // Database problem
            throw new DatabaseException(e, queryHelper);

        } finally {
            queryHelper.closeRs();
            closeConnection(conn);
        }
    }

    /**
     *
     * @param queryHelper
     * @param e
     */
    public void handleError(Exception e) {
        if (e instanceof DatabaseException && ((DatabaseException) e).getQueryHelper() != null) {
            // The error is from a subquery. Don't need to log it since it's been logged in the exception.
        } else {
            logger.log(Level.SEVERE, LogConfigManager.DATABASE_ACCESS_PREFIX + " Problem executing a query", e);
        }
        errors.add("database", new ActionMessage("common.error.database"));
    }
}
