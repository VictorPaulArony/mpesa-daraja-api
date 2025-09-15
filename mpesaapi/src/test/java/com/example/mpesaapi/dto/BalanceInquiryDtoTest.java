package com.example.mpesaapi.dto;

import static org.junit.jupiter.api.Assertions.assertEquals;
import org.junit.jupiter.api.Test;

class BalanceInquiryDtoTest {

    @Test
    void testSettersAndGetters() {
        BalanceInquiryDto dto = new BalanceInquiryDto();
        dto.setInitiator("Admin");
        dto.setCommandID("Balance");
        dto.setSecurityCredential("secure");
        dto.setPartyA("600000");
        dto.setIdentifierType("4");
        dto.setRemarks("Check balance");
        dto.setQueueTimeOutURL("http://timeout.com");
        dto.setResultURL("http://result.com");

        assertEquals("Admin", dto.getInitiator());
        assertEquals("Balance", dto.getCommandID());
        assertEquals("secure", dto.getSecurityCredential());
        assertEquals("600000", dto.getPartyA());
        assertEquals("4", dto.getIdentifierType());
        assertEquals("Check balance", dto.getRemarks());
        assertEquals("http://timeout.com", dto.getQueueTimeOutURL());
        assertEquals("http://result.com", dto.getResultURL());
    }
}
