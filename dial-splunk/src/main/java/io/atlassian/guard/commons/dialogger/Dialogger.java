package io.atlassian.guard.commons.dialogger;

import io.atlassian.guard.commons.dialogger.flownode.DecisionFlowNode;
import io.atlassian.guard.commons.dialogger.flownode.EndFlowNode;
import io.atlassian.guard.commons.dialogger.flownode.ErrorFlowNode;
import io.atlassian.guard.commons.dialogger.flownode.StartFlowNode;
import io.atlassian.guard.commons.dialogger.flownode.StepFlowNode;
import io.atlassian.guard.commons.dialogger.flownode.SubroutineFlowNode;
import io.micrometer.tracing.Span;
import io.micrometer.tracing.TraceContext;
import io.micrometer.tracing.Tracer;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import net.logstash.logback.marker.Markers;
import org.jetbrains.annotations.NotNull;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.concurrent.ConcurrentHashMap;
import java.util.function.Function;

import static java.util.stream.Collectors.counting;
import static java.util.stream.Collectors.groupingBy;

/**
 * See <a href="https://mermaid.js.org/syntax/flowchart.html">Mermaid flowchart syntax</a> for how to create diagrams.
 */
@Slf4j
@Component
@RequiredArgsConstructor
public class Dialogger {
    private static final long PURGE_SCHEDULE_MS = 600_000; // 10 minutes
    private static final long STALE_MS = 60_000; // 1 minute

    private final Map<String, Flow> flows = new ConcurrentHashMap<>();

    private final Tracer tracer;

    public void addNode(FlowNode flowNode) {
        flows.computeIfAbsent(getTraceId(), ignore -> new Flow(flowNode.getDescription())).add(flowNode);
    }

    /**
     * Starts a flow with a standard shape.
     * If not called, the log <code>message</code> will be the logMessage of the first flownode.
     *
     * @param logMessage The <code>message</code> field of the eventual log when the flow is {@link #close(String) closed}.
     */
    public void init(String logMessage) {
        flows.put(getTraceId(), new Flow(logMessage));
    }

    public void start(String description) {
        addNode(new StartFlowNode(description));
    }

    public void step(String description) {
        addNode(new StepFlowNode(description));
    }

    public void decision(String description, boolean decision) {
        addNode(new DecisionFlowNode(description, decision));
    }

    public void featureFlag(String flagName, boolean enabled) {
        decision(String.format("Is feature flag\n_%s_\nenabled?", flagName), enabled);
    }

    public void subroutine(String description) {
        addNode(new SubroutineFlowNode(description));
    }

    public void subroutine(String description, long durationMillis) {
        // TODO: Create a "timed subroutine" that clocks timing when method "stopWatch()" is called
        //  or when next flownode is added
        addNode(new SubroutineFlowNode(description + "\nmilliseconds: " + durationMillis));
    }

    public void error(String description) {
        addNode(new ErrorFlowNode(description));
    }

    public void error(Throwable e) {
        error(String.format("%s: %s", e.getClass().getSimpleName(), e.getMessage()));
    }

    /**
     * Ends the flow and logs it as dialogger.
     */
    public void close(String description) {
        Flow flow = flows.remove(getTraceId());
        if (flow == null || flow.isEmpty()) {
            log.warn("log() called on empty flow");
            return;
        }

        flow.add(EndFlowNode.create(description, flow.getHttpStatus(), flow.getDuration()));

        log.info(Markers.append("uml", flow.toUml()), flow.getMessage());
    }

    /**
     * {@link #close(String) Closes} the flow with "response returned" description.
     */
    public void responseReturned() {
        close("response\nreturned");
    }

    @NotNull
    private String getTraceId() {
        return Optional.of(tracer)
                .map(Tracer::currentSpan)
                .map(Span::context)
                .map(TraceContext::traceId)
                .orElseGet(() -> ""); // should never happen
    }

    /**
     * Delete flows that weren't logged to prevent memory leak.
     */
    @Scheduled(fixedDelay = PURGE_SCHEDULE_MS)
    private void purge() {
        long staleAge = System.currentTimeMillis() - STALE_MS;

        List<Map.Entry<String, Flow>> staleEntries = flows.entrySet().stream()
                .filter(entry -> entry.getValue().getCreated() < staleAge)
                .toList();

        staleEntries.stream()
                .map(Map.Entry::getKey)
                .forEach(flows::remove);

        Map<String, Long> summary = staleEntries.stream()
                .map(Map.Entry::getValue)
                .map(Flow::getMessage)
                .collect(groupingBy(Function.identity(), counting()));

        if (!summary.isEmpty()) {
            log.info("Purged stale UmlFlows: {}", summary);
        }
    }
}
