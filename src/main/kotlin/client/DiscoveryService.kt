package client

import node.Node
import node.RemoteNode
import protocol.Protocol
import protocol.message.Message
import protocol.udp.MulticastSender
import protocol.udp.UDPReceiver


class DiscoveryService {

    fun handleResponse() {

    }

    fun sendMulticast() {
        val sender = MulticastSender(Protocol.MULTICAST_PORT, Protocol.MULTICAST_ADR)
        val msg = Message(payload = "Testing...")
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

