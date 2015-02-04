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
package com.kwoksys.framework.parsers.wiki;

import com.kwoksys.framework.util.HtmlUtils;
import com.kwoksys.biz.system.core.AppPaths;

import java.util.Map;
import java.util.HashMap;
import java.util.regex.Pattern;
import java.util.regex.Matcher;

/**
 * Parser
 */
public abstract class Parser {

    public static final String WIKI_URL = AppPaths.KB_ARTICLE_DETAIL + "?title=";

    private Map options;

    public abstract String parseHtml(String content);

    public void setOption(String propName, String propValue) {
        if (options == null) {
            options = new HashMap();
        }
        options.put(propName, propValue);
    }

    public Map getOptions() {
        return options;
    }

    /**
     * Replaces line breaks with an html BR tag.
     * @param content
     * @return
     */
    public static String replaceLineBreaks(String content) {
        return content == null ? "" : content.replace("\n", "<br>");
    }

    /**
     * Encodes content to be html friendly.
     * @param content
     * @return
     */
    public static String htmlEncode(String content) {
        return HtmlUtils.encode(content);
    }

    /**
     * Converts http, https, etc to html href link.
     * @param fieldValue
     * @return
     */
    public static String convertUrls(String fieldValue) {
        String regex = "(?<!\")((http|https|ftp)://[^\\s]+)(?!\")";
        Pattern p = Pattern.compile(regex, Pattern.CASE_INSENSITIVE);
        Matcher m = p.matcher(fieldValue);
        if (m.find()) {
            fieldValue = m.replaceAll("<a href=\"$1\" target=\"_blank\">$1</a>");
        }
        return fieldValue;
    }
}