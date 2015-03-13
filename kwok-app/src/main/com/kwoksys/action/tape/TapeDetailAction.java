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

import com.kwoksys.action.common.template.*;
//import com.kwoksys.action.tape.TapeLicenseForm;
import com.kwoksys.action.tape.TapeSpecTemplate;
import com.kwoksys.biz.ServiceProvider;
import com.kwoksys.biz.admin.dto.AccessUser;
import com.kwoksys.biz.auth.core.Access;
import com.kwoksys.biz.tape.TapeService;
import com.kwoksys.biz.tape.core.TapeUtils;
import com.kwoksys.biz.tape.dao.TapeQueries;
import com.kwoksys.biz.tape.dto.Tape;
import com.kwoksys.biz.tape.dto.TapeSoftwareMap;
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
 * Action class for tape license.
 */
public class TapeDetailAction extends Action2 {

    public String execute() throws Exception {
//        TapeLicenseForm actionForm = (TapeLicenseForm) getBaseForm(TapeLicenseForm.class);
        AccessUser user = requestContext.getUser();

        TapeService tapeService = ServiceProvider.getTapeService(requestContext);
//        Tape tape = tapeService.getTape(actionForm.getTapeId());

//        get tape by extracting tapeId na binato ni tape add controller
        Tape tape = tapeService.getTape(Integer.parseInt(request.getParameter("tapeId")));

//        System.out.println("tape id: "+tape.getTapeName());
        int colSpan = 4;
//        String softwareId = "";

        // For adding Software.
//        List softwareOptions = new ArrayList();
//        String formSoftwareLicense = AppPaths.IT_MGMT_AJAX_TAPE_ASSIGN_LICENSE + "?softwareId=";
//        String cmd = actionForm.getCmd();

        RowStyle ui = new RowStyle();

//        QueryBits query = new QueryBits();
//        query.addSortColumn(Software.NAME);
//        query.addSortColumn("license_key");

//        List<TapeSoftwareMap> maps = tapeService.getInstalledLicense(query, tape.getId());
//        List installedLicenses = new ArrayList();

        //
        // Template: StandardTemplate
        //
        StandardTemplate standardTemplate = new StandardTemplate(requestContext);
//        standardTemplate.setAttribute("softwareOptions", softwareOptions);
//        standardTemplate.setPathAttribute("formAddLicAction", AppPaths.TAPE_LICENSE_ADD_2);
//        standardTemplate.setAttribute("canRemoveLicense", Access.hasPermission(user, AppPaths.TAPE_LICENSE_REMOVE_2));
//        standardTemplate.setPathAttribute("formRemoveLicenseAction", AppPaths.TAPE_LICENSE_REMOVE_2);
        standardTemplate.setAttribute("formCancelLink", Links.getCancelLink(requestContext, AppPaths.TAPE_DETAIL + "?tapeId=" + tape.getId()).getString());
//        standardTemplate.setPathAttribute("formGetSoftwareLicenseAction", formSoftwareLicense);
//        if (Access.hasPermission(user, AppPaths.SOFTWARE_AJAX_DETAILS)) {
//            standardTemplate.setPathAttribute("formLicenseAjaxAction", AppPaths.SOFTWARE_AJAX_DETAILS);
//        }
        standardTemplate.setAttribute("colSpan", colSpan);

        //
        // Template: CustomFieldsTemplate
        //
        CustomFieldsTemplate customFieldsTemplate = new CustomFieldsTemplate();
        standardTemplate.addTemplate(customFieldsTemplate);
        customFieldsTemplate.setObjectTypeId(ObjectTypes.TAPE);
        customFieldsTemplate.setObjectId(tape.getId());
//        customFieldsTemplate.setObjectAttrTypeId(tape.getMediaType());
        customFieldsTemplate.setShowDefaultHeader(false);

        //
        // Template: HeaderTemplate
        //
        HeaderTemplate headerTemplate = standardTemplate.getHeaderTemplate();
        TapeUtils.addTapeHeaderCommands(requestContext, headerTemplate, tape.getId());
        headerTemplate.setPageTitleKey("itMgmt.tapeDetail.header", new Object[] {tape.getTapeBarcodeNumber()});

//        // Assign Software link.
        if (Access.hasPermission(user, AppPaths.TAPE_LICENSE_ADD_2)) {
            Link link = new Link(requestContext);
            link.setAjaxPath(AppPaths.TAPE_DETAIL + "?cmd=add&tapeId=" + tape.getId());
            link.setTitleKey("itMgmt.cmd.softwareLicenseAssign");
            headerTemplate.addHeaderCmds(link);
        }

//        if (cmd.equals("add")) {
            // The user is trying to add a Software, show him the Software list.
//            query = new QueryBits();
//            query.addSortColumn(TapeQueries.getOrderByColumn(Software.NAME));
//
//            List<Map> softwareList = tapeService.getAvailableSoftware(query);
//            for (Map software : softwareList) {
//                softwareOptions.add(new LabelValueBean(software.get("software_name").toString(),
//                        software.get("software_id").toString()));
//            }
//
//            if (!softwareList.isEmpty()) {
//                // Get the first software, we want to show licenses for the first software.
//                LabelValueBean b = (LabelValueBean) softwareOptions.get(0);
//                softwareId = b.getValue();
//
//                //
//                // Template: FooterTemplate
//                //
//                FooterTemplate footerTemplate = standardTemplate.getFooterTemplate();
//                footerTemplate.setOnloadJavascript("updateView('softwareLicensesDiv', '" + AppPaths.ROOT + formSoftwareLicense + softwareId + "');");
//            }
//        }

//        if (!maps.isEmpty()) {
//            boolean viewSoftwareDetail = Access.hasPermission(user, AppPaths.SOFTWARE_DETAIL);
//            for (TapeSoftwareMap map : maps) {
//                Map datamap = new HashMap();
//                datamap.put("rowClass", ui.getRowClass());
//                datamap.put("mapId", map.getMapId());
//
//                Link softwareLink = new Link(requestContext);
//                softwareLink.setTitle(map.getSoftware().getName());
//
//                if (viewSoftwareDetail) {
//                    // Show the link only if user has access to the page.
//                    softwareLink.setAjaxPath(AppPaths.SOFTWARE_DETAIL + "?softwareId=" + map.getSoftwareId());
//                }
//                datamap.put("softwareName", softwareLink);
//
//                String licenseKey = HtmlUtils.encode(map.getLicense().getKey());
//                if (licenseKey.isEmpty()) {
//                    licenseKey = Localizer.getText(requestContext, "itMgmt.tapeDetail.unknownLicense");
//                }
//                datamap.put("softwareId", map.getSoftwareId());
//                datamap.put("licenseId", map.getLicenseId());
//                datamap.put("licenseKey", licenseKey);
//                datamap.put("licenseNote", SoftwareUtils.formatLicenseKey(map.getLicense().getNote()));
//                installedLicenses.add(datamap);
//            }
//            standardTemplate.setAttribute("installedLicenses", installedLicenses);
//        } else {
//            //
//            // Template: TableEmptyTemplate
//            //
//            TableEmptyTemplate empty = new TableEmptyTemplate();
//            standardTemplate.addTemplate(empty);
//            empty.setColSpan(colSpan);
//            empty.setRowText(Localizer.getText(requestContext, "itMgmt.tapeDetail.emptyTableMessage"));
//        }

        //
        // Template: TapeSpecTemplate
        //
//        System.out.println("tape -> " + tape.getTapeName());
        System.out.println("serial -> " + tape.getTapeSerialNumber());
        System.out.println("barcode -> " + tape.getTapeBarcodeNumber());
        System.out.println("manufacturer -> " + tape.getManufacturerName());
        System.out.println("vendor -> " + tape.getVendorName());
        System.out.println("media type -> " + tape.getMediaType());
        System.out.println("location -> " + tape.getTapeLocation());
        System.out.println("retention -> " + tape.getTapeRetention());
        System.out.println("system -> " + tape.getTapeSystem());
        System.out.println("status -> " + tape.getTapeStatus());
        System.out.println("time -> " + tape.getTapeTransactionTime());
        TapeSpecTemplate tmpl = new TapeSpecTemplate(tape);
        standardTemplate.addTemplate(tmpl);
//        tmpl.setPopulateLinkedContract(!cmd.equals("add"));

        //
        // Template: TabsTemplate
        //
        TabsTemplate tabs = new TabsTemplate();
        standardTemplate.addTemplate(tabs);
        tabs.setTabList(TapeUtils.tapeTabList(tape, requestContext));
        tabs.setTabActive(TapeUtils.TAPE_LICENSE_TAB);

        //
        // Template: ActionErrorsTemplate
        //
        standardTemplate.addTemplate(new ActionErrorsTemplate());

        return standardTemplate.findTemplate(STANDARD_TEMPLATE);
    }
}