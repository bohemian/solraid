package io.atlassian.guard.commons.validation.constraints;


import io.atlassian.guard.commons.validation.validators.DomainValidator;
import jakarta.validation.Constraint;
import jakarta.validation.Payload;

import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

import static java.lang.annotation.ElementType.*;

/**
 * Validates that a String <i>appears</i> to be a valid domain.
 * <code>parameterName</code> and <code>code</code> may be used by a <code>@ControllerAdvice</code>
 * to generate compliant error responses.
 */
@Constraint(validatedBy = DomainValidator.class)
@Documented
@Target({FIELD, PARAMETER, METHOD})
@Retention(RetentionPolicy.RUNTIME)
public @interface Domain {
    String message() default "Invalid domain";

    String parameterName() default "domain";

    String code() default "INVALID_DOMAIN";

    Class<?>[] groups() default {};

    Class<? extends Payload>[] payload() default {};
}

