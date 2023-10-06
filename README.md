# AEM Guides - SPA Project

This is the simple AEM project integrated with React:

The develop branch is the most updated one.

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

## How AEM integrated with React

1- Create a simple component called Author under '/apps/wknd-spa-react/components/author' which contains input field and a checkbox.
2- we need to create a sling model to inject the user data to the Java objects. For this purpose, 'Autor.java' class is created under 'core/models.
    This class extends the ComponentExporter interface which serialize the AEM component to Json type which then can be used by SPA at client side. This             interface has one method called 'getExportedType' which return resource type.
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
    
    ```MapTo('wknd-spa-react/components/author')(Author, CustomEditConfig);```

5- after deploying this code, we would be able to see changes into component appears right a way on the front end.





    
