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
package com.kwoksys.action.hardware;

import com.kwoksys.action.common.template.*;
import com.kwoksys.biz.ServiceProvider;
import com.kwoksys.biz.admin.dto.AccessUser;
import com.kwoksys.biz.auth.core.Access;
import com.kwoksys.biz.hardware.HardwareService;
import com.kwoksys.biz.hardware.core.HardwareUtils;
import com.kwoksys.biz.hardware.dao.HardwareQueries;
import com.kwoksys.biz.hardware.dto.Hardware;
import com.kwoksys.biz.hardware.dto.HardwareSoftwareMap;
import com.kwoksys.biz.software.SoftwareUtils;
import com.kwoksys.biz.software.dto.Software;
import com.kwoksys.biz.system.core.AppPaths;
import com.kwoksys.biz.system.core.Links;
import com.kwoksys.biz.system.core.ObjectTypes;
import com.kwoksys.framework.connections.database.QueryBits;
import com.kwoksys.framework.properties.Localizer;
import com.kwoksys.framework.struts2.Action2;
import com.kwoksys.framework.ui.Link;
import com.kwoksys.framework.ui.RowStyle;
import com.kwoksys.framework.util.HtmlUtils;
import org.apache.struts.util.LabelValueBean;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Action class for hardware license.
 */
public class HardwareDetailAction extends Action2 {

