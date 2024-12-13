package io.atlassian.guard.commons.dialogger;

import lombok.Getter;

/**
 * See <a href="https://mermaid.js.org/syntax/flowchart.html">Mermaid flowchart syntax</a> for available shapes.
 */
@Getter
public enum FlowNodeShape {
    RHOMBUS("{", "}"),
    RECTANGLE("[", "]"),
    ROUNDED_RECTANGLE("(", ")"),
    CIRCLE("((", "))"),
    DOUBLE_CIRCLE("(((", ")))"),
    HEXAGON("{{", "}}"),
    STADIUM("([", "])"),
    SUBROUTINE("[[", "]]"),
    DATABASE("[(", ")]"),
    SLANTED_RECTANGLE("[/", "/]"),
    BACK_SLANTED_RECTANGLE("[\\", "\\]"),
    TRAPEZOID("[/", "\\]"),
    INVERTED_TRAPEZOID("[\\", "/]"),
    FLAG_LEFT(">", "]");

    private final String start;
    private final String end;

    FlowNodeShape(String start, String end) {
        this.start = start;
        this.end = end;
    }
}
