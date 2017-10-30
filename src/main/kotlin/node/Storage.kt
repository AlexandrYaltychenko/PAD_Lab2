package node

import data.Book
import protocol.Query

interface Storage {
    fun getData() : MutableList<Book>
    val count : Int
}