    public String execute() throws Exception {
        HardwareLicenseForm actionForm = (HardwareLicenseForm) getBaseForm(HardwareLicenseForm.class);
        AccessUser user = requestContext.getUser();

        HardwareService hardwareService = ServiceProvider.getHardwareService(requestContext);
        Hardware hardware = hardwareService.getHardware(actionForm.getHardwareId());

        int colSpan = 4;
        String softwareId = "";

        // For adding Software.
        List softwareOptions = new ArrayList();
        String formSoftwareLicense = AppPaths.IT_MGMT_AJAX_HARDWARE_ASSIGN_LICENSE + "?softwareId=";
        String cmd = actionForm.getCmd();

        RowStyle ui = new RowStyle();

        QueryBits query = new QueryBits();
        query.addSortColumn(Software.NAME);
        query.addSortColumn("license_key");

        List<HardwareSoftwareMap> maps = hardwareService.getInstalledLicense(query, hardware.getId());
        List installedLicenses = new ArrayList();

        //
        // Template: StandardTemplate
        //
        StandardTemplate standardTemplate = new StandardTemplate(requestContext);
        standardTemplate.setAttribute("softwareOptions", softwareOptions);
        standardTemplate.setPathAttribute("formAddLicAction", AppPaths.HARDWARE_LICENSE_ADD_2);
        standardTemplate.setAttribute("canRemoveLicense", Access.hasPermission(user, AppPaths.HARDWARE_LICENSE_REMOVE_2));
        standardTemplate.setPathAttribute("formRemoveLicenseAction", AppPaths.HARDWARE_LICENSE_REMOVE_2);
        standardTemplate.setAttribute("formCancelLink", Links.getCancelLink(requestContext, AppPaths.HARDWARE_DETAIL + "?hardwareId=" + hardware.getId()).getString());
        standardTemplate.setPathAttribute("formGetSoftwareLicenseAction", formSoftwareLicense);
        if (Access.hasPermission(user, AppPaths.SOFTWARE_AJAX_DETAILS)) {
            standardTemplate.setPathAttribute("formLicenseAjaxAction", AppPaths.SOFTWARE_AJAX_DETAILS);
        }
        standardTemplate.setAttribute("colSpan", colSpan);

        //
        // Template: CustomFieldsTemplate
        //
        CustomFieldsTemplate customFieldsTemplate = new CustomFieldsTemplate();
        standardTemplate.addTemplate(customFieldsTemplate);
        customFieldsTemplate.setObjectTypeId(ObjectTypes.HARDWARE);
        customFieldsTemplate.setObjectId(hardware.getId());
        customFieldsTemplate.setObjectAttrTypeId(hardware.getType());
        customFieldsTemplate.setShowDefaultHeader(false);

        //
        // Template: HeaderTemplate
        //
        HeaderTemplate headerTemplate = standardTemplate.getHeaderTemplate();
        HardwareUtils.addHardwareHeaderCommands(requestContext, headerTemplate, hardware.getId());
        headerTemplate.setPageTitleKey("itMgmt.hardwareDetail.header", new Object[] {hardware.getName()});

        // Assign Software link.
        if (Access.hasPermission(user, AppPaths.HARDWARE_LICENSE_ADD_2)) {
            Link link = new Link(requestContext);
            link.setAjaxPath(AppPaths.HARDWARE_DETAIL + "?cmd=add&hardwareId=" + hardware.getId());
            link.setTitleKey("itMgmt.cmd.softwareLicenseAssign");
            headerTemplate.addHeaderCmds(link);
        }

        if (cmd.equals("add")) {
            // The user is trying to add a Software, show him the Software list.
            query = new QueryBits();
            query.addSortColumn(HardwareQueries.getOrderByColumn(Software.NAME));

            List<Map> softwareList = hardwareService.getAvailableSoftware(query);
            for (Map software : softwareList) {
                softwareOptions.add(new LabelValueBean(software.get("software_name").toString(),
                        software.get("software_id").toString()));
            }

            if (!softwareList.isEmpty()) {
                // Get the first software, we want to show licenses for the first software.
                LabelValueBean b = (LabelValueBean) softwareOptions.get(0);
                softwareId = b.getValue();

                //
                // Template: FooterTemplate
                //
                FooterTemplate footerTemplate = standardTemplate.getFooterTemplate();
                footerTemplate.setOnloadJavascript("updateView('softwareLicensesDiv', '" + AppPaths.ROOT + formSoftwareLicense + softwareId + "');");
            }
        }

        if (!maps.isEmpty()) {
            boolean viewSoftwareDetail = Access.hasPermission(user, AppPaths.SOFTWARE_DETAIL);
            for (HardwareSoftwareMap map : maps) {
                Map datamap = new HashMap();
                datamap.put("rowClass", ui.getRowClass());
                datamap.put("mapId", map.getMapId());

                Link softwareLink = new Link(requestContext);
                softwareLink.setTitle(map.getSoftware().getName());

                if (viewSoftwareDetail) {
                    // Show the link only if user has access to the page.
                    softwareLink.setAjaxPath(AppPaths.SOFTWARE_DETAIL + "?softwareId=" + map.getSoftwareId());
                }
                datamap.put("softwareName", softwareLink);

                String licenseKey = HtmlUtils.encode(map.getLicense().getKey());
                if (licenseKey.isEmpty()) {
                    licenseKey = Localizer.getText(requestContext, "itMgmt.hardwareDetail.unknownLicense");
                }
                datamap.put("softwareId", map.getSoftwareId());
                datamap.put("licenseId", map.getLicenseId());
                datamap.put("licenseKey", licenseKey);
                datamap.put("licenseNote", SoftwareUtils.formatLicenseKey(map.getLicense().getNote()));
                installedLicenses.add(datamap);
            }
            standardTemplate.setAttribute("installedLicenses", installedLicenses);
        } else {
            //
            // Template: TableEmptyTemplate
            //
            TableEmptyTemplate empty = new TableEmptyTemplate();
            standardTemplate.addTemplate(empty);
            empty.setColSpan(colSpan);
            empty.setRowText(Localizer.getText(requestContext, "itMgmt.hardwareDetail.emptyTableMessage"));
        }

        //
        // Template: HardwareSpecTemplate
        //
        HardwareSpecTemplate tmpl = new HardwareSpecTemplate(hardware);
        standardTemplate.addTemplate(tmpl);
        tmpl.setPopulateLinkedContract(!cmd.equals("add"));

        //
        // Template: TabsTemplate
        //
        TabsTemplate tabs = new TabsTemplate();
        standardTemplate.addTemplate(tabs);
        tabs.setTabList(HardwareUtils.hardwareTabList(hardware, requestContext));
        tabs.setTabActive(HardwareUtils.HARDWARE_LICENSE_TAB);

        //
        // Template: ActionErrorsTemplate
        //
        standardTemplate.addTemplate(new ActionErrorsTemplate());

        return standardTemplate.findTemplate(STANDARD_TEMPLATE);
    }
}