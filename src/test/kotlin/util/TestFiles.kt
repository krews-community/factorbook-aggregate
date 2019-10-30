package util

import org.assertj.core.api.Assertions.*
import java.nio.file.*

fun getResourcePath(relativePath: String): Path {
    val url = TestCmdRunner::class.java.classLoader.getResource(relativePath)
    return Paths.get(url.toURI())
}

fun assertOutputMatches(filename: String) =
    assertThat(testOutputDir.resolve(filename)).hasSameContentAs(testOutputResourcesDir.resolve(filename))!!

// Resource Directories
val testInputResourcesDir = getResourcePath("test-input-files")
val testOutputResourcesDir = getResourcePath("test-output-files")

// Test Working Directories
val testDir = Paths.get("/tmp/aggregate-test")!!
val testInputDir = testDir.resolve("input")!!
val testOutputDir = testDir.resolve("output")!!

/*
 * Input Files
 */
val PEAKS= testInputDir.resolve("ENCFF165UME.bed")!!
val SIGNAL = testInputDir.resolve("ENCFF847JMY.chr22.bigWig")!!
val CHR22_CHROM_INFO = testInputDir.resolve("hg38.chr22.chrom.sizes")!!

/*
 * Output File Names
 */
const val PREFIX = "ENCFF165UME"
const val CLEANED_PEAKS = "$PREFIX$CLEANED_BED_SUFFIX"
const val SUMMITS = "$PREFIX$SUMMITS_FILE_SUFFIX"
const val VALUES = "$PREFIX$VALUES_TSV_SUFFIX"
const val AGGREGATE = "$PREFIX$AGGREGATE_TSV_SUFFIX"