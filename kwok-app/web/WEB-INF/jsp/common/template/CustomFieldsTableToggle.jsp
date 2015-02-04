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

<bean:define id="customFieldsTemplate" name="CustomFieldsTemplate" type="com.kwoksys.action.common.template.CustomFieldsTemplate"/>
<bean:define id="customFields" name="customFieldsTemplate" property="customFields"/>

<logic:notEmpty name="customFields">
    <jsp:include page="/WEB-INF/jsp/common/template/CustomFieldsWidget.jsp"/>
    <div id="customFields" style="display:none">
        <jsp:include page="/WEB-INF/jsp/common/template/CustomFieldsTable.jsp"/>
    </div>

    <script type="text/javascript">
        function expandCustomFields() {
            showContent('customFields');swapViews('customFieldsCollapse','customFieldsExpand');
        }
        function collapseCustomFields() {
            hideContent('customFields');swapViews('customFieldsExpand','customFieldsCollapse');
        }
        <logic:equal name="customFieldsTemplate" property="expandCustomFields" value="true">
            expandCustomFields();
        </logic:equal>
    </script>
    <p>
</logic:notEmpty>
