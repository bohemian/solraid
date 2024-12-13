package io.atlassian.guard.commons.dialogger

import helper.TestUtils
import io.atlassian.guard.commons.dialogger.flownode.ErrorFlowNode
import spock.lang.Specification
import spock.lang.Unroll

import io.micrometer.tracing.Tracer

import static FlowNodeShape.RHOMBUS
import static FlowNodeShape.ROUNDED_RECTANGLE

class DialoggerSpec extends Specification {

    @Unroll("#iterationIndex #scenario: getValidCSVLine(#input) should explode")
    def "UMLFlow rendering"() {
        given:
        def node1 = new FlowNode(ROUNDED_RECTANGLE, "prime check 1")
        def node2 = new FlowNode(RHOMBUS, "is 1\nprime?").setConnectorLabel("No")
        def node3 = new FlowNode(ROUNDED_RECTANGLE, "return false")
        def node4 = new ErrorFlowNode("boom")
        def flow = new Flow()

        expect:
        flow.toUml() == ''

        when:
        flow.add(node1)
        then:
        flow.toUml() == 'flowchart LR;A("`prime check 1`")'

        when:
        flow.add(node2)
        then:
        flow.toUml() == 'flowchart LR;A("`prime check 1`")-->B{"`is 1;prime?`"}'

        when:
        flow.add(node3)
        then:
        flow.toUml() == 'flowchart LR;A("`prime check 1`")-->B{"`is 1;prime?`"};B-->|No|C("`return false`")'

        when:
        flow.add(node4)
        then:
        flow.toUml() == 'flowchart LR;A("`prime check 1`")-->B{"`is 1;prime?`"};B-->|No|C("`return false`");C-->D{{"`boom`"}};style D fill:#fcc'
    }

    def "test id method"() {
        expect:
        Flow.nodeId(0) == 'A' as char
        Flow.nodeId(1) == 'B' as char
        Flow.nodeId(25) == 'Z' as char
        Flow.nodeId(26) == 'a' as char
        Flow.nodeId(51) == 'z' as char
    }

    def "test purge method"() {
        given:
        def tracer = Mock(Tracer)
        def dialogger = new Dialogger(tracer)

        and: "there's a recently created flow"
        dialogger.flows.put("traceId1", new Flow("fresh description"))
        and: "there's a flow created a long time ago"
        def staleFlow = new Flow("stale description")
        TestUtils.setField(staleFlow, "created", System.currentTimeMillis() - 1_000_000)
        dialogger.flows.put("traceId2", staleFlow)

        when:
        dialogger.purge()

        then:
        dialogger.flows.containsKey("traceId1")
        !dialogger.flows.containsKey("traceId2")
    }

}