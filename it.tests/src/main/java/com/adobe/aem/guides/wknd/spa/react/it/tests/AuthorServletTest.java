package com.adobe.aem.guides.wknd.spa.react.core.servlets;

import io.wcm.testing.mock.aem.junit5.AemContext;
import io.wcm.testing.mock.aem.junit5.AemContextExtension;
import org.apache.sling.api.SlingHttpServletRequest;
import org.apache.sling.api.SlingHttpServletResponse;
import org.apache.sling.testing.mock.sling.servlet.MockSlingHttpServletRequest;
import org.apache.sling.testing.mock.sling.servlet.MockSlingHttpServletResponse;
import org.junit.Before;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.apache.sling.api.resource.Resource;
import org.junit.jupiter.api.extension.ExtendWith;

import javax.servlet.ServletException;

import java.io.IOException;
import java.io.PrintWriter;
import java.io.StringWriter;

import static junit.framework.Assert.assertEquals;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

@ExtendWith(AemContextExtension.class)
public class AuthorServletTest {

    // AEM related objects not available at build time, with AemContext can get all the objects
    AemContext aemContext = new AemContext();

    private AuthorServlet authorServlet;

    @BeforeEach
    public void setup() {
        authorServlet = new AuthorServlet();
        //create a mock jcr resource with given property
        aemContext.build().resource("/content/spa/test" , "jcr:title", "servlet page");
        aemContext.currentResource("/content/spa/test");
    }

    @Test
    public void testDoGet() throws ServletException, IOException {

        MockSlingHttpServletRequest request = aemContext.request();
        MockSlingHttpServletResponse response = aemContext.response();

        // Call the doGet method of the servlet with mock resp and req
        authorServlet.doGet(request, response);

        assertEquals(" This is servlet page", response.getOutputAsString());
    }
}