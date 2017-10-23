package node

import data.Book

interface QueryProcessor{
    fun applySort(data : List<Book>) : MutableList<Book>
    fun applyGroup(data : List<Book>) : MutableList<Book>
    fun applyFilter(data : List<Book>) : MutableList<Book>
    fun applySelect(data : List<Book>) : MutableList<Book>
}