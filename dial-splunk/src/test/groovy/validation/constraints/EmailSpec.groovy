package validation.constraints

import helper.EmailPayload
import jakarta.validation.Validation
import spock.lang.Specification
import spock.lang.Unroll

class EmailSpec extends Specification {

    def validator = Validation.buildDefaultValidatorFactory().getValidator()

    @Unroll("Email constraint annotation test: #email should be valid #expectedToBeValid")
    def "Email constraint annotation test"() {

        given:
        def payload = new EmailPayload(email)

        when:
        def violations = validator.validate(payload)

        then:
        violations.isEmpty() == expectedToBeValid

        where:
        email                                | expectedToBeValid
        'foo@abc.com'                        | true
        '주세요@भार्गव.com'                     | true
        'bad@email@xyz.com'                  | false
        '"fred"@xyz.com'                     | true
        'name+tag@example.com'               | true
        'name\\@tag@example.com'             | true
        'spaces\\ are\\ allowed@example.com' | true
    }
}
