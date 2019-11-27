import org.junit.jupiter.api.*
import step.*
import util.*
import java.nio.file.*

class AggregateTests {
    @BeforeEach fun setup() = setupTest()
    @AfterEach fun cleanup() = cleanupTest()

    @Test fun `Test aggregate`() {
        var peaks: MutableList<PeaksRow> = mutableListOf()
        readPeaksFile(testInputDir.resolve(SUMMITS)) {
            peaks.add(it)
        }
        cmdRunner.aggregate(testInputDir.resolve(SIGNAL), peaks, testOutputDir.resolve(AGGREGATE), false)
        assertOutputMatches(AGGREGATE)
    }

    @Test fun `Test aggregate reversed`() {
        var peaks: MutableList<PeaksRow> = mutableListOf()
        readPeaksFile(testInputDir.resolve(SUMMITS_STRAND)) {
            peaks.add(it)
        }
        cmdRunner.aggregate(testInputDir.resolve(SIGNAL), peaks, testOutputDir.resolve(AGGREGATE_STRAND), true)
        assertOutputMatches(AGGREGATE_STRAND)
    }
}
