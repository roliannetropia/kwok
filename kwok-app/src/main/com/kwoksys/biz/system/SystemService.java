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
package com.kwoksys.biz.system;

import com.kwoksys.biz.system.dao.SystemDao;
import com.kwoksys.biz.system.dto.SystemInfo;
import com.kwoksys.biz.system.dto.linking.ObjectLink;
import com.kwoksys.framework.properties.AppProperties;
import com.kwoksys.framework.exceptions.DatabaseException;
import com.kwoksys.framework.http.RequestContext;
import org.apache.struts.action.ActionMessages;

import java.util.List;
import java.util.Map;

/**
 * SystemService
 */
public class SystemService {

    private RequestContext requestContext;

    public SystemService(RequestContext requestContext) {
        this.requestContext = requestContext;
    }

    public String getVersion() {
        return AppProperties.get(AppProperties.APP_VERSION_KEY);
    }

    public String getBuildNumber() {
        return AppProperties.get(AppProperties.APP_VERSION_KEY) + "." + AppProperties.get(AppProperties.BUILD_DATE);
    }

    public Map getDatabaseInfo() throws DatabaseException {
        return new SystemDao(requestContext).getDatabaseInfo();
    }

    public SystemInfo getSystemInfo() throws DatabaseException {
        return new SystemDao(requestContext).getSystemInfo();
    }

    public Map getSystemConfig() throws DatabaseException {
        return new SystemDao(requestContext).getSystemConfig();
    }

    public List<String> getFlushSystemCacheKeys(Long cacheTime) throws DatabaseException {
        return new SystemDao(requestContext).getFlushSystemCacheKeys(cacheTime);
    }

    public ActionMessages validateSystemCaches(Long cacheTime) throws DatabaseException {
        return new SystemDao(requestContext).validateSystemCaches(cacheTime);
    }

    public ActionMessages resetSystemCache(String cacheKey) throws DatabaseException {
        return new SystemDao(requestContext).resetSystemCache(cacheKey, requestContext.getSysdate().getTime());
    }

    public int getObjectMapCount(List objectTypeIds, Integer linkedObjectId, List linkedObjectTypeIds) throws DatabaseException {
        return new SystemDao(requestContext).getObjectMapCount(objectTypeIds, linkedObjectId, linkedObjectTypeIds);
    }

    public int getLinkedObjectMapCount(List linkedObjectTypeIds, Integer objectId, Integer objectTypeId) throws DatabaseException {
        return new SystemDao(requestContext).getLinkedObjectMapCount(linkedObjectTypeIds, objectId, objectTypeId);
    }

    /**
     * This adds a object mapping.
     * @return
     * @throws DatabaseException
     */
    public ActionMessages addObjectMapping(ObjectLink objectMap) throws DatabaseException {
        return new SystemDao(requestContext).addObjectMapping(objectMap);
    }

    /**
     * This deletes a object mapping.
     * @return
     * @throws DatabaseException
     */
    public ActionMessages deleteObjectMapping(ObjectLink objectMap) throws DatabaseException {
        return new SystemDao(requestContext).deleteObjectMapping(objectMap);
    }
}
