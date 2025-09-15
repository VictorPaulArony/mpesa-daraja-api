package com.example.mpesaapi.service;

import com.example.mpesaapi.config.MpesaConfigProperties;
import com.example.mpesaapi.dto.ReversalDto;
import okhttp3.*;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.io.IOException;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class ReversalTest {

    private MpesaService mpesaService;
    private MpesaConfigProperties config;
    private OkHttpClient httpClient;
    private Reversal reversal;

    @BeforeEach
    void setUp() {
        mpesaService = mock(MpesaService.class);
        config = mock(MpesaConfigProperties.class);
        httpClient = mock(OkHttpClient.class);
        reversal = new Reversal(mpesaService, config);
    }

    @Test
    void testRequestReversalSuccess() throws IOException {
        // Arrange
        ReversalDto dto = new ReversalDto();
        dto.setInitiator("testInitiator");
        dto.setSecurityCredential("secure");
        dto.setCommandID("TransactionReversal");
        dto.setTransactionID("TX123");
        dto.setAmount("100");
        dto.setReceiverParty("600000");
        dto.setRecieverIdentifierType("11");
        dto.setQueueTimeOutURL("https://example.com/timeout");
        dto.setResultURL("https://example.com/result");
        dto.setRemarks("Test");
        dto.setOcassion("TestOccasion");

        when(config.getEnv()).thenReturn("sandbox");
        when(mpesaService.authenticate()).thenReturn("mocked-token");

        ResponseBody mockBody = mock(ResponseBody.class);
        when(mockBody.string()).thenReturn("Reversal Successful");

        Response response = new Response.Builder()
                .code(200)
                .message("OK")
                .protocol(Protocol.HTTP_1_1)
                .request(new Request.Builder().url("https://sandbox.safaricom.co.ke").build())
                .body(mockBody)
                .build();

        Call mockCall = mock(Call.class);
        when(httpClient.newCall(any(Request.class))).thenReturn(mockCall);
        when(mockCall.execute()).thenReturn(response);

        // Act
        String result = reversal.requestReversal(dto);

        // Assert
        assertEquals("Reversal Successful", result);
    }

    @Test
    void testRequestReversalFailsOnNullResponse() throws IOException {
        // Arrange
        ReversalDto dto = new ReversalDto();
        dto.setInitiator("test");
        dto.setSecurityCredential("secure");
        dto.setCommandID("cmd");
        dto.setTransactionID("TX123");
        dto.setAmount("100");
        dto.setReceiverParty("600000");
        dto.setRecieverIdentifierType("11");
        dto.setQueueTimeOutURL("https://timeout.url");
        dto.setResultURL("https://result.url");
        dto.setRemarks("remarks");
        dto.setOcassion("occasion");

        when(config.getEnv()).thenReturn("sandbox");
        when(mpesaService.authenticate()).thenReturn("token");

        Response response = new Response.Builder()
                .code(200)
                .message("OK")
                .request(new Request.Builder().url("https://sandbox.safaricom.co.ke").build())
                .protocol(Protocol.HTTP_1_1)
                .body(null) // null body
                .build();

        Call mockCall = mock(Call.class);
        when(httpClient.newCall(any(Request.class))).thenReturn(mockCall);
        when(mockCall.execute()).thenReturn(response);

        // Act & Assert
        IOException exception = assertThrows(IOException.class, () -> reversal.requestReversal(dto));
        assertEquals("Response body can not be null", exception.getMessage());
    }
}
