package io.atlassian.guard.commons.controller.model;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.ToString;

import java.util.List;

import static org.springframework.http.HttpStatus.NOT_FOUND;

@RequiredArgsConstructor(access = AccessLevel.PRIVATE)
@Getter
@JsonInclude(JsonInclude.Include.NON_EMPTY)
@ToString
// As per https://jsonapi.org/format/#document-top-level
public class RestPayload<T> {
    private final T data;
    private final List<RestError> errors;
    private final RestLinks links;
    private final RestMeta meta;

    public static <T> RestPayload<T> ok(T data, RestLinks links, RestMeta meta) {
        return new RestPayload<>(data, null, null, null);
    }

    public static <T> RestPayload<T> ok(T data) {
        return ok(data, null, null);
    }

    public static <T> RestPayload<T> error(RestLinks links, RestMeta meta, List<RestError> errors) {
        return new RestPayload<>(null, errors, links, meta);
    }

    public static <T> RestPayload<T> error(List<RestError> errors) {
        return error(null, null, errors);
    }

    public static <T> RestPayload<T> error(RestError singleError) {
        return error(null, null, List.of(singleError));
    }

    public static <T> RestPayload<T> notFound() {
        return error(RestError.builder().status(String.valueOf(NOT_FOUND.value())).build());
    }
}
