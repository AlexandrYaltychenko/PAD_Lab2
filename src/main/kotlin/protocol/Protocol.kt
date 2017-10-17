package protocol

object Protocol {
    val MULTICAST_PORT = 14141
    val MULTICAST_ADR = "230.1.1.1"
    val DEFAULT_DATAGRAM_SIZE = 1024
    val DEFAULT_NODE_PORT = 4000
    val CLIENT_RESPONSE_PORT = 11144
    val CLIENT_RESPONSE_ADR = "127.0.0.1"
    val DISCOVERY_MESSAGE_SCHEMA_ADR = "schema/discovery_message.json"
    val DATA_MESSAGE_SCHEMA_ADR = "schema/data_message.json"
    val DEFAULT_DISCOVERY_TIMEOUT = 5000
}