package io.atlassian.guard.commons.dialogger.flownode;

import io.atlassian.guard.commons.dialogger.FlowNode;

import static io.atlassian.guard.commons.dialogger.FlowNodeShape.RHOMBUS;

public class DecisionFlowNode extends FlowNode {
    public DecisionFlowNode(String description, boolean decision) {
        super(RHOMBUS, description);
        setConnectorLabel(decision ? "Yes" : "No");
    }
}
