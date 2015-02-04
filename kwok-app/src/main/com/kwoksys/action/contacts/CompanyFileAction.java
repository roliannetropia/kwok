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
import com.kwoksys.action.common.template.StandardV2Template;
import com.kwoksys.action.common.template.TableTemplate;
import com.kwoksys.action.common.template.TabsTemplate;
import com.kwoksys.biz.ServiceProvider;
import com.kwoksys.biz.admin.dto.AccessUser;
import com.kwoksys.biz.auth.core.Access;
import com.kwoksys.biz.contacts.ContactService;
import com.kwoksys.biz.contacts.core.CompanyTabs;
import com.kwoksys.biz.contacts.core.CompanyUtils;
import com.kwoksys.biz.contacts.dto.Company;
import com.kwoksys.biz.files.FileService;
import com.kwoksys.biz.files.core.FileUtils;
import com.kwoksys.biz.files.dao.FileQueries;
import com.kwoksys.biz.files.dto.File;
import com.kwoksys.biz.system.core.AppPaths;
import com.kwoksys.biz.system.core.Image;
import com.kwoksys.biz.system.core.Links;
import com.kwoksys.biz.system.core.configs.ConfigManager;
import com.kwoksys.framework.connections.database.QueryBits;
import com.kwoksys.framework.properties.Localizer;
import com.kwoksys.framework.session.SessionManager;
import com.kwoksys.framework.struts2.Action2;
import com.kwoksys.framework.ui.Link;
import com.kwoksys.framework.ui.RowStyle;
import com.kwoksys.framework.util.Counter;

import java.util.*;

/**
 * Action class for display Company file attachments.
 */
public class CompanyFileAction extends Action2 {

    public String execute() throws Exception {
        AccessUser user = requestContext.getUser();

        // Get request parameters
        Integer companyId = requestContext.getParameter("companyId");
        String orderBy = SessionManager.getOrSetAttribute(requestContext, "orderBy", SessionManager.FILES_ORDER_BY, File.NAME);
        String order = SessionManager.getOrSetAttribute(requestContext, "order", SessionManager.FILES_ORDER, QueryBits.ASCENDING);

        ContactService contactService = ServiceProvider.getContactService(requestContext);
        Company company = contactService.getCompany(companyId);

        // These are for Company Files.
        boolean canDeleteFile = Access.hasPermission(user, AppPaths.CONTACTS_COMPANY_FILE_DELETE);
        String fileDeletePath = AppPaths.CONTACTS_COMPANY_FILE_DELETE + "?companyId=" + companyId + "&fileId=";
        String fileDeleteText = Localizer.getText(requestContext, "form.button.delete");

        boolean canDownloadFile = Access.hasPermission(user, AppPaths.CONTACTS_COMPANY_FILE_DOWNLOAD);
        String fileDownloadPath = AppPaths.CONTACTS_COMPANY_FILE_DOWNLOAD + "?companyId=" + companyId + "&fileId=";

        QueryBits query = new QueryBits();
        if (FileUtils.isSortableColumn(orderBy)) {
            query.addSortColumn(FileQueries.getOrderByColumn(orderBy), order);
        }

        List<File> files = contactService.getCompanyFiles(query, company.getId());
        List dataList = new ArrayList();

        if (!files.isEmpty()) {
            RowStyle ui = new RowStyle();
            Counter counter = new Counter();

            for (File file : files) {
                List columns = new ArrayList();
                columns.add(counter.incrCounter() + ".");
                // Show a download link
                columns.add(Links.getFileIconLink(requestContext, canDownloadFile, file.getLogicalName(),
                        fileDownloadPath + file.getId()));

                columns.add(file.getTitle());
                columns.add(file.getCreationDate());
                columns.add(FileUtils.formatFileSize(requestContext, file.getSize()));

                if (canDeleteFile) {
                    columns.add(new Link(requestContext).setAjaxPath(fileDeletePath + file.getId()).setEscapeTitle(fileDeleteText));
                }
                Map map = new HashMap();
                map.put("rowClass", ui.getRowClass());
                map.put("columns", columns);
                dataList.add(map);
            }
        }

        List columnHeaders = new ArrayList(Arrays.asList(File.ROWNUM, File.NAME, File.LABEL, File.CREATION_DATE, File.FILE_SIZE));
        if (canDeleteFile) {
            columnHeaders.add("command");
        }

        //
        // Template: StandardV2Template
        //
        StandardV2Template standardTemplate = new StandardV2Template(requestContext);

        //
        // Template: CompanySpecTemplate
        //
        CompanySpecTemplate tmpl = new CompanySpecTemplate(company);
        standardTemplate.addTemplate(tmpl);

        //
        // Template: HeaderTemplate
        //
        HeaderTemplate header = standardTemplate.getHeaderTemplate();
        header.setPageTitleKey("contactMgmt.companyDetail.header", new Object[] {company.getName()});

        //
        // Template: TabsTemplate
        //
        TabsTemplate tabs = new TabsTemplate();
        standardTemplate.addTemplate(tabs);
        tabs.setTabList(CompanyUtils.companyTabList(requestContext, company));
        tabs.setTabActive(CompanyTabs.FILES_TAB);

        //
        // Template: TableTemplate
        //
        TableTemplate tableHeader = new TableTemplate();
        standardTemplate.addTemplate(tableHeader);
        tableHeader.setColumnPath(AppPaths.CONTACTS_COMPANY_FILES + "?companyId=" + companyId);
        tableHeader.setColumnHeaders(columnHeaders);
        tableHeader.setColumnTextKey("files.colName.");
        tableHeader.setSortableColumnHeaders(FileUtils.getSortableColumns());
        tableHeader.setOrderBy(orderBy);
        tableHeader.setOrder(order);
        tableHeader.setDataList(dataList);
        tableHeader.setEmptyRowMsgKey("files.noAttachments");

        // Link to add company file page
        FileService fileService = ServiceProvider.getFileService(requestContext);
        if (Access.hasPermission(user, AppPaths.CONTACTS_COMPANY_FILE_ADD)) {
            Link link = new Link(requestContext);
            link.setTitleKey("files.fileAttach");
            if (fileService.isDirectoryExist(ConfigManager.file.getCompanyFileRepositoryLocation())) {
                link.setAjaxPath(AppPaths.CONTACTS_COMPANY_FILE_ADD + "?companyId=" + companyId);
                link.setImgSrc(Image.getInstance().getGroupIcon());
            } else {
                link.setImgAlt("files.warning.invalidPath");
                link.setImgSrc(Image.getInstance().getWarning());
            }
            header.addHeaderCmds(link);
        }

        // Link to back to company list page
        if (Access.hasPermission(user, AppPaths.CONTACTS_COMPANY_LIST)) {
            Link link = new Link(requestContext);
            link.setAjaxPath(AppPaths.CONTACTS_COMPANY_LIST);
            link.setTitleKey("contactMgmt.cmd.companyList");
            header.addHeaderCmds(link);
        }

        return standardTemplate.findTemplate(STANDARD_TEMPLATE);
    }
}
