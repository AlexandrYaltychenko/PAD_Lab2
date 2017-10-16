package protocol

object Protocol {
    val MULTICAST_PORT = 14141
    val MULTICAST_ADR = "230.1.1.1"
    val DEFAULT_DATAGRAM_SIZE = 1024
    val DEFAULT_NODE_PORT = 4000
    val CLIENT_RESPONSE_PORT = 11144
    val CLIENT_RESPONSE_ADR = "127.0.0.1"
    val DISCOVERY_MESSAGE_SCHEMA = """
        {
            "senderType" : "Type of sender",
            "messageType": "Type of message",
            "messageStatus" : "Status of message",
            "connections" : 0,
            "responsePort": 2000,
            "responseAdrr": "178.168.65.187",
            "jora":15
        }"""
}