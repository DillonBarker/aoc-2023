package net.dill.y2023.day5

import net.dill.*
import net.dill.readTestInputBlocks

const val DAY = "day5"

fun main() {
    fun part1(input: String): Long {
        val cleanedInput = input.split("\n\n")
        val seeds = cleanedInput[0].split("seeds: ")[1].split(" ")

        // seed-to-soil-map
        val seedToSoil = getPairs(cleanedInput[1])

        // soil-to-fertilizer-map
        val soilToFertilizer = getPairs(cleanedInput[2])

        // fertilizer-to-water-map
        val fertilizerToWater = getPairs(cleanedInput[3])

        // water-to-light-map
        val waterToLight = getPairs(cleanedInput[4])

        // light-to-temperature-map
        val lightToTemperature = getPairs(cleanedInput[5])

        // temperature-to-humidity-map
        val temperatureToHumidity = getPairs(cleanedInput[6])

        // humidity-to-location-map
        val humidityToLocation = getPairs(cleanedInput[7])


        // for each seed go through process to get location number
        val locations = mutableListOf<Long>()
        seeds.forEach { seed ->
            val soil = getValue(seedToSoil, seed.toLong())
            val fertilizer = getValue(soilToFertilizer, soil)
            val water = getValue(fertilizerToWater, fertilizer)
            val light = getValue(waterToLight, water)
            val temperature = getValue(lightToTemperature, light)
            val humidity = getValue(temperatureToHumidity, temperature)
            val location = getValue(humidityToLocation, humidity)
            locations += location
        }

        return locations.min()
    }

    fun part2(input: List<String>): Int {
        return input.size
    }

    val testInput1 = readTestInputBlocks(DAY, "a")
    val testInput2 = readTestInput(DAY, "b")
    check(part1(testInput1) == 35L)
    check(part2(testInput2) == 0)


    val input = readInputBlocks(DAY)
    part1(input).println()
//    part2(input).println()
}

fun getValue(map: List<Pair<Long, Long>>, previous: Long): Long {
    val value: Long
    val foundValue = map.find { it.first == previous }
    value = foundValue?.second ?: previous
    return value
}

fun getPairs(input: String): List<Pair<Long, Long>> {
    val pairList = mutableListOf<Pair<Long,Long>>()
    val size = input.split("\n").size - 1

    for (each in 1..size) {
        val map = input.split("\n")[each].split(" ")
        val destinationRangeStart = map[0]
        val sourceRangeStart = map[1]
        val rangeLength = map[2]

        for (i in 0..<rangeLength.toInt()) {
            val pair = sourceRangeStart.toLong() + i to destinationRangeStart.toLong() + i
            pairList.add(pair)
        }
    }
    return pairList
}
