package net.dill.y2023.day8

import net.dill.println
import net.dill.readInput
import net.dill.readTestInput

const val DAY = "day8"

fun main() {
    fun part1(input: List<String>): Int {
        val instructions = input[0]
        val network = input.drop(1).drop(1)

        val networkMaps = mutableMapOf<String, Pair<String, String>>()
        network.forEach {
            val node = it.split(" = ")
            networkMaps[node[0]] = Pair(node[1].split(", ")[0].replace("(", ""), node[1].split(", ")[1].replace(")", ""))
        }

        var steps = 0
        var location = "AAA"
        while (location != "ZZZ") {
            instructions.forEach { instruction ->
                when (instruction) {
                    'L' -> location = networkMaps[location]?.first!!
                    'R' -> location = networkMaps[location]?.second!!
                }
                steps++
            }
        }
        return steps
    }

    fun part2(input: List<String>): Long {
        val instructions = input[0]
        val network = input.drop(1).drop(1)

        val networkMaps = mutableMapOf<String, Pair<String, String>>()
        network.forEach {
            val node = it.split(" = ")
            networkMaps[node[0]] = Pair(node[1].split(", ")[0].replace("(", ""), node[1].split(", ")[1].replace(")", ""))
        }

        val counts = networkMaps.keys.filter { it.endsWith("A") }.map { startingPoint ->
            var current = startingPoint
            var count = 0L
            while (!current.endsWith("Z")) {
                instructions.forEach { instruction ->
                    current = when (instruction) {
                        'L' -> networkMaps[current]!!.first
                        'R' -> networkMaps[current]!!.second
                        else -> println("PANIC").toString()
                    }
                }
                count += instructions.length
            }
            count
        }

        return counts.reduce { acc, i -> leastCommonMultiple(acc, i) }
    }

    val testInput1 = readTestInput(DAY, "a")
    val testInput2 = readTestInput(DAY, "b")
    check(part1(testInput1) == 6)
    check(part2(testInput2) == 6L)


    val input = readInput(DAY)
    part1(input).println()
    part2(input).println()
}

fun leastCommonMultiple(a: Long, b: Long): Long {
    return a * b / greatestCommonDivisor(a, b)
}

tailrec fun greatestCommonDivisor(a: Long, b: Long): Long {
    return if (b == 0L) a
    else greatestCommonDivisor(b, a % b)
}