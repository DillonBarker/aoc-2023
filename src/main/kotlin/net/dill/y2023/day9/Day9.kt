package net.dill.y2023.day9

import net.dill.println
import net.dill.readInput
import net.dill.readTestInput
import kotlin.math.absoluteValue

const val DAY = "day9"



fun isComplete(line: List<Int>): Boolean {
    return line.all { it == 0 }
}

fun main() {
    fun part1(input: List<String>): Int {
        var score = 0
        input.forEach { line ->
            val history = mutableListOf<MutableList<Int>>()
            val splitLine = line.split(" ").map { it.toInt() }.toMutableList()

            history.add(splitLine)

            var count = 0

            while (!isComplete(history.last())) {
                val historyLine = mutableListOf<Int>()

                history[count].forEachIndexed { i, v ->
                    if (i < history[count].size - 1) {
                        val dif = history[count][i + 1] - v
                        historyLine.add(dif)
                    }
                }
                history.add(historyLine)
                count ++
            }
            history.reversed().forEachIndexed { i, v ->
                if (i == 0) history.last().add(0)
                else {
                    val added = history.reversed()[i].last() + history.reversed()[i - 1].last()
                    history.reversed()[i].add(added)
                }
            }
            score += history.first().last()
        }
        return score
    }

    fun part2(input: List<String>): Int {
        var score = 0
        input.forEach { line ->
            val history = mutableListOf<MutableList<Int>>()
            val splitLine = line.split(" ").map { it.toInt() }.toMutableList()

            history.add(splitLine)

            var count = 0

            while (!isComplete(history.last())) {
                val historyLine = mutableListOf<Int>()

                history[count].forEachIndexed { i, v ->
                    if (i < history[count].size - 1) {
                        val dif = history[count][i + 1] - v
                        historyLine.add(dif)
                    }
                }
                history.add(historyLine)
                count ++
            }
            history.reversed().forEachIndexed { i, v ->
                if (i == 0) history.last().add(0, 0)
                else {
                    val added = history.reversed()[i].first() - history.reversed()[i - 1].first()
                    history.reversed()[i].add(0, added)
                }
            }
            score += history.first().first()
        }
        return score
    }

    val testInput1 = readTestInput(DAY, "a")
    val testInput2 = readTestInput(DAY, "b")
    check(part1(testInput1) == 114)
    check(part2(testInput2) == 2)


    val input = readInput(DAY)
    part1(input).println()
    part2(input).println()
}
