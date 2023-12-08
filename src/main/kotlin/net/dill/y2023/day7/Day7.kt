package net.dill.y2023.day7

import net.dill.println
import net.dill.readInput
import net.dill.readTestInput

const val DAY = "day7"

// Didn't solve this one myself, worked with solution online to help me solve - need to retry with my own solution.

fun main() {
    data class Hand(val cards: List<Int>, val groups: List<Int>, val bid: Int)

    fun part1(input: List<String>): Int {
        val values = listOf('T', 'J', 'Q', 'K', 'A')

        val result = input.asSequence().map { it.split(" ") }.map { (hand, bid) ->
                val cards = hand.map { card -> values.indexOf(card).let { if (it > -1) it + 10 else card.digitToInt() } }
                val groups = cards.groupBy { it }.map { it.value.size }.sortedByDescending { it }
                Hand(cards, groups, bid.toInt())
            }
            .sortedWith(
                compareBy(
                    { it.groups[0] },
                    { it.groups[1] },
                    { it.cards[0] },
                    { it.cards[1] },
                    { it.cards[2] },
                    { it.cards[3] },
                    { it.cards[4] })
            )
            .mapIndexed { index, hand -> (index + 1) * hand.bid }
            .sum()
        return result
    }

    fun part2(input: List<String>): Int {
        val values = listOf('T', 'Q', 'K', 'A')
        val result = input.asSequence().map { it.split(" ") }.map { (hand, bid) ->
                val cards = hand.map { card -> values.indexOf(card).let { if (it > -1) it + 10 else card.digitToIntOrNull() ?: 1 } }
                val groups = (2..13)
                    .map { swap -> cards.map { if (it == 1) swap else it }.groupBy { it }.map { it.value.size }.sortedByDescending { it } }
                    .sortedWith(compareBy({ it[0] }, { it.getOrNull(1) }))
                    .last()
                Hand(cards, groups, bid.toInt())
            }
            .sortedWith(
                compareBy(
                    { it.groups[0] },
                    { it.groups.getOrNull(1) },
                    { it.cards[0] },
                    { it.cards[1] },
                    { it.cards[2] },
                    { it.cards[3] },
                    { it.cards[4] })
            )
            .mapIndexed { index, hand -> (index + 1) * hand.bid }
            .sum()
        return result
    }

    val testInput1 = readTestInput(DAY, "a")
    val testInput2 = readTestInput(DAY, "b")
    check(part1(testInput1) == 6440)
    check(part2(testInput2) == 5905)


    val input = readInput(DAY)
    part1(input).println()
    part2(input).println()
}
