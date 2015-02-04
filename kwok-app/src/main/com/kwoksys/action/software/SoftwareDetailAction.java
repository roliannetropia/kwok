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
import com.kwoksys.biz.admin.AdminService;
import com.kwoksys.biz.admin.dto.AccessUser;
import com.kwoksys.biz.auth.core.Access;
import com.kwoksys.biz.software.SoftwareService;
import com.kwoksys.biz.software.SoftwareUtils;
import com.kwoksys.biz.software.dao.SoftwareQueries;
import com.kwoksys.biz.software.dto.Software;
import com.kwoksys.biz.software.dto.SoftwareLicense;
import com.kwoksys.biz.system.core.AppPaths;
import com.kwoksys.biz.system.core.Image;
import com.kwoksys.biz.system.core.Links;
import com.kwoksys.biz.system.core.ObjectTypes;
import com.kwoksys.biz.system.core.configs.ConfigManager;
import com.kwoksys.framework.connections.database.QueryBits;
import com.kwoksys.framework.properties.Localizer;
import com.kwoksys.framework.struts2.Action2;
import com.kwoksys.framework.ui.Link;
import com.kwoksys.framework.ui.RowStyle;
import com.kwoksys.framework.util.Counter;
import com.kwoksys.framework.util.HtmlUtils;
import com.kwoksys.framework.util.NumberUtils;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Action class for showing software detail.
 */
public class SoftwareDetailAction extends Action2 {

