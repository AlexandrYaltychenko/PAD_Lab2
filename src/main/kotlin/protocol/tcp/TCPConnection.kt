package protocol.tcp

import protocol.asDiscoveryMessage
import protocol.encode
import protocol.message.DiscoveryMessage
import java.io.BufferedReader
import java.io.InputStreamReader
import java.io.PrintWriter
import java.net.Socket

class TCPConnection(port: Int, host: String) {
    private val socket = Socket(host, port)
    private var writer : PrintWriter
    private var reader : BufferedReader

    init {

        writer = PrintWriter(socket.outputStream)
        reader = BufferedReader(InputStreamReader(socket.inputStream))
    }

    fun writeMsg(msg: DiscoveryMessage) {
        writer.println(msg.encode())
        writer.flush()
    }

    fun readMsg(): DiscoveryMessage? {
        try {
            return reader.readLine().asDiscoveryMessage()
        } catch (e: Exception) {
            return null
        }
    }

    fun close() {
        if (isClosed)
            return
        writer.close()
        reader.close()
        socket.close()
    }

    val isClosed: Boolean
        get() = socket.isClosed

}