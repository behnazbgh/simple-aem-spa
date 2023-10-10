package com.adobe.aem.guides.wknd.spa.react.core.listeners;


import com.adobe.aem.guides.wknd.spa.react.core.utils.ResolverUtil;
import org.apache.sling.api.SlingConstants;
import org.apache.sling.api.resource.Resource;
import org.apache.sling.api.resource.ResourceResolver;
import org.apache.sling.api.resource.ResourceResolverFactory;
import org.osgi.service.component.annotations.Component;
import org.osgi.service.component.annotations.Reference;
import org.osgi.service.event.Event;
import org.osgi.service.event.EventConstants;
import org.osgi.service.event.EventHandler;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import javax.jcr.Node;

@Component(
        service = EventHandler.class,
        immediate = true,
        property = {
                EventConstants.EVENT_TOPIC +"=org/apache/sling/api/resource/Resource/ADDED",
                EventConstants.EVENT_TOPIC + "=org/apache/sling/api/resource/Resource/CHANGED",
                EventConstants.EVENT_TOPIC + "=org/apache/sling/api/resource/Resource/REMOVED",
                EventConstants.EVENT_FILTER + "=(path=/content/wknd-spa-react/us/en/home/*",
        }
)
public class OSGIEventHandler implements EventHandler {

    private final Logger log = LoggerFactory.getLogger(this.getClass());

    @Reference
    ResourceResolverFactory resourceResolverFactory;

    @Override
    public void handleEvent(Event event) {

        try {
            ResourceResolver resourceResolver = ResolverUtil.newResolver(resourceResolverFactory);
            Resource resource = resourceResolver.getResource(event.getProperty(SlingConstants.PROPERTY_PATH).toString());
            Node node = resource.adaptTo(Node.class);
            node.setProperty("eventHandlerTask", "Even " + event.getTopic()+ " by " + resourceResolver.getUserID());
        } catch (Exception e) {
            throw new RuntimeException(e);
        }

        log.info("Resource event", event.getTopic(), event.getProperty(SlingConstants.PROPERTY_PATH));

    }
}
