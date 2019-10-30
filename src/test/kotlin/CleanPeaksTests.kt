import org.junit.jupiter.api.*
import step.cleanPeaks
import util.*

class CleanPeaksTests {
    @BeforeEach fun setup() = setupTest()
    @AfterEach fun cleanup() = cleanupTest()

    @Test fun `Test cleanPeaks`() {
        cleanPeaks(PEAKS, TEST_CHR_FILTER, testOutputDir.resolve(CLEANED_PEAKS))
        assertOutputMatches(CLEANED_PEAKS)
    }

}