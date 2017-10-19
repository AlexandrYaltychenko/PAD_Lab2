package protocol.tcp

import protocol.asDataMessage
import protocol.asDiscoveryMessage
import protocol.encode
import protocol.message.DataMessage
import protocol.message.DiscoveryMessage
import java.io.BufferedReader
import java.io.InputStreamReader
import java.io.PrintWriter
import java.net.Socket

class TCPConnection(private val socket : Socket) {
    private var writer : PrintWriter = PrintWriter(socket.outputStream)
    private var reader : BufferedReader = BufferedReader(InputStreamReader(socket.inputStream))

    constructor(host : String, port : Int) : this(Socket(host,port))

    fun writeMsg(msg: DataMessage) {
        writer.println(msg.encode())
        writer.flush()
    }

    fun readMsg(): DataMessage? {
        try {
            return reader.readLine()?.asDataMessage()
        } catch (e: Exception) {
            e.printStackTrace()
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