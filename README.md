# AmiiboRepo
![Screenshot_20201231-201327_AmiiboRepo](https://user-images.githubusercontent.com/7444521/103785173-e5c84580-5008-11eb-8ce0-5a3357093c16.jpg)
![Screenshot_20201231-201444_AmiiboRepo](https://user-images.githubusercontent.com/7444521/103785174-e5c84580-5008-11eb-93ab-fc0ba657894c.jpg)

### Design

![final-architecture](https://user-images.githubusercontent.com/7444521/103432363-a4204080-4bab-11eb-8482-30557d54cfb6.png)

We decided to go with our Google's recommended architecture using Android Architecture Components.  Most commonly this is MVVM design pattern aligned with Clean Architecture.


A unidirectional, reactive flow will ensure that because things are decoupled further with each components only dependent on the one "below" it in the diagram.  Modulatization becomes easier as well if we continue to scale the application and parts of our application need to be portable across functional boundaries.  As modularity and reusability remains our focus we will be able to increase the speed at which we add new features. Another important principle is that we should drive our UI from a model, preferably a persistent model.


Developing our application along these design patterns keeps us from designating our app's entry points—such as activities, services, and broadcast receivers—as sources of data.  Creating well-defined boundaries of responsibility between various modules of our app allows us to expose as little as possible from each module making testing easier with each.  We persist as much relevant and fresh data as possible and assign one data source to be the single source of truth.

For this application we used a the recommended single activity design with navigation components for our two fragments.  With the simplicity of this application we used two fragments with their associated viewmodel dependent on our repository.  This ensured specific verticals of functionality with a limited amount of data passed between the views maintained in our presentation layer.  Persisting our data in local storage if we were to use the view suddenly.  We would want to use a shared activity across viewmodels if we were to have specific interactions and saved states across the list and specific view fragment.

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

### Dagger Graph and Dependency Injection

Manual dependency injection or service locators in an Android app can be problematic depending on the size of your project.  Dagger automatically generates code that mimics the code you would otherwise have hand-written. Because the code is generated at compile time, it's traceable and more performant.  Dagger frees you from writing tedious and error-prone boilerplate code by:
- Generating the application graph that you manually implemented in the manual dependency injection modules.
- Creating factories for the classes available in the application graph. This is how dependencies are satisfied internally.
- Deciding whether to reuse a dependency or create a new instance through the use of scopes.
- Creating containers for specific flows as you did with the login flow in the previous section using Dagger subcomponents. This improves your app's performance by releasing objects in memory when they're no longer needed.

For this project we went with an application scoped dagger graph limiting cross-module graphs.  Our ApplicationComponents is responsible for constructing the dependency graph across our Application and Subcomponents Modules.  Each of these modules have the ability to provide instances of objects and manage their own Subcomponents each with their own scopes.  A large application graph was used for the sake of brevity.  For a larger application we would be sure to divide our modules out by Activity and Fragment scopes, with our modularity leaving it open to introduce our own custom scopes for specific components so our dependency injection graphs only keep objects around for as long as they need.

With the further development of Android architecture components we are able to lean on the Android provided APIs to handle scopes and injection with our ViewModels such as using ActivityViewModels if we want to share ViewModels across activities.  Another example of this is how we used a ViewModelProviderFactory to tell the Android ViewModel framework to know when to instantiate a specific ViewModel singleton and when to reuse an existing one.

### Room

![a7da8f5ea91bac52](https://user-images.githubusercontent.com/7444521/103432470-4db40180-4bad-11eb-9511-4af5ea1613a3.png)

Because we have such a large list of Amiibos that lacked any sort of paging functionality we decided that we would store it locally with ROOM as part of Android Jetpack.  Encapsulating the implementation in the repository layer allows us to load from the web only when we first load the application.  The schema of our database mirroring that of our Amiibo object.  Our "mediator" (housed in our repository) that gets the local items initially stored and user created objects emits objects to the ViewModel in a congruent list.

In terms of our database relationships it's as simple as it could be since we are using a single table.  A way to make things more complicated would be to keep the Release object as part of our Amiibo and label it as an @Inner table with Room.  In SQL world this would be considered a one-to-one relationship with Release being a child.  Here is an example of that :

![one-to-one](https://user-images.githubusercontent.com/7444521/103789127-a6502800-500d-11eb-9c66-a93f3b5aad9d.png)

If we wanted to scale our application we would want to consider perhaps that we would have many releases associated with a single Amiibo in this case we would want to consider a one-to-many relationship with each Release having it's own unique ID while also referencing the ID of the parent Amiibo object.  Here is an example of that :

![one-to-many](https://user-images.githubusercontent.com/7444521/103789305-d8fa2080-500d-11eb-99f6-20874ebc2da1.png)

Eventually Amiibos may become collectables and certain releases could be considered more valuable than other releases on the same Amiibo and this would have our users want to order the Amiibos.  If we were to have specific Releases contain multiple Amiibos and specific Amiibos contain multiple.  For this we would create a "junction" table that would connect the two.  Maybe ORDERS is the right word or maybe not.  Here is an example of that :

![many-to-many](https://user-images.githubusercontent.com/7444521/103789765-60e02a80-500e-11eb-887c-628621c8bf08.png)

As part of our Room DAO we have our annotations for @Insert, @Update, and @Delete but we need to provide our own SQL statement for the @Query queries.  We keep things simple in this example with SELECT statements with a conditinal WHERE.

If we wanted to complicate things and do it ourselves we would use SQLite3 for android and go through a series of queries like this baseballexample :
```
sqlite> CREATE TABLE players (id INTEGER PRIMARY KEY ASC, name TEXT, seasons_played INTEGER);
sqlite> CREATE TABLE teams (id INTEGER PRIMARY KEY ASC, name TEXT);
sqlite> CREATE TABLE players_teams (player_id INTEGER, team_id INTEGER, won_championship BOOLEAN);
```
We would then be required to populate our database and retrieve our items in a specific order :
```
INSERT INTO players (name, seasons_played) VALUES ('Nolan Ryan', 27);
INSERT INTO players (name, seasons_played) VALUES ('Jim Sundberg', 16);
SELECT * FROM players ORDER BY seasons_played;
```
A junction table would be necessary for all of the data we have :
```
sqlite> INSERT INTO players_teams (player_id, team_id, won_championship) VALUES (1, 4, 0);
sqlite> INSERT INTO players_teams (player_id, team_id, won_championship) VALUES (1, 3, 1);
```
With the introduction of a junction table we would be required to query multiple tables with a JOIN.  The most common join we have in our tool box is called an INNER JOIN. An INNER JOIN allows us to combine two tables based on a condition we provide. The only rows from both tables that remain after the join are the rows that satisfy the condition.  The condition we want to find in the second table is what comes after the ON keyword.  The table we want more information provided to comes before the ON keyword.  Because we use an INNER JOIN the only rows present are the rows that satisfy our condition :
```
SELECT * FROM players INNER JOIN players_teams ON players.id = players_teams.player_id;
```
A LEFT OUTER JOIN is very similar to an INNER JOIN with one big exception. If a row in the table being joined on does not match the condition specified in the join, the row still remains in the result set.

If we want to get more specific information we can also group results together and aggregate values.
```
SELECT teams.name, COUNT(players.name) FROM teams INNER JOIN players_teams ON teams.id = players_teams.team_id INNER JOIN players ON players.id = players_teams.player_id GROUP BY teams.name;
```
