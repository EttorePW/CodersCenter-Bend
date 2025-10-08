package com.coderscenter.backend.enums;

import lombok.Getter;

@Getter
public enum DayLabel {

    MONTAG("Montag"),
    DIENSTAG("Dienstag"),
    MITTWOCH("Mittwoch"),
    DONNERSTAG("Donnerstag"),
    FREITAG("Freitag"),
    SAMSTAG("Samstag"),
    SONNTAG("Sonntag");

    private String label;

    DayLabel(String label) {
        this.label = label;
    }
}
