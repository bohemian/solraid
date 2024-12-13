package io.atlassian.guard.commons.dialogger.flownode;

import io.atlassian.guard.commons.dialogger.FlowNode;

import static io.atlassian.guard.commons.dialogger.FlowNodeShape.HEXAGON;
import static io.atlassian.guard.commons.dialogger.NodeColor.RED;

public class ErrorFlowNode extends FlowNode {
    public ErrorFlowNode(String description) {
        super(HEXAGON, description);
        setColor(RED);
    }
}
