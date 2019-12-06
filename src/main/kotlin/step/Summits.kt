package step

import mu.KotlinLogging
import util.*
import java.nio.file.*

private val log = KotlinLogging.logger {}

/**
 * Resizes peaks to a fixed width around their midpoint, excluding any peaks which extend off the chromosome,
 * and sorts results by Q-value then P-value then signal. If an offset is passed, shifts peaks by a set
 * number of basepairs.
 *
 * @param peaks path to peaks to resize.
 * @param chromSizes Map of chromosomes to sizes for this assembly.
 * @param newSize fixed width to which peaks should be resized.
 * @param output path to write resized output peaks.
 * @param offset number of base pairs to shift chrom start and end by (Optional)
 */
fun peakSummit(row: PeaksRow, chromSizes: Map<String, Int>, newSize: Int, offset: Int? = null,
            chrFilter: Set<String>? = null): PeaksRow? {

    if (chrFilter != null && chrFilter.contains(row.chrom)) return null
    val chromSize = chromSizes[row.chrom] ?: return null
    val chromEnd = if (offset != null) {
        val newEnd = row.chromEnd + offset
        when {
            newEnd > chromSize -> chromSize
            newEnd < 1 -> 1
            else -> newEnd
        }
    } else row.chromEnd
    val chromStart = if (offset != null) {
        val newStart = row.chromStart + offset
        when {
            newStart > chromEnd -> chromEnd - 1
            newStart < 0 -> 0
            else -> newStart
        }
    } else row.chromStart
    val midpoint = chromStart + row.peak
    if (midpoint < newSize || midpoint + newSize > chromSize) return null

    val newStart = midpoint - newSize
    val newEnd = midpoint + newSize
    return row.copy(chromStart = newStart, chromEnd = newEnd)
}

/**
 * Construct map of chromosome names to sizes from chrom sizes file
 *
 * @param from path to chrom sizes file
 */
fun parseChromSizes(from: Path): Map<String, Int> {
    val lines = Files.readAllLines(from)
    return lines.map { line ->
        val parts = line.split("\t")
        parts[0] to parts[1].toInt()
    }.toMap()
}



