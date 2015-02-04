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
package com.kwoksys.framework.servlets;

import com.kwoksys.biz.scheduler.FetchIssueMailTask;
import com.kwoksys.biz.scheduler.SampleTask;
import com.kwoksys.biz.system.core.configs.ConfigManager;
import com.kwoksys.biz.system.core.configs.LogConfigManager;
import org.quartz.*;
import org.quartz.impl.StdSchedulerFactory;

import javax.servlet.ServletConfig;
import javax.servlet.http.HttpServlet;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * This is to control the trigger and scheduled jobs.
 */
public class SchedulerServlet extends HttpServlet {

    private static final Logger logger = Logger.getLogger(SchedulerServlet.class.getName());
    private Scheduler scheduler;

    public void init(ServletConfig cfg) {
        try {
            logger.info(LogConfigManager.SCHEDULER_PREFIX + " Scheduling jobs...");

            SchedulerFactory factory = new StdSchedulerFactory();
            scheduler = factory.getScheduler();
            scheduler.start();

            // Scheduling SampleTask
            JobDetail sampleTask = JobBuilder.newJob(SampleTask.class)
                    .withIdentity("sampleTask")
                    .build();

            Trigger sampleTrigger = TriggerBuilder.newTrigger()
                    .withIdentity("sampleTrigger")
                    .startNow()
                    .withSchedule(SimpleScheduleBuilder.simpleSchedule()
                        .withIntervalInSeconds(300)
                        .repeatForever())
                    .build();

            scheduler.scheduleJob(sampleTask, sampleTrigger);

            // Scheduling FetchIssueMailTask, only schedule the task if repeatInterval is not 0.
            int repeatInterval = ConfigManager.email.getPopRepeatInterval();

            if (repeatInterval != 0) {
                JobDetail fetchIssueMailTask = JobBuilder.newJob(FetchIssueMailTask.class)
                        .withIdentity("fetchIssueMailTask")
                        .build();

                Trigger issueMailTrigger = TriggerBuilder.newTrigger()
                        .withIdentity("fetchIssueMailTrigger")
                        .startNow()
                        .withSchedule(SimpleScheduleBuilder.simpleSchedule()
                            .withIntervalInMilliseconds(ConfigManager.email.getPopRepeatInterval())
                            .repeatForever())
                        .build();

                scheduler.scheduleJob(fetchIssueMailTask, issueMailTrigger);
            }

            logger.info(LogConfigManager.SCHEDULER_PREFIX + " Scheduler started.");

        } catch (Exception e) {
            logger.log(Level.SEVERE, LogConfigManager.SCHEDULER_PREFIX + " Failed to start scheduler.", e);
        }

        logger.info(LogConfigManager.CONFIG_PREFIX + " Kwok application started successfully");
    }

    public void destroy() {
        try {
            if (scheduler != null) {
                scheduler.shutdown();
            }
            logger.info(LogConfigManager.SHUTDOWN_PREFIX + " Scheduler shutdown.");

        } catch (Exception e) {
            logger.log(Level.WARNING, LogConfigManager.SHUTDOWN_PREFIX + " Error shutting down scheduler.", e);
        }
    }
}
