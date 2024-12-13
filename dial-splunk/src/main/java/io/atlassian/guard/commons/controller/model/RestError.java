package io.atlassian.guard.commons.controller.model;

import lombok.Builder;
import lombok.Data;
import lombok.RequiredArgsConstructor;

import java.util.UUID;

@RequiredArgsConstructor
@Data
@Builder
// As per https://jsonapi.org/format/#errors, except "links"
public class RestError {
    /**
     * a unique identifier for this particular occurrence of the problem.
     */
    private final String id = UUID.randomUUID().toString();
    /**
     * the HTTP status code applicable to this problem, expressed as a string value. This SHOULD be provided.
     */
    private final String status;
    /**
     * an application-specific error code, expressed as a string value.
     */
    private final String code;
    /**
     * a short, human-readable summary of the problem that SHOULD NOT change from occurrence to occurrence of the problem.
     */
    private final String title;
    /**
     * a human-readable explanation specific to this occurrence of the problem.
     */
    private final String detail;
    /**
     * an object containing references to the primary source of the error.
     */
    private final RestErrorSource source;
    /**
     * a metaobject containing non-standard meta-information about the error.
     */
    private final RestMeta meta;
}

