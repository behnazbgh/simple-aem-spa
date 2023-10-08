package com.adobe.aem.guides.wknd.spa.react.core.utils;

import org.apache.sling.api.resource.LoginException;
import org.apache.sling.api.resource.ResourceResolver;
import org.apache.sling.api.resource.ResourceResolverFactory;

import java.util.HashMap;
import java.util.Map;

public final class ResolverUtil {

    public static final String AEM_SERVICE_USER = "aemserviceuser";

    public static ResourceResolver newResolver( ResourceResolverFactory resourceResolverFactory) throws LoginException {

        final Map<String, Object> paramMap = new HashMap<String, Object>();
        paramMap.put(ResourceResolverFactory.SUBSERVICE, AEM_SERVICE_USER);
        ResourceResolver resourceResolver = resourceResolverFactory.getServiceResourceResolver(paramMap);
        return resourceResolver;

    }
}
