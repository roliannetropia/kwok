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
package com.kwoksys.biz.reports.dao;

import com.kwoksys.biz.reports.dto.SoftwareUsage;
import com.kwoksys.biz.base.BaseDao;
import com.kwoksys.framework.http.RequestContext;
import com.kwoksys.framework.connections.database.QueryBits;
import com.kwoksys.framework.connections.database.QueryHelper;
import com.kwoksys.framework.exceptions.DatabaseException;
import com.kwoksys.framework.util.StringUtils;

import java.sql.Connection;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * ReportDao class.
 */
public class ReportDao extends BaseDao {

    public ReportDao(RequestContext requestContext) {
        super(requestContext);
    }

    public List<SoftwareUsage> getSoftwareUsage(QueryBits query) throws DatabaseException {
        Connection conn = getConnection();

        QueryHelper queryHelper = new QueryHelper(ReportQueries.selectSoftwareUsageQuery(query));

        try {
            List list = new ArrayList();
            ResultSet rs = queryHelper.executeQuery(conn);

            while (rs.next()) {
                SoftwareUsage softwareUsage = new SoftwareUsage();
                softwareUsage.setId(rs.getInt("software_id"));
                softwareUsage.setName(rs.getString("software_name"));
                softwareUsage.setDescription(StringUtils.replaceNull(rs.getString("software_description")));
                softwareUsage.setTypeName(StringUtils.replaceNull(rs.getString("software_type_name")));
                softwareUsage.setHardwareId(rs.getInt("hardware_id"));
                softwareUsage.setHardwareName(StringUtils.replaceNull(rs.getString("hardware_name")));
                softwareUsage.setHardwareOwnerId(rs.getInt("hardware_owner_id"));
                softwareUsage.setHardwareOwnerName(StringUtils.replaceNull(rs.getString("hardware_owner_display_name")));
                list.add(softwareUsage);
            }
            return list;

        } catch (Exception e) {
            // Database problem
            throw new DatabaseException(e, queryHelper);

        } finally {
            queryHelper.closeRs();
            closeConnection(conn);
        }
    }

    public int getSoftwareUsageCount(QueryBits query) throws DatabaseException {
        return getRowCount(ReportQueries.selectSoftwareUsageCountQuery(query));
    }

    public List<Map<String, String>> getHardwareMembers(QueryBits searchQuery, QueryBits query) throws DatabaseException {
        QueryHelper queryHelper = new QueryHelper(ReportQueries.selectHardwareMembersQuery(searchQuery, query));

        return executeQueryReturnList(queryHelper);
    }

    public int getHardwareMembersCount(QueryBits query) throws DatabaseException {
        return getRowCount(ReportQueries.selectHardwareMembersCountQuery(query));
    }

    public int getHardwareLicenseCount(QueryBits query) throws DatabaseException {
        return getRowCount(ReportQueries.getHardwareLicenseCountQuery(query));
    }

    public List<Map<String, String>> getHardwareLicenses(QueryBits query) throws DatabaseException {
        QueryHelper queryHelper = new QueryHelper(ReportQueries.getHardwareLicensesQuery(query));

        return executeQueryReturnList(queryHelper);
    }
}