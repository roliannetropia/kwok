<%--
 * ====================================================================
 * Copyright 2005-2012 Wai-Lun Kwok
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
--%>
<%@ taglib uri="http://struts.apache.org/tags-bean" prefix="bean" %>
<%@ taglib uri="http://struts.apache.org/tags-html" prefix="html" %>
<%@ taglib uri="http://struts.apache.org/tags-logic" prefix="logic" %>

<bean:define id="headerTemplate" name="HeaderTemplate" type="com.kwoksys.action.common.template.HeaderTemplate"/>
<bean:define id="standardTemplate" name="StandardTemplate" type="com.kwoksys.action.common.template.StandardTemplate"/>

<logic:equal name="standardTemplate" property="ajax" value="false">
<!DOCTYPE html>
<html>
<head>
    <meta HTTP-EQUIV="Content-Type" content="text/html; charset=UTF-8">
    <title><bean:write name="headerTemplate" property="titleText"/></title>
    <link rel="stylesheet" href="${path.defaultStyle}" type="text/css">
    <link rel="stylesheet" href="${Header_themeStylePath}" type="text/css">
    <link rel="shortcut icon" href="${image.favicon}">

    <style type="text/css">
    body {
        font-size:${headerTemplate.fontSize}px;
    }
    </style>

    <%-- User-defined stylesheet --%>
    <logic:notEmpty name="Header_stylesheet">
        <style type="text/css"><bean:write name="Header_stylesheet" filter="false"/></style>
    </logic:notEmpty>

    <script type="text/javascript" src="${path.javascript}"></script>
    <script type="text/javascript" src="${path.ckeditor}"></script>

    <link rel="stylesheet" href="${path.jqueryBase}/themes/base/jquery.ui.all.css">
    <script src="${path.jqueryBase}/jquery-1.6.2.js"></script>
    <script src="${path.jqueryBase}/ui/jquery.ui.core.js"></script>
    <script src="${path.jqueryBase}/ui/jquery.ui.widget.js"></script>
    <script src="${path.jqueryBase}/ui/jquery.ui.datepicker.js"></script>
    <script>
    var options = {
            showOn: "button",
            buttonImage: "${image.calendar}",
            buttonImageOnly: true,
            dateFormat: "${headerTemplate.jqueryDateFormat}"
        };
    </script>
</head>

<body>
<div id="container">
    <div id="header">
        <%-- This is for showing logout message.--%>
        <div id="shortcuts">
        <bean:write name="Header_welcomeUserMessage" ignore="true"/>

        <logic:notEmpty name="Header_logoutPath">
            | ${Header_logoutPath}
        </logic:notEmpty>
        <logic:notEmpty name="Header_userPreferencePath">
            | ${Header_userPreferencePath}
        </logic:notEmpty>
        <logic:notEmpty name="Header_adminPath">
            | ${Header_adminPath}
        </logic:notEmpty>
        </div>
        <bean:write name="headerTemplate" property="appLogoPath" filter="false"/>
    </div>

    <div id="moduleTabs">
        <logic:notEmpty name="Header_moduleTabs">
            <logic:iterate id="tab" name="Header_moduleTabs">
                <logic:equal name="tab" property="moduleActive" value="true">
                    <div id="headerModule${tab.moduleId}"><a href="${tab.modulePath}"><bean:message key="core.moduleName.${tab.moduleName}"/></a></div>
                </logic:equal>
                <logic:equal name="tab" property="moduleActive" value="custom">
                    <div><a href="${tab.modulePath}"><bean:write name="tab" property="moduleName"/></a></div>
                </logic:equal>
            </logic:iterate>
            <div class="end">&nbsp;</div>
        </logic:notEmpty>
    </div>

<div id="content">
</logic:equal>

<script type="text/javascript">
    // Highlight selected module tab
    var moduleIds = [${headerTemplate.moduleIds}];
    for (var iid=0;iid<moduleIds.length;iid++) {
        var elem = document.getElementById('headerModule' + moduleIds[iid]);
        if (elem != null) {
            elem.className = (moduleIds[iid] == ${headerTemplate.moduleId}) ? 'active' : '';
        }
    }
</script>

<logic:equal name="standardTemplate" property="ajax" value="true">
    <script type="text/javascript">
        document.title = '<bean:write name="headerTemplate" property="titleText"/>';
    </script>
</logic:equal>

<logic:notEmpty name="headerTemplate" property="headerCmds">
    <div class="subCmds row1">
        <bean:message key="core.template.header.cmd"/>&nbsp;
        <logic:iterate id="cmd" name="headerTemplate" property="headerCmds">
            <logic:empty name="cmd" property="path">
                <span class="inactive">${cmd.string}</span>
            </logic:empty>

            <logic:notEmpty name="cmd" property="path">${cmd.string}</logic:notEmpty>
        </logic:iterate>
    </div>
</logic:notEmpty>

<logic:notEmpty name="headerTemplate" property="notificationMsg">
    <div class="subCmds row1" style="text-align: center;"><bean:write name="headerTemplate" property="notificationMsg"/></div>
</logic:notEmpty>

<table style="width:100%"><tr><td id="contentContainer">

<logic:notEmpty name="Header_navCmds">
    <div class="nav section"><bean:write name="Header_navCmds" filter="false"/></div>
</logic:notEmpty>

<logic:notEmpty name="Header_title">
    <h2 class="${headerTemplate.titleClass}"><bean:write name="Header_title"/></h2>
</logic:notEmpty>