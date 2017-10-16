package protocol.udp

import protocol.Protocol
import protocol.asDiscoveryMessage
import protocol.message.DiscoveryMessage
import java.net.DatagramPacket
import java.net.DatagramSocket

class UDPReceiver(mcPort: Int) {
    private val serverSocket = DatagramSocket(mcPort)
    fun receiveMessage(): DiscoveryMessage? {
        val receiveData = ByteArray(Protocol.DEFAULT_DATAGRAM_SIZE)
        val receivePacket = DatagramPacket(receiveData,
                receiveData.size)
        return try {
            serverSocket.receive(receivePacket)
            String(receivePacket.data, 0, receivePacket.length).asDiscoveryMessage()
        } catch (e: Exception) {
            null
        }
    }
}