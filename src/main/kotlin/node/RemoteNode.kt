package node

import data.Book
import protocol.Protocol

class RemoteNode(override val port : Int, override val host : String = Protocol.CLIENT_RESPONSE_ADR, override val connectionsCount: Int = 0) : Node {

    suspend override fun getData(): List<Book> {
        TODO("NOT IMPLEMENTED")
        /*val connection = TCPConnection(port, host)
        val msg = DiscoveryMessage(Header(senderType = SenderType.NODE, messageType = MessageType.TCP_QUERY, messageStatus = MessageStatus.NORMAL))
        println("sent request to Node $port")
        connection.writeMsg(msg)
        val result = connection.readMsg()
        return if (result != null) {
            println("processing result...")
            val type = object : TypeToken<List<Book>>(){}.type
            //Gson().fromJson(result.payload, type)
        } else {
            listOf()
        }*/
    }
}