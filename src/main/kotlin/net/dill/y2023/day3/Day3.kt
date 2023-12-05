package net.dill.y2023.day3

import net.dill.println
import net.dill.readInput
import net.dill.readTestInput

const val DAY = "day3"

fun searchLineBelow(input: List<String>, lineIndex: Int, indexes: List<Int>): Boolean {
    var foundSymbol = false
    val lineBelow = input[lineIndex + 1]
    indexes.forEach {
        if (isSymbol(lineBelow[it].toString())) foundSymbol = true
    }

    val sameLine = input[lineIndex]
    val before = indexes.first() - 1
    val after = indexes.last() + 1
    if (before >= 0) {
        if (isSymbol(sameLine[before].toString())) foundSymbol = true
    }
    if (after < sameLine.length) {
        if (isSymbol(sameLine[after].toString())) foundSymbol = true
    }

    val diagonalBefore = indexes.first() - 1
    val diagonalAfter = indexes.last() + 1
    if (diagonalBefore >= 0) {
        if (isSymbol(lineBelow[diagonalBefore].toString())) foundSymbol = true
    }
    if (diagonalAfter < lineBelow.length) {
        if (isSymbol(lineBelow[diagonalAfter].toString())) foundSymbol = true
    }
    return foundSymbol
}

fun searchLineAbove(input: List<String>, lineIndex: Int, indexes: List<Int>): Boolean {
    var foundSymbol = false
    val lineAbove = input[lineIndex - 1]

    indexes.forEach {
        if (isSymbol(lineAbove[it].toString())) foundSymbol = true
    }

    val sameLine = input[lineIndex]
    val before = indexes.first() - 1
    val after = indexes.last() + 1
    if (before >= 0) {
        if (isSymbol(sameLine[before].toString())) foundSymbol = true
    }
    if (after < sameLine.length) {
        if (isSymbol(sameLine[after].toString())) foundSymbol = true
    }

    val diagonalBefore = indexes.first() - 1
    val diagonalAfter = indexes.last() + 1
    if (diagonalBefore >= 0) {
        if (isSymbol(lineAbove[diagonalBefore].toString())) foundSymbol = true
    }
    if (diagonalAfter < lineAbove.length) {
        if (isSymbol(lineAbove[diagonalAfter].toString())) foundSymbol = true
    }
    return foundSymbol
}

fun checkAround(input: List<String>, lineIndex: Int, indexes: List<Int>): Boolean {
    if (indexes.isEmpty()) return false
    if (lineIndex == 0) {
        return searchLineBelow(input, lineIndex, indexes)
    } else if (lineIndex == input.size - 1) {
        return  searchLineAbove(input, lineIndex, indexes)
    } else {
        if (searchLineBelow(input, lineIndex, indexes)) return true
        if (searchLineAbove(input, lineIndex, indexes)) return true
    }
    return false
}

fun main() {
    fun part1(input: List<String>): Int {
        val parts = mutableListOf<Int>()
        input.forEachIndexed { lineIndex, line ->
            val splitLine = line.split("")
            var numbers = ""
            val indexes = mutableListOf<Int>()
            splitLine.forEachIndexed { itemIndex, item ->
                if (itemIndex == splitLine.size - 1) {
                    val symbolFound = checkAround(input, lineIndex, indexes)
                    if (symbolFound) parts += numbers.toInt()
                    numbers = ""
                    indexes.clear()
                }
                if (isNumber(item)) {
                    numbers += item
                    indexes += itemIndex - 1
                }
                if (isSymbol(item)) {
                    if (numbers.isNotEmpty()) parts += numbers.toInt()
                    numbers = ""
                    indexes.clear()
                }
                if (isPeriod(item)) {
                    val symbolFound = checkAround(input, lineIndex, indexes)
                    if (symbolFound) parts += numbers.toInt()
                    numbers = ""
                    indexes.clear()
                }
            }
        }
        return parts.sum()
    }

    fun part2(input: List<String>): Int {
        val gearRatios = mutableListOf<Int>()
        input.forEachIndexed { lineIndex, line ->
            val splitLine = line.split("")
            splitLine.forEachIndexed { itemIndex, item ->
                if (isGear(item)) {
                    val gearRatio = searchAroundGear(input, (itemIndex -1), lineIndex)
                    gearRatios += gearRatio
                }
            }
        }
        return gearRatios.sum()
    }

    val testInput1 = readTestInput(DAY, "a")
    val testInput2 = readTestInput(DAY, "b")
    check(part1(testInput1) == 4361)
    check(part2(testInput2) == 467835)


    val input = readInput(DAY)
    part1(input).println()
    part2(input).println()
}

