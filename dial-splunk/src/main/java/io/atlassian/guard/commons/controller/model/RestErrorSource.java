package io.atlassian.guard.commons.controller.model;

/**
 * References to the primary source of an error.
 *
 * @param pointer   JSON Pointer [RFC6901] to the value in the request document that caused the error,
 *                  e.g. "/data" for a primary data object, or "/data/attributes/title" for a specific attribute etc.
 * @param parameter The URI query parameter caused the error
 * @param header    the name of the request header that caused the error
 */
public record RestErrorSource(String pointer, String parameter, String header) {
}
