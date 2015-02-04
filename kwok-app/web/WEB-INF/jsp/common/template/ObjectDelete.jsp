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

<bean:define id="objectDeleteTemplate" name="ObjectDeleteTemplate" type="com.kwoksys.action.common.template.ObjectDeleteTemplate"/>

<jsp:include page="/WEB-INF/jsp/common/template/ActionError.jsp"/>

<table class="tabBody">
    <logic:empty name="objectDeleteTemplate" property="titleText">
        <tr>
            <td class="header1 emptyHeader">&nbsp;</td>
        </tr>
    </logic:empty>
    <logic:notEmpty name="objectDeleteTemplate" property="titleText">
        <tr class="header1">
            <td><b><bean:write name="objectDeleteTemplate" property="titleText"/></b></td>
        </tr>
    </logic:notEmpty>
    <tr>
        <td>
            <form action="${objectDeleteTemplate.formAction}" method="post">
            <logic:iterate id="var" name="objectDeleteTemplate" property="formHiddenVariableMap">
                <html:hidden property="${var.key}" value="${var.value}"/>
            </logic:iterate>
            <table>
                    <tr>
                        <td><img src="${image.deleteIcon}" class="standard" alt=""> <bean:message key="${objectDeleteTemplate.confirmationMsgKey}"/><p>
                        <html:submit onclick="disableButton(this)"><bean:message key="${objectDeleteTemplate.submitButtonKey}"/></html:submit>
                            <html:link action="${objectDeleteTemplate.formCancelAction}" styleClass="cancel"><bean:message key="form.button.cancel"/></html:link>
                        </td>
                    </tr>
            </table>
            </form>
        </td>
    </tr>
</table>
<p>