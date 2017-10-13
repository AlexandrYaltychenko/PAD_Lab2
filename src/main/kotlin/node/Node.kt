package node

import data.Entry
interface Node {
    val port : Int
    val host : String
    val connectionsCount : Int
    suspend fun getData() : List<Entry>
}