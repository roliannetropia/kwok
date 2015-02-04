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

<h3 class="noLine">
<span id="customFieldsExpand"><a href="javascript:expandCustomFields();">
   <img src="${image.toggleExpandIcon}" class="standard" alt="" style="margin-right:5px"></a>
</span>

<span id="customFieldsCollapse" style="display:none">
   <a href="javascript:collapseCustomFields();">
       <img src="${image.toggleCollapseIcon}" class="standard" alt="" style="margin-right:5px"></a>
</span>
<bean:message key="admin.customAttrList"/>
</h3>