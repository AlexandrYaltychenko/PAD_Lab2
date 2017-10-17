package client

import kotlinx.coroutines.experimental.CommonPool
import kotlinx.coroutines.experimental.delay
import kotlinx.coroutines.experimental.launch
import node.Node
import node.RemoteNode
import protocol.Protocol
import protocol.message.DiscoveryMessage
import protocol.message.Header
import protocol.udp.MulticastSender
import protocol.udp.UDPReceiver


class DiscoveryService {

    suspend fun handleResponse() {

    }

    suspend fun sendMulticast() {
        val sender = MulticastSender(Protocol.MULTICAST_PORT, Protocol.MULTICAST_ADR)
        val msg = DiscoveryMessage(Header())
        sender.sendMulticast(msg)
        println("multicast sent!")
        val listener = UDPReceiver(Protocol.CLIENT_RESPONSE_PORT)
        val nodes = mutableListOf<Node>()
        val processResponse = launch(CommonPool){
            while (true) {
                val response = listener.receiveMessage()
                if (response != null) {
                    println("got response $response")
                    nodes.add(RemoteNode(response.header.responsePort,response.header.responseAdr, response.connections))
                }
            }
        }
        delay(5000)
        processResponse.cancel()
        println("Results: ${nodes.joinToString { "(${it.host}:${it.port}  connections: ${it.connectionsCount})"}}")
    }
}

