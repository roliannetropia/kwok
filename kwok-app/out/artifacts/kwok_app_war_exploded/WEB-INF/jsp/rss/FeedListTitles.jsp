<%--
 * ====================================================================
 * Copyright 2005-2014 Wai-Lun Kwok
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
<%@ taglib uri="http://struts.apache.org/tags-logic" prefix="logic" %>

<link rel="stylesheet" href="${path.defaultStyle}" type="text/css">
<link rel="stylesheet" href="${themeStylePath}" type="text/css">

<div id="rssList" height="100%" >
    <ul>
    <logic:notEmpty name="rssFeeds">
        <logic:iterate id="rssFeed" name="rssFeeds">
            <li>${rssFeed}</li>
        </logic:iterate>
    </logic:notEmpty>
    </ul>
</div>

