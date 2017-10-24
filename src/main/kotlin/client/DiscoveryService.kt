package client

import kotlinx.coroutines.experimental.CommonPool
import kotlinx.coroutines.experimental.delay
import kotlinx.coroutines.experimental.launch
import node.Node
import node.RemoteNode
import protocol.Protocol
import protocol.message.*
import protocol.udp.MulticastSender
import protocol.udp.UDPReceiver
import java.util.*


class DiscoveryService {

    suspend fun handleResponse() {

    }

    suspend fun sendMulticast(query: String) {
        val level = 1
        val sender = MulticastSender(Protocol.MULTICAST_PORT, Protocol.MULTICAST_ADR)
        val msg = DiscoveryMessage(DiscoveryHeader())
        println("sending multicast...")
        sender.sendMulticast(msg)
        val listener = UDPReceiver(Protocol.CLIENT_RESPONSE_PORT)
        val nodes = mutableListOf<Node>()
        val processResponse = launch(CommonPool) {
            while (true) {
                val response = listener.receiveMessage()
                if (response != null) {
                    nodes.add(RemoteNode(response.header.responsePort, response.header.responseAdr, response.connections))
                }
            }
        }
        delay(Protocol.DEFAULT_DISCOVERY_TIMEOUT)
        processResponse.cancel()
        println("selecting maven from ${nodes.size} asked")
        if (nodes.size > 0) {
            val maven = nodes.maxBy { it.connectionsCount }
            val data = maven?.getData(DataMessage(DataHeader(senderType = SenderType.CLIENT,
                    asked = mutableSetOf()), uid = UUID.randomUUID().toString(),
                    query = query, level = level))?.data ?: listOf()
            println("got data (${data.size} items):")
            for (book in data)
                println(book)
        } else
            println("no asked response")
    }
}

