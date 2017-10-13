package protocol.message

import protocol.Protocol

data class Message(val senderType: SenderType = SenderType.CLIENT,
                   val messageType: MessageType = MessageType.UDP_MULTICAST,
                   val messageStatus: MessageStatus = MessageStatus.NORMAL,
                   val payload: String = "empty",
                   val responsePort: Int = Protocol.CLIENT_RESPONSE_PORT,
                   val responseAdr: String = Protocol.CLIENT_RESPONSE_ADR)