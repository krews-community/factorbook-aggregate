import org.assertj.core.api.Assertions.*
import org.junit.jupiter.api.*
import step.*
import util.*

@Disabled class CompleteAppTests {
    @BeforeEach fun setup() = setupTest()
    //@AfterEach fun cleanup() = cleanupTest()

    @Test fun `run complete task`() {
        cmdRunner.runTask(
            PEAKS,
            SIGNAL,
            CHR22_CHROM_INFO,
            TEST_CHR_FILTER,
            0,
            testOutputDir
        )

        assertOutputMatches(CLEANED_PEAKS)
        assertOutputMatches(SUMMITS)
        assertOutputMatches(VALUES)
        assertOutputMatches(AGGREGATE)
    }
}