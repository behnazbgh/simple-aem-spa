package com.adobe.aem.guides.wknd.spa.react.core.models.impl;

import com.adobe.aem.guides.wknd.spa.react.core.models.GetPages;
import com.adobe.aem.guides.wknd.spa.react.core.services.GetPagesService;
import com.adobe.aem.guides.wknd.spa.react.core.services.GetPagesTitleService;
import com.day.cq.wcm.api.Page;
import org.apache.sling.api.SlingHttpServletRequest;
import org.apache.sling.api.adapter.Adaptable;
import org.apache.sling.models.annotations.DefaultInjectionStrategy;
import org.apache.sling.models.annotations.Model;
import org.apache.sling.models.annotations.injectorspecific.OSGiService;
import org.osgi.service.component.annotations.Reference;

import java.util.Iterator;
import java.util.List;
import java.util.Optional;

@Model(adaptables = SlingHttpServletRequest.class,
adapters = GetPages.class, defaultInjectionStrategy = DefaultInjectionStrategy.OPTIONAL
)
public class GetPagesImpl implements GetPages {

    @OSGiService
    GetPagesService getPagesService;
    @Reference
    GetPagesTitleService getPagesTitleService;
    @Override
    public Iterator<Page> getPages() {
        return getPagesService.getPages();
    }

    @Override
    public List<String> getPagesTitle() {
        return getPagesTitleService.getPagesTitle();
    }
}
