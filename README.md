# Simple AEM-SPA Demo Project

This is the simple AEM project integrated with React:

The develop branch is the most updated one.

## How AEM integrated with React

1- Create a simple component called Author under `/apps/wknd-spa-react/components/author` which contains input field and a checkbox.
2- we need to create a sling model to inject the user data to the Java objects. For this purpose, 'Autor.java' class is created under 'core/models.
    This class extends the ComponentExporter interface which serialize the AEM component to Json type which then can be used by SPA at client side. This interface has one method called 'getExportedType' which return resource type.
    
3- Implementation class is created and annotated with @Model so sling knows this is the sling model class.
```
@Model(
        adaptables = SlingHttpServletRequest.class,
        adapters ={
                Author.class,
                ComponentExporter.class
        },
        resourceType = AuthorImpl.RESOURCE_TYPE,
        defaultInjectionStrategy = DefaultInjectionStrategy.OPTIONAL
        )
```
here SlingHttpServletRequest is adapted to sling model class and component exporter interface.    

in this class the resource type is defined as well which is the component:
```
static final String RESOURCE_TYPE = "wknd-spa-react/components/author
```

4- create Author.js under ui.frontend,
    in this class the data will come into 'props', and after checking if it not empty, render the data on AEM page which contains this component.
    The important part of this class is the mapping, with MapTo , AEM component map to the React component.
    
    ```
    MapTo('wknd-spa-react/components/author')(Author, CustomEditConfig);
    
    ```

5- after deploying this code, we would be able to see changes into component appears right a way on the front end.

## AEM GET/POST Servlet

AuthorServlet.java:
this class extends `SlingSafeMethodsServlet` which only GET method is allowd, the class is annotated with `@Component` which is the way the class is registered into OSGI framework, so OSGI initilize the servlet at the start of AEM instance and keep it ready for Sling FW. Sling FW knows which method to execute based on the path, and then actual execution of the servlet happens in the serverlet container.

```
@Component(service = { Servlet.class })
@SlingServletResourceTypes(
        resourceTypes="wknd-spa-react/components/page",
        methods=HttpConstants.METHOD_GET,
        extensions="txt")
@ServiceDescription("Author Servlet")
```

this is example is bind to `sling:resourceType`, means any resource in AEM with the given path, can trigger the servlet.

PostServlet.java:
Since this is POST method, this class extends `SlingAllMethodsServlet`.

The annotation looks like this:

```
@Component(
        service = Servlet.class,
        property = {
                "sling.servlet.paths=/bin/my-servlet",
                "sling.servlet.methods=" + HttpConstants.METHOD_POST
        })
```

*** POST method can be triggered from Postman.

## AEM Scheduler

with the help of Scheduler, the java code can be executed based on different configuration which is OSGI configuration management. for this reason, there are two classes created `SchedulerConfiguration` and `ScheduledTask`

SchedulerConfiguration is resposible to define the items of configuration 

```
@ObjectClassDefinition(name="A scheduled task",
        description = "Sling scheduler configuration ")

 @AttributeDefinition(
            name = "scheduler name",
            description = "Name of the scheduler",
            type = AttributeType.STRING
    )
    public String schedulerName() default "Sling Scheduler Configuration";

    @AttributeDefinition(
            name = "Crone Expression",
            description = "used by scheduler",
            type = AttributeType.STRING
    )
    public String cronExpression() default "0/20 * * * * ?";
}

```

ScheduledTask class extends `Runnable`, through addScheduler() scheduler is added.

```
@Activate
    protected void activate(SchedulerConfiguration schedulerConfiguration){
        schedulerId = schedulerConfiguration.schedulerName().hashCode();
        addScheduler(schedulerConfiguration);

    }
private void addScheduler(SchedulerConfiguration schedulerConfiguration) {
        ScheduleOptions scheduleOptions = scheduler.EXPR(schedulerConfiguration.cronExpression());
        scheduleOptions.name(String.valueOf(schedulerId));
        scheduleOptions.canRunConcurrently(false);
        scheduler.schedule(this, scheduleOptions);

        log.info("\n ------- Schedule added --------");

    }
```
through ScheduleOptions, we can specify variuos scheduling parameter like name, cron expression and whethere this job can be concurrent with other jobs or no.

## AEM Workflow

This is the example of AEM workflow triggered with GET request. we pass a payload through our request which is the page path in AEM and we want to execute this workflow on that page.

in this code, workflowSession is used.

```
 WorkflowSession workflowSession = resourceResolver.adaptTo(WorkflowSession.class);
            WorkflowModel workflowModel = workflowSession.getModel("/var/workflow/models/page-version");
            WorkflowData workflowData = workflowSession.newWorkflowData("JCR_PATH", payload);
            status = workflowSession.startWorkflow(workflowModel, workflowData).getState();
```

ResourceResolver is mapped to workflowSession, workflow model is retrieved from `/var/workflow/models/page-version` (This workflow created from interface and can do page versioning), workflow data is defined as payload which the path of the page.

## Unit testing in AEM

There are 4 dependencies involve in AEM testin: JUnit5
Mockito Test Framework
Apache Sling Mocks
AEM Mocks Test Framework (by io.wcm)

