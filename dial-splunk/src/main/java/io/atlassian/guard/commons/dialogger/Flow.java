package io.atlassian.guard.commons.dialogger;

import lombok.Data;

import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;

@Data
public class Flow {
    public static final String FLOWCHART_LEFT_TO_RIGHT = "flowchart LR";
    private final List<FlowNode> nodes = new CopyOnWriteArrayList<>();
    /**
     * The log <code>message</code> accompanying the whole flow.
     */
    private final String message;
    private final long created = System.currentTimeMillis();
    private int httpStatus;

    public void add(FlowNode node) {
        nodes.add(node);
    }

    public String toUml() {
        if (isEmpty()) { // unlikely
            return "";
        }

        StringBuilder sb = new StringBuilder(FLOWCHART_LEFT_TO_RIGHT).append('\n').append(nodes.get(0).toNodeUml(0));
        StringBuilder styles = new StringBuilder(nodes.get(0).getStyle(0));

        for (int i = 1; i < nodes.size(); i++) {
            var node = nodes.get(i);
            sb.append(nodes.get(i - 1).toConnectorUml()).append(node.toNodeUml(i));
            if (i < nodes.size() - 1) {
                sb.append('\n').append(nodeId(i));
            }
            styles.append(node.getStyle(i));
        }

        /*
         * Semicolons are used as placeholders for line breaks in the dialogger to reduce screen real estate in Splunk logs
         * when not using the Splunk UML Renderer™, which restores line breaks (required by the Mermaid UML renderer)
         */
        return sb.append(styles).toString().replace('\n', ';');
    }

    public boolean isEmpty() {
        return nodes.isEmpty();
    }

    /**
     * Handles up to 52 nodes.
     */
    static char nodeId(int i) {
        return (char) ((i > 25 ? 'a' : 'A') + (i % 26));
    }

    public long getDuration() {
        return System.currentTimeMillis() - created;
    }
}
