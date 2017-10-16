package protocol.udp

import protocol.encode
import protocol.message.DiscoveryMessage
import java.net.DatagramPacket
import java.net.DatagramSocket
import java.net.InetAddress

class UDPSender(val mcPort: Int, val mcIPStr: String) {
    fun sendMessage(msg: DiscoveryMessage) {
        val address = InetAddress.getByName(mcIPStr)
        val buffer = msg.encode().toString().toByteArray()
        val packet = DatagramPacket(
                buffer, buffer.size, address, mcPort
        )
        val datagramSocket = DatagramSocket()
        datagramSocket.send(packet)
    }
}