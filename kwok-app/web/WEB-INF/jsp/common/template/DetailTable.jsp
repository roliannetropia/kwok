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

<bean:define id="template" name="DetailTableTemplate${RootTemplate.prefix}" type="com.kwoksys.action.common.template.DetailTableTemplate"/>
<bean:define id="table" name="template" property="table"/>

<table class="<bean:write name="template" property="style"/>">

<logic:iterate id="tr" name="table">
    <tr>
        <logic:iterate id="td" name="tr">
            <th style="width:15%">
                <logic:present name="td" property="headerKey"><bean:message key="${td.headerKey}"/>:</logic:present>
                <logic:present name="td" property="headerText"><bean:write name="td" property="headerText"/>:</logic:present>
            </th>
            <td<logic:notEmpty name="template" property="width"> style="width:<bean:write name="template" property="width"/>"</logic:notEmpty>><bean:write name="td" property="value" filter="false"/>&nbsp;</td>
        </logic:iterate>
    </tr>
</logic:iterate>
</table>

