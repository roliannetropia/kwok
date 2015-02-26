package com.kwoksys.action.tape;

import com.kwoksys.action.common.template.HeaderTemplate;
import com.kwoksys.action.common.template.StandardTemplate;
import com.kwoksys.action.tape.TapeSearchForm;
import com.kwoksys.action.tape.TapeSearchTemplate;
import com.kwoksys.biz.ServiceProvider;
import com.kwoksys.biz.admin.dto.AccessUser;
import com.kwoksys.biz.admin.dto.AttributeField;
import com.kwoksys.biz.admin.dto.AttributeFieldCount;
import com.kwoksys.biz.auth.core.Access;
import com.kwoksys.biz.tape.TapeService;
import com.kwoksys.biz.tape.dao.TapeQueries;
import com.kwoksys.biz.system.core.AppPaths;
import com.kwoksys.biz.system.core.AttributeManager;
import com.kwoksys.biz.system.core.Attributes;
import com.kwoksys.biz.system.core.Links;
import com.kwoksys.framework.connections.database.QueryBits;
import com.kwoksys.framework.properties.Localizer;
import com.kwoksys.framework.session.SessionManager;
import com.kwoksys.framework.struts2.Action2;
import com.kwoksys.framework.ui.Link;
import com.kwoksys.framework.ui.RowStyle;

import javax.servlet.http.HttpSession;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Action class for tape index page.
 */
public class IndexAction extends Action2 {

