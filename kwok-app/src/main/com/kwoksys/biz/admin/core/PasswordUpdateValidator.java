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
package com.kwoksys.biz.admin.core;

import com.kwoksys.biz.admin.dto.AccessUser;
import com.kwoksys.biz.system.core.configs.ConfigManager;

import java.util.List;
import java.util.Arrays;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * PasswordUpdateValidator
 */
public class PasswordUpdateValidator {

    /**
     * Validates whether the password meets minimum password length requirement.
     * @param password
     * @return
     */
    public static boolean validateLength(String password) {
        return (password.length() >= ConfigManager.auth.getSecurityMinPasswordLength());
    }

    /**
     * Validates new user password against password complexity requirement.
     * @param newPassword
     * @param accessUser
     * @return
     */
    public static boolean validateComplexity(String newPassword, AccessUser accessUser) {
        if (!ConfigManager.admin.isSecurityPasswordComplexityEnabled()) {
            return true;
        }

        if (newPassword.toLowerCase().contains(accessUser.getFirstName().toLowerCase())) {
            return false;
        }

        if (newPassword.toLowerCase().contains(accessUser.getLastName().toLowerCase())) {
            return false;
        }

        // Password complexity MUST meet 3 of the 4 following: at least 1 upper letter, 1 lower letter, 1 number, and
        // 1 special character.
        List<String> requirements = Arrays.asList("([a-z])", "([A-Z])", "([0-9])", "([\\p{Punct}])");
        int found = 0;
        for (String regex : requirements) {
            Matcher m = Pattern.compile(regex).matcher(newPassword);
            if (m.find()) {
                found++;
            }
        }
        if (found < 3) {
            return false;
        }
        return true;
    }

}
