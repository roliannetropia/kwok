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
<%@ taglib uri="http://struts.apache.org/tags-logic" prefix="logic" %>

<jsp:include page="/WEB-INF/jsp/issues/IssueSpecTemplate.jsp"/>

<%-- Some ajax script here. --%>
<div id="hardwareDetail">&nbsp;</div>
<script type="text/javascript">
function hardwarePopup(obj, hardwareId) {
    cmenu.popupDiv = 'hardwareDetail';
    cmenu.url = '${ajaxHardwareDetailPath}';
    cmenu.dropit(obj, hardwareId);
}
</script>

<jsp:include page="/WEB-INF/jsp/common/template/Tabs.jsp"/>

<jsp:setProperty name="RootTemplate" property="prefix" value="_hardware"/>
<jsp:include page="/WEB-INF/jsp/hardware/HardwareListTemplate.jsp"/>

<p>
<jsp:setProperty name="RootTemplate" property="prefix" value="_software"/>
<jsp:include page="/WEB-INF/jsp/common/template/Table.jsp"/>

<p>
<jsp:setProperty name="RootTemplate" property="prefix" value="_companies"/>
<jsp:include page="/WEB-INF/jsp/common/template/Table.jsp"/>
