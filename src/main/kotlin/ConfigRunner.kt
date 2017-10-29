import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import kotlinx.coroutines.experimental.CommonPool
import kotlinx.coroutines.experimental.Job
import kotlinx.coroutines.experimental.launch
import kotlinx.coroutines.experimental.runBlocking
import node.LocalNode
import node.RemoteNode
import util.StringFromFile
import java.io.File
import java.util.*

fun main(args: Array<String>) = runBlocking {
    val str = StringFromFile("config.json")
    val result: List<NodeConfig> = Gson().fromJson(str, object : TypeToken<List<NodeConfig>>() {}.type)
    result.forEach {
        Thread {
            println("running node at ${it.port}...")
            LocalNode(it.port, it.nodes.map { RemoteNode(it.port, it.host) }, dataCount = it.dataCount).start()
        }.start()
    }
}

data class NodeConfig(val port: Int, val host: String, val dataCount: Int, val nodes: List<NodeConfig>)