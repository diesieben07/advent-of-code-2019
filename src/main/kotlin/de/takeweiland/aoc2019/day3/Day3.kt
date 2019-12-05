package de.takeweiland.aoc2019.day3

import de.takeweiland.aoc2019.inputReader
import java.io.FileWriter
import java.util.*
import kotlin.Comparator
import kotlin.collections.ArrayList
import kotlin.collections.HashMap
import kotlin.math.abs

private data class Position(val x: Int, val y: Int) {

    companion object {
        val origin = Position(0, 0)
    }

    operator fun plus(movement: Movement) = Position(
        x = this.x + movement.direction.xMul * movement.length,
        y = this.y + movement.direction.yMul * movement.length
    )

    fun manhattan(other: Position): Int = abs(this.x - other.x) + abs(this.y - other.y)

    override fun toString(): String = "($x,$y)"

}

private class OrderByDistanceTo(private val reference: Position) : Comparator<Position> {
    override fun compare(o1: Position, o2: Position): Int {
        return reference.manhattan(o1).compareTo(reference.manhattan(o2))
    }
}

private data class Movement(val direction: Direction, val length: Int) {
    constructor(spec: String) : this(
        direction = enumValueOf<Direction>(spec[0].toString()),
        length = spec.substring(1).toInt()
    )

    fun asSingleSteps(): List<Movement> {
        return Collections.nCopies(length, copy(length = 1))
    }

    companion object {
        fun list(spec: String) = spec.split(',').map { Movement(it) }
    }
}

private enum class Direction(val xMul: Int = 0, val yMul: Int = 0) {
    U(yMul = 1), D(yMul = -1), L(xMul = -1), R(xMul = 1)
}

private fun readInput(): List<List<Movement>> {
    return inputReader(3).useLines { lines ->
        lines.mapTo(mutableListOf()) { line ->
            Movement.list(line)
        }
    }
}

private fun findIntersections(directions: List<List<Movement>>): SortedSet<Position> {
    val intersections = TreeSet(OrderByDistanceTo(Position.origin))
    val seenPositions = HashMap<Position, MutableSet<Int>>()
    val stepsToPositions = HashMap<Position, IntArray>()

    val map = HashMap<Position, Char>()
    map[Position.origin] = 'o'

    for ((index, lineDirections) in directions.withIndex()) {
        var pos = Position.origin
        var stepCount = 0
        for (dir in lineDirections) {
            for (step in dir.asSingleSteps()) {
                pos += step
                stepCount++
                if (pos != Position.origin) {
                    val prevSteps = stepsToPositions.getOrPut(pos) { IntArray(directions.size) }
                    if (prevSteps[index] == 0) prevSteps[index] = stepCount

                    val set = seenPositions.getOrPut(pos, ::HashSet)
                    set += index
                    if (set.size > 1) {
                        intersections += pos
                        map[pos] = 'X'
                    } else {
                        map[pos] = '0' + index
                    }
                }
            }
        }
    }

//    val minX = map.keys.map { it.x }.min()!!
//    val maxX = map.keys.map { it.x }.max()!!
//    val minY = map.keys.map { it.y }.min()!!
//    val maxY = map.keys.map { it.y }.max()!!
//
//    FileWriter("map.txt").buffered().use { writer ->
//        for (y in maxY downTo minY) {
//            for (x in minX..maxX) {
//                writer.write((map[Position(x, y)] ?: '.').toInt())
//            }
//            writer.newLine()
//        }
//    }

    val bestIntersection = intersections.map { stepsToPositions[it]?.sum() ?: Int.MAX_VALUE }.min()
    println(bestIntersection)

    return intersections
}

fun main() {
    val directions = readInput()
//    val directions = listOf(
//        Movement.list("R75,D30,R83,U83,L12,D49,R71,U7,L72"),
//        Movement.list("U62,R66,U55,R34,D71,R55,D58,R83")
//    )
    val intersections = findIntersections(directions)
    println(intersections)
    println(intersections.firstOrNull()?.manhattan(Position.origin))

}
