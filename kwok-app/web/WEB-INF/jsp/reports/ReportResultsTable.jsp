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

<logic:notEmpty name="reportTitle">
    <h2><bean:write name="reportTitle"/></h2>
</logic:notEmpty>

<table border="1" cellspacing="0" cellpadding="5">
    <tr>
    <logic:iterate id="header" name="columnHeaders">
        <td><bean:write name="header"/></td>
    </logic:iterate>
    </tr>
    <logic:iterate id="row" name="rows">
        <tr>
            <logic:iterate id="column" name="row">
                <td><logic:present name="column"><bean:write name="column"/></logic:present>&nbsp;</td>
            </logic:iterate>
        </tr>
    </logic:iterate>
</table>
