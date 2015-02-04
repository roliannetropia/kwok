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

<table class="standard section">
<tr>

<td style="width:50%; vertical-align:top">
<logic:notEmpty name="configList">
    <logic:iterate id="link" name="configList">
        <p>${link.string}
    </logic:iterate>
</logic:notEmpty>
</td>

<td style="width:50%; vertical-align:top">
<logic:notEmpty name="configListB">
    <logic:iterate id="link" name="configListB">
        <p>${link.string}
    </logic:iterate>
</logic:notEmpty>
</td>

</tr>
</table>
