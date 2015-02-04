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

import com.kwoksys.schema.SchemaSetupParams;
import com.kwoksys.schema.SchemaUtil;
import com.kwoksys.schema.output.BaseOutput;
import com.kwoksys.schema.output.SystemOutput;

import java.io.IOException;
import java.sql.Connection;
import java.sql.SQLException;
import java.sql.Statement;

/**
 * SchemaBase
 */
public abstract class SchemaBase {

    protected static BaseOutput output = SystemOutput.getInstance();

    public abstract void create(SchemaSetupParams params) throws SQLException, IOException;

    public abstract void upgrade(SchemaSetupParams params) throws SQLException, IOException;

    /** Schema version number **/
    protected String version;

    public SchemaBase(String version) {
        this.version = version;
    }

    public String loadSqlfile(String filename) throws IOException {
        return SchemaUtil.loadSqlfile(filename);
    }

    /**
     * Creates a blank database.
     * @param stmt
     * @param params
     * @throws java.sql.SQLException
     */
    public void createDatabase(SchemaSetupParams params) throws SQLException {
        Connection conn = SchemaUtil.createConnection(params, "");
        Statement stmt = conn.createStatement();

        output.print("Creating database " + params.getDatabase() + " ... ");
        stmt.executeUpdate("CREATE DATABASE "+ params.getDatabase() + " WITH ENCODING 'UTF8'");
        output.println("done");

        stmt.close();
        closeConnection(conn);
    }

    /**
     * Preppares database for install/upgrade
     * @param stmt
     * @throws java.sql.SQLException
     */
    public void prepDatabase(Statement stmt, SchemaSetupParams params) throws SQLException {
        // Update timezone to GMT
        stmt.executeUpdate("ALTER DATABASE " + params.getDatabase() + " SET TimeZone=GMT");

        // Create plpgsql language
        try {
            output.print("Creating language 'plpgsql' ... ");
            stmt.executeUpdate("CREATE TRUSTED PROCEDURAL LANGUAGE 'plpgsql' HANDLER plpgsql_call_handler VALIDATOR plpgsql_validator");
            output.println("done");

        } catch (Exception e) {
            if (params.isDebug()) {
                output.println(e);
            } else {
                output.println("Language 'plpgsql' already exists ... done");
            }
        }
    }

    /**
     * Closes a connection.
     */
    public void closeConnection(Connection conn) {
        if (conn != null) {
            try {
                output.println("Closing connection ...\n");
                conn.close();
            } catch (Exception closeException) {/* ignored */}
        }
    }

    //
    // Getters and setters
    //
    public BaseOutput getOutput() {
        return output;
    }
    public void setOutput(BaseOutput outputs) {
        output = outputs;
    }

    public String getVersion() {
        return version;
    }
}
