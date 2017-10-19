package protocol.message

import data.Book
import protocol.Protocol

data class DataMessage(val header: DataHeader, val uid: String, val query: String, val level: Int = 0, val data: List<Book> = listOf()) : Message

data class DiscoveryMessage(val header: DiscoveryHeader = DiscoveryHeader(),
                            val connections: Int = 0) : Message

data class DiscoveryHeader(val senderType: SenderType = SenderType.CLIENT,
                           val messageType: MessageType = MessageType.UDP_MULTICAST,
                           val messageStatus: MessageStatus = MessageStatus.NORMAL,
                           val responsePort: Int = Protocol.CLIENT_RESPONSE_PORT,
                           val responseAdr: String = Protocol.CLIENT_RESPONSE_ADR)

data class DataHeader(val senderType: SenderType = SenderType.CLIENT,
                      val messageType: MessageType = MessageType.TCP_QUERY,
                      val messageStatus: MessageStatus = MessageStatus.NORMAL,
                      val asked: MutableSet<Int>)

