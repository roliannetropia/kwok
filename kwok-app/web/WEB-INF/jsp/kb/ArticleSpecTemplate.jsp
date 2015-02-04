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

<bean:define id="articleSpecTemplate" name="ArticleSpecTemplate" type="com.kwoksys.action.kb.ArticleSpecTemplate"/>

<table class="standard">
    <tr>
        <td><h2><bean:write name="articleSpecTemplate" property="article.name"/></h2>
            <bean:message key="common.column.article_id"/>: <bean:write name="articleSpecTemplate" property="article.id"/>
            <br><bean:message key="common.column.article_creator"/>: <bean:write name="articleSpecTemplate" property="articleCreator"/>
            <br><bean:message key="common.column.article_creation_date"/>: <bean:write name="articleSpecTemplate" property="article.creationDate"/>
        </td>
    </tr>
</table>
<p>
