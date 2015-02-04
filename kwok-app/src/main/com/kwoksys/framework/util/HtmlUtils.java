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

import com.kwoksys.biz.system.core.Image;
import com.kwoksys.framework.http.RequestContext;
import com.kwoksys.framework.ui.Link;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

public class HtmlUtils {

    /**
     * Creates a Link with a given URL.
     *
     * @return ..
     */
    public static String formatExternalLink(RequestContext requestContext, String url) {
        if (!StringUtils.isEmpty(url)) {
            return new Link(requestContext).setExternalPath(url).setTitle(url).setImgSrc(Image.getInstance().getExternalPopupIcon()).setImgAlignRight().getString();
        } else {
            return "";
        }
    }

    /**
     * This is to take an email address and convert it to a link.
     *
     * @param emailAddress
     * @return ..
     */
    public static String formatMailtoLink(String emailAddress) {
        if (emailAddress == null || emailAddress.isEmpty()) {
            return "";
        } else {
            return "<a href=\"mailto:" + encode(emailAddress) + "\" target=\"_blank\">" + encode(emailAddress) + "</a>";
        }
    }

    public static String encode(String input) {
        if (input == null || input.isEmpty()) {
            return "";
        }
        return encodeStringBuffer(new StringBuffer(input)).toString();
    }

    public static List encode(List<String> values) {
        List list = new ArrayList();
        if (values != null) {
            for (Iterator<String> iter = values.iterator(); iter.hasNext();) {
                String value = iter.next();
                iter.remove();
                list.add(encode(value));
            }
        }
        return list;
    }

    public static StringBuffer encodeStringBuffer(StringBuffer input) {
        if (input == null || input.length() == 0) {
            return new StringBuffer();
        }

        StringBuffer buffer = new StringBuffer();
        for (int i = 0; i < input.length(); i++) {
            char c = input.charAt(i);
            switch (c) {
                case '<':
                    buffer.append("&lt;");
                    break;
                case '>':
                    buffer.append("&gt;");
                    break;
                case '&':
                    buffer.append("&amp;");
                    break;
                case '"':
                    buffer.append("&quot;");
                    break;
                default:
                    buffer.append(c);
            }
        }
        return buffer;
    }

    /**
     * Replaces line breaks with html break tag.
     *
     * @param input
     * @return ..
     */
    public static String formatMultiLineDisplay(String input) {
        return input == null ? "" : encode(input).replace("\n", "<br>");
    }

    /**
     * Removes all html contents for a given string.
     * @param input
     * @return
     */
    public static String removeHtmlTags(String input) {
        return input == null ? "" : input.replace("\n", "").replaceAll("\\<script.*?\\</script\\>", "").replaceAll("\\<.*?\\>","");
    }
}
