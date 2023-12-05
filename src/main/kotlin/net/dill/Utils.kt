package net.dill

import java.math.BigInteger
import java.security.MessageDigest
import kotlin.io.path.Path
import kotlin.io.path.readLines
import kotlin.io.path.readText

/**
 * Reads lines from the given input txt file.
 */
fun readInput(day: String) = Path("src/main/kotlin/net/dill/y2023/$day/${day.capitalize()}.txt").readLines()
fun readInputBlocks(day: String) = Path("src/main/kotlin/net/dill/y2023/$day/${day.capitalize()}.txt").readText()

/**
 * Reads lines from the given test input txt file.
 */
fun readTestInput(day: String, part: String?) = Path("src/main/kotlin/net/dill/y2023/$day/${day.capitalize()}_${part}_test.txt").readLines()

fun readTestInputBlocks(day: String, part: String?) = Path("src/main/kotlin/net/dill/y2023/$day/${day.capitalize()}_${part}_test.txt").readText()

/**
 * Converts string to md5 hash.
 */
fun String.md5() = BigInteger(1, MessageDigest.getInstance("MD5").digest(toByteArray()))
    .toString(16)
    .padStart(32, '0')

/**
 * The cleaner shorthand for printing output.
 */
fun Any?.println() = println(this)
