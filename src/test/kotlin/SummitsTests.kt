import org.junit.jupiter.api.*
import step.*
import util.*

class SummitsTests {
    @BeforeEach fun setup() = setupTest()
    @AfterEach fun cleanup() = cleanupTest()

    @Test fun `run summits step`() {
        var peaks: MutableList<PeaksRow> = mutableListOf()
        readPeaksFile(testInputDir.resolve(CLEANED_PEAKS)) {
            peaks.add(it)
        }
        val chromSizes = parseChromSizes(CHR22_CHROM_INFO)
        val peakSummits = summits(peaks, chromSizes, 2000, 0, TEST_CHR_FILTER)
        writePeaksFile(testOutputDir.resolve(SUMMITS), peakSummits)
        assertOutputMatches(SUMMITS)
    }

}