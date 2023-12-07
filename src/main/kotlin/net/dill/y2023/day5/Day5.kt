package net.dill.y2023.day5

import net.dill.*

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

        val seedMaps = mutableListOf<LongRange>()

        seedInput.windowed(2, 2, partialWindows = true).forEach { (first, second) ->
            seedMaps.add(first.toLong()..<first.toLong()+second.toLong())
        }
        val soilMaps = calcNextMap(cleanedInput[1], seedMaps)
        println(soilMaps.minBy { it.first }.first)
        val fertilizerMaps = calcNextMap(cleanedInput[2], soilMaps)
        println(fertilizerMaps.minBy { it.first }.first)
        val waterMaps = calcNextMap(cleanedInput[3], fertilizerMaps)
        println(waterMaps.minBy { it.first }.first)
        val lightMaps = calcNextMap(cleanedInput[4], waterMaps)
        println(lightMaps.minBy { it.first }.first)
        val temperatureMaps = calcNextMap(cleanedInput[5], lightMaps)
        println(temperatureMaps.minBy { it.first }.first)
        val humidityMaps = calcNextMap(cleanedInput[6], temperatureMaps)
        println(humidityMaps.minBy { it.first }.first)

        val locationMaps = calcNextMap(cleanedInput[7], humidityMaps)
        println(locationMaps.minBy { it.first }.first)

        return locationMaps.minBy { it.first }.first
    }

    val testInput1 = readTestInputBlocks(DAY, "a")
    val testInput2 = readTestInputBlocks(DAY, "b")
//    check(part1(testInput1) == 35L)
    check(part2(testInput2) == 46L)

    //216635734
    //47909639

    val input = readInputBlocks(DAY)
//    part1(input).println()
    part2(input).println()
}

fun calcNextMap(input: String, previousMaps: MutableList<LongRange>): MutableList<LongRange> {
    val nextMaps = mutableListOf<LongRange>()
    val size = input.split("\n").size - 1
    val unmatchedRanges = mutableListOf<LongRange>()
    for (each in 1..size) {
        val map = input.split("\n")[each].split(" ")
        val destinationRangeStart = map[0].toLong()
        val sourceRangeStart = map[1].toLong()
        val rangeLength = map[2].toLong()

        if (unmatchedRanges.isNotEmpty()) {
            previousMaps.addAll(unmatchedRanges)
            unmatchedRanges.clear()
        }

        val iterator = previousMaps.iterator()

        while (iterator.hasNext()) {
            val previousMap = iterator.next()

            val intersectionType = checkIntersection(previousMap, sourceRangeStart..sourceRangeStart+rangeLength)

            when (intersectionType) {
                IntersectionType.PARTIAL_CROSSOVER -> {
                    val split = splitRangesByPartialCrossover(previousMap, sourceRangeStart..sourceRangeStart+rangeLength)
                    val soilMapNoCross = split.noCrossover
                    val soilMapCross = split.crossover
                    if (soilMapNoCross != null) {
                        if (each == size) nextMaps.add(previousMap)
                    }
                    if (soilMapCross != null) {
                        val diff = destinationRangeStart - sourceRangeStart
                        val nextMap = diff + soilMapCross.first..diff + soilMapCross.last
                                nextMaps.add(nextMap)
                        if (soilMapNoCross != null) unmatchedRanges.add(soilMapNoCross)
                        iterator.remove()
                    }
                }
                IntersectionType.NO_CROSSOVER -> {
                    if (each == size) {
                        nextMaps.add(previousMap)
                        iterator.remove()
                    }
                }
                IntersectionType.TOTAL_CROSSOVER -> {
                    val diff = destinationRangeStart - sourceRangeStart
                    val nextMap = diff + previousMap.first..diff + previousMap.last
                    nextMaps.add(nextMap)
                    iterator.remove()
                }
            }
        }
    }

    return mergeOverlappingRanges(nextMaps)
}

fun mergeOverlappingRanges(ranges: List<LongRange>): MutableList<LongRange> {
    val sortedRanges = ranges.sortedBy { it.first }
    val mergedRanges = mutableListOf<LongRange>()

    var currentRange = sortedRanges.firstOrNull()

    for (range in sortedRanges.drop(1)) {
        if (currentRange != null && currentRange.last >= range.first - 1) {
            // Merge overlapping ranges
            currentRange = LongRange(currentRange.first, maxOf(currentRange.last, range.last))
        } else {
            // Add non-overlapping range to the result
            mergedRanges.add(currentRange!!)
            currentRange = range
        }
    }

    // Add the last range to the result
    if (currentRange != null) {
        mergedRanges.add(currentRange!!)
    }

    return mergedRanges
}


enum class IntersectionType {
    TOTAL_CROSSOVER,
    PARTIAL_CROSSOVER,
    NO_CROSSOVER
}

data class RangeSplit(val crossover: LongRange?, val noCrossover: LongRange?)

fun splitRangesByPartialCrossover(range1: LongRange, range2: LongRange): RangeSplit {
    // Calculate the overlapping range
    val overlapStart = maxOf(range1.first, range2.first)
    val overlapEnd = minOf(range1.last, range2.last)
    val crossoverRange = LongRange(overlapStart.coerceIn(range1), overlapEnd.coerceIn(range1))

    // Check for partial crossover
    if (crossoverRange.first <= crossoverRange.last) {
        // Calculate the no-crossover ranges
        val noCrossoverRange1 = LongRange(range1.first, overlapStart - 1)
        val noCrossoverRange2 = LongRange(overlapEnd + 1, range1.last.coerceAtLeast(range2.last))

        return RangeSplit(crossoverRange, listOfNotNull(noCrossoverRange1, noCrossoverRange2).firstOrNull())
    }

    // No partial crossover
    return RangeSplit(null, null)
}


fun checkIntersection(range1: LongRange, range2: LongRange): IntersectionType {
    // Check for total crossover
    if (range2.first in range1 && range2.last in range1 ||
        range1.first in range2 && range1.last in range2) {
        return IntersectionType.TOTAL_CROSSOVER
    }

    // Check for partial crossover
    else if (range2.first in range1 || range2.last in range1 ||
        range1.first in range2 || range1.last in range2) {
        return IntersectionType.PARTIAL_CROSSOVER
    }

    // No crossover
    else {
        return IntersectionType.NO_CROSSOVER
    }
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
