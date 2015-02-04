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
package com.kwoksys.action.common.template;

import com.kwoksys.biz.base.BaseTemplate;
import com.kwoksys.framework.http.RequestContext;
import com.kwoksys.framework.properties.Localizer;
import com.kwoksys.framework.util.HtmlUtils;
import org.apache.struts.action.ActionMessage;
import org.apache.struts.action.ActionMessages;

import java.util.Iterator;
import java.util.LinkedHashSet;
import java.util.Set;

/**
 * ActionErrorsTemplate
 */
public class ActionErrorsTemplate extends BaseTemplate {

    private boolean showRequiredFieldMsg;

    private String message;

    public ActionErrorsTemplate() {
        super(ActionErrorsTemplate.class);
    }

    public void applyTemplate() throws Exception {
        ActionMessages errors = (ActionMessages) requestContext.getRequest().getSession().getAttribute(RequestContext.URL_PARAM_ERROR);

        if (errors != null) {
            // Remove errors from session and copy it over to request.
            requestContext.getRequest().getSession().removeAttribute(RequestContext.URL_PARAM_ERROR);
            if (requestContext.getParameterBoolean(RequestContext.URL_PARAM_ERROR)) {

                Set set = new LinkedHashSet();

                for (Iterator propIter = errors.properties(); propIter.hasNext();) {
                    for (Iterator errorIter = errors.get((String)propIter.next()); errorIter.hasNext();) {
                        ActionMessage actionMessage = (ActionMessage) errorIter.next();
                        set.add(HtmlUtils.encode(Localizer.getText(requestContext, actionMessage.getKey(), actionMessage.getValues())));
                    }
                }
                requestContext.getRequest().setAttribute(RequestContext.URL_PARAM_ERROR, set);
            }
        }
    }

    @Override
    public String getJspPath() {
        return "/WEB-INF/jsp/common/template/ActionError.jsp";
    }

    public boolean isShowRequiredFieldMsg() {
        return showRequiredFieldMsg;
    }

    public void setShowRequiredFieldMsg(boolean showRequiredFieldMsg) {
        this.showRequiredFieldMsg = showRequiredFieldMsg;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }
}
