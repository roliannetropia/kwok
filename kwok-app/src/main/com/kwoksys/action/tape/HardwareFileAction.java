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
package com.kwoksys.action.tape;

import com.kwoksys.action.common.template.HeaderTemplate;
import com.kwoksys.action.common.template.StandardV2Template;
import com.kwoksys.action.common.template.TableTemplate;
import com.kwoksys.action.common.template.TabsTemplate;
import com.kwoksys.biz.ServiceProvider;
import com.kwoksys.biz.admin.dto.AccessUser;
import com.kwoksys.biz.auth.core.Access;
import com.kwoksys.biz.files.FileService;
import com.kwoksys.biz.files.core.FileUtils;
import com.kwoksys.biz.files.dao.FileQueries;
import com.kwoksys.biz.files.dto.File;
import com.kwoksys.biz.hardware.HardwareService;
import com.kwoksys.biz.hardware.core.HardwareUtils;
import com.kwoksys.biz.hardware.dto.Hardware;
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
 * Action class for showing hardware files.
 */
public class HardwareFileAction extends Action2 {

    public String execute() throws Exception {
        AccessUser user = requestContext.getUser();

        // Get request parameters
        Integer hardwareId = requestContext.getParameter("hardwareId");
        String orderBy = SessionManager.getOrSetAttribute(requestContext, "orderBy", SessionManager.FILES_ORDER_BY, File.NAME);
        String order = SessionManager.getOrSetAttribute(requestContext, "order", SessionManager.FILES_ORDER, QueryBits.ASCENDING);

        HardwareService hardwareService = ServiceProvider.getHardwareService(requestContext);
        Hardware hardware = hardwareService.getHardware(hardwareId);

        String fileDownloadPath = AppPaths.HARDWARE_FILE_DOWNLOAD + "?hardwareId=" + hardwareId + "&fileId=" ;
        String fileDeletePath = AppPaths.HARDWARE_FILE_DELETE + "?hardwareId=" + hardwareId + "&fileId=";

        // These are for Hardware Files.
        boolean canDeleteFile = Access.hasPermission(user, AppPaths.HARDWARE_FILE_DELETE);
        boolean canDownloadFile = Access.hasPermission(user, AppPaths.HARDWARE_FILE_DOWNLOAD);

        QueryBits query = new QueryBits();
        if (FileUtils.isSortableColumn(orderBy)) {
            query.addSortColumn(FileQueries.getOrderByColumn(orderBy), order);
        }

        List dataList = new ArrayList();

        List<File> fileList = hardwareService.getHardwareFiles(query, hardware.getId());
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
        // Template: StandardV2Template
        //
        StandardV2Template standardTemplate = new StandardV2Template(requestContext);

        //
        // Template: HeaderTemplate
        //
        HeaderTemplate headerTemplate = standardTemplate.getHeaderTemplate();
        headerTemplate.setPageTitleKey("itMgmt.hardwareDetail.header", new Object[] {hardware.getName()});
        HardwareUtils.addHardwareHeaderCommands(requestContext, headerTemplate, hardwareId);

        // Add Hardware File.
        FileService fileService = ServiceProvider.getFileService(requestContext);
        if (Access.hasPermission(user, AppPaths.HARDWARE_FILE_ADD)) {
            Link link = new Link(requestContext);
            link.setTitleKey("files.fileAttach");
            if (fileService.isDirectoryExist(ConfigManager.file.getHardwareFileRepositoryLocation())) {
                link.setAjaxPath(AppPaths.HARDWARE_FILE_ADD + "?hardwareId=" + hardwareId);
                link.setImgSrc(Image.getInstance().getFileAddIcon());
            } else {
                link.setImgAlt("files.warning.invalidPath");
                link.setImgSrc(Image.getInstance().getWarning());
            }
            headerTemplate.addHeaderCmds(link);
        }

        //
        // Template: HardwareSpecTemplate
        //
        HardwareSpecTemplate tmpl = new HardwareSpecTemplate(hardware);
        standardTemplate.addTemplate(tmpl);

        //
        // Template: TabsTemplate
        //
        TabsTemplate tabs = new TabsTemplate();
        standardTemplate.addTemplate(tabs);
        tabs.setTabList(HardwareUtils.hardwareTabList(hardware, requestContext));
        tabs.setTabActive(HardwareUtils.HARDWARE_FILE_TAB);

        //
        // Template: TableTemplate
        //
        TableTemplate tableHeader = new TableTemplate();
        standardTemplate.addTemplate(tableHeader);
        tableHeader.setColumnPath(AppPaths.HARDWARE_FILE + "?hardwareId=" + hardwareId);
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