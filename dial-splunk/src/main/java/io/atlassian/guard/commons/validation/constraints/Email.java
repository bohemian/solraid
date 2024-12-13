package io.atlassian.guard.commons.validation.constraints;


import io.atlassian.guard.commons.validation.validators.DomainValidator;
import io.atlassian.guard.commons.validation.validators.EmailValidator;
import jakarta.validation.Constraint;
import jakarta.validation.Payload;

import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

import static java.lang.annotation.ElementType.*;

/**
 * Validates that a String <i>appears</i> to be a valid email.
 * <code>parameterName</code> and <code>code</code> may be used by a <code>@ControllerAdvice</code>
 * to generate compliant error responses.
 */
@Constraint(validatedBy = EmailValidator.class)
@Documented
@Target({FIELD, PARAMETER, METHOD})
@Retention(RetentionPolicy.RUNTIME)
public @interface Email {
    String message() default "Invalid email";

    String parameterName() default "email";

    String code() default "INVALID_EMAIL";

    Class<?>[] groups() default {};

    Class<? extends Payload>[] payload() default {};
}

