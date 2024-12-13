package io.atlassian.guard.commons.apps.recon.handlers;

import static io.atlassian.guard.commons.utils.HandyUtils.isEmpty;

public class PolicyAttributeHandler implements AttributeHandler {

    @Override
    public Object rectify(Object raw) {
        if (isEmpty(raw)) {
            return null;
        }
        var policyId = String.valueOf(raw).replace("policy_id:", "");
        if (policyId.equals("null")) {
            return null;
        }
        return policyId;
    }

}
