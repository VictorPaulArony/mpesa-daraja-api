package com.example.mpesaapi.dto;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class B2CRequestDtoTest {

    @Test
    void testSettersAndGetters() {
        B2CRequestDto dto = new B2CRequestDto();
        dto.setInitiatorName("Initiator");
        dto.setSecurityCredential("secure");
        dto.setCommandID("B2CCommand");
        dto.setAmount("500");
        dto.setPartyA("600000");
        dto.setPartyB("254712345678");
        dto.setRemarks("Salary");
        dto.setQueueTimeOutURL("http://timeout.com");
        dto.setResultURL("http://result.com");
        dto.setOccassion("Occasion");

        assertEquals("Initiator", dto.getInitiatorName());
        assertEquals("secure", dto.getSecurityCredential());
        assertEquals("B2CCommand", dto.getCommandID());
        assertEquals("500", dto.getAmount());
        assertEquals("600000", dto.getPartyA());
        assertEquals("254712345678", dto.getPartyB());
        assertEquals("Salary", dto.getRemarks());
        assertEquals("http://timeout.com", dto.getQueueTimeOutURL());
        assertEquals("http://result.com", dto.getResultURL());
        assertEquals("Occasion", dto.getOccassion());
    }
}
