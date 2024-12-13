package validation.constraints

import helper.DomainPayload
import jakarta.validation.Validation
import spock.lang.Specification
import spock.lang.Unroll

class DomainSpec extends Specification {

    def validator = Validation.buildDefaultValidatorFactory().getValidator()

    @Unroll("Domain constraint annotation test: #domain validity should be #expectedToBeValid")
    def "Domain constraint annotation test"() {

        given:
        def payload = new DomainPayload(domain)

        when:
        def violations = validator.validate(payload)

        then:
        violations.isEmpty() == expectedToBeValid

        where:
        domain                                                                    | expectedToBeValid
        'abc.com'                                                                 | true
        '!.!'                                                                     | false
        'xn--punycode.com'                                                        | false
        'part-length-ok.com'                                                      | true
        'part-length-exceeds-63-0123456789-0123456789-0123456789-0123456789.com'  | false
        'example.com123'                                                          | true
        'example.a'                                                               | false
        'abcdefghijklmnopqrstuvwxyzabcdefghijklmnopqrstuvwxyzabcdefghijklmno.com' | false
        'example-domain.com'                                                      | true
        'exámple.com'                                                             | true
        'example..com'                                                            | false
        'example.xyz-'                                                            | false
        '-example.com'                                                            | false
        'example--domain.com'                                                     | false
    }
}


