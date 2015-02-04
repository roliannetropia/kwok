/*
 * ====================================================================
 * Copyright 2005-2011 Wai-Lun Kwok
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
 */
package com.kwoksys.biz.system.core;

/**
 * Keywords
 */
public class Keywords {

    public static final String CUSTOM_FIELD_VALUE_VAR = "${CUSTOM_FIELD_VALUE}";

    public static final String CUSTOM_FIELD_VALUE_VAR_EXAMPLE = "http://www.kwoksys.com/${CUSTOM_FIELD_VALUE}";

    public static final String OPTIONAL_CUSTOM_FIELD = "_blank";

    public static final String USER_USERNAME_VAR = "${USERNAME}";

    /**
     * Using %% syntax instead of ${} because the properties files don't seem to support it.
     */
    public static final String ISSUE_ASSIGNEE_VAR = "%ISSUE_ASSIGNEE%";

    public static final String ISSUE_ID_VAR = "%ISSUE_ID%";

    public static final String ISSUE_REPORTED_BY_VAR = "%ISSUE_REPORTED_BY%";

    public static final String ISSUE_REPORTED_ON_VAR = "%ISSUE_REPORTED_ON%";

    public static final String ISSUE_STATUS_VAR = "%ISSUE_STATUS%";

    public static final String ISSUE_PRIORITY_VAR = "%ISSUE_PRIORITY%";

    public static final String ISSUE_TYPE_VAR = "%ISSUE_TYPE%";

    public static final String ISSUE_DESCRIPTION_VAR = "%ISSUE_DESCRIPTION%";

    public static final String ISSUE_COMMENT_VAR = "%ISSUE_COMMENT%";

    public static final String ISSUE_COMMENTED_BY_VAR = "%ISSUE_COMMENTED_BY%";

    public static final String ISSUE_COMMENTED_DATE_VAR = "%ISSUE_COMMENTED_DATE%";

    public static final String ISSUE_URL_VAR = "%ISSUE_URL%";
}
