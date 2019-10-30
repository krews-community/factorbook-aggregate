import org.junit.jupiter.api.*
import step.*
import util.*

class SummitsTests {
    @BeforeEach fun setup() = setupTest()
    @AfterEach fun cleanup() = cleanupTest()

    @Test fun `run summits step`() {
        val chromSizes = parseChromSizes(CHR22_CHROM_INFO)
        summits(
            testInputDir.resolve(CLEANED_PEAKS),
            chromSizes,
            2000,
            testOutputDir.resolve(SUMMITS),
            0,
            TEST_CHR_FILTER
        )

        assertOutputMatches(SUMMITS)
    }

}