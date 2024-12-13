package io.atlassian.guard.commons.dialogger.flownode;

import io.atlassian.guard.commons.dialogger.FlowNode;

import static io.atlassian.guard.commons.dialogger.FlowNodeShape.ROUNDED_RECTANGLE;

public class StepFlowNode extends FlowNode {
    public StepFlowNode(String description) {
        super(ROUNDED_RECTANGLE, description);
    }
}
