package step

import util.*
import java.nio.file.*

const val BIG_WIG_VALUES_OVER_BED = "bigwigvaluesoverbed"

/**
 * Gets the values of a bigWig over a bed.
 *
 * @param bigWigIn input bigWig file.
 * @param bedIn input bed file.
 * @param outputTsv the output tsv.
 */
fun CmdRunner.values(bigWigIn: Path, bedIn: Path, outputTsv: Path) {
    this.run("./lib/bigwigvaluesoverbed $bigWigIn $bedIn $outputTsv")
}

/**
 * Get the aggregate values of an input tsv.
 * 
 * Specifically, the input tsv must be of the form N (rows) * M (cols) values.
 * The output tsv will be of the form M (rows) * 1 (cols) values, where each
 * row is the sum of the respective input column.
 *
 * @param bigWigIn input bigWig file.
 * @param bedIn input bed file.
 * @param outputTsv the output tsv.
 */
fun CmdRunner.aggregate(inputTsv: Path, outputTsv: Path) {
    var vals: FloatArray? = null
    val inputStream = Files.newInputStream(inputTsv)
    inputStream.reader().useLines { lines ->
        lines.forEach { line ->
            val lineParts = line.trim().split("\t")
            if (vals == null) {
                vals = FloatArray(lineParts.size)
            }
            lineParts.forEachIndexed { index, value ->
                vals!![index] += value.toFloat()
            }
        }
    }
    Files.newBufferedWriter(outputTsv).use { writer ->
        for (value in vals!!) {
            writer.write("$value\n")
        }
    }
}