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
package com.kwoksys.schema.versions;

import com.kwoksys.schema.SchemaFactory;
import com.kwoksys.schema.SchemaSetupParams;
import com.kwoksys.schema.SchemaUtil;

import java.io.IOException;
import java.sql.Connection;
import java.sql.SQLException;
import java.sql.Statement;

/**
 * Schema creation class.
 */
public class Schema_2_8_x extends SchemaBase {

    private String createSql;
    private String upgradeSql;
    private String refreshSql;

    public Schema_2_8_x(String version) {
        super(version);

        createSql = "versions/postgres/v" + version + "/create-schema-base-v" + version + ".sql";
        upgradeSql = "versions/postgres/v" + version + "/upgrade-to-v" + version + ".sql";
        refreshSql = "versions/postgres/v" + version + "/refresh-views-and-procedures-v" + version + ".sql";
    }

    /**
     * Creates database schema.
     * @param params
     * @throws java.sql.SQLException
     */
    public void create(SchemaSetupParams params) throws SQLException, IOException {
        Connection conn = SchemaUtil.createConnection(params, params.getDatabase());
        Statement stmt = conn.createStatement();

        // Creates plpgsql language
        prepDatabase(stmt, params);

        // Create base schema
        createSchema(stmt);

        // Bring the schema to current version
        upgradeSchema(stmt);

        // Creating views and stored procedures
        createViewsProcedures(stmt);

        stmt.close();
        closeConnection(conn);
    }

    /**
     * Upgrades database schema.
     * @param conn
     * @param stmt
     * @param params
     * @throws java.sql.SQLException
     * @throws java.io.IOException
     */
    public void upgrade(SchemaSetupParams params) throws SQLException, IOException {
        SchemaBase previousSchema = SchemaFactory.getPreviousSchema(version);

        if (!params.getInstalledVersion().equals(previousSchema.getVersion())) {
            previousSchema.upgrade(params);
        }

        Connection conn = SchemaUtil.createConnection(params, params.getDatabase());
        Statement stmt = conn.createStatement();

        // Create tables
        upgradeSchema(stmt);

        // Creating views and stored procedures, only for the latest upgrade
        if (SchemaFactory.getPreviousSchema(SchemaFactory.getLatestSchema().getVersion()).equals(previousSchema)) {
            createViewsProcedures(stmt);
        }

        stmt.close();
        closeConnection(conn);
    }

    /**
     * Creates tables.
     * @param stmt
     * @throws java.sql.SQLException
     * @throws java.io.IOException
     */
    private void createSchema(Statement stmt) throws SQLException, IOException {
        output.print("Creating schema base ... ");
        stmt.executeUpdate(loadSqlfile(createSql));
        output.println("done");
    }

    /**
     * Creates views and stored procedures.
     * @param stmt
     * @throws java.sql.SQLException
     * @throws java.io.IOException
     */
    private void createViewsProcedures(Statement stmt) throws SQLException, IOException {
        output.print("Creating views and stored procedures ... ");
        stmt.executeUpdate(loadSqlfile(refreshSql));
        output.println("done");
    }

    /**
     * Runs upgrade statements.
     */
    private void upgradeSchema(Statement stmt) throws SQLException, IOException {
        output.print("Upgrading schema to version " + getVersion() + " ... ");
        stmt.executeUpdate(loadSqlfile(upgradeSql));
        output.println("done");
    }
}