package com.example.mpesaapi.dto;
import static org.junit.jupiter.api.Assertions.assertEquals;
import org.junit.jupiter.api.Test;

class B2BRequestDtoTest {

    @Test
    void testSettersAndGetters() {
        B2BRequestDto dto = new B2BRequestDto();
        dto.setInitiatorName("TestInitiator");
        dto.setAccountReference("Ref123");
        dto.setSecurityCredential("secure123");
        dto.setCommandID("Command");
        dto.setSenderIdentifierType("4");
        dto.setReceiverIdentifierType("2");
        dto.setAmount(1000.5f);
        dto.setPartyA("600000");
        dto.setPartyB("600001");
        dto.setRemarks("Test Remarks");
        dto.setQueueTimeOutURL("http://timeout.com");
        dto.setResultURL("http://result.com");
        dto.setOccassion("Occasion");

        assertEquals("TestInitiator", dto.getInitiatorName());
        assertEquals("Ref123", dto.getAccountReference());
        assertEquals("secure123", dto.getSecurityCredential());
        assertEquals("Command", dto.getCommandID());
        assertEquals("4", dto.getSenderIdentifierType());
        assertEquals("2", dto.getReceiverIdentifierType());
        assertEquals(1000.5f, dto.getAmount());
        assertEquals("600000", dto.getPartyA());
        assertEquals("600001", dto.getPartyB());
        assertEquals("Test Remarks", dto.getRemarks());
        assertEquals("http://timeout.com", dto.getQueueTimeOutURL());
        assertEquals("http://result.com", dto.getResultURL());
        assertEquals("Occasion", dto.getOccassion());
    }
}