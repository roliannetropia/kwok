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
package com.kwoksys.framework.csv;

import au.com.bytecode.opencsv.CSVReader;

import java.io.*;
import java.util.List;

/**
 * CsvReader
 */
public class CsvReader {

    private List list;

    public CsvReader(File file) throws Exception {
        BufferedReader bufferedReader = new BufferedReader(new FileReader(file));
        CSVReader csvReader = new CSVReader(bufferedReader);
        try {
            list = csvReader.readAll();
        } catch (Exception e) {
            //
        }
        csvReader.close();
    }

    public List<String[]> getData() throws IOException {
        return list;
    }

    public void cleanup() {
        list = null;
    }
}
