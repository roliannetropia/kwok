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

<h2 class="noLine">
    <bean:message key="portal.siteList.header"/>
    <span class="command">[<a href="javascript:void(0);" onclick="toggleView('div1');"><bean:message key="portal.siteList.toggleDiv"/></a>]</span>
</h2>
<p>
<div id="div1" class="displayBlock">
<%-- We're doing a few loops here, from a list of categories to a list of sites. --%>
<logic:notEmpty name="dataList">
<logic:iterate id="categories" name="dataList">
    <logic:iterate id="category" name="categories" property="key">
        <h3><bean:write name="category" property="value"/></h3>
            &nbsp;&nbsp;
            <logic:iterate id="site" name="categories" property="value">
                <logic:equal name="site" property="appFrame" value="iframe">
                    <a onclick="showContent('iframeDiv');toggleIframe('iframe', '${site.sitePath}');return false;" href="${site.sitePath}"><bean:write name="site" property="siteName"/></a>
                </logic:equal>
                <logic:equal name="site" property="appFrame" value="popup">
                    <a href="${site.sitePath}" target="_blank"><bean:write name="site" property="siteName"/></a>
                    <a href="${site.sitePath}" target="_blank"><html:img src="${externalPopupImage}" titleKey="common.image.externalPopup" alt=""/></a>
                </logic:equal>&nbsp;
            </logic:iterate><p>
    </logic:iterate>
</logic:iterate>
</logic:notEmpty>
<%-- Show some message when there is no data --%>
<logic:empty name="dataList">
    <table class="standard">
        <jsp:include page="/WEB-INF/jsp/common/template/TableEmpty.jsp"/>
    </table>
</logic:empty>
</div>

<div id="iframeDiv" style="display:none;">
    <div class="solidLine">&nbsp;</div><iframe src="" height="800" width="100%" id="iframe" frameborder="0" class="portalIframe"></iframe>
</div>
