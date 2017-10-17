package node

import data.Book
interface Node {
    val port : Int
    val host : String
    val connectionsCount : Int
    suspend fun getData() : List<Book>
}