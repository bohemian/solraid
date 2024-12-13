package io.atlassian.guard.commons.validation.validators;

import io.atlassian.guard.commons.validation.constraints.Domain;
import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;

import java.util.regex.Pattern;

public class DomainValidator implements ConstraintValidator<Domain, String> {
    static final String DOMAIN_REGEX = "(?:[\\p{LD}\\p{M}](?:(?!.*--)[\\p{LD}\\p{M}-]{0,61}[\\p{LD}\\p{M}])?\\.)+[\\p{LD}\\p{M}](?!.--)[\\p{LD}\\p{M}-]{0,61}[\\p{LD}\\p{M}]";
    /**
     * Allows any letter/digit/diacritic from any alphabet, with other restrictions on format.
     */
    private static final Pattern DOMAIN_PATTERN = Pattern.compile(DOMAIN_REGEX);


    @Override
    public boolean isValid(String input, ConstraintValidatorContext constraintValidatorContext) {
        return isValid(input);
    }

    /**
     * Validates domain format.
     *
     * @param domain a domain, eg <code>"abc.com"</code>
     * @return <code>true</code> if the format is valid
     */
    public static boolean isValid(String domain) {
        return domain != null && DOMAIN_PATTERN.matcher(domain).matches();
    }
}
