package net.dill.y2023.day1

import net.dill.println
import net.dill.readInput
import net.dill.readTestInput

const val DAY = "day1"

fun main() {
    fun part1(input: List<String>): Long {

        var sum = 0L
        input.forEach {
            val (first, last) = findFirstAndLastSingleDigits(it)
            sum += (first.toString() + last.toString()).toLong()
        }

        return sum
    }

    fun part2(input: List<String>): Long {
        var sum = 0L
        input.forEach {
            val (first, last) = findFirstAndLastSingleDigitsWithText(it)
            sum += (first.toString() + last.toString()).toLong()
        }

        return sum
    }

    val testInput1 = readTestInput(DAY, "a")
    val testInput2 = readTestInput(DAY, "b")
    check(part1(testInput1) == 142L)
    check(part2(testInput2) == 281L)

    val input = readInput(DAY)
    part1(input).println()
    part2(input).println()
}

private fun findFirstAndLastSingleDigits(input: String): Pair<Int?, Int?> {
    val regex = Regex("\\d")
    val matches = regex.findAll(input).map { it.value.toInt() }.toList()

    val first = matches.first()
    val last = matches.last()

    return Pair(first, last)
}

private fun findFirstAndLastSingleDigitsWithText(input: String): Pair<Int?, Int?> {
    val numberMap = mapOf(
        "one" to 1, "two" to 2, "three" to 3, "four" to 4,
        "five" to 5, "six" to 6, "seven" to 7, "eight" to 8, "nine" to 9
    )

    val regex = Regex("(?:[1-9]|one|two|three|four|five|six|seven|eight|nine)")
    val matches = mutableListOf<String>()

    for (i in input.indices) {
        val substring = input.substring(i)
        val match = regex.find(substring)?.value
        match?.let { matches.add(it) }
    }

    val first = matches.first().toIntOrNull() ?: numberMap[matches.first()]
    val last = matches.last().toIntOrNull()  ?: numberMap[matches.last()]

    return Pair(first, last)
}