    public String execute() throws Exception {
        AccessUser accessUser = requestContext.getUser();

        SoftwareLicenseForm actionForm = (SoftwareLicenseForm) getBaseForm(SoftwareLicenseForm.class);
        Integer softwareId = actionForm.getSoftwareId();

        SoftwareService softwareService = ServiceProvider.getSoftwareService(requestContext);
        Software software = softwareService.getSoftware(softwareId);

        RowStyle ui = new RowStyle();

        // For adding a license.
        String cmd = actionForm.getCmd();
        String formAction = AppPaths.SOFTWARE_LICENSE_DELETE_2;
        Integer formLicenseId = 0;
        String rowClass = "";
        boolean canEditLicense = Access.hasPermission(accessUser, AppPaths.SOFTWARE_LICENSE_EDIT_2);
        boolean canDeleteLicense = Access.hasPermission(accessUser, AppPaths.SOFTWARE_LICENSE_DELETE_2);
        boolean hasHardwareAccess = Access.hasPermission(accessUser, AppPaths.HARDWARE_DETAIL);
        boolean canViewHardwarePath = Access.hasPermission(accessUser, AppPaths.IT_MGMT_AJAX_GET_HARDWARE_DETAIL);

        AdminService adminService = ServiceProvider.getAdminService(requestContext);
        boolean hasLicenseCustomFields = adminService.hasCustomFields(ObjectTypes.SOFTWARE_LICENSE);

        String editLicensePath = (hasLicenseCustomFields ? AppPaths.SOFTWARE_LICENSE_EDIT : AppPaths.SOFTWARE_DETAIL) + "?softwareId=" + softwareId;
        boolean showAddLicenseForm = false;

        //
        // Template: StandardTemplate
        //
        StandardTemplate standardTemplate = new StandardTemplate(requestContext);

        //
        // Template: HeaderTemplate
        //
        HeaderTemplate header = standardTemplate.getHeaderTemplate();

        if (!cmd.isEmpty() && !hasLicenseCustomFields) {
            // We will change the license key table based on whether we're adding/editing a license.
            if (cmd.equals("add") && Access.hasPermission(accessUser, AppPaths.SOFTWARE_LICENSE_ADD)) {
                formAction = AppPaths.SOFTWARE_LICENSE_ADD_2;

                showAddLicenseForm = true;
                rowClass = ui.getRowClass();
                canDeleteLicense = false;

                if (!actionForm.isResubmit()) {
                    actionForm.setLicense(new SoftwareLicense());
                    actionForm.setLicenseEntitlement("1");
                }
            } else if (cmd.equals("edit")) {
                formAction = AppPaths.SOFTWARE_LICENSE_EDIT_2;
                canDeleteLicense = false;

                SoftwareLicense softwareLicense = softwareService.getSoftwareLicense(actionForm.getSoftwareId(),
                        actionForm.getLicenseId());

                // Not a resubmit, set some defaults
                if (!actionForm.isResubmit()) {
                    actionForm.setLicense(softwareLicense);
                }
                formLicenseId = actionForm.getLicenseId();
            }

            //
            // Template: ActionErrorsTemplate
            //
            ActionErrorsTemplate errorsTemplate = new ActionErrorsTemplate();
            standardTemplate.addTemplate(errorsTemplate);
            errorsTemplate.setShowRequiredFieldMsg(true);
        }

        // Get Software License dataset.
        QueryBits query = new QueryBits();
        query.addSortColumn(SoftwareQueries.getOrderByColumn("license_key"));
        query.addSortColumn("license_id");
        query.addSortColumn(SoftwareQueries.getOrderByColumn("hardware_name"));

        Map installationCount = softwareService.getSoftwareLicenseCount(softwareId);
        int colSpan = 6;

        List<Map> softwareLicenseList = softwareService.getSoftwareLicenses(query, softwareId);
        List softwareLicenses = new ArrayList();

        if (!softwareLicenseList.isEmpty()) {
            // We want to keep track of the last license id appeared in the dataset,
            // because we need to do some rowspan in jsp
            Integer lastLicenseId = 0;

            for (Map license : softwareLicenseList) {
                Integer currentLicenseId = NumberUtils.replaceNull(license.get("license_id"));
                Map map = new HashMap();

                if (!lastLicenseId.equals(currentLicenseId)) {
                    lastLicenseId = currentLicenseId;
                    map.put("showCol", 1);
                    map.put("rowSpan", installationCount.get(license.get("license_id")));
                    if (canEditLicense) {
                        map.put("editPath", editLicensePath + "&licenseId=" + license.get("license_id") + "&cmd=edit");
                    }
                    map.put("licenseId", currentLicenseId);

                    Link link = new Link(requestContext);
                    link.setJavascript("toggleViewUpdate('cf" + currentLicenseId + "','" + AppPaths.ROOT
                            + AppPaths.SOFTWARE_AJAX_CUSTOM_FIELDS + "?softwareId=" + softwareId + "&licenseId=" + currentLicenseId + "')");
                    link.setImgSrc(Image.getInstance().getMagGlassIcon());

                    String popupLink = hasLicenseCustomFields ? "&nbsp;" + link.getString(): "";

                    map.put("licenseKey", HtmlUtils.encode((String)license.get("license_key")) + popupLink);
                    map.put("licenseEntitlement", license.get("license_entitlement"));

                    String licenseNotes = SoftwareUtils.formatLicenseKey(requestContext, (String)license.get("license_note"), ConfigManager.app.getSoftwareLicenseNotesNumChars(), currentLicenseId);
                    map.put("licenseNote", licenseNotes);
                    rowClass = ui.getRowClass();
                }
                map.put("rowClass", rowClass);

                Object hardwareId = license.get("hardware_id");

                StringBuilder hardwareName = new StringBuilder();

                Link hardwareDetailsLink = new Link(requestContext);
                hardwareDetailsLink.setTitle((String) license.get("hardware_name"));

                if (hardwareId != null && hasHardwareAccess) {
                    hardwareDetailsLink.setAjaxPath(AppPaths.HARDWARE_DETAIL + "?hardwareId=" + hardwareId);
                    hardwareName.append(hardwareDetailsLink);
                    if (canViewHardwarePath) {
                        hardwareDetailsLink = new Link(requestContext);
                        hardwareDetailsLink.setAppPath(AppPaths.HARDWARE_DETAIL + "?hardwareId=" + hardwareId);
                        hardwareDetailsLink.setOnclick("hardwarePopup(this," + hardwareId + ");return false;");
                        hardwareDetailsLink.setImgSrc(Image.getInstance().getMagGlassIcon());
                        hardwareName.append("&nbsp;").append(hardwareDetailsLink);
                    }
                } else {
                    hardwareName.append(hardwareDetailsLink);
                }
                map.put("hardwareName", hardwareName);
                map.put("hardwareId", hardwareId);
                softwareLicenses.add(map);
            }
        } else {
            //
            // Template: TableEmptyTemplate
            //
            TableEmptyTemplate empty = new TableEmptyTemplate();
            standardTemplate.addTemplate(empty);
            empty.setColSpan(colSpan);
            empty.setRowText(Localizer.getText(requestContext, "itMgmt.softwareDetail_emptyTableMessage"));
        }

        // Set a counter.
        Counter counter = new Counter();

        // Get Software License dataset.
        query = new QueryBits();
        query.addSortColumn(SoftwareQueries.getOrderByColumn("hardware_name"));

        List softwareNeedLicenseList = new ArrayList();
        List<Map> hardwareList = softwareService.getSoftwareLicenseHardwareList(query, softwareId);

        for (Map hardware : hardwareList) {
            Map map = new HashMap();
            Link hardwareDetailsLink = new Link(requestContext);
            hardwareDetailsLink.setTitle(HtmlUtils.encode((String) hardware.get("hardware_name")));
            if (hasHardwareAccess) {
                hardwareDetailsLink.setAjaxPath(AppPaths.HARDWARE_DETAIL + "?hardwareId=" + hardware.get("hardware_id"));
            }
            map.put("hardwareName", hardwareDetailsLink);

            // Put rowNum.
            map.put("rowNum", counter.incrCounter());
            softwareNeedLicenseList.add(map);
        }

        standardTemplate.setAttribute("softwareId", softwareId);
        standardTemplate.setAttribute("cmd", cmd);
        standardTemplate.setPathAttribute("formAction", formAction);
        standardTemplate.setAttribute("formCancelLink", Links.getCancelLink(requestContext, AppPaths.SOFTWARE_DETAIL + "?softwareId=" + softwareId).getString());
        standardTemplate.setAttribute("formLicenseId", formLicenseId);
        standardTemplate.setAttribute("rowClass", rowClass);
        standardTemplate.setAttribute("addLicense", showAddLicenseForm);
        standardTemplate.setAttribute("canDeleteLicense", canDeleteLicense);
        standardTemplate.setAttribute("ajaxHardwareDetailPath", AppPaths.IT_MGMT_AJAX_GET_HARDWARE_DETAIL + "?hardwareId=");
        standardTemplate.setAttribute("softwareLicenseList", softwareLicenses);
        standardTemplate.setAttribute("softwareNeedLicenseList", softwareNeedLicenseList);
        standardTemplate.setAttribute("colSpan", colSpan);

        //
        // Template: SoftwareSpecTemplate
        //
        SoftwareSpecTemplate softwareSpecTemplate = new SoftwareSpecTemplate(software);
        standardTemplate.addTemplate(softwareSpecTemplate);
        softwareSpecTemplate.setPopulateLinkedContract(true);

        //
        // Template: CustomFieldsTemplate
        //
        CustomFieldsTemplate customFieldsTemplate = new CustomFieldsTemplate();
        standardTemplate.addTemplate(customFieldsTemplate);
        customFieldsTemplate.setObjectTypeId(ObjectTypes.SOFTWARE);
        customFieldsTemplate.setObjectId(softwareId);
        customFieldsTemplate.setObjectAttrTypeId(software.getType());
        customFieldsTemplate.setShowDefaultHeader(false);

        //
        // Template: HeaderTemplate
        //
        SoftwareUtils.addSoftwareHeaderCommands(requestContext, header, softwareId);

        // Add Software license.
        if (Access.hasPermission(accessUser, AppPaths.SOFTWARE_LICENSE_ADD)) {
            Link link = new Link(requestContext);
            link.setAjaxPath(hasLicenseCustomFields ? AppPaths.SOFTWARE_LICENSE_ADD + "?softwareId=" + softwareId : AppPaths.SOFTWARE_DETAIL + "?softwareId=" + softwareId + "&cmd=add");
            link.setTitleKey("itMgmt.cmd.softwareLicenseAdd");
            header.addHeaderCmds(link);
        }

        header.setPageTitleKey("itMgmt.softwareDetail.header", new Object[] {software.getName()});

        //
        // Template: TabsTemplate
        //
        TabsTemplate tabs = new TabsTemplate();
        standardTemplate.addTemplate(tabs);
        tabs.setTabList(SoftwareUtils.softwareTabList(requestContext, software));
        tabs.setTabActive(SoftwareUtils.DETAILS_TAB);

        return standardTemplate.findTemplate(STANDARD_TEMPLATE);
    }
}