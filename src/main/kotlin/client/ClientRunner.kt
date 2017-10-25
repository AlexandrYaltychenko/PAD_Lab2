package client

import kotlinx.coroutines.experimental.runBlocking
import protocol.asQuery

fun main(args: Array<String>) = runBlocking {
    println("enter your query...")
    val query = readLine()
    if (query?.asQuery() != null) {
        DiscoveryService().sendMulticast(query)
    } else
        println("The query you entered is not valid")
}