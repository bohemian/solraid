package helper;

import io.atlassian.guard.commons.validation.constraints.Domain;

public class DomainPayload {
    @Domain
    final String domain;

    public DomainPayload(String domain) {
        this.domain = domain;
    }
}
