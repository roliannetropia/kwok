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

/**
 * AbstractTag
 */
public abstract class Tag {

    private StringBuffer content;
    private String regex;

    private String openTag;
    private String closeTag;
    private String tag;
    private String example;

    public Tag() {}

    public Tag(String regex) {
        this.regex = regex;
    }

    public abstract StringBuffer parseContent(StringBuffer content, Parser parser);

    public void setTags(String openTag, String closeTag) {
        this.openTag = openTag;
        this.closeTag = closeTag;
    }

    public StringBuffer getContent() {
        return content;
    }

    public void setContent(StringBuffer content) {
        this.content = content;
    }

    public String getRegex() {
        return regex;
    }

    public String getOpenTag() {
        return openTag;
    }

    public void setOpenTag(String openTag) {
        this.openTag = openTag;
    }

    public String getCloseTag() {
        return closeTag;
    }

    public void setCloseTag(String closeTag) {
        this.closeTag = closeTag;
    }

    public String getTag() {
        return tag;
    }

    public void setTag(String tag) {
        this.tag = tag;
    }

    public String getExample() {
        return example;
    }

    public void setExample(String example) {
        this.example = example;
    }
}
