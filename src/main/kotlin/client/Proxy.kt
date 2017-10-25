package client

import NodeConfig
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import node.LocalNode
import node.RemoteNode
import protocol.Protocol
import util.StringFromFile

class Proxy : LocalNode(Protocol.PROXY_PORT,
        Gson().fromJson<List<NodeConfig>>(StringFromFile("config.json"),
                object : TypeToken<List<NodeConfig>>() {}.type).map { RemoteNode(it.port, it.host) },
        dataCount = 0) {

    override fun handleMulticasts() {
        //not supporting UDP
    }
}