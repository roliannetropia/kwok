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
package com.kwoksys.framework.connections.database;

import com.kwoksys.biz.system.core.configs.LogConfigManager;
import com.kwoksys.framework.exceptions.DatabaseException;

import java.sql.*;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.logging.Logger;

/**
 * QueryManager
 */
public class QueryHelper {

    private static final Logger logger = Logger.getLogger(QueryHelper.class.getName());

    private static final String IN_PARAM = "in";
    private static final String OUT_PARAM = "out";

    private List<Object[]> sqlParams = new ArrayList();
    private List<Object> sqlOutputs = new ArrayList();

    private String sqlQuery;
    private ResultSet rs;
    private PreparedStatement pstmt;

    public QueryHelper(String sqlQuery) {
        this.sqlQuery = sqlQuery;
    }

    public void addInputStringConvertNull(String value) {
        if (value == null || value.isEmpty()) {
            sqlParams.add(new Object[]{IN_PARAM, Types.NULL, Types.VARCHAR});
        } else {
            addInputString(value);
        }
    }

    public void addInputIntegerConvertNull(Integer value) {
        if (value == null || value.equals(0)) {
            sqlParams.add(new Object[]{IN_PARAM, Types.NULL, Types.INTEGER});
        } else {
            addInputInt(value);
        }
    }

    public void addInputDoubleConvertNull(Double value) {
        if (value == null || value.equals(0.0)) {
            sqlParams.add(new Object[]{IN_PARAM, Types.NULL, Types.DOUBLE});
        } else {
            addInputDouble(value);
        }
    }

    public void addInputString(String value) {
        sqlParams.add(new Object[]{IN_PARAM, Types.VARCHAR, value});
    }

    public void addInputInt(Integer value) {
        sqlParams.add(new Object[]{IN_PARAM, Types.INTEGER, value});
    }

    public void addInputLong(Long value) {
        sqlParams.add(new Object[]{IN_PARAM, Types.BIGINT, value});
    }

    public void addInputDouble(Double value) {
        sqlParams.add(new Object[]{IN_PARAM, Types.DOUBLE, value});
    }

    public void addOutputParam(int keyPos) {
        //cstmt.registerOutParameter(1, Types.INTEGER);
        // we only need Types.INTEGER, order is populated automatically
        sqlParams.add(new Object[]{OUT_PARAM, keyPos, keyPos});
    }

    public String concatSqlParams() {
        StringBuilder output = new StringBuilder();
        for (Object[] param : sqlParams) {
            output.append("[").append(param[2]).append("] ");
        }
        return output.toString();
    }

    /**
     * To be deprecated
     * @param conn
     * @param sqlQuery
     * @return
     * @throws SQLException
     */
    public List executeQueryReturnList(Connection conn) throws SQLException {
        List result = new ArrayList();

        rs = executeQuery(conn);

        ResultSetMetaData rsm = rs.getMetaData();
        while (rs.next()) {
            // Add the current column to the temp list
            Map map = new HashMap();
            for (int counter = 1; counter <= rsm.getColumnCount(); counter++) {
                // Add each column to the map
                map.put(rsm.getColumnName(counter), rs.getString(counter));
            }
            result.add(map);
        }
        return result;
    }

    /**
     * Returns a resultSet.
     * @param conn
     * @param sqlQuery
     * @return
     * @throws java.sql.SQLException
     */
    public ResultSet executeQuery(Connection conn) throws SQLException {
        // Log this query.
        logger.log(LogConfigManager.getLogLevel(LogConfigManager.DATABASE_ACCESS_PREFIX), LogConfigManager.DATABASE_ACCESS_PREFIX + " Executing query: " + sqlQuery + "; params: " + concatSqlParams());

        pstmt = conn.prepareStatement(sqlQuery);

        // Set parameters
        int i = 1;
        for (Object[] param : sqlParams) {
            int dataType = (Integer)param[1];
            Object dataValue = param[2];

            if (dataType == Types.INTEGER) {
                pstmt.setInt(i++, (Integer) dataValue);

            } else if (dataType == Types.BIGINT) {
                pstmt.setLong(i++, (Long) dataValue);

            } else if (dataType == Types.VARCHAR) {
                pstmt.setString(i++, (String) dataValue);
            }
        }

        // Execute callable statement
        rs = pstmt.executeQuery();
        return rs;
    }

    /**
     * Returns a CallableStmt.
     * @param conn
     * @param sqlQuery
     * @throws java.sql.SQLException
     */
    public void executeProcedure(Connection conn) throws DatabaseException {
        // Log this query.
        logger.log(LogConfigManager.getLogLevel(LogConfigManager.DATABASE_ACCESS_PREFIX), LogConfigManager.DATABASE_ACCESS_PREFIX + " Executing query: " + sqlQuery + "; params: " + concatSqlParams());

        CallableStatement cstmt = null;
        try {
            cstmt = conn.prepareCall(sqlQuery);

            int i = 1;
            for (Object[] param : sqlParams) {
                Object paramType = param[0];
                int dataType = (Integer)param[1];
                Object dataValue = param[2];

                if (paramType.equals(IN_PARAM)) {
                    if (dataType == Types.NULL) {
                        cstmt.setNull(i++, (Integer)dataValue);

                    } else if (dataType == Types.INTEGER) {
                        cstmt.setInt(i++, (Integer) dataValue);

                    } else if (dataType == Types.BIGINT) {
                        cstmt.setLong(i++, (Long) dataValue);

                    } else if (dataType == Types.VARCHAR) {
                        cstmt.setString(i++, (String) dataValue);

                    } else if (dataType == Types.DOUBLE) {
                        cstmt.setDouble(i++, (Double) dataValue);

                    } else if (dataType == Types.TIMESTAMP) {
                        cstmt.setTimestamp(i++, (Timestamp) dataValue);
                    }
                } else if (paramType.equals(OUT_PARAM)) {
                    cstmt.registerOutParameter(i++, dataType);
                }
            }

            // Execute callable statement
            cstmt.execute();

            i = 1;
            for (Object[] param : sqlParams) {
                Object paramType = param[0];
                int dataType = (Integer)param[1];

                if (paramType.equals(OUT_PARAM)) {
                    switch (dataType) {
                        case Types.INTEGER:
                            sqlOutputs.add(cstmt.getInt(i++));
                            break;
                    }
                }
                i++;
            }
        } catch (Exception e) {
            // Database problem
            throw new DatabaseException(e, this);
        } finally {
            if (pstmt != null) {
                try {
                    pstmt.close();
                    pstmt = null;
                } catch (Exception e) { /* ignored */ }
            }
            if (cstmt != null) {
                try {
                    cstmt.close();
                } catch (Exception e) { /* ignored */ }
            }
        }
    }

    /**
     * Closes all resources created related to query execution
     */
    public void closeRs() {
        try {
            rs.close();
        } catch (Exception e) { /* ignored */ }
    }

    public String getSqlQuery() {
        return sqlQuery;
    }

    public List<Object> getSqlOutputs() {
        return sqlOutputs;
    }
}