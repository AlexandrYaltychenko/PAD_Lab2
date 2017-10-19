package node

import data.Book
import kotlinx.coroutines.experimental.*
import protocol.Protocol
import protocol.message.*
import protocol.tcp.TCPConnection
import protocol.udp.MulticastListener
import protocol.udp.UDPSender
import java.net.ServerSocket
import java.util.*
import java.util.concurrent.ConcurrentHashMap


class LocalNode(override val port: Int, private val nodes: List<Node>) : Node {
    private val data = mutableListOf<Book>()
    private val history = ConcurrentHashMap<String, Long>()

    private suspend fun handleMulticasts() {
        val client = MulticastListener(Protocol.MULTICAST_PORT, Protocol.MULTICAST_ADR)
        while (true) {
            println("waiting for a multicast...")
            val msg = client.catchMulticast()
            if (msg != null) {
                launch(CommonPool) {
                    println("got multicast message $msg")
                    val response = DiscoveryMessage(DiscoveryHeader(senderType = SenderType.NODE, responsePort = port), connections = nodes.size)
                    println("sending response $response")
                    val delay = Math.abs(Random().nextInt() % 6000 + 1000).toLong()
                    println("delaying $delay")
                    delay(delay)
                    UDPSender(msg.header.responsePort, msg.header.responseAdr).sendMessage(response)
                    println("response sent!")
                }
            } else println("got invalid multicast message... ignoring...")
        }
    }

    private suspend fun handleTCP() {
        val server = ServerSocket(port)
        while (true) {
            val connection = TCPConnection(server.accept())
            launch(CommonPool) {
                val msg = connection.readMsg()
                if (msg != null) {
                    println("got query = $msg")
                    val data = Collections.synchronizedList(mutableListOf<Book>())
                    data.addAll(getData(DataMessage(DataHeader(senderType = msg.header.senderType, asked = msg.header.asked),
                            uid = msg.uid, query = msg.query, level = msg.level)).data)
                    val response = DataMessage(DataHeader(senderType = SenderType.NODE,
                            messageType = MessageType.TCP_QUERY, asked = msg.header.asked),
                            uid = msg.uid, query = msg.query, level = msg.level, data = data)
                    connection.writeMsg(response)
                } else
                    println("got invalid query... ignoring...")
                connection.close()
            }
        }
    }

    override val host: String
        get() = Protocol.CLIENT_RESPONSE_ADR
    override val connectionsCount: Int
        get() = nodes.size

    suspend override fun getData(request: DataMessage): DataMessage {
        if (request.header.asked.contains(port) || history.contains(request.uid))
            return DataMessage(DataHeader(SenderType.NODE,
                    asked = request.header.asked),
                    uid = request.uid,
                    query = request.query,
                    data = listOf())
        val asked = request.header.asked
        val query = request.query
        val level = request.level
        val uid = request.uid
        history[request.uid] = System.currentTimeMillis()
        asked.add(port)
        println("processing query $query with level $level")
        val list = Collections.synchronizedList(mutableListOf<Book>())
        list.addAll(data)
        val jobs = Collections.synchronizedList(mutableListOf<Job>())
        if (level > 0) {
            nodes.mapTo(jobs) {
                launch(CommonPool) {
                    if (!asked.contains(it.port)) {
                        val data = it.getData(DataMessage(DataHeader(SenderType.NODE, messageType = MessageType.TCP_QUERY, asked = asked),
                                uid = uid, query = query, level = level - 1))
                        list.addAll(data.data)
                    }
                }
            }
        }
        for (job in jobs) {
            job.join()
        }
        return DataMessage(DataHeader(SenderType.NODE, MessageType.TCP_RESULT, asked = asked), uid, query, level, list)
    }

    fun start() = runBlocking {
        initData()
        val multicastListener = launch(CommonPool) {
            handleMulticasts()
        }
        val dataCollector = launch(CommonPool) {
            handleTCP()
        }
        multicastListener.join()
        dataCollector.join()
    }

    private fun initData() {
        for (i in 0 until Math.abs(Random().nextInt() % 20 + 3))
            data.add(Book(UUID.randomUUID().toString().trim('-'), UUID.randomUUID().toString(), UUID.randomUUID().toString(), Math.abs(Random().nextInt() % 115 + 1900)))
        println("Node $port initialized with ${data.size} data entries")
    }
}