package template

import println
import readInput
import readTestInput

const val DAY = "dayX"

fun main() {
    fun part1(input: List<String>): Int {
        return input.size
    }

    fun part2(input: List<String>): Int {
        return input.size
    }

    val testInput1 = readTestInput(DAY, "a")
    val testInput2 = readTestInput(DAY, "b")
    check(part1(testInput1) == 0)
    check(part2(testInput2) == 0)


    val input = readInput(DAY)
    part1(input).println()
    part2(input).println()
}
