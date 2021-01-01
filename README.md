# AmiiboRepo

### Clean Android Architecture

We decided to go with our Google's recommended architecture using Android Architecture Components.  This is MVVM design pattern aligned with Clearn Architecture.

Clean architecture is the separation of concerns. We want the “inner levels” meaning we start on the inside with the web/domain going through the use cases through the viewmodel to the view (outermost) to have no idea of what is going on in the classes that it calls across circles.

The outer circle represents the concrete mechanisms that are specific to the platform such as networking and database access. Moving inward, each circle is more abstract and higher-level. The center circle is the most abstract and contains business logic, which doesn’t rely on the platform or the framework you’re using.

A unidirectional flow will ensure that because things are decoupled further and modularity remains a focus we will be able to increase the speed at which we add new features. Librification becomes easier as well if we continue to scale the application and parts of our application need to be portable across apps. Between our Use Case and domain we want to use interfaces for two reasons

A Data layer which is of a higher, more abstract level doesn’t depend on a framework, lower-level layer. The repository is an abstraction of Data Access and it does not depend on details. It depends on abstraction.

![final-architecture](https://user-images.githubusercontent.com/7444521/103432363-a4204080-4bab-11eb-8482-30557d54cfb6.png)

### Room

Because we have such a large list of Amiibos that lacked any sort of paging functionality we decided that we would store it locally with ROOM as part of Android Jetpack.  Encapsulating the implementation in the repository layer allows us to load from the web only when we first load the application.  The schema of our database mirroring that of our Amiibo object.  Our "mediator" that gets the local items initially stored and user created objects emits objects to the ViewModel in a congruent list.

![a7da8f5ea91bac52](https://user-images.githubusercontent.com/7444521/103432470-4db40180-4bad-11eb-9511-4af5ea1613a3.png)

