package io.atlassian.guard.commons.validation.validators;

import io.atlassian.guard.commons.validation.constraints.Email;
import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;
import lombok.val;

import java.util.regex.Pattern;

import static io.atlassian.guard.commons.validation.validators.DomainValidator.DOMAIN_REGEX;

public class EmailValidator implements ConstraintValidator<Email, String> {
    public static final String EMAIL_USER_REGEX = "(?:(?!\\.)(?!.*?\\.\\.)([\\p{LD}\\p{M}!#$%&'*+/=?^_`{|}~.-]+(?:(\\\\.)|[\\p{LD}\\p{M}!#$%&'*+/=?^_`{|}~.-]+)*|\"(?:[\\x01-\\x08\\x0b\\x0c\\x0e-\\x1f\\x21\\x23-\\x5b\\x5d-\\x7f]|\\\\[\\x01-\\x09\\x0b\\x0c\\x0e-\\x7f])*\"))(?<!\\.)";
    /**
     * Allows any letter/digit/diacritic from any alphabet, with other restrictions on format.
     */
    private static final Pattern EMAIL_PATTERN = Pattern.compile(EMAIL_USER_REGEX + "@" + DOMAIN_REGEX);

    /**
     * Validates domain format.
     *
     * @param email an email, eg <code>"user1@abc.com"</code>
     * @return <code>true</code> if the format is valid
     */
    public static boolean isValid(String email) {
        return email != null && EMAIL_PATTERN.matcher(email).matches();
    }

    @Override
    public boolean isValid(String input, ConstraintValidatorContext constraintValidatorContext) {
        return isValid(input);
    }
}
