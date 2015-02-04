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
package com.kwoksys.biz.system;

import com.kwoksys.framework.properties.Localizer;
import com.kwoksys.framework.http.RequestContext;
import com.kwoksys.framework.util.DatetimeUtils;

import java.util.Date;

/**
 * SystemUtil
 */
public class SystemUtils {
    public static String formatExpirationDate(RequestContext requestContext, Long unixTimestamp, Date expirationDate,
                                              int expirationCountdown) {
        String output = "";
        long oneDay = 86400000;
        long daysLimit = expirationCountdown * oneDay;

        if (expirationDate != null) {
            long diff = expirationDate.getTime() - unixTimestamp;
            String expireDateString = DatetimeUtils.toShortDate(expirationDate);

            if (diff > daysLimit) {
                output = expireDateString;

            } else if (diff > 0 && diff < oneDay) {
                output =  Localizer.getText(requestContext,
                        "contracts.expiration.counter.lessThanOne",
                        new Object[]{expireDateString});

            } else if (diff > 0) {
                output =  Localizer.getText(requestContext,
                        "contracts.expiration.counter.oneOrMore",
                        new Object[]{expireDateString, diff/oneDay});

            } else {
                output = Localizer.getText(requestContext,
                        "itMgmt.contractList.contract_expiration_date.expired",
                        new Object[]{expireDateString});
            }
        }
        return output;
    }
}
