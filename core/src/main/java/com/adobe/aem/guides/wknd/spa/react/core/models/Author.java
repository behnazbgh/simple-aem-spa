package com.adobe.aem.guides.wknd.spa.react.core.models;

import com.adobe.cq.export.json.ComponentExporter;

public interface Author extends ComponentExporter {

    String getFirstName();
    String getLastName();

    Boolean getIsAemDeveloper();
}
