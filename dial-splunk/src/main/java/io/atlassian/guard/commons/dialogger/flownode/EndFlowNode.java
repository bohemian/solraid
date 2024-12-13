package io.atlassian.guard.commons.dialogger.flownode;

import io.atlassian.guard.commons.dialogger.FlowNode;

import static io.atlassian.guard.commons.dialogger.FlowNodeShape.CIRCLE;
import static io.atlassian.guard.commons.dialogger.NodeColor.RED;

public class EndFlowNode extends FlowNode {
    public EndFlowNode(String description) {
        super(CIRCLE, description);
    }

    public static FlowNode create(String description, int httpCode, long duration) {
        String status = isNormalHttpCode(httpCode) ? "OK" : String.valueOf(httpCode);
        EndFlowNode node = new EndFlowNode(String.format("%s\nStatus %s\nLatency: %d ms", description, status, duration));
        if (!isNormalHttpCode(httpCode)) {
            node.setColor(RED);
        }
        return node;
    }

    private static boolean isNormalHttpCode(int httpCode) {
        return httpCode == 0 || httpCode / 100 == 2;
    }
}
