import org.junit.jupiter.api.*
import util.*

class CompleteAppTests {
    @BeforeEach fun setup() = setupTest()
    //@AfterEach fun cleanup() = cleanupTest()

    @Test fun `run complete task`() {
        cmdRunner.runTask(
            listOf(PEAKS),
            SIGNAL,
            CHR22_CHROM_INFO,
            TEST_CHR_FILTER,
            0,
            testOutputDir,
            false
        )

        //assertOutputMatches(CLEANED_PEAKS)
        //assertOutputMatches(SUMMITS)
        //assertOutputMatches(VALUES)
        assertOutputMatches(AGGREGATE)
    }
}