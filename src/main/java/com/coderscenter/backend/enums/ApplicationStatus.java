package com.coderscenter.backend.enums;

import lombok.Getter;

@Getter
public enum ApplicationStatus {
    ABGESCHICKT("Abgeschickt"),
    ZUSAGE("Zusage"),
    ABSAGE("Absage"),
    ERSTES_GESPRAECH("1. Gespräch"),
    ZWEITES_GESPRAECH("2. Gespräch"),
    DRITTES_GESPRAECH("3. Gespräch"),
    EVIDENZ("Evidenz");

    private final String label;

    ApplicationStatus(String label) {
        this.label = label;
    };

    @Override
    public String toString() {
        return label;
    }
}
