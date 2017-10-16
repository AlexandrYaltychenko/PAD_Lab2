package node

import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import data.Entry
import protocol.Protocol
import protocol.message.*
import protocol.tcp.TCPConnection

class RemoteNode(override val port : Int, override val host : String = Protocol.CLIENT_RESPONSE_ADR, override val connectionsCount: Int = 0) : Node {

    suspend override fun getData(): List<Entry> {
        TODO("NOT IMPLEMENTED")
        /*val connection = TCPConnection(port, host)
        val msg = DiscoveryMessage(Header(senderType = SenderType.NODE, messageType = MessageType.TCP_QUERY, messageStatus = MessageStatus.NORMAL))
        println("sent request to Node $port")
        connection.writeMsg(msg)
        val result = connection.readMsg()
        return if (result != null) {
            println("processing result...")
            val type = object : TypeToken<List<Entry>>(){}.type
            //Gson().fromJson(result.payload, type)
        } else {
            listOf()
        }*/
    }
}