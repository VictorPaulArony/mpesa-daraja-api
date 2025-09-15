package com.example.mpesaapi.dto;

import static org.junit.jupiter.api.Assertions.assertEquals;
import org.junit.jupiter.api.Test;

class STKPushRequestDtoTest {

    @Test
    void testGettersAndSetters() {
        STKPushRequestDto dto = new STKPushRequestDto();

        dto.setTransactionType("CustomerPayBillOnline");
        dto.setAmount("1500");
        dto.setPhoneNumber("254712345678");
        dto.setAccountReference("INV123");
        dto.setTransactionDesc("Payment for invoice INV123");

        assertEquals("CustomerPayBillOnline", dto.getTransactionType());
        assertEquals("1500", dto.getAmount());
        assertEquals("254712345678", dto.getPhoneNumber());
        assertEquals("INV123", dto.getAccountReference());
        assertEquals("Payment for invoice INV123", dto.getTransactionDesc());
    }
}
