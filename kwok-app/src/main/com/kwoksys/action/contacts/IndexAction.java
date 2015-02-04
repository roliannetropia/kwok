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
package com.kwoksys.action.contacts;

import com.kwoksys.action.common.template.HeaderTemplate;
import com.kwoksys.action.common.template.StandardTemplate;
import com.kwoksys.biz.ServiceProvider;
import com.kwoksys.biz.admin.dto.AccessUser;
import com.kwoksys.biz.auth.core.Access;
import com.kwoksys.biz.contacts.ContactService;
import com.kwoksys.biz.contacts.dao.ContactQueries;
import com.kwoksys.biz.system.core.AppPaths;
import com.kwoksys.biz.system.core.AttributeManager;
import com.kwoksys.biz.system.core.ObjectTypes;
import com.kwoksys.framework.connections.database.QueryBits;
import com.kwoksys.framework.session.SessionManager;
import com.kwoksys.framework.struts2.Action2;
import com.kwoksys.framework.ui.Link;

import javax.servlet.http.HttpSession;
import java.util.ArrayList;
import java.util.List;

/**
 * Action class contact mgmt module index page
 */
public class IndexAction extends Action2 {

    public String execute() throws Exception {
        ContactSearchForm actionForm = (ContactSearchForm) getSessionBaseForm(ContactSearchForm.class);

        AccessUser user = requestContext.getUser();

        HttpSession session = request.getSession();

        // Company search.
        List links = new ArrayList();
        String companySearchAction = "";
        if (Access.hasPermission(user, AppPaths.CONTACTS_COMPANY_LIST)) {
            if (session.getAttribute(SessionManager.COMPANY_SEARCH_CRITERIA_MAP) != null) {
                links.add(new Link(requestContext).setAppPath(AppPaths.CONTACTS_COMPANY_LIST)
                        .setTitleKey("common.search.showLastSearch").getString());
            }

            links.add(new Link(requestContext).setAppPath(AppPaths.CONTACTS_COMPANY_LIST + "?cmd=showAll")
                    .setTitleKey("contactMgmt.index.companyList").getString());

            companySearchAction = AppPaths.ROOT + AppPaths.CONTACTS_COMPANY_LIST;
        }

        // Ready to pass variables to query.
        QueryBits query = new QueryBits();
        query.addSortColumn(ContactQueries.getOrderByColumn("creation_date"), QueryBits.DESCENDING);
        query.setLimit(30, 0);

        // Contact search.
        String contactSearchAction = Access.hasPermission(user, AppPaths.CONTACTS_CONTACT_LIST) ? AppPaths.ROOT + AppPaths.CONTACTS_CONTACT_LIST : "";

        ContactService contactService = ServiceProvider.getContactService(requestContext);

        //
        // Template: StandardTemplate
        //
        StandardTemplate standardTemplate = new StandardTemplate(requestContext);
        standardTemplate.setAttribute("customFieldsOptions", new AttributeManager(requestContext).getCustomFieldOptions(ObjectTypes.COMPANY));
        standardTemplate.setAttribute("formCompanySearchAction", companySearchAction);
        standardTemplate.setAttribute("formContactSearchAction", contactSearchAction);
        standardTemplate.setAttribute("companyTagList", contactService.getExistingCompanyTags(query));
        standardTemplate.setAttribute("companyListPath", AppPaths.CONTACTS_COMPANY_LIST + "?cmd=search&companyTag=");
        standardTemplate.setAttribute("linkList", links);
        standardTemplate.setAttribute("numCompanyRecords", contactService.getCompanyCount(new QueryBits()));

        //
        // Template: HeaderTemplate
        //
        HeaderTemplate header = standardTemplate.getHeaderTemplate();
        header.setTitleKey("core.moduleName.5");
        header.setTitleClassNoLine();
        
        // Link to add company page
        if (Access.hasPermission(user, AppPaths.CONTACTS_COMPANY_ADD)) {
            Link link = new Link(requestContext);
            link.setAjaxPath(AppPaths.CONTACTS_COMPANY_ADD);
            link.setTitleKey("contactMgmt.cmd.companyAdd");
            header.addHeaderCmds(link);
        }

        return standardTemplate.findTemplate(STANDARD_TEMPLATE);
    }
}
