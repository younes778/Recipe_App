# Recipe_App
Recipe App : retrive data about recipes by searching keyword. In this Application many of the new Android technologies has been used such as:
- [RecyclerView](https://developer.android.com/guide/topics/ui/layout/recyclerview) : with different levels of TYPE to show different part such as items, loading and the query get exhausted

- [Model View ViewModel architecture](https://medium.com/upday-devs/android-architecture-patterns-part-3-model-view-viewmodel-e7eeee76b73b) : the new recommanded architectural pattern that is based on the Observable pattern, lifedata with the mutable and mediator types and the viewModel and repositories

- [Retrofit](https://square.github.io/retrofit/) : for requesting queries from the API [Food2Fork](https://www.food2fork.com/) that gathers different food recipes with social ranking.

- [Glide](https://github.com/bumptech/glide) for downloading images asynchronisly

- [Executors](https://developer.android.com/reference/java/util/concurrent/Executor) for executing threads in the background and handling timeout and exhausted queries.

- [RxJava / RxAndroid](https://github.com/ReactiveX/RxAndroid) for executing threads in the background

- [RoomDB](https://developer.android.com/topic/libraries/architecture/room) for caching data when synchronization is impossible
