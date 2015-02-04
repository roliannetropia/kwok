/*
 * ====================================================================
 * Copyright 2005-2012 Wai-Lun Kwok
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
import com.kwoksys.biz.system.core.AppPaths;
import com.kwoksys.biz.system.core.Image;
import com.kwoksys.biz.system.core.configs.LogConfigManager;
import com.kwoksys.framework.http.RequestContext;
import com.kwoksys.framework.properties.Localizer;
import com.kwoksys.framework.struts2.Action2;
import com.opensymphony.xwork2.config.entities.ActionConfig;
import com.opensymphony.xwork2.config.entities.ResultConfig;

import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * RootTemplate
 */
public abstract class RootTemplate extends BaseTemplate {

    private static final Logger logger = Logger.getLogger(RootTemplate.class.getName());

    private List<String> jspTemplates = new ArrayList();

    protected String templatePath;

    protected String templateName = Action2.SUCCESS;

    protected boolean ajax;

    private boolean autoGenerateTemplate;

    /**
     * To support same template used multiple times.
     */
    private String prefix;

    public RootTemplate(Class clazz, RequestContext requestContext) {
        super(clazz);
        setRequestContext(requestContext);
    }

    public String findTemplate(String templateName) throws Exception {
        if (ajax && templateName.equals(Action2.STANDARD_TEMPLATE)) {
            templateName = Action2.STANDARD_AJAX_TEMPLATE;
        }
        applyTemplate();
        return templateName;
    }

    private void applyTemplates(List<BaseTemplate> templates, boolean autoGenerateTemplate) throws Exception {
        for (BaseTemplate template : templates) {
            template.setRequestContext(requestContext);
            template.applyTemplate();

            request.setAttribute(template.getName()
                    + (template.getPrefix() == null ? "" : template.getPrefix()), template);

            if (autoGenerateTemplate) {
                if (template.getJspPath() != null) {
                    String jspPath = template.getJspPath();
                    logger.log(LogConfigManager.getLogLevel(LogConfigManager.TEMPLATE_PREFIX), LogConfigManager.TEMPLATE_PREFIX + " Include: " + jspPath);
                    jspTemplates.add(jspPath);
                } else {
                    logger.log(Level.WARNING, LogConfigManager.TEMPLATE_PREFIX + " Missing template jsp: " + template);
                }
            }
            applyTemplates(template.getTemplates(), false);
        }
    }

    /**
     * Applies this template
     * @throws Exception
     */
    public void applyTemplate() throws Exception {
        applyTemplates(getTemplates(), autoGenerateTemplate);

        request.setAttribute(name, this);
        request.setAttribute("image", Image.getInstance());
        request.setAttribute("path", AppPaths.getInstance());
        request.setAttribute(RootTemplate.class.getSimpleName(), this);

        // Get pageStartTime from servlet servlet
        Long pageStartTime = (Long) request.getAttribute(RequestContext.PAGE_START_TIME);

        if (pageStartTime != null && requestContext.getUser().isLoggedOn()) {

            request.setAttribute(RequestContext.PAGE_EXEC_TIME, Localizer.getText(requestContext, "common.pageExecutionTime",
                new Object[]{new DecimalFormat("#0.000").format((double) (System.currentTimeMillis() - pageStartTime) / 1000)}));
        }

        ActionConfig actionConfig = requestContext.getActionConfig();
        if (actionConfig != null) {
            Map<String, ResultConfig> resultsMap = actionConfig.getResults();

            ResultConfig resultConfig = resultsMap.get(templateName);

            if (resultConfig != null) {
                templatePath = resultConfig.getParams().get("location");
            }
        }
    }

    public boolean isAutoGenerateTemplate() {
        return autoGenerateTemplate;
    }

    public void setAutoGenerateTemplate(boolean autoGenerateTemplate) {
        this.autoGenerateTemplate = autoGenerateTemplate;
    }

    public List<String> getJspTemplates() {
        return jspTemplates;
    }

    public void setAttribute(String key, Object value) {
        request.setAttribute(key, value);
    }

    public void setPathAttribute(String key, String value) {
        request.setAttribute(key, AppPaths.ROOT + value);
    }

    public void setTemplateName(String templateName) {
        this.templateName = templateName;
    }

    public String getTemplatePath() {
        return templatePath;
    }

    public String getPrefix() {
        return prefix;
    }

    public void setPrefix(String prefix) {
        this.prefix = prefix;
    }

    public boolean isAjax() {
        return ajax;
    }
}