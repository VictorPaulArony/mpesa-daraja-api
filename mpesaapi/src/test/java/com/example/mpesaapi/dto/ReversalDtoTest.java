package com.example.mpesaapi.dto;

import static org.junit.jupiter.api.Assertions.assertEquals;
import org.junit.jupiter.api.Test;

class ReversalDtoTest {

    @Test
    void testGettersAndSetters() {
        ReversalDto dto = new ReversalDto();

        dto.setInitiator("Reverser");
        dto.setSecurityCredential("secure123");
        dto.setCommandID("TransactionReversal");
        dto.setTransactionID("TX123456");
        dto.setAmount("5000");
        dto.setReceiverParty("600001");
        dto.setRecieverIdentifierType("4");
        dto.setResultURL("https://example.com/result");
        dto.setQueueTimeOutURL("https://example.com/timeout");
        dto.setRemarks("Duplicate transaction");
        dto.setOcassion("Compensation");

        assertEquals("Reverser", dto.getInitiator());
        assertEquals("secure123", dto.getSecurityCredential());
        assertEquals("TransactionReversal", dto.getCommandID());
        assertEquals("TX123456", dto.getTransactionID());
        assertEquals("5000", dto.getAmount());
        assertEquals("600001", dto.getReceiverParty());
        assertEquals("4", dto.getRecieverIdentifierType());
        assertEquals("https://example.com/result", dto.getResultURL());
        assertEquals("https://example.com/timeout", dto.getQueueTimeOutURL());
        assertEquals("Duplicate transaction", dto.getRemarks());
        assertEquals("Compensation", dto.getOcassion());
    }
}

