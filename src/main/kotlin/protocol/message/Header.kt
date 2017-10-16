package protocol.message

import protocol.Protocol

data class Header(val senderType: SenderType = SenderType.CLIENT,
                  val messageType: MessageType = MessageType.UDP_MULTICAST,
                  val messageStatus: MessageStatus = MessageStatus.NORMAL,
                  val responsePort: Int = Protocol.CLIENT_RESPONSE_PORT,
                  val responseAdr: String = Protocol.CLIENT_RESPONSE_ADR)