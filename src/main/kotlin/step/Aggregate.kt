package step

import util.*
import java.nio.file.*
import org.jetbrains.bio.big.*

/**
 * Gets the aggregate values of an input bigWig and set of equally-sized peaks.
 *
 * @param bigWigIn input bigWig file.
 * @param peaks input peaks; must all be the same length.
 */
fun CmdRunner.aggregate(bigWigIn: Path, peaks: List<PeaksRow>, output: Path) {
    var values = DoubleArray(peaks[0].chromEnd - peaks[0].chromStart)
    val inv = 1.0 / peaks.size
    BigWigFile.read(bigWigIn).use { bigWig ->
        peaks.forEach {
            bigWig.summarize(it.chrom, it.chromStart, it.chromEnd, it.chromEnd - it.chromStart).forEachIndexed { index, value ->
                values[index] += value.sum * inv
            }
        }
    }
    Files.newBufferedWriter(output).use { writer ->
        for (value in values) {
            writer.write("${value.toFloat()}\n")
        }
    }
}
