package protocol.message

data class DiscoveryMessage(val header: Header = Header(),
                            val connections: Int = 0) : Message