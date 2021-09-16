import org.hl7.fhir.r5.model.FhirPublication
import org.hl7.fhir.r5.model.OperationOutcome
import org.hl7.fhir.validation.ValidationEngine
import org.junit.jupiter.api.TestInstance
import org.junit.jupiter.params.ParameterizedTest
import org.junit.jupiter.params.provider.CsvFileSource
import org.slf4j.LoggerFactory
import kotlin.test.fail

// Inspired by https://github.com/HL7/fhir-shc-vaccination-ig
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
class ProfileTest {

    private val validator = createValidator()
    private val logger = LoggerFactory.getLogger(ProfileTest::class.java)

    /**
     * Executes test cases for which no errors should occur.
     *
     * @param resource location of file containing resource to validate
     * @param profile FHIR profile against which to validate resource
     */
    @ParameterizedTest
    @CsvFileSource(resources = ["/tests.csv"], numLinesToSkip = 1)
    fun `Validate valid resources`(resource: String, profile: String) {
        val outcome = validator.validate("src/test/resources/$resource", listOf(profile))
        val errorMessages = mutableListOf<String>()

        outcome.issue.forEachIndexed { i, it ->
            val message = "[${i + 1}] ${createMessage(it)}"
            when (it.severity) {
                OperationOutcome.IssueSeverity.INFORMATION -> logger.info(message)
                OperationOutcome.IssueSeverity.WARNING -> logger.warn(message)
                else -> {
                    logger.error(message)
                    errorMessages.add(message)
                }
            }
        }

        if (errorMessages.isEmpty()) return

        val failureMessage = StringBuilder().run {
            appendLine("source: $resource, profile: $profile")
            outcome.issue.forEachIndexed { i, it -> appendLine("[${i + 1}] ${createMessage(it)}") }
            toString()
        }

        fail(failureMessage)
    }

    /**
     * Executes test cases for which validation issues should occur.
     * Filters by optional parameters. Will pass if at least one match.
     *
     * @param resource location of file containing resource to validate
     * @param profile FHIR profile against which to validate resource
     * @param expectedIssueSeverity issue severity, optional
     * @param expectedLocation issue expression location, optional, can be partial
     * @param expectedErrorMessage issue message, optional, can be partial
     */
    @ParameterizedTest
    @CsvFileSource(resources = ["/tests_errors.csv"], numLinesToSkip = 1)
    fun `Validate invalid resources`(
        resource: String,
        profile: String,
        expectedIssueSeverity: OperationOutcome.IssueSeverity?,
        expectedLocation: String?,
        expectedErrorMessage: String?
    ) {
        val outcome = validator.validate("src/test/resources/$resource", listOf(profile))

        val hasExpectedIssue = outcome.issue
            .filter { expectedIssueSeverity == null || it.severity == expectedIssueSeverity }
            .filter { expectedErrorMessage == null || it.details.text.contains(expectedErrorMessage) }
            .any { expectedLocation == null || it.expression.any { exp -> exp.asStringValue().contains(expectedLocation) } }

        if (hasExpectedIssue) return

        val failureMessage = StringBuilder().run {
            appendLine("Expected issue with level=$expectedIssueSeverity, location=$expectedLocation, message=$expectedErrorMessage")

            if (outcome.issue.isNotEmpty()) {
                appendLine("The following issues were found:")
                outcome.issue.forEachIndexed { i, it -> appendLine("[${i + 1}] ${createMessage(it)}") }
            }

            toString()
        }

        fail(failureMessage)
    }
}

private fun createMessage(issue: OperationOutcome.OperationOutcomeIssueComponent): String {
    val location = issue.expression?.firstOrNull()?.asStringValue() ?: ""
    return "${issue.severity.display}: type=${issue.code.display}, location=$location, message=${issue.details.text}"
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
