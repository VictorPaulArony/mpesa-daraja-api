package com.example.mpesaapi.dto;

import static org.junit.jupiter.api.Assertions.assertEquals;
import org.junit.jupiter.api.Test;

class C2BSimulationDtoTest {

    @Test
    void testSettersAndGetters() {
        C2BSimulationDto dto = new C2BSimulationDto();
        dto.setShortCode("600000");
        dto.setCommandID("CustomerPayBillOnline");
        dto.setAmount("150");
        dto.setMSISDN("254712345678");
        dto.setBillRefNumber("INV123");

        assertEquals("600000", dto.getShortCode());
        assertEquals("CustomerPayBillOnline", dto.getCommandID());
        assertEquals("150", dto.getAmount());
        assertEquals("254712345678", dto.getMSISDN());
        assertEquals("INV123", dto.getBillRefNumber());
    }
}

