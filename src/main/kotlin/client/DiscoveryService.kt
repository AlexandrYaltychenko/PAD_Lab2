package client

import node.Node
import protocol.Protocol
import protocol.message.DiscoveryMessage
import protocol.message.Header
import protocol.udp.MulticastSender
import protocol.udp.UDPReceiver


class DiscoveryService {

    fun handleResponse() {

    }

    fun sendMulticast() {
        val sender = MulticastSender(Protocol.MULTICAST_PORT, Protocol.MULTICAST_ADR)
        val msg = DiscoveryMessage(Header())
        sender.sendMulticast(msg)
        println("multicast sent!")
        val listener = UDPReceiver(Protocol.CLIENT_RESPONSE_PORT)
        val nodes = mutableListOf<Node>()
        while (true) {
            val msg = listener.receiveMessage()
            if (msg != null) {
                println("got response $msg")
                //nodes.add(RemoteNode(msg.payload.toInt(),))
            } else
                println("got invalid response... ignoring")
        }
    }
}

