package node

import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import data.Base
import data.Book
import protocol.Protocol
import protocol.message.*
import protocol.tcp.TCPConnection
import protocol.udp.MulticastListener
import protocol.udp.UDPSender
import util.StringFromFile
import util.randomIndex
import java.net.ServerSocket
import java.util.*
import java.util.concurrent.ConcurrentHashMap


class LocalNode(override val port: Int, private val nodes: List<Node>, private val dataCount: Int = 0) : Node {
    private val data = mutableListOf<Book>()
    private val history = ConcurrentHashMap<String, Long>()


    private fun handleMulticasts() {
        val client = MulticastListener(Protocol.MULTICAST_PORT, Protocol.MULTICAST_ADR)
        while (true) {
            println("waiting for a multicast...")
            val msg = client.catchMulticast()
            if (msg != null) {
                Thread {
                    println("got multicast message $msg")
                    val response = DiscoveryMessage(DiscoveryHeader(senderType = SenderType.NODE, responsePort = port), connections = nodes.size)
                    println("sending response $response")
                    val delay = Math.abs(Random().nextInt() % 6000 + 1000).toLong()
                    println("delaying $delay")
                    Thread.sleep(delay)
                    UDPSender(msg.header.responsePort, msg.header.responseAdr).sendMessage(response)
                    println("response sent!")
                }.start()
            } else println("got invalid multicast message... ignoring...")
        }
    }

    private fun handleTCP() {
        val server = ServerSocket(port)
        while (true) {
            val connection = TCPConnection(server.accept())
            Thread {
                val msg = connection.readMsg()
                if (msg != null) {
                    println("$port got query = $msg")
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
            }.start()
        }
    }

    override val host: String
        get() = Protocol.CLIENT_RESPONSE_ADR
    override val connectionsCount: Int
        get() = nodes.size

    override fun getData(request: DataMessage): DataMessage {
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
        val jobs = Collections.synchronizedList(mutableListOf<Thread>())
        if (level > 0) {
            nodes.mapTo(jobs) {
                Thread {
                    println("launching coroutine to asks nodes...")
                    if (!asked.contains(it.port)) {
                        val data = it.getData(DataMessage(DataHeader(SenderType.NODE, messageType = MessageType.TCP_QUERY, asked = asked),
                                uid = uid, query = query, level = level - 1))
                        list.addAll(data.data)
                    }
                }
            }
        }
        jobs.forEach {
            it.start()
        }
        jobs.forEach {
            it.join()
        }
        return DataMessage(DataHeader(SenderType.NODE, MessageType.TCP_RESULT, asked = asked), uid, query, level, list)
    }

    fun start() {
        initData()
        Thread {
            handleMulticasts()
        }.start()
        Thread {
            handleTCP()
        }.start()
    }

    private fun initData() {
        val random = Random()
        val base: Base = Gson().fromJson(StringFromFile("base.json"), (object : TypeToken<Base>() {}).type)
        val itemsCount = if (dataCount > 0) dataCount else random.randomIndex(3, 15)
        for (i in 0 until itemsCount) {
            data.add(Book(base.titles[random.randomIndex(max = base.titles.size)],
                    base.authors[random.randomIndex(max = base.authors.size)],
                    base.desc[random.randomIndex(max = base.desc.size)],
                    Math.abs(Random().nextInt() % 115 + 1900),
                    port))
        }
        println("Node $port initialized with ${data.size} data entries")
    }
}