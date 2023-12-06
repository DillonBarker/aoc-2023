package net.dill.y2023.day5

import net.dill.*
import java.util.concurrent.Executors
import java.util.concurrent.TimeUnit

const val DAY = "day5"

fun main() {
    fun part1(input: String): Long {
        val cleanedInput = input.split("\n\n")
        val seeds = cleanedInput[0].split("seeds: ")[1].split(" ")

        val locations = mutableListOf<Long>()
        seeds.forEach { seed ->
            val soil = getNext(cleanedInput[1], seed.toLong())
            val fertilizer = getNext(cleanedInput[2], soil)
            val water = getNext(cleanedInput[3], fertilizer)
            val light = getNext(cleanedInput[4], water)
            val temperature = getNext(cleanedInput[5], light)
            val humidity = getNext(cleanedInput[6], temperature)
            val location = getNext(cleanedInput[7], humidity)
            locations += location
        }

        return locations.min()
    }

    fun part2(input: String): Long {
        val cleanedInput = input.split("\n\n")

        val seedInput = cleanedInput[0].split("seeds: ")[1].split(" ")
        val locations = mutableListOf<Long>()

        val seedMap = mutableListOf<LongRange>()
        seedInput.windowed(2, 2, partialWindows = true).forEach { (first, second) ->
            seedMap.add(first.toLong()..first.toLong()+second.toLong())
        }
        println(seedMap)

        seedInput.windowed(2, 2, partialWindows = true).forEach { (first, second) ->
            val localLocations = mutableListOf<Long>()
            for (seed in (first.toLong() until (first.toLong() + second.toLong()))) {
                val location = calculateLocation(cleanedInput, seed)
                localLocations.add(location)
            }
            synchronized(locations) {
                locations.addAll(localLocations)
            }
        }

        return locations.min()
    }

    val testInput1 = readTestInputBlocks(DAY, "a")
    val testInput2 = readTestInputBlocks(DAY, "b")
//    check(part1(testInput1) == 35L)
    check(part2(testInput2) == 46L)

    //65711604

    val input = readInputBlocks(DAY)
//    part1(input).println()
    part2(input).println()
}

fun calculateLocation(input: List<String>, seed: Long): Long {
    val soil = getNext(input[1], seed)
    val fertilizer = getNext(input[2], soil)
    val water = getNext(input[3], fertilizer)
    val light = getNext(input[4], water)
    val temperature = getNext(input[5], light)
    val humidity = getNext(input[6], temperature)
    return getNext(input[7], humidity)
}

fun getValue(map: List<Pair<Long, Long>>, previous: Long): Long {
    val value: Long
    val foundValue = map.find { it.first == previous }
    value = foundValue?.second ?: previous
    return value
}

fun getNext(input: String, source: Long): Long {
    val size = input.split("\n").size - 1
    var next: Long = source
    for (each in 1..size) {
        val map = input.split("\n")[each].split(" ")
        val destinationRangeStart = map[0].toLong()
        val sourceRangeStart = map[1].toLong()
        val rangeLength = map[2].toLong()

        if (source in (sourceRangeStart..<sourceRangeStart+rangeLength)) {
            val diff = destinationRangeStart - sourceRangeStart
            next = diff + source
            break
        }
    }
    return next
}
