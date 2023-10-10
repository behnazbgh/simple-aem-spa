package com.adobe.aem.guides.wknd.spa.react.core.models;

import com.day.cq.wcm.api.Page;

import java.util.Iterator;
import java.util.List;

public interface GetPages {

    public Iterator<Page> getPages();
    public List<String> getPagesTitle();
}
