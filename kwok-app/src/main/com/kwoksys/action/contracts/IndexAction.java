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
package com.kwoksys.action.contracts;

import com.kwoksys.action.common.template.HeaderTemplate;
import com.kwoksys.action.common.template.StandardTemplate;
import com.kwoksys.biz.ServiceProvider;
import com.kwoksys.biz.admin.dto.AccessUser;
import com.kwoksys.biz.auth.core.Access;
import com.kwoksys.biz.contracts.ContractService;
import com.kwoksys.biz.system.core.AppPaths;
import com.kwoksys.framework.connections.database.QueryBits;
import com.kwoksys.framework.properties.Localizer;
import com.kwoksys.framework.session.SessionManager;
import com.kwoksys.framework.struts2.Action2;
import com.kwoksys.framework.ui.Link;
import com.kwoksys.framework.util.NumberUtils;

import javax.servlet.http.HttpSession;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Action class of Contract index page.
 */
public class IndexAction extends Action2 {

    public String execute() throws Exception {
        AccessUser user = requestContext.getUser();

        HttpSession session = request.getSession();
        ContractService contractService = ServiceProvider.getContractService(requestContext);

        getSessionBaseForm(ContractSearchForm.class);

        // Contract list
        boolean hasContractsAccess = Access.hasPermission(user, AppPaths.CONTRACTS_LIST);
        List<Link> links = new ArrayList();

        if (hasContractsAccess) {
            // The search criteria map is not null, that means the user has performed a search.
            if (session.getAttribute(SessionManager.CONTRACT_SEARCH_CRITERIA_MAP) != null) {
                links.add(new Link(requestContext).setAppPath(AppPaths.CONTRACTS_LIST)
                        .setTitleKey("common.search.showLastSearch"));
            }

            links.add(new Link(requestContext).setAppPath(AppPaths.CONTRACTS_LIST + "?cmd=showNonExpired")
                    .setTitleKey("contracts.filter.currentContracts"));

            links.add(new Link(requestContext).setAppPath(AppPaths.CONTRACTS_LIST + "?cmd=showAll")
                    .setTitleKey("contracts.filter.allContracts"));
        }

        // Contracts summary
        List<Map> summary = contractService.getContractsSummary();
        List formattedSummary = new ArrayList();

        for (Map map : summary) {
            Map formattedMap = new HashMap();
            if (NumberUtils.replaceNull(map.get("count")) > 0) {
                formattedMap.put("text", Localizer.getText(requestContext, "contracts.search.expire_"+map.get("interval")));
                formattedMap.put("path", new Link(requestContext).setAjaxPath(AppPaths.CONTRACTS_LIST + "?cmd=groupBy&contractExpire="+map.get("interval")).setEscapeTitle(String.valueOf(map.get("count"))));
                formattedSummary.add(formattedMap);
            }
        }

        //
        // Template: StandardTemplate
        //
        StandardTemplate standardTemplate = new StandardTemplate(requestContext);
        standardTemplate.setAttribute("numContractRecords", contractService.getContractCount(new QueryBits()));
        standardTemplate.setAttribute("contractFilters", links);
        standardTemplate.setAttribute("contractsSummary", formattedSummary);

        //
        // Template: ContractSearchTemplate
        //
        ContractSearchTemplate searchTemplate = new ContractSearchTemplate();
        standardTemplate.addTemplate(searchTemplate);
        searchTemplate.setFormAction(AppPaths.CONTRACTS_LIST);

        //
        // Template: HeaderTemplate
        //
        HeaderTemplate header = standardTemplate.getHeaderTemplate();
        header.setTitleKey("core.moduleName.3");
        header.setTitleClassNoLine();
        
        if (Access.hasPermission(user, AppPaths.CONTRACTS_ADD)) {
            Link link = new Link(requestContext);
            link.setAjaxPath(AppPaths.CONTRACTS_ADD);
            link.setTitleKey("itMgmt.contractAdd.title");
            header.addHeaderCmds(link);
        }

        return standardTemplate.findTemplate(STANDARD_TEMPLATE);
    }
}