import org.junit.jupiter.api.*
import step.*
import util.*

class AggregateTests {
    @BeforeEach fun setup() = setupTest()
    @AfterEach fun cleanup() = cleanupTest()

    @Test fun `Test values`() {
        cmdRunner.values(SIGNAL, testInputDir.resolve(SUMMITS), testOutputDir.resolve(VALUES))
        assertOutputMatches(VALUES)
    }

    @Test fun `Test aggregate`() {
        cmdRunner.aggregate(testInputDir.resolve(VALUES), testOutputDir.resolve(AGGREGATE))
        assertOutputMatches(AGGREGATE)
    }

}