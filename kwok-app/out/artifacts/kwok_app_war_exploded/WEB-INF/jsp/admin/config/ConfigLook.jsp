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

<table class="standard details">
    <tr>
        <th><bean:message key="admin.config.theme"/>:</th>
        <td><bean:write name="theme"/></td>
    </tr>
    <tr>
        <th><bean:message key="admin.config.stylesheet"/>:</th>
        <td><pre><bean:write name="stylesheet"/></pre></td>
    </tr>
    <tr>
        <th><bean:message key="admin.config.homeCustomDescription"/>:</th>
        <td><bean:write name="homeCustomDescription" filter="false"/></td>
    </tr>
</table>
