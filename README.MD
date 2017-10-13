# PAD LAB2

##### Distributed Data Collection

Working language is **Kotlin**. 

### Build

In order to build Kotlin with Gradle you should set up the kotlin-gradle plugin, apply it to your project and add kotlin-stdlib dependencies. Those actions may also be performed automatically in IntelliJ IDEA by invoking the Tools | Kotlin | Configure Kotlin in Project action (details: https://kotlinlang.org/docs/reference/using-gradle.html) 

**Gradle repositories:**
~~~
mavenCentral()
    repositories {
        jcenter()
    }
~~~

**Gradle dependencies:**
~~~
compile "org.jetbrains.kotlin:kotlin-stdlib-jre8:$kotlin_version"
compile 'org.jetbrains.kotlinx:kotlinx-coroutines-core:0.18'
compile group: 'com.google.code.gson', name: 'gson', version: '2.7'
~~~

**Kotlin experimental**
This project uses experimental kotlin coroutines, which are in development.
Add these lines to build.gradle to hide compilation warning:
~~~
kotlin {
    experimental {
        coroutines "enable"
    }
}
~~~

### Run

You can run the following instances:
+ DiscoveryService (ClientRunner.kt)
+ LocalNode (NodeRunner.kt)




