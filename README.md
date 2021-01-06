# AmiiboRepo

### Design

![final-architecture](https://user-images.githubusercontent.com/7444521/103432363-a4204080-4bab-11eb-8482-30557d54cfb6.png)

We decided to go with our Google's recommended architecture using Android Architecture Components.  Most commonly this is MVVM design pattern aligned with Clean Architecture.


A unidirectional, reactive flow will ensure that because things are decoupled further with each components only dependent on the one "below" it in the diagram.  Modulatization becomes easier as well if we continue to scale the application and parts of our application need to be portable across functional boundaries.  As modularity and reusability remains our focus we will be able to increase the speed at which we add new features. Another important principle is that we should drive our UI from a model, preferably a persistent model.


Developing our application along these design patterns keeps us from designating our app's entry points—such as activities, services, and broadcast receivers—as sources of data.  Creating well-defined boundaries of responsibility between various modules of our app allows us to expose as little as possible from each module making testing easier with each.  We persist as much relevant and fresh data as possible and assign one data source to be the single source of truth.

### Architecture

![The Clean Architecture](https://user-images.githubusercontent.com/7444521/103781150-ef9b7a00-5003-11eb-8d1c-1e41fbf1bb22.png)

Using Clean Architecture we want to distinguish the core functionality of our code and limit dependencies.  Clean architecture is the separation of concerns. 


The core functionality in this case would be our Domain layer and the rules of receiving Amiibo information and displaying it to the user.  All the while maintaining created user created Amiibos.  With our application infrastructure extending from the core functionality.  Like our APIs, database, and user interface.  These are interchangable and our core is not dependent on them if they need to change.  Components in infrastructure layer are volatile, likely to change, and are commonly replaced.  Meaning we do not want these components tightly bound to the core functionality.  

We declare more graunularity with the introduction of our Entities, aka our Amiibos.  These entities internally contain rules that will not change regardless of the applicaion such as our response structure and user-specific information related to creation.  Adapters are including in this further granularity and can be interpreted as our Fragment ViewModels translating UI specific logic to those of our business rules.  Repackaging it in the form of Use Cases and Entities.  The Use Cases for our applications are the behavior rules for our app.  In AmiiboRepo this behavior would be the logic that dictates :
- Storage and retrieval of our Retrofit data locally with Room and when to do so
- Local storage and retrieval of image resources with the files system
- User created Amiibos storage and merging with the web retrieved Amiibos


Data goes in from the view, goes through the controllers and databases into the use cases and heads towards the center of the system, which is the entities.  Entities get populated by the repositories and those entities are injected into all the controllers and use cases that make use of them.  We make sure our Entity classes are the only source of data going INTO the UseCase


When looking at the architecture as a traffic cone, then you have the tip which is the most pure and abstract, having zero dependencies and rarely changing. The wide, bottom layer is then the most concrete and most likely to change. Each layer of the cone has dependencies on the layer above it.  The bottom represents the concrete mechanisms that are specific to the platform such as UI, networking, and database access. Moving upward om the cone, each layer is more abstract and represents logic at higher-level. The center circle is the most abstract and contains business logic, which doesn’t rely on the platform or the framework you’re using.


Clean Architecture does a great job of forcing the use of SOLID programming principles.  
- Single Responsibility Principle (SRP) duh
- Open Closed Principle (OCP) Open means open for extension. Closed means closed for modification.
- Liskov Substitution Principle (LSP) Lower level classes or components can be substituted without affecting the behavior of the higher level classes and components. This can be done by implementing abstract classes or interfaces.
- Interface Segregation Principle (ISP) When possible use interfacees the interface only exposes the subset of methods that a dependent class needs.
- Dependency Inversion Principle (DIP) "Hollywood Principle"

### Dagger Graph
Why we kept it with an application scope and how we would create subcomponents if we had more time

### Room

Because we have such a large list of Amiibos that lacked any sort of paging functionality we decided that we would store it locally with ROOM as part of Android Jetpack.  Encapsulating the implementation in the repository layer allows us to load from the web only when we first load the application.  The schema of our database mirroring that of our Amiibo object.  Our "mediator" that gets the local items initially stored and user created objects emits objects to the ViewModel in a congruent list.

![a7da8f5ea91bac52](https://user-images.githubusercontent.com/7444521/103432470-4db40180-4bad-11eb-9511-4af5ea1613a3.png)

### Retrofit and Local Storage Mediation



### Local Image Storage and Creating a User

### Decisions with the User Experience and Camera
