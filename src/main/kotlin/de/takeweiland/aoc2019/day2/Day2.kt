package de.takeweiland.aoc2019.day2

import de.takeweiland.aoc2019.input
import de.takeweiland.aoc2019.inputReader

fun main() {
    val memory = input(2).split(',').map { it.toInt() }.toIntArray()
//    val memory = intArrayOf(1,1,1,4,99,5,6,0,99)
    val memCopy = memory.copyOf()
    memCopy[1] = 12
    memCopy[2] = 2
    executeProgram(memCopy)
    println(memCopy.contentToString())

    println(findOutputValue(memory, 19690720))
}

private fun findOutputValue(memory: IntArray, desiredValue: Int): Int {
    for (noun in 0..99) {
        for (verb in 0..99) {
            val memCopy = memory.copyOf()
            memCopy[1] = noun
            memCopy[2] = verb
            executeProgram(memCopy)
            if (memCopy[0] == desiredValue) {
                return 100 * noun + verb
            }
        }
    }
    throw Exception("value not found")
}

private fun executeProgram(memory: IntArray) {
    var pos = 0
    do {
        val instruction = memory[pos]
        when (instruction) {
            1 -> memory[memory[pos + 3]] = memory[memory[pos + 1]] + memory[memory[pos + 2]]
            2 -> memory[memory[pos + 3]] = memory[memory[pos + 1]] * memory[memory[pos + 2]]
            99 -> {}
            else -> throw Exception("Invalid Opcode $instruction")
        }
        pos += 4
    } while (instruction != 99)
}