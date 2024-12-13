package io.atlassian.guard.commons.dialogger.flownode;

import io.atlassian.guard.commons.dialogger.FlowNode;

import static io.atlassian.guard.commons.dialogger.FlowNodeShape.CIRCLE;

public class StartFlowNode extends FlowNode {
    public StartFlowNode(String description) {
        super(CIRCLE, description);
    }
}
