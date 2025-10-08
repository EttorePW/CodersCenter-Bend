package com.coderscenter.backend.enums;

import lombok.Getter;

@Getter
public enum PermissionType {
    REGISTER("Registrieren"),
    SCHEDULE("Einsatzplan Erstellen");

    private final String label;

    PermissionType(String label) {
        this.label = label;
    }
}
