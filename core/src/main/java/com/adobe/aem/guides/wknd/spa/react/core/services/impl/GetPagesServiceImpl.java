package com.adobe.aem.guides.wknd.spa.react.core.services.impl;

import com.adobe.aem.guides.wknd.spa.react.core.services.GetPagesService;
import com.adobe.aem.guides.wknd.spa.react.core.utils.ResolverUtil;
import com.day.cq.wcm.api.Page;
import com.day.cq.wcm.api.PageManager;
import org.apache.sling.api.resource.LoginException;
import org.apache.sling.api.resource.ResourceResolver;
import org.apache.sling.api.resource.ResourceResolverFactory;
import org.apache.sling.jcr.api.SlingRepository;
import org.osgi.service.component.ComponentContext;
import org.osgi.service.component.annotations.Activate;
import org.osgi.service.component.annotations.Component;
import org.osgi.service.component.annotations.Reference;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Iterator;


@Component(service = GetPagesService.class, immediate = true)
public class GetPagesServiceImpl implements GetPagesService {

    private static final Logger log = LoggerFactory.getLogger(GetPagesServiceImpl.class);

    @Reference
    ResourceResolverFactory resourceResolverFactory;

    @Reference
    SlingRepository slingRepository;

    @Activate
    public void activate(ComponentContext componentContext){
    log.info(componentContext.getBundleContext().getBundle().getSymbolicName(),
            componentContext.getBundleContext().getBundle().getBundleId()
                );

    }
    @Override
    public Iterator<Page> getPages() {
        try {
            ResourceResolver resourceResolver = ResolverUtil.newResolver(resourceResolverFactory);
            PageManager pageManager = resourceResolver.adaptTo(PageManager.class);
            Page page = pageManager.getPage("content/wknd-spa-react/us/en/home");
            Iterator<Page> pages = page.listChildren();
            return pages;
        }catch (LoginException e){
            log.info("Login Exception" , e.getMessage());
        }

        return null;
    }
}
