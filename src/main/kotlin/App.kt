import com.github.ajalt.clikt.core.CliktCommand
import com.github.ajalt.clikt.parameters.options.*
import com.github.ajalt.clikt.parameters.types.*
import mu.KotlinLogging
import step.*
import util.*
import java.nio.file.*

private val log = KotlinLogging.logger {}

fun main(args: Array<String>) = Cli().main(args)

class Cli : CliktCommand() {

    private val peaks by option("--peaks", help = "path to peaks in narrowPeak format")
        .path(exists = true).required()
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

    override fun run() {
        val cmdRunner = DefaultCmdRunner()
        cmdRunner.runTask(peaks, signal, chromInfo, chrFilter.toSet(), offset, outputDir)
    }
}

/**
 * Runs pre-processing and meme for raw input files
 *
 * @param peaks path to raw narrowPeaks file
 * @param signal path to signal bigWig file
 */
fun CmdRunner.runTask(peaks: Path, signal: Path, chromInfo: Path, chrFilter: Set<String>? = null, offset: Int, outputDir: Path) {
    log.info {
        """
        Running histone aggregation task for
        peaks: $peaks
        signal: $signal
        chromInfo: $chromInfo
        chromFilter: $chrFilter
        offset: $offset
        outputDir: $outputDir
        """.trimIndent()
    }
    val outPrefix = peaks.fileName.toString().split(".").first()
    val combinedOutPrefix = "${peaks.fileName.toString().split(".").first()}_${signal.fileName.toString().split(".").first()}"
    val chromSizes = parseChromSizes(chromInfo)

    // Rewrite peaks names, apply chrom filter
    // Name rewrite is necessary because given peaks input may not include them
    val cleanedPeaks = outputDir.resolve("$outPrefix$CLEANED_BED_SUFFIX")
    cleanPeaks(peaks, chrFilter, cleanedPeaks)

    val summitsFile = outputDir.resolve("$outPrefix$SUMMITS_FILE_SUFFIX")
    summits(cleanedPeaks, chromSizes, 2000, summitsFile, offset, chrFilter)

    val valuesFile = outputDir.resolve("$combinedOutPrefix$VALUES_TSV_SUFFIX")
    values(signal, summitsFile, valuesFile)
    
    val aggregateFile = outputDir.resolve("$combinedOutPrefix$AGGREGATE_TSV_SUFFIX")
    aggregate(valuesFile, aggregateFile)
}