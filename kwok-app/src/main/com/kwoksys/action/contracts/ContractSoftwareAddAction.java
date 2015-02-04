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

import com.kwoksys.action.common.template.ActionErrorsTemplate;
import com.kwoksys.action.common.template.HeaderTemplate;
import com.kwoksys.action.common.template.StandardTemplate;
import com.kwoksys.biz.ServiceProvider;
import com.kwoksys.biz.admin.dto.AccessUser;
import com.kwoksys.biz.auth.core.Access;
import com.kwoksys.biz.contracts.ContractService;
import com.kwoksys.biz.contracts.dto.Contract;
import com.kwoksys.biz.software.SoftwareSearch;
import com.kwoksys.biz.software.SoftwareService;
import com.kwoksys.biz.software.dto.Software;
import com.kwoksys.biz.system.core.AppPaths;
import com.kwoksys.biz.system.core.Links;
import com.kwoksys.framework.connections.database.QueryBits;
import com.kwoksys.framework.struts2.Action2;
import com.kwoksys.framework.ui.Link;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Action class for associating contract software.
 */
public class ContractSoftwareAddAction extends Action2 {

    public String execute() throws Exception {
        AccessUser user = requestContext.getUser();

        ContractRelationshipForm actionForm = (ContractRelationshipForm) getBaseForm(ContractRelationshipForm.class);

        SoftwareService service = ServiceProvider.getSoftwareService(requestContext);

        ContractService contractService = ServiceProvider.getContractService(requestContext);
        Contract contract = contractService.getContract(actionForm.getContractId());

        List formattedList = new ArrayList();

        if (!actionForm.getFormSoftwareId().isEmpty()) {
            SoftwareSearch softwareSearch = new SoftwareSearch();
            softwareSearch.put("softwareIdEquals", actionForm.getFormSoftwareId());

            // Ready to pass variables to query.
            List<Software> softwareList = service.getSoftwareList(new QueryBits(softwareSearch));
            if (!softwareList.isEmpty()) {
                boolean viewSoftwareDetail = Access.hasPermission(user, AppPaths.SOFTWARE_DETAIL);

                for (Software software : softwareList) {
                    Map map = new HashMap();
                    map.put("softwareId", software.getId());
                    actionForm.setSoftwareId(software.getId());

                    Link link = new Link(requestContext);
                    link.setTitle(software.getName());
                    if (viewSoftwareDetail) {
                        link.setAjaxPath(AppPaths.SOFTWARE_DETAIL + "?softwareId=" + software.getId());
                    }
                    map.put("softwareName", link.getString());
                    formattedList.add(map);
                }
                request.setAttribute("softwareList", formattedList);
            }
        }

        //
        // Template: StandardTemplate
        //
        StandardTemplate standardTemplate = new StandardTemplate(requestContext);
        standardTemplate.setAttribute("contractId", actionForm.getContractId());
        standardTemplate.setPathAttribute("formSearchAction", AppPaths.CONTRACTS_SOFTWARE_ADD + "?contractId=" + actionForm.getContractId());
        standardTemplate.setPathAttribute("formSaveAction", AppPaths.CONTRACTS_SOFTWARE_ADD_2 + "?contractId=" + actionForm.getContractId());

        if (actionForm.getFormSoftwareId().isEmpty()) {
            standardTemplate.setAttribute("selectSoftwareMessage", "form.noSearchInput");
        } else if (formattedList.isEmpty()) {
            standardTemplate.setAttribute("selectSoftwareMessage", "form.noSearchResult");
        }

        standardTemplate.setAttribute("disableSaveButton", formattedList.isEmpty());
        standardTemplate.setAttribute("formCancelLink", Links.getCancelLink(requestContext, AppPaths.CONTRACTS_ITEMS + "?contractId=" + actionForm.getContractId()).getString());

        //
        // Template: ContractSpecTemplate
        //
        ContractSpecTemplate template = new ContractSpecTemplate(contract);
        standardTemplate.addTemplate(template);

        //
        // Template: HeaderTemplate
        //
        HeaderTemplate header = standardTemplate.getHeaderTemplate();
        header.setPageTitleKey("itMgmt.contractDetail.header", new Object[] {contract.getName()});

        //
        // Template: ActionErrorsTemplate
        //
        ActionErrorsTemplate errorsTemplate = new ActionErrorsTemplate();
        standardTemplate.addTemplate(errorsTemplate);

        return standardTemplate.findTemplate(STANDARD_TEMPLATE);
    }
}