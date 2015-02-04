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
package com.kwoksys.biz.scheduler;

import com.kwoksys.biz.ServiceProvider;
import com.kwoksys.biz.issues.IssueService;
import com.kwoksys.framework.http.RequestContext;
import com.kwoksys.biz.system.core.configs.ConfigManager;
import com.kwoksys.biz.system.core.configs.LogConfigManager;
import com.kwoksys.framework.connections.mail.PopConnection;
import com.kwoksys.framework.servlets.SystemInitServlet;
import org.quartz.Job;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;

import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * Fetch issue emails.
 */
public class FetchIssueMailTask implements Job {

    private static final Logger logger = Logger.getLogger(FetchIssueMailTask.class.getName());

    public void execute(JobExecutionContext context) throws JobExecutionException {
        if (!SystemInitServlet.init) {
            logger.info(LogConfigManager.SCHEDULER_PREFIX + " Fetching issue emails... system not initialized, skipping");
            return;
        }

        // Creates a pop connection object
        PopConnection conn = new PopConnection();
        conn.setHost(ConfigManager.email.getPopHost());
        conn.setPort(ConfigManager.email.getPopPort());
        conn.setUsername(ConfigManager.email.getPopUsername());
        conn.setPassword(ConfigManager.email.getPopPassword());
        conn.setMessagesLimit(ConfigManager.email.getPopMessagesLimit());
        conn.setSenderIgnoreList(ConfigManager.email.getPopSenderIgnoreList());

        if (conn.isConfigured()) {
            logger.log(LogConfigManager.getLogLevel(LogConfigManager.SCHEDULER_PREFIX), LogConfigManager.SCHEDULER_PREFIX + " Fetching issue emails...");
            try {
                IssueService issueService = ServiceProvider.getIssueService(new RequestContext());
                issueService.retrieveIssueEmails(conn);

            } catch (Exception e) {
                logger.log(Level.WARNING, LogConfigManager.SCHEDULER_PREFIX + " Problem fetching emails ", e);
            }
        } else {
            logger.log(LogConfigManager.getLogLevel(LogConfigManager.SCHEDULER_PREFIX), LogConfigManager.SCHEDULER_PREFIX + " Fetching issue emails... not configured, skipping");
        }
    }
}