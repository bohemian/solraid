package io.atlassian.guard.commons.dialogger;

import lombok.Data;

import java.util.Optional;

import static io.atlassian.guard.commons.dialogger.Flow.nodeId;

@Data
public class FlowNode {
    private final FlowNodeShape shape;
    private final String description;
    private NodeColor color;
    private String connectorLabel;

    public String toNodeUml(int nodeNumber) {
        return String.format("%s%s\"`%s`\"%s", nodeId(nodeNumber), shape.getStart(), description, shape.getEnd());
    }

    public FlowNode setColor(NodeColor nodeColor) {
        this.color = nodeColor;
        return this;
    }

    public FlowNode setConnectorLabel(String label) {
        this.connectorLabel = label;
        return this;
    }

    public String toConnectorUml() {
        StringBuilder sb = new StringBuilder("-->");
        Optional.ofNullable(connectorLabel).ifPresent(label -> sb.append('|').append(label).append('|'));
        return sb.toString();
    }

    public String getStyle(int i) {
        return Optional.ofNullable(color)
                .map(NodeColor::getCode)
                .map(colorCode -> String.format("\nstyle %s fill:#%s", nodeId(i), colorCode))
                .orElse("");
    }
}
