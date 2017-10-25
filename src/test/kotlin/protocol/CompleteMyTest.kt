package protocol

import client.DiscoveryService
import kotlinx.coroutines.experimental.runBlocking
import main

internal class CompleteMyTest {
    @org.junit.jupiter.api.Test
    fun testAll() {
        Thread {
            runBlocking { DiscoveryService().sendMulticast("ORDER (author) FILTER (year>1900)") }
        }
        main(arrayOf())
    }

}