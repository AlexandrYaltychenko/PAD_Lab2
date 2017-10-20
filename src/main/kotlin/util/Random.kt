package util

import java.util.*

fun Random.randomIndex(min: Int = 0, max: Int): Int {
    if (max <= 0)
        return 0
    return Math.abs(this.nextInt()) % max + min
}