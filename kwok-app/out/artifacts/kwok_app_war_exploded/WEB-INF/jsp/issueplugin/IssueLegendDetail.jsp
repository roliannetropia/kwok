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

<jsp:include page="/WEB-INF/jsp/common/template/HeaderGeneric.jsp"/>

<p>
<table class="standard">
<logic:iterate id="attributeField" name="attributeFieldList">
    <bean:define id="attributeField" name="attributeField" property="value" type="com.kwoksys.biz.admin.dto.AttributeField"/>
    <tr>
        <td class="attrFieldName" nowrap><bean:write name="attributeField" property="name"/>:</td>
        <td class="smallDottedLine infoContent"><bean:write name="attributeField" property="description"/>&nbsp;</td>
    </tr>
</logic:iterate>
</table>

<jsp:include page="/WEB-INF/jsp/common/template/FooterGeneric.jsp"/>