import org.junit.jupiter.api.*
import step.*
import util.*

class AggregateTests {
    @BeforeEach fun setup() = setupTest()
    @AfterEach fun cleanup() = cleanupTest()

    @Test fun `Test aggregate`() {
        var peaks: MutableList<PeaksRow> = mutableListOf()
        readPeaksFile(testInputDir.resolve(SUMMITS)) {
            peaks.add(it)
        }
        cmdRunner.aggregate(testInputDir.resolve(SIGNAL), peaks, testOutputDir.resolve(AGGREGATE))
        assertOutputMatches(AGGREGATE)
    }

}