package validation.validators

import io.atlassian.guard.commons.validation.validators.EmailValidator
import spock.lang.Specification
import spock.lang.Unroll

class EmailValidatorSpec extends Specification {

    @Unroll("EmailValidator test #iterationIndex: #input match should be #expected")
    def "EmailValidator test"() {
        given:
        def validator = new EmailValidator();

        expect:
        validator.isValid(input, null) == expected

        where:
        input                                                                    | expected
        'foo@bar.com'                                                            | true
        'fo\\o@bar.com'                                                          | true
        '주세요@a.com'                                                              | true
        'भार्गव@भार्गव.com'                                                      | true
        'test@莎士比亚.org'                                                      | true
        '莎士比亚@莎士比亚.org'                                                  | true
        'hans@m端ller.com'                                                       | true
        'test|123@m端ller.com'                                                   | true
        'prettyandsimple@example.com'                                            | true
        'very.common@example.com'                                                | true
        'other.email-with-dash@example.com'                                      | true
        'x@example.com'                                                          | true
        'disposable.style.email.with+symbol@example.com'                         | true
        'example-indeed@strange-example.com'                                     | true
        ''                                                                       | false
        'someString'                                                             | false
        'somestring@'                                                            | false
        'somestring@foo'                                                         | false
        'somestring@foo@bar.com'                                                 | false
        'john.smith(comment)@example.com'                                        | false
        '(comment)john.smith@example.com'                                        | false
        'john.smith@(comment)example.com'                                        | false
        'john.smith@example.com(comment)'                                        | false
        '"sam@gmail.com"'                                                        | false
        '"email with space @gmail.com"'                                          | false
        'email with space @gmail.com'                                            | false
        'email@space in domain.com'                                              | false
        'test6@invalid.co m'                                                     | false
        'email@^foo.com'                                                         | false
        'email@foo.^com'                                                         | false
        'a@iaaainkcomiaaainkcomiaaainkcomiaaainkcomiaaainkcomiaaainkcomiaab.com' | false
        'sam.tom@example.c'                                                      | false
        'name@example.com.c'                                                     | false
        'sam.@example.com'                                                       | false
        '.sam@example.com'                                                       | false
        'sam.ram.@example.com'                                                   | false
        '.sam.ram@example.com'                                                   | false
        'sam...ram@example.com'                                                  | false
        'sam@example.com.'                                                       | false
    }

}
