package com.coderscenter.backend.enums;

import lombok.Getter;

@Getter
public enum ApplicationChannel {

    LINKEDIN("LinkedIn"),
    INITIATIV("Initiativbewerbung"),
    JOBBOERSE("Jobbörse"),
    HOMEPAGE("Unternehmenshomepage"),
    KARRIERE("Karriere.at"),
    SONSTIGES("Sonstiges");

    private final String label;

    ApplicationChannel(String label) {
        this.label = label;
    }

    @Override
    public String toString() {
        return label;
    }
}
