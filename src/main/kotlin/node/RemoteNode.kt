package node

import data.Book
import protocol.Protocol
import protocol.message.DataHeader
import protocol.message.DataMessage
import protocol.message.SenderType
import protocol.tcp.TCPConnection

class RemoteNode(override val port: Int, override val host: String = Protocol.CLIENT_RESPONSE_ADR, override val connectionsCount: Int = 0) : Node {

    override fun getData(request: DataMessage): DataMessage {
        val connection = TCPConnection(host, port)
        println("getting data from $port...")
        connection.writeMsg(DataMessage(DataHeader(senderType = request.header.senderType, asked = request.header.asked),
                uid = request.uid,
                query = request.query,
                level = request.level))
        val result = connection.readMsg() ?: DataMessage(DataHeader(senderType = SenderType.NODE, asked = mutableSetOf()),
                uid = request.uid,
                query = request.query,
                level = request.level,
                data = listOf())
        println("got result ${result.data.size} items")
        return result
    }
}