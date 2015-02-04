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
<%@ taglib prefix="logic" uri="http://struts.apache.org/tags-logic" %>

<bean:define id="standardTemplate" name="StandardTemplate" type="com.kwoksys.action.common.template.StandardTemplate"/>

<logic:equal name="standardTemplate" property="autoGenerateTemplate" value="false">
    <jsp:include page="/WEB-INF/jsp/common/template/HeaderTemplate.jsp"/>

    <jsp:include page="${standardTemplate.templatePath}"/>

    <jsp:include page="/WEB-INF/jsp/common/template/FooterTemplate.jsp"/>
</logic:equal>

<logic:equal name="standardTemplate" property="autoGenerateTemplate" value="true">
    <logic:iterate id="jspPath" name="standardTemplate" property="jspTemplates">
        <jsp:include page="${jspPath}"/>
    </logic:iterate>
</logic:equal>

