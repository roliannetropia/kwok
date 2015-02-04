/*
 * ====================================================================
 * Copyright 2005-2014 Wai-Lun Kwok
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
package com.kwoksys.framework.struts2;

import com.kwoksys.biz.base.BaseForm;
import com.kwoksys.biz.system.core.AppPaths;
import com.kwoksys.framework.http.RequestContext;
import com.opensymphony.xwork2.ActionSupport;
import org.apache.struts.action.ActionMessages;
import org.apache.struts2.ServletActionContext;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

/**
 * BaseAction
 */
public class Action2 extends ActionSupport {

    public static final String STANDARD_TEMPLATE = "standard_template";
    public static final String STANDARD_AJAX_TEMPLATE = "standard_ajax_template";
    public static final String AJAX_TEMPLATE = "ajax_template";
    public static final String SUCCESS = "success";

    protected HttpServletRequest request;
    protected RequestContext requestContext;
    protected HttpServletResponse response = ServletActionContext.getResponse();

    private BaseForm actionForm;

    public Action2 setRequestContext(RequestContext requestContext) {
        this.requestContext = requestContext;
        this.request = requestContext.getRequest();
        return this;
    }

    public String redirect(String path) throws IOException {
        response.sendRedirect(AppPaths.ROOT + path);
        return null;
    }

    public void saveActionErrors(ActionMessages errors) {
        request.getSession().setAttribute(RequestContext.URL_PARAM_ERROR, errors);
        request.getSession().setAttribute(RequestContext.FORM_KEY, actionForm);
    }

    /**
     * Get BaseForm.
     * @param clazz
     * @param sessionKey
     * @return
     * @throws Exception
     */
    private BaseForm getBaseForm(Class clazz, String sessionKey) throws Exception {
        boolean resubmit = requestContext.getParameterBoolean(RequestContext.URL_PARAM_RESUBMIT);
        boolean error = requestContext.getParameterBoolean(RequestContext.URL_PARAM_ERROR);
        boolean preserveSession = !sessionKey.equals(RequestContext.FORM_KEY);
        BaseForm sessionActionForm = (BaseForm) request.getSession().getAttribute(sessionKey);
        boolean newForm = false;

        if (error || preserveSession) {
            actionForm = sessionActionForm;
        }

        if (actionForm == null) {
            actionForm = (BaseForm)clazz.newInstance();
            newForm = true;

            if (preserveSession) {
                request.getSession().setAttribute(sessionKey, actionForm);
            }
        }
        if (resubmit || newForm) {
            actionForm.setRequest(requestContext);
        }

        if (!preserveSession) {
            request.getSession().removeAttribute(sessionKey);
        }

        // Check both error and sessionActionForm. In case of a browser refresh, sessionActionForm may be null.
        actionForm.setResubmit((error && sessionActionForm != null) || resubmit);

        request.setAttribute(sessionKey, actionForm);
        return actionForm;
    }

    public BaseForm getBaseForm(Class clazz) throws Exception {
        return getBaseForm(clazz, RequestContext.FORM_KEY);
    }

    public BaseForm getSessionBaseForm(Class clazz) throws Exception {
        return getBaseForm(clazz, clazz.getSimpleName());
    }

    public <T> T saveActionForm(T actionForm) {
        this.actionForm = (BaseForm) actionForm;
        ((BaseForm)actionForm).setRequest(requestContext);
        return actionForm;
    }
}
