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

<div id="tabs">
<logic:iterate id="tab" name="TabsTemplate" property="tabList">
    <logic:equal name="tab" property="tabName" value="${TabsTemplate.tabActive}">
        <div class="active"><bean:write name="tab" property="tabText"/></div>
    </logic:equal>
    <logic:notEqual name="tab" property="tabName" value="${TabsTemplate.tabActive}">
        <a href="${path.root}${tab.tabPath}" class="inactive"><bean:write name="tab" property="tabText"/></a>
    </logic:notEqual>
</logic:iterate>
</div>

<%-- To stop Firefox from rendering the table below this with the wrong alignment. --%>
<div style="clear: both"></div>
