package node

import data.Book
import protocol.Protocol
import protocol.Query
import protocol.asQuery
import java.util.Comparator

class DefaultQueryProcessor(strQuery : String) : QueryProcessor{
    private val query : Query = strQuery.asQuery() ?: Query()

    override fun applySort(data: List<Book>): MutableList<Book> {
        val comparator = Comparator<Book> { o1, o2 ->
            query.sort.forEach {
                if (Protocol.QUERY_FIELD.contains(it)) {
                    println("comparing by $it")
                    val result = when (it) {
                        "author" -> o1.author.compareTo(o2.author)
                        "desc" -> o1.desc.compareTo(o2.desc)
                        "title" -> o1.title.compareTo(o2.title)
                        else -> o1.year.compareTo(o2.year)
                    }
                    if (result != 0)
                        return@Comparator result
                }
            }
            0
        }
        return data.sortedWith(comparator).toMutableList()
    }

    override fun applyGroup(data: List<Book>): MutableList<Book> {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    override fun applyFilter(data: List<Book>): MutableList<Book> {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    override fun applySelect(data: List<Book>): MutableList<Book> {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }
}