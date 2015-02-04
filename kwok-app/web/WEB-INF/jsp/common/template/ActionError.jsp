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

<logic:present name="ActionErrorsTemplate">
    <bean:define id="actionErrorsTemplate" name="ActionErrorsTemplate" type="com.kwoksys.action.common.template.ActionErrorsTemplate"/>

    <p class="section">
    <logic:notEmpty name="actionErrorsTemplate" property="message">
        <bean:write name="actionErrorsTemplate" property="message" filter="false"/>&nbsp;
    </logic:notEmpty>

    <logic:equal name="actionErrorsTemplate" property="showRequiredFieldMsg" value="true">
        <bean:message key="requiredFieldBlurb"/>
    </logic:equal>
</logic:present>

<logic:notEmpty name="_error">
<div class="error section"><b><bean:message key="form.error.input"/></b>
    <ul>
        <logic:iterate id="error" name="_error">
            <li>${error}</li>
        </logic:iterate>
    </ul>
</div><p>
</logic:notEmpty>