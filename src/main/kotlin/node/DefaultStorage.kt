package node

import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import data.Base
import data.Book
import protocol.Query
import util.StringFromFile
import util.randomIndex
import java.util.*

class DefaultStorage (src : Int, dataCount : Int) : Storage {
    private val data = initData(src, dataCount)
    override val count: Int
        get() = data.size

    override fun getData(): MutableList<Book> {
        return data
    }

    companion object {
        private fun initData(src : Int, dataCount: Int) : MutableList<Book> {
            val random = Random()
            val data = mutableListOf<Book>()
            val base: Base = Gson().fromJson(StringFromFile("base.json"), (object : TypeToken<Base>() {}).type)
            for (i in 0 until dataCount) {
                data.add(Book(base.titles[random.randomIndex(max = base.titles.size)],
                        base.authors[random.randomIndex(max = base.authors.size)],
                        base.desc[random.randomIndex(max = base.desc.size)],
                        Math.abs(Random().nextInt() % 115 + 1900),
                        src))
            }
            println("Node $src initialized with ${data.size} data entries")
            return data
        }
    }

}