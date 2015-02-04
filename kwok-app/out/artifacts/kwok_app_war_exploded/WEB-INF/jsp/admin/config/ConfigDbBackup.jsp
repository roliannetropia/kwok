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

<table class="standard">
    <tr>
        <th width="20%"><bean:message key="admin.config.db.backup.pgDumpPath"/>:</th>
        <td width="80%"><bean:write name="backupCmdPath"/>
            <logic:equal name="validBackupCmdPath" value="false">
                <br><img src="${image.warning}" class="standard" alt="">
                <span class="error"><bean:message key="admin.config.file.invalidPathWarning"/></span>
            </logic:equal>
        </td>
    </tr>
    <tr>
        <th><bean:message key="admin.config.db.backup.repositoryPath"/>:</th>
        <td><bean:write name="backupRepoPath"/>
            <logic:equal name="validBackupRepoPath" value="false">
                <br><img src="${image.warning}" class="standard" alt="">
                <span class="error"><bean:message key="admin.config.file.invalidPathWarning"/></span>
            </logic:equal>
        </td>
    </tr>
    <tr>
        <th><bean:message key="admin.config.db.backup.cmdPath"/>:</th>
        <td><bean:write name="backupCmd"/>
            <logic:equal name="backupCmdEnabled" value="true">
                [<a href="${backupExecutePath}"><bean:message key="admin.config.db.backup.execute.cmd"/></a>]
            </logic:equal>
            <logic:equal name="backupCmdEnabled" value="false">
                [<span class="inactive"><bean:message key="admin.config.db.backup.execute.cmd"/></span>]
            </logic:equal>
        </td>
    </tr>
</table>

<p>
<h3><bean:message key="admin.config.db.backup.filesHeader"/></h3>
<table border="1" cellpadding="4">
    <tr>
        <th><bean:message key="files.colName.file_name"/></th>
        <th><bean:message key="files.colName.modification_date"/></th>
        <th><bean:message key="files.colName.file_byte_size"/></th>
    </tr>
    <logic:notEmpty name="backupFiles">
        <logic:iterate id="file" name="backupFiles">
            <tr>
                <td><bean:write name="file" property="filename"/></td>
                <td><bean:write name="file" property="fileModifiedDate"/></td>
                <td><bean:write name="file" property="filesize"/></td>
            </tr>
        </logic:iterate>
    </logic:notEmpty>
</table>
