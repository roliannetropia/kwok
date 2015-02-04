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
package com.kwoksys.biz.admin.dao;

import com.kwoksys.biz.admin.dto.SystemConfig;
import com.kwoksys.biz.base.BaseDao;
import com.kwoksys.framework.http.RequestContext;
import com.kwoksys.framework.connections.database.QueryHelper;
import com.kwoksys.framework.exceptions.DatabaseException;
import org.apache.struts.action.ActionMessages;

import java.sql.Connection;
import java.util.List;

/**
 * AdminDao
 */
public class AdminDao extends BaseDao {

    public AdminDao(RequestContext requestContext) {
        super(requestContext);
    }

    /**
     * Gets database names.
     *
     * @return ..
     */
    public List getDatabases() throws DatabaseException {
        QueryHelper queryHelper = new QueryHelper(AdminQueries.selectDatabases());

        return executeQueryReturnList(queryHelper);
    }

    /**
     * Updates application configuration.
     *
     * @return ..
     */
    public ActionMessages updateConfig(List<SystemConfig> configList) throws DatabaseException {
        if (!configList.isEmpty()) {
            String sqlQuery = AdminQueries.updateApplicationConfigQuery();

            Connection conn = getConnection();

            try {
                for (SystemConfig systemConfig : configList) {
                    QueryHelper queryHelper = new QueryHelper(sqlQuery);
                    queryHelper.addInputString(systemConfig.getConfigKey());
                    queryHelper.addInputStringConvertNull(systemConfig.getConfigValue());

                    queryHelper.executeProcedure(conn);
                }
            } catch (Exception e) {
                // Database problem
                handleError(e);

            } finally {
                closeConnection(conn);
            }
        }
        return errors;
    }
}
