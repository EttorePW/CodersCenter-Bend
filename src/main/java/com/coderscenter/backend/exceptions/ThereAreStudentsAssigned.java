package com.coderscenter.backend.exceptions;

import lombok.Getter;

@Getter
public class ThereAreStudentsAssigned extends RuntimeException {

    private String url;
    public ThereAreStudentsAssigned(String message, String url) {
        super(message);
        this.url = url;
    }
}
