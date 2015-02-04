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

import com.kwoksys.framework.parsers.wiki.generic.MatchingTags;
import com.kwoksys.framework.parsers.wiki.generic.SingleTag;
import com.kwoksys.framework.parsers.wiki.mediawiki.*;

import java.util.*;

/**
 * MediawikiParser
 */
public class MediawikiParser extends Parser {

    public static List<Tag> list =  new ArrayList();
    
    private Map nowikiMap = new HashMap();

    private int externalLinkCounter = 1;

    static {
        // Strip <nowiki> tags
        NowikiStripTag stripTag = new NowikiStripTag("<nowiki>(([\\S\\s\r\n])*?)</nowiki>");
        stripTag.setTags("<nowiki>", "</nowiki>");
        stripTag.setRestoreGroup(1);
        list.add(stripTag);

        stripTag = new NowikiStripTag("<pre(.*?)>(([\\S\\s\r\n])*?)</pre>");
        stripTag.setTags("<pre$1>", "</pre>");
        stripTag.setRestoreGroup(2);
        list.add(stripTag);

        Tag tag = new HtmlEncodeTag();
        list.add(tag);

        // Restore allowed HTML tags
        tag = new HtmlRestoreTag("&lt;(i|b|del|s|blockquote|(br(|( *?/)|/))" +
                "|((code|div|font|span|table|td|tr)(.*?))" +
                "|((/)(i|b|del|s|blockquote|code|font|div|span|table|td|tr)))&gt;");
        list.add(tag);

        tag = new MatchingTags("'''''(.+?)'''''");
        tag.setExample("'''''bold & italic'''''");
        tag.setTags("<i><b>", "</b></i>");
        list.add(tag);

        tag = new MatchingTags("'''(.+?)'''");
        tag.setExample("'''bold'''");
        tag.setTags("<b>", "</b>");
        list.add(tag);

        tag = new MatchingTags("''(.+?)''");
        tag.setExample("''italic''");
        tag.setTags("<i>", "</i>");
        list.add(tag);

        tag = new MatchingTags("^=====(.+?)=====");
        tag.setExample("=====level 5=====");
        tag.setTags("<h5>", "</h5>");
        list.add(tag);

        tag = new MatchingTags("^====(.+?)====");
        tag.setExample("====level 4====");
        tag.setTags("<h4>", "</h4>");
        list.add(tag);

        tag = new MatchingTags("^===(.+?)===");
        tag.setExample("===level 3===");
        tag.setTags("<h3>", "</h3>");
        list.add(tag);

        tag = new MatchingTags("^==(.+?)==");
        tag.setExample("==level 2==");
        tag.setTags("<h2 class=\"mediawikiH2\">", "</h2>");
        list.add(tag);

        tag = new MatchingTags("^=(.+?)=");
        tag.setExample("=level 1=");
        tag.setTags("<h1 class=\"mediawikiH1\">", "</h1>");
        list.add(tag);

        tag = new SingleTag("^----");
        tag.setExample("----");
        tag.setTag("<hr/>");
        list.add(tag);

        tag = new MatchingTags("^\\*(.+)$");
        tag.setExample("* one");
        tag.setTags("<li>", "\n</li>");
        list.add(tag);

        tag = new SingleTag("^<li>");
        tag.setTag("<ul><li>");
        list.add(tag);

        tag = new SingleTag("</li>$");
        tag.setTag("</li></ul>");
        list.add(tag);

        tag = new SingleTag("^$");
        tag.setTag("\n<p>");
        list.add(tag);

        tag = new NestedLinkTag("^ (([\\S\\s\r\n])*?)(\n[^ ])");
        tag.setExample(" preformatted text is done with a space at the beginning of the line");
        tag.setTags("<pre>", "\n</pre>$3");
        list.add(tag);

        tag = new InternalLabelLinkTag("\\[\\[(.+?)\\|(.+?)\\]\\]");
        tag.setExample("[[Main Page|different text]]");
        list.add(tag);

        tag = new InternalLinkTag("\\[\\[(.+?)\\]\\]");
        tag.setExample("[[Main Page]]");
        list.add(tag);

        tag = new ExternalLinkTag("(^|.)((https|http|ftp)://.+?)($|[^\\w\\.:/\\?=&#;])");
        tag.setExample("http://mediawiki.org");
        list.add(tag);

        tag = new ExternalLabelLinkTag("\\[((https|http|ftp)://.+?) (.+?)\\]");
        tag.setExample("[http://mediawiki.org MediaWiki]");
        list.add(tag);

        tag = new ExternalNumberLinkTag("\\[((https|http|ftp)://.+?)\\]");
        tag.setExample("[http://mediawiki.org]");
        list.add(tag);

        // Put <nowiki> tags back
        NowikiRestoreTag restoreTag = new NowikiRestoreTag("&lt;nowiki&gt;([0-9]+)&lt;/nowiki&gt;");
        restoreTag.setRestoreGroup(1);
        restoreTag.setTags("", "");
        list.add(restoreTag);

        restoreTag = new NowikiRestoreTag("&lt;pre(.*?)&gt;([0-9]+)&lt;/pre&gt;");
        restoreTag.setRestoreGroup(2);
        restoreTag.setTags("<pre$1>", "</pre>");
        list.add(restoreTag);
    }

    public String parseHtml(String content) {
        StringBuffer sb = new StringBuffer(content);
        for (Tag tag : list) {
            sb = tag.parseContent(sb, this);
        }
        return sb.toString();
    }

    /**
     * Returns protocol prefix, e.g. http, https, etc.
     * @param prefix
     * @return
     */
    public static String getLinkCssClass(String prefix) {
        if ("https".equals(prefix) || "http".equals(prefix) || "ftp".equals(prefix)) {
            return "link_" + prefix;
        }
        return "";
    }

    public int incrExternalLinkLevel() {
        return externalLinkCounter++;
    }

    public Map getNowikiMap() {
        return nowikiMap;
    }
}