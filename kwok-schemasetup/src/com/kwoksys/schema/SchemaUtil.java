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
package com.kwoksys.schema;

import com.kwoksys.schema.output.BaseOutput;
import com.kwoksys.schema.output.SystemOutput;

import java.io.BufferedInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.sql.*;
import java.util.Properties;

/**
 * SchemaUtil
 */
public class SchemaUtil {

    /**
     * Creates a new connection with the default database name.
     */
    public static Connection createConnection(SchemaSetupParams params, String database) throws SQLException {
        BaseOutput output = SystemOutput.getInstance();

        // Put all params in a property objects, making it easier to read.
        Properties prop = new Properties();
        prop.put("user", params.getUsername());
        prop.put("password", params.getPassword());

        output.print("Connecting to PostgreSQL Server ...");

        if (!database.isEmpty()) {
            output.print(", database: " + database + ", ...");
        }

        Connection conn = DriverManager.getConnection("jdbc:postgresql://" + params.getHost() + ":" + params.getPort() + "/" + database, prop);
        DatabaseMetaData meta = conn.getMetaData();

        output.println(" Postgres version " + meta.getDatabaseProductVersion() + " (supports 8.2.x and above) ... done");
        return conn;
    }

    public static String loadSqlfile(String filename) throws IOException {
        InputStream in = SchemaUtil.class.getResourceAsStream(filename);
        BufferedInputStream stream = new BufferedInputStream(in);

        byte[] b = new byte[stream.available()];
        stream.read(b);
        stream.close();
        return new String(b);
    }

    public static String checkDatabaseVersion(Statement stmt) throws SQLException {
        String schemaVersion = "";

        stmt.execute("select config_value from system_config where config_key='schema.version'");
        ResultSet result = stmt.getResultSet();
        if (result.next()) {
            schemaVersion = result.getString("config_value");
        }

        return schemaVersion;
    }
}
