package com.adobe.aem.guides.wknd.spa.react.core.schedulers;


import org.osgi.service.metatype.annotations.AttributeDefinition;
import org.osgi.service.metatype.annotations.AttributeType;
import org.osgi.service.metatype.annotations.ObjectClassDefinition;



@ObjectClassDefinition(name="A scheduled task",
        description = "Sling scheduler configuration ")
public @interface SchedulerConfiguration {

    @AttributeDefinition(
            name = "scheduler name",
            description = "Name of the scheduler",
            type = AttributeType.STRING
    )
    public String schedulerName() default "Sling Scheduler Configuration";

    @AttributeDefinition(
            name = "Crone Expression",
            description = "used by scheduler",
            type = AttributeType.STRING
    )
    public String cronExpression() default "0/20 * * * * ?";
}
