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
        <th><bean:message key="admin.config.file.maxUploadSize"/>:</th>
        <td><bean:write name="maxFileUploadSize"/></td>
    </tr>
    <tr>
        <th><bean:message key="admin.config.file.kilobyte"/>:</th>
        <td><bean:write name="kilobyteUnits"/>&nbsp;<bean:message key="files.colData.file_size.bytes"/></td>
    </tr>
    <tr>
        <th><bean:message key="admin.config.file.company.repositoryPath"/>:</th>
        <td><bean:write name="companyRepositoryPath"/>
            <logic:equal name="validCompanyRepositoryPath" value="false">
                <br>
                <span class="error"><img src="${image.warning}" class="standard" alt="">
                    <bean:message key="admin.configWarning.fileRepositoryPath"/></span>
            </logic:equal>
        </td>
    </tr>
    <tr>
        <th><bean:message key="admin.config.file.issue.repositoryPath"/>:</th>
        <td><bean:write name="issueRepositoryPath"/>
            <logic:equal name="validIssueRepositoryPath" value="false">
                <br>
                <span class="error"><img src="${image.warning}" class="standard" alt="">
                    <bean:message key="admin.configWarning.fileRepositoryPath"/></span>
            </logic:equal>
        </td>
    </tr>
    <tr>
        <th><bean:message key="admin.config.file.hardware.repositoryPath"/>:</th>
        <td><bean:write name="hardwareRepositoryPath"/>
            <logic:equal name="validHardwareRepositoryPath" value="false">
                <br>
                <span class="error"><img src="${image.warning}" class="standard" alt="">
                    <bean:message key="admin.configWarning.fileRepositoryPath"/></span>
            </logic:equal>
        </td>
    </tr>
    <tr>
        <th><bean:message key="admin.config.file.software.repositoryPath"/>:</th>
        <td><bean:write name="softwareRepositoryPath"/>
            <logic:equal name="validSoftwareRepositoryPath" value="false">
                <br>
                <span class="error"><img src="${image.warning}" class="standard" alt="">
                    <bean:message key="admin.configWarning.fileRepositoryPath"/></span>
            </logic:equal>
        </td>
    </tr>
    <tr>
        <th><bean:message key="admin.config.file.contract.repositoryPath"/>:</th>
        <td><bean:write name="contractRepositoryPath"/>
            <logic:equal name="validContractRepositoryPath" value="false">
                <br>
                <span class="error"><img src="${image.warning}" class="standard" alt="">
                    <bean:message key="admin.configWarning.fileRepositoryPath"/></span>
            </logic:equal>
        </td>
    </tr>
        <tr>
        <th><bean:message key="admin.config.file.kb.repositoryPath"/>:</th>
        <td><bean:write name="kbRepositoryPath"/>
            <logic:equal name="validKbRepositoryPath" value="false">
                <br>
                <span class="error"><img src="${image.warning}" class="standard" alt="">
                    <bean:message key="admin.configWarning.fileRepositoryPath"/></span>
            </logic:equal>
        </td>
    </tr>
</table>
