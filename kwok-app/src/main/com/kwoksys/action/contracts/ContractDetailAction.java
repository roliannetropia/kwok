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

import com.kwoksys.action.common.template.*;
import com.kwoksys.biz.ServiceProvider;
import com.kwoksys.biz.admin.dto.AccessUser;
import com.kwoksys.biz.auth.core.Access;
import com.kwoksys.biz.contracts.ContractService;
import com.kwoksys.biz.contracts.core.ContractUtils;
import com.kwoksys.biz.contracts.dto.Contract;
import com.kwoksys.biz.files.FileService;
import com.kwoksys.biz.files.core.FileUtils;
import com.kwoksys.biz.files.dao.FileQueries;
import com.kwoksys.biz.files.dto.File;
import com.kwoksys.biz.system.core.AppPaths;
import com.kwoksys.biz.system.core.Image;
import com.kwoksys.biz.system.core.Links;
import com.kwoksys.biz.system.core.ObjectTypes;
import com.kwoksys.biz.system.core.configs.ConfigManager;
import com.kwoksys.framework.connections.database.QueryBits;
import com.kwoksys.framework.session.SessionManager;
import com.kwoksys.framework.struts2.Action2;
import com.kwoksys.framework.ui.Link;
import com.kwoksys.framework.ui.RowStyle;
import com.kwoksys.framework.util.Counter;

import java.util.*;

/**
 * Action class for contract detail.
 */
public class ContractDetailAction extends Action2 {

    public String execute() throws Exception {
        AccessUser user = requestContext.getUser();

        Integer contractId = requestContext.getParameter("contractId");
        String orderBy = SessionManager.getOrSetAttribute(requestContext, "orderBy", SessionManager.FILES_ORDER_BY, File.NAME);
        String order = SessionManager.getOrSetAttribute(requestContext, "order", SessionManager.FILES_ORDER, QueryBits.ASCENDING);

        FileService fileService = ServiceProvider.getFileService(requestContext);
        ContractService contractService = ServiceProvider.getContractService(requestContext);
        Contract contract = contractService.getContract(contractId);

        String fileDownloadPath = AppPaths.CONTRACTS_FILE_DOWNLOAD + "?contractId=" + contractId + "&fileId=";
        String fileDeletePath = AppPaths.CONTRACTS_FILE_DELETE + "?contractId=" + contractId + "&fileId=";

        boolean canDeleteFile = Access.hasPermission(user, AppPaths.CONTRACTS_FILE_DELETE);
        boolean canDownloadFile = Access.hasPermission(user, AppPaths.CONTRACTS_FILE_DOWNLOAD);

        QueryBits query = new QueryBits();
        if (FileUtils.isSortableColumn(orderBy)) {
            query.addSortColumn(FileQueries.getOrderByColumn(orderBy), order);
        }

        List dataList = new ArrayList();

        List<File> fileList = contractService.getContractFiles(query, contractId);
        if (!fileList.isEmpty()) {
            RowStyle ui = new RowStyle();
            Counter counter = new Counter();

            for (File file : fileList) {
                List columns = new ArrayList();
                columns.add(counter.incrCounter() + ".");
                // Show a download link
                columns.add(Links.getFileIconLink(requestContext, canDownloadFile, file.getLogicalName(), fileDownloadPath + file.getId()));

                columns.add(file.getTitle());
                columns.add(file.getCreationDate());
                columns.add(FileUtils.formatFileSize(requestContext, file.getSize()));

                if (canDeleteFile) {
                    columns.add(new Link(requestContext).setAjaxPath(fileDeletePath + file.getId()).setTitleKey("form.button.delete"));
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
        // Template: StandardTemplate
        //
        StandardTemplate standardTemplate = new StandardTemplate(requestContext);

        //
        // Template: TableTemplate
        //
        TableTemplate tableHeader = new TableTemplate();
        standardTemplate.addTemplate(tableHeader);
        tableHeader.setColumnPath(AppPaths.CONTRACTS_DETAIL + "?contractId=" + contractId);
        tableHeader.setColumnHeaders(columnHeaders);
        tableHeader.setColumnTextKey("files.colName.");
        tableHeader.setSortableColumnHeaders(FileUtils.getSortableColumns());
        tableHeader.setOrderBy(orderBy);
        tableHeader.setOrder(order);
        tableHeader.setDataList(dataList);
        tableHeader.setEmptyRowMsgKey("files.noAttachments");

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

        // Edit contract link.
        if (Access.hasPermission(user, AppPaths.CONTRACTS_EDIT)) {
            Link link = new Link(requestContext);
            link.setAjaxPath(AppPaths.CONTRACTS_EDIT + "?contractId=" + contractId);
            link.setTitleKey("itMgmt.cmd.contractEdit");
            header.addHeaderCmds(link);
        }

        // Delete contract link.
        if (Access.hasPermission(user, AppPaths.CONTRACTS_DELETE)) {
            Link link = new Link(requestContext);
            link.setAjaxPath(AppPaths.CONTRACTS_DELETE + "?contractId=" + contractId);
            link.setTitleKey("itMgmt.cmd.contractDelete");
            header.addHeaderCmds(link);
        }

        if (Access.hasPermission(user, AppPaths.CONTRACTS_FILE_ADD)) {
            Link link = new Link(requestContext);
            link.setTitleKey("files.fileAttach");
            if (fileService.isDirectoryExist(ConfigManager.file.getContractFileRepositoryLocation())) {
                link.setAjaxPath(AppPaths.CONTRACTS_FILE_ADD + "?contractId=" + contractId);
                link.setImgSrc(Image.getInstance().getFileAddIcon());
            } else {
                link.setImgAlt("files.warning.invalidPath");
                link.setImgSrc(Image.getInstance().getWarning());
            }
            header.addHeaderCmds(link);
        }

        Link link = new Link(requestContext);
        link.setAjaxPath(AppPaths.CONTRACTS_LIST);
        link.setTitleKey("itMgmt.cmd.contractList");
        header.addHeaderCmds(link);

        //
        // Template: TabsTemplate
        //
        TabsTemplate tabs = new TabsTemplate();
        standardTemplate.addTemplate(tabs);
        tabs.setTabList(ContractUtils.contractTabList(requestContext, contract));
        tabs.setTabActive(ContractUtils.FILES_TAB);

        //
        // Template: CustomFieldsTemplate
        //
        CustomFieldsTemplate customFieldsTemplate = new CustomFieldsTemplate();
        standardTemplate.addTemplate(customFieldsTemplate);
        customFieldsTemplate.setObjectTypeId(ObjectTypes.CONTRACT);
        customFieldsTemplate.setObjectId(contractId);
        customFieldsTemplate.setObjectAttrTypeId(contract.getType());
        customFieldsTemplate.setShowDefaultHeader(false);

        return standardTemplate.findTemplate(STANDARD_TEMPLATE) ;
    }
}
