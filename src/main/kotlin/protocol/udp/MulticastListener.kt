package protocol.udp

import protocol.Protocol
import protocol.asMessage
import protocol.message.Message
import java.net.DatagramPacket
import java.net.InetAddress
import java.net.MulticastSocket

class MulticastListener(mcPort: Int, mcIPStr: String) {
    private val mcSocket = MulticastSocket(mcPort)
    private val mcIPAddress = InetAddress.getByName(mcIPStr)

    init {
        mcSocket.joinGroup(mcIPAddress)
    }

    fun catchMulticast(): Message? {
        val packet = DatagramPacket(ByteArray(Protocol.DEFAULT_DATAGRAM_SIZE), Protocol.DEFAULT_DATAGRAM_SIZE)
        mcSocket.receive(packet)
        (packet.address.hostAddress)
        return try {
            String(packet.data, packet.offset,
                    packet.length).asMessage()
        } catch (e : Exception){
            null
        }
    }

    fun close() {
        mcSocket.leaveGroup(mcIPAddress)
        mcSocket.close()
    }
}