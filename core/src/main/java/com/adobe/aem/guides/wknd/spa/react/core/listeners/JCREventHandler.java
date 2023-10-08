package com.adobe.aem.guides.wknd.spa.react.core.listeners;

import org.apache.sling.jcr.api.SlingRepository;
import org.osgi.service.component.annotations.Component;
import org.osgi.service.component.annotations.Reference;
import org.osgi.service.event.EventHandler;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.jcr.RepositoryException;
import javax.jcr.Session;
import javax.jcr.observation.Event;
import javax.jcr.observation.EventIterator;
import javax.jcr.observation.EventListener;

@Component(immediate = true, service = EventListener.class)
public class JCREventHandler implements EventListener {

    private final Logger log = LoggerFactory.getLogger(this.getClass());
    private Session session;
    @Reference
    SlingRepository slingRepository;


    public void activate() throws RepositoryException {

        session = slingRepository.loginService("aemserviceuser", null);
        session.getWorkspace().getObservationManager().addEventListener(
                this,
                Event.NODE_ADDED | Event.PROPERTY_ADDED,  // actions which trigger event
                "content/wknd-spa-react/us/en/home",        // any action on this JCR path
                true,                                       //deep
                null,
                null,
                false
        );
    }

    @Override
    public void onEvent(EventIterator eventIterator) {

        while (eventIterator.hasNext() && !eventIterator.hasNext()){
            if(eventIterator.nextEvent() != null){
                try {
                    log.info("\n Type:{}, path:{}", eventIterator.nextEvent().getType(),
                            eventIterator.nextEvent().getPath()
                    );
                } catch (RepositoryException e) {
                    log.error("\n error while handling event", e.getMessage());
                }
            }

        }

    }

}