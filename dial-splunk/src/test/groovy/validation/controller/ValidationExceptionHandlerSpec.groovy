package validation.controller

import helper.DomainPayload
import io.atlassian.guard.commons.controller.ValidationExceptionHandler
import jakarta.validation.ConstraintViolationException
import jakarta.validation.Validation
import spock.lang.Specification
import spock.lang.Unroll

class ValidationExceptionHandlerSpec extends Specification {

    def validator = Validation.buildDefaultValidatorFactory().getValidator()
    def handler = new ValidationExceptionHandler()

    @Unroll("ValidationExceptionHandler test #iterationIndex")
    def "ValidationExceptionHandler test"() {
        given:
        def payload = new DomainPayload('invalid_domain')

        when:
        def violations = validator.validate(payload)
        def exception = new ConstraintViolationException(violations)
        def validationResult = handler.handleValidationException(exception)

        then:
        def body = validationResult.body
        !body.data
        def error = body.errors[0]
        def actual = Eval.x(error, 'x.' + errorAttribute)
        actual == expected

        where:
        errorAttribute | expected
        'code'         | 'INVALID_DOMAIN'
        'status'       | '400'
        'title'        | 'Invalid domain'
        'detail'       | "field 'domain' was 'invalid_domain'"
    }
}


