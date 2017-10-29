package node

import data.Book
import protocol.Protocol
import protocol.Query
import protocol.asQuery
import java.util.*
import kotlin.reflect.KProperty1
import kotlin.reflect.full.memberProperties

class DefaultQueryProcessor(strQuery: String) : QueryProcessor {
    private val query: Query = strQuery.asQuery() ?: Query()

    override fun applySort(data: List<Book>): MutableList<Book> {
        val comparator = Comparator<Book> { o1, o2 ->
            query.sort.forEach {
                val fields = mutableMapOf<String, KProperty1<Book, *>>()
                for (field in Book::class.memberProperties) {
                    fields[field.name] = field
                }
                val param1 = fields[it]?.get(o1)
                val param2 = fields[it]?.get(o2)
                val result: Int = when (param1) {
                    is Int -> when (param2) {
                        is Int -> param1.compareTo(param2)
                        else -> 0
                    }
                    is String -> when (param2) {
                        is String -> param1.compareTo(param2)
                        else -> 0
                    }
                    else -> 0
                }

                if (result != 0)
                    return@Comparator result
            }
            0
        }
        return data.sortedWith(comparator).toMutableList()
    }

    override fun applyGroup(data: List<Book>): MutableList<Book> {
        val result = data.toMutableList()
        if (query.group.isEmpty())
            return result
        val fields = mutableMapOf<String, KProperty1<Book, *>>()
        for (field in Book::class.memberProperties) {
            fields[field.name] = field
        }
        val params = query.group
                .filter { it -> fields.keys.contains(it) }
                .map { it ->
                    fields[it]
                }
        val iterator = result.iterator()
        val group = mutableSetOf<Int>()
        while (iterator.hasNext()) {
            val book = iterator.next()
            val hash = params.map { it?.get(book) }.hashCode()
            if (group.contains(hash))
                iterator.remove()
            else
                group.add(hash)
        }
        return result
    }

    override fun applyFilter(data: List<Book>): MutableList<Book> {
        val params = query.filter
        val fields = mutableMapOf<String, KProperty1<Book, *>>()
        for (field in Book::class.memberProperties) {
            fields[field.name] = field
        }
        val result = data.toMutableList()
        if (params.isEmpty())
            return result
        params.forEach { param ->
            val items = param.split(*Protocol.FILTER_OPERATORS.toTypedArray())
            println("items $items")
            if (items.size < 2 || fields.keys.contains(items[0])) {
                val operator = param.findAnyOf(Protocol.FILTER_OPERATORS)?.second
                operator?.let {
                    val iterator = result.iterator()
                    while (iterator.hasNext()) {
                        val book = iterator.next()
                        val field = fields[items[0]]
                        if (field != null) {
                            val bookField = field.get(book)
                            bookField?.let {
                                if ((bookField is String && !filter(bookField, items[1], operator))
                                        || (bookField is Int && !filter(bookField, items[1].toInt(), operator))) {
                                    println("removing $book")
                                    iterator.remove()
                                }
                            }
                        } else
                            println("not removing $book")
                        println("size ${result.size}")
                    }
                }
            }
        }
        return result
    }

    private inline fun <reified T> filter(a: Comparable<T>, b: T, op: String): Boolean {
        println("filtering $a  $b by $op")
        return when (op) {
            ">" -> a > b
            "<" -> a < b
            ">=" -> a >= b
            "<=" -> a <= b
            "=" -> when (T::class) {
                String::class -> {
                    return if ((b as String).contains('*')) {
                        println("regex check ${a as String} to $b")
                        a.matches(b.toRegex())
                    } else
                        a == b
                }
                else -> a == b
            }
            else -> false
        }
    }

}