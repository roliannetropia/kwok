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
package com.kwoksys.biz.system.core.configs;

import com.kwoksys.framework.util.NumberUtils;

import java.util.Arrays;
import java.util.List;
import java.util.Map;

/**
 * BaseConfigManager
 */
abstract class BaseConfigManager {

    private Map<String, String> configMap;

    protected BaseConfigManager() {}

    abstract void init(Map<String, String> configMap);

    void setConfigMap(Map<String, String> configMap) {
        this.configMap = configMap;
    }

    boolean getBoolean(String configKey) {
        return Boolean.valueOf(configMap.get(configKey));
    }

    String getString(String configKey) {
        return configMap.get(configKey);
    }

    int getInt(String configKey) {
        return NumberUtils.replaceNull(configMap.get(configKey));
    }

    long getLong(String configKey) {
        return Long.valueOf(configMap.get(configKey));
    }

    String[] getStringArray(String configKey) {
        return configMap.get(configKey).split(",");
    }

    List<String> getStringList(String configKey) {
        return Arrays.asList(configMap.get(configKey).split(","));
    }
}
