package client

import kotlinx.coroutines.experimental.runBlocking

fun main(args : Array<String>) = runBlocking{
    DiscoveryService().sendMulticast("ORDER (author) FILTER (author=.*,year<=1829)")
}