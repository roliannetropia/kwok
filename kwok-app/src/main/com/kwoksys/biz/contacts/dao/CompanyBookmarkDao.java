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
package com.kwoksys.biz.contacts.dao;

import com.kwoksys.biz.base.BaseDao;
import com.kwoksys.biz.system.core.ObjectTypes;
import com.kwoksys.framework.http.RequestContext;
import com.kwoksys.framework.connections.database.QueryHelper;
import com.kwoksys.framework.exceptions.DatabaseException;
import org.apache.struts.action.ActionMessages;

/**
 * CompanyBookmarkDao
 */
public class CompanyBookmarkDao extends BaseDao {

    public CompanyBookmarkDao(RequestContext requestContext) {
        super(requestContext);
    }

    /**
     * Reset Company Bookmark count.
     *
     * @return ..
     */
    public ActionMessages resetCount(Integer companyId) throws DatabaseException {
        QueryHelper queryHelper = new QueryHelper(ContactQueries.updateCompanyBookmarkCountQuery());
        queryHelper.addInputInt(ObjectTypes.COMPANY);
        queryHelper.addInputInt(companyId);

        return executeProcedure(queryHelper);
    }
}
