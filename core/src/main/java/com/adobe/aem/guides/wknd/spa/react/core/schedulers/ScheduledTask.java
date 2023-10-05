/*
 *  Copyright 2015 Adobe Systems Incorporated
 *
 *  Licensed under the Apache License, Version 2.0 (the "License");
 *  you may not use this file except in compliance with the License.
 *  You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 *  Unless required by applicable law or agreed to in writing, software
 *  distributed under the License is distributed on an "AS IS" BASIS,
 *  WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 *  See the License for the specific language governing permissions and
 *  limitations under the License.
 */
package com.adobe.aem.guides.wknd.spa.react.core.schedulers;

import org.apache.sling.commons.scheduler.ScheduleOptions;
import org.apache.sling.commons.scheduler.Scheduler;
import org.osgi.service.component.annotations.Activate;
import org.osgi.service.component.annotations.Component;
import org.osgi.service.component.annotations.Deactivate;
import org.osgi.service.component.annotations.Reference;
import org.osgi.service.metatype.annotations.Designate;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * A simple demo for cron-job like tasks that get executed regularly.
 * It also demonstrates how property values can be set. Users can
 * set the property values in /system/console/configMgr
 */
@Designate(ocd=SchedulerConfiguration.class)
@Component(immediate = true, service=Runnable.class)
public class ScheduledTask implements Runnable {

    private final Logger log = LoggerFactory.getLogger(this.getClass());
    private int schedulerId;
    @Reference
    private Scheduler scheduler;

    @Activate
    protected void activate(SchedulerConfiguration schedulerConfiguration){
        schedulerId = schedulerConfiguration.schedulerName().hashCode();
        addScheduler(schedulerConfiguration);

    }

    @Deactivate
    protected void deactivate(SchedulerConfiguration schedulerConfiguration){
        removeScheduler();
    }

    private void removeScheduler() {
        scheduler.unschedule(String.valueOf(schedulerId));
    }

    private void addScheduler(SchedulerConfiguration schedulerConfiguration) {
        ScheduleOptions scheduleOptions = scheduler.EXPR(schedulerConfiguration.cronExpression());
        scheduleOptions.name(String.valueOf(schedulerId));
        scheduleOptions.canRunConcurrently(false);
        scheduler.schedule(this, scheduleOptions);

        log.info("\n ------- Schedule added --------");

    }

    @Override
    public void run() {
    log.info("\n =========== Run Method Executing ===========");
    }
}
