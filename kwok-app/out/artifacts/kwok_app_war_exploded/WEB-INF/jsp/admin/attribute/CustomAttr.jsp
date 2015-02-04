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

<table class="standard">
<logic:iterate id="objectTypes" name="objectTypesMap">
    <tr><td width="30%">
        <h3><bean:message key="common.objectType.${objectTypes.key}"/>&nbsp;
            <logic:notEmpty name="attrAddPath">
                <span class="command">[<html:link action="${attrAddPath}${objectTypes.key}"><bean:message key="admin.attributeAdd.command"/></html:link>]</span>
            </logic:notEmpty>
            <logic:notEmpty name="attrGroupAddPath">
                <span class="command">[<html:link action="${attrGroupAddPath}${objectTypes.key}"><bean:message key="admin.attributeGroupAdd.command"/></html:link>]</span>
            </logic:notEmpty>
        </h3>
        <blockquote>
        <logic:iterate id="typeGroupsMap" name="objectTypes" property="value">
            <bean:define id="groupMap" name="typeGroupsMap" property="value"/>
            <logic:notEmpty name="typeGroupsMap" property="key">
            <h3><bean:write name="typeGroupsMap" property="key" filter="false"/>&nbsp;
                <logic:present name="groupMap" property="attrGroupEditPath">
                    <span class="command">[<html:link action="${groupMap.attrGroupEditPath}"><bean:message key="common.command.Edit"/></html:link>]
                </logic:present>
                <logic:present name="groupMap" property="attrGroupDeletePath">
                    <span class="command">[<html:link action="${groupMap.attrGroupDeletePath}"><bean:message key="common.command.Delete"/></html:link>]
                </logic:present>
            </h3>
            </logic:notEmpty>
            <ul>
            <logic:iterate id="attr" name="groupMap" property="customFields">
                <li><bean:write name="attr" property="attrName" filter="false"/></li>
            </logic:iterate>
            </ul>
        </logic:iterate>
        </blockquote>
    </td></tr>
</logic:iterate>
</table>
