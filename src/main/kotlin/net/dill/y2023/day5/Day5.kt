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
        val fertilizerMaps = calcNextMap(cleanedInput[2], soilMaps)
        val waterMaps = calcNextMap(cleanedInput[3], fertilizerMaps)
        val lightMaps = calcNextMap(cleanedInput[4], waterMaps)
        val temperatureMaps = calcNextMap(cleanedInput[5], lightMaps)
        val humidityMaps = calcNextMap(cleanedInput[6], temperatureMaps)
        val locationMaps = calcNextMap(cleanedInput[7], humidityMaps)
        return locationMaps.minBy { it.first }.first
    }

    val testInput1 = readTestInputBlocks(DAY, "a")
    val testInput2 = readTestInputBlocks(DAY, "b")
    check(part1(testInput1) == 35L)
    check(part2(testInput2) == 46L)

    val input = readInputBlocks(DAY)
    part1(input).println()
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

    return mergeOverlappingRanges(rearrangeToMinMaxRange(nextMaps))
}

fun rearrangeToMinMaxRange(ranges: List<LongRange>): List<LongRange> {
    return ranges.map {
        if (it.first > it.last) LongRange(it.last, it.first)
        else it
    }
}

fun mergeOverlappingRanges(ranges: List<LongRange>): MutableList<LongRange> {
    val sortedRanges = ranges.sortedBy { it.first }
    val mergedRanges = mutableListOf<LongRange>()

    var currentRange = sortedRanges.firstOrNull()

    for (range in sortedRanges.drop(1)) {
        currentRange = if (currentRange != null && currentRange.last >= range.first - 1) {
            LongRange(currentRange.first, maxOf(currentRange.last, range.last))
        } else {
            mergedRanges.add(currentRange!!)
            range
        }
    }

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
    val overlapStart = maxOf(range1.first, range2.first)
    val overlapEnd = minOf(range1.last, range2.last)
    val crossoverRange = LongRange(overlapStart.coerceIn(range1), overlapEnd.coerceIn(range1))

    if (crossoverRange.first <= crossoverRange.last) {
        val noCrossoverRange1 = LongRange(range1.first, overlapStart - 1)
        val noCrossoverRange2 = LongRange(overlapEnd + 1, range1.last.coerceAtLeast(range2.last))

        return RangeSplit(crossoverRange, listOfNotNull(noCrossoverRange1, noCrossoverRange2).firstOrNull())
    }

    return RangeSplit(null, null)
}


fun checkIntersection(range1: LongRange, range2: LongRange): IntersectionType {
    if (range2.first in range1 && range2.last in range1 ||
        range1.first in range2 && range1.last in range2) {
        return IntersectionType.TOTAL_CROSSOVER
    }

    else if (range2.first in range1 || range2.last in range1 ||
        range1.first in range2 || range1.last in range2) {
        return IntersectionType.PARTIAL_CROSSOVER
    }

    else {
        return IntersectionType.NO_CROSSOVER
    }
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
