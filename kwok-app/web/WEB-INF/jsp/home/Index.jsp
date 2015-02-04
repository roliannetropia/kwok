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

<bean:define id="standardTemplate" name="StandardTemplate" type="com.kwoksys.action.common.template.StandardTemplate"/>

<table class="standard section">
    <tr>
    <td style="width:100%; vertical-align:top;">
        <%-- We let users customize this home page  --%>
        <logic:empty name="homeCustomDescription">
            <h2 class="noLine"><bean:message key="common.app.name"/></h2>
            <p><bean:message key="common.app.description"/></p>
        </logic:empty>
        <logic:notEmpty name="homeCustomDescription">
            <bean:write name="homeCustomDescription" filter="false"/>
        </logic:notEmpty>
    </td>

    <td style="width:340px; vertical-align:top; text-align:center">
        <%-- If user is not logged in, show login form. --%>
        <logic:equal name="isUserLoggedOn" value="false">
        <form action="${formLoginAction}" name="loginForm" method="post">
            <%-- Show redirect path if available--%>
            <html:hidden name="form" property="redirectPath" value="${redirectPath}"/>
            <div class="header1 userInfoHeader"><bean:message key="home.index.sectionLoginHeader"/></div>
            <div class="row1 userInfo">
                <logic:notEmpty name="_error">
                    <p><span class="error">
                        <logic:iterate id="error" name="_error">
                            ${error}
                        </logic:iterate>
                    </span>
                </logic:notEmpty>
                <p><bean:message key="auth.login.username"/>:
                <br/><html:text name="form" property="username" size="30" style="width:260px; height:24px;"/>
                
                <p><bean:message key="auth.login.password"/>:
                <br/><input type="password" name="password" size="30" style="width:260px; height:24px;"/>
                
                <logic:notEmpty name="domain">
                    <p><bean:message key="auth.login.domain"/>:
                    <br/><html:select name="form" property="domain" styleClass="authDomain" style="width:260px; height:24px;">
                    <html:options collection="domainOptions" property="value" labelProperty="label"/>
                    </html:select>
                </logic:notEmpty>

                <p><html:submit onclick="disableButton(this)">
                        <bean:message key="home.index.sectionLoginButton"/>
                    </html:submit>

                <p><span class="formFieldDesc"><bean:message key="home.index.sectionLoginDesc"/></span>
            </div>
        </form>
        </logic:equal>
        <%-- If user is logged in, show user info. --%>
        <logic:equal name="isUserLoggedOn" value="true">
            <div class="header1 userInfoHeader"><bean:message key="home.index.sectionUserHeader"/>: <bean:write name="user" property="displayName"/></div>
            <div class="row1 userInfo">
                <ul class="hideIcon">
                <li>
                    <span class="header3"><b><bean:message key="common.column.username"/></b>:</span>
                    <bean:write name="user" property="username" filter="false"/>
                <li>
                    <span class="header3"><b><bean:message key="common.column.user_email"/></b>:</span>
                    <bean:write name="user" property="email" filter="false"/>
                <li>
                    <span class="header3"><b><bean:message key="common.column.user_member_since"/></b>:</span>
                    <br/><bean:write name="user" property="creationDate" filter="false"/>
                </ul>
            </div>
        </logic:equal>

        <%-- Features --%>
        <p>
        <form action="${formAction}" method="get">
        <div class="row1 userInfo">
                <bean:message key="home.index.localeHeader"/>:
                <br><html:select name="form" property="locale" onchange="changeSelectedOption(this);" style="width:260px; height:24px;">
                    <html:options collection="localeOptions" property="value" labelProperty="label"/>
                </html:select>
                <p>
                <bean:message key="home.index.themeHeader"/>:
                <logic:iterate id="theme" name="themeList">
                    <a href="${formAction}?theme=${theme}"><bean:message key="admin.config.theme.${theme}"/></a>&nbsp;
                </logic:iterate>
                <p>
                <bean:message key="home.index.fontSizesHeader"/>:
                <logic:iterate id="fontSize" name="fontSizes">
                    <a href="${formAction}?fontSize=${fontSize}"><bean:write name="fontSize"/></a>&nbsp;
                </logic:iterate>
        </div>
        </form>
    </td>
    </tr>
</table>
