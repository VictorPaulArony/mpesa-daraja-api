package com.example.mpesaapi.dto;

import static org.junit.jupiter.api.Assertions.assertEquals;
import org.junit.jupiter.api.Test;

class RegisterURLDtoTest {

    @Test
    void testGettersAndSetters() {
        RegisterURLDto dto = new RegisterURLDto();

        dto.setShortCode("600000");
        dto.setResponseType("Completed");
        dto.setConfirmationURL("https://example.com/confirm");
        dto.setValidationURL("https://example.com/validate");

        assertEquals("600000", dto.getShortCode());
        assertEquals("Completed", dto.getResponseType());
        assertEquals("https://example.com/confirm", dto.getConfirmationURL());
        assertEquals("https://example.com/validate", dto.getValidationURL());
    }
}

