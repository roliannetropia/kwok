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

import com.kwoksys.schema.versions.SchemaBase;
import com.kwoksys.schema.versions.Schema_2_8_x;

import java.util.*;

/**
 * SchemaFactory
 */
public class SchemaFactory {

    static Map<String, SchemaBase> versionMap = new LinkedHashMap();
    static Map<String, String> previousVersionMap = new LinkedHashMap();

    static {
        // Use this list to build the versionMap and previousVersionMap.
        String[] versions = new String[] {"2.8.9", "2.8.8", "2.8.7", "2.8.6", "2.8.5", "2.8.4", "2.8.3", "2.8.2", "2.8.1", "2.8.0"};

        // Build versionMap and previousVersionMap
        String version = null;
        for (String prevVersion : versions) {
            versionMap.put(prevVersion, new Schema_2_8_x(prevVersion));

            if (version != null) {
                previousVersionMap.put(version, prevVersion);
            }
            version = prevVersion;
        }
    }

    public static SchemaBase getSchema(String version) {
        return versionMap.get(version);
    }

    public static SchemaBase getLatestSchema() {
        return getSchema(SchemaProperties.get(SchemaProperties.SCHEMA_VERSION));
    }

    public static SchemaBase getPreviousSchema(String version) {
        return getSchema(previousVersionMap.get(version));
    }
}
