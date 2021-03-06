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

<bean:define id="hardwareSpecTemplate" name="HardwareSpecTemplate" type="com.kwoksys.action.hardware.HardwareSpecTemplate"/>

<logic:notEqual name="hardwareSpecTemplate" property="disableHeader" value="true">
    <h2><bean:write name="hardwareSpecTemplate" property="headerText"/></h2>
</logic:notEqual>

<jsp:include page="/WEB-INF/jsp/common/template/DetailTable.jsp"/>

<logic:notEmpty name="HardwareSpecTemplate_linkedContracts">
    <h3><bean:message key="hardware.detail.colName.linkedContracts"/></h3>
        <div class="section"><bean:write name="HardwareSpecTemplate_linkedContracts" filter="false"/></div>
</logic:notEmpty>
<p>