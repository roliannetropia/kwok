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
<%@ taglib uri="http://struts.apache.org/tags-logic" prefix="logic" %>
<%@ taglib uri="http://struts.apache.org/tags-bean" prefix="bean" %>

<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.0 Transitional//EN">
<html>
	<head>
		<meta HTTP-EQUIV="Content-Type" content="text/html; charset=UTF-8">
		<title><bean:write name="HeaderGenericTemplate_titleText"/></title>
		<link rel="stylesheet" href="${path.defaultStyle}" type="text/css">
        <link rel="stylesheet" href="${HeaderGenericTemplate_themeStylePath}" type="text/css">
        <script type="text/javascript" src="${path.javascript}"></script>
        <script type="text/javascript">
            window.focus();
        </script>
	</head>
	    <%-- To check whether to append onloadJavascript--%>
    <logic:empty name="HeaderGenericTemplate_onloadJavascript">
        <body>
    </logic:empty>
    <logic:notEmpty name="HeaderGenericTemplate_onloadJavascript">
        <body onload="${HeaderGenericTemplate_onloadJavascript}">
    </logic:notEmpty>

<div id="genericHeaderTitle" class="themeBgImg">
    <bean:write name="HeaderGenericTemplate_headerText"/>
</div>

