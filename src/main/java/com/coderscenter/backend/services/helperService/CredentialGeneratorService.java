package com.coderscenter.backend.services.helperService;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.Random;

@Service
@RequiredArgsConstructor
public class CredentialGeneratorService {

    private final Random random = new Random();

    public String autoGenerator(String label) {
        return label + random.nextInt(10) + random.nextInt(10) + random.nextInt(10) + random.nextInt(10);
    }
}
