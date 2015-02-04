/*
 * ====================================================================
 * Copyright 2005-2014 Wai-Lun Kwok
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
package com.kwoksys.schema.commands;

import com.kwoksys.schema.SchemaFactory;
import com.kwoksys.schema.SchemaSetupParams;
import com.kwoksys.schema.SchemaUtil;
import com.kwoksys.schema.output.BaseOutput;
import com.kwoksys.schema.versions.SchemaBase;

import java.sql.Connection;
import java.sql.Statement;

/**
 * SchemaSetupCommand
 */
public class SchemaSetupCommand {

    public void execute(BaseOutput out, SchemaSetupParams params) throws Exception {
        SchemaBase schema = SchemaFactory.getLatestSchema();

        if (params.getInstallType().equals(SchemaSetupParams.CREATE)) {
            out.println("Preparing to install version " + schema.getVersion() + " ... ");

            // Create database
            schema.createDatabase(params);

            // Create schema
            schema.create(params);

        } else if (params.getInstallType().equals(SchemaSetupParams.UPGRADE)) {
            // Connecting to existing repository
            Connection conn = SchemaUtil.createConnection(params, params.getDatabase());
            Statement stmt = conn.createStatement();

            // Get and set installed version
            params.setInstalledVersion(SchemaUtil.checkDatabaseVersion(stmt));

            // Checks installed version
            if (params.getInstalledVersion().equals(schema.getVersion())) {
                out.println("Schema has the lastest version " + params.getInstalledVersion() + ", no upgrade required");
                return;

            } else if (SchemaFactory.getSchema(params.getInstalledVersion()) == null) {
                throw new Exception("Upgrade from your installed version " + params.getInstalledVersion() + " is not supported");
            }

            out.println("Preparing to upgrade from version " + params.getInstalledVersion() + " to version " +
                    schema.getVersion() + " ... ");

            schema.prepDatabase(stmt, params);

            stmt.close();
            schema.closeConnection(conn);

            schema.upgrade(params);
        }
        out.println("Schema setup completed successfully");
    }
}
