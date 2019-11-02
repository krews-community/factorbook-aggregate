package step

import util.*
import java.nio.file.Path

/**
 * Rewrites narrowPeaks files with new names.
 * Optionally filters out chromosomes that do not match given filter
 *
 * @param peaksBed the peaks file
 * @param chrFilter Optional set of chromsomes to filter against. Anything not included is filtered out.
 * @param out the file to the filtered peaks results to
 */
fun cleanPeaks(peaksBed: Path, chrFilter: Set<String>?): List<PeaksRow> {
    val filteredPeaks = mutableListOf<PeaksRow>()
    var peakCount = 0
    readPeaksFile(peaksBed) { row ->
        if (!excludedByChrFilter(row, chrFilter)) {
            filteredPeaks += row.copy(name = "peak_${peakCount++}")
        }
    }
    return filteredPeaks
}

private fun excludedByChrFilter(row: PeaksRow, chrFilter: Set<String>?) =
        chrFilter != null && chrFilter.contains(row.chrom)
