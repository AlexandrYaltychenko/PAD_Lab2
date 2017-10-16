package protocol.message

import protocol.Protocol

data class DiscoveryMessage(val header: Header = Header(),
                            val connections: Int = 0)