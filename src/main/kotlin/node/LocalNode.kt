package node

import data.Book
import kotlinx.coroutines.experimental.CommonPool
import kotlinx.coroutines.experimental.delay
import kotlinx.coroutines.experimental.launch
import kotlinx.coroutines.experimental.runBlocking
import protocol.Protocol
import protocol.message.DiscoveryMessage
import protocol.message.Header
import protocol.message.SenderType
import protocol.udp.MulticastListener
import protocol.udp.UDPSender
import java.util.*


class LocalNode(override val port: Int, private val nodes: List<Node>) : Node {
    private val data = mutableListOf<Book>()
    private suspend fun handleMulticasts() {
        val client = MulticastListener(Protocol.MULTICAST_PORT, Protocol.MULTICAST_ADR)
        while (true) {
            println("waiting for a multicast...")
            val msg = client.catchMulticast()
            if (msg != null) {
                launch(CommonPool){
                    println("got multicast message $msg")
                    val response = DiscoveryMessage(Header(senderType = SenderType.NODE, responsePort = port))
                    println("sending response $response")
                    val delay = Math.abs(Random().nextInt() % 6000 + 4000).toLong()
                    println("delaying $delay")
                    delay(delay)
                    UDPSender(msg.header.responsePort, msg.header.responseAdr).sendMessage(response)
                    println("response sent!")
                }
            } else println("got invalid multicast message... ignoring...")
        }
    }

    override val host: String
        get() = Protocol.CLIENT_RESPONSE_ADR
    override val connectionsCount: Int
        get() = nodes.size

    suspend override fun getData(): List<Book> {
        val list = Collections.synchronizedList(mutableListOf<Book>())
        list.addAll(data)
        for (node in nodes) {
            launch(CommonPool) {
                list.addAll(node.getData())
            }
        }
        return data
    }

    fun start() = runBlocking {
        initData()
        val job = launch(CommonPool) {
            handleMulticasts()
        }
        job.join()
    }

    private fun initData() {
        for (i in 0 until Math.abs(Random().nextInt() % 20 + 3))
            data.add(Book(UUID.randomUUID().toString().trim('-'), UUID.randomUUID().toString(), UUID.randomUUID().toString(), Math.abs(Random().nextInt()%115+1900) ))
        println("Node $port initialized with ${data.size} data entries")
    }
}