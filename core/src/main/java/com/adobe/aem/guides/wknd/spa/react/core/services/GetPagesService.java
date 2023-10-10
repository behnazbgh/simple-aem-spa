package com.adobe.aem.guides.wknd.spa.react.core.services;

import com.day.cq.wcm.api.Page;

import java.util.Iterator;

public interface GetPagesService {

    public Iterator<Page> getPages();
}
