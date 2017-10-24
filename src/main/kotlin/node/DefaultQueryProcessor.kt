package node

import data.Book
import protocol.Protocol
import protocol.Query
import protocol.asQuery
import java.util.Comparator
import kotlin.reflect.KProperty1

class DefaultQueryProcessor(strQuery: String) : QueryProcessor {
    private val query: Query = strQuery.asQuery() ?: Query()

    override fun applySort(data: List<Book>): MutableList<Book> {
        val comparator = Comparator<Book> { o1, o2 ->
            query.sort.forEach {
                if (Protocol.QUERY_FIELD.contains(it)) {
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
        val result = data.toMutableList()
        if (query.group.isEmpty())
            return result
        println("grouping by ${query.group}")
        val params = query.group
                .filter { it -> Protocol.QUERY_FIELD.contains(it) }
                .map { it ->
                    when (it) {
                        "author" -> Book::author
                        "title" -> Book::title
                        "desc" -> Book::desc
                        else -> Book::year
                    }
                }
        val iterator = result.iterator()
        val group = mutableSetOf<Int>()
        while (iterator.hasNext()) {
            val book = iterator.next()
            val hash = params.map { it.get(book) }.hashCode()
            if (group.contains(hash))
                iterator.remove()
            else
                group.add(hash)
        }
        return result
    }

    override fun applyFilter(data: List<Book>): MutableList<Book> {
        val params = query.filter
        println("params $params")
        val result = data.toMutableList()
        if (params.isEmpty())
            return result
        params.forEach { param ->
            val items = param.split(*Protocol.FILTER_OPERATORS.toTypedArray())
            println("items $items")
            if (items.size < 2 || Protocol.QUERY_FIELD.contains(items[0])) {
                val operator = param.findAnyOf(Protocol.FILTER_OPERATORS)?.second
                //println("operator $operator")
                operator?.let {
                    val iterator = result.iterator()
                    while (iterator.hasNext()) {
                        val book = iterator.next()
                        if (!when (items[0]) {
                            "author" -> filterString(book.author, items[1], operator)
                            "title" -> filterString(book.title, items[1], operator)
                            "desc" -> filterString(book.desc, items[1], operator)
                            "year" -> filterInt(book.year, items[1].toInt(), operator)
                            else -> false
                        }) {
                            println("removing $book")
                            iterator.remove()
                        } else
                            println("not removing $book")
                        println("size ${result.size}")
                    }
                }
            }
        }
        return result
    }

    private fun filterInt(a: Int, b: Int, op: String): Boolean {
        return when (op) {
            ">" -> a > b
            "<" -> a < b
            ">=" -> a >= b
            "<=" -> a <= b
            "=" -> a == b
            else -> false
        }
    }

    private fun filterString(a: String, b: String, op: String): Boolean {
        return when (op) {
            ">" -> a > b
            "<" -> a < b
            ">=" -> a >= b
            "<=" -> a <= b
            "=" -> {
                return if (b.contains('*')) {
                    println("regex check $a to $b")
                    a.matches(b.toRegex())
                } else
                    a == b
            }
            else -> false
        }
    }

}