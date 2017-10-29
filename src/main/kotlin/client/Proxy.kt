package client

import NodeConfig
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import node.LocalNode
import node.Node
import node.RemoteNode
import protocol.Protocol
import util.StringFromFile

class Proxy : LocalNode(Protocol.PROXY_PORT,
        initNodes(Gson().fromJson<List<NodeConfig>>(StringFromFile("config.json"),
                object : TypeToken<List<NodeConfig>>() {}.type).toMutableList()),
        dataCount = 0) {

    override fun handleMulticasts() {
        //not supporting UDP
    }


    companion object {
        fun initNodes(nodes: MutableList<NodeConfig>): MutableList<Node> {
            val count = nodes.size
            val nodeSet = mutableSetOf<Int>()
            val result = mutableSetOf<Node>()
            while (nodeSet.size < count) {
                val maxNode = nodes
                        .maxBy {
                            it.nodes.filter {
                                !nodeSet.contains(it.port)
                            }.size
                        } ?: break
                result.add(RemoteNode(maxNode.port, maxNode.host))
                nodeSet.add(maxNode.port)
                nodeSet.addAll(maxNode.nodes.map { it.port })
                nodes.removeAll { nodeSet.contains(it.port) }
            }
            println("initialized with nodes ${result.joinToString { it.port.toString() }}")
            return result.toMutableList()
        }
    }

}