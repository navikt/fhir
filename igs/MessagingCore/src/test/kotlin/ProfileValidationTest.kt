import com.sksamuel.hoplite.ConfigLoader
import org.hl7.fhir.r5.model.FhirPublication
import org.hl7.fhir.r5.model.OperationOutcome
import org.hl7.fhir.validation.ValidationEngine
import org.junit.jupiter.api.TestInstance
import org.junit.jupiter.api.fail
import org.junit.jupiter.params.ParameterizedTest
import org.junit.jupiter.params.provider.MethodSource
import org.slf4j.LoggerFactory
import java.io.File
typealias Severity = OperationOutcome.IssueSeverity
typealias IssueType = OperationOutcome.IssueType

// Inspired by https://github.com/HL7/fhir-shc-vaccination-ig
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
class ProfileValidationTest {
    private val validator = createValidator()
    private val logger = LoggerFactory.getLogger(ProfileValidationTest::class.java)

    @ParameterizedTest
    @MethodSource("testCaseSource")
    fun `Validate FHIR Resource according to Profile`(input: TestCase) {
        val outcome = validator.validate("src/test/resources/${input.resource}", listOf(input.profile))
        val issues = outcome.issue.map { it.toData() }

        issues.forEachIndexed { i, it -> logger.info("${i + 1}. $it") }

        val missingIssues = input.expectedIssues
            .filterNot { expected -> issues.any { expected.matches(it) } }
        val unexpectedErrors = issues
            .filterNot { it.severity in listOf(Severity.INFORMATION, Severity.WARNING) }
            .filterNot { input.expectedIssues.any { expected -> expected.matches(it) } }

        if (missingIssues.isEmpty() && unexpectedErrors.isEmpty()) return

        val failureMessage = StringBuilder().run {
            if (missingIssues.isNotEmpty()) {
                appendLine("Missing Expected Issues:")
                missingIssues.forEach { appendLine("- $it") }
            }
            if (unexpectedErrors.isNotEmpty()) {
                appendLine("Unexpected Errors:")
                unexpectedErrors.forEach { appendLine("- $it") }
            }

            toString()
        }

        fail(failureMessage)
    }

    companion object {
        data class Config(val testCases: List<TestCase>)

        @JvmStatic
        fun testCaseSource(): List<TestCase> {
            val file = File("src/test/resources/tests.json")
            return ConfigLoader().loadConfigOrThrow<Config>(file).testCases
        }
    }
}

private fun createValidator() =
    ValidationEngine(
        "hl7.fhir.r4.core#4.0.1",
        System.getProperty("txsrvr") ?: "http://tx.fhir.org",
        System.getProperty("txLog"),
        FhirPublication.R4,
        true,
        "4.0.1"
    ).apply {
        context.loadFromFolder("fsh-generated/resources")
    }

data class TestCase(
    val resource: String,
    val profile: String,
    val expectedIssues: List<Issue> = emptyList()
)

data class Issue(
    val severity: Severity,
    val type: IssueType?,
    val location: String?,
    val message: String?
) {
    fun matches(other: Issue): Boolean {
        if (severity != other.severity) return false
        if (type != null && type != other.type) return false
        if (location != null && location != other.location) return false
        return (message == null || (other.message?.contains(message, ignoreCase = true) == true))
    }
}

private fun OperationOutcome.OperationOutcomeIssueComponent.toData() =
    Issue(severity, code, expression.joinToString("|") { it.asStringValue() }, details.text)
