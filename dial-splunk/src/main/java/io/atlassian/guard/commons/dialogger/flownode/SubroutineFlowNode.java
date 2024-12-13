package io.atlassian.guard.commons.dialogger.flownode;

import io.atlassian.guard.commons.dialogger.FlowNode;

import static io.atlassian.guard.commons.dialogger.FlowNodeShape.SUBROUTINE;

public class SubroutineFlowNode extends FlowNode {
    public SubroutineFlowNode(String description) {
        super(SUBROUTINE, description);
    }
}
