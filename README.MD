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
+ DiscoveryService (ClientRunner.kt) - Client with discovery service
+ LocalNode (NodeRunner.kt) - One node
+ Config (ConfigRunner.kt) - Whole node configuration corresponding to config.json

### Description of the protocol

**Default parameters**

Protocol.kt contains default parameters, which could be modified by the user of application:
+ MULTICAST_PORT = 14141
+ MULTICAST_ADR = "230.1.1.1"
+ DEFAULT_DATAGRAM_SIZE = 1024
+ CLIENT_RESPONSE_PORT = 11444
+ CLIENT_RESPONSE_ADR = "127.0.0.1"
+ DISCOVERY_MESSAGE_SCHEMA_ADR = "schema/discovery_message.json"
+ DATA_MESSAGE_SCHEMA_ADR = "schema/data_message.json"
+ DEFAULT_DISCOVERY_TIMEOUT = 5000L
+ DSL_KEYWORDS = setOf("ORDER", "FILTER", "GROUP")
+ QUERY_FIELD = setOf("desc","author","title","year")
+ FILTER_OPERATORS = setOf("<=",">=","=","<",">")

**Nodes**

All nodes are running on the localhost and listening to MULTICAST_PORT for a multicast (group MULTICAST_ADR). The port for UDP response to discovery service is CLIENT_RESPONSE_PORT.
Every node is running in a different thread and it is associated with different port. At startup all nodes are run corresponding to config.json.

**Discovery service**

Discovery service sends multicast and waits DEFAULT_DISCOVERY_TIMEOUT to get responses. After that, the node with maximum number of connections is selected and asked using TCP.

**Data details**

As data unit is used book data class, which contains informations about title, author, description, year and special debug parameter src (this parameter is set by the node, which has generated the book and used in development purposes). Books are generated using the little database in base.json.

**DSL protocol**

DSL protocol allows to sort, group and filter the books. The available keywords and fields are presented in Protocol.kt as DSL_KEYWORDS, QUERY_FIELDS. 

The query represents a String, which should contain command keywords and arguments in parentheses. 

*Get books sorted by author:*
~~~
SORT (author)
~~~
*Get books sorted by title and year and filtered by author=Pushkin*
~~~
SORT(title,year) FILTER(author=Pushkin)
~~~
*Get books sorted by desc, filtered by author which name should begin wuth 'T' and grouped by year*
~~~
SORT(desc) FILTER(author=T.*) GROUP (year)
~~~
The order of keywords and its arguments is not important.
