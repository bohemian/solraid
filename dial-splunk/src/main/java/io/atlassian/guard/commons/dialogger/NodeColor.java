package io.atlassian.guard.commons.dialogger;

import lombok.Getter;

@Getter
public enum NodeColor {
    YELLOW("ff8"),
    ORANGE("fc0"),
    RED("fcc"),
    GREEN("cfc"),
    BLUE("fff"),
    PURPLE("fcf");

    private final String code;

    NodeColor(String code) {
        this.code = code;
    }
}
