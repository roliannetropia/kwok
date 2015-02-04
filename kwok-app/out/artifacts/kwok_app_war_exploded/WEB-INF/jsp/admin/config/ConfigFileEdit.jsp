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

<jsp:include page="/WEB-INF/jsp/common/template/ActionError.jsp"/>

<form action="${formAction}" method="post">
<input type="hidden" name="_resubmit" value="true">
<input type="hidden" name="cmd" value="${cmd}"/>
<table class="standardForm">
    <tr>
        <th width="30%"><bean:message key="admin.config.file.kilobyte"/>:</th>
        <td><html:select name="form" property="kilobyteUnits">
            <html:options collection="kbUnitOptions" property="value" labelProperty="label"/>
            </html:select><bean:message key="files.colData.file_size.bytes"/>
        </td>
    </tr>    
    <tr>
        <th><bean:message key="admin.config.file.company.repositoryPath"/>:</th>
        <td><html:text name="form" property="fileRepositoryCompany" size="60" disabled="${disableFilePathUpdate}"/></td>
    </tr>
    <tr>
        <th><bean:message key="admin.config.file.issue.repositoryPath"/>:</th>
        <td><html:text name="form" property="fileRepositoryIssue" size="60" disabled="${disableFilePathUpdate}"/></td>
    </tr>
    <tr>
        <th><bean:message key="admin.config.file.hardware.repositoryPath"/>:</th>
        <td><html:text name="form" property="fileRepositoryHardware" size="60" disabled="${disableFilePathUpdate}"/></td>
    </tr>
    <tr>
        <th><bean:message key="admin.config.file.software.repositoryPath"/>:</th>
        <td><html:text name="form" property="fileRepositorySoftware" size="60" disabled="${disableFilePathUpdate}"/></td>
    </tr>
    <tr>
        <th><bean:message key="admin.config.file.contract.repositoryPath"/>:</th>
        <td><html:text name="form" property="fileRepositoryContract" size="60" disabled="${disableFilePathUpdate}"/></td>
    </tr>
    <tr>
        <th><bean:message key="admin.config.file.kb.repositoryPath"/>:</th>
        <td><html:text name="form" property="fileRepositoryKb" size="60" disabled="${disableFilePathUpdate}"/></td>
    </tr>
    <tr>
        <td>&nbsp;</td>
        <td><span class="formFieldDesc"><bean:message key="admin.configDesc.module.file.repositoryPath"/></span></td>
    </tr>   
    <tr>
        <td>&nbsp;</td>
        <td><html:submit onclick="disableButton(this)">
            <bean:message key="form.button.save"/>
        </html:submit> 
            ${formCancelLink}
        </td>
    </tr>
</table>
</form>
