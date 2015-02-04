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
package com.kwoksys.action.software;

import com.kwoksys.biz.ServiceProvider;
import com.kwoksys.biz.software.SoftwareService;
import com.kwoksys.biz.system.dto.Bookmark;
import com.kwoksys.biz.system.core.AppPaths;
import com.kwoksys.framework.http.RequestContext;
import com.kwoksys.framework.struts2.Action2;
import org.apache.struts.action.ActionMessages;

/**
 * Action class for editing software bookmark.
 */
public class SoftwareBookmarkEdit2Action extends Action2 {

    public String execute() throws Exception {
        SoftwareService softwareService = ServiceProvider.getSoftwareService(requestContext);

        SoftwareBookmarkForm actionForm = saveActionForm(new SoftwareBookmarkForm());

        // Instantiate SoftwareBookmark class.
        Bookmark bookmark = softwareService.getSoftwareBookmark(actionForm.getSoftwareId(),
                actionForm.getBookmarkId());

        bookmark.setName(actionForm.getBookmarkName());
        bookmark.setPath(actionForm.getBookmarkPath());
        bookmark.setDescription("");

        ActionMessages errors = softwareService.updateSoftwareBookmark(bookmark);
        if (!errors.isEmpty()) {
            saveActionErrors(errors);
            return redirect(AppPaths.SOFTWARE_BOOKMARK_EDIT + "?softwareId=" + bookmark.getObjectId()
                    + "&bookmarkId=" + bookmark.getId() + "&" + RequestContext.URL_PARAM_ERROR_TRUE);
        } else {
            return redirect(AppPaths.SOFTWARE_BOOKMARK + "?softwareId=" + bookmark.getObjectId());
        }
    }
}
