package com.coderscenter.backend.exceptions;

import lombok.Data;

@Data
public class SubjectNotMatchException extends RuntimeException {
    String url;
    public SubjectNotMatchException(String message, String url) {
        super(message);
        this.url = url;
    }
}
