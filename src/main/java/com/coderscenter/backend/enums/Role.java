package com.coderscenter.backend.enums;

import lombok.Getter;

@Getter
public enum Role {

    ADMIN("Administrator"),
    VERWALTUNG("Verwaltung"),
    HONORAR_TRAINER("Honorartrainer"),
    TRAINER("Trainer"),
    STUDENT("Student");

    private String label;

    Role(String label) {
        this.label = label;
    }
}
