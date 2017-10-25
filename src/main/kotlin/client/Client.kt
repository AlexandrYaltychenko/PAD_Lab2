package client

import protocol.Protocol
import protocol.message.DataHeader
import protocol.message.DataMessage
import protocol.message.MessageType
import protocol.message.SenderType
import protocol.tcp.TCPConnection
import java.util.*

class Client {
    fun performQuery(query: String) {
        val connection = TCPConnection(Protocol.PROXY_HOST, Protocol.PROXY_PORT)
        connection.writeMsg(DataMessage(DataHeader(senderType = SenderType.CLIENT, messageType = MessageType.TCP_QUERY),
                UUID.randomUUID().toString(), query = query))
        val result = connection.readMsg()
        println("got data:")
        val data = result?.data
        data?.let {
            for (book in data)
                println(book)
        }

    }
}