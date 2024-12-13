package io.atlassian.guard.commons.controller;

import io.atlassian.guard.commons.controller.model.RestError;
import io.atlassian.guard.commons.controller.model.RestErrorSource;
import io.atlassian.guard.commons.controller.model.RestPayload;
import jakarta.validation.ConstraintViolation;
import jakarta.validation.ConstraintViolationException;
import org.hibernate.validator.internal.metadata.descriptor.ConstraintDescriptorImpl;
import org.hibernate.validator.internal.metadata.location.ConstraintLocation;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;

import java.lang.annotation.Annotation;
import java.util.ArrayList;
import java.util.List;

/**
 * Intercepts ConstraintViolationExceptions and converts their constraint violations to <a href="https://jsonapi.org/format/#errors">jsonapi.org errors</a>.
 * <p>
 * To use, extend and annotate with <code>@ControllerAdvice</code>
 */
public class ValidationExceptionHandler {

    @ExceptionHandler(ConstraintViolationException.class)
    public ResponseEntity<?> handleValidationException(ConstraintViolationException exception) {
        List<RestError> errors = new ArrayList<>();
        for (ConstraintViolation<?> violation : exception.getConstraintViolations()) {
            Object invalidValue = violation.getInvalidValue();
            ConstraintDescriptorImpl<?> descriptor = (ConstraintDescriptorImpl<?>) violation.getConstraintDescriptor();
            ConstraintLocation.ConstraintLocationKind location = descriptor.getConstraintLocationKind();
            Annotation annotation = descriptor.getAnnotation();
            String message = descriptor.getMessageTemplate();
            String path = (String) descriptor.getAttributes().getOrDefault("parameterName", annotation.annotationType().getSimpleName());
            RestErrorSource errorSource = null;
            if (location == ConstraintLocation.ConstraintLocationKind.PARAMETER) {
                errorSource = new RestErrorSource(null, path, null);
            }
            String detail = String.format("%s '%s' was '%s'", location.name().toLowerCase(), path, invalidValue);
            String code = (String) descriptor.getAttributes().get("code");
            RestError error = new RestError(String.valueOf(HttpStatus.BAD_REQUEST.value()), code, message, detail, errorSource, null);
            errors.add(error);
        }
        return ResponseEntity.badRequest().body(RestPayload.error(errors));
    }

}
