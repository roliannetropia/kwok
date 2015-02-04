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
package com.kwoksys.framework.util;

import com.kwoksys.biz.system.core.configs.ConfigManager;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.TimeZone;

/**
 * We do some datetime conversion here.
 */
public class DatetimeUtils {

    public static GregorianCalendar newCalendar() {
        return new GregorianCalendar(TimeZone.getTimeZone(ConfigManager.system.getTimezoneBase()));
    }

    public static String createDatetimeString(String year, String month, String date) {
        if (year.isEmpty() || month.isEmpty() || date.isEmpty()) {
            return "";
        } else {
            return year + "-" + month + "-" + date + " 00:00:00";
        }
    }

    public static boolean isValidDate(String year, String month, String date) {
        try {
            GregorianCalendar gc = newCalendar();
            // must do this, so that the month/date don't wrap
            gc.setLenient(false);
            gc.set(GregorianCalendar.YEAR, Integer.parseInt(year));
            // There is a -1 because month start with 0
            gc.set(GregorianCalendar.MONTH, Integer.parseInt(month)-1);
            gc.set(GregorianCalendar.DATE, Integer.parseInt(date));

            gc.getTime();
            return true;
        } catch (Exception e) {
            return false;
        }
    }

    public static boolean isValidDateValue(String fieldValue) {
        boolean valid = true;
            try {
                DatetimeUtils.toDate(fieldValue, ConfigManager.system.getDateFormat());
            } catch (Exception e) {
                valid = false;
            }
        return valid;
    }

    /**
     * Gets Date object from resultset with GMT timezone.
     * @param datetime
     * @return
     */
    public static Date getDate(ResultSet rs, String column) throws SQLException {
        return rs.getTimestamp(column, newCalendar());
    }

    public static String toLocalDate(Date date) {
        return toLocalString(date, ConfigManager.system.getDateFormat());
    }

    public static String toLocalDatetime(Date date) {
        return toLocalString(date, ConfigManager.system.getDateFormat() + " " + ConfigManager.system.getTimeFormat());
    }

    public static String toShortDate(Date date) {
        return toString(date, ConfigManager.system.getDateFormat());
    }

    public static String toShortDate(long milliseconds) {
        return toShortDate(longToDate(milliseconds));
    }

    public static String toYearString(Date date) {
        return toString(date, "yyyy");
    }

    public static String toMonthString(Date date) {
        return toString(date, "MM");
    }

    public static String toDateString(Date date) {
        return toString(date, "dd");
    }

    private static String toLocalString(Date date, String pattern) {
        if (date == null) {
            return "";
        }

        // Set the timezone.
        TimeZone localTimezone = TimeZone.getTimeZone(ConfigManager.system.getTimezoneLocal());
        localTimezone.getDSTSavings();

        // Set it to the local timezone
        DateFormat dateFormat = new SimpleDateFormat(pattern);
        dateFormat.setTimeZone(localTimezone);

        return dateFormat.format(date);
    }

    private static String toString(Date date, String pattern) {
        if (date == null) {
            return "";
        }
        DateFormat dateFormat = new SimpleDateFormat(pattern);
        dateFormat.setTimeZone(TimeZone.getTimeZone(ConfigManager.system.getTimezoneBase()));
        
        return dateFormat.format(date);
    }

    /**
     * Converts datetime string to Date object with GMT timezone.
     * @param datetime
     * @return
     */
    public static Date toDate(String datetime, String pattern) throws Exception {
        if (datetime == null || datetime.isEmpty()) {
            return null;
        }

        // Set it to the base timezone
        DateFormat dateFormat = new SimpleDateFormat(pattern);

        // must do this, so that the month/date don't wrap
        dateFormat.setLenient(false);
        dateFormat.setTimeZone(TimeZone.getTimeZone(ConfigManager.system.getTimezoneBase()));

        return dateFormat.parse(datetime);
    }

    /**
     * Converts milliseconds to Date object.
     * @param milliseconds
     * @return
     */
    public static Date longToDate(long milliseconds) {
        Calendar calendar = newCalendar();
        calendar.setTimeInMillis(milliseconds);
        return calendar.getTime();
    }
}
