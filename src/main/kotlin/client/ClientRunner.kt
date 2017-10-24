package client

import kotlinx.coroutines.experimental.runBlocking

fun main(args : Array<String>) = runBlocking{
    DiscoveryService().sendMulticast("GROUP (author,desc) ORDER (author) FILTER (author=.*,year<=2100)")
}