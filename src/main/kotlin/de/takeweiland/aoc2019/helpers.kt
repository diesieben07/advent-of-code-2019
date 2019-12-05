package de.takeweiland.aoc2019

import java.io.BufferedReader

private object DummyClass

fun inputReader(day: Int): BufferedReader {
    return DummyClass::class.java.getResourceAsStream("/day$day.txt")
        .bufferedReader()
}

fun input(day: Int): String {
    return inputReader(day).use { it.readText() }
}