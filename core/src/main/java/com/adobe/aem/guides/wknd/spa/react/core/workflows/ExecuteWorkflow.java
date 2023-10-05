package com.adobe.aem.guides.wknd.spa.react.core.workflows;

import com.day.cq.workflow.WorkflowException;
import com.day.cq.workflow.WorkflowSession;
import com.day.cq.workflow.exec.WorkflowData;
import com.day.cq.workflow.model.WorkflowModel;
import org.apache.commons.lang3.StringUtils;
import org.apache.sling.api.SlingHttpServletRequest;
import org.apache.sling.api.SlingHttpServletResponse;
import org.apache.sling.api.resource.ResourceResolver;
import org.apache.sling.api.servlets.SlingSafeMethodsServlet;
import org.apache.sling.servlets.annotations.SlingServletPaths;
import org.osgi.service.component.annotations.Component;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import javax.servlet.Servlet;
import javax.servlet.ServletException;
import java.io.IOException;

@Component(service = Servlet.class)
@SlingServletPaths(
        value = {"/bin/executeworkflow", "/aem/executeworkflow"}
)
public class ExecuteWorkflow extends SlingSafeMethodsServlet {

    private final Logger log = LoggerFactory.getLogger(this.getClass());
    String status = "Workflow Executing";

    @Override
    protected void doGet(final SlingHttpServletRequest req,
                         final SlingHttpServletResponse resp) throws ServletException, IOException {

        final ResourceResolver resourceResolver = req.getResourceResolver();
        String payload = req.getRequestParameter("page").getString();
        log.info("-----payload======" + payload);
        try {
        if(StringUtils.isNotBlank(payload)) {
            WorkflowSession workflowSession = resourceResolver.adaptTo(WorkflowSession.class);
            WorkflowModel workflowModel = workflowSession.getModel("/var/workflow/models/page-version");
            log.info("-----workflowmodel======" + workflowModel);
            WorkflowData workflowData = workflowSession.newWorkflowData("JCR_PATH", payload);
            status = workflowSession.startWorkflow(workflowModel, workflowData).getState();
        }
            } catch (WorkflowException e) {
               log.info("\n Error in workflow", e.getMessage());
            }

            resp.setContentType("application/json");
            resp.getWriter().write(status);
        }
    }

