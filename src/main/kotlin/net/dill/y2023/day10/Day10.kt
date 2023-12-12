package net.dill.y2023.day10

import net.dill.println
import net.dill.readInput
import net.dill.readTestInput

const val DAY = "day10"

fun findCharPosition(grid: List<List<Char>>, targetChar: Char): Triple<Char, Int, Int> {
    var triple = Triple('.', 0, 0)
    for ((rowIndex, rowList) in grid.withIndex()) {
        for ((colIndex, char) in rowList.withIndex()) {
            if (char == targetChar) {
                triple = return Triple(char, rowIndex, colIndex)
            }
        }
    }
    return triple
}

fun nextPath(grid: List<List<Char>>, from: Triple<Char, Int, Int>): Triple<Char, Int, Int> {
    // search north, south, east and west
    val triple = Triple('.', 0, 0)
    val potentialTriples = mutableListOf<Triple<, Int, Int>>()
    // check north
    if (from.second > 0) {
        val charNorth = grid[from.second - 1][from.third]
        when (charNorth) {
            '|' -> potentialTriples.add(Triple(from.first, from.second - 1, from.third))
            '7' -> potentialTriples.add(Triple(from.first, from.second - 1, from.third))
            'F' -> potentialTriples.add(Triple(from.first, from.second - 1, from.third))
            else -> {}
        }
    }


    // check south
    if (from.second < grid.size - 1) {
        val charSouth = grid[from.second + 1][from.third]
        when (charSouth) {
            '|' -> potentialTriples.add(Triple(from.first, from.second + 1, from.third))
            'L' -> potentialTriples.add(Triple(from.first, from.second + 1, from.third))
            'J' -> potentialTriples.add(Triple(from.first, from.second + 1, from.third))
            else -> {}
        }
    }

    // check east
    if (from.third < grid.size - 1) {
        val charEast = grid[from.second][from.third + 1]
        when (charEast) {
            '-' -> potentialTriples.add(Triple(from.first, from.second, from.third + 1))
            '7' -> potentialTriples.add(Triple(from.first, from.second, from.third + 1))
            'J' -> potentialTriples.add(Triple(from.first, from.second, from.third + 1))
            else -> {}
        }
    }

    // check west
    if (from.third > 0) {
        val charWest = grid[from.second][from.third - 1]
        when (charWest) {
            '-' -> potentialTriples.add(Triple(from.first, from.second, from.third - 1))
            'L' -> potentialTriples.add(Triple(from.first, from.second, from.third - 1))
            'F' -> potentialTriples.add(Triple(from.first, from.second, from.third - 1))
            else -> {}
        }
    }
    return triple
}

fun main() {
    fun part1(input: List<String>): Int {
        val parsedInput = mutableListOf<MutableList<Char>>()
        input.forEach { line ->
            val parsedLine = mutableListOf<Char>()
            line.forEach { char ->
                parsedLine.add(char)
            }
            parsedInput.add(parsedLine)
        }

        val start = findCharPosition(parsedInput, 'S') // Y, X

        val paths = nextPath(parsedInput, start)


        var count = 0
        return count
    }

    fun part2(input: List<String>): Int {
        return input.size
    }

    val testInput1 = readTestInput(DAY, "a")
    val testInput2 = readTestInput(DAY, "b")
    check(part1(testInput1) == 4)
    check(part2(testInput2) == 0)


    val input = readInput(DAY)
    part1(input).println()
    part2(input).println()
}
