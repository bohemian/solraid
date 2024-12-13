package validation.validators;


import io.atlassian.guard.commons.validation.validators.DomainValidator;
import spock.lang.Specification;
import spock.lang.Unroll;

class DomainValidatorSpec extends Specification {

    @Unroll("DomainValidator test #iterationIndex: #input match should be #expected")
    def "DomainValidator test"() {
        given:
        def validator = new DomainValidator();

        expect:
        validator.isValid(input, null) == expected

        where:
        input                                                                  | expected
        'x.com'                                                                | true
        '123456789012345678901234567890123456789012345678901234567890123.com'  | true
        '1234567890123456789012345678901234567890123456789012345678901234.com' | false
        'x.y.z.co.uk'                                                          | true
        'x.y.z-z.co.uk'                                                        | true
        'x.y.z--z.co.uk'                                                       | false
        '-x.y.z.co.uk'                                                         | false
        'x.y.z.co.uk-'                                                         | false
        'x.-y.z.co.uk'                                                         | false
        'x.y.z-.co.uk'                                                         | false
        '  xyz.com'                                                            | false
        'xyz.com  '                                                            | false
        'xyz. com'                                                             | false
        'Россия.ru'                                                            | true
        'xyz.一二三.cn'                                                        | true // allow non-ascii digits
        'xyz.ab--cd.com'                                                       | false // disallow double dashes
        'भार्गव.com'                                                           | true // allow diacritics
        'भार्गवसंगथन.com'                                                      | true
        'xn--31b4d.com'                                                        | false // don't allow punycode
    }

}