fun searchAroundGear(input: List<String>, itemIndex: Int, lineIndex: Int): Int {
    val powers = mutableListOf<Int>()
    val lineBelow = input[lineIndex + 1]
    val lineAbove = input[lineIndex - 1]
    val sameLine = input[lineIndex]

    if (lineIndex == 0) {
        // there are none
    }else if (lineIndex == input.size - 1) {
        // there are none
    } else {
        // search all
        if (isNumber(sameLine[itemIndex - 1].toString())) {
            val number = sameLine.findAttachedNumberReversed(itemIndex - 1)
            if (number.isNotEmpty()) powers += number.toInt()
        }
        if (isNumber(sameLine[itemIndex + 1].toString())) {
            val number = sameLine.findAttachedNumber(itemIndex + 1)
            if (number.isNotEmpty()) powers += number.toInt()
        }
        var joinedNumberAbove = false
        if (isNumber(lineAbove[itemIndex].toString())) {
            val numberBefore = lineAbove.findAttachedNumberReversed(itemIndex)
            val numberAfter = lineAbove.findAttachedNumber(itemIndex)
            val number = numberBefore.dropLast(1) + numberAfter
            if (number.isNotEmpty()) {
                joinedNumberAbove = true
                powers += number.toInt()
            }
        }
        if (isNumber(lineAbove[itemIndex - 1].toString()) && joinedNumberAbove != true) {
            val number = lineAbove.findAttachedNumberReversed(itemIndex - 1)
            if (number.isNotEmpty()) powers += number.toInt()
        }
        if (isNumber(lineAbove[itemIndex + 1].toString()) && joinedNumberAbove != true) {
            val number = lineAbove.findAttachedNumber(itemIndex + 1)
            if (number.isNotEmpty()) powers += number.toInt()
        }
        var joinedNumberBelow = false
        if (isNumber(lineBelow[itemIndex].toString())) {
            val numberBefore = lineBelow.findAttachedNumberReversed(itemIndex)
            val numberAfter = lineBelow.findAttachedNumber(itemIndex)
            val number = numberBefore.dropLast(1) + numberAfter
            if (number.isNotEmpty()) {
                joinedNumberBelow = true
                powers += number.toInt()
            }
        }
        if (isNumber(lineBelow[itemIndex - 1].toString()) && joinedNumberBelow != true) {
            val number = lineBelow.findAttachedNumberReversed(itemIndex - 1)
            if (number.isNotEmpty()) powers += number.toInt()
        }
        if (isNumber(lineBelow[itemIndex + 1].toString()) && joinedNumberBelow != true) {
            val number = lineBelow.findAttachedNumber(itemIndex + 1)
            if (number.isNotEmpty()) powers += number.toInt()
        }
    }
    return if (powers.size > 1) powers.reduce { acc, i ->  acc * i }
    else 0
}

fun String.findAttachedNumber(index: Int): String {
    return substring(index).takeWhile { it.isDigit() }
}

fun String.findAttachedNumberReversed(index: Int): String {
    if (index < 0 || index >= length || !get(index).isDigit()) {
        return ""
    }

    val reversedSubstring = substring(0, index+1).reversed()
    return reversedSubstring.takeWhile { it.isDigit() }.reversed()
}

fun isNumber(input: String): Boolean {
    val regex = Regex("\\d")
    return input.matches(regex)
}

fun isSymbol(input: String): Boolean {
    if (isNumber(input)) return false
    return input != "."
}

fun isPeriod(input: String): Boolean {
    return input == "."
}

fun isGear(input: String): Boolean {
    return input == "*"
}
