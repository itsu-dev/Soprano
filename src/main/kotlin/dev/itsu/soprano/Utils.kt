package dev.itsu.soprano

import java.io.File
import java.io.FileOutputStream
import java.nio.charset.StandardCharsets

object Utils {
    fun log(text: String) {
        FileOutputStream(File("log.txt"), true).bufferedWriter(StandardCharsets.UTF_8).use {
            it.append(text)
        }
    }

    fun log(e: Exception) {
        log(e.stackTraceToString())
    }

    fun readLog() = File("log.txt").bufferedReader(StandardCharsets.UTF_8).use { it.readText() }

    fun clearLog() = File("log.txt").delete()
}