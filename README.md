# Summary
Android Retrofit FlowCallAdapterFactory

Retrofit 2 `CallAdapter.Factory` for `kotlin coroutine's Flow<T>`

# Usage

Add `FlowCallAdapterFactory` as `Call` adapter when building your `Retrofit` instance:

```kotlin
val retrofit = Retrofit.Builder()
    .baseUrl("https://thdev.tech/")
    .addConverterFactory(StringConverterFactory())
    .addCallAdapterFactory(FlowCallAdapterFactory())
    .build()
```

Your service methods can now use `Flow<T>` as their return type.

```kotlin
interface Service {
    @GET("/example")
    fun getExample(): Flow<Example>

    @GET("/example")
    fun getExample(): Flow<Response<Example>>
}
```

# Download

![Maven Central](https://img.shields.io/maven-central/v/tech.thdev/flow-call-adapter-factory)

```
implementation("tech.thdev:flow-call-adapter-factory:latest_version")
```

Release version are available in [Sonatyp's repository.](https://search.maven.org/artifact/tech.thdev/flow-call-adapter-factory?fbclid=IwAR31h2mm6MYMGG_Wrwm4lB_Gm0EoQkMOUv63pX27WVwIYB6MATbWkQgNAWc)

## Default dependency

```
implementation("org.jetbrains.kotlin:kotlin-stdlib:$version")
implementation("org.jetbrains.kotlinx:kotlinx-coroutines-core:$version")
implementation("com.squareup.retrofit2:retrofit:$retrofitVersion")
```

## License

```
Copyright 2021 TaeHwan-dev

Licensed under the Apache License, Version 2.0 (the "License");
you may not use this file except in compliance with the License.
You may obtain a copy of the License at

   http://www.apache.org/licenses/LICENSE-2.0

Unless required by applicable law or agreed to in writing, software
distributed under the License is distributed on an "AS IS" BASIS,
WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
See the License for the specific language governing permissions and
limitations under the License.
```
