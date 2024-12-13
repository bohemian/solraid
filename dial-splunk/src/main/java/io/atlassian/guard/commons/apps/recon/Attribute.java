package io.atlassian.guard.commons.apps.recon;

import io.atlassian.guard.commons.apps.recon.handlers.AttributeHandler;
import io.atlassian.guard.commons.apps.recon.handlers.PolicyAttributeHandler;
import io.atlassian.guard.commons.apps.recon.handlers.TimestampAttributeHandler;
import lombok.AllArgsConstructor;
import lombok.Getter;

import java.util.function.Function;

@AllArgsConstructor
@Getter
public enum Attribute {
    ID,
    LAST_MODIFIED(new TimestampAttributeHandler()),
    POLICY_ID();

    private final AttributeHandler handler;

    Attribute() {
        this(Function.identity());
    }

    Attribute(Function<Object, Object> rectifier) {
        this(new AttributeHandler() {
        });
    }
}
