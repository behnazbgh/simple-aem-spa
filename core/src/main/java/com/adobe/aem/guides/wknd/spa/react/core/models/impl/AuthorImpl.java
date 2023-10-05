package com.adobe.aem.guides.wknd.spa.react.core.models.impl;

import com.adobe.aem.guides.wknd.spa.react.core.models.Author;
import com.adobe.cq.export.json.ComponentExporter;
import com.adobe.cq.export.json.ExporterConstants;
import org.apache.commons.lang3.StringUtils;
import org.apache.sling.api.SlingHttpServletRequest;
import org.apache.sling.api.adapter.Adaptable;
import org.apache.sling.models.annotations.DefaultInjectionStrategy;
import org.apache.sling.models.annotations.Exporter;
import org.apache.sling.models.annotations.Model;
import org.apache.sling.models.annotations.injectorspecific.ValueMapValue;

@Model(
        adaptables = SlingHttpServletRequest.class,
        adapters ={
                Author.class,
                ComponentExporter.class
        },
        resourceType = AuthorImpl.RESOURCE_TYPE,
        defaultInjectionStrategy = DefaultInjectionStrategy.OPTIONAL
        )
@Exporter(name= ExporterConstants.SLING_MODEL_EXPORTER_NAME, extensions = ExporterConstants.SLING_MODEL_EXTENSION)
public class AuthorImpl implements Author {

    static final String RESOURCE_TYPE = "wknd-spa-react/components/author";

    @ValueMapValue
    private String firstName;

    @ValueMapValue
    private String lastName;

    @ValueMapValue
    private Boolean isAemDeveloper;

    @Override
    public String getFirstName() {
        return StringUtils.isNotBlank(firstName) ? firstName.toUpperCase() : "NA";
    }

    @Override
    public String getLastName() {
        return StringUtils.isNotBlank(lastName) ? lastName.toUpperCase() : "NA";
    }

    @Override
    public Boolean getIsAemDeveloper() {
        return isAemDeveloper;
    }

    @Override
    public String getExportedType() {
        return AuthorImpl.RESOURCE_TYPE;
    }
}
