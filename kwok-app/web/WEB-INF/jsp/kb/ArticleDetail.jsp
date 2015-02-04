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

<bean:define id="article" name="article" type="com.kwoksys.biz.kb.dto.Article"/>

<table class="standard">
<tr>
    <td style="width:80%">
        <h2><bean:write name="article" property="name"/></h2>
        <div class="section">
        <bean:write name="articleText" filter="false"/>
        </div>
        <p><br><h3><bean:message key="files.fileAttachmentTab"/></h3>

        <div class="section">
        <logic:empty name="files">
            <bean:message key="files.noAttachments"/>
        </logic:empty>
        <logic:notEmpty name="files">
            <logic:iterate id="file" name="files">
                <bean:define id="fileObj" name="file" property="file" type="com.kwoksys.biz.files.dto.File"/>
                <p><bean:write name="file" property="fileName" filter="false"/>&nbsp;
                    <bean:write name="fileObj" property="title"/>
                    (<bean:write name="file" property="filesize"/>, <bean:write name="fileObj" property="creationDate"/>)
                    <logic:equal name="canDeleteFile" value="true">
                        [${file.deleteFilePath}]
                    </logic:equal>
            </logic:iterate>
        </logic:notEmpty>
        </div>
    </td>
    <td style="width:20%" valign="top">
        <div class="kbArticleInfo">
            <p><h3><bean:message key="kb.articleDetail.subHeader"/></h3>
            <bean:message key="common.column.article_id"/>: <bean:write name="article" property="id"/>
            <br><bean:message key="common.column.article_creator"/>: <bean:write name="articleCreator"/>
            <br><bean:message key="common.column.article_creation_date"/>: <bean:write name="article" property="creationDate"/>
            <p><i>(<bean:message key="kb.colName.view_count.description" arg0="${article.viewCount}"/>)</i>
        </div>
    </td>
</tr>
</table>
