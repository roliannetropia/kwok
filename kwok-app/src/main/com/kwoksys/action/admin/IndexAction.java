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
package com.kwoksys.action.admin;

import com.kwoksys.action.common.template.HeaderTemplate;
import com.kwoksys.action.common.template.StandardTemplate;
import com.kwoksys.biz.admin.core.AdminUtils;
import com.kwoksys.biz.admin.dto.AccessUser;
import com.kwoksys.biz.auth.core.Access;
import com.kwoksys.biz.system.core.AppPaths;
import com.kwoksys.biz.system.core.Image;
import com.kwoksys.biz.system.core.Links;
import com.kwoksys.biz.system.core.configs.ConfigManager;
import com.kwoksys.framework.properties.Localizer;
import com.kwoksys.framework.struts2.Action2;
import com.kwoksys.framework.ui.Link;

import java.util.ArrayList;
import java.util.List;

/**
 * Action class for Admin Index page.
 */
public class IndexAction extends Action2 {

    public String execute() throws Exception {
        boolean reloadLocalizer = requestContext.getParameterString("cmd").equals("reloadLocalizer");
        if (reloadLocalizer) {
            Localizer.reload();
        }

        AccessUser user = requestContext.getUser();

        List configList = new ArrayList();
        // Link to database config page.
        if (Access.hasPermission(user, AppPaths.ADMIN_CONFIG)) {
            configList.add(Links.getSystemSettingsImageLink(requestContext));

            // Link to application config page.
            Link link = new Link(requestContext);
            link.setImgSrc(Image.getInstance().getAdminAppConfIcon());
            link.setTitleKey("admin.config.app");
            link.setAjaxPath(AppPaths.ADMIN_CONFIG + "?cmd=" + AdminUtils.ADMIN_APP_CMD);
            configList.add(link);

            // Link to security settings
            link = new Link(requestContext);
            link.setImgSrc(Image.getInstance().getAdminSecurityIcon());
            link.setTitleKey("admin.config.auth");
            link.setAjaxPath(AppPaths.ADMIN_CONFIG + "?cmd=" + AdminUtils.ADMIN_AUTH_CMD);
            configList.add(link);

            // Link to email settings page.
            link = new Link(requestContext);
            link.setImgSrc(Image.getInstance().getAdminEmailIcon());
            link.setTitleKey("admin.config.email.title");
            link.setAjaxPath(AppPaths.ADMIN_CONFIG + "?cmd=" + AdminUtils.ADMIN_EMAIL_CMD);
            configList.add(link);

            // Link to company config page.
            link = new Link(requestContext);
            link.setImgSrc(Image.getInstance().getAdminCompanyIcon());
            link.setTitleKey("admin.configCompany.title");
            link.setAjaxPath(AppPaths.ADMIN_CONFIG + "?cmd=" + AdminUtils.ADMIN_COMPANY_CMD);
            configList.add(link);

            // File Settings
            link = new Link(requestContext);
            link.setImgSrc(Image.getInstance().getAdminFileSettingsIcon());
            link.setTitleKey("admin.config.file");
            link.setAjaxPath(AppPaths.ADMIN_CONFIG + "?cmd=" + AdminUtils.ADMIN_FILE_CMD);
            configList.add(link);

            // Link to look and feel config page.
            link = new Link(requestContext);
            link.setImgSrc(Image.getInstance().getAdminLookFeelIcon());
            link.setTitleKey("admin.index.lookAndFeel");
            link.setAjaxPath(AppPaths.ADMIN_CONFIG + "?cmd=" + AdminUtils.ADMIN_LOOK_FEEL_CMD);
            configList.add(link);
        }

        // Link to Attributes page.
        if (Access.hasPermission(user, AppPaths.ADMIN_ATTRIBUTE_LIST)) {
            Link link = new Link(requestContext);
            link.setImgSrc(Image.getInstance().getAdminSystemFieldsIcon());
            link.setTitleKey("admin.attributeList");
            link.setAjaxPath(AppPaths.ADMIN_ATTRIBUTE_LIST);
            configList.add(link);
        }

        // Link to Custom Attributes page.
        if (Access.hasPermission(user, AppPaths.ADMIN_CUSTOM_ATTR_LIST)) {
            Link link = new Link(requestContext);
            link.setImgSrc(Image.getInstance().getAdminCustomFieldsIcon());
            link.setTitleKey("admin.customAttrList");
            link.setAjaxPath(AppPaths.ADMIN_CUSTOM_ATTR_LIST);
            configList.add(link);
        }

        // Link to groups page
        if (Access.hasPermission(user, AppPaths.ADMIN_GROUP_LIST)) {
            Link link = new Link(requestContext);
            link.setImgSrc(Image.getInstance().getGroupIcon());
            link.setTitleKey("admin.index.groupList");
            link.setAjaxPath(AppPaths.ADMIN_GROUP_LIST);
            configList.add(link);
        }

        // Link to Users page
        if (Access.hasPermission(user, AppPaths.ADMIN_USER_LIST)) {
            Link link = new Link(requestContext);
            link.setImgSrc(Image.getInstance().getUserIcon());
            link.setTitleKey("admin.config.users");
            link.setAjaxPath(AppPaths.ADMIN_USER_INDEX);
            configList.add(link);
        }

        // Link to data import
        if (Access.hasPermission(user, AppPaths.ADMIN_DATA_IMPORT_INDEX)) {
            Link link = new Link(requestContext);
            link.setImgSrc(Image.getInstance().getAdminDataImport());
            link.setTitleKey("import");
            link.setAjaxPath(AppPaths.ADMIN_DATA_IMPORT_INDEX);
            configList.add(link);
        }

        configList.add(new Link(requestContext).setImgSrc(Image.getInstance().getAdminDataImport())
                .setTitleKey("admin.config.localizationReload").setAjaxPath(AppPaths.ADMIN_INDEX + "?cmd=reloadLocalizer"));

        List configListB = new ArrayList();
        // Documentation
        Link link = new Link(requestContext);
        link.setImgSrc(Image.getInstance().getAdiminDocIcon());
        link.setTitleKey("admin.index.documentation");
        link.setExternalPath(AppPaths.SITE_DOCUMENTATION);
        configListB.add(link);

        // Check to see if it should show Send Feedback link
        link = new Link(requestContext);
        link.setImgSrc(Image.getInstance().getAdminSurveyIcon());
        link.setTitleKey("admin.index.usageSurvey");
        link.setExternalPath(AppPaths.SITE_SURVEY + "?ver=" + ConfigManager.getSchemaVersion());
        configListB.add(link);

        //
        // Template: StandardTemplate
        //
        StandardTemplate standardTemplate = new StandardTemplate(requestContext);
        request.setAttribute("configList", configList);
        request.setAttribute("configListB", configListB);

        //
        // Template: HeaderTemplate
        //
        HeaderTemplate headerTemplate = standardTemplate.getHeaderTemplate();
        headerTemplate.setTitleKey("admin.index.title");

        if (reloadLocalizer) {
            headerTemplate.setNotificationMsg(Localizer.getText(requestContext, "admin.config.localizationReload.success"));
        }

        return standardTemplate.findTemplate(STANDARD_TEMPLATE);
    }
}
