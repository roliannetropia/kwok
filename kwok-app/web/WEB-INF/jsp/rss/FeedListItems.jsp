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

<link rel="stylesheet" href="${path.defaultStyle}" type="text/css">
<link rel="stylesheet" href="${themeStylePath}" type="text/css">

<bean:define id="rssFeed" name="rssFeed" type="com.kwoksys.biz.rss.dto.RssFeed"/>

<div id="rssItems">
    <div>
    <a href="${rssFeed.model.link}" target="_blank"><bean:write name="rssFeed" property="model.title"/></a>
        <logic:notEmpty name="editRssFeedPath">
             [<a href="javascript:void(0);"
                onclick="parent.location.href='${editRssFeedPath}${rssFeed.id}'"><bean:message key="common.command.Edit"/></a>]
        </logic:notEmpty>
        <logic:notEmpty name="deleteRssFeedPath">
             [<a href="javascript:void(0);"
                onclick="parent.location.href='${deleteRssFeedPath}${rssFeed.id}'"><bean:message key="common.command.Delete"/></a>]
        </logic:notEmpty>
    </div>

<logic:iterate id="item" name="rssFeedItems" indexId="indexId">
    <div class="rssTitle solidLine">
        <a href="${item.link}" target="_blank" class="rssTitle"><bean:write name="item" property="title" filter="false"/></a>
    </div>
    <div><bean:write name="item" property="description" filter="false"/></div>
</logic:iterate>
</div>
