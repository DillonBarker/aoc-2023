package net.dill.y2023.day6

import net.dill.println
import net.dill.readInput
import net.dill.readTestInput

const val DAY = "day6"

fun main() {
    fun part1(input: List<String>): Int {
        val timeLine = input[0].split(": ")[1].split(" ").filter { it.isNotEmpty()}
        val distanceLine = input[1].split(": ")[1].split(" ").filter { it.isNotEmpty()}

        val counts = mutableListOf<Int>()
        for (race in distanceLine.indices) {
            var count = 0
            val time = timeLine[race].toInt()
            val distance = distanceLine[race].toInt()
            for (speed in distance downTo 1) {
                val timeLeft = time - speed
                val distanceTravelled = timeLeft * speed
                if (distanceTravelled > distance) count++
            }
            counts.add(count)
        }

        return counts.reduce { acc, i ->  acc * i }
    }

    fun part2(input: List<String>): Long {
        val timeLine = input[0].split(": ")[1].split(" ").filter { it.isNotEmpty()}
        val distanceLine = input[1].split(": ")[1].split(" ").filter { it.isNotEmpty()}

        val raceTime = timeLine.joinToString(separator = "").toLong()
        val raceDistance = distanceLine.joinToString(separator = "").toLong()

        var count = 0L

        println(raceDistance downTo 1)

        for (speed in raceDistance downTo 1) {
            val timeLeft = raceTime - speed
            val distanceTravelled = timeLeft * speed
            if (distanceTravelled > raceDistance) count++
        }

        return count
    }

    val testInput1 = readTestInput(DAY, "a")
    val testInput2 = readTestInput(DAY, "b")
//    check(part1(testInput1) == 288)
    check(part2(testInput2) == 71503L)


    val input = readInput(DAY)
//    part1(input).println()
    part2(input).println()
}
