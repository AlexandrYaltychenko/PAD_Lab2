package util

import java.io.File

fun StringFromFile(filename : String) : String {
    return File(filename).bufferedReader().use { it.readText() }
}