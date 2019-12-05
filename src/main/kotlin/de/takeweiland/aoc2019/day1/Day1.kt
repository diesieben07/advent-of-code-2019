package de.takeweiland.aoc2019.day1

import de.takeweiland.aoc2019.inputReader

fun main() {
    val totalFuel = inputReader(1).useLines { lines ->
        lines.sumBy { calcFuel(it.toInt()) }
    }
    println(totalFuel)

    val totalFuelInclusive = inputReader(1).useLines { lines ->
        lines.sumBy {
            calcFuelRecursively(
                calcFuel(
                    it.toInt()
                )
            )
        }
    }
    println(totalFuelInclusive)
}

private tailrec fun calcFuelRecursively(initialFuel: Int, add: Int = 0): Int {
    val additionalFuel = calcFuel(initialFuel)
    return if (additionalFuel <= 0) {
        initialFuel + add
    } else {
        calcFuelRecursively(additionalFuel, initialFuel + add)
    }
}

private fun calcFuel(mass: Int): Int = mass / 3 - 2