in this project there is class called `AuthorServletTest.java` which annotated by `@ExtendWith(AemContextExtension.class)`.
Test class has a setup() which annotates with @BeforeEach, meaning it runs before any other test method.so we can initialize all required mock objects here.

```
 @BeforeEach
    public void setup() {
        authorServlet = new AuthorServlet();
        //create a mock jcr resource with given property
        aemContext.build().resource("/content/spa/test" , "jcr:title", "servlet page");
        aemContext.currentResource("/content/spa/test");
    }
```

*** we need to create instance of AemContext because AEM related objects are not available on build time.
    through aemContext we create a mock jcr resource with the expected property.

` AemContext aemContext = new AemContext();`

Next is test on the main method of our servlet:

```
@Test
    public void testDoGet() throws ServletException, IOException {

        MockSlingHttpServletRequest request = aemContext.request();
        MockSlingHttpServletResponse response = aemContext.response();

        // Call the doGet method of the servlet with mock resp and req
        authorServlet.doGet(request, response);

        assertEquals(" This is servlet page", response.getOutputAsString());
    }
```
*** Mock resp and req are available through aemContext instance.

```
MockSlingHttpServletRequest request = aemContext.request();
MockSlingHttpServletResponse response = aemContext.response();
```

at last assertEqual method compare the actual result with the expected one, and if it's true then test is passed.

## Event Listener (JCR Event Handling)
Event handling done by different methods in AEM, this one is listen to the event happen at JCR nodes.
For this part, aem service user need to be created and granted the required permission. Then it should be mapped to the bundle which needs to use this service user to run the code. For this there is factory setting i OSGI called `sling service user mapper `. In the code our class need to implements EventListener and its method called `onEvent()`. 

1- Create a session and login to JCR via system user, after access to JCR, through `getObservationManager()`, event is added with required properties.

```
@Reference
SlingRepository slingRepository;
private Session session;

session = slingRepository.loginService("aemserviceuser", null);
session.getWorkspace().getObservationManager().addEventListener(
                this,
                Event.NODE_ADDED | Event.PROPERTY_ADDED,
                "content/wknd-spa-react/us/en/home",
                true,
                null,
                null,
                false
        );
```

## Event Listener (OSGI Event Handling)
This event handler is preffered over JCR even handler, because in JCR, event is type iterator, and each time the event is triggered, we need to iterate over it to get the information, but in OSGI, the event is a single object.
In OSGI event handler class, we annotate the class like below:

```
@Component(
        service = EventHandler.class,
        immediate = true,
        property = {
                EventConstants.EVENT_TOPIC +"=org/apache/sling/api/resource/Resource/ADDED",
                EventConstants.EVENT_TOPIC + "=org/apache/sling/api/resource/Resource/CHANGED",
                EventConstants.EVENT_TOPIC + "=org/apache/sling/api/resource/Resource/REMOVED",
                EventConstants.EVENT_FILTER + "=(path=/content/wknd-spa-react/us/en/home/*",
        }
)
```
Topics are defined based on different sort of changes on the node. through filter we specify on which path, we are monitoring the events.
Again here need to obtain the resourceResolver on behalf of the system user which we defined. (Unitlity class is provided for this where it return ResourceResolver on behalf of the system user).

```
  @Override
    public void handleEvent(Event event) {

        try {
            ResourceResolver resourceResolver = ResolverUtil.newResolver(resourceResolverFactory);
            Resource resource = resourceResolver.getResource(event.getProperty(SlingConstants.PROPERTY_PATH).toString());
            Node node = resource.adaptTo(Node.class);
            node.setProperty("eventHandlerTask", "Even " + event.getTopic()+ " by " + resourceResolver.getUserID());
        } catch (Exception e) {
            throw new RuntimeException(e);
        }

        log.info("Resource event", event.getTopic(), event.getProperty(SlingConstants.PROPERTY_PATH));

    }
```

Once we get hold of resource resolver, we can access nodes on JCR and change the properties or add other nodes.


## Modules

The main parts of the project are:

* **core**: Java bundle containing all core functionality like OSGi services, listeners or schedulers, as well as component-related Java code such as servlets or request filters.
* **ui.apps**: contains the /apps (and /etc) parts of the project, ie JS&CSS clientlibs, components, templates and runmode specific configs
* **ui.content**: contains sample content using the components from the ui.apps
* **ui.tests**: Java bundle containing JUnit tests that are executed server-side. This bundle is not to be deployed onto production.
* **ui.frontend**: an optional dedicated front-end build mechanism. Depending on the branch this will be either the **React** or **Angular** source code.

## How to build

To build all the modules run in the project root directory the following command with Maven 3:

    mvn clean install

If you have a running AEM instance you can build and package the whole project and deploy into AEM with

    mvn clean install -PautoInstallSinglePackage

Or to deploy it to a publish instance, run

    mvn clean install -PautoInstallSinglePackagePublish

### Building for AEM 6.x.x

The project has been designed for **AEM as a Cloud Service**. The project is also backward compatible with AEM **6.4.8** by adding the `classic` profile when executing a build, i.e:

    mvn clean install -PautoInstallSinglePackage -Pclassic
    
