package com.adobe.aem.guides.wknd.spa.react.core.services.impl;

import com.adobe.aem.guides.wknd.spa.react.core.services.GetPagesService;
import com.adobe.aem.guides.wknd.spa.react.core.services.GetPagesTitleService;
import com.day.cq.wcm.api.Page;
import org.osgi.service.component.annotations.Reference;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;


import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

public class GetPagesTitleServiceImpl implements GetPagesTitleService {

    private static final Logger log = LoggerFactory.getLogger(GetPagesTitleServiceImpl.class);

    @Reference
    GetPagesService getPagesService;
    @Override
    public List<String> getPagesTitle() {
        List<String> pageList = new ArrayList<>();

        try {
            Iterator<Page> pageIterator = getPagesService.getPages();
            while (pageIterator.hasNext()){
                pageList.add(pageIterator.next().getTitle());
            }
            return pageList;

        }catch (Exception e){
            log.info("\n Exception : {} ", e.getMessage());
        }
        return null;
    }
}
