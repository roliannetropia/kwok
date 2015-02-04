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

import com.kwoksys.action.common.template.ActionErrorsTemplate;
import com.kwoksys.action.common.template.HeaderTemplate;
import com.kwoksys.action.common.template.StandardTemplate;
import com.kwoksys.biz.ServiceProvider;
import com.kwoksys.biz.admin.dto.AccessUser;
import com.kwoksys.biz.auth.core.Access;
import com.kwoksys.biz.hardware.HardwareService;
import com.kwoksys.biz.hardware.core.HardwareSearch;
import com.kwoksys.biz.hardware.dto.Hardware;
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
 * Action class for adding Hardware Member.
 */
public class HardwareMemberAddAction extends Action2 {

    public String execute() throws Exception {
        HardwareMemberForm actionForm = (HardwareMemberForm) getBaseForm(HardwareMemberForm.class);

        AccessUser user = requestContext.getUser();

        HardwareService hardwareService = ServiceProvider.getHardwareService(requestContext);
        Hardware hardware = hardwareService.getHardware(actionForm.getHardwareId());

        List hardwareList = new ArrayList();

        //
        // Template: StandardTemplate
        //
        StandardTemplate standardTemplate = new StandardTemplate(requestContext);
        standardTemplate.setAttribute("hardwareId", hardware.getId());
        standardTemplate.setAttribute("formSearchAction", AppPaths.HARDWARE_MEMBER_ADD + "?hardwareId=" + hardware.getId());
        standardTemplate.setAttribute("formSaveAction", AppPaths.HARDWARE_MEMBER_ADD_2);
        standardTemplate.setAttribute("formCancelLink", Links.getCancelLink(requestContext, AppPaths.HARDWARE_MEMBER + "?hardwareId=" + hardware.getId()).getString());

        if (!actionForm.getFormHardwareId().isEmpty()) {
            HardwareSearch hardwareSearch = new HardwareSearch();
            hardwareSearch.put(HardwareSearch.HARDWARE_ID_EQUALS, actionForm.getFormHardwareId());

            // Ready to pass variables to query.
            List<Hardware> hardwareDataset = hardwareService.getHardwareList(new QueryBits(hardwareSearch));
            if (!hardwareDataset.isEmpty()) {
                boolean hasHardwareAccess = Access.hasPermission(user, AppPaths.HARDWARE_DETAIL);

                for (Hardware memberHardware : hardwareDataset) {
                    Map map = new HashMap();
                    map.put("hardwareId", memberHardware.getId());

                    Link hardwareLink = new Link(requestContext);
                    hardwareLink.setTitle(memberHardware.getName());
                    if (hasHardwareAccess) {
                        hardwareLink.setAjaxPath(AppPaths.HARDWARE_DETAIL + "?hardwareId=" + memberHardware.getId());
                    }
                    map.put("hardwareName", hardwareLink.getString());
                    hardwareList.add(map);
                }
                standardTemplate.setAttribute("hardwareList", hardwareList);
            }
        }

        if (actionForm.getFormHardwareId().isEmpty()) {
            standardTemplate.setAttribute("selectHardwareMessage", "form.noSearchInput");
        } else if (hardwareList.isEmpty()) {
            standardTemplate.setAttribute("selectHardwareMessage", "form.noSearchResult");
        }

        standardTemplate.setAttribute("disableSaveButton", hardwareList.isEmpty());

        //
        // Template: HeaderTemplate
        //
        HeaderTemplate header = standardTemplate.getHeaderTemplate();
        header.setPageTitleKey("itMgmt.hardwareDetail.header", new Object[] {hardware.getName()});

        //
        // Template: HardwareSpecTemplate
        //
        HardwareSpecTemplate tmpl = new HardwareSpecTemplate(hardware);
        standardTemplate.addTemplate(tmpl);

        //
        // Template: ActionErrorsTemplate
        //
        standardTemplate.addTemplate(new ActionErrorsTemplate());

        return standardTemplate.findTemplate(STANDARD_TEMPLATE);
    }
}