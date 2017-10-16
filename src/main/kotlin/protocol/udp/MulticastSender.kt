package protocol.udp

import protocol.encode
import protocol.message.DiscoveryMessage
import java.net.DatagramPacket
import java.net.InetAddress
import java.net.MulticastSocket

class MulticastSender(val mcPort: Int, val mcIPStr: String) {

    fun sendMulticast(msg: DiscoveryMessage) {
        val multicastSocket = MulticastSocket()
        multicastSocket.reuseAddress = true
        multicastSocket.`interface` = InetAddress.getLoopbackAddress()
        val mcIPAddress = InetAddress.getByName(mcIPStr)
        val message = msg.encode().toByteArray()
        val packet = DatagramPacket(message, message.size)
        packet.address = mcIPAddress
        packet.port = mcPort
        multicastSocket.send(packet)
        multicastSocket.close()
    }
}