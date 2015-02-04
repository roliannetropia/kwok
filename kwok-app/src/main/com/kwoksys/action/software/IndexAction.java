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
package com.kwoksys.action.software;

import com.kwoksys.action.common.template.HeaderTemplate;
import com.kwoksys.action.common.template.StandardTemplate;
import com.kwoksys.biz.ServiceProvider;
import com.kwoksys.biz.admin.dto.AccessUser;
import com.kwoksys.biz.auth.core.Access;
import com.kwoksys.biz.software.SoftwareService;
import com.kwoksys.biz.system.core.AppPaths;
import com.kwoksys.framework.connections.database.QueryBits;
import com.kwoksys.framework.properties.Localizer;
import com.kwoksys.framework.session.SessionManager;
import com.kwoksys.framework.struts2.Action2;
import com.kwoksys.framework.ui.Link;

import javax.servlet.http.HttpSession;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Action class for software index page.
 */
public class IndexAction extends Action2 {

    public String execute() throws Exception {
        AccessUser user = requestContext.getUser();
        HttpSession session = request.getSession();

        getSessionBaseForm(SoftwareSearchForm.class);

        // Link to software list.
        List links = new ArrayList();

        if (Access.hasPermission(user, AppPaths.SOFTWARE_LIST)) {
            Map linkMap;

            if (session.getAttribute(SessionManager.SOFTWARE_SEARCH_CRITERIA_MAP) != null) {
                linkMap = new HashMap();
                linkMap.put("urlPath", AppPaths.SOFTWARE_LIST);
                linkMap.put("urlText", Localizer.getText(requestContext, "common.search.showLastSearch"));
                links.add(linkMap);
            }

            linkMap = new HashMap();
            linkMap.put("urlPath", AppPaths.SOFTWARE_LIST + "?cmd=showAll");
            linkMap.put("urlText", Localizer.getText(requestContext, "itMgmt.index.showAllSoftware"));
            links.add(linkMap);
        }

        SoftwareService softwareService = ServiceProvider.getSoftwareService(requestContext);

        // Get the number of records.
        int numSoftwareRecords = softwareService.getSoftwareCount(new QueryBits());

        //
        // Template: StandardTemplate
        //
        StandardTemplate standardTemplate = new StandardTemplate(requestContext);
        standardTemplate.setAttribute("numSoftwareRecords", numSoftwareRecords);
        standardTemplate.setAttribute("linkList", links);

        //
        // Template: SoftwareSearchTemplate
        //
        SoftwareSearchTemplate searchTemplate = new SoftwareSearchTemplate();
        standardTemplate.addTemplate(searchTemplate);
        searchTemplate.setFormAction(AppPaths.SOFTWARE_LIST);

        //
        // Template: HeaderTemplate
        //
        HeaderTemplate header = standardTemplate.getHeaderTemplate();
        header.setTitleKey("core.moduleName.2");
        header.setTitleClassNoLine();

        if (Access.hasPermission(user, AppPaths.SOFTWARE_ADD)) {
            Link link = new Link(requestContext);
            link.setAjaxPath(AppPaths.SOFTWARE_ADD);
            link.setTitleKey("itMgmt.cmd.softwareAdd");
            header.addHeaderCmds(link);
        }

        return standardTemplate.findTemplate(STANDARD_TEMPLATE);
    }
}
