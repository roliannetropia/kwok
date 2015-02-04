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

<logic:iterate id="attrMap" name="customFields">
    <div class="customField">
    <logic:notEmpty name="attrMap" property="key">
        <h3><bean:write name="attrMap" property="key" filter="false"/></h3>
    </logic:notEmpty>
    <table class="standard">
        <logic:iterate id="attr" name="attrMap" property="value">
            <tr><th><bean:write name="attr" property="name"/>:</th>
            <td>
                <bean:write name="attr" property="value" filter="false"/>
                <logic:notEmpty name="attr" property="attrLinkUrl">
                    [<a href="${attr.attrLinkUrl}" target="_blank"><bean:write name="attr" property="attrLinkName"/></a>]
                </logic:notEmpty>
            </td></tr>
        </logic:iterate>
    </table>
    </div>
</logic:iterate>
