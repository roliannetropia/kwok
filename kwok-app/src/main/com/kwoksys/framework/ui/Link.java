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
package com.kwoksys.framework.ui;

import com.kwoksys.biz.system.core.AppPaths;
import com.kwoksys.framework.http.RequestContext;
import com.kwoksys.framework.properties.Localizer;
import com.kwoksys.framework.util.HtmlUtils;

/**
 * LinkHelper
 */
public class Link {

    private RequestContext requestContext;
    private String title;
    private String path;
    private boolean imgAlignRight = false;
    private String imgSrc = "";
    private String imgAlt = "";
    private String imgAltText = "";
    private int imgWidth;
    private int imgHeight;
    private String target;
    private String styleClass;
    private String onclick;

    public Link(RequestContext requestContext) {
        this.requestContext= requestContext;
    }

    @Deprecated
    public String toString() {
        return getString();
    }

    public String getString() {
        StringBuilder link = new StringBuilder();

        if (!imgSrc.isEmpty()) {
            link.append("<img src=\"").append(imgSrc).append("\" class=\"standard\" alt=\"").append(imgAltText)
                    .append("\" title=\"").append(imgAltText).append("\"");

            if (imgWidth != 0 && imgHeight != 0) {
                link.append(" width=\"").append(imgWidth).append("\" height=\"").append(imgHeight).append("\"");
            }

            link.append(">");
        }

        if (title != null) {
            if (imgAlignRight) {
                link.insert(0, title);
            } else {
                link.append(title);
            }
        }

        if (path != null) {
            StringBuilder sb = new StringBuilder();
            if (target != null) {
                sb.append(" target=\"" + target + "\"");
            }
            if (styleClass != null) {
                sb.append(" class=\"" + styleClass + "\"");
            }
            if (onclick != null) {
                sb.append(" onclick=\"" + onclick + "\"");
            }
            link.insert(0, "<a href=\"" + path + "\"" + sb.toString() + ">");
            link.append("</a>");
        }
        return link.toString();
    }

    /**
     * Set both href and onclick paths, using javascript for onclick event.
     *
     * "return false;" is needed to get rid of the spinning image on IE.
     */
    public Link setAjaxPath(String path) {
        this.path = AppPaths.ROOT + path;
        onclick = "updateViewHistory('content',this.href);return false;";
        return this;
    }

    /**
     * For export.
     * @param path
     */
    public Link setExportPath(String path) {
        this.path = AppPaths.ROOT + path;
        this.target = "_blank";
        return this;
    }

    public Link setAppPath(String path) {
        this.path = AppPaths.ROOT + path;
        return this;
    }

    public Link setPath(String path) {
        this.path = path;
        return this;
    }

    public Link setJavascript(String javascript) {
        path = "javascript:void(0);";
        onclick = javascript;
        return this;
    }

    public Link setExternalPath(String path) {
        this.path = HtmlUtils.encode(path);
        this.target = "_blank";
        return this;
    }

    public String getOnclick() {
        return onclick;
    }

    public Link setOnclick(String onclick) {
        this.onclick = onclick;
        return this;
    }

    public Link setTitle(String title) {
        this.title = HtmlUtils.encode(title);
        return this;
    }

    public Link setEscapeTitle(String title) {
        this.title = title;
        return this;
    }

    public Link setTitleKey(String contentKey) {
        this.title = Localizer.getText(requestContext, contentKey);
        return this;
    }

    public Link setImgSrc(String iconPath) {
        this.imgSrc = iconPath;
        return this;
    }

    public Link setAppImgSrc(String iconPath) {
        this.imgSrc = AppPaths.ROOT + iconPath;
        return this;
    }

    public String getPath() {
        return path;
    }

    public String getImgSrc() {
        return imgSrc;
    }

    public String getTarget() {
        return target;
    }

    public String getImgAlt() {
        return imgAlt;
    }

    public Link setImgAlt(String imgAlt) {
        this.imgAlt = imgAlt;
        return this;
    }

    public String getImgAltText() {
        return imgAltText;
    }

    public void setImgAltText(String imgAltText) {
        this.imgAltText = imgAltText;
    }

    public void setImgAltKey(String imgAltKey) {
        this.imgAltText = Localizer.getText(requestContext, imgAltKey);
    }

    public void setImgSize(int width, int height) {
        this.imgWidth = width;
        this.imgHeight = height;
    }

    public Link setStyleClass(String styleClass) {
        this.styleClass = styleClass;
        return this;
    }

    public Link setImgAlignRight() {
        this.imgAlignRight = true;
        return this;
    }
}
