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
package com.kwoksys.framework.parsers.wiki.mediawiki;

import com.kwoksys.biz.kb.KbUtils;
import com.kwoksys.biz.system.core.AttributeFieldIds;
import com.kwoksys.framework.parsers.wiki.MediawikiParser;
import com.kwoksys.framework.parsers.wiki.Parser;
import com.kwoksys.framework.parsers.wiki.Tag;
import com.kwoksys.framework.util.StringUtils;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * InternalLabelLinkTag
 */
public class InternalLabelLinkTag extends Tag {

    public InternalLabelLinkTag(String regex) {
        super(regex);
    }

    public StringBuffer parseContent(StringBuffer content, Parser parser) {
        StringBuffer sb = new StringBuffer();
        Matcher m = Pattern.compile(getRegex(), Pattern.MULTILINE).matcher(content);
        while (m.find()) {
            String replace = "<a href=\"" + MediawikiParser.WIKI_URL
                    + KbUtils.formatWikiNamespace(AttributeFieldIds.KB_ARTICLE_SYNTAX_MEDIAWIKI, m.group(1)) + "\">" + m.group(2) + "</a>";
            m.appendReplacement(sb, StringUtils.encodeMatcherReplacement(replace));
        }
        m.appendTail(sb);
        return sb;
    }
}