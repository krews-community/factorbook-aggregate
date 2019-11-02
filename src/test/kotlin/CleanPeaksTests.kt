import org.junit.jupiter.api.*
import step.cleanPeaks
import util.*

class CleanPeaksTests {
    @BeforeEach fun setup() = setupTest()
    @AfterEach fun cleanup() = cleanupTest()

    @Test fun `Test cleanPeaks`() {
        val peaks = cleanPeaks(PEAKS, TEST_CHR_FILTER)
        writePeaksFile(testOutputDir.resolve(CLEANED_PEAKS), peaks)
        assertOutputMatches(CLEANED_PEAKS)
    }

}