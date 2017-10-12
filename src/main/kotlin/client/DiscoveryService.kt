package client

import protocol.Protocol
import java.net.DatagramPacket
import java.net.DatagramSocket
import java.net.InetAddress


class DiscoveryService {

    fun sendMulticast(){
        val mcPort = Protocol.MULTICAST_PORT
        val mcIPStr = Protocol.MULTICAST_ADR
        val udpSocket = DatagramSocket()

        val mcIPAddress = InetAddress.getByName(mcIPStr)
        val msg = "Hello".toByteArray()
        val packet = DatagramPacket(msg, msg.size)
        packet.address = mcIPAddress
        packet.port = mcPort
        udpSocket.send(packet)

        println("Sent a  multicast message.")
        println("Exiting application")
        udpSocket.close()
    }

}

