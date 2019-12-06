import com.github.ajalt.clikt.core.CliktCommand
import com.github.ajalt.clikt.parameters.options.*
import com.github.ajalt.clikt.parameters.types.*
import mu.KotlinLogging
import step.*
import util.*
import java.nio.file.*
import org.jetbrains.bio.big.*

private val log = KotlinLogging.logger {}

fun main(args: Array<String>) = Cli().main(args)

class Cli : CliktCommand() {

    private val peaks by option("--peaks", help = "path to peaks in narrowPeak format")
        .path(exists = true).multiple()
    private val signal by option("--signal", help = "path to bigWig signal file")
        .path(exists = true).required()
    private val chromInfo by option("--chrom-info", help = "path to chromosome lengths for this assembly")
        .path(exists = true).required()
    private val chrFilter by option("--chrom-filter",
        help = "chromosomes to filter out before running").multiple()
    private val offset by option("--offset", help = "offset, in bp, to shift peaks")
        .int().default(0)
    private val outputDir by option("--output-dir", help = "path to write output")
        .path().required()
    private val alignStrand by option("--align-strand", help = "If set, then the values will be aligned by strand (minus strand will be flipped).")
        .flag()

    override fun run() {
        val cmdRunner = DefaultCmdRunner()
        cmdRunner.runTask(peaks, signal, chromInfo, chrFilter.toSet(), offset, outputDir, alignStrand)
    }
}

/**
 * Runs pre-processing and meme for raw input files
 *
 * @param peaks path to raw narrowPeaks file
 * @param signal path to signal bigWig file
 */
fun CmdRunner.runTask(allPeaks: List<Path>, signal: Path, chromInfo: Path, chrFilter: Set<String>? = null, offset: Int, outputDir: Path, alignStrand: Boolean) {
    log.info {
        """
        Running histone aggregation task for
        allPeaks: $allPeaks
        signal: $signal
        chromInfo: $chromInfo
        chromFilter: $chrFilter
        offset: $offset
        outputDir: $outputDir
        alignStrand: $alignStrand
        """.trimIndent()
    }
    BigWigFile.read(signal).use { bigWig ->
        for (peaks in allPeaks) {        
            val combinedOutPrefix = "${peaks.fileName.toString().split(".").first()}_${signal.fileName.toString().split(".").first()}"
            val chromSizes = parseChromSizes(chromInfo)
        
            // Rewrite peaks names, apply chrom filter
            // Name rewrite is necessary because given peaks input may not include them
            val aggregateSize = 2000
            var peaksCount = 0
            var values = DoubleArray(aggregateSize * 2)
            readPeaksFile(peaks) read@ { rows ->
                val summaries = rows
                    .filter { row -> !excludedByChrFilter(row, chrFilter) }
                    .map { cleaned -> peakSummit(cleaned, chromSizes, aggregateSize, offset, chrFilter) }
                    .filterNotNull()
                    .map { summit ->
                        var summary = try {
                            bigWig.summarize(summit.chrom, summit.chromStart, summit.chromEnd, summit.chromEnd - summit.chromStart)
                        } catch (e: NoSuchElementException) {
                            return@map null
                        }
                        if (alignStrand && summit.strand == '-') {
                            summary = summary.reversed()
                        }
                        summary
                    }
                    .filterNotNull()
                summaries.forEach { summary ->
                    summary.forEachIndexed { index, value ->
                        values[index] += value.sum
                    }
                    peaksCount++
                }
            }

            val aggregateFile = outputDir.resolve("$combinedOutPrefix$AGGREGATE_TSV_SUFFIX")
            Files.newBufferedWriter(aggregateFile).use { writer ->
                for (value in values) {
                    writer.write("${value.toFloat()/peaksCount}\n")
                }
            }
        }
    }

    log.info { "Done" }
}

private fun excludedByChrFilter(row: PeaksRow, chrFilter: Set<String>?) =
    chrFilter != null && chrFilter.contains(row.chrom)
