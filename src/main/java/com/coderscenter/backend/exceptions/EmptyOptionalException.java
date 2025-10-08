package com.coderscenter.backend.exceptions;

import lombok.Data;

@Data
public class EmptyOptionalException extends Exception{

    private String url;
    public EmptyOptionalException(String message, String url) {
        super(message);
        this.url = url;
    }
}
