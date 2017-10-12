package node

import protocol.Protocol
import java.net.DatagramPacket
import java.net.InetAddress
import java.net.MulticastSocket


class Node {
    fun catchMulticast(){
        val mcPort = Protocol.MULTICAST_PORT
        val mcIPStr = Protocol.MULTICAST_ADR
        var mcSocket: MulticastSocket? = null
        var mcIPAddress: InetAddress? = null
        mcIPAddress = InetAddress.getByName(mcIPStr)
        println(mcIPAddress)
        mcSocket = MulticastSocket(mcPort)
        println("Multicast Receiver running at:" + mcSocket.localSocketAddress)
        mcSocket.joinGroup(mcIPAddress!!)

        val packet = DatagramPacket(ByteArray(1024), 1024)

        println("Waiting for a  multicast message...")
        mcSocket.receive(packet)
        val msg = String(packet.data, packet.offset,
                packet.length)
        println("[Multicast  Receiver] Received:" + msg)

        mcSocket.leaveGroup(mcIPAddress)
        mcSocket.close()
    }
}