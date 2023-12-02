package day2

import println
import readInput
import readTestInput

const val DAY = "day2"

fun main() {
    fun part1(input: List<String>): Int {
        val map = mapOf("blue" to 14, "green" to 13, "red" to 12)
        var count = 0
        var impossible = false

        input.forEach {
            val a = it.split(": ")
            val id = a[0].split(" ")[1]
            val subsets = a[1].split("; ")
            subsets.forEach {
                val each = it.split(", ")
                each.forEach { thing ->
                    val cubesAndColour = (thing.split(" "))
                    if (cubesAndColour[0].toInt() > map[cubesAndColour[1]]!!) {
                        impossible = true
                    }
                }
            }
            if (!impossible) count += id.toInt()
            impossible = false
        }

        return count
    }

    fun part2(input: List<String>): Int {
        var map = mutableMapOf("blue" to 0, "green" to 0, "red" to 0)
        var sum = 0
        input.forEach {
            val a = it.split(": ")

            val subsets = a[1].split("; ")
            subsets.forEach {
                val each = it.split(", ")
                each.forEach { thing ->
                    val cubesAndColour = (thing.split(" "))
                    if (cubesAndColour[0].toInt() > map[cubesAndColour[1]]!!) {
                        map[cubesAndColour[1]] = cubesAndColour[0].toInt()
                    }
                }
            }
            val power = map.values.reduce { acc, i ->  acc * i }

            sum += power
            map = mutableMapOf("blue" to 0, "green" to 0, "red" to 0)
        }

        return sum
    }

    val testInput1 = readTestInput(DAY, "a")
    val testInput2 = readTestInput(DAY, "b")
    check(part1(testInput1) == 8)
    check(part2(testInput2) == 2286)


    val input = readInput(DAY)
    part1(input).println()
    part2(input).println()
}
