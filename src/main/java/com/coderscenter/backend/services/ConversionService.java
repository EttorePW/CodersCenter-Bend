package com.coderscenter.backend.services;

import com.coderscenter.backend.exceptions.EmptyOptionalException;
import org.springframework.stereotype.Service;

import java.util.Optional;


@Service
public class ConversionService {

    public <T> T getEntityFromOptional(Optional<T> optional, String url) throws EmptyOptionalException{
        if (optional.isEmpty()){
            throw new EmptyOptionalException("Unexpected empty Optional",url);
        }
        return optional.get();
    }


}
