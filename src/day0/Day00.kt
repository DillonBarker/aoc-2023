package day0

import println
import readInput

fun main() {
    fun part1(input: List<String>): Int {
        var calories = 0
        var maxCalories = 0

        input.forEach { item ->
            if (item.isEmpty()) {
                maxCalories = maxOf(maxCalories, calories)
                calories = 0
            } else {
                calories += item.toInt()
            }
        }

        return maxOf(maxCalories, calories)
    }

    fun part2(input: List<String>): Int {
        var calories = 0
        val list = mutableListOf<Int>()

        input.forEach { item ->
            if (item.isEmpty()) {
                list.add(calories)
                calories = 0
            } else {
                calories += item.toInt()
            }
        }

        if (calories > 0) {
            list.add(calories)
        }

        return list.sortedByDescending { it }.take(3).sum()
    }

    fun part2Improved(input: List<String>): Int {
        val calorieGroups = input
            .fold(mutableListOf(0)) { acc, item ->
                if (item.isEmpty()) acc.add(0)
                else acc[acc.size - 1] += item.toInt()
                acc
            }
            .filter { it > 0 }
            .sortedDescending()
            .take(3)

        return calorieGroups.sum()
    }


    val testInput = readInput("day0", "Day00_test")
    check(part1(testInput) == 24000)
    check(part2(testInput) == 45000)
    check(part2Improved(testInput) == 45000)

    val input = readInput("day0", "Day00")
    part1(input).println()
    part2(input).println()
}
