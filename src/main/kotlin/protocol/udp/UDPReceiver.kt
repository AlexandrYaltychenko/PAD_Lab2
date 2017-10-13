package protocol.udp

import protocol.Protocol
import protocol.asMessage
import protocol.message.Message
import java.net.DatagramPacket
import java.net.DatagramSocket

class UDPReceiver(mcPort: Int) {
    private val serverSocket = DatagramSocket(mcPort)
    fun receiveMessage(): Message? {
        val receiveData = ByteArray(Protocol.DEFAULT_DATAGRAM_SIZE)
        val receivePacket = DatagramPacket(receiveData,
                receiveData.size)
        return try {
            serverSocket.receive(receivePacket)
            String(receivePacket.data, 0, receivePacket.length).asMessage()
        } catch (e: Exception) {
            null
        }
    }
}