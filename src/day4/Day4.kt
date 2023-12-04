package day4

import println
import readInput
import readTestInput

const val DAY = "day4"

fun main() {
    fun part1(input: List<String>): Int {
        var totalPoints = 0
        input.forEach {
            val a = it.split(": ")
            val numbers = a[1].split(" | ")
            val winningNumbers = numbers[0].split(" ").filter { it.isNotEmpty() }
            val myNumbers = numbers[1].split(" ").filter { it.isNotEmpty() }

            var points = 0
            myNumbers.forEach {
                if (winningNumbers.contains(it)) {
                    points ++
                }
            }
            if (points > 1) {
                totalPoints += Math.pow(2.0, (points - 1.toDouble())).toInt()
            } else if (points == 1) {
                totalPoints ++
            }
            points = 0
        }
        return totalPoints
    }

    fun part2(input: List<String>): Int {
        val scratchCards = MutableList(input.size) { 1 }

        input.forEach {
            val a = it.split(": ")
            val id = a[0].split("\\s+".toRegex())[1].toInt()
            val numbers = a[1].split(" | ")
            val winningNumbers = numbers[0].split(" ").filter { it.isNotEmpty() }
            val myNumbers = numbers[1].split(" ").filter { it.isNotEmpty() }

            var winners = 0
            myNumbers.forEach {
                if (winningNumbers.contains(it)) {
                    winners ++
                }
            }

            for (i in id..<id+winners) {
                val copies = scratchCards[id-1]
                scratchCards[i] += 1 * copies
            }

        }

        return scratchCards.sum()
    }

    val testInput1 = readTestInput(DAY, "a")
    val testInput2 = readTestInput(DAY, "b")
    check(part1(testInput1) == 13)
    check(part2(testInput2) == 30)

    val input = readInput(DAY)
    part1(input).println()
    part2(input).println()
}
