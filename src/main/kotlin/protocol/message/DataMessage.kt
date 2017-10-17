package protocol.message

import data.Book

data class DataMessage(val query: String?, val level : Int = 0, val data : List<Book>) : Message