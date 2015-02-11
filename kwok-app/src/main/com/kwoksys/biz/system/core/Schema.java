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
package com.kwoksys.biz.system.core;

/**
 * Schema
 */
public class Schema {

    public static class AssetHardwareTable {
        public static final int HARDWARE_NAME_MAX_LEN = 100;
        public static final int HARDWARE_MODEL_NAME_MAX_LEN = 50;
        public static final int HARDWARE_MODEL_NUMBER_MAX_LEN = 50;
        public static final int HARDWARE_SERIAL_NUMBER_MAX_LEN = 50;
    }

    public static class AssetTapeTable {
//        todo
        public static final int TAPE_NAME_MAX_LEN = 100;
//        public static final int TAPE_MODEL_NAME_MAX_LEN = 50;
//        public static final int TAPE_MODEL_NUMBER_MAX_LEN = 50;
        public static final int TAPE_SERIAL_NUMBER_MAX_LEN = 50;
        public static final int TAPE_BARCODE_NUMBER_MAX_LEN = 50;
    }

    public static class CompanyTable {
        public static final int COMPANY_NAME_MAX_LEN = 100;
    }

    public static class ContractTable {
        public static final int CONTRACT_NAME_MAX_LEN = 100;
    }

    public static class IssueTable {
        public static final int ISSUE_NAME_MAX_LEN = 120;

        // This limit is applied to the UI only. The issue description column in the database is defined as text type,
        // which supports a very large length.
        public static final int ISSUE_DESCRIPTION_MAX_LEN = 4000;
    }
}
