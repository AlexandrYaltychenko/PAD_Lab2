package protocol.udp

import protocol.Protocol
import protocol.asDiscoveryMessage
import protocol.message.DiscoveryMessage
import java.net.DatagramPacket
import java.net.InetAddress
import java.net.MulticastSocket

class MulticastListener(mcPort: Int, mcIPStr: String) {
    private val mcSocket = MulticastSocket(mcPort)
    private val mcIPAddress = InetAddress.getByName(mcIPStr)

    init {
        mcSocket.`interface` = InetAddress.getLoopbackAddress()
        mcSocket.joinGroup(mcIPAddress)
    }

    fun catchMulticast(): DiscoveryMessage? {
        val packet = DatagramPacket(ByteArray(Protocol.DEFAULT_DATAGRAM_SIZE), Protocol.DEFAULT_DATAGRAM_SIZE)
        mcSocket.receive(packet)
        return try {
            String(packet.data, packet.offset,
                    packet.length).asDiscoveryMessage()
        } catch (e : Exception){
            e.printStackTrace()
            null
        }
    }

    fun close() {
        mcSocket.leaveGroup(mcIPAddress)
        mcSocket.close()
    }
}