package net.dill.y2023.day7

import net.dill.println
import net.dill.readInput
import net.dill.readTestInput

const val DAY = "day7"


fun rankHands(hands: MutableMap<String, Int>): List<Int> {
    val fiveOfKindMap = hands.filter {
        isFiveOfAKind(it.key)
    }
    val fourOfKindMap = hands.filter {
        isFourOfAKind(it.key)
    }
    val fullHouseMap = hands.filter {
        isFullHouse(it.key)
    }
    val threeOfKindMap = hands.filter {
        isThreeOfAKind(it.key)
    }
    val twoPairMap = hands.filter {
        isTwoPair(it.key)
    }
    val onePairMap = hands.filter {
        isOnePair(it.key)
    }
    val noPairMap = hands.filter {
        isNoPair(it.key)
    }

    val noPair = orderHandsByStrength(noPairMap)
    val onePair = orderHandsByStrength(onePairMap)
    val twoPair = orderHandsByStrength(twoPairMap)
    val threeOfKind = orderHandsByStrength(threeOfKindMap)
    val fullHouse = orderHandsByStrength(fullHouseMap)
    val fourOfKind = orderHandsByStrength(fourOfKindMap)
    val fiveOfKind = orderHandsByStrength(fiveOfKindMap)
    return fiveOfKind + fourOfKind + fullHouse + threeOfKind + twoPair + onePair + noPair
}

fun orderHandsByStrength(hands: Map<String, Int>): List<Int> {
    val strengthOrder = listOf("A", "K", "Q", "J", "T", "9", "8", "7", "6", "5", "4", "3", "2")

    return hands.entries
        .map { (hand, value) ->
            val strength = hand.map { char -> strengthOrder.indexOf(char.toString()) + 1 }.joinToString("").toInt()
            strength to value
        }
        .sortedBy { it.first }
        .map { (_, value) ->
            value
        }
}

fun isNoPair(string: String): Boolean {
    val charCounts = string.groupingBy { it }.eachCount()

    return charCounts.size == 5
}

fun isOnePair(string: String): Boolean {
    val charCounts = string.groupingBy { it }.eachCount()

    return charCounts.size == 4 && charCounts.containsValue(2) && charCounts.containsValue(1)
}

fun isTwoPair(string: String): Boolean {
    val charCounts = string.groupingBy { it }.eachCount()

    return charCounts.size == 3 && charCounts.containsValue(2) && charCounts.containsValue(1)
}

fun isThreeOfAKind(string: String): Boolean {
    val charCounts = string.groupingBy { it }.eachCount()

    return charCounts.containsValue(3) && charCounts.containsValue(1)
}

fun isFullHouse(string: String): Boolean {
    val charCounts = string.groupingBy { it }.eachCount()

    return charCounts.containsValue(3) && charCounts.containsValue(2)
}

fun isFourOfAKind(string: String): Boolean {
    val charCounts = string.groupingBy { it }.eachCount()

    return charCounts.size == 2 && charCounts.containsValue(4)
}

fun isFiveOfAKind(string: String): Boolean {
    return string.all { it == string[0] }
}

fun main() {
    fun part1(input: List<String>): Int {
        val allHands = mutableMapOf<String, Int>()
        input.forEach {
            val hand = it.split(" ")[0]
            val bid = it.split(" ")[1]
            allHands[hand] = bid.toInt()
        }
        val rankedHands = rankHands(allHands)
        var total = 0
        var size = rankedHands.size

        for (each in rankedHands) {
            total += each * size
            size--
        }
        println(total)
        return total
    }
    //206590105 too low
    //248421062 too low
    //232556724
    fun part2(input: List<String>): Int {
        return input.size
    }

    val testInput1 = readTestInput(DAY, "a")
    val testInput2 = readTestInput(DAY, "b")
    check(part1(testInput1) == 6440)
//    check(part2(testInput2) == 0)


    val input = readInput(DAY)
    part1(input).println()
//    part2(input).println()
}
