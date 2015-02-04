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

import com.kwoksys.action.common.template.*;
import com.kwoksys.biz.ServiceProvider;
import com.kwoksys.biz.admin.dto.AccessUser;
import com.kwoksys.biz.auth.core.Access;
import com.kwoksys.biz.files.FileService;
import com.kwoksys.biz.files.core.FileUtils;
import com.kwoksys.biz.files.dao.FileQueries;
import com.kwoksys.biz.files.dto.File;
import com.kwoksys.biz.software.SoftwareService;
import com.kwoksys.biz.software.SoftwareUtils;
import com.kwoksys.biz.software.dto.Software;
import com.kwoksys.biz.system.core.AppPaths;
import com.kwoksys.biz.system.core.Image;
import com.kwoksys.biz.system.core.Links;
import com.kwoksys.biz.system.core.configs.ConfigManager;
import com.kwoksys.framework.connections.database.QueryBits;
import com.kwoksys.framework.session.SessionManager;
import com.kwoksys.framework.struts2.Action2;
import com.kwoksys.framework.ui.Link;
import com.kwoksys.framework.ui.RowStyle;
import com.kwoksys.framework.util.Counter;

import java.util.*;

/**
 * Action class for displaying software file.
 */
public class SoftwareFileAction extends Action2 {

    public String execute() throws Exception {
        AccessUser accessUser = requestContext.getUser();

        // Get request parameters
        Integer softwareId= requestContext.getParameter("softwareId");
        String orderBy = SessionManager.getOrSetAttribute(requestContext, "orderBy", SessionManager.FILES_ORDER_BY, File.NAME);
        String order = SessionManager.getOrSetAttribute(requestContext, "order", SessionManager.FILES_ORDER, QueryBits.ASCENDING);

        SoftwareService softwareService = ServiceProvider.getSoftwareService(requestContext);
        Software software = softwareService.getSoftware(softwareId);

        String fileDownloadPath = AppPaths.SOFTWARE_FILE_DOWNLOAD + "?softwareId=" + softwareId + "&fileId=";
        String fileDeletePath = AppPaths.SOFTWARE_FILE_DELETE + "?softwareId=" + softwareId + "&fileId=";

        // These are for Software Files.
        boolean canDeleteFile = Access.hasPermission(accessUser, AppPaths.SOFTWARE_FILE_DELETE);
        boolean canDownloadFile = Access.hasPermission(accessUser, AppPaths.SOFTWARE_FILE_DOWNLOAD);

        QueryBits query = new QueryBits();
        if (FileUtils.isSortableColumn(orderBy)) {
            query.addSortColumn(FileQueries.getOrderByColumn(orderBy), order);
        }

        List<File> files = softwareService.getSoftwareFiles(query, softwareId);
        List dataList = new ArrayList();
        if (!files.isEmpty()) {
            RowStyle ui = new RowStyle();
            Counter counter = new Counter();

            for (File file : files) {
                List columns = new ArrayList();
                columns.add(counter.incrCounter() + ".");
                // Show a download link when the user is allowed to download the file.
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
        // Template: StandardV2Template
        //
        StandardV2Template standardTemplate = new StandardV2Template(requestContext);

        //
        // Template: HeaderTemplate
        //
        HeaderTemplate header = standardTemplate.getHeaderTemplate();
        SoftwareUtils.addSoftwareHeaderCommands(requestContext, header, softwareId);
        header.setPageTitleKey("itMgmt.softwareDetail.header", new Object[] {software.getName()});

        // Add Software File.
        FileService fileService = ServiceProvider.getFileService(requestContext);
        if (Access.hasPermission(accessUser, AppPaths.SOFTWARE_FILE_ADD)) {
            Link link = new Link(requestContext);
            link.setTitleKey("files.fileAttach");
            if (fileService.isDirectoryExist(ConfigManager.file.getSoftwareFileRepositoryLocation())) {
                link.setAjaxPath(AppPaths.SOFTWARE_FILE_ADD + "?softwareId=" + softwareId);
                link.setImgSrc(Image.getInstance().getFileAddIcon());
            } else {
                link.setImgAlt("files.warning.invalidPath");
                link.setImgSrc(Image.getInstance().getWarning());
            }
            header.addHeaderCmds(link);
        }

        //
        // Template: SoftwareSpecTemplate
        //
        SoftwareSpecTemplate tmpl = new SoftwareSpecTemplate(software);
        standardTemplate.addTemplate(tmpl);

        //
        // Template: ActionErrorsTemplate
        //
        standardTemplate.addTemplate(new ActionErrorsTemplate());

        //
        // Template: TabsTemplate
        //
        TabsTemplate tabs = new TabsTemplate();
        standardTemplate.addTemplate(tabs);
        tabs.setTabList(SoftwareUtils.softwareTabList(requestContext, software));
        tabs.setTabActive(SoftwareUtils.FILES_TAB);

        //
        // Template: TableTemplate
        //
        TableTemplate tableHeader = new TableTemplate();
        standardTemplate.addTemplate(tableHeader);
        tableHeader.setColumnPath(AppPaths.SOFTWARE_FILE + "?softwareId=" + softwareId);
        tableHeader.setColumnHeaders(columnHeaders);
        tableHeader.setColumnTextKey("files.colName.");
        tableHeader.setSortableColumnHeaders(FileUtils.getSortableColumns());
        tableHeader.setOrderBy(orderBy);
        tableHeader.setOrder(order);
        tableHeader.setDataList(dataList);
        tableHeader.setEmptyRowMsgKey("files.noAttachments");

        return standardTemplate.findTemplate(STANDARD_TEMPLATE);
    }
}
