package node

import protocol.message.DataMessage
import protocol.message.SenderType

interface Node {
    val port : Int
    val host : String
    val connectionsCount : Int
    suspend fun getData(request : DataMessage) : DataMessage
}