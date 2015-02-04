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
package com.kwoksys.biz.system.core;

/**
 * Images
 */
public class Image {
    private static final Image INSTANCE = new Image(AppPaths.ROOT);

    private String rootPath = "";

    private Image(String rootPath) {
        this.rootPath = rootPath;
    }

    public static Image getInstance() {
        return INSTANCE;
    }

    public String getFavicon() {
        return rootPath + "/favicon.ico";
    }

    public String getAdminIcon() {
        return rootPath + "/common/images/silkicons/wrench.png";
    }

    public String getAdminDataImport() {
        return rootPath + "/common/images/silkicons/data_import.png";
    }

    public String getAdiminDocIcon() {
        return rootPath + "/common/images/silkicons/admin_documentation.png";
    }

    public String getAdminAppConfIcon() {
        return rootPath + "/common/images/silkicons/application_config.png";
    }

    public String getAdminLookFeelIcon() {
        return rootPath + "/common/images/opensource/owg/looknfeel.png";
    }

    public String getAdminSecurityIcon() {
        return rootPath + "/common/images/silkicons/admin_security_settings.png";
    }

    public String getAdminSystemIcon() {
        return rootPath + "/common/images/silkicons/cog.png";
    }

    public String getAdminCompanyIcon() {
        return rootPath + "/common/images/silkicons/company.png";
    }

    public String getAdminCustomFieldsIcon() {
        return rootPath + "/common/images/silkicons/custom_fields.png";
    }

    public String getAdminEmailIcon() {
        return rootPath + "/common/images/silkicons/email_settings.png";
    }

    public String getAdminFileSettingsIcon() {
        return rootPath + "/common/images/silkicons/file_settings.png";
    }

    public String getAdminSurveyIcon() {
        return rootPath + "/common/images/silkicons/usage_survey.png";
    }

    public String getAdminSystemFieldsIcon() {
        return rootPath + "/common/images/silkicons/system_fields.png";
    }

    public String getAppLogo() {
        return rootPath + "/common/images/logo.png";
    }

    public String getBlogComment() {
        return rootPath + "/common/images/opensource/kp/speech-bubble.gif";
    }

    public String getCalendar() {
        return rootPath + "/common/images/silkicons/calendar_view_month.png";
    }

    public String getCsvFileIcon() {
        return rootPath + "/common/images/silkicons/page_excel.png";
    }

    public String getFileAddIcon() {
        return rootPath + "/common/images/silkicons/page_add.png";
    }

    public String getFileImageDirIcon() {
        return rootPath + "/common/images/silkicons/";
    }

    public String getGroupIcon() {
        return rootPath + "/common/images/silkicons/group.png";
    }

    public String getGroupAddIcon() {
        return rootPath + "/common/images/silkicons/group_add.png";
    }

    public String getGroupDeleteIcon() {
        return rootPath + "/common/images/silkicons/group_delete.png";
    }

    public String getGroupEditIcon() {
        return rootPath + "/common/images/silkicons/group_edit.png";
    }

    public String getHelpIcon() {
        return rootPath + "/common/images/silkicons/help.png";
    }

    public String getInfoIcon() {
        return rootPath + "/common/images/silkicons/information.png";
    }

    public String getLogout() {
        return rootPath + "/common/images/silkicons/door_out.png";
    }

    public String getMagGlassIcon() {
        return rootPath + "/common/images/opensource/kp/mag-glass.gif";
    }

    public String getNoteAddIcon() {
        return rootPath + "/common/images/silkicons/note_add.png";
    }

    public String getPermissionYesIcon() {
        return rootPath +  "/common/images/silkicons/tick.png";
    }

    public String getPermissionNoIcon() {
        return rootPath + "/common/images/silkicons/cross.png";
    }

    public String getPopupClose() {
        return rootPath + "/common/images/close-popup.gif";
    }

    public String getPreference() {
        return rootPath + "/common/images/wikimedia-commons/applications-other-16.gif";
    }

    public String getPrintIcon() {
        return rootPath + "/common/images/silkicons/print.png";
    }
    public String getRssFeedIcon() {
        return rootPath + "/common/images/silkicons/feed.png";
    }

    public String getRssFeedAddIcon() {
        return rootPath + "/common/images/silkicons/feed_add.png";
    }

    public String getSortIcon() {
        return rootPath + "/common/images/sort.gif";
    }

    public String getSortAscIcon() {
        return rootPath + "/common/images/sort-asc.gif";
    }

    public String getSortDesc() {
        return rootPath + "/common/images/sort-desc.gif";
    }

    public String getToggleExpandIcon() {
        return rootPath + "/common/images/toggle-expand-15.png";
    }

    public String getToggleCollapseIcon() {
        return rootPath + "/common/images/toggle-collapse-15.png";
    }

    public String getWarning() {
        return rootPath + "/common/images/silkicons/error.png";
    }

    public String getExternalPopupIcon() {
        return rootPath + "/common/images/opensource/kp/external.gif";
    }

    public String getDeleteIcon() {
        return rootPath + "/common/images/wikimedia-commons/user-trash-48.gif";
    }

    public String getUserIcon() {
        return rootPath + "/common/images/silkicons/user.png";
    }

    public String getUserAddIcon() {
        return rootPath + "/common/images/silkicons/user_add.png";
    }

    public String getUserDeleteIcon() {
        return rootPath + "/common/images/silkicons/user_delete.png";
    }

    public String getUserEditIcon() {
        return rootPath + "/common/images/silkicons/user_edit.png";
    }

    public String getVCardIcon() {
        return rootPath + "/common/images/silkicons/vcard.png";
    }
}
