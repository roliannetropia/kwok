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
        <th width="30%"><bean:message key="admin.config.os"/>:</th>
        <td width="70%"><bean:write name="os"/></td>
    </tr>
    <tr>
        <th><bean:message key="admin.config.os.arch"/>:</th>
        <td><bean:write name="osArch"/></td>
    </tr>
    <tr><td colspan="2">
        <h3><bean:message key="admin.config.jvm"/></h3>
    </td></tr>
    <tr>
        <th><bean:message key="admin.config.jvm.version"/>:</th>
        <td><bean:write name="jvmVersion"/></td>
    </tr>
    <tr>
        <th><bean:message key="admin.config.jvm.vendor"/>:</th>
        <td><bean:write name="jvmVendor"/></td>
    </tr>
    <tr>
        <th><bean:message key="admin.config.jvm.home"/>:</th>
        <td><bean:write name="jvmHome"/></td>
    </tr>
    <tr>
        <th><bean:message key="admin.config.system.user.home"/>:</th>
        <td><bean:write name="userHome"/></td>
    </tr>
    <tr>
        <th><bean:message key="admin.config.jvm.freeMemory"/>:</th>
        <td><bean:write name="jvmFreeMemory"/></td>
    </tr>
    <tr>
        <th><bean:message key="admin.config.jvm.totalMemory"/>:</th>
        <td><bean:write name="jvmTotalMemory"/></td>
    </tr>
    <tr>
        <th><bean:message key="admin.config.jvm.maxMemory"/>:</th>
        <td><bean:write name="jvmMaxMemory"/></td>
    </tr>
    <tr><td colspan="2">
        <h3><bean:message key="admin.config.db.server"/></h3>
    </td></tr>
    <tr>
        <th><bean:message key="admin.config.db.serverName"/>:</th>
        <td><bean:write name="dbProductName"/></td>
    </tr>
    <tr>
        <th><bean:message key="admin.config.db.serverVersion"/>:</th>
        <td><bean:write name="dbProductVersion"/></td>
    </tr>
    <logic:notEmpty name="databases">
    <tr>
        <th><bean:message key="admin.config.db.databases"/>:</th>
        <td><bean:write name="databases"/></td>
    </tr>
    </logic:notEmpty>
    <tr><td colspan="2">
        <h3><bean:message key="admin.configHeader.db_config"/>
        <span class="command"> [${backupLink}]</span></h3>
    </td></tr>
    <tr>
        <th><bean:message key="admin.config.db.host"/>:</th>
        <td><bean:write name="dbHost"/></td>
    </tr>
    <tr>
        <th><bean:message key="admin.config.db.port"/>:</th>
        <td><bean:write name="dbPort"/></td>
    </tr>
    <tr>
        <th><bean:message key="admin.config.db.schemaName"/>:</th>
        <td><bean:write name="dbName"/></td>
    </tr>
    <tr>
        <th><bean:message key="admin.config.db.maxPoolSize"/>:</th>
        <td><bean:write name="dbMaxPoolSize"/></td>
    </tr>
    <tr>
        <th><bean:message key="admin.config.db.poolSizeCurrent"/>:</th>
        <td><bean:write name="dbPoolSizeCurrent"/></td>
    </tr>
    <tr><td colspan="2">
        <h3><bean:message key="admin.config.logging"/>
        <logic:notEmpty name="loggingLink">
            <span class="command"> [${loggingLink}]</span>
        </logic:notEmpty></h3>
    </td></tr>
    <tr>
        <th><bean:message key="admin.config.logging.database"/>:</th>
        <td><bean:write name="databaseAccessLogLevel"/></td>
    </tr>
    <tr>
        <th><bean:message key="admin.config.logging.ldap"/>:</th>
        <td><bean:write name="ldapLogLevel"/></td>
    </tr>
    <tr>
        <th><bean:message key="admin.config.logging.scheduler"/>:</th>
        <td><bean:write name="schedulerLogLevel"/></td>
    </tr>
    <tr>
        <th><bean:message key="admin.config.logging.template"/>:</th>
        <td><bean:write name="templateLogLevel"/></td>
    </tr>
</table>
