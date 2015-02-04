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

import org.apache.commons.codec.DecoderException;
import org.apache.commons.codec.EncoderException;
import org.apache.commons.codec.binary.Base64;
import org.apache.commons.codec.net.BCodec;
import org.apache.commons.lang.StringEscapeUtils;

import java.util.Collection;
import java.util.List;
import java.util.Map;
import java.util.logging.Logger;
import java.util.regex.Matcher;

/**
 * This is for String manipulation methods.
 */
public class StringUtils {
    private static final Logger logger = Logger.getLogger(StringUtils.class.getName());

    /**
     * This is for taking an objects. If the objects is null, return the defaultValue.
     *
     * @param temp
     * @param defaultValue
     * @return ..
     */
    public static String replaceNull(Object temp, String defaultValue) {
        if (temp == null || temp.toString().trim().isEmpty()) {
            return defaultValue;

        } else {
            // By default, we trim spaces.
            return temp.toString().trim();
        }
    }

    /**
     * This is for taking an objects. If the objects is null, return the defaultValue.
     */
    public static String replaceNull(Object temp) {
        return replaceNull(temp, "");
    }

    /**
     * Encodes CKEditor characters. Any single quote with a blackward slash and a single quote.
     * Ckeditor requires encoding a certain characters for it to display correctly.
     */
    public static String encodeCkeditorValue(String input) {
        if (input == null) {
            return "";
        }
        return input.replace("\\", "\\\\").replace("'", "\\'");
    }

    /**
     * Encodes VCard characters.
     * @param input
     * @return
     */
    public static String encodeVCard(String input) {
        if (input == null || input.isEmpty()) {
            return "";
        }
        // Don't know what to encode.
        return input;
    }

    /**
     * Matcher class can reference a group with a leading dollar sign. Not encoding the dollar sign could cause
     * illegal group reference exception.
     * @return
     */
    public static String encodeMatcherReplacement(String input) {
        return Matcher.quoteReplacement(input);
    }

    /**
     * Encode a string to base64. Supports UTF8.
     * @return
     */
    public static String encodeBase64Codec(String input) {
        BCodec codec = new BCodec();
        try {
            return codec.encode(input);

        } catch (EncoderException e) {
            logger.warning("Problem with base64 encoding.");
            return "";
        }
    }

    /**
     * Decode base64-encoded string. Supports UTF8.
     * @return
     */
    public static String decodeBase64Codec(String input) {
        if (input.isEmpty()) {
            return "";
        }
        BCodec codec = new BCodec();
        try {
            return codec.decode(input);

        } catch (DecoderException e) {
            logger.warning("Problem with base64 decoding.");
            return "";
        }
    }

    /**
     * Decode base64-encoded string.
     * @return
     */
    public static String decodeBase64(String input) {
        return new String(Base64.decodeBase64(input.getBytes()));
    }

    public static String join(String[] strings, String token) {
        // The joining is tricky as it needs to loop through a String[] and
        // skip empty elements inside the String[]
        if (strings == null || strings.length == 0) {
            return "";
        }

        StringBuilder sb = new StringBuilder();
        boolean appendToken = false;

        for (String string : strings) {
            if (!string.isEmpty()) {
                if (appendToken) {
                    sb.append(token);
                }
                sb.append(string);
                // Now, we have the first element, next element should have a comma before it.
                appendToken = true;
            }
        }
        return sb.toString();
    }

    /**
     * Joins a list of strings.
     * @param strings
     * @param token
     * @return
     */
    public static String join(Collection<String> strings, String token) {
        return join(strings.toArray(new String[strings.size()]), token);
    }

    public static String joinIntegers(List<Integer> integers, String token) {
        // The joining is tricky as it needs to loop through a String[] and
        // skip empty elements inside the String[]
        if (integers == null || integers.isEmpty()) {
            return "";
        }

        StringBuilder buffer = new StringBuilder();
        boolean appendToken = false;

        for (Integer integer : integers) {
            if (appendToken) {
                buffer.append(token);
            }
            buffer.append(integer);
            // Now, we have the first element, next element should have a comma before it.
            appendToken = true;
        }
        return buffer.toString();
    }

    /**
     * Joins a list of maps.
     *
     * @param list
     * @param token
     * @return ..
     */
    public static String join(List<Map> maps, String key, String token) {
        StringBuilder buffer = new StringBuilder();
        boolean appendToken = false;

        for (Map map : maps) {
            if (!map.isEmpty()) {
                if (appendToken) {
                    buffer.append(token);
                }
                buffer.append(map.get(key));
                appendToken = true;
            }
        }
        return buffer.toString();
    }
    
    public static String decodeHtml(String input) {
        return StringEscapeUtils.unescapeHtml(input);
    }

    public static boolean isEmpty(String value) {
        return value == null || value.isEmpty();
    }
}