    public String execute() throws Exception {
        AccessUser user = requestContext.getUser();
        HttpSession session = request.getSession();

        getSessionBaseForm(TapeSearchForm.class);

        // Link to tape list.
        List links = new ArrayList();
        if (Access.hasPermission(user, AppPaths.TAPE_LIST)) {
            Map linkMap;

            if (session.getAttribute(SessionManager.TAPE_SEARCH_CRITERIA_MAP) != null) {
                linkMap = new HashMap();
                linkMap.put("urlPath", AppPaths.TAPE_LIST);
                linkMap.put("urlText", Localizer.getText(requestContext, "common.search.showLastSearch"));
                links.add(linkMap);
            }

            linkMap = new HashMap();
            linkMap.put("urlPath", AppPaths.TAPE_LIST + "?cmd=showAll");
            linkMap.put("urlText", Localizer.getText(requestContext, "itMgmt.index.showAllTape"));
            links.add(linkMap);
        }

        // Get the number of records.
        TapeService tapeService = ServiceProvider.getTapeService(requestContext);
        int numTapeRecords = tapeService.getTapeCount(new QueryBits());

        AttributeManager attributeManager = new AttributeManager(requestContext);

        /*
         * Count Tape section
         */
        // We'll use the same queryBits for a few different queries below
        QueryBits query = new QueryBits();
        query.addSortColumn(TapeQueries.getOrderByColumn(AttributeField.NAME));

        Map attrFieldTypeMap = attributeManager.getAttrFieldMapCache(Attributes.MEDIA_TYPE);
        Map attrFieldSystemMap = attributeManager.getAttrFieldMapCache(Attributes.TAPE_SYSTEM);
        Map attrFieldStatusMap = attributeManager.getAttrFieldMapCache(Attributes.TAPE_STATUS);
        Map attrFieldLocMap = attributeManager.getAttrFieldMapCache(Attributes.TAPE_LOCATION);

//        Group by Media type.
        List mediaTypeCounts = new ArrayList();
        List<AttributeFieldCount> mediaTypeData = tapeService.getMediaTypeCount(query);
        RowStyle ui = new RowStyle();

        for (AttributeFieldCount tape : mediaTypeData) {
            AttributeField attrField = (AttributeField) attrFieldTypeMap.get(tape.getAttrFieldId());
            String mediaTypeName = attrField != null ? Links.getAttrFieldIcon(requestContext, attrField).getString() : Localizer.getText(requestContext, "itMgmt.index.na");

            Map map = new HashMap();
            map.put("countKey", mediaTypeName);
            map.put("countValue", tape.getObjectCount());
            map.put("style", ui.getRowClass());
            map.put("path", AppPaths.ROOT + AppPaths.TAPE_LIST + "?cmd=groupBy&mediaType=" + tape.getAttrFieldId());
            mediaTypeCounts.add(map);
        }

        // Group by Tape system.
        List tapeSystemCounts = new ArrayList();
        List<AttributeFieldCount> tapeSystemData = tapeService.getTapeSystemCount(query);
        ui = new RowStyle();

        for (AttributeFieldCount tape : tapeSystemData) {
            AttributeField attrField = (AttributeField) attrFieldSystemMap.get(tape.getAttrFieldId());
            String tapeSystemName = attrField != null ? attrField.getName() : Localizer.getText(requestContext, "itMgmt.index.na");

            Map map = new HashMap();
            map.put("countKey", tapeSystemName);
            map.put("countValue", tape.getObjectCount());
            map.put("style", ui.getRowClass());
            map.put("path", AppPaths.ROOT + AppPaths.TAPE_LIST + "?cmd=groupBy&tapeSystem=" + tape.getAttrFieldId());
            tapeSystemCounts.add(map);
        }

        // Group by Tape status.
        List tapeStatusCounts = new ArrayList();
        List<AttributeFieldCount> tapeStatusData = tapeService.getTapeStatusCount(query);
        ui = new RowStyle();

        for (AttributeFieldCount tape : tapeStatusData) {
            AttributeField attrField = (AttributeField) attrFieldStatusMap.get(tape.getAttrFieldId());
            String tapeStatusName = attrField != null ? attrField.getName() : Localizer.getText(requestContext, "itMgmt.index.na");

            Map map = new HashMap();
            map.put("countKey", tapeStatusName);
            map.put("countValue", tape.getObjectCount());
            map.put("style", ui.getRowClass());
            map.put("path", AppPaths.ROOT + AppPaths.TAPE_LIST + "?cmd=groupBy&tapeStatus=" + tape.getAttrFieldId());
            tapeStatusCounts.add(map);
        }

        // Group by Tape location.
        List locationCounts = new ArrayList();
        List<AttributeFieldCount> tapeLocationData = tapeService.getTapeLocationCount(query);
        ui = new RowStyle();

        for (AttributeFieldCount tape : tapeLocationData) {
            AttributeField attrField = (AttributeField) attrFieldLocMap.get(tape.getAttrFieldId());
            String tapeLocName = attrField != null ? attrField.getName() : Localizer.getText(requestContext, "itMgmt.index.na");

            Map map = new HashMap();
            map.put("countKey", tapeLocName);
            map.put("countValue", tape.getObjectCount());
            map.put("style", ui.getRowClass());
            map.put("path", AppPaths.ROOT + AppPaths.TAPE_LIST + "?cmd=groupBy&tapeLocation=" + tape.getAttrFieldId());
            locationCounts.add(map);
        }

        //
        // Template: StandardTemplate
        //
        StandardTemplate standardTemplate = new StandardTemplate(requestContext);
        standardTemplate.setAttribute("numTapeRecords", numTapeRecords);
        standardTemplate.setAttribute("linkList", links);
        standardTemplate.setAttribute("mediaTypeCountList", mediaTypeCounts);
        standardTemplate.setAttribute("tapeLocationCountList", locationCounts);
        standardTemplate.setAttribute("tapeSystemCounts", tapeSystemCounts);
        standardTemplate.setAttribute("tapeStatusCounts", tapeStatusCounts);

        //
        // Template: HeaderTemplate
        //
        HeaderTemplate header = standardTemplate.getHeaderTemplate();
        header.setTitleKey("core.moduleName.16");
        header.setTitleClassNoLine();
        
        // Link to add tape.
        if (Access.hasPermission(user, AppPaths.TAPE_ADD)) {
            Link link = new Link(requestContext);
            link.setAjaxPath(AppPaths.TAPE_ADD);
            link.setTitleKey("itMgmt.cmd.tapeAdd");
            header.addHeaderCmds(link);
        }

        //
        // Template: TapeSearchTemplate
        //
        TapeSearchTemplate searchTemplate = new TapeSearchTemplate();
        standardTemplate.addTemplate(searchTemplate);
        searchTemplate.setFormAction(AppPaths.TAPE_LIST);
//        todo media type
        searchTemplate.setMediaTypeData(mediaTypeData);
        searchTemplate.setTapeLocationData(tapeLocationData);
        searchTemplate.setTapeSystemData(tapeSystemData);
        searchTemplate.setTapeStatusData(tapeStatusData);

        return standardTemplate.findTemplate(STANDARD_TEMPLATE);
    }
}
