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
package com.kwoksys.action.portal;

import com.kwoksys.biz.base.BaseTemplate;
import com.kwoksys.action.common.template.DetailTableTemplate;
import com.kwoksys.biz.portal.dto.Site;
import com.kwoksys.biz.system.core.Image;
import com.kwoksys.framework.properties.Localizer;
import com.kwoksys.framework.exceptions.DatabaseException;
import com.kwoksys.framework.ui.Link;
import com.kwoksys.framework.ui.WidgetUtils;
import com.kwoksys.framework.util.HtmlUtils;

/**
 * SiteSpecTemplate
 */
public class SiteSpecTemplate extends BaseTemplate {

    private DetailTableTemplate detailTableTemplate = new DetailTableTemplate();

    private Site site;

    public SiteSpecTemplate(Site site) {
        super(SiteSpecTemplate.class);
        this.site = site;

        addTemplate(detailTableTemplate);
    }

    public void applyTemplate() throws DatabaseException {
        DetailTableTemplate.Td td = detailTableTemplate.new Td();
        td.setHeaderKey("common.column.site_id");
        td.setValue(site.getId());
        detailTableTemplate.addTd(td);

        td = detailTableTemplate.new Td();
        td.setHeaderKey("common.column.site_name");
        td.setValue(HtmlUtils.encode(site.getName()));
        detailTableTemplate.addTd(td);

        td = detailTableTemplate.new Td();
        td.setHeaderKey("common.column.site_path");

        Link link = new Link(requestContext);
        link.setTitle(site.getPath());

        if (site.getSupportIframe() == 1) {
            link.setPath(site.getPath());
            link.setOnclick("showContent('iframeDiv');toggleIframe('iframe', '" + site.getPath() + "');");
        } else {
            link.setExternalPath(site.getPath());
            link.setImgSrc(Image.getInstance().getExternalPopupIcon());
            link.setImgAltKey("common.image.externalPopup");
            link.setImgAlignRight();
        }
        td.setValue(link.getString());
        detailTableTemplate.addTd(td);

        td = detailTableTemplate.new Td();
        td.setHeaderKey("common.column.site_description");
        td.setValue(HtmlUtils.formatMultiLineDisplay(site.getDescription()));
        detailTableTemplate.addTd(td);

        td = detailTableTemplate.new Td();
        td.setHeaderKey("common.column.site_category");
        td.setValue(HtmlUtils.encode(site.getCategoryName()));
        detailTableTemplate.addTd(td);

        td = detailTableTemplate.new Td();
        td.setHeaderKey("common.column.site_placement");
        td.setValue(Localizer.getText(requestContext, Site.getSitePlacementMessageKey(site.getPlacement())));
        detailTableTemplate.addTd(td);

        td = detailTableTemplate.new Td();
        td.setHeaderKey("common.column.site_support_iframe");
        td.setValue(Localizer.getText(requestContext, Site.getSupportIframe(site.getSupportIframe())));
        detailTableTemplate.addTd(td);

        td = detailTableTemplate.new Td();
        td.setHeaderKey("common.column.creator");
        td.setValue(WidgetUtils.formatCreatorInfo(requestContext, site.getCreationDate(), site.getCreator()));
        detailTableTemplate.addTd(td);

        td = detailTableTemplate.new Td();
        td.setHeaderKey("common.column.modifier");
        td.setValue(WidgetUtils.formatCreatorInfo(requestContext, site.getModificationDate(), site.getModifier()));
        detailTableTemplate.addTd(td);
    }

    @Override
    public String getJspPath() {
        return "/WEB-INF/jsp/portal/SiteSpecTemplate.jsp";
    }
}