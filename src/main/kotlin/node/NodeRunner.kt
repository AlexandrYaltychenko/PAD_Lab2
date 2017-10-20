package node

fun main(args: Array<String>) {
    println("enter the number of port...")
    val port = readLine()?.toInt() ?: 4000
    println("enter the ports of associated nodes separated by comma")
    val nodes = mutableListOf<Node>()
    val ports = readLine()
    ports?.let {
        if (ports.isNotEmpty()) {
            val items = ports.trim().split(",")
            items.mapTo(nodes) { RemoteNode(it.toInt()) }
        }
    }
    println("Node started at port $port with ${nodes.size} connections")
    LocalNode(port, nodes).start()